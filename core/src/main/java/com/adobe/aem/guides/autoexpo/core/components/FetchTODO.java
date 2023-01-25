package com.adobe.aem.guides.autoexpo.core.components;

import static com.adobe.aem.guides.autoexpo.core.components.FetchTODO.COMPONENT_NAME;
import static com.adobe.aem.guides.autoexpo.core.constants.AppConstants.TODO_ENDPOINT;
import static com.adobe.aem.guides.autoexpo.core.constants.AppConstants.TODO_THREAD_SLEEP_TIME;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.autoexpo.core.services.WriteTODOService;

@Component(immediate = true, name = COMPONENT_NAME)
public class FetchTODO {
	private static final String TAG = FetchTODO.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchTODO.class);
    protected static final String COMPONENT_NAME = "Fetch Todo Component";

    @Reference
    WriteTODOService writeTODOService;
    
    @Activate
    protected void activate() {
        LOGGER.info("{}: {} - activated", TAG, COMPONENT_NAME);
        Runnable task = () -> {
            try {
                Thread.sleep(TODO_THREAD_SLEEP_TIME);
                while (!Thread.currentThread().isInterrupted()) {
                    // Get output from the API
                    String todoData = fetchData();
                    // Call the OSGi service which will write data into the repository
                    writeTODOService.writeData(todoData);
                    LOGGER.info("{}: Trying to write TODO data in repository at: {}", TAG, LocalDateTime.now());
                }
            } catch (InterruptedException e) {
                LOGGER.error("{}: Exception occurred: {}", TAG, e.getMessage());
            }
        };
        // Create a new Thread with the above Runnable
        Thread todoThread = new Thread(task);
        // Set the name of the thread
        todoThread.setName("TODO Thread");
        // Start the thread
        todoThread.start();
    }

    private String fetchData() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        // Output in String format
        String output = StringUtils.EMPTY;
        try {
            // GET Request
            HttpGet request = new HttpGet(TODO_ENDPOINT);
            // Set the API media type in http accept header
            request.setHeader("accept", "application/json");
            // Send the request, It will immediately return the response in HttpResponse object
            HttpResponse response = httpClient.execute(request);
            // Get the status code
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException(TAG + ": Failed with error code: " + statusCode);
            }
            // Pull back the response object
            HttpEntity httpEntity = response.getEntity();
            // Getting output in String
            output = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            LOGGER.error("{}: Exception occurred: {}", TAG, e.getMessage());
        }
        return output;
    }
}
