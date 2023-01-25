package com.adobe.aem.guides.autoexpo.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
 
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
 
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
 
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Invoke Workflow Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/invoke/workflow" })
public class InvokeWorkflowServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 6508244607422006788L;
	protected final transient Logger log = LoggerFactory.getLogger(this.getClass()); 
    @Reference
    private transient WorkflowService workflowService; 
    @Reference
    private transient ResourceResolverFactory resolverFactory;
    
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		Session session = null;
        try {
        	
        
        
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "writeSystemUser");
        ResourceResolver resolver = null;
         
         
        WorkflowSession wfSession = workflowService.getWorkflowSession(session);
        WorkflowModel wfModel = wfSession.getModel("/var/workflow/models/dam/dam_download_asset");
        WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", "/content/dam/we-retail/en/features/cart.png");
        wfSession.startWorkflow(wfModel, wfData);
        session.save();
        session.logout();
        
        }catch (Exception e) {
			// TODO: handle exception
		}
        
	}
}
