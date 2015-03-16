package com.hengxin.platform.batch.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.entity.JobWork;
import com.hengxin.platform.batch.enums.ETaskGroupId;
import com.hengxin.platform.batch.repository.JobWorkRepository;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.ShowStackTraceException;
import com.hengxin.platform.common.repository.CurrentWorkDateRepository;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.AutoSubscribeParamService;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.security.enums.EBizRole;

@Service
public class BatchBizTaskService {

	private static final Logger LOG = LoggerFactory
			.getLogger(BatchBizTaskService.class);
	
	private static final String messageKey = "batch.biz.task.success.message";

	private static final String SUCCEED = "0";
	
	@Autowired
	private JobWorkService jobWorkService;

	@Autowired
	private JobWorkRepository jobWorkRepository;
	
	@Autowired
	private MemberMessageService memberMessageService;

	@Autowired
	private CurrentWorkDateRepository currentWorkDateRepository;
	
	@Autowired
	private AutoSubscribeParamService autoSubscribeParamService;

	@PersistenceContext(unitName = "default")
	private EntityManager em;
	
	/**
	 * 创建任务队列
	 * @param taskGroupId
	 * @param currOpId
	 */
	@Transactional
	public void createJobWork(String taskGroupId, String currOpId){
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		if(existsJobWorks(taskGroupId, workDate)){
			throw new ShowStackTraceException(EErrorCode.BATCH_EXEC_FAILED,"任务已经存在!");
		}
		LOG.debug("开始创建任务....taskGroupId is {} ", taskGroupId);
		StoredProcedureQuery procedure = em
				.createStoredProcedureQuery("pkg_job_work.create_job_tsk");
		procedure.registerStoredProcedureParameter(1, Date.class,
				ParameterMode.IN);
		procedure.setParameter(1, workDate);
		procedure.registerStoredProcedureParameter(2, String.class,
				ParameterMode.IN);
		procedure.setParameter(2, taskGroupId);
		procedure.registerStoredProcedureParameter(3, String.class,
				ParameterMode.IN);
		procedure.setParameter(3, currOpId);
		procedure.registerStoredProcedureParameter(4, Long.class,
				ParameterMode.OUT);
		procedure.registerStoredProcedureParameter(5, String.class,
				ParameterMode.OUT);
		procedure.registerStoredProcedureParameter(6, String.class,
				ParameterMode.OUT);
		procedure.execute();
		String retCode = (String) procedure.getOutputParameterValue(5);
		String retMsg = (String) procedure.getOutputParameterValue(6);
		LOG.debug("执行结果, retCode=" + retCode + ",retMsg=" + retMsg);
		if (StringUtils.equals(SUCCEED, retCode)) {
			LOG.debug("任务创建成功， taskGroupId is {}", taskGroupId);
			CommonBusinessUtil.init();
		}else{
			LOG.debug("任务创建失败， taskGroupId is {}", taskGroupId);
			throw new ShowStackTraceException(EErrorCode.BATCH_EXEC_FAILED,"任务创建失败:"+retMsg);
		}
	}

	/**
	 * 执行指定任务组taskGroupId的任务
	 * @param taskGroupId
	 * @param autoRun
	 * @param currOpId
	 * @throws BizServiceException
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void execute(String taskGroupId, boolean autoRun, String currOpId){
		if (!autoRun) {
			boolean exists = jobWorkService.existUnSucceedTasks(Arrays.asList(taskGroupId));
			if (!exists) {
				throw new ShowStackTraceException(EErrorCode.TECH_DATA_NOT_EXIST,
						"不存在要执行的任务");
			}
		}
		LOG.debug("开始执行任务....taskGroupId is {}", taskGroupId);
		StoredProcedureQuery procedure = em
				.createStoredProcedureQuery("pkg_job_batch.execute_tsk");
		procedure.registerStoredProcedureParameter(1, String.class,
				ParameterMode.IN);
		procedure.setParameter(1, taskGroupId);
		procedure.registerStoredProcedureParameter(2, String.class,
				ParameterMode.IN);
		procedure.setParameter(2, currOpId);
		procedure.registerStoredProcedureParameter(3, String.class,
				ParameterMode.OUT);
		procedure.registerStoredProcedureParameter(4, String.class,
				ParameterMode.OUT);
		procedure.execute();
		String retCode = (String) procedure.getOutputParameterValue(3);
		String retMsg = (String) procedure.getOutputParameterValue(4);
		LOG.debug("任务执行结果, retCode=" + retCode + ",retMsg=" + retMsg);
		if (StringUtils.equals(SUCCEED, retCode)) {
			LOG.debug("任务执行成功....taskGroupId is {}", taskGroupId);
			Date workDate = CommonBusinessUtil.getCurrentWorkDate();
			/** launch auto subscriber param job. **/
			autoSubscribeParamService.launchPendingSubscribeParams();
			// refresh cache
			CommonBusinessUtil.init();
			// send message to roles
			if(StringUtils.equalsIgnoreCase(ETaskGroupId.ROLLDATE.getCode(), taskGroupId)){
				this.sendMsgToBizRole(EMessageType.MESSAGE, Arrays.asList(
						EBizRole.ADMIN,
	                    EBizRole.TRANS_MANAGER, 
	                    EBizRole.PLATFORM_SETTLEMENT_MANAGER,
	                    EBizRole.PLATFORM_SETTLEMENT_DOUBLE_CHECK_1,
	                    EBizRole.PLATFORM_SETTLEMENT_DOUBLE_CHECK_2,
	                    EBizRole.PLATFORM_SETTLEMENT_MAKER_1,
	                    EBizRole.PLATFORM_SETTLEMENT_MAKER_2,
	                    EBizRole.PLATFORM_SETTLEMENT_CMB_MAKER,
	                    EBizRole.PLATFORM_SETTLEMENT_ICBC_MAKER
	                    ), workDate);
			}
		} 
		else {
			CommonBusinessUtil.init();
			LOG.debug("批量任务执行失败....taskGroupId is {}", taskGroupId);
			throw new ShowStackTraceException(EErrorCode.BATCH_EXEC_FAILED,"任务执行失败:"+retMsg);
		}
	}
	
	/**
	 * 发送提醒消息
	 * @param msgType
	 * @param roles
	 * @param workDate
	 */
	private void sendMsgToBizRole(EMessageType msgType, List<EBizRole> roles, Date workDate) {
		try{
			// 系统交易日期为 {0} 的日终批量执行成功
			memberMessageService.sendMessage(msgType, messageKey, roles, DateUtils.formatDate(workDate, "yyyy-MM-dd"));
		}catch(Exception ex){
			LOG.debug("批量任务执行失败........", ex);
		}
	}
	
	private boolean existsJobWorks(String taskGroupId, Date workDate){
		List<JobWork> list = jobWorkService.getJobWorkList(taskGroupId, workDate);
		return !list.isEmpty();
	}

}
