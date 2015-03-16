package com.hengxin.platform.account.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.account.dto.UserRechargeApplDto;
import com.hengxin.platform.account.dto.downstream.CurrentAcctTransferDto;
import com.hengxin.platform.account.dto.downstream.FundApplApproveDto;
import com.hengxin.platform.account.dto.downstream.RechargeSearchDto;
import com.hengxin.platform.account.dto.downstream.UnsignedAcctTransferDto;
import com.hengxin.platform.account.dto.downstream.UnsignedUserInfoSearchDto;
import com.hengxin.platform.account.dto.upstream.BankAccountDto;
import com.hengxin.platform.account.dto.upstream.UnsignedUserInfoDto;
import com.hengxin.platform.account.dto.upstream.UserRechargeTrxJnlsDto;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.dto.biz.req.UserRechargeReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.RechargeApplPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.service.UserRechargeService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.fund.repository.RechargeApplRepository;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 会员充值.
 * 
 * @author dcliu
 * 
 */
@Controller
public class UserRechargeController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRechargeController.class);

	@Autowired
	private AcctService acctService;
	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private BankAcctService bankAcctService;
	@Autowired
	private UserRechargeService userRechargeService;
	@Autowired
	private MemberMessageService memberMessageService;
    @Autowired
    private RechargeApplRepository rechargeApplRepository;

	/**
	 * 非签约会员账户充值获取对应账号的会员信息.
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/queryUnsignedUserInfo")
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP })
	public ResultDto queryUnsignedUserInfo(HttpServletRequest request,
			Model model, @RequestBody UnsignedUserInfoSearchDto searchDto) {
		UnsignedUserInfoDto unsignedUserInfoDto = null;
		try {
			unsignedUserInfoDto = userRechargeService
					.queryUnsignedUserInfo(searchDto);
		} catch(BizServiceException e) {
			LOGGER.debug("查询信息失败",e);
		    return ResultDtoFactory.toNack(e.getError());
		}
		return ResultDtoFactory.toAck("用户信息获取成功", unsignedUserInfoDto);
	}

	/**
	 * 非签约会员账户充值界面.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP })
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_UNSIGNED_RECHARGE_URL, method = RequestMethod.GET)
	public String unSignedAcctRechargeView(HttpServletRequest request, Model model) {
		List<EnumOption> applStatusItems = new ArrayList<EnumOption>();
		List<EFundApplStatus> applItems = Arrays.asList(EFundApplStatus.ALL,
														EFundApplStatus.WAIT_APPROVAL,
														EFundApplStatus.APPROVED,EFundApplStatus.REJECT);
		for(EFundApplStatus applStatus:applItems){
			applStatusItems.add(new EnumOption(applStatus.name(), applStatus.getText()));
		}
		model.addAttribute("applStatusItems", applStatusItems);
		model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
		return "myaccount/unsigned_recharge_appl_list";
	}

	/**
	 * 发起非签约会员账户充值申请.
	 * 
	 * @param request
	 * @param mode
	 * @param transferDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP })
	@RequestMapping(value = "myaccount/unsigned/acct/recharge", method = RequestMethod.POST)
	public ResultDto createUnsignedAcctRechargeAppl(HttpServletRequest request,
			Model mode, @RequestBody UnsignedAcctTransferDto transferDto) {
		UserRechargeReq req = ConverterService.convert(transferDto,UserRechargeReq.class);
		req.setCurrentUser(securityContext.getCurrentUserId());
		req.setCurrentDate(CommonBusinessUtil.getCurrentWorkDate());
		userRechargeService.doUnSignedUserOnBankRechargeAppl(req);
		return ResultDtoFactory.toAck("发起充值申请成功");
	}
	
	/**
	 * 获取非签约会员充值申请列表
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/get/unsigned/recharge/appl/list")
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP,
            Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP_APPROVE }, logical = Logical.OR)
	public DataTablesResponseDto<UserRechargeApplDto> getUnSignedUserRechargeApplList(
			HttpServletRequest request, HttpSession session, Model model,
			@RequestBody RechargeSearchDto searchDto){
	    if(securityContext.hasPermission(Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP)){
	        searchDto.setCreateOpid(securityContext.getCurrentUserId());
	    }
		return userRechargeService.getUserRechargeApplList(searchDto, null);
	}
	
	/**
	 * 非签约会员账户充值审批页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP_APPROVE })
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_UNSIGNED_RECHARGE_APPROVE_URL, method = RequestMethod.GET)
	public String unSignedAcctRechargeApproveView(HttpServletRequest request, Model model) {
		List<EnumOption> applStatusItems = new ArrayList<EnumOption>();
		List<EFundApplStatus> applItems = Arrays.asList(EFundApplStatus.ALL,
														EFundApplStatus.WAIT_APPROVAL,
														EFundApplStatus.APPROVED,EFundApplStatus.REJECT);
		for(EFundApplStatus applStatus:applItems){
			applStatusItems.add(new EnumOption(applStatus.name(), applStatus.getText()));
		}

		model.addAttribute("applStatusItems", applStatusItems);
		model.addAttribute("cashPoolItems", getStaticOptions(ECashPool.class, false));
		model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
		return "myaccount/unsigned_recharge_appl_list_approval";
	}
	
	@ResponseBody
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP_APPROVE })
	@RequestMapping(value = "myaccount/recharge/appl/approve", method = RequestMethod.POST)
	public ResultDto rechargeApprove(HttpServletRequest request,
			Model mode, @RequestBody FundApplApproveDto dto){
		String userId = securityContext.getCurrentUserId();
		userRechargeService.doUnSignUserRechargeApplApprove(dto.getAppId(), dto.isPassed(), dto.getComments(), userId);
		RechargeApplPo appl = rechargeApplRepository.getByApplNo(dto.getAppId());
        sendRechargeMessage(appl.getUserId(), appl.getTrxAmt());
		return ResultDtoFactory.toAck("处理完成");
	}

	/**
	 * 账户充值界面.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_DEPOSITE })
	@RequestMapping(value = "myaccount/currentacctrechargeview", method = RequestMethod.GET)
	public BankAccountDto currentAcctRechargeView(HttpServletRequest request, Model model) {
		String userId = securityContext.getCurrentUserId();
		List<BankAcct> bankAcctList = bankAcctService.findBankAcctByUserIdAndSignedFlg(userId, EFlagType.YES.getCode());
		if (bankAcctList.isEmpty()) {
        	throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST, "用户银行资料信息不存在");
		}
		return new BankAccountDto(bankAcctList.get(0).getBnkAcctNo());
	}

	/**
	 * 账户充值.
	 * 
	 * @param request
	 * @param mode
	 * @param transferDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_DEPOSITE })
	@RequestMapping(value = "myaccount/currentacctrecharge", method = RequestMethod.POST)
	public ResultDto signedAcctRecharge(HttpServletRequest request,
			Model mode, @RequestBody CurrentAcctTransferDto transferDto) {
		Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
		String userId = securityContext.getCurrentUserId();
		BankAcct bankAcct = bankAcctService.findBankAcctByBnkAcctNo(transferDto.getBankAcctNo());
		AcctPo acct = acctService.getAcctByUserId(userId);
		UserRechargeReq req = new UserRechargeReq();
		req.setAcctNo(acct.getAcctNo());
		req.setAmount(transferDto.getAmount());
		req.setBnkAcctNo(bankAcct.getBnkAcctNo());
		req.setCurrentUser(securityContext.getCurrentUserId());
		req.setMemo(transferDto.getMemo());
		req.setPassword(transferDto.getPassword());
		req.setUserId(userId);
		req.setCurrentDate(currentDate);
		req.setUserName(securityContext.getCurrentUser().getUsername());
		userRechargeService.doSignedUserOnPlatRecharge(req);
        sendRechargeMessage(userId, transferDto.getAmount());
		return ResultDtoFactory.toAck("充值成功");
	}

    /**
     * 发送充值提醒短信
     * 
     * @param req
     */
    private void sendRechargeMessage(String userId, BigDecimal amt) {
        DecimalFormat format = new DecimalFormat();
        format.applyPattern("##,##0.00");
        String messageKey = "myaccount.recharge.message";
        memberMessageService.sendMessage(EMessageType.SMS, messageKey, userId,
                DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), format.format(amt.doubleValue()));
    }
	
	/**
	 * 获取非签约用户充值记录
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/currentacctrechargetrxjnls")
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP })
	@Deprecated
	public DataTablesResponseDto<UserRechargeTrxJnlsDto> getCurrentAcctRechargeTrxJnls(
			HttpServletRequest request, HttpSession session, Model model,
			@RequestBody RechargeSearchDto searchDto) {
		return userRechargeService.getUserRechargeTrxJnlList(searchDto, null,
				EFlagType.NO);
	}

	/**
	 * 平台账户充值界面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "myaccount/platformrechargeview", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM })
	public BankAccountDto platformRechargeView(HttpServletRequest request, Model model) {
		String userId = CommonBusinessUtil.getExchangeUserId();
		List<BankAcct> bankAcctList = bankAcctService.findBankAcctByUserId(userId);
		return new BankAccountDto(bankAcctList.get(0).getBnkAcctNo());
	}


	/**
	 * 平台账户充值
	 * @param request
	 * @param mode
	 * @param transferDto
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_DEPOSITE })
	@RequestMapping(value = "myaccount/platformrecharge", method = RequestMethod.POST)
	public ResultDto platformRecharge(HttpServletRequest request, Model mode,
			@RequestBody CurrentAcctTransferDto transferDto) {
		Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
		String userId = CommonBusinessUtil.getExchangeUserId();
		String currOpId = securityContext.getCurrentUserId();
		BankAcct bankAcct = bankAcctService.findBankAcctByBnkAcctNo(transferDto.getBankAcctNo());
		AcctPo acct = acctService.getAcctByUserId(userId);
		UserRechargeReq req = new UserRechargeReq();
		req.setAcctNo(acct.getAcctNo());
		req.setAmount(transferDto.getAmount());
		req.setBnkAcctNo(bankAcct.getBnkAcctNo());
		req.setCurrentUser(currOpId);
		req.setMemo(transferDto.getMemo());
		req.setPassword(transferDto.getPassword());
		req.setUserId(userId);
		req.setCurrentDate(currentDate);
		req.setUserName(bankAcct.getBnkAcctName());
		userRechargeService.doSignedUserOnPlatRecharge(req);
		return ResultDtoFactory.toAck("充值成功");
	}

}
