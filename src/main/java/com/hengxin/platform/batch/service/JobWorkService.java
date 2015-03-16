package com.hengxin.platform.batch.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.entity.JobWork;
import com.hengxin.platform.batch.enums.EJobWorkStatus;
import com.hengxin.platform.batch.enums.ETaskGroupId;
import com.hengxin.platform.batch.repository.JobWorkRepository;
import com.hengxin.platform.common.domain.CurrentWorkDate;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.repository.CurrentWorkDateRepository;

@Service
public class JobWorkService {

	private static final Logger LOG = LoggerFactory
			.getLogger(JobWorkService.class);

	@Autowired
	private JobWorkRepository jobWorkRepository;

	@Autowired
	private CurrentWorkDateRepository currentWorkDateRepository;

	/**
	 * 当前工作日期的指定任务组里任务是否全部执行成功
	 * @param taskGroupId
	 * @return
	 */
	private int getBatchTskSize(List<String> taskGroupIdList, List<EJobWorkStatus> jobWorkStatus) {
		CurrentWorkDate currentWorkDate = currentWorkDateRepository.findAll().get(0);
		Date currWorkDate = currentWorkDate.getCurrentWorkDate();
		//Date preWorkDate = currentWorkDate.getPreWorkDate();
		List<JobWork> list = jobWorkRepository.findByTaskGroupIdInAndExecStatusInAndWorkDateIn(
				taskGroupIdList, jobWorkStatus, Arrays.asList(currWorkDate));
		return list.size();
	}
	
	/**
	 * 判断批量任务是否处理中状态
	 * @return
	 */
	public boolean isBatchBizTaskProcessing(){
		int size1 = this.getBatchTskSize(Arrays.asList(ETaskGroupId.REPAYMENT.getCode()), 
				Arrays.asList(EJobWorkStatus.EXECUTING, EJobWorkStatus.FAILED));
		int size2 = this.getBatchTskSize(Arrays.asList(ETaskGroupId.BIZTASK.getCode()), 
				Arrays.asList(EJobWorkStatus.READY,EJobWorkStatus.EXECUTING, 
						EJobWorkStatus.SUCCEED, EJobWorkStatus.FAILED));
		int size3 = this.getBatchTskSize(Arrays.asList(ETaskGroupId.ROLLDATE.getCode()), 
				Arrays.asList(EJobWorkStatus.READY, EJobWorkStatus.EXECUTING, EJobWorkStatus.FAILED));
		boolean processing = size1>0 || size2>0 || size3>0;
		if(LOG.isDebugEnabled()){
			LOG.debug("判断任务是否在执行....");
			LOG.debug("正在执行的任务或者失败的任务个数为...."+(size1+size2+size3));
		}
		return processing;
	}
	
	public boolean existUnSucceedTasks(List<String> taskGroupIds){
		int size = this.getBatchTskSize(taskGroupIds, 
				Arrays.asList(EJobWorkStatus.READY, EJobWorkStatus.EXECUTING, EJobWorkStatus.FAILED));
		return size>0;
	}
	
	@Transactional(readOnly=true)
	public List<JobWork> getJobWorkList(String taskGroupId, Date workDate)throws BizServiceException{
		return jobWorkRepository.findByTaskGroupIdAndWorkDateOrderBySeqNumAsc(taskGroupId, workDate);
	}
	
}
