package com.adobe.aem.guides.autoexpo.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Query Builder servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/autoexpo/articlepagelisting" })
public class ArticlePageListingServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -3919358571734157283L;
	@Reference
	private ResourceResolverFactory resolverFactory;
	private static final Logger log = LoggerFactory.getLogger(ArticlePageListingServlet.class);
	@Reference
	private QueryBuilder builder;
	private Session session;
	private ResourceResolver resourceResolver;
	private HashSet<String> uniquePageList, uniqueChildPageList;
	private PageManager pageManager;
	private JSONObject leadershipJourneyJson, childPagesListJourneyJson;
	private JSONArray leadershipJsonArray, childPageJsonArray;
	protected ValueMap properties;
	String result = "";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		leadershipJourneyJson = new JSONObject();
		try {
			leadershipJsonArray = new JSONArray();
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
				JSONObject damAsset = new JSONObject();				
				damAsset.put("pageTitle", currentPage.getTitle().toString());
				damAsset.put("pagePath", currentPage.getPath().toString());
				
				String currentPagepath = currentPage.getParent().getPath().toString();
				Resource resource = resourceResolver.getResource(currentPagepath);
				
				if (!(currentPagepath).equalsIgnoreCase("/content/autoexpo/us/en")
						|| !(currentPagepath).equalsIgnoreCase("/content/autoexpo/us")
						|| !(currentPagepath).equalsIgnoreCase("/content/autoexpo")) {
					damAsset.put("test", "This is for testing");
					leadershipJsonArray.put(damAsset);	
					if(resource!=null) {
						Page parentPage = resource.adaptTo(Page.class);
						Iterator<Page> listChildPages = parentPage.listChildren();
						while (listChildPages.hasNext()) {
							
						}
					}
				}else {
					leadershipJsonArray.put(damAsset);
				}
				//getChildPages(resourceResolver, currentPage.getPath().toString());
			}
			leadershipJourneyJson.put("ParentPageList", leadershipJsonArray);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(leadershipJourneyJson.toString());

		} catch (RepositoryException e) {
			throw new IllegalStateException("Failed to retrieve the Jcr Session", e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}

	}

	@SuppressWarnings("unused")
	private void getChildPages(ResourceResolver resourceResolver, String path) {
		try {
			childPagesListJourneyJson = new JSONObject();
			childPageJsonArray = new JSONArray();
			Resource resource = resourceResolver.getResource(path);
			Page parentPage = resource.adaptTo(Page.class);
			if (resource != null) {
				System.out.println("resource is not null ..... " + path);				
				boolean hasChildren = parentPage.getContentResource().adaptTo(Node.class).hasNodes();
				System.out.println(hasChildren);
			} else {
				System.out.println("resource is null ..... " + path);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

}
