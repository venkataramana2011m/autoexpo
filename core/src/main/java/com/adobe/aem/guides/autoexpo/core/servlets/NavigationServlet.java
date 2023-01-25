/*
 *  Copyright 2015 Adobe Systems Incorporated
 *  This Class is to have the Navigation for 
 *  the Auto Expo Project 
 *      
 */
package com.adobe.aem.guides.autoexpo.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.autoexpo.core.services.UtilityService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/autoexpo/navigation" })
public class NavigationServlet extends SlingSafeMethodsServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7310953259219639157L;

	private static final Logger log = LoggerFactory.getLogger(NavigationServlet.class);

	@Reference
	private QueryBuilder builder;

	private Session session;

	private ResourceResolver resourceResolver;

	public UtilityService utilityService;

	private HashSet<String> uniquePageList;

	private JSONObject NavigationJson;

	private JSONArray NavigationJsonArray;

	private PageManager pageManager;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		NavigationJson = new JSONObject();
		NavigationJsonArray = new JSONArray();
		try {
			resourceResolver = request.getResourceResolver();
			session = resourceResolver.adaptTo(Session.class);
			Map<String, String> predicate = new HashMap<>();
			predicate.put("path", "/content/autoexpo/us/en");
			predicate.put("1_property", "jcr:primaryType");
			predicate.put("1_property.value", "cq:Page");
			Query query = builder.createQuery(PredicateGroup.create(predicate), session);
			query.setStart(0);
			query.setHitsPerPage(50);
			uniquePageList = new HashSet<String>();
			pageManager = resourceResolver.adaptTo(PageManager.class);
			SearchResult searchResult = query.getResult();
			for (Hit hit : searchResult.getHits()) {
				Page currentPage = pageManager.getPage(hit.getPath());
				uniquePageList.add(currentPage.getParent().getPath().toString());
			}
			Iterator<String> itr = uniquePageList.iterator();
			while (itr.hasNext()) {
				Page currentPage = pageManager.getPage(itr.next());
				JSONObject pageList = new JSONObject();
				pageList.put("pageTitle", currentPage.getTitle().toString());
				pageList.put("pagePath", currentPage.getPath().toString());
				NavigationJsonArray.put(pageList);
			}
			NavigationJson.put("PageList", NavigationJsonArray);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(NavigationJson.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
}