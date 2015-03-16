package com.hengxin.platform.batch.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.batch.dto.JobWorkDto;
import com.hengxin.platform.batch.dto.JobWorkSearchDto;
import com.hengxin.platform.batch.entity.JobWork;
import com.hengxin.platform.batch.enums.EJobWorkStatus;
import com.hengxin.platform.batch.enums.ETaskGroupId;
import com.hengxin.platform.batch.service.BatchBizTaskService;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.exception.ShowStackTraceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class JobWorkController extends BaseController {

	private List<ETaskGroupId> tskGroupOptions = Arrays.asList(
			ETaskGroupId.REPAYMENT, ETaskGroupId.BIZTASK);

	@Autowired
	private JobWorkService jobWorkService;
	
    @Autowired
    private SecurityContext securityContext;
	
	@Autowired
	private BatchBizTaskService batchBizTaskService;

	/**
	 * 执行指定任务组编号的所有任务单元
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	@RequestMapping(value = "batch/execute/taskgroup/jobWork", method = RequestMethod.POST)
	public ResultDto executeTaskGroup(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestBody JobWorkSearchDto searchDto) {
		Date workDate = searchDto.getWorkDate();
		String taskGroupId = searchDto.getTaskGroupId();
		List<JobWork> jobWorkList = jobWorkService.getJobWorkList(taskGroupId, workDate);
		boolean canExec = excuteBtnEnable(jobWorkList);
		if (!canExec) {
			return ResultDtoFactory.toNack(EErrorCode.BATCH_EXEC_INVALID);
		}
		try {
			String currOpId = securityContext.getCurrentUserId();
			batchBizTaskService.execute(taskGroupId, false, currOpId);
		} catch (BizServiceException ex){
			throw new ShowStackTraceException(ex.getError(), "任务失败", ex);
		}
		return ResultDtoFactory
				.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}

	/**
	 * 跳转所有批量任务列表页面(此交易系统中暂时不用)
	 * @param request
	 * @param session
	 * @param model
	 * @param taskGroupId
	 * @param workDateStr
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = UrlConstant.SYSTEM_MANAGEMENT_BATCH_JOB_WORK_LIST_URL)
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	public String toJobWorkListAll(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestParam(value = "taskGroupId", defaultValue = "") String taskGroupId,
			@RequestParam(value = "workDate", defaultValue = "") String workDateStr) {
		List<EnumOption> tskGrpOptions = new ArrayList<EnumOption>();
		for (ETaskGroupId grp : tskGroupOptions) {
			tskGrpOptions.add(new EnumOption(grp.getCode(), grp.getText()));
		}
		if (StringUtils.isBlank(taskGroupId)) {
			taskGroupId = CommonBusinessUtil.getDayEndBatchTaskGroupId();
		}
		Date workDate = null;
		if (StringUtils.isBlank(workDateStr)) {
			workDate = CommonBusinessUtil.getCurrentWorkDate();
		} 
		else {
			workDate = DateUtils.getDate(workDateStr, "yyyy-MM-dd");
		}
		JobWorkDto jobWork = getJobWorkDto(taskGroupId, workDate, UrlConstant.SYSTEM_MANAGEMENT_BATCH_JOB_WORK_LIST_URL);
		model.addAttribute("tskGroupOptions", tskGrpOptions);
		model.addAttribute("workDto", jobWork);
		return "batch/job_work_list";
	}
	
	/**
	 * 跳转至批量还款界面
	 * @param request
	 * @param session
	 * @param model
	 * @param taskGroupId
	 * @param workDateStr
	 * @return
	 */
	@RequestMapping(value = UrlConstant.SYSTEM_MANAGEMENT_BATCH_JOB_TASK_REPAYMENT_LIST_URL)
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	public String toBatchRepaymentList(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestParam(value = "taskGroupId", defaultValue = "") String taskGroupId,
			@RequestParam(value = "workDate", defaultValue = "") String workDateStr){
		List<EnumOption> tskGrpOptions = new ArrayList<EnumOption>();
		tskGrpOptions.add(new EnumOption(ETaskGroupId.REPAYMENT.getCode(), ETaskGroupId.REPAYMENT.getText()));
		if (StringUtils.isBlank(taskGroupId)) {
			taskGroupId = ETaskGroupId.REPAYMENT.getCode();
		}
		Date workDate = null;
		if (StringUtils.isBlank(workDateStr)) {
			workDate = CommonBusinessUtil.getCurrentWorkDate();
		} 
		else {
			workDate = DateUtils.getDate(workDateStr, "yyyy-MM-dd");
		}
		JobWorkDto jobWork = getJobWorkDto(taskGroupId, workDate, UrlConstant.SYSTEM_MANAGEMENT_BATCH_JOB_TASK_REPAYMENT_LIST_URL);
		model.addAttribute("tskGroupOptions", tskGrpOptions);
		model.addAttribute("workDto", jobWork);
		return "batch/job_work_list";
	}
	
	@RequestMapping(value = UrlConstant.DAYEND_MANAGEMENT_BATCH_BIZTASK_LIST_URL)
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	public String toBatchBizTaskList(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestParam(value = "taskGroupId", defaultValue = "") String taskGroupId,
			@RequestParam(value = "workDate", defaultValue = "") String workDateStr){
		List<EnumOption> tskGrpOptions = new ArrayList<EnumOption>();
		tskGrpOptions.add(new EnumOption(ETaskGroupId.BIZTASK.getCode(), ETaskGroupId.BIZTASK.getText()));
		if (StringUtils.isBlank(taskGroupId)) {
			taskGroupId = ETaskGroupId.BIZTASK.getCode();
		}
		Date workDate = null;
		if (StringUtils.isBlank(workDateStr)) {
			workDate = CommonBusinessUtil.getCurrentWorkDate();
		} 
		else {
			workDate = DateUtils.getDate(workDateStr, "yyyy-MM-dd");
		}
		JobWorkDto jobWork = getJobWorkDto(taskGroupId, workDate, UrlConstant.DAYEND_MANAGEMENT_BATCH_BIZTASK_LIST_URL);
		model.addAttribute("tskGroupOptions", tskGrpOptions);
		model.addAttribute("workDto", jobWork);
		return "batch/job_work_list";
	}
	
	@RequestMapping(value = UrlConstant.DAYEND_MANAGEMENT_BATCH_ROLLDATE_LIST_URL)
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	public String toRollDateTaskList(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestParam(value = "taskGroupId", defaultValue = "") String taskGroupId,
			@RequestParam(value = "workDate", defaultValue = "") String workDateStr){
		List<EnumOption> tskGrpOptions = new ArrayList<EnumOption>();
		tskGrpOptions.add(new EnumOption(ETaskGroupId.ROLLDATE.getCode(), ETaskGroupId.ROLLDATE.getText()));
		if (StringUtils.isBlank(taskGroupId)) {
			taskGroupId = ETaskGroupId.ROLLDATE.getCode();
		}
		Date workDate = null;
		if (StringUtils.isBlank(workDateStr)) {
			workDate = CommonBusinessUtil.getCurrentWorkDate();
		} 
		else {
			workDate = DateUtils.getDate(workDateStr, "yyyy-MM-dd");
		}
		JobWorkDto jobWork = getJobWorkDto(taskGroupId, workDate, UrlConstant.DAYEND_MANAGEMENT_BATCH_ROLLDATE_LIST_URL);
		model.addAttribute("tskGroupOptions", tskGrpOptions);
		model.addAttribute("workDto", jobWork);
		return "batch/job_work_list";
	}
	
	/**
	 * 获取指定任务组任务单元列表
	 * @param request
	 * @param session
	 * @param model
	 * @param taskGroupId
	 * @param workDateStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batch/getJobWorkList", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	public JobWorkDto getJobWorkList(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestParam(value = "taskGroupId", defaultValue = "") String taskGroupId,
			@RequestParam(value = "workDate", defaultValue = "") String workDateStr){
		Date workDate = null;
		if (StringUtils.isBlank(workDateStr)) {
			workDate = CommonBusinessUtil.getCurrentWorkDate();
		}
		else {
			workDate = DateUtils.getDate(workDateStr, "yyyy-MM-dd");
		}
		return getJobWorkDto(taskGroupId, workDate,"batch/getJobWorkList");
	}
	
	/**
	 * 创建制定任务组任务单元队列
	 * @param request
	 * @param session
	 * @param model
	 * @param taskGroupId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batch/create/taskgroup/jobWork", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.BATCH_JOB_WORK_LIST_MGT })
	public ResultDto createJobWork(
			HttpServletRequest request,
			HttpSession session,
			Model model,
			@RequestBody JobWorkSearchDto searchDto){
		String currOpId = securityContext.getCurrentUserId();
		batchBizTaskService.createJobWork(searchDto.getTaskGroupId(), currOpId);
		return ResultDtoFactory
				.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}

	private Boolean excuteBtnEnable(List<JobWork> jobWorkList) {
		boolean bool = false;
		if (jobWorkList != null && !jobWorkList.isEmpty()) {
			for (JobWork wk : jobWorkList) {
				EJobWorkStatus status = wk.getExecStatus();
				if (EJobWorkStatus.SUCCEED!=status) {
					bool = true;
					break;
				}
			}
		}
		return Boolean.valueOf(bool);
	}
	
	private Boolean createBtnEnable(List<JobWork> jobWorkList){
		boolean bool = true;
		if (jobWorkList != null && !jobWorkList.isEmpty()) {
			bool = false;
		}
		return Boolean.valueOf(bool);
	}
	
	private JobWorkDto getJobWorkDto(String taskGroupId, Date workDate, String retUrl){
		List<JobWork> jobWorkList = jobWorkService.getJobWorkList(taskGroupId, workDate);
		Boolean excBtnEnable = this.excuteBtnEnable(jobWorkList);
		Boolean createBtnEnable = this.createBtnEnable(jobWorkList);
		JobWorkDto jobWork = new JobWorkDto();
		jobWork.setWorkDate(workDate);
		jobWork.setExecBtnEnable(excBtnEnable);
		jobWork.setCreateBtnEnable(createBtnEnable);
		jobWork.setTaskGroupId(taskGroupId);
		jobWork.setList(jobWorkList);
		jobWork.setCurrentWorkDate(workDate);
		jobWork.setRetUrl(retUrl);
		return jobWork;
	}

}
