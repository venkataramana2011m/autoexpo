package com.adobe.aem.guides.autoexpo.core.models;

import java.util.List;
import java.util.Map;

public interface ProductListWithMap {	
	String getProductListingTitle();
    List<Map<String,String>> getProductDetailsWithMap();
}
