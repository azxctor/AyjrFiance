/*
 * Project Name: kmfex-platform
 * File Name: UserController.java
 * Class Name: UserController
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
package com.hengxin.platform.app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.hengxin.platform.account.dto.AccountDetailsDto;
import com.hengxin.platform.account.dto.InvestBenifitDto;
import com.hengxin.platform.account.dto.downstream.AccountDetailsSearchDto;
import com.hengxin.platform.account.dto.upstream.AccountOverviewSearchDto;
import com.hengxin.platform.account.dto.upstream.BenifitDto;
import com.hengxin.platform.account.service.AccountDetailsService;
import com.hengxin.platform.account.service.AccountOverviewService;
import com.hengxin.platform.app.dto.downstream.AccountBrowsingDto;
import com.hengxin.platform.app.dto.downstream.AccountBrowsingV2DownDto;
import com.hengxin.platform.app.dto.downstream.AccountBrowsingV3DownDto;
import com.hengxin.platform.app.dto.downstream.AccountDetailListDownstreamDto;
import com.hengxin.platform.app.dto.downstream.AccountDetailTypeDto;
import com.hengxin.platform.app.dto.downstream.AssetDto;
import com.hengxin.platform.app.dto.downstream.BuildingDto;
import com.hengxin.platform.app.dto.downstream.CarDto;
import com.hengxin.platform.app.dto.downstream.ChattelMortgageDto;
import com.hengxin.platform.app.dto.downstream.DetailListDto;
import com.hengxin.platform.app.dto.downstream.FicoScoreDto;
import com.hengxin.platform.app.dto.downstream.FinancingItemDto;
import com.hengxin.platform.app.dto.downstream.InvestBenefitDto;
import com.hengxin.platform.app.dto.downstream.MicroFinanceBaoDto;
import com.hengxin.platform.app.dto.downstream.MyInvestOverviewDownDto;
import com.hengxin.platform.app.dto.downstream.PersonalAssetDto;
import com.hengxin.platform.app.dto.downstream.PersonalLiabilityDto;
import com.hengxin.platform.app.dto.downstream.SignResultDownDto;
import com.hengxin.platform.app.dto.downstream.SignResultDownDtoV3;
import com.hengxin.platform.app.dto.downstream.UserInfoDto;
import com.hengxin.platform.app.dto.downstream.UserInfoV2DownDto;
import com.hengxin.platform.app.dto.downstream.WarrantorDto;
import com.hengxin.platform.app.dto.upstream.AccountDetailListDto;
import com.hengxin.platform.app.dto.upstream.AccountDetailListV2UpDto;
import com.hengxin.platform.app.dto.upstream.LoginDto;
import com.hengxin.platform.app.dto.upstream.NickNameUpDto;
import com.hengxin.platform.app.dto.upstream.PasswordV2UpDto;
import com.hengxin.platform.app.dto.upstream.ProductDto;
import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.velocity.NumberTool;
import com.hengxin.platform.common.validation.ValidateException;
import com.hengxin.platform.escrow.dto.EswAcctDto;
import com.hengxin.platform.escrow.service.EswAccountService;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.InvestorProfitSummaryService;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.downstream.FinancingMarketItemDetailDto;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.market.service.PackageSubscirbeService;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.converter.MemberInfoConverter;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.dto.MemberDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PasswordDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EInvestorLevel;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.controller.FinancingPackageListController;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.domain.User.NickNameGroup;
import com.hengxin.platform.security.service.UserService;

/**
 * 用户相关控制器.
 * 
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private AccountOverviewService accountOverviewService;
	@Autowired
	private AccountDetailsService accountDetailsService;

	@Autowired
	private AcctService acctService;

	@Autowired
	private UserService userService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private transient FinancingResourceService resourceService;

	@Autowired
	private transient FinancingMarketService financingMarketService;

	@Autowired
	private transient FundAcctBalService fundAccoutService;

	@Autowired
	private transient PackageSubscirbeService subcribeService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private FinancingPackageService financingPackageService;
	
	@Autowired
	private InvestorProfitSummaryService investorProfitSummaryService;

	@Autowired
	private EswAccountService eswAccountService; 

	// inject web controller
	private FinancingPackageListController financingPackageListController;
	
	@PostConstruct
	public void init() {
		LOGGER.debug("init financingPackageListController");
		financingPackageListController = new FinancingPackageListController();
		financingPackageListController.setProductService(productService);
		financingPackageListController
				.setFinancingPackageService(financingPackageService);
		financingPackageListController.setSecurityContext(securityContext);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto doAppSignin(@OnValid @RequestBody LoginDto loginDto,
			BindingResult result) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppSignin() start: ");
		}
		try {
			SecurityContext.login(loginDto.getUserName(),
					loginDto.getPassword());
			if (!securityContext.isInvestor()) {
				SecurityContext.logout();
				return ResultDtoFactory.toNack(MessageUtil
						.getMessage("app.error.not.investor"));
			}
		} catch (LockedAccountException e1) {
			result.rejectValue(LiteralConstant.PASSWORD,
					"member.error.password.toomanyfailure");
		} catch (IncorrectCredentialsException e1) {
			result.rejectValue(LiteralConstant.PASSWORD, "member.error.signin.fail");
		} catch (UnknownAccountException e1) {
			result.rejectValue(LiteralConstant.USERNAME, "member.error.not_exist");
		} catch (DisabledAccountException e1) {
			result.rejectValue(LiteralConstant.USERNAME, "member.error.username.status");
        }
		if (result.hasErrors()) {
			throw new ValidateException(result);
		}
		return ResultDtoFactory.toAck("登陆成功");
	}

	/*
	 * 资产及收益
	 * 
	 * @author = Tom
	 */
	@RequestMapping(value = "/asset", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_DEPOSITE,
			Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW,
			Permissions.MY_ACCOUNT_OVERVIEW_ESTIMATED_INCOME }, logical = Logical.OR)
	public ResultDto doAppAsset() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAsset() start: ");
		}
		AssetDto data = new AssetDto();
		NumberTool tool = new NumberTool();
		User user = securityContext.getCurrentUser();
		AccountOverviewSearchDto aDto = accountOverviewService
				.getAccountOverviewDetailInfo(user.getUserId());
		if (securityContext.isInvestor()) {
			if (securityContext.isFinancier()) {
				data.setMemberType(EMemberType.INVESTOR_FINANCER.getText());
			} else {
				data.setMemberType(EMemberType.INVESTOR.getText());
			}
		} else if (securityContext.isFinancier()) {
			data.setMemberType(EMemberType.FINANCER.getText());
		}
		data.setUserName(aDto.getUserInfo().getUsername());
		data.setAccountNum(aDto.getUserInfo().getAcctNo());
		data.setTotalAsset(tool.formatMoney(aDto.getCurrentAccount().getBal()));
		data.setAvailableAsset(aDto.getCurrentAccount().getAvailableBal());
		data.setCashableAmt(aDto.getCurrentAccount().getCashableAmt());
		return ResultDtoFactory.toAck("获取成功", data);
	}

	/*
	 * 账户总览
	 * 
	 * @author = Tom
	 */
	@RequestMapping(value = "/accountBrowsing", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_DEPOSITE,
			Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW,
			Permissions.MY_ACCOUNT_OVERVIEW_ESTIMATED_INCOME }, logical = Logical.OR)
	public ResultDto doAppAccountBrowsing() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAccountBrowsing() start: ");
		}
		NumberTool tool = new NumberTool();
		AccountBrowsingDto data = new AccountBrowsingDto();
		User user = securityContext.getCurrentUser();
		AccountOverviewSearchDto aDto = accountOverviewService
				.getAccountOverviewDetailInfo(user.getUserId());
		BenifitDto bDto = accountOverviewService.getBenifitDetails(user
				.getUserId());
		data.setTotalAsset(tool.formatMoney(aDto.getCurrentAccount().getBal()));
		data.setAvailableAsset(aDto.getCurrentAccount().getAvailableBal());
		data.setRecharge(aDto.getAccountOverview().getRechargeSum());
		data.setWithdrawal(aDto.getAccountOverview().getWithdrawSum());
		data.setCurrentSettlement(aDto.getAccountOverview().getInterestSum());
		data.setInvestment(aDto.getAccountOverview().getInvestSum());
		data.setFinancingRepayment(aDto.getAccountOverview().getFinanceSum());
		List<InvestBenefitDto> result = new ArrayList<InvestBenefitDto>();
		if (bDto.getInvestBenifit() != null
				&& !bDto.getInvestBenifit().isEmpty()) {
			for (InvestBenifitDto d : bDto.getInvestBenifit()) {
				InvestBenefitDto investBenefitDto = new InvestBenefitDto();
				if (d != null) {
					investBenefitDto.setInvestBenefit(d.getInvestBenifit());
					investBenefitDto.setWeek(d.getWeek());
					result.add(investBenefitDto);
				}
			}
		}
		data.setInvestBenefit(result.subList(result.size() - 5, result.size()));
		return ResultDtoFactory.toAck("获取成功", data);
	}

	/*
	 * 小微宝
	 * 
	 * @author = Tom
	 */
	@RequestMapping(value = "/microFinanceBao", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_XWB })
	public ResultDto doAppMicroFinanceBao() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppMicroFinanceBao() start: ");
		}
		MicroFinanceBaoDto data = new MicroFinanceBaoDto();
		User user = securityContext.getCurrentUser();
		AccountOverviewSearchDto aDto = accountOverviewService
				.getAccountOverviewDetailInfo(user.getUserId());
		data.setTotalAsset(aDto.getXwbAccount().getBal().toString());
		data.setYesterdayProfit(aDto.getXwbAccount().getEarningsYesterday()
				.toString());
		data.setAccumulateProfit(aDto.getXwbAccount().getAccumCrAmt()
				.toString());
		data.setAnnualProfitRate(aDto.getXwbAccount().getRate().toString());
		return ResultDtoFactory.toAck("获取成功", data);
	}

	/*
	 * 账户明细
	 * 
	 * @author Tom
	 */
	@RequestMapping(value = "/accountDetailList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_ACCOUNT_DETAILS })
	public ResultDto doAppAccountDetailList(
			@OnValid @RequestBody AccountDetailListDto accountDetailListDto) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAccountDetailList() start: ");
		}
		AccountDetailListDownstreamDto data = new AccountDetailListDownstreamDto();
		User user = securityContext.getCurrentUser();
		NumberTool tool = new NumberTool();
		AccountDetailsSearchDto searchDto = new AccountDetailsSearchDto();
		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("trxDt");
		searchDto.setSortedColumns(aiSortCol);
		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
		searchDto.setDisplayLength(20);
		searchDto.setDisplayStart(Integer.parseInt(accountDetailListDto
				.getDisplayStart()));
		searchDto.setCreateDate(new Date(accountDetailListDto.getCreateTime()));
		if (accountDetailListDto.getStartTime() != null
				|| accountDetailListDto.getEndTime() != null) {
			searchDto.setFromDate(accountDetailListDto.getStartTime());
			searchDto.setToDate(accountDetailListDto.getEndTime());
		}
		if (accountDetailListDto.getType() == null) {
			searchDto.setUseType(EFundUseType.INOUTALL);
		} else {
			searchDto.setUseType(EnumHelper.translate(EFundUseType.class,
					accountDetailListDto.getType()));
		}
		DataTablesResponseDto<AccountDetailsDto> accountdetailDto = accountDetailsService
				.getAccountDetails(searchDto, user.getUserId());
		List<DetailListDto> result = new ArrayList<DetailListDto>();
		if (accountdetailDto.getData() != null) {
			for (AccountDetailsDto d : accountdetailDto.getData()) {
				DetailListDto dto = new DetailListDto();
				dto.setAmount(tool.formatMoney(d.getTrxAmt()));
				dto.setRemarks(d.getTrxMemo());
				dto.setType(d.getUseType().getText());
				dto.setTime(d.getTrxDt().toString());
				dto.setPayRecvFlg(d.getPayRecvFlg());
				result.add(dto);
			}
		}
		data.setDetailList(result);
		return ResultDtoFactory.toAck("获取成功", data);
	}

	/*
	 * 查看用户信息接口
	 * 
	 * @author = fangwei
	 */
	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultDto userInfo() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("userInfo() start: ");
		}
		UserInfoDto data = new UserInfoDto();
		User user = securityContext.getCurrentUser();
		String userId = securityContext.getCurrentUserId();
		MemberApplication memberPo = memberService
				.getMemberWithLatestStatus(userId);

		MemberDto memberDto = new MemberDto();
		if (memberPo != null) {
			Class<? extends MemberDto> clazz = memberPo.getUserType() == EUserType.PERSON ? PersonDto.class
					: OrganizationDto.class;

			// 拒绝状态下取原信息，原信息为空，表示第一次提交，需要将拒绝信息pop到add页面
			if (memberPo.getStatus() == EApplicationStatus.REJECT) {
				Member oldMember = memberService.getMemberByUserId(userId);
				if (oldMember != null) {
					memberDto = ConverterService.convert(oldMember, clazz,
							MemberInfoConverter.class);
					memberDto.setUserType(securityContext.getCurrentUser()
							.getType());
					memberDto.setStatus(EApplicationStatus.ACCEPT);
				} else {
					memberDto = ConverterService.convert(memberPo, clazz);
					memberDto.setStatus(EApplicationStatus.UNCOMMITTED);
				}
				memberDto.setRejectLastTime(true);
				memberDto.setComments(memberPo.getComments());
			} else {
				memberDto = ConverterService.convert(memberPo, clazz);
			}

			// 修改的pending状态需要看到原信息
			if (memberPo.getStatus() == EApplicationStatus.PENDDING) {
				Member oldMember = memberService.getMemberByUserId(userId);
				if (oldMember != null) {
					memberDto.setOldMember(ConverterService.convert(oldMember,
							clazz, MemberInfoConverter.class));
				}
			}
			// 取账号信息
			AcctPo acctPo = acctService.getAcctByUserId(userId);
			if (acctPo != null) {
				AccountDto accountDto = ConverterService.convert(acctPo,
						AccountDto.class);
				memberDto.setAccount(accountDto);
				// 账号信息set到原信息中
				if (memberDto.getOldMember() != null) {
					memberDto.getOldMember().setAccount(accountDto);
				}
			}

		} else {
			// 第一次补全时email和mobile应该是注册时填入的
			memberDto.setEmail(user.getEmail());
			memberDto.setMobile(user.getMobile());
		}

		if (securityContext.isInvestor()) {
			if (securityContext.isFinancier()) {
				memberDto.setMemberType(EMemberType.INVESTOR_FINANCER);
			} else {
				memberDto.setMemberType(EMemberType.INVESTOR);
			}
		} else if (securityContext.isFinancier()) {
			memberDto.setMemberType(EMemberType.FINANCER);
		}

		Member member = memberService.getMemberByUserId(userId);

		/** 20150313 安益手机端，因为银行卡信息在新增的托管账户表中，所以需要做更改 **/
		AcctPo acct = acctService.getAcctByUserId(userId);
		String bankAcctName = null;
		String bankAcctNum = null;
		String bankName = null;
		if(null != acct){
			String acctNo = acct.getAcctNo();
			EswAcctDto eswAcctDto = eswAccountService.findByAcctNo(acctNo);
			if(null != eswAcctDto){
				bankAcctName = eswAcctDto.getBankCardName();
				bankAcctNum = eswAcctDto.getBankCardNo();
				bankName = eswAcctDto.getBankName();
			}
		}

		if (member != null) {
			data.setIdCardNum(member.getPersonIdCardNumber());
		}
		if (user != null) {
			data.setPhoneNum(user.getMobile());
			data.setEmail(user.getEmail());
			data.setUserName(user.getName());
		}
		if (memberDto != null) {
			data.setCategory(memberDto.getUserType() == null ? null : memberDto
					.getUserType().getText());
			data.setMemberType(memberDto.getMemberType() == null ? null
					: memberDto.getMemberType().getText());
/** 
			data.setBankAccountName(memberDto.getMaskBankAccountName());
			data.setBankAccountNum(memberDto.getMakBankAccount());
			
			String province = memberDto.getBankOpenProvince() == null ? ""
					: memberDto.getBankOpenProvince().getText();
			String city = memberDto.getBankOpenCity() == null ? ""
					: memberDto.getBankOpenCity().getText();
			String shortname = memberDto.getBankShortName() == null ? ""
					: memberDto.getBankShortName().getText();
			String branch = memberDto.getBankOpenBranch() == null ? ""
					: memberDto.getBankOpenBranch();
		
			data.setBankName(province + city + shortname + branch);
**/

			/** 20150313 安益手机端更改 **/
			data.setBankAccountName(bankAcctName);
			data.setBankAccountNum(bankAcctNum);
			data.setBankName(bankName);

			data.setAccountNum(memberDto.getAccount() == null ? null
					: memberDto.getAccount().getAcctNo());
			data.setSetUpTime(memberDto.getAccount() == null ? null : memberDto
					.getAccount().getCreateTs());
		}
		return ResultDtoFactory.toAck("获取成功", data);
	}

	/*
	 * 融资宝详情接口
	 */
	@Deprecated
	@RequestMapping(value = "/productDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto getDetailPage(@OnValid @RequestBody ProductDto productDto, HttpServletRequest request) {
        LOGGER.debug("getDetailPage() invoked");
        FinancingMarketItemDetailDto itemDto = getItemBaseInfo(productDto.getProductId());
        ProductPackage pkg = financingMarketService.getPackageDetail(productDto.getProductId());
        if (pkg != null) {
            itemDto.setPkg(ConverterService.convert(pkg, ProductPackageDto.class));
            ProductDetailsDto productDetail = financingMarketService.getProductDetail(pkg.getProductId());
            if (productDetail.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productDetail.getWarrantyType() == EWarrantyType.NOTHING) {
                productDetail.setGuaranteeInstitution("--");
            }
            itemDto.setProduct(productDetail);
        }
        
        NumberTool numberTool = new NumberTool();
        
        FinancingItemDto financingItemDto = new FinancingItemDto();
        
        if(itemDto!=null){
        	if(itemDto.getPkg()!=null) {
        		financingItemDto.setPackageName("NO."+itemDto.getPkg().getId()+" "+itemDto.getPkg().getPackageName());
        		financingItemDto.setState(itemDto.getPkg().getStatus()==null?null:itemDto.getPkg().getStatus().getText());
        		financingItemDto.setStateCode(itemDto.getPkg().getStatus()==null?null:itemDto.getPkg().getStatus().getCode());
        		financingItemDto.setProgress(numberTool.formatMoney(itemDto.getProgress())+"%");
        		
        		financingItemDto.setEndTime(itemDto.getPkg().getSupplyEndTime());
        		
        		financingItemDto.setStartTime(itemDto.getPkg().getSupplyStartTime());
        		financingItemDto.setFinancedAmount(numberTool.formatMoney(itemDto.getPkg().getPackageQuota()));
        	}
	        
	        FicoScoreDto ficoScoreDto = new FicoScoreDto();
	    	ficoScoreDto.setRiskLevelCode(itemDto.getRiskLevel()==null?null:itemDto.getRiskLevel().getCode());
	        financingItemDto.setFicoScoreDto(ficoScoreDto);
	        
	        if(itemDto.getProduct()!=null) {
	        	
	        	financingItemDto.setRiskSafeguard(itemDto.getProduct().getWarrantyType()==null?null:itemDto.getProduct().getWarrantyType().getText());;
	        	if(itemDto.getProduct().getRatePercent()!=null){
	        		financingItemDto.setAnnualProfit(itemDto.getProduct().getRatePercent()[0]+"."+itemDto.getProduct().getRatePercent()[1]+"%");
	        	}
	        	financingItemDto.setWrtrCreditDesc(itemDto.getProduct().getWrtrCreditDesc()); // 公司简介
	        	financingItemDto.setLimitTime(itemDto.getProduct().getTermLength()+(itemDto.getProduct().getTermType()==null?null:itemDto.getProduct().getTermType().getText()));
	        	financingItemDto.setFinancier(itemDto.getProduct().getUser()==null?null:itemDto.getProduct().getUser().getMaskName());
	        	financingItemDto.setRepaymentWay(itemDto.getProduct().getPayMethod()==null?null:itemDto.getProduct().getPayMethod().getText());
	        	financingItemDto.setIndustry(itemDto.getProduct().getFinancierIndustryType()==null?null:itemDto.getProduct().getFinancierIndustryType().getText());
	        	financingItemDto.setFinancePurpose(itemDto.getProduct().getLoanPurpose());
	 	        financingItemDto.setGuaranteeInstitution(HtmlUtils.htmlUnescape(itemDto.getProduct().getGuaranteeInstitution()));
				if (itemDto.getProduct().getWarrantyType() != EWarrantyType.MONITORASSETS
						&& itemDto.getProduct().getWarrantyType() != EWarrantyType.NOTHING
						&& StringUtils.isNotBlank(itemDto.getProduct()
								.getWarrantId())) {
					StringBuilder url = new StringBuilder();
					if (AppConfigUtil.getUploadFileURL().startsWith("/")) {
						url.append(request.getScheme()).append("://")
								.append(request.getServerName());
						if (request.getServerPort() != LiteralConstant.PORT_80
								&& request.getServerPort() != LiteralConstant.PORT_443) {
							url.append(":").append(request.getServerPort());
						}
					}
					url.append(AppConfigUtil.getUploadFileURL());
					Agency agency = memberService.getAgencyByUserId(itemDto
							.getProduct().getWarrantId());
					if (agency != null
							&& StringUtils.isNotBlank(agency.getLicenceNoImg())) {
						financingItemDto.setGuaranteeLicenseNoImg(url.append(
								agency.getLicenceNoImg()).toString());
						financingItemDto.setGuaranteeLicenseNoImgName(agency
								.getLicenceNoImg());
					}
				}
		    	
		    	BigDecimal productScore = BigDecimal.ZERO;
		    	switch (itemDto.getProduct().getProductLevel()) {
		    	case HIGH_QUALITY:
		    		productScore = BigDecimal.valueOf(4);
		    		break;
		    	case MEDIUM:
		    		productScore = BigDecimal.valueOf(3);
		    		break;
		    	case ACCEPTABLE:
		    		productScore = BigDecimal.valueOf(2);
		    		break;
		    	case HIGH_RISK:
		    		productScore = BigDecimal.ONE;
		    		break;
		    	default:
		    		
		    	}
		    	BigDecimal financierScore = new BigDecimal(NumberUtils.toDouble(itemDto.getProduct().getFinancierLevel())).divide(new BigDecimal(50), 2, RoundingMode.DOWN);
		    	BigDecimal warratorScore = new BigDecimal(NumberUtils.toDouble(itemDto.getProduct().getWarrantorLevel())).divide(new BigDecimal(25), 1, RoundingMode.DOWN);
		    	ficoScoreDto.setProductGrage(subZeroAndDot(numberTool.formatMoney(productScore)));
		    	ficoScoreDto.setFinancierGrage(subZeroAndDot(numberTool.formatMoney(financierScore)));
		    	ficoScoreDto.setWarrantGrage(subZeroAndDot(numberTool.formatMoney(warratorScore)));
	        	
	        	List<BuildingDto> buildingList = new ArrayList<BuildingDto>();
	        	if(itemDto.getProduct().getProductMortgageResidentialDetailsDtoList()!=null&&itemDto.getProduct().getProductMortgageResidentialDetailsDtoList().size()>0) {
	        		for(ProductMortgageResidentialDetailsDto detailDto :itemDto.getProduct().getProductMortgageResidentialDetailsDtoList()){
	        			BuildingDto buildingDto = new BuildingDto();
	        			buildingDto.setArea(numberTool.formatMoney(detailDto.getArea()));
	        			buildingDto.setPurchasePrice(numberTool.formatMoney(detailDto.getPurchasePrice()));
	        			buildingDto.setEvaluatePrice(numberTool.formatMoney(detailDto.getEvaluatePrice()));
	        			buildingDto.setMarketPrice(numberTool.formatMoney(detailDto.getMarketPrice()));
	        			buildingList.add(buildingDto);
	        		}
	        	}
	        	financingItemDto.setBuildingList(buildingList);
	        	
	        	
	        	List<CarDto> carList = new ArrayList<CarDto>();
	        	if(itemDto.getProduct().getProductMortgageVehicleDetailsDtoList()!=null&&itemDto.getProduct().getProductMortgageVehicleDetailsDtoList().size()>0){
	        		for(ProductMortgageVehicleDetailsDto detailDto :itemDto.getProduct().getProductMortgageVehicleDetailsDtoList()){
	        			CarDto carDto = new CarDto();
	        			carDto.setBrand(detailDto.getBrand());
	        			carDto.setType(detailDto.getType());
	        			carList.add(carDto);
	        		}    
	        	}
	        	financingItemDto.setCarList(carList);
	        	
	        	List<ChattelMortgageDto> chattelMortgageList = new ArrayList<ChattelMortgageDto>();
	        	if(itemDto.getProduct().getProductPledgeDetailsDtoList()!=null&&itemDto.getProduct().getProductPledgeDetailsDtoList().size()>0) {
	        		for(ProductPledgeDetailsDto detailDto :itemDto.getProduct().getProductPledgeDetailsDtoList()){
	        			ChattelMortgageDto chattelMortgageDto = new ChattelMortgageDto();
	        			chattelMortgageDto.setName(detailDto.getName());
	        			chattelMortgageDto.setPledgeClass(detailDto.getPledgeClass());
	        			chattelMortgageDto.setType(detailDto.getType());
	        			chattelMortgageDto.setCount(detailDto.getCount()==null?"0":String.valueOf(detailDto.getCount()));
	        			chattelMortgageDto.setPrice(numberTool.formatMoney(detailDto.getPrice()));
	        			chattelMortgageDto.setLocation(detailDto.getLocation());
	        			chattelMortgageList.add(chattelMortgageDto);
	        		} 
	        	}
	        	financingItemDto.setChattelMortgageList(chattelMortgageList);
	        	
	        	List<PersonalAssetDto> personalAssetList = new ArrayList<PersonalAssetDto>();
	        	if(itemDto.getProduct().getProductAssetDtoList()!=null&&itemDto.getProduct().getProductAssetDtoList().size()>0) {
	        		for(ProductAssetDto detailDto :itemDto.getProduct().getProductAssetDtoList()){
	        			PersonalAssetDto personalAssetDto = new PersonalAssetDto();
	        			personalAssetDto.setAssertAmount(numberTool.formatMoney(detailDto.getAssertAmount()));
	        			personalAssetDto.setDtype(detailDto.getDtype()==null?null:detailDto.getDtype().getText());
	        			personalAssetDto.setNotes(detailDto.getNotes());
	        			personalAssetList.add(personalAssetDto);
	        		} 
	        	}
		        financingItemDto.setPersonalAssetList(personalAssetList);
		        
		        List<PersonalLiabilityDto> personalLiabilityList = new ArrayList<PersonalLiabilityDto>();
		        if(itemDto.getProduct().getProductDebtDtoList()!=null&&itemDto.getProduct().getProductDebtDtoList().size()>0) {
		        	for(ProductDebtDto detailDto :itemDto.getProduct().getProductDebtDtoList()){
		        		PersonalLiabilityDto personalLiabilityDto = new PersonalLiabilityDto();
		        		personalLiabilityDto.setDtype(detailDto.getDtype()==null?null:detailDto.getDtype().getText());
		        		personalLiabilityDto.setDebtAmount(numberTool.formatMoney(detailDto.getDebtAmount()));
		        		personalLiabilityDto.setMonthlyPayment(numberTool.formatMoney(detailDto.getMonthlyPayment()));
		        		personalLiabilityDto.setNotes(detailDto.getNotes());
		        		personalLiabilityList.add(personalLiabilityDto);
		        	} 
		        }
		        financingItemDto.setPersonalLiabilityList(personalLiabilityList);
	        }
	        
	        WarrantorDto warrantorDto = new WarrantorDto();
	    	warrantorDto.setWarrantEnterpriseStr(itemDto.getWarrantEnterpriseStr());
	    	warrantorDto.setWarrantPersonStr(itemDto.getWarrantPersonStr());
	        financingItemDto.setWarrantorDto(warrantorDto);
	        
        }
    	return ResultDtoFactory.toAck("获取成功",financingItemDto);
    }

	public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    } 
	
	private FinancingMarketItemDetailDto getItemBaseInfo(String id) {
		ProductPackage item = financingMarketService.getPackageDetail(id);
		String userId = securityContext.getCurrentUserId();
		FinancingMarketItemDetailDto resp = subcribeService
				.getLatestSubcribeAmount(id, userId);
		ProductDetailsDto product = new ProductDetailsDto();
		try {
			resp.setBalance(fundAccoutService.getUserCurrentAcctAvlBal(userId,
					true));
			if (item != null) {
				resp.setRemainingTime(item.getSupplyEndTime().getTime()
						- System.currentTimeMillis());
				resp.setRemainingStartTime(item.getSupplyStartTime().getTime()
						- System.currentTimeMillis());

				BigDecimal progress = item.getSupplyAmount().divide(
						item.getPackageQuota(), 3, RoundingMode.DOWN);
				if (item.getStatus() == EPackageStatus.SUBSCRIBE) {
					MarketItemDto itemCache = resourceService.getItem(item
							.getId());
					if (itemCache == null) {
						LOGGER.warn("package does NOT exists in cache: {}",
								item.getId());
					} else {
						progress = itemCache.getProgress();
					}
				}
				resp.setProgress(SubscribeUtils.formatSubsProgress(progress));
				product.setRate(item.getProduct().getRate());
				product.setTermToDays(item.getProduct().getTermToDays());
				product.setPayMethod(item.getProduct().getPayMethod());
				product.setTermLength(item.getProduct().getTermLength());
				if (item.getStatus() == EPackageStatus.WAIT_SUBSCRIBE) {
					resp.setSubscribed(false);
					resp.setReason(MessageUtil
							.getMessage("market.error.subsribe.notstart"));
				}
				if (item.getStatus() == EPackageStatus.WAIT_SIGN) {
					resp.setSubscribed(false);
					resp.setReason(MessageUtil
							.getMessage("market.error.subscribe.haspassed"));
				}
				// 不能申购自己发布的融资包
				if (securityContext.getCurrentUserId().equalsIgnoreCase(
						item.getProduct().getUser().getUserId())) {
					resp.setSubscribed(false);
					resp.setReason(MessageUtil
							.getMessage("market.error.subscirbe.youown"));
				}
			} else {
				LOGGER.warn("Financing market item not found: " + id);
			}
			if (resp.getBalance().compareTo(resp.getMinSubscribeAmount()) < 0) {
				resp.setSubscribed(false);
				resp.setReason(MessageFormat.format(
						MessageUtil.getMessage("market.error.balance.lack2"),
						resp.getMinSubscribeAmount().setScale(2, RoundingMode.DOWN).toString()));
			}
		} catch (BizServiceException ex) {
			resp.setSubscribed(false);
			resp.setReason(ex.getMessage());
		}

		if (!securityContext.isInvestor()) {
			resp.setSubscribed(false);
			resp.setReason(MessageUtil
					.getMessage("market.error.subscribe.permissions.nothave"));
		} else if (!CommonBusinessUtil.isMarketOpen()) {
			resp.setSubscribed(false);
			resp.setReason(MessageUtil.getMessage("market.error.market.close"));
		}
		resp.setProduct(product);
		return resp;
	}

	@RequestMapping(value = "/accountDetailType", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_ACCOUNT_DETAILS })
	public ResultDto doAppAccountDetailType() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAccountDetailType() start: ");
		}
		AccountDetailTypeDto data = new AccountDetailTypeDto();
		List<EFundUseType> result = new ArrayList<EFundUseType>();
		
		result.add(EFundUseType.INOUTALL);
		
		List<EFundUseType> filterDicts = accountDetailsService.getFilterDictsByRoles();
		Iterator<EFundUseType> iter = filterDicts.iterator();
		while(iter.hasNext()){
			EFundUseType type = iter.next();
			if(type==null) continue;
			result.add(type);
		}
		data.setDetailType(result);
		return ResultDtoFactory.toAck("获取成功", data);
	}
	
	
	/***************/
	/* 二期新增接口 */
	/***************/
	
	/**
	 * 二期登录接口.
	 * 
	 * @param loginDto
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/login2", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto doAppSigninV2(@OnValid @RequestBody LoginDto loginDto,
			BindingResult result) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppSignin() start: ");
		}
		SignResultDownDto signResultDownDto = null;
		try {
			SecurityContext.login(loginDto.getUserName(),
					loginDto.getPassword());
			// 既没有投资人权限，也没融资人权限	或者		在生成环境中的融资人
			if (!securityContext.isInvestor()
					&& !securityContext.isFinancier()) { // 第二期 :融资人登录
				SecurityContext.logout();
				signResultDownDto = new SignResultDownDto(
						securityContext.isInvestor(), securityContext.isFinancier());
				return ResultDtoFactory.toNack(MessageUtil
						.getMessage("app.error.not.investor"), signResultDownDto);
			}
		} catch (LockedAccountException e1) {
			result.rejectValue(LiteralConstant.PASSWORD,
					"member.error.password.toomanyfailure");
		} catch (IncorrectCredentialsException e1) {
			result.rejectValue(LiteralConstant.PASSWORD, "member.error.signin.fail");
		} catch (UnknownAccountException e1) {
			result.rejectValue(LiteralConstant.USERNAME, "member.error.not_exist");
		} catch (DisabledAccountException e1) {
			result.rejectValue(LiteralConstant.USERNAME, "member.error.username.status");
        }
		if (result.hasErrors()) {
			throw new ValidateException(result);
		}
		signResultDownDto = new SignResultDownDto(
				securityContext.isInvestor(), securityContext.isFinancier());
		return ResultDtoFactory.toAck("登陆成功", signResultDownDto);
	}
	
	/*
	 * 二期账户总览接口
	 * 
	 * refer to myaccount/accountoverview, myaccount/investment/myprofit
	 * 
	 * @author yicai
	 */
	@RequestMapping(value = "/account/overview2", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_DEPOSITE,
			Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW,
			Permissions.MY_ACCOUNT_OVERVIEW_ESTIMATED_INCOME }, logical = Logical.OR)
	public ResultDto doAppAccountBrowsingV2() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAccountBrowsing() start: ");
		}
		NumberTool tool = new NumberTool();
		AccountBrowsingV2DownDto data = new AccountBrowsingV2DownDto();
		User user = securityContext.getCurrentUser();
		
		/* 账户资产总额相关及比例 */
		AccountOverviewSearchDto aDto = accountOverviewService
				.getAccountOverviewDetailInfo(user.getUserId());
		data.setTotalAsset(tool.formatMoney(aDto.getCurrentAccount().getTotalAssets()));
		data.setBal(tool.formatMoney(aDto.getCurrentAccount().getBal()));
		data.setTotalPrincipal(tool.formatMoney(aDto.getCurrentAccount().getTotalPrincipal()));
		// 充值
		data.setRechargeSum(aDto.getAccountOverview().getRechargeSum());
		data.setRechargeNum(tool.formatMoney(aDto.getAccountOverview().getRechargeNum()));
		data.setRechargeProp(aDto.getAccountOverview().getRechargeProp());
		// 提现
		data.setWithdrawSum(aDto.getAccountOverview().getWithdrawSum());
		data.setWithdrawNum(tool.formatMoney(aDto.getAccountOverview().getWithdrawNum()));
		data.setWithdrawProp(aDto.getAccountOverview().getWithdrawProp());
		// 活期结息
		data.setCurrentSettlementSum(aDto.getAccountOverview().getInterestSum());
		data.setCurrentSettlementNum(tool.formatMoney(aDto.getAccountOverview().getInterestNum()));
		data.setCurrentSettlementProp(aDto.getAccountOverview().getInterestProp());
		// 投资申购
		data.setInvestmentSum(aDto.getAccountOverview().getInvestSum());
		data.setInvestmentNum(tool.formatMoney(aDto.getAccountOverview().getInvestNum()));
		data.setInvestmentProp(aDto.getAccountOverview().getInvestProp());
		// 融资还款
		data.setFinancingRepaymentSum(aDto.getAccountOverview().getFinanceSum());
		data.setFinancingRepaymentNum(tool.formatMoney(aDto.getAccountOverview().getFinanceNum()));
		data.setFinancingRepaymentProp(aDto.getAccountOverview().getFinanceProp());
		
		/* 账户投资收益概览 */
		BenifitDto bDto = accountOverviewService.getBenifitDetails(user
				.getUserId());
		List<InvestBenefitDto> result = new ArrayList<InvestBenefitDto>();
		if (bDto.getInvestBenifit() != null
				&& !bDto.getInvestBenifit().isEmpty()) {
			for (InvestBenifitDto d : bDto.getInvestBenifit()) {
				InvestBenefitDto investBenefitDto = new InvestBenefitDto();
				if (d != null) {
					investBenefitDto.setInvestBenefit(d.getInvestBenifit());
					investBenefitDto.setWeek(d.getWeek());
					result.add(investBenefitDto);
				}
			}
		}
		data.setInvestBenefit(result.subList(result.size() - 5, result.size()));
		
		/* 账户债权总览 */
		Map<String, BigDecimal> map = financingPackageListController
				.processSummaryCreditor();
		MyInvestOverviewDownDto myInvestOverviewDownDto = convertMyInvestOverviewDownDto(map);
		data.setTotalNextEarnings(myInvestOverviewDownDto.getTotalNextPayAmt()); // 下期预期总收益
		data.setTotalPrincipalApplications(myInvestOverviewDownDto.getTotalPrincipal());// 申购本金总额
		data.setTotalRestAmt(myInvestOverviewDownDto.getTotalRestAmt()); // 剩余本息总额
		
		/* 新增4收益字段:待实现收益, 预期总收益, 已实现收益, 预期年化总收益率 */
		String userId = securityContext.getCurrentUserId();
		BigDecimal expectTotalProfitRate = investorProfitSummaryService.getExpectTotalProfitRate(userId);
    	BigDecimal expectTotalProfit = investorProfitSummaryService.getInvestorExpectTotalReceivedProfit(userId);
    	BigDecimal realizedTotalProfit = investorProfitSummaryService.getInvestorRealizedReceivedProfit(userId);
    	BigDecimal unRecvProfit = investorProfitSummaryService.getUnReceivedProfit(userId);
		data.setExpectTotalProfit(tool.formatMoney(unRecvProfit));
		data.setRealizedTotalRecvProfit(tool.formatMoney(expectTotalProfit));
		data.setExpectTotalRecvProfit(tool.formatMoney(realizedTotalProfit));
		data.setExpectTotalProfitRate(expectTotalProfitRate.toString());
		return ResultDtoFactory.toAck("获取成功", data);
	}
	
	private MyInvestOverviewDownDto convertMyInvestOverviewDownDto(
			Map<String, BigDecimal> map) {
		BigDecimal totalNextPayAmt = map.get("totalNextPayAmt"); // 下期预期总收益
		BigDecimal totalPrincipal = map.get("totalPrincipal"); // 申购本金总额
		BigDecimal totalRestAmt = map.get("totalRestAmt"); // 剩余本息总额
		NumberTool tool = new NumberTool();
		MyInvestOverviewDownDto myInvestOverviewDownDto = new MyInvestOverviewDownDto();
		myInvestOverviewDownDto.setTotalPrincipal(tool
				.formatMoney(totalPrincipal));
		myInvestOverviewDownDto.setTotalNextPayAmt(tool
				.formatMoney(totalNextPayAmt));
		myInvestOverviewDownDto.setTotalRestAmt(tool.formatMoney(totalRestAmt));
		return myInvestOverviewDownDto;
	}
	
	/*
	 * 账户明细
	 * 
	 * refer to myaccount/getaccountdetails
	 * 
	 * @author Tom, yicai
	 */
	@RequestMapping(value = "/account/detail", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_ACCOUNT_DETAILS })
	public ResultDto doAppAccountDetailList(
			@OnValid @RequestBody AccountDetailListV2UpDto accountDetailListDto) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAccountDetailList() start: ");
		}
		AccountDetailListDownstreamDto data = new AccountDetailListDownstreamDto();
		User user = securityContext.getCurrentUser();
		NumberTool tool = new NumberTool();
		AccountDetailsSearchDto searchDto = initAccountDetailsSearchDto(accountDetailListDto);
		DataTablesResponseDto<AccountDetailsDto> accountdetailDto = accountDetailsService
				.getAccountDetails(searchDto, user.getUserId());
		List<DetailListDto> result = new ArrayList<DetailListDto>();
		if (accountdetailDto.getData() != null) {
			for (AccountDetailsDto d : accountdetailDto.getData()) {
				DetailListDto dto = new DetailListDto();
				dto.setAmount(tool.formatMoney(d.getTrxAmt()));
				dto.setRemarks(d.getTrxMemo());
				dto.setType(d.getUseType().getText());
				dto.setTime(d.getTrxDt().toString());
				dto.setPayRecvFlg(d.getPayRecvFlg());
				result.add(dto);
			}
		}
		data.setDetailList(result);
		return ResultDtoFactory.toAck("获取成功", data);
	}

	private AccountDetailsSearchDto initAccountDetailsSearchDto(
			AccountDetailListV2UpDto accountDetailListUpDto) {
		AccountDetailsSearchDto searchDto = new AccountDetailsSearchDto();
		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("trxDt");
		searchDto.setSortedColumns(aiSortCol);
		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
		searchDto.setDisplayLength(20); // default 20 item per page
		searchDto.setDisplayStart(Integer.parseInt(accountDetailListUpDto
				.getDisplayStart()));
		searchDto.setCreateDate(new Date(System.currentTimeMillis())); // default current time
		if (accountDetailListUpDto.getStartTime() != null
				|| accountDetailListUpDto.getEndTime() != null) {
			searchDto.setFromDate(accountDetailListUpDto.getStartTime());
			searchDto.setToDate(accountDetailListUpDto.getEndTime());
		}
		if (accountDetailListUpDto.getType() == null) {
			searchDto.setUseType(EFundUseType.INOUTALL);
		} else {
			searchDto.setUseType(EnumHelper.translate(EFundUseType.class,
					accountDetailListUpDto.getType()));
		}
		if (accountDetailListUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(accountDetailListUpDto.getDisplayLength());
		}
		if (accountDetailListUpDto.getCreateTime() != 0) {
			searchDto.setCreateDate(new Date(accountDetailListUpDto.getCreateTime()));
		}
		return searchDto;
	}

	/*
	 * 查看用户信息接口
	 * 
	 * @author yicai
	 */
	@RequestMapping(value = "/userInfo2", method = RequestMethod.GET)
	@ResponseBody
	public ResultDto userInfoV2() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("userInfo() start: ");
		}
		UserInfoV2DownDto data = new UserInfoV2DownDto();
		User user = securityContext.getCurrentUser();
		String userId = securityContext.getCurrentUserId();
		EInvestorLevel level = EInvestorLevel.NULL;
		try {
			level = securityContext.getInvestorLevelInfo().getLevel();
		} catch (NullPointerException e) {
			// it is financier, we do nothing.
			LOGGER.debug(" {} is financier", userId);
		}
		data.setMemberLevel(level); // 二期新增加字段
		MemberApplication memberPo = memberService
				.getMemberWithLatestStatus(userId);

		MemberDto memberDto = new MemberDto();
		if (memberPo != null) {
			Class<? extends MemberDto> clazz = memberPo.getUserType() == EUserType.PERSON ? PersonDto.class
					: OrganizationDto.class;

			// 拒绝状态下取原信息，原信息为空，表示第一次提交，需要将拒绝信息pop到add页面
			if (memberPo.getStatus() == EApplicationStatus.REJECT) {
				Member oldMember = memberService.getMemberByUserId(userId);
				if (oldMember != null) {
					memberDto = ConverterService.convert(oldMember, clazz,
							MemberInfoConverter.class);
					memberDto.setUserType(securityContext.getCurrentUser()
							.getType());
					memberDto.setStatus(EApplicationStatus.ACCEPT);
				} else {
					memberDto = ConverterService.convert(memberPo, clazz);
					memberDto.setStatus(EApplicationStatus.UNCOMMITTED);
				}
				memberDto.setRejectLastTime(true);
				memberDto.setComments(memberPo.getComments());
			} else {
				memberDto = ConverterService.convert(memberPo, clazz);
			}

			// 修改的pending状态需要看到原信息
			if (memberPo.getStatus() == EApplicationStatus.PENDDING) {
				Member oldMember = memberService.getMemberByUserId(userId);
				if (oldMember != null) {
					memberDto.setOldMember(ConverterService.convert(oldMember,
							clazz, MemberInfoConverter.class));
				}
			}
			// 取账号信息
			AcctPo acctPo = acctService.getAcctByUserId(userId);
			if (acctPo != null) {
				AccountDto accountDto = ConverterService.convert(acctPo,
						AccountDto.class);
				memberDto.setAccount(accountDto);
				// 账号信息set到原信息中
				if (memberDto.getOldMember() != null) {
					memberDto.getOldMember().setAccount(accountDto);
				}
			}

		} else {
			// 第一次补全时email和mobile应该是注册时填入的
			memberDto.setEmail(user.getEmail());
			memberDto.setMobile(user.getMobile());
		}

		if (securityContext.isInvestor()) {
			if (securityContext.isFinancier()) {
				memberDto.setMemberType(EMemberType.INVESTOR_FINANCER);
			} else {
				memberDto.setMemberType(EMemberType.INVESTOR);
			}
		} else if (securityContext.isFinancier()) {
			memberDto.setMemberType(EMemberType.FINANCER);
		}

		Member member = memberService.getMemberByUserId(userId);

		if (member != null) {
			data.setIdCardNum(member.getPersonIdCardNumber());
		}
		if (user != null) {
			data.setPhoneNum(user.getMobile());
			data.setEmail(user.getEmail());
			data.setUserName(user.getName());
		}
		if (memberDto != null) {
			data.setCategory(memberDto.getUserType() == null ? null : memberDto
					.getUserType().getText());
			data.setMemberType(memberDto.getMemberType() == null ? null
					: memberDto.getMemberType().getText());

			data.setBankAccountName(memberDto.getMaskBankAccountName());
			data.setBankAccountNum(memberDto.getMakBankAccount());

			String province = memberDto.getBankOpenProvince() == null ? ""
					: memberDto.getBankOpenProvince().getText();
			String city = memberDto.getBankOpenCity() == null ? ""
					: memberDto.getBankOpenCity().getText();
			String shortname = memberDto.getBankShortName() == null ? ""
					: memberDto.getBankShortName().getText();
			String branch = memberDto.getBankOpenBranch() == null ? ""
					: memberDto.getBankOpenBranch();
			data.setBankName(province + city + shortname + branch);

			data.setAccountNum(memberDto.getAccount() == null ? null
					: memberDto.getAccount().getAcctNo());
			data.setSetUpTime(memberDto.getAccount() == null ? null : memberDto
					.getAccount().getCreateTs());
		}
		return ResultDtoFactory.toAck("获取成功", data);
	}
	
	/*
	 * 融资包详情接口.
	 */
	@RequestMapping(value = "/productDetail2", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto getDetailPageV2(@OnValid @RequestBody ProductDto productDto, HttpServletRequest request) {
        LOGGER.debug("getDetailPage() invoked");
        FinancingMarketItemDetailDto itemDto = getItemBaseInfo(productDto.getProductId());
        ProductPackage pkg = financingMarketService.getPackageDetail(productDto.getProductId());
        if (pkg != null) {
            itemDto.setPkg(ConverterService.convert(pkg, ProductPackageDto.class));
            ProductDetailsDto productDetail = financingMarketService.getProductDetail(pkg.getProductId());
            if (productDetail.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productDetail.getWarrantyType() == EWarrantyType.NOTHING) {
                productDetail.setGuaranteeInstitution("--");
            }
            itemDto.setProduct(productDetail);
        }
        
        NumberTool numberTool = new NumberTool();
        
        FinancingItemDto financingItemDto = new FinancingItemDto();
        
        if(itemDto!=null){
        	if(itemDto.getPkg()!=null) {
        		financingItemDto.setPackageName("NO."+itemDto.getPkg().getId()+" "+itemDto.getPkg().getPackageName());
        		financingItemDto.setState(itemDto.getPkg().getStatus()==null?null:itemDto.getPkg().getStatus().getText());
        		financingItemDto.setStateCode(itemDto.getPkg().getStatus()==null?null:itemDto.getPkg().getStatus().getCode());
        		financingItemDto.setProgress(numberTool.formatMoney(itemDto.getProgress())+"%");
        		
        		financingItemDto.setEndTime(itemDto.getPkg().getSupplyEndTime());
        		
        		financingItemDto.setStartTime(itemDto.getPkg().getSupplyStartTime());
        		financingItemDto.setFinancedAmount(numberTool.formatMoney(itemDto.getPkg().getPackageQuota()));
        	}
	        
	        FicoScoreDto ficoScoreDto = new FicoScoreDto();
	    	ficoScoreDto.setRiskLevelCode(itemDto.getRiskLevel()==null?null:itemDto.getRiskLevel().getCode());
	        financingItemDto.setFicoScoreDto(ficoScoreDto);
	        
	        if(itemDto.getProduct()!=null) {
	        	
	        	financingItemDto.setRiskSafeguard(itemDto.getProduct().getWarrantyType()==null?null:itemDto.getProduct().getWarrantyType().getText());
	        	if (itemDto.getProduct().getRatePercent() != null) {
	        		financingItemDto.setAnnualProfit(itemDto.getProduct().getRatePercent()[0]+"."+itemDto.getProduct().getRatePercent()[1]+"%");
	        	}
	        	financingItemDto.setWrtrCreditDesc(itemDto.getProduct().getWrtrCreditDesc()); // 公司简介
	        	financingItemDto.setLimitTime(itemDto.getProduct().getTermLength()+(itemDto.getProduct().getTermType()==null?null:itemDto.getProduct().getTermType().getText()));
	        	financingItemDto.setFinancier(itemDto.getProduct().getUser()==null?null:itemDto.getProduct().getUser().getMaskName());
	        	financingItemDto.setRepaymentWay(itemDto.getProduct().getPayMethod()==null?null:itemDto.getProduct().getPayMethod().getText());
	        	financingItemDto.setIndustry(itemDto.getProduct().getFinancierIndustryType()==null?null:itemDto.getProduct().getFinancierIndustryType().getText());
	        	financingItemDto.setFinancePurpose(itemDto.getProduct().getLoanPurpose());
	 	        financingItemDto.setGuaranteeInstitution(HtmlUtils.htmlUnescape(itemDto.getProduct().getGuaranteeInstitution()));
				if (itemDto.getProduct().getWarrantyType() != EWarrantyType.MONITORASSETS
						&& itemDto.getProduct().getWarrantyType() != EWarrantyType.NOTHING
						&& StringUtils.isNotBlank(itemDto.getProduct()
								.getWarrantId())) {
					StringBuilder url = new StringBuilder();
					if (AppConfigUtil.getUploadFileURL().startsWith("/")) {
						url.append(request.getScheme()).append("://")
								.append(request.getServerName());
						if (request.getServerPort() != 80
								&& request.getServerPort() != 443) {
							url.append(":").append(request.getServerPort());
						}
					}
					url.append(AppConfigUtil.getUploadFileURL());
					Agency agency = memberService.getAgencyByUserId(itemDto
							.getProduct().getWarrantId());
					if (agency != null
							&& StringUtils.isNotBlank(agency.getLicenceNoImg())) {
						financingItemDto.setGuaranteeLicenseNoImg(url.append(
								agency.getLicenceNoImg()).toString());
						financingItemDto.setGuaranteeLicenseNoImgName(agency
								.getLicenceNoImg());
					}
				}
				/* 二期改动：项目风险等级 */
		    	ficoScoreDto.setProductGrage(itemDto.getProduct().getProductLevel().getCode());
		    	ficoScoreDto.setFinancierGrage(itemDto.getProduct().getFinancierLevel());
		    	ficoScoreDto.setWarrantGrage(itemDto.getProduct().getWarrantorLevel());
	        	
	        	List<BuildingDto> buildingList = new ArrayList<BuildingDto>();
	        	if(itemDto.getProduct().getProductMortgageResidentialDetailsDtoList()!=null&&itemDto.getProduct().getProductMortgageResidentialDetailsDtoList().size()>0) {
	        		for(ProductMortgageResidentialDetailsDto detailDto :itemDto.getProduct().getProductMortgageResidentialDetailsDtoList()){
	        			BuildingDto buildingDto = new BuildingDto();
	        			buildingDto.setArea(numberTool.formatMoney(detailDto.getArea()));
	        			buildingDto.setPurchasePrice(numberTool.formatMoney(detailDto.getPurchasePrice()));
	        			buildingDto.setEvaluatePrice(numberTool.formatMoney(detailDto.getEvaluatePrice()));
	        			buildingDto.setMarketPrice(numberTool.formatMoney(detailDto.getMarketPrice()));
	        			buildingList.add(buildingDto);
	        		}
	        	}
	        	financingItemDto.setBuildingList(buildingList);
	        	
	        	
	        	List<CarDto> carList = new ArrayList<CarDto>();
	        	if(itemDto.getProduct().getProductMortgageVehicleDetailsDtoList()!=null&&itemDto.getProduct().getProductMortgageVehicleDetailsDtoList().size()>0){
	        		for(ProductMortgageVehicleDetailsDto detailDto :itemDto.getProduct().getProductMortgageVehicleDetailsDtoList()){
	        			CarDto carDto = new CarDto();
	        			carDto.setBrand(detailDto.getBrand());
	        			carDto.setType(detailDto.getType());
	        			carList.add(carDto);
	        		}    
	        	}
	        	financingItemDto.setCarList(carList);
	        	
	        	List<ChattelMortgageDto> chattelMortgageList = new ArrayList<ChattelMortgageDto>();
	        	if(itemDto.getProduct().getProductPledgeDetailsDtoList()!=null&&itemDto.getProduct().getProductPledgeDetailsDtoList().size()>0) {
	        		for(ProductPledgeDetailsDto detailDto :itemDto.getProduct().getProductPledgeDetailsDtoList()){
	        			ChattelMortgageDto chattelMortgageDto = new ChattelMortgageDto();
	        			chattelMortgageDto.setName(detailDto.getName());
	        			chattelMortgageDto.setPledgeClass(detailDto.getPledgeClass());
	        			chattelMortgageDto.setType(detailDto.getType());
	        			chattelMortgageDto.setCount(detailDto.getCount()==null?"0":String.valueOf(detailDto.getCount()));
	        			chattelMortgageDto.setPrice(numberTool.formatMoney(detailDto.getPrice()));
	        			chattelMortgageDto.setLocation(detailDto.getLocation());
	        			chattelMortgageList.add(chattelMortgageDto);
	        		} 
	        	}
	        	financingItemDto.setChattelMortgageList(chattelMortgageList);
	        	
	        	List<PersonalAssetDto> personalAssetList = new ArrayList<PersonalAssetDto>();
	        	if(itemDto.getProduct().getProductAssetDtoList()!=null&&itemDto.getProduct().getProductAssetDtoList().size()>0) {
	        		for(ProductAssetDto detailDto :itemDto.getProduct().getProductAssetDtoList()){
	        			PersonalAssetDto personalAssetDto = new PersonalAssetDto();
	        			personalAssetDto.setAssertAmount(numberTool.formatMoney(detailDto.getAssertAmount()));
	        			personalAssetDto.setDtype(detailDto.getDtype()==null?null:detailDto.getDtype().getText());
	        			personalAssetDto.setNotes(detailDto.getNotes());
	        			personalAssetList.add(personalAssetDto);
	        		} 
	        	}
		        financingItemDto.setPersonalAssetList(personalAssetList);
		        
		        List<PersonalLiabilityDto> personalLiabilityList = new ArrayList<PersonalLiabilityDto>();
		        if(itemDto.getProduct().getProductDebtDtoList()!=null&&itemDto.getProduct().getProductDebtDtoList().size()>0) {
		        	for(ProductDebtDto detailDto :itemDto.getProduct().getProductDebtDtoList()){
		        		PersonalLiabilityDto personalLiabilityDto = new PersonalLiabilityDto();
		        		personalLiabilityDto.setDtype(detailDto.getDtype()==null?null:detailDto.getDtype().getText());
		        		personalLiabilityDto.setDebtAmount(numberTool.formatMoney(detailDto.getDebtAmount()));
		        		personalLiabilityDto.setMonthlyPayment(numberTool.formatMoney(detailDto.getMonthlyPayment()));
		        		personalLiabilityDto.setNotes(detailDto.getNotes());
		        		personalLiabilityList.add(personalLiabilityDto);
		        	} 
		        }
		        financingItemDto.setPersonalLiabilityList(personalLiabilityList);
	        }
	        
	        WarrantorDto warrantorDto = new WarrantorDto();
	    	warrantorDto.setWarrantEnterpriseStr(itemDto.getWarrantEnterpriseStr());
	    	warrantorDto.setWarrantPersonStr(itemDto.getWarrantPersonStr());
	        financingItemDto.setWarrantorDto(warrantorDto);
	        
        }
    	return ResultDtoFactory.toAck("获取成功", financingItemDto);
    }
	
	/**
	 * 密码修改，post
	 * 
	 * 
	 * @param passwordDto
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/password/update", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MY_SETTINGS_PASSWORD_SETTINGS })
    public ResultDto passwordUpdate(@OnValid @RequestBody PasswordV2UpDto passwordV2UpDto) {
        LOGGER.info("passwordUpdate() start: ");
        PasswordDto passwordDto = ConverterService.convert(passwordV2UpDto, PasswordDto.class);
        String userName = securityContext.getCurrentUserName();
        passwordDto.setUserName(userName);
        this.userService.updatePassword(passwordDto);
        return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
    }

	/**
	 * 用户名修改，post
	 * 
	 * @param user
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/nickname/update", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MY_SETTINGS_PASSWORD_SETTINGS })
    public ResultDto nicknameUpdate(@OnValid(NickNameGroup.class) @RequestBody NickNameUpDto nickNameUpDto) {
        LOGGER.info("nicknameUpdate() start: ");
        User user = ConverterService.convert(nickNameUpDto, User.class);
        User oldUser = securityContext.getCurrentUser();
        this.userService.updateUserName(oldUser.getUserId(), user.getUsername(), oldUser.getUsername());
        return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
    }
    
    
    /**
     * 三期接口
     */

    /**
	 * 三期登录接口
	 * 
	 * @param loginDto
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/login3", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto doAppSigninV3(@OnValid @RequestBody LoginDto loginDto,
			BindingResult result) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppSignin() start: ");
		}
		SignResultDownDtoV3 signResultDownDtoV3 = null;
		try {
			SecurityContext.login(loginDto.getUserName(),
					loginDto.getPassword());
			// 既没有担保机构，也没有服务中心（服务中心，代理服务中心，普通服务中心）
			if (!securityContext.isProdServ()
					&& !securityContext.isAuthServiceCenter()
					&& !securityContext.isAuthServiceCenterAgent()
					&& !securityContext.isAuthServiceCenterGeneral()) {
				SecurityContext.logout();
				signResultDownDtoV3 = new SignResultDownDtoV3(
						securityContext.isProdServ(),
						securityContext.isAuthServiceCenter(),
						securityContext.isAuthServiceCenterAgent(),
						securityContext.isAuthServiceCenterAgent());
				return ResultDtoFactory.toNack(MessageUtil
						.getMessage("app.error.not.prodserv.or.authservicecenter"), signResultDownDtoV3);
			}
		} catch (LockedAccountException e1) {
			result.rejectValue(LiteralConstant.PASSWORD,
					"member.error.password.toomanyfailure");
		} catch (IncorrectCredentialsException e1) {
			result.rejectValue(LiteralConstant.PASSWORD, "member.error.signin.fail");
		} catch (UnknownAccountException e1) {
			result.rejectValue(LiteralConstant.USERNAME, "member.error.not_exist");
		} catch (DisabledAccountException e1) {
			result.rejectValue(LiteralConstant.USERNAME, "member.error.username.status");
        }
		if (result.hasErrors()) {
			throw new ValidateException(result);
		}
		signResultDownDtoV3 = new SignResultDownDtoV3(
				securityContext.isProdServ(),
				securityContext.isAuthServiceCenter(),
				securityContext.isAuthServiceCenterAgent(),
				securityContext.isAuthServiceCenterAgent());
		return ResultDtoFactory.toAck("登陆成功", signResultDownDtoV3);
	}

	/*
	 * 三期账户总览接口
	 * 
	 * refer to myaccount/accountoverview, myaccount/investment/myprofit
	 * 
	 * @author yicai
	 */
	@RequestMapping(value = "/account/overview3", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_DEPOSITE,
			Permissions.MY_ACCOUNT_OVERVIEW_WITHDRAW,
			Permissions.MY_ACCOUNT_OVERVIEW_ESTIMATED_INCOME }, logical = Logical.OR)
	public ResultDto doAppAccountBrowsingV3() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doAppAccountBrowsing() start: ");
		}
		NumberTool tool = new NumberTool();
		AccountBrowsingV3DownDto data = new AccountBrowsingV3DownDto();
		User user = securityContext.getCurrentUser();
		
		/* 账户资产总额相关及比例 */
		AccountOverviewSearchDto aDto = accountOverviewService
				.getAccountOverviewDetailInfo(user.getUserId());
		data.setTotalAsset(tool.formatMoney(aDto.getCurrentAccount().getTotalAssets()));
		data.setBal(tool.formatMoney(aDto.getCurrentAccount().getBal()));
		data.setTotalPrincipal(tool.formatMoney(aDto.getCurrentAccount().getTotalPrincipal()));
		// 可用余额，可提现：三期区别于二期之处
		data.setAvailableAsset(aDto.getCurrentAccount().getAvailableBal());
		data.setAvailableCash(aDto.getCurrentAccount().getCashableAmt());
		// 充值
		data.setRechargeSum(aDto.getAccountOverview().getRechargeSum());
		data.setRechargeNum(tool.formatMoney(aDto.getAccountOverview().getRechargeNum()));
		data.setRechargeProp(aDto.getAccountOverview().getRechargeProp());
		// 提现
		data.setWithdrawSum(aDto.getAccountOverview().getWithdrawSum());
		data.setWithdrawNum(tool.formatMoney(aDto.getAccountOverview().getWithdrawNum()));
		data.setWithdrawProp(aDto.getAccountOverview().getWithdrawProp());
		// 活期结息
		data.setCurrentSettlementSum(aDto.getAccountOverview().getInterestSum());
		data.setCurrentSettlementNum(tool.formatMoney(aDto.getAccountOverview().getInterestNum()));
		data.setCurrentSettlementProp(aDto.getAccountOverview().getInterestProp());
		// 投资申购
		data.setInvestmentSum(aDto.getAccountOverview().getInvestSum());
		data.setInvestmentNum(tool.formatMoney(aDto.getAccountOverview().getInvestNum()));
		data.setInvestmentProp(aDto.getAccountOverview().getInvestProp());
		// 融资还款
		data.setFinancingRepaymentSum(aDto.getAccountOverview().getFinanceSum());
		data.setFinancingRepaymentNum(tool.formatMoney(aDto.getAccountOverview().getFinanceNum()));
		data.setFinancingRepaymentProp(aDto.getAccountOverview().getFinanceProp());
		
		/* 账户投资收益概览 */
		BenifitDto bDto = accountOverviewService.getBenifitDetails(user
				.getUserId());
		List<InvestBenefitDto> result = new ArrayList<InvestBenefitDto>();
		if (bDto.getInvestBenifit() != null
				&& !bDto.getInvestBenifit().isEmpty()) {
			for (InvestBenifitDto d : bDto.getInvestBenifit()) {
				InvestBenefitDto investBenefitDto = new InvestBenefitDto();
				if (d != null) {
					investBenefitDto.setInvestBenefit(d.getInvestBenifit());
					investBenefitDto.setWeek(d.getWeek());
					result.add(investBenefitDto);
				}
			}
		}
		data.setInvestBenefit(result.subList(result.size() - 5, result.size()));
		
		/* 账户债权总览 */
		Map<String, BigDecimal> map = financingPackageListController
				.processSummaryCreditor();
		MyInvestOverviewDownDto myInvestOverviewDownDto = convertMyInvestOverviewDownDto(map);
		data.setTotalNextEarnings(myInvestOverviewDownDto.getTotalNextPayAmt()); // 下期预期总收益
		data.setTotalPrincipalApplications(myInvestOverviewDownDto.getTotalPrincipal());// 申购本金总额
		data.setTotalRestAmt(myInvestOverviewDownDto.getTotalRestAmt()); // 剩余本息总额
		
		/* 新增4收益字段:待实现收益, 预期总收益, 已实现收益, 预期年化总收益率 */
		String userId = securityContext.getCurrentUserId();
		BigDecimal expectTotalProfitRate = investorProfitSummaryService.getExpectTotalProfitRate(userId);
    	BigDecimal expectTotalProfit = investorProfitSummaryService.getInvestorExpectTotalReceivedProfit(userId);
    	BigDecimal realizedTotalProfit = investorProfitSummaryService.getInvestorRealizedReceivedProfit(userId);
    	BigDecimal unRecvProfit = investorProfitSummaryService.getUnReceivedProfit(userId);
		data.setExpectTotalProfit(tool.formatMoney(unRecvProfit));
		data.setRealizedTotalRecvProfit(tool.formatMoney(expectTotalProfit));
		data.setExpectTotalRecvProfit(tool.formatMoney(realizedTotalProfit));
		data.setExpectTotalProfitRate(expectTotalProfitRate.toString());
		return ResultDtoFactory.toAck("获取成功", data);
	}
	
}
