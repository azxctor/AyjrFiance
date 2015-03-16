package com.hengxin.platform.job.bean;

import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.batch.service.BatchBizTaskService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CacheUtil;

@DisallowConcurrentExecution
public class BatchTaskJob extends QuartzJobBean {

	private static final Logger LOG = LoggerFactory
			.getLogger(BatchTaskJob.class);

	@Autowired
	private BatchBizTaskService batchBizTaskService;

	@Override
	protected void executeInternal(JobExecutionContext cxt)
			throws JobExecutionException {
		LOG.debug("系统批量任务调起.....");
		JobDataMap dataMap = cxt.getTrigger().getJobDataMap();
		if(dataMap==null||dataMap.isEmpty()){
			throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED,
					"批量任务 JobDataMap参数不能为空...");
		}
		CacheUtil.disableCache();
		try{
			for (Entry<String, Object> entry : dataMap.entrySet()) {
				String key = entry.getKey();
				Object groupId = entry.getValue();
				if (groupId == null) {
					throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED,
							"批量任务 JobDataMap参数"+key+"不能为空...");
				}
				long cost = 0;
				long start = System.currentTimeMillis();
				String taskGroupId = String.valueOf(groupId);
				batchBizTaskService.execute(taskGroupId, true, "sys");
				cost = System.currentTimeMillis() - start;
				LOG.debug("task group id " + taskGroupId
						+ " is invoked...cost times is " + cost + " milliseconds");
			}
		}catch(BizServiceException ex){
			LOG.error("error in execute batch job", ex);
			CacheUtil.enableCache();
			throw ex;
		}
		CacheUtil.enableCache();
	}

}
