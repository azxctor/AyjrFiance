package com.hengxin.platform.job.bean;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.member.service.MemberMessageService;

@DisallowConcurrentExecution
public class BatchSMSJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchSMSJob.class);

	@Autowired
	private MemberMessageService bean;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		LOGGER.debug("running job...");
		bean.sendMessageViaJob();
	}

}
