package com.adobe.aem.guides.autoexpo.core.workflows;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.metadata.MetaDataMap;

/**
 * @author Manda Pavan Kumar
 */
@Component(service = ParticipantStepChooser.class, property = { "chooser.label=" + "Dynamic Participant Step Example" })
public class DynamicParticipantStepExample implements ParticipantStepChooser {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {
		log.info("----------< [{}] >----------", this.getClass().getName());

		String participant = "admin";

		Workflow workflow = workItem.getWorkflow();

		List<HistoryItem> workflowHistory = workflowSession.getHistory(workflow);

		if (!workflowHistory.isEmpty()) {
			
			participant = "administrators";
		} else {
			participant = "admin";
		}

		log.info("----------< Pavan Kumar Manda Participant: {} >----------", participant);

		return participant;
	}

}