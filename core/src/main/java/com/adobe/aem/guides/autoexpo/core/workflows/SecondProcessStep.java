package com.adobe.aem.guides.autoexpo.core.workflows;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

/**
 * @author Pavan Kumar
 */
@Component(service = WorkflowProcess.class, property = { "process.label=" + "Second Process Step" })
public class SecondProcessStep implements WorkflowProcess {

 private final Logger log = LoggerFactory.getLogger(this.getClass());

 @Override
 public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
   throws WorkflowException {

  log.info("Pavan Kumar Manda <<<<>>>> Executing second workflow process step");

  // Getting the metadata map
  MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();

  // Getting the data stored
  String data = (String) map.get("Data");

  log.info("Pavan Kumar Manda <<<<>>>> Data from the first step: {}", data);
 }

}