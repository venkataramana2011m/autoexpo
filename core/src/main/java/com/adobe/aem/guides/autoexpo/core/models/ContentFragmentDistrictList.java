package com.adobe.aem.guides.autoexpo.core.models;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentFragmentDistrictList {
	public static final String DISTRICT_NAME = "name";

	@Inject
	@Self
	private Resource resource;

	private Optional<ContentFragment> contentFragment;

	@PostConstruct
	public void init() {
		contentFragment = Optional.ofNullable(resource.adaptTo(ContentFragment.class));
	}

	public String getDistrictName() {
		return contentFragment.map(cf -> cf.getElement("txtDistrictName")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	public String getDistrictCode() {
		return contentFragment.map(cf -> cf.getElement("txtDistrictCode")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	public String getMultiAboutDistrict() {
		return contentFragment.map(cf -> cf.getElement("multiTxtAboutDistrict")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	

}
