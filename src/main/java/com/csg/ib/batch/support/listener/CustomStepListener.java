package com.csg.ib.batch.support.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomStepListener extends StepExecutionListenerSupport {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CustomStepListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {

		LOGGER.info("BEFORE STEP ----- Step Name : {}", stepExecution.getStepName());

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("AFTER STEP ----- Step Name : {}", stepExecution.getStepName());
		return ExitStatus.COMPLETED;

	}

}
