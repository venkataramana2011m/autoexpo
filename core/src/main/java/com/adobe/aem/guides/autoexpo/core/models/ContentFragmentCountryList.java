package com.adobe.aem.guides.autoexpo.core.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentFragmentCountryList {

	public static final String MODEL_TITLE = "Countryname";

	@Inject
	@Self
	private Resource resource;

	private Optional<ContentFragment> contentFragment;

	@PostConstruct
	public void init() {
		contentFragment = Optional.ofNullable(resource.adaptTo(ContentFragment.class));
	}

	public String getCountryCode() {
		return contentFragment.map(cf -> cf.getElement("txtCountryCode")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}
	
	public String getCountryName() {
		return contentFragment.map(cf -> cf.getElement("txtCountryName")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}
	public String getCountryMap() {
		return contentFragment.map(cf -> cf.getElement("countryMap")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	

	public List<ContentFragmentStateListModel> getMetroList() {
		return Arrays
				.asList((String[]) contentFragment.map(cf -> cf.getElement("multiStateList")).map(ContentElement::getValue)
						.map(FragmentData::getValue).orElse(new String[0]))
				.stream().map(multiStateListPath -> resource.getResourceResolver().resolve(multiStateListPath))
				.filter(Objects::nonNull)
				.map(multiStateListResource -> multiStateListResource.adaptTo(ContentFragmentStateListModel.class))
				.collect(Collectors.toList());
	}

}
