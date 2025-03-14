package com.adobe.aem.guides.autoexpo.core.servlets;

import com.day.cq.search.QueryBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Book Detail Servlet",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/autoexpo/articlelisting",
                ServletResolverConstants.DEFAULT_RESOURCE_TYPE + "=sling/servlet/default",
                ServletResolverConstants.SLING_SERVLET_SELECTORS + "=bookDetailSelector"
        })
public class ArticleListingServlet extends SlingSafeMethodsServlet {
    @Reference
    protected ResourceResolverFactory resolverFactory;
    private static final Logger log = LoggerFactory.getLogger(ArticleListingServlet.class);
    private ResourceResolver resourceResolver;
    private Session session;
    @Reference
    private QueryBuilder builder;
    @Override
    protected void doGet(@org.jetbrains.annotations.NotNull SlingHttpServletRequest request, @org.jetbrains.annotations.NotNull SlingHttpServletResponse response) throws ServletException, IOException {
        try{
            log.info("Inside the doGet Method..");
        } catch (Exception e){
            log.error(e.getMessage());
        }
        super.doGet(request, response);
    }
}
