package org.redquark.demo.core.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

/**
 * @author Anirudh Sharma
 */
@Component(
		service = Servlet.class, 
		property = { 
				Constants.SERVICE_DESCRIPTION + "=Trigger Worklow Servlet",
				"sling.servlet.methods=" + HttpConstants.METHOD_GET, 
				"sling.servlet.paths=" + "/bin/triggerWorkflow" 
				}
		)
public class TriggerWorkflowServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4235730140092282985L;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		try {
			final String payloadPath = "/content/we-retail/language-masters/en/equipment";

			// Getting the resource resolver
			final ResourceResolver resolver = request.getResourceResolver();

			// Get the workflow session from the resource resolver
			final WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class);

			// Workflow model path - This is the already created workflow
			final String model = "/var/workflow/models/metadata_map_example";

			// Get the workflow model object
			final WorkflowModel workflowModel = workflowSession.getModel(model);

			// Create a workflow Data (or Payload) object pointing to a resource via JCR
			// Path (alternatively, a JCR_UUID can be used)
			final WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", payloadPath);

			// Optionally pass in workflow metadata via a Map
			final Map<String, Object> workflowMetadata = new HashMap<>();
			workflowMetadata.put("anyData", "You Want");
			workflowMetadata.put("evenThingsLike", new Date());

			// Start the workflow!
			workflowSession.startWorkflow(workflowModel, workflowData, workflowMetadata);

			log.info("Workflow: {} started", model);
			
			response.getWriter().println("Workflow Exxecuted");

		} catch (WorkflowException | IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
