package com.hengxin.platform.account.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.hengxin.platform.account.dto.BatchWithdDepApplApproveDto;
import com.hengxin.platform.account.dto.downstream.FundApplApproveDto;
import com.hengxin.platform.account.dto.downstream.WithdDepApplListSearchDto;
import com.hengxin.platform.account.dto.upstream.WithdrawalApplDetailDto;
import com.hengxin.platform.account.service.ExcelManagerService;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;
import com.hengxin.platform.fund.repository.WithdrawDepositApplRepository;
import com.hengxin.platform.fund.service.UserWithdrawalService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: BatchProcController
 * 
 * @author jishen
 * 
 */
@Controller
public class BatchProcController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BatchProcController.class);

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private UserWithdrawalService userWithdrawalService;
	@Autowired
	private ExcelManagerService excelManagerService;
    @Autowired
    private MemberMessageService memberMessageService;
    @Autowired
    private WithdrawDepositApplRepository withdrawDepositApplRepository;

	/**
	 * 工行资金池批量处理页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.SETTLEMENT_BATCH_PROC_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public String batchProcView(HttpServletRequest request,
			HttpSession session, Model model) {
		LOGGER.info("batchProcView() invoked");
		model.addAttribute("bankList",
				getDynamicOptions(EOptionCategory.BANK, true));
		return "myaccount/batch_procView";
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequestMapping(value = "settlement/exportExcel", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public ModelAndView getExcel(HttpServletRequest request, Model model,
			@RequestParam("bnkCd") String bnkCd,
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate) {
		try {
			List<WithdrawDepositApplPo> list = userWithdrawalService
					.getWithdrawDepositApplByCashPoolAndStatus(
							EFundApplStatus.WAIT_APPROVAL,
							EFundDealStatus.DEALING, ECashPool.ICBC_COMMON);
			String fileName = bnkCd .concat(".xls");
			String tempPath = AppConfigUtil.getExcelTemplatePath();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applList", list);
			map.put("fileName", fileName);
			map.put("tempPath", tempPath);
			return new ModelAndView(excelManagerService,map);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
		}
	}

	/**
	 * 得到提现申请列表(分页)
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("settlement/withddepappllist")
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public DataTablesResponseDto<WithdrawalApplDetailDto> getWithdrawDepositApplList(
			HttpServletRequest request, Model model,
			@RequestBody WithdDepApplListSearchDto searchDto) {
		DataTablesResponseDto<WithdrawalApplDetailDto> result = new DataTablesResponseDto<WithdrawalApplDetailDto>();
		result.setEcho(searchDto.getEcho());
		try {
			if (null != searchDto.getSearchDate()) {
				searchDto.setFromDate(DateUtils.getStartDate(searchDto
						.getSearchDate()));
				searchDto.setToDate(DateUtils.getEndDate(searchDto
						.getSearchDate()));
			}
		} catch (ParseException e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
		}
		result = userWithdrawalService.getWithdrawDepositApplList(null,
				searchDto, true, false);
		return result;
	}

    /**
     * 发送提现提醒短信
     * 
     * @param req
     */
    private void sendWithdrawalRejectMessage(String userId, BigDecimal amt) {
        LOGGER.debug("发送提醒....");
        DecimalFormat format = new DecimalFormat();
        format.applyPattern("##,##0.00");
        String messageKey = "myaccount.withdrawal.reject.message";
        memberMessageService.sendMessage(EMessageType.SMS, messageKey, userId,
                DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), format.format(amt.doubleValue()));
        memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey, userId,
                DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), format.format(amt.doubleValue()));
    }
    
	/**
	 * 提现申请否决处理
	 * 
	 * @param request
	 * @param mode
	 * @param dto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "settlement/withddepapplapprove", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public ResultDto withdrawalUnApprove(HttpServletRequest request,
			Model mode, @RequestBody FundApplApproveDto dto) {
		String userId = securityContext.getCurrentUserId();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		userWithdrawalService.approvalWithdrawDepositAppl(dto.getAppId(),
				dto.isPassed(), dto.getComments(), userId, workDate);
		WithdrawDepositApplPo appl = withdrawDepositApplRepository.findOne(dto.getAppId());
        sendWithdrawalRejectMessage(appl.getUserId(), appl.getTrxAmt());
		return ResultDtoFactory.toAck("否决处理成功");
	}

	/**
	 * 提现申请批量审批处理
	 * 
	 * @param request
	 * @param mode
	 * @param dto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "settlement/withdrawalApprove", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public ResultDto withdrawalApprove(HttpServletRequest request, Model mode,
			@RequestBody BatchWithdDepApplApproveDto dto) {
		String userId = securityContext.getCurrentUserId();
		List<WithdrawDepositApplPo> list = userWithdrawalService
				.getWithdrawDepositApplByCashPoolAndStatus(
						EFundApplStatus.WAIT_APPROVAL,
						EFundDealStatus.DEALING, ECashPool.ICBC_COMMON);
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		userWithdrawalService.doBatchUnSignUserWithdrawalApprove(list, userId,
				workDate);
		return ResultDtoFactory.toAck("提现批量审批处理成功");
	}

	/**
	 * 提现申请批量确认处理
	 * 
	 * @param request
	 * @param mode
	 * @param dto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "settlement/withdrawalConfirm", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM })
	public ResultDto withdrawalConfirm(HttpServletRequest request, Model mode,
			@RequestBody BatchWithdDepApplApproveDto dto) {
		String userId = securityContext.getCurrentUserId();
		List<WithdrawDepositApplPo> list = userWithdrawalService
				.getWithdrawDepositApplByCashPoolAndStatus(EFundApplStatus.APPROVED,
						EFundDealStatus.DEALING, ECashPool.ICBC_COMMON);
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		userWithdrawalService.doBatchUnSignUserWithdrawalConfirm(list, userId,
				workDate);
		return ResultDtoFactory.toAck("提现批量确认处理成功");
	}
}
