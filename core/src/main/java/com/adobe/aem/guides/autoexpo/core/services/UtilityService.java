package com.adobe.aem.guides.autoexpo.core.services;

import java.util.Map;

import javax.jcr.Session;

import com.adobe.aem.guides.autoexpo.core.beans.BookListParamBean;
import com.day.cq.search.QueryBuilder;

import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONObject;

public interface UtilityService {
	public String getFileNameWithoutExtension(String fileName);
    public Map<String, String> getNavigationPredicate();
    public String getNavigationListUsingQuery(QueryBuilder builder, Session session,  Map<String, String> NavPredicate, ResourceResolver resourceResolver);
    public JSONObject getBooksList(String selectedCategory, String selectedDate, ResourceResolver resourceResolver, BookListParamBean bookListParamBean);
}
