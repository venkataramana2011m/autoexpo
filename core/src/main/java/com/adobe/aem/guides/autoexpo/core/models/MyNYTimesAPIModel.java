package com.adobe.aem.guides.autoexpo.core.models;

import com.adobe.aem.guides.autoexpo.core.configs.MyNYTimesApiConfiguration;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class MyNYTimesAPIModel {
    @Inject
    MyNYTimesApiConfiguration myNYTimesApiConfiguration;

    private String NYTendPoint;
    private String NYTapiId;
    private String NYTaPIKey;
    private String NYTsecretKey;
    private String NYTbooksListByCategoryEndPoint;
    private String NYTbooksListEndPoint;


    @PostConstruct
    private void init(){
        NYTendPoint =  myNYTimesApiConfiguration.nytimesEndPoint();
        NYTapiId = myNYTimesApiConfiguration.nytimesApiId();
        NYTaPIKey = myNYTimesApiConfiguration.nytimesAPIKey();
        NYTsecretKey = myNYTimesApiConfiguration.nytimesSecretKey();
        NYTbooksListByCategoryEndPoint=myNYTimesApiConfiguration.nytimesBooksListByCategoryEndPoint();
        NYTbooksListEndPoint=myNYTimesApiConfiguration.nytimesBooksListEndPoint();
    }

    public String getNYTBooksListEndPoint() {
        return NYTbooksListEndPoint;
    }

    public String getNYTBooksListByCategoryEndPoint() {
        return NYTbooksListByCategoryEndPoint;
    }

    public String getNYTSecretKey() {
        return NYTsecretKey;
    }

    public String getNYTAPIKey() {
        return NYTaPIKey;
    }

    public String getNYTApiId() {
        return NYTapiId;
    }

    public String getNYTEndPoint() {
        return NYTendPoint;
    }
}
