package com.adobe.aem.guides.autoexpo.core.servlets;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.autoexpo.core.constants.AppConstants;
import com.adobe.aem.guides.autoexpo.core.models.ContentFragmentCountryList;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Content Fragement POC servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/autoexpo/cfpoc",
		"sling.servlet.extensions=" + "json" })
@SuppressWarnings("unused")
public class MyCFPOCCountryServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -3416511514478145079L;
	private static final Logger log = LoggerFactory.getLogger(MyCFPOCCountryServlet.class);
	private static final List<String> reservedParams = Arrays.asList("search");
	private AppConstants appConstants = new AppConstants();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		final QueryBuilder queryBuilder = request.getResourceResolver().adaptTo(QueryBuilder.class);

		final Map<String, String> map = new HashMap<String, String>();
		map.put("type", "dam:Asset");
		map.put("path", "/content/dam");
		map.put("boolproperty", "jcr:content/contentFragment");
		map.put("boolproperty.value", "true");
		map.put("property", "jcr:content/data/cq:model");
		map.put("property.value", "/conf/autoexpo/settings/dam/cfm/models/countrylist");
		final String search = request.getParameter("search");
		if (StringUtils.isNotEmpty(search)) {
			map.put("fulltext", search);
			map.put("fulltext.relPath", "jcr:content/data/master");
		}

		int paramCount = 1;
		for (final String key : request.getParameterMap().keySet()) {
			paramCount++;
			if (!reservedParams.contains(key)) {
				map.put(paramCount + "_property", "jcr:content/data/master/" + key);
				map.put(paramCount + "_property.value", request.getParameter(key));
			}
		}

		final Query query = queryBuilder.createQuery(PredicateGroup.create(map),
				request.getResourceResolver().adaptTo(Session.class));
		final SearchResult result = query.getResult();
		final ObjectMapper objectMapper = new ObjectMapper();
		final List<ContentFragmentCountryList> test = result.getHits().stream().map(hit -> {
			try {
				return request.getResourceResolver().resolve(hit.getPath()).adaptTo(ContentFragmentCountryList.class);
			} catch (RepositoryException e) {
				log.error("Error collecting search results", e);
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		
		response.setContentType("application/json");
		try {
			response.getWriter().write(objectMapper.writeValueAsString(test));
		} catch (JsonProcessingException e) {
			response.getWriter().write("{ \"error\": \"Could not write movies as JSON\" }");
		}
	}
}
