package com.adobe.aem.guides.autoexpo.core.models;

import com.adobe.aem.guides.autoexpo.core.configs.MyNYTimesApiConfiguration;

import java.util.List;

public interface MyNYTimesAPIManager {
    List<MyNYTimesApiConfiguration> getConfigurationList();
}
