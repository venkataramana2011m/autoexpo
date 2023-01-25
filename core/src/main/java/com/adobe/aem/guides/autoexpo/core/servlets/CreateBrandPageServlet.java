package com.adobe.aem.guides.autoexpo.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Brand Child Pages servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/autoexpo/createbrandpages" })
public class CreateBrandPageServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 3690376704642221914L;
	private static final String BRAND_PAGE_TEMPLATE = "/conf/autoexpo/settings/wcm/templates/page-content";
	private static final String RENDERER = "autoexpo/components/page";
	@Reference
	private ResourceResolverFactory resolverFactory;
	private String pageName;
	private String pageTitle;
	private String path = "/content/autoexpo/us/en/new-cars";
	private Page prodPage = null;
	private Session session;
	@Reference
	private QueryBuilder builder;
	private static final Logger log = LoggerFactory.getLogger(CreateBrandPageServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		try {
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
				log.error("Fetching Images using Query Builder ...... !!!! ");
				pageName = getFileNameWithoutExtension(hit.getTitle().toLowerCase());
				log.error("Fetching Page Name using Query Builder ...... !!!! " + (pageName));
				pageTitle = getFileNameWithoutExtension(hit.getTitle().toUpperCase());
				log.error("Fetching Page Name using Query Builder ...... !!!! " + (pageTitle));
				createPages(pageName, pageTitle, resourceResolver);
			}
			session.save();
			session.refresh(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void createPages(String pageName, String pageTitle, ResourceResolver resourceResolver) {
		try {
			log.error("With in the Create Page method ...... !!!! ");
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			log.error("With in the Create Page method ...... !!!! ");
			prodPage = pageManager.create(path, pageName, BRAND_PAGE_TEMPLATE, pageTitle, true);
			Resource resource = resourceResolver.getResource(prodPage.getPath());
			Node pageNode = resource.adaptTo(Node.class);
			Node jcrNode = null;
			if (prodPage.hasContent()) {
				jcrNode = prodPage.getContentResource().adaptTo(Node.class);
			} else {
				jcrNode = pageNode.addNode("jcr:content", "cq:PageContent");
			}
			jcrNode.setProperty("sling:resourceType", RENDERER);
			Node root = session.getNode(prodPage.getPath().toString() + "/jcr:content/root/responsivegrid");
			Node day = root.addNode("biographycomponent", "nt:unstructured");
			day.setProperty("sling:resourceType", "autoexpo/components/biographycomponent");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getFileNameWithoutExtension(String fileName) {
		String deleteExtension = fileName.substring(0, fileName.lastIndexOf("."));
		return deleteExtension;

	}
}
