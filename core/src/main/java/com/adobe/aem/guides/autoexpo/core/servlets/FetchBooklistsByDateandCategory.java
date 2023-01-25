package com.adobe.aem.guides.autoexpo.core.servlets;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import java.io.IOException;
import javax.servlet.Servlet;
import java.io.BufferedReader;
import org.slf4j.LoggerFactory;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.osgi.framework.Constants;
import javax.servlet.ServletException;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import com.adobe.aem.guides.autoexpo.core.constants.AppConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Book Listing servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/autoexpo/fetchbooks" })
public class FetchBooklistsByDateandCategory extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = -7008481344149026663L;
	private static final Logger log = LoggerFactory.getLogger(FetchBooklistsByDateandCategory.class);
	private static final AppConstants appConstants = new AppConstants();
	private HashSet<String> uniqueBookList;

	@SuppressWarnings("static-access")
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer jsonResponseData = new StringBuffer();
		String readLine = null;
		log.error("----------< Executing Book Listing Builder Servlet >----------");
		try {
			uniqueBookList = new HashSet<String>();
			String selDate = request.getParameter("date");
			String selCategory = request.getParameter("list");
			System.out.println("selected date :::" + selDate);
			System.out.println("selected Category :::" + selCategory);
			URL getUrl = new URL(appConstants.TNYTIMES_ENDPOINT.toString() + appConstants.TNYTIMES_BOOKS_LIST.toString()
					+ selDate.toString() + appConstants.TNYTIMES_BOOKS_LIST_DATE_FRWD_SLASH.toString()
					+ selCategory.toString() + appConstants.TNYTIMES_BOOKS_LIST_DATE_EXTENSION.toString()
					+ appConstants.TNYTIMES_QUERYPARAMETER.toString()
					+ appConstants.TNYTIMES_API_KEY_PARAMETER_KEY.toString()
					+ appConstants.TNYTIMES_API_KEY_PARAMETER_VALUE.toString());
	
			
			HttpURLConnection conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				while ((readLine = in.readLine()) != null) {
					uniqueBookList.add(readLine);
				}
				in.close();
				Iterator<String> itr = uniqueBookList.iterator();
				while (itr.hasNext()) {
					jsonResponseData.append(itr.next());
				}
				log.error("JSON String Data " + jsonResponseData.toString());
			} else {
				log.error("Response Code :: " + responseCode);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonResponseData.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
