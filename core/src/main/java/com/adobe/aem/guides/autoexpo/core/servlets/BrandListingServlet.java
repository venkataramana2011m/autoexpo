package com.adobe.aem.guides.autoexpo.core.servlets;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
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

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Brand Listing servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/autoexpo/brandlisting" })
public class BrandListingServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972237991347014852L;

	private static final Logger log = LoggerFactory.getLogger(BrandListingServlet.class);
	@Reference
	private QueryBuilder builder;
	private Session session;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		JSONObject damAssetsJson = new JSONObject();
		try {
			JSONArray damAssetsJsonArray = new JSONArray();
			log.info("----------< Executing Query Builder Servlet >----------");
			ResourceResolver resourceResolver = request.getResourceResolver();
			session = resourceResolver.adaptTo(Session.class);
			Map<String, String> predicate = new HashMap<>();
			predicate.put("path", "/content/dam/autoexpo/brandlogos");
			predicate.put("1_property", "jcr:primaryType");
			predicate.put("1_property.value", "dam:Asset");
			predicate.put("2_property", "jcr:content/metadata/dc:format");
			predicate.put("2_property.1_value", "image/jpeg");
			predicate.put("2_property.2_value", "image/jpg");
			predicate.put("2_property.3_value", "image/png");
			Query query = builder.createQuery(PredicateGroup.create(predicate), session);
			query.setStart(0);
			query.setHitsPerPage(500);

			SearchResult searchResult = query.getResult();
			for (Hit hit : searchResult.getHits()) {
				JSONObject damAsset = new JSONObject();

				damAsset.put("imgPath", hit.getPath().toString());
				damAsset.put("title", getFileNameWithoutExtension(hit.getTitle().toString()));
				damAsset.put("pagePath",
						getPagePath(getFileNameWithoutExtension(hit.getTitle().toString()), resourceResolver));
				damAssetsJsonArray.put(damAsset);
			}
			damAssetsJson.put("DamAssetList", damAssetsJsonArray);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// Set JSON in String
			response.getWriter().write(damAssetsJson.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (session != null) {
				session.logout();
			}
		}

	}

	public String getFileNameWithoutExtension(String fileName) {
		String deleteExtension = fileName.substring(0, fileName.lastIndexOf("."));
		return deleteExtension;

	}

	public String getPagePath(String pageName, ResourceResolver resourceResolver) {
		String trgtPagePath=StringUtils.EMPTY;
		try {
			Map<String, String> predicate = new HashMap<>();
			predicate.put("path", "/content/autoexpo/us/en");
			predicate.put("1_property", "jcr:primaryType");
			predicate.put("1_property.value", "cq:Page");
			Query query = builder.createQuery(PredicateGroup.create(predicate), session);
			query.setStart(0);
			query.setHitsPerPage(50);
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			pageManager = resourceResolver.adaptTo(PageManager.class);
			SearchResult searchResult = query.getResult();
			for (Hit hit : searchResult.getHits()) {
				Page currentPage = pageManager.getPage(hit.getPath());
				if(currentPage.getTitle().equalsIgnoreCase(pageName)) {
					trgtPagePath = currentPage.getPath()+".html";
					log.error("With in the Create Page method ...... !!!! "+trgtPagePath);
					
				}

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
		return trgtPagePath;

	}

}
