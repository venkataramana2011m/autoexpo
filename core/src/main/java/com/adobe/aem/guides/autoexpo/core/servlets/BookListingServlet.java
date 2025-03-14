package com.adobe.aem.guides.autoexpo.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.autoexpo.core.constants.AppConstants;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Book Listing servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/autoexpo/booklisting" })
public class BookListingServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = -7008481344149026663L;
	private static final Logger log = LoggerFactory.getLogger(BookListingServlet.class);
	private static final AppConstants appConstants = new AppConstants();
	private static final String BRAND_PAGE_TEMPLATE = "/conf/autoexpo/settings/wcm/templates/page-content";
	private static final String RENDERER = "autoexpo/components/page";
	@Reference
	private ResourceResolverFactory resolverFactory;
	private String pageName = null;
	private String pageTitle;
	private String path = "/content/autoexpo/us/en/new-cars";
	private Page prodPage = null;
	@Reference
	private QueryBuilder builder;
	private HashSet<String> uniqueBookList;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer jsonResponseData = new StringBuffer();
		com.adobe.aem.guides.autoexpo.core.servlets.BookListingServlet bookListingServlet = new BookListingServlet();
		String readLine = null;
		log.error("----------< Executing Book Listing Builder Servlet >----------");
		try {
			bookListingServlet.uniqueBookList = new HashSet<String>();
			URL getUrl = new URL(
					appConstants.TNYTIMES_ENDPOINT.toString() + appConstants.TNYTIMES_BOOKS_CATEGORIES_API.toString()
							+ appConstants.TNYTIMES_QUERYPARAMETER.toString()
							+ appConstants.TNYTIMES_API_KEY_PARAMETER_KEY.toString()
							+ appConstants.TNYTIMES_API_KEY_PARAMETER_VALUE.toString());
			HttpURLConnection conection = (HttpURLConnection) getUrl.openConnection();
			conection.setRequestMethod("GET");
			int responseCode = conection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				while ((readLine = in.readLine()) != null) {
					bookListingServlet.uniqueBookList.add(readLine);
				}
				in.close();
				Iterator<String> itr = bookListingServlet.uniqueBookList.iterator();
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
