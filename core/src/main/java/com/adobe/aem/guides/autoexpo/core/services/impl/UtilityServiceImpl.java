package com.adobe.aem.guides.autoexpo.core.services.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.jcr.Session;

import com.adobe.aem.guides.autoexpo.core.beans.BookListParamBean;
import com.adobe.aem.guides.autoexpo.core.constants.AppConstants;
import com.adobe.aem.guides.autoexpo.core.services.UtilityService;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONArray;
import org.json.JSONObject;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.net.InternetDomainName;

public class UtilityServiceImpl implements UtilityService {

	private HttpClient client;
	private String domainName;
	private static final AppConstants appConstants = new AppConstants();

	@Override
	public Map<String, String> getNavigationPredicate() {
		Map<String, String> predicate = new HashMap<>();
		predicate.put("path", "/content/autoexpo/us/en");
		predicate.put("1_property", "jcr:primaryType");
		predicate.put("1_property.value", "cq:Page");
		return predicate;
	}

	@Override
	public String getNavigationListUsingQuery(QueryBuilder builder, Session session, Map<String, String> NavPredicate,
			ResourceResolver resourceResolver) {

		JSONObject NavigationJson = new JSONObject();
		JSONArray NavigationJsonArray = new JSONArray();
		try {
			Query query = builder.createQuery(PredicateGroup.create(NavPredicate), session);
			query.setStart(0);
			query.setHitsPerPage(50);
			HashSet<String> uniquePageList = new HashSet<String>();
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return NavigationJson.toString();
	}

	@Override
	public String getFileNameWithoutExtension(String fileName) {
		String deleteExtension = fileName.substring(0, fileName.lastIndexOf("."));
		return deleteExtension;
	}

	@Override
	public JSONObject getBooksList(String selectedCategory, String selectedDate, ResourceResolver resourceResolver,
			BookListParamBean bookListParamBean) {
		// TODO Auto-generated method stub
		String weserviceServerURL = StringUtils.EMPTY;
		JSONObject booksJson = null;
		String selDate;
		String selCategory;
		try {
			client = HttpClients.createDefault();
			selDate = selectedDate;
			selCategory = selectedCategory;
			weserviceServerURL = domainName.concat(getDomainPath(selDate, selCategory));
            URIBuilder builder = new URIBuilder(weserviceServerURL);
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("x-apikey", appConstants.TNYTIMES_API_KEY_PARAMETER_VALUE.toString());
            httpGet.setHeader("accept", "application/json");
            HttpResponse httpResponse = client.execute(httpGet);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            booksJson = new JSONObject(responseString);
            
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public String getDomainPath(String selDate, String selCategory) {
		String trgtdomainPath = StringUtils.EMPTY;
		trgtdomainPath = appConstants.TNYTIMES_ENDPOINT.toString() + appConstants.TNYTIMES_BOOKS_LIST.toString()
				+ selDate.toString() + appConstants.TNYTIMES_BOOKS_LIST_DATE_FRWD_SLASH.toString()
				+ selCategory.toString() + appConstants.TNYTIMES_BOOKS_LIST_DATE_EXTENSION.toString()
				+ appConstants.TNYTIMES_QUERYPARAMETER.toString()
				+ appConstants.TNYTIMES_API_KEY_PARAMETER_KEY.toString()
				+ appConstants.TNYTIMES_API_KEY_PARAMETER_VALUE.toString();

		return trgtdomainPath;
	}

}
