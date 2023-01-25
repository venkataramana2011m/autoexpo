package com.adobe.aem.guides.autoexpo.core.models.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.autoexpo.core.models.ProductListWithMap;


@Model(adaptables = SlingHttpServletRequest.class, adapters = ProductListWithMap.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListWithMapImpl implements ProductListWithMap {
	private static final Logger LOG = LoggerFactory.getLogger(ProductListWithMapImpl.class);

	@SlingObject
	Resource componentResource;

	@ValueMapValue
	@Default(values = "Product Listing")
	private String productListingTitle;

	@Override
	public String getProductListingTitle() {
		return productListingTitle;
	}

	@Override
	public List<Map<String, String>> getProductDetailsWithMap() {
		List<Map<String, String>> productDetailsMap = new ArrayList<>();
		try {
			if (componentResource != null) {
				LOG.info("\n Book Details with Path {}  ", componentResource.getPath());
				Resource productDetail = componentResource.getChild("productDetailsWithMap");
				if (productDetail != null) {
					for (Resource book : productDetail.getChildren()) {
						Map<String, String> productMap = new HashMap<>();
						productMap.put("productName", book.getValueMap().get("productName", String.class));
						productMap.put("productImage", book.getValueMap().get("productImage", String.class));
						productMap.put("productPrice", book.getValueMap().get("productPrice", String.class));
						productMap.put("productQuantity", book.getValueMap().get("productQuantity", String.class));
						productMap.put("productPath", book.getValueMap().get("productPath", String.class));
						productMap.put("productAvailability",
								book.getValueMap().get("productAvailability", String.class));
						productDetailsMap.add(productMap);
					}
				}
			}
		} catch (Exception e) {
			LOG.info("\n ERROR while getting Book Details {} ", e.getMessage());
		}
		LOG.info("\n SIZE {} ", productDetailsMap.size());
		return productDetailsMap;
	}

}
