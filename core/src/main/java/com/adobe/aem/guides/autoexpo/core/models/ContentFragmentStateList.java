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
public class ContentFragmentStateList {	

	public static final String MODEL_TITLE = "Statename";

	@Inject
	@Self
	private Resource resource;

	private Optional<ContentFragment> contentFragment;

	@PostConstruct
	public void init() {
		contentFragment = Optional.ofNullable(resource.adaptTo(ContentFragment.class));
	}

	public String getStateCode() {
		return contentFragment.map(cf -> cf.getElement("stateCode")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}
	
	public String getStateName() {
		return contentFragment.map(cf -> cf.getElement("stateName")).map(ContentElement::getContent)
				.orElse(StringUtils.EMPTY);
	}

	

	public List<ContentFragmentMetroList> getMetroList() {
		return Arrays
				.asList((String[]) contentFragment.map(cf -> cf.getElement("metroList")).map(ContentElement::getValue)
						.map(FragmentData::getValue).orElse(new String[0]))
				.stream().map(metroListPath -> resource.getResourceResolver().resolve(metroListPath))
				.filter(Objects::nonNull)
				.map(metroListResource -> metroListResource.adaptTo(ContentFragmentMetroList.class))
				.collect(Collectors.toList());
	}

}
