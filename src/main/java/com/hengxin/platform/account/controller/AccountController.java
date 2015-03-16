/*
 * Project Name: kmfex-platform
 * File Name: AccountController.java
 * Class Name: AccountController
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.account.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import com.hengxin.platform.account.dto.AccountDetailsDto;
import com.hengxin.platform.account.dto.CurrentAccountDto;
import com.hengxin.platform.account.dto.InvestmentProfitDto;
import com.hengxin.platform.account.dto.PrintInfoSearchDto;
import com.hengxin.platform.account.dto.TradeDetailsDto;
import com.hengxin.platform.account.dto.WithDspPrintPriInfoDto;
import com.hengxin.platform.account.dto.WithdrawApplDto;
import com.hengxin.platform.account.dto.WithdrawalApplSumAmtDto;
import com.hengxin.platform.account.dto.downstream.AccountDetailsSearchDto;
import com.hengxin.platform.account.dto.downstream.BnkCardBalSearchDto;
import com.hengxin.platform.account.dto.downstream.CurrentAcctTransferDto;
import com.hengxin.platform.account.dto.downstream.FundApplApproveDto;
import com.hengxin.platform.account.dto.downstream.TradeDetailsSearchDto;
import com.hengxin.platform.account.dto.downstream.WithDrawApplyDto;
import com.hengxin.platform.account.dto.downstream.WithdConfirmDto;
import com.hengxin.platform.account.dto.downstream.WithdDepApplListSearchDto;
import com.hengxin.platform.account.dto.upstream.AccountBalDto;
import com.hengxin.platform.account.dto.upstream.AccountOverviewSearchDto;
import com.hengxin.platform.account.dto.upstream.BenifitDto;
import com.hengxin.platform.account.dto.upstream.BnkCardBalDto;
import com.hengxin.platform.account.dto.upstream.PrintInfoDto;
import com.hengxin.platform.account.dto.upstream.WithdrawalApplDetailDto;
import com.hengxin.platform.account.service.AccountDetailsExcelService;
import com.hengxin.platform.account.service.AccountDetailsService;
import com.hengxin.platform.account.service.AccountOverviewService;
import com.hengxin.platform.account.service.PlatformAccountDetailsExcel;
import com.hengxin.platform.account.service.PrintInfoService;
import com.hengxin.platform.account.service.TradeDetailsService;
import com.hengxin.platform.account.service.WithdrawDepositApplsApproveExcel;
import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.escrow.enums.EEbcUserType;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.dto.biz.req.UserWithdrawalReq;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.WithdrawDepositApplRepository;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.service.InvestorProfitSummaryService;
import com.hengxin.platform.fund.service.UserWithdrawalService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: AccountController.
 * 
 * @author congzhou
 * 
 */

@Controller
public class AccountController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountController.class);

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private AccountDetailsService accountDetailsService;
	@Autowired
	private UserWithdrawalService userWithdrawalService;
	@Autowired
	private BankAcctService bankAcctService;
	@Autowired
	private AccountOverviewService accountOverviewService;
	@Autowired
	private TradeDetailsService tradeDetailsService;
	@Autowired
	private PrintInfoService printInfoService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private InvestorProfitSummaryService investorProfitSummaryService;
	@Autowired
	private PlatformAccountDetailsExcel platformAccountDetailsExcel;
	@Autowired
	private WithdrawDepositApplsApproveExcel withdrawDepositApplsApproveExcel;
	@Autowired
	private AccountDetailsExcelService accountDetailsExcelService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private WithdrawDepositApplRepository withdrawDepositApplRepository;
	@Autowired
	private UserService userService;

	/**
	 * 发送提现提醒短信
	 * 
	 * @param req
	 */
	private void sendWithdrawalMessage(String userId, BigDecimal amt) {
		LOGGER.debug("发送提醒....");
		DecimalFormat format = new DecimalFormat();
		format.applyPattern("##,##0.00");
		String messageKey = "myaccount.withdrawal.message";
		memberMessageService.sendMessage(EMessageType.SMS, messageKey, userId,
				DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
				format.format(amt.doubleValue()));
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
				DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
				format.format(amt.doubleValue()));
		memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey,
				userId,
				DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
				format.format(amt.doubleValue()));
	}

	/**
	 * 非签约会员提现申请审批列表导出.
	 */
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_WITHDRAW_APPLY_EX_APPROVE_URL, method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE_EX })
	public ModelAndView withdrawDepositApplsApproveExView(
			HttpServletRequest request, Model model,
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("cashPool") String cashPool,
			@RequestParam("status") String status,
			@RequestParam("applStatusFlag") boolean applFlag,
			@RequestParam("dealStatusFlag") boolean dealFlag) {
		try {
			WithdDepApplListSearchDto searchDto = new WithdDepApplListSearchDto();

			if (applFlag) {
				searchDto.setApplStatus(EFundApplStatus.valueOf(status));
			}
			if (dealFlag) {
				searchDto.setDealStatus(EFundDealStatus.valueOf(status));
			}

			if (!StringUtils.isBlank(cashPool)) {
				searchDto.setCashPool(ECashPool.valueOf(cashPool));
			}
			if (!StringUtils.isBlank(fromDate)) {
				searchDto.setFromDate(DateUtils.getDate(fromDate,
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(toDate)) {
				searchDto.setToDate(DateUtils.getDate(toDate,
						LiteralConstant.YYYY_MM_DD));
			}
			String userId = securityContext.getCurrentUserId();
			List<WithdrawalApplDetailDto> detailList = userWithdrawalService
					.getWithdrawDepositApplExList(userId, searchDto, applFlag,
							dealFlag);
			String fileName = "非签约会员提现.xls";
			String tempPath = AppConfigUtil.getWithdrawDAATExcelTemplatePath();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("detailList", detailList);
			map.put("fileName", fileName);
			map.put("tempPath", tempPath);

			return new ModelAndView(withdrawDepositApplsApproveExcel, map);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
		}
	}

	/**
	 * 非签约会员提现申请审批列表打印 .
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/withDspPrintView")
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE_PRINT })
	public List<WithdrawalApplDetailDto> withdrawDepositApplsApprovePrintView(
			HttpServletRequest request, HttpSession session, Model model,
			@RequestBody WithDspPrintPriInfoDto printDto) {
		try {
			WithdDepApplListSearchDto searchDto = new WithdDepApplListSearchDto();

			if (Boolean.parseBoolean(printDto.getApplFlag())) {
				searchDto.setApplStatus(EFundApplStatus.valueOf(printDto
						.getStatus()));
			}
			if (Boolean.parseBoolean(printDto.getDealFlag())) {
				searchDto.setDealStatus(EFundDealStatus.valueOf(printDto
						.getStatus()));
			}

			if (!StringUtils.isBlank(printDto.getCashPool())) {
				searchDto
						.setCashPool(ECashPool.valueOf(printDto.getCashPool()));
			}
			if (!StringUtils.isBlank(printDto.getFromDate())) {
				searchDto.setFromDate(DateUtils.getDate(printDto.getFromDate(),
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(printDto.getToDate())) {
				searchDto.setToDate(DateUtils.getDate(printDto.getToDate(),
						LiteralConstant.YYYY_MM_DD));
			}
			String userId = securityContext.getCurrentUserId();
			List<WithdrawalApplDetailDto> dtos = userWithdrawalService
					.getWithdrawDepositApplExList(userId, searchDto,
							Boolean.parseBoolean(printDto.getApplFlag()),
							Boolean.parseBoolean(printDto.getDealFlag()));
			if (null == dtos) {
				dtos = new ArrayList<WithdrawalApplDetailDto>();
			}
			return dtos;
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
		}
	}

	/**
	 * 非签约会员提现申请确认列表打印 .
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/withDspPrint2View")
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE_PRINT2 })
	public List<WithdrawalApplDetailDto> withdrawDepositApplsApprovePrint2View(
			HttpServletRequest request, HttpSession session, Model model,
			@RequestBody WithDspPrintPriInfoDto printDto) {
		try {
			WithdDepApplListSearchDto searchDto = new WithdDepApplListSearchDto();

			if (Boolean.parseBoolean(printDto.getApplFlag())) {
				searchDto.setApplStatus(EFundApplStatus.valueOf(printDto
						.getStatus()));
			}
			if (Boolean.parseBoolean(printDto.getDealFlag())) {
				searchDto.setDealStatus(EFundDealStatus.valueOf(printDto
						.getStatus()));
			}

			if (!StringUtils.isBlank(printDto.getCashPool())) {
				searchDto
						.setCashPool(ECashPool.valueOf(printDto.getCashPool()));
			}
			if (!StringUtils.isBlank(printDto.getFromDate())) {
				searchDto.setFromDate(DateUtils.getDate(printDto.getFromDate(),
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(printDto.getToDate())) {
				searchDto.setToDate(DateUtils.getDate(printDto.getToDate(),
						LiteralConstant.YYYY_MM_DD));
			}
			String userId = securityContext.getCurrentUserId();
			List<WithdrawalApplDetailDto> dtos = userWithdrawalService
					.getWithdrawDepositApplExList(userId, searchDto,
							Boolean.parseBoolean(printDto.getApplFlag()),
							Boolean.parseBoolean(printDto.getDealFlag()));
			if (null == dtos) {
				dtos = new ArrayList<WithdrawalApplDetailDto>();
			}
			return dtos;
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
		}
	}

	/**
	 * 
	 * 导出平台账号明细excel.
	 * 
	 * @param request
	 * @param model
	 */

	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequestMapping(value = UrlConstant.PLATFORM_ACCOUNT_DETAIL_EX_URL, method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_VIEW_DETAIL_EX })
	public ModelAndView accountDetailsListForPlatformInExcel(
			HttpServletRequest request, Model model,
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("useType") String useType) {
		try {
			AccountDetailsSearchDto searchDto = new AccountDetailsSearchDto();
			if (!StringUtils.isBlank(fromDate)) {
				searchDto.setFromDate(DateUtils.getDate(fromDate,
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(toDate)) {
				searchDto.setToDate(DateUtils.getDate(toDate,
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(useType)) {
				searchDto.setUseType(EFundUseType.valueOf(useType));
			}
			String userId = CommonBusinessUtil.getExchangeUserId();
			List<AccountDetailsDto> detailList = accountDetailsService
					.getAccountExcDetails(searchDto, userId);

			String fileName = "平台账号明细.xls";
			String tempPath = AppConfigUtil
					.getPlatAccountDetailExcelTemplatePath();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("detailList", detailList);
			map.put("fileName", fileName);
			map.put("tempPath", tempPath);

			return new ModelAndView(platformAccountDetailsExcel, map);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
		}
	}

	/**
	 * Render account details page.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_ACCOUNT_DETAILS_URL)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_ACCOUNT_DETAILS })
	public String renderAccountDetailsList(HttpServletRequest request,
			HttpSession session, Model model) {
		addUseTypeOptionsToModel(model);
		model.addAttribute("isPlatformUser", securityContext.isPlatformUser());
		return "myaccount/account_list";
	}

	/**
	 * Render platform account details page.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.SETTLEMENT_PLATFORM_ACCOUNT_DETAILS_URL)
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_VIEW_DETAIL })
	public String renderAccountDetailsListForPlatform(
			HttpServletRequest request, HttpSession session, Model model) {
		return this.renderAccountDetailsList(request, session, model);
	}

	/**
	 * Description: 出入金类型.
	 * 
	 * @param model
	 */

	private void addUseTypeOptionsToModel(Model model) {
		List<EnumOption> userTypeList = new ArrayList<EnumOption>();
		userTypeList.add(new EnumOption(EFundUseType.INOUTALL.name(),
				EFundUseType.INOUTALL.getText()));
		List<EFundUseType> filterDicts = accountDetailsService
				.getFilterDictsByRoles();
		Iterator<EFundUseType> iter = filterDicts.iterator();
		while (iter.hasNext()) {
			EFundUseType type = iter.next();
			if (type == null)
				continue;
			userTypeList.add(new EnumOption(type.name(), type.getText()));
		}
		model.addAttribute("useTypeList", userTypeList);
	}

	/**
	 * Get account details List.
	 * 
	 * @param request
	 * @param model
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping("myaccount/getaccountdetails")
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_ACCOUNT_DETAILS })
	public DataTablesResponseDto<AccountDetailsDto> getAccountDetailsList(
			HttpServletRequest request, Model model,
			@RequestBody AccountDetailsSearchDto query) {
		String userId = securityContext.getCurrentUserId();
		DataTablesResponseDto<AccountDetailsDto> result = accountDetailsService
				.getAccountDetails(query, userId);
		result.setEcho(query.getEcho());
		return result;
	}

	/**
	 * Get my account details XLS.
	 * 
	 * @param request
	 * @param model
	 * @param query
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequestMapping(value = "myaccount/getaccountdetailsinxls", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_ACCOUNT_DETAILS })
	public ModelAndView getAccountDetailsListInExcel(
			HttpServletRequest request, Model model,
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("useType") String useType) {
		try {
			AccountDetailsSearchDto searchDto = new AccountDetailsSearchDto();
			if (!StringUtils.isBlank(fromDate)) {
				searchDto.setFromDate(DateUtils.getDate(fromDate,
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(toDate)) {
				searchDto.setToDate(DateUtils.getDate(toDate,
						LiteralConstant.YYYY_MM_DD));
			}
			if (!StringUtils.isBlank(useType)) {
				searchDto.setUseType(EFundUseType.valueOf(useType));
			}

			String userId = securityContext.getCurrentUserId();
			List<AccountDetailsDto> accountdetails = accountDetailsService
					.getAccountDetailsList(searchDto, userId);

			String fileName = "我的账户明细.xls";
			// FIXME
			String tempPath = AppConfigUtil
					.getMyAccountDetailExcelTemplatePath();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("accountdetails", accountdetails);
			map.put("fileName", fileName);
			map.put("tempPath", tempPath);

			return new ModelAndView(accountDetailsExcelService, map);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
		}
	}

	/**
	 * Get platform account details List.
	 * 
	 * @param request
	 * @param model
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping("myaccount/getplatformaccountdetails")
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_VIEW_DETAIL })
	public DataTablesResponseDto<AccountDetailsDto> getPlatformAccountDetailsList(
			HttpServletRequest request, Model model,
			@RequestBody AccountDetailsSearchDto query) {
		String userId = CommonBusinessUtil.getExchangeUserId();
		DataTablesResponseDto<AccountDetailsDto> result = accountDetailsService
				.getAccountDetails(query, userId);
		result.setEcho(query.getEcho());
		return result;
	}

	/**
	 * 获取银行可充值金额.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("myaccount/getbnkCardBal")
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_OTHERS })
	public BnkCardBalDto getBnkCardBal(HttpServletRequest request, Model model,
			@RequestBody BnkCardBalSearchDto searchDto) {
		String userId = securityContext.getCurrentUserId();
		return accountOverviewService.getBnkCardBal(searchDto.getBnkAcctNo(),
				userId);
	}

	/**
	 * 获取打印信息（充值申请审批）.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/queryPrintInfo")
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM_PRINT })
	public PrintInfoDto queryPrintInfo(HttpServletRequest request,
			HttpSession session, Model model,
			@RequestBody PrintInfoSearchDto searchDto) {
		LOGGER.info("queryPrintInfo() invoked");
		return printInfoService.getPrintInfoDetail(searchDto);
	}

	/**
	 * 获取打印信息（充值申请）.
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/queryApplPrintInfo")
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP })
	public PrintInfoDto queryApplPrintInfo(HttpServletRequest request,
			HttpSession session, Model model,
			@RequestBody PrintInfoSearchDto searchDto) {
		LOGGER.info("queryPrintInfo() invoked");
		return printInfoService.getApplPrintInfoDetail(searchDto);
	}

	/**
	 * 账户提现界面.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/currentacctwithdrawalview", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW })
	public AccountBalDto currentAcctWithdrawalView(HttpServletRequest request,
			Model model) {
		LOGGER.info("currentAcctWithdrawalView() invoked");
		String userId = securityContext.getCurrentUserId();
		AccountBalDto dto = accountDetailsService
				.getCurrentAcctCashableAmt(userId);
		List<BankAcct> bankAcctList = bankAcctService
				.findBankAcctByUserId(userId);
		dto.setBankAcctNo(bankAcctList.get(0).getBnkAcctNo());
		return dto;
	}

	/**
	 * 平台账户提现界面.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/plarformwithdrawalview", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_WITHDRAW })
	public AccountBalDto plarformWithdrawalView(HttpServletRequest request,
			Model model) {
		LOGGER.info("plarformWithdrawalView() invoked");
		String userId = CommonBusinessUtil.getExchangeUserId();
		AccountBalDto dto = accountDetailsService
				.getCurrentAcctCashableAmt(userId);
		List<BankAcct> bankAcctList = bankAcctService
				.findBankAcctByUserId(userId);
		dto.setBankAcctNo(bankAcctList.get(0).getBnkAcctNo());
		return dto;
	}

	/**
	 * 账户提现.
	 * 
	 * @param request
	 * @param mode
	 * @param transferDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/currentacctwithdrawal", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW })
	public ResultDto currentAcctWithdrawal(HttpServletRequest request,
			Model mode, @RequestBody CurrentAcctTransferDto transferDto) {
		LOGGER.info("currentAcctWithdrawal() invoked");
		Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
		String userId = securityContext.getCurrentUserId();
		BankAcct bankAcct = bankAcctService.findBankAcctByUserId(userId).get(0);
		UserWithdrawalReq req = new UserWithdrawalReq();
		req.setAmount(transferDto.getAmount());
		req.setBnkAcctNo(null == bankAcct ? "" : bankAcct.getBnkAcctNo());
		/** Notice: this should be bank name rather than full name. **/
		req.setBnkOpenProv(bankAcct.getBnkName());
		req.setCurrentUser(securityContext.getCurrentUserId());
		req.setMemo(transferDto.getMemo());
		req.setPassword(transferDto.getPassword());
		req.setUserId(userId);
		// 存中文名
		req.setUserName(securityContext.getCurrentUser().getName());
		req.setRemFlg(transferDto.getRemFlg());
		req.setCurrentDate(currentDate);
		userWithdrawalService.doSignUserWithdrawalOnPlat(req);
		sendWithdrawalMessage(req.getUserId(), req.getAmount());
		return ResultDtoFactory.toAck("提现成功");
	}

	/**
	 * 平台账户提现.
	 * 
	 * @param request
	 * @param mode
	 * @param transferDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/platformwithdrawal", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW,
			Permissions.PLATFORM_UNIVERSAL_ACCOUNT_WITHDRAW })
	public ResultDto platformWithdrawal(HttpServletRequest request, Model mode,
			@RequestBody CurrentAcctTransferDto transferDto) {
		LOGGER.info("platformWithdrawal() invoked");
		Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
		String userId = CommonBusinessUtil.getExchangeUserId();
		BankAcct bankAcct = bankAcctService.findBankAcctByUserId(userId).get(0);
		UserWithdrawalReq req = new UserWithdrawalReq();
		req.setAmount(transferDto.getAmount());
		req.setBnkAcctNo(null == bankAcct ? "" : bankAcct.getBnkAcctNo());
		req.setCurrentUser(securityContext.getCurrentUserId());
		req.setMemo(transferDto.getMemo());
		req.setPassword(transferDto.getPassword());
		req.setUserId(userId);
		// FIXME
		// 存中文名
		req.setUserName(securityContext.getCurrentUser().getName());
		req.setRemFlg(transferDto.getRemFlg());
		req.setCurrentDate(currentDate);
		userWithdrawalService.doSignUserWithdrawalOnPlat(req);
		sendWithdrawalMessage(req.getUserId(), req.getAmount());
		return ResultDtoFactory.toAck("提现成功");
	}

	/**
	 * 账户总览.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_OVEWVIEW_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_OTHERS }, logical = Logical.OR)
	public String getAccountOverviewDetails(HttpServletRequest request,
			Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoke AccountController.getAccountOverviewDetails");
		}
		String userId = securityContext.getCurrentUserId();
		AccountOverviewSearchDto dto = accountOverviewService
				.getAccountOverviewDetailInfo(userId);
		User user = userService.getUserByUserId(userId);

		dto.getUserInfo().setInvestor(securityContext.isInvestor());
		dto.getUserInfo().setFinancier(securityContext.isFinancier());
		model.addAttribute("userInfo", dto.getUserInfo());
		model.addAttribute("currentAccount", dto.getCurrentAccount());
		model.addAttribute("xwbAccount", dto.getXwbAccount());
		model.addAttribute("accountOverview", dto.getAccountOverview());
		model.addAttribute("accountDetails", dto.getAccountDetails());
		model.addAttribute("investBenifit", dto.getInvestBenifit());
		model.addAttribute("rechargeUrl", "currentacctrecharge");
		model.addAttribute("wirhdrawUrl", "currentacctwithdrawal");
		model.addAttribute("withdrawApplyUrl", "withdrawApply");
		model.addAttribute("accountDetailsUrl", "getaccountdetails");
		model.addAttribute("eswAcctStatus", user.getEswAcctStatus());// 开通托管账户状态
		model.addAttribute("bindingCardStatus", user.getBindingCardStatus());// 绑卡状态
		model.addAttribute("iconUrl", accountOverviewService.getIconUrl(userId));// 头像Url
		model.addAttribute("userTypes", getUserTypes());//银行账户类型：个人/企业
		return "myaccount/overview";
	}
	
	private List<EnumOption> getUserTypes() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		options.add(new EnumOption(EEbcUserType.PERSON.name(),
				EEbcUserType.PERSON.getText()));
		options.add(new EnumOption(EEbcUserType.ENTERPRISE.name(),
				EEbcUserType.ENTERPRISE.getText()));
		return options;
	}

	/**
	 * Description: 平台账户总览.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.PLATFORM_ACCOUNT_OVERVIEW_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_VIEW })
	public String getPlatfromAccountOverview(HttpServletRequest request,
			Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoke AccountController.getPlatfromAccountOverview");
		}
		String userId = CommonBusinessUtil.getExchangeUserId();
		AccountOverviewSearchDto dto = accountOverviewService
				.getAccountOverviewDetailInfo(userId);
		dto.getUserInfo().setInvestor(securityContext.isInvestor());
		dto.getUserInfo().setFinancier(securityContext.isFinancier());
		model.addAttribute("userInfo", dto.getUserInfo());
		model.addAttribute("currentAccount", dto.getCurrentAccount());
		model.addAttribute("accountOverview", dto.getAccountOverview());
		model.addAttribute("accountDetails", dto.getAccountDetails());
		model.addAttribute("platformAmt", dto.getTotalAmount());
		model.addAttribute("rechargeUrl", "platformrecharge");
		model.addAttribute("wirhdrawUrl", "platformwithdrawal");
		model.addAttribute("withdrawApplyUrl", "platformwithdrawApply");
		model.addAttribute("accountDetailsUrl", "getplatformaccountdetails");
		return "myaccount/platform_overview";
	}

	/**
	 * 
	 * Description: 获取投资收益.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/accountbenifits", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_ESTIMATED_INCOME })
	public BenifitDto getAccountBenifits(HttpServletRequest request, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoke AccountController.getAccountBenifits");
		}
		String userId = securityContext.getCurrentUserId();
		return accountOverviewService.getBenifitDetails(userId);
	}

	@ResponseBody
	@RequestMapping(value = "myaccount/investment/myprofit", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_OTHERS })
	public InvestmentProfitDto getInvestmentProfitDto(
			HttpServletRequest request, Model model) {
		String userId = securityContext.getCurrentUserId();
		BigDecimal expectTotalProfitRate = investorProfitSummaryService
				.getExpectTotalProfitRate(userId);
		BigDecimal expectTotalProfit = investorProfitSummaryService
				.getInvestorExpectTotalReceivedProfit(userId);
		BigDecimal realizedTotalProfit = investorProfitSummaryService
				.getInvestorRealizedReceivedProfit(userId);
		BigDecimal unRecvProfit = investorProfitSummaryService
				.getUnReceivedProfit(userId);
		InvestmentProfitDto dto = new InvestmentProfitDto();
		dto.setExpectTotalRecvProfit(unRecvProfit);
		dto.setExpectTotalProfit(expectTotalProfit);
		dto.setRealizedTotalRecvProfit(realizedTotalProfit);
		dto.setExpectTotalProfitRate(expectTotalProfitRate);
		dto.setRealizedTotalRecvProfitRate(BigDecimal.ZERO);
		return dto;
	}

	/**
	 * 
	 * Description: 获取饼图数据.
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/accountInfo", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_OTHERS,
			Permissions.PLATFORM_UNIVERSAL_ACCOUNT_VIEW }, logical = Logical.OR)
	public BenifitDto getAccountInfo(HttpServletRequest request, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoke AccountController.getAccountInfo");
		}
		String userId;
		if (securityContext.isPlatformUser()) {
			userId = CommonBusinessUtil.getExchangeUserId();
		} else {
			userId = securityContext.getCurrentUserId();
		}
		BenifitDto result = accountOverviewService.getBenifitDetails(userId);
		return result;
	}

	/**
	 * Render account details page 获取活期账户详情
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "myaccount/currentAccountDetail", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_OTHERS })
	public CurrentAccountDto getCurrentAccountDetail(
			HttpServletRequest request, HttpSession session, Model model) {
		LOGGER.info("getCurrentAccountDetail() invoked");
		String userId = securityContext.getCurrentUserId();
		return accountOverviewService.getCurrentAccountDetail(userId);
	}

	/**
	 * 交易明细
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_TRANS_DETAILS_URL)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_TRANS_DETAILS })
	public String renderTradeDetailsList(HttpServletRequest request,
			HttpSession session, Model model) {
		addTrdTypeOptionsToModel(model);
		return "myaccount/trans_list";
	}

	/**
	 * Description:
	 * 
	 * @param model
	 */

	private void addTrdTypeOptionsToModel(Model model) {
		List<EnumOption> trdTypeList = new ArrayList<EnumOption>();
		trdTypeList.add(new EnumOption(EFundTrdType.ALL.name(),
				EFundTrdType.ALL.getText()));
		trdTypeList.add(new EnumOption(EFundTrdType.BONDSUBS.name(),
				EFundTrdType.BONDSUBS.getText()));
		if (securityContext.canViewTransferMarket()) {
			trdTypeList.add(new EnumOption(EFundTrdType.BONDASSIGN.name(),
					EFundTrdType.BONDASSIGN.getText()));
		}
		model.addAttribute("trdTypeList", trdTypeList);
	}

	/**
	 * 得到交易明细列表
	 * 
	 * @param request
	 * @param model
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping("myaccount/gettradeDetails")
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_TRANS_DETAILS })
	public DataTablesResponseDto<TradeDetailsDto> getTradeDetailsList(
			HttpServletRequest request, Model model,
			@RequestBody TradeDetailsSearchDto query) {
		String userId = securityContext.getCurrentUserId();
		DataTablesResponseDto<TradeDetailsDto> result = tradeDetailsService
				.getTradeDetails(query, userId);
		result.setEcho(query.getEcho());
		return result;
	}

	/**
	 * 非签约会员提现申请页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "myaccount/withdrawApplyView", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY })
	public WithdrawApplDto withdrawApplView(HttpServletRequest request,
			HttpSession session, Model model) {
		String userId = securityContext.getCurrentUserId();
		BankAcct bankAcct = bankAcctService.findBankAcctByUserId(userId).get(0);
		String bankName = (null == bankAcct.getBnkCd() ? "" : SystemDictUtil
				.getDictByCategoryAndCode(EOptionCategory.BANK,
						bankAcct.getBnkCd()).getText());
		bankAcct.setBnkName(bankName);
		WithdrawApplDto dto = ConverterService.convert(bankAcct,
				WithdrawApplDto.class);
		StringBuilder bank = new StringBuilder();
		String province = SystemDictUtil.getDictByCategoryAndCode(
				EOptionCategory.REGION, bankAcct.getBnkOpenProv()).getText();
		String city = SystemDictUtil.getDictByCategoryAndCode(
				EOptionCategory.REGION, bankAcct.getBnkOpenCity()).getText();
		bank.append(province).append(" ").append(city).append(" ")
				.append(bankName).append(" ").append(bankAcct.getBnkBrch());
		dto.setBnkFullName(bank.toString());
		return dto;
	}

	/**
	 * 平台提现申请页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "myaccount/platformwithdrawApplyView", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_WITHDRAW })
	public WithdrawApplDto platformWithdrawApplView(HttpServletRequest request,
			HttpSession session, Model model) {
		String userId = CommonBusinessUtil.getExchangeUserId();
		BankAcct bankAcct = bankAcctService.findBankAcctByUserId(userId).get(0);
		String bankName = (null == bankAcct.getBnkCd() ? "" : SystemDictUtil
				.getDictByCategoryAndCode(EOptionCategory.BANK,
						bankAcct.getBnkCd()).getText());
		bankAcct.setBnkName(bankName);
		WithdrawApplDto dto = ConverterService.convert(bankAcct,
				WithdrawApplDto.class);
		StringBuilder bank = new StringBuilder();
		String province = SystemDictUtil.getDictByCategoryAndCode(
				EOptionCategory.REGION, bankAcct.getBnkOpenProv()).getText();
		String city = SystemDictUtil.getDictByCategoryAndCode(
				EOptionCategory.REGION, bankAcct.getBnkOpenCity()).getText();
		bank.append(province).append(" ").append(city).append(" ")
				.append(bankName).append(" ").append(bankAcct.getBnkBrch());
		dto.setBnkFullName(bank.toString());
		return dto;
	}

	/**
	 * 
	 * 非签约会员提现申请
	 * 
	 * @param request
	 * @param mode
	 * @param withDrawApplyDto
	 * @return
	 */
	@RequestMapping(value = "myaccount/withdrawApply", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY })
	public ResultDto withdrawApply(HttpServletRequest request, Model mode,
			@RequestBody WithDrawApplyDto applyDto) {
		User user = securityContext.getCurrentUser();
		UserWithdrawalReq req = new UserWithdrawalReq();
		req.setUserId(user.getUserId());
		req.setPassword(user.getPassword());
		req.setAmount(applyDto.getAmount());
		req.setRemFlg(null);
		req.setBnkAcctNo(applyDto.getBnkAcctNo());
		req.setCurrentUser(securityContext.getCurrentUserId());
		req.setCurrentDate(CommonBusinessUtil.getCurrentWorkDate());
		req.setBnkAcctName(applyDto.getBnkAcctName());
		/** Notice: this should be bank name rather than full name. **/
		req.setBnkOpenProv(applyDto.getBnkName());
		req.setMemo(applyDto.getMemo());
		userWithdrawalService.doUnSignUserWithdrawalOnPlat(req);
		return ResultDtoFactory.toAck("提交申请成功");
	}

	/**
	 * 
	 * 平台提现申请
	 * 
	 * @param request
	 * @param mode
	 * @param withDrawApplyDto
	 * @return
	 */
	@RequestMapping(value = "myaccount/platformwithdrawApply", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PLATFORM_UNIVERSAL_ACCOUNT_WITHDRAW })
	public ResultDto platformWithdrawApply(HttpServletRequest request,
			Model mode, @RequestBody WithDrawApplyDto applyDto) {
		UserPo user = userRepository.findUserByUserId(CommonBusinessUtil
				.getExchangeUserId());
		UserWithdrawalReq req = new UserWithdrawalReq();
		req.setUserId(user.getUserId());
		req.setPassword(user.getPassword());
		req.setAmount(applyDto.getAmount());
		req.setRemFlg(EFlagType.NO.getCode());
		req.setBnkAcctNo(applyDto.getBnkAcctNo());
		req.setCurrentUser(securityContext.getCurrentUserId());
		req.setCurrentDate(CommonBusinessUtil.getCurrentWorkDate());
		req.setBnkAcctName(applyDto.getBnkAcctName());
		req.setBnkOpenProv(applyDto.getBnkName());
		req.setMemo(applyDto.getMemo());
		userWithdrawalService.doUnSignUserWithdrawalOnPlat(req);
		return ResultDtoFactory.toAck("提交申请成功");
	}

	/**
	 * 非签约会员提现申请审批列表页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_WITHDRAW_APPLY_APPROVE_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public String withdrawDepositApplsApproveView(HttpServletRequest request,
			HttpSession session, Model model) {
		model.addAttribute(LiteralConstant.TYPE, DictConsts.TYPE_APPROVE);
		model.addAttribute("title", "提现申请审批");
		model.addAttribute("dealStatusOptions", false);
		model.addAttribute("applStatusOptions", true);
		model.addAttribute("applStatus",
				getStaticOptions(EFundApplStatus.class, false));
		List<DynamicOption> list = getDynamicOptions(EOptionCategory.BANK, true);
		list.set(0, new DynamicOption(EOptionCategory.BANK.getCode(), "", "全部"));
		model.addAttribute("bankList", list);
		model.addAttribute("cashPoolItems", getCashPoolItems());
		return "myaccount/withddepappls_view";
	}

	/**
	 * 非签约会员提现申请扣款确认列表页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_WITHDRAW_APPLY_CONFIRM_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM })
	public String withdrawDepositApplsConfirmView(HttpServletRequest request,
			HttpSession session, Model model) {
		model.addAttribute(LiteralConstant.TYPE, DictConsts.TYPE_CONFIRM);
		model.addAttribute("title", "提现申请确认");
		model.addAttribute("dealStatusOptions", true);
		model.addAttribute("applStatusOptions", false);
		List<EnumOption> dealStatusList = new ArrayList<EnumOption>();
		for (EFundDealStatus dealStatus : DictConsts.CONFIRM_OPTION) {
			dealStatusList.add(new EnumOption(dealStatus.name(), dealStatus
					.getText()));
		}
		model.addAttribute("dealStatus", dealStatusList);
		List<DynamicOption> list = getDynamicOptions(EOptionCategory.BANK, true);
		list.set(0, new DynamicOption(EOptionCategory.BANK.getCode(), "", "全部"));
		model.addAttribute("bankList", list);
		model.addAttribute("cashPoolItems", getCashPoolItems());
		return "myaccount/withddepappls_view";
	}

	/**
	 * 非签约会员提现申请列表页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "myaccount/withddepapplsview", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY_VIEW })
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	public String withdrawDepositApplsView(HttpServletRequest request,
			HttpSession session, Model model) {
		model.addAttribute(LiteralConstant.TYPE, DictConsts.TYPE_VIEW);
		model.addAttribute("title", "提现申请记录");
		model.addAttribute("dealStatusOptions", true);
		model.addAttribute("applStatusOptions", false);
		model.addAttribute("cashPoolItems", getCashPoolItems());
		model.addAttribute("dealStatus",
				getStaticOptions(EFundDealStatus.class, false));
		return "myaccount/withddepappls_view";
	}

	private List<EnumOption> getCashPoolItems() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		options.add(new EnumOption(ECashPool.ALL.name(), ECashPool.ALL
				.getText()));
		options.add(new EnumOption(ECashPool.CMB_COMMON.name(),
				ECashPool.CMB_COMMON.getText()));
		options.add(new EnumOption(ECashPool.CMB_SPECIAL.name(),
				ECashPool.CMB_SPECIAL.getText()));
		options.add(new EnumOption(ECashPool.ICBC_COMMON.name(),
				ECashPool.ICBC_COMMON.getText()));
		return options;
	}

	/**
	 * 平台提现申请列表页面
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "myaccount/platformwithddepapplsview", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY_VIEW })
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	public String platformWithdrawDepositApplsView(HttpServletRequest request,
			HttpSession session, Model model) {

		model.addAttribute(LiteralConstant.TYPE, DictConsts.TYPE_VIEW);
		model.addAttribute("title", "提现申请记录");
		model.addAttribute("dealStatusOptions", true);
		model.addAttribute("applStatusOptions", false);
		model.addAttribute("cashPoolItems", getCashPoolItems());
		model.addAttribute("dealStatus",
				getStaticOptions(EFundDealStatus.class, false));
		model.addAttribute("isExchangeWithdrawalAppl", true);
		return "myaccount/withddepappls_view";
	}

	/**
	 * 得到提现申请列表
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@RequestMapping("myaccount/withddepappllist")
	@ResponseBody
	@RequiresPermissions(value = {
			Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY_VIEW,
			Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM,
			Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE }, logical = Logical.OR)
	public DataTablesResponseDto<WithdrawalApplDetailDto> getWithdrawDepositApplList(
			HttpServletRequest request, Model model,
			@RequestBody WithdDepApplListSearchDto searchDto) {
		String userId = securityContext.getCurrentUserId();
		DataTablesResponseDto<WithdrawalApplDetailDto> result = new DataTablesResponseDto<WithdrawalApplDetailDto>();
		result.setEcho(searchDto.getEcho());
		boolean approve = securityContext.canApproveWithdrawDepositAppl();
		boolean view = securityContext.canViewWithdrawDepositApplTable();
		boolean confirm = securityContext.canConfirmWithdrawDepositAppl();
		if (view && !approve && !confirm) {
			result = userWithdrawalService.getWithdrawDepositApplList(userId,
					searchDto, approve, confirm);
		}
		if (confirm) {
			result = getApplList(searchDto, approve, confirm);
		}
		if (approve) {
			result = getApplList(searchDto, approve, confirm);
		}
		return result;
	}

	/**
	 * 得到提现申请列表汇总金额.
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@RequestMapping("myaccount/withddepapplsumamt")
	@ResponseBody
	@RequiresPermissions(value = {
			Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM,
			Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE }, logical = Logical.OR)
	public WithdrawalApplSumAmtDto getWithdrawSumAmt(
			HttpServletRequest request, Model model,
			@RequestBody WithdDepApplListSearchDto searchDto) {
		boolean approve = securityContext.canApproveWithdrawDepositAppl();
		boolean confirm = securityContext.canConfirmWithdrawDepositAppl();
		WithdrawalApplSumAmtDto withdrawApplSumAmtDto = userWithdrawalService
				.getWithdrawApplSumAmtDto(null, searchDto, approve, confirm);
		return withdrawApplSumAmtDto;
	}

	/**
	 * 
	 * 
	 * @param searchDto
	 * @param approve
	 * @param confirm
	 * @return
	 */

	private DataTablesResponseDto<WithdrawalApplDetailDto> getApplList(
			WithdDepApplListSearchDto searchDto, boolean approve,
			boolean confirm) {
		DataTablesResponseDto<WithdrawalApplDetailDto> result;
		result = userWithdrawalService.getWithdrawDepositApplList(null,
				searchDto, approve, confirm);
		return result;
	}

	/**
	 * 得到平台提现申请列表.
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@RequestMapping("myaccount/platformwithddepappllist")
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY_VIEW }, logical = Logical.OR)
	public DataTablesResponseDto<WithdrawalApplDetailDto> getPlatfomWithdrawDepositApplList(
			HttpServletRequest request, Model model,
			@RequestBody WithdDepApplListSearchDto searchDto) {
		String userId = CommonBusinessUtil.getExchangeUserId();
		DataTablesResponseDto<WithdrawalApplDetailDto> result = new DataTablesResponseDto<WithdrawalApplDetailDto>();
		result.setEcho(searchDto.getEcho());
		result = userWithdrawalService.getWithdrawDepositApplList(userId,
				searchDto, false, false);
		return result;
	}

	/**
	 * 提现申请表审批.
	 * 
	 * @param request
	 * @param mode
	 * @param transferDto
	 * @return
	 */
	@RequestMapping(value = "myaccount/withddepapplapprove", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE })
	public ResultDto withddepapplApprove(HttpServletRequest request,
			Model mode, @RequestBody FundApplApproveDto dto) {
		String userId = securityContext.getCurrentUserId();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		userWithdrawalService.approvalWithdrawDepositAppl(dto.getAppId(),
				dto.isPassed(), dto.getComments(), userId, workDate);
		return ResultDtoFactory.toAck("审批成功");
	}

	/**
	 * 确定对非签约用户放款.
	 * 
	 * @param request
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "myaccount/withdrawalafterconfirm", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM })
	public ResultDto withdrawalAfterConfirm(HttpServletRequest request,
			Model mode, @RequestBody WithdConfirmDto dto) {
		String userId = securityContext.getCurrentUserId();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		userWithdrawalService.doUnSignUserWithdrawalAfterApprove(
				dto.getApplId(), userId, workDate);
		WithdrawDepositApplPo applPo = withdrawDepositApplRepository
				.findByApplNo(dto.getApplId());
		sendWithdrawalMessage(applPo.getUserId(), applPo.getTrxAmt());
		return ResultDtoFactory.toAck("放款成功");
	}

	/**
	 * 驳回对非签约用户放款.
	 * 
	 * @param request
	 * @param mode
	 * @return
	 */
	@RequestMapping(value = "myaccount/withdrawalreject", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM })
	public ResultDto withdrawalReject(HttpServletRequest request, Model mode,
			@RequestBody WithdConfirmDto dto) {
		String userId = securityContext.getCurrentUserId();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		userWithdrawalService.doUnSignUserWithdrawalReject(dto, userId,
				workDate);
		WithdrawDepositApplPo applPo = withdrawDepositApplRepository
				.findByApplNo(dto.getApplId());
		sendWithdrawalRejectMessage(applPo.getUserId(), applPo.getTrxAmt());
		return ResultDtoFactory.toAck("放款申请已驳回");
	}

}
