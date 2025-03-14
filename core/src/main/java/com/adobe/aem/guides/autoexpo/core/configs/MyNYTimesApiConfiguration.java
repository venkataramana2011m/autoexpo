package com.adobe.aem.guides.autoexpo.core.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Newyork Times Osgi Configuration",
        description = "This configuration captures the Newyork Times API configuration details"
)
public @interface MyNYTimesApiConfiguration {

    @AttributeDefinition(
            name = "NYTimes Endpoint",
            description = "Enter the URL for the Newyork Times API Endpoint"
    )
    String nytimesEndPoint() default "https://api.nytimes.com/svc/";

    @AttributeDefinition(
            name = "NYTimes ApiID",
            description = "Enter Api Id Newyork Times API"
    )
    String nytimesApiId() default "8fa5985b-0a4b-4b12-a773-58bf5f2d1280";

    @AttributeDefinition(
            name = "NYTimes API Key",
            description = "Enter API-Key Newyork Times API"
    )
    String nytimesAPIKey() default "y5GQJ5ljcgfpEZ4TpGRMnWb7RabGFPUB";

    @AttributeDefinition(
            name = "NYTimes Secret Key",
            description = "Enter Secret Key Newyork Times API"
    )
    String nytimesSecretKey() default "AVE7orBih1FoFfsN";

    @AttributeDefinition(
            name = "NYTimes Books List By Category Endpoint",
            description = "Enter the URL for the Newyork Times API Endpoint"
    )
    String nytimesBooksListByCategoryEndPoint() default "books/v3/lists/names.json";

    @AttributeDefinition(
            name = "NYTimes Books List Endpoint",
            description = "Enter the URL for the Newyork Times API Endpoint"
    )
    String nytimesBooksListEndPoint() default "books/v3/lists.json";
}