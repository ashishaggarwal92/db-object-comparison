package com.csg.ib.batch.support.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Configuration;

@Configuration("customJobExecutionListener")
public class CustomJobExecutionListener extends JobExecutionListenerSupport {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CustomJobExecutionListener.class);

	@Override
	public void afterJob(JobExecution jobExecution) {

		LOGGER.info("Job Name : {}", jobExecution.getJobInstance().getJobName());
		LOGGER.info("Job Params : {}", jobExecution.getJobParameters());

	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("Job Name : {}", jobExecution.getJobInstance().getJobName());
		LOGGER.info("Job Params : {}", jobExecution.getJobParameters());

	}

}
