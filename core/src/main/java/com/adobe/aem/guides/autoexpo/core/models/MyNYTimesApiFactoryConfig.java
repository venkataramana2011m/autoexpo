package com.adobe.aem.guides.autoexpo.core.models;

import com.adobe.aem.guides.autoexpo.core.configs.MyNYTimesApiConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = MyNYTimesApiFactoryConfig.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = MyNYTimesApiConfiguration.class, factory = true)
public class MyNYTimesApiFactoryConfig {
    protected static final String SERVICE_NAME = "Newyork Times Service";
    private static final String TAG = MyNYTimesApiFactoryConfig.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(MyNYTimesApiFactoryConfig.class);

    private String NyTimesEndPoint;
    private String NyTimesApiId;
    private String NyTimesAPIKey;
    private String NyTimesSecretKey;
    private String NyTimesBooklistAPI;
    private String NyTimesBooksByCategory;
    public String getNYTimesEndPoint() { return NyTimesEndPoint;}
    public String getNYTimesApiId() {return NyTimesApiId;}
    public String getNYTimesAPIKey() {return NyTimesAPIKey;}
    public String getNYTimesSecretKey() {return NyTimesSecretKey;}
    public String getNYTimesBooklistAPI() {return NyTimesBooklistAPI;}
    public String getNYTimesBooksByCategory() {return NyTimesBooksByCategory;}
    @Activate
    @Modified
    protected void activate(MyNYTimesApiConfiguration config) {
        NyTimesEndPoint = config.nytimesEndPoint();
        NyTimesApiId = config.nytimesApiId();
        NyTimesAPIKey = config.nytimesAPIKey();
        NyTimesSecretKey = config.nytimesSecretKey();
        NyTimesBooklistAPI = config.nytimesBooksListEndPoint();
        NyTimesBooksByCategory = config.nytimesBooksListByCategoryEndPoint();
    }
}