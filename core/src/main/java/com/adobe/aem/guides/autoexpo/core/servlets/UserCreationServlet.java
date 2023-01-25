package com.adobe.aem.guides.autoexpo.core.servlets;

import java.io.IOException;
import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.jcr.Value;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Create AEM User servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/autoexpo/usercreate" })
public class UserCreationServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = -5768178637700426689L;
	private static final Logger log = LoggerFactory.getLogger(UserCreationServlet.class);
	@Reference
	ResourceResolverFactory resolverFactory;

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String userName = "kumarmanda";
		String password = "Welcome@123";
		String firstname = "Manda";
		log.error("With in the Create User method ...... !!!! ");
		try {
			ResourceResolver resolver = request.getResourceResolver();
			Session session = request.getResourceResolver().adaptTo(Session.class);
			UserManager userManager = resolver.adaptTo(UserManager.class);
			User user = userManager.createUser(userName, password);
			if (!userManager.isAutoSave()) {
				log.info("User created successfully..");
				Value fnamevalue = session.getValueFactory().createValue(firstname, PropertyType.STRING);
				user.setProperty("./profile/familyName", fnamevalue);
				session.save();
			}		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
