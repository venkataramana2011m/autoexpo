package com.adobe.aem.guides.autoexpo.core.models.impl;

import com.adobe.aem.guides.autoexpo.core.configs.MyNYTimesApiConfiguration;
import com.adobe.aem.guides.autoexpo.core.models.MyNYTimesAPIManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, service = MyNYTimesAPIManager.class, enabled = true)
public class MyNYTimesAPIManagerImpl implements MyNYTimesAPIManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyNYTimesAPIManagerImpl.class);
    private List<MyNYTimesApiConfiguration> configList;
    @Reference(name = "Configuration Factory", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected synchronized void bindConfigurationFactory(final MyNYTimesApiConfiguration config){
        if(configList == null){
            configList = new ArrayList<>();
        }
        configList.add(config);
    }
    protected synchronized void unbindConfigurationFactory(final MyNYTimesApiConfiguration config){
        configList.remove(config);
    }

    @Override
    public List<MyNYTimesApiConfiguration> getConfigurationList() {
        return configList;
    }
    public void setConfigurationList(List<MyNYTimesApiConfiguration> configurationList){
        this.configList = configurationList;
    }


}
