package com.hengxin.platform.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class ExampleJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExampleJob.class);

	@Autowired
	@Qualifier("exampleJobBean")
	Object bean;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		LOGGER.debug("running job...");
		LOGGER.debug("{}", bean);
	}

}
