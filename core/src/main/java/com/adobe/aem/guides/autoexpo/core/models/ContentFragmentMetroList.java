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
public class ContentFragmentMetroList {
	public static final String METRO_NAME = "Metroname";

	@Inject
	@Self
	private Resource resource;

	private Optional<ContentFragment> contentFragment;

	@PostConstruct
	public void init() {
		contentFragment = Optional.ofNullable(resource.adaptTo(ContentFragment.class));
	}

	public String getMetroName() {
		return contentFragment.map(cf -> cf.getElement("metroName")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	public String getPageTitle() {
		return contentFragment.map(cf -> cf.getElement("pageTitle")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	public String getPageDesc() {
		return contentFragment.map(cf -> cf.getElement("pageDesc")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	public String getPageKeywords() {
		return contentFragment.map(cf -> cf.getElement("pageKeywords")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}
	
	public String getLocalisedContent() {
		return contentFragment.map(cf -> cf.getElement("localizedContent")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

}
