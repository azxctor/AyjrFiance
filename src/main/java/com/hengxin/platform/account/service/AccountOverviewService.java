package com.hengxin.platform.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.account.dto.AccountOverviewDto;
import com.hengxin.platform.account.dto.CurrentAccountDto;
import com.hengxin.platform.account.dto.InvestBenifitDto;
import com.hengxin.platform.account.dto.UserInfoDto;
import com.hengxin.platform.account.dto.XwbAccountDto;
import com.hengxin.platform.account.dto.upstream.AccountOverviewSearchDto;
import com.hengxin.platform.account.dto.upstream.BenifitDto;
import com.hengxin.platform.account.dto.upstream.BnkCardBalDto;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.dto.biz.rsp.BankAcctAmtRsp;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.repository.SubAcctTrxJnlRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctAgreementManageService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.risk.enums.ERiskBearLevel;
import com.hengxin.platform.risk.service.InvestRiskBearService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: AccountOverviewService.
 * 
 * @author jishen
 * 
 */
@Service
public class AccountOverviewService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountOverviewService.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AcctService acctService;
	@Autowired
	private SubAcctRepository subAcctRepository;
	@Autowired
	private SubAcctTrxJnlRepository subAcctTrxJnlRepository;
	@Autowired
	private BankAcctRepository bankAcctRepository;
	@Autowired
	private BankAcctAgreementManageService bankAcctAgreementManageService;
	@Autowired
	private FundAcctBalService fundAcctBalService;
	@Autowired
	private PkgTradeJnlRepository pkgTradeJnlRepository;
	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private PositionService positionService;
	@Autowired
	private InvestRiskBearService investRiskBearService;

	/**
	 * get accountOverview info.
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AccountOverviewSearchDto getAccountOverviewDetailInfo(String userId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoke method getAccountOverviewDetailInfo(" + userId
					+ ")");
		}
		NumberFormat percent = NumberFormat.getPercentInstance();
		percent.setMaximumFractionDigits(4);
		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = acctService.getAcctByUserId(userId);
		if (acctPo == null) {
			LOGGER.warn("account not found for user id {}", userId);
			throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
		}
		String acctNo = acctPo.getAcctNo();
		// 获取该会员的 活期子账户信息
		SubAcctPo currentAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctNo,
						ESubAcctNo.CURRENT.getCode());
		if (currentAcctPo == null) {
			throw new BizServiceException(EErrorCode.SUB_ACCT_NOT_EXIST);
		}
		String subAcctNo = currentAcctPo.getSubAcctNo();
		SubAcctPo xwbaoAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctPo.getAcctNo(),
						ESubAcctNo.XWB.getCode());

		AccountOverviewSearchDto dto = new AccountOverviewSearchDto();
		// 设置用户信息
		UserPo userPo = userRepository.findOne(userId);
		UserInfoDto userInfoDto = ConverterService.convert(userPo,
				UserInfoDto.class);
		userInfoDto.setOpenXwb(securityContext.canOpenXWB());
		User user = securityContext.getCurrentUser();
		String lastLoginTs = DateUtils.formatDate(user.getLastLoginTs(),
				"yyyy-MM-dd HH:mm:ss");
		userInfoDto.setLastLoginTs(lastLoginTs);
		List<BankAcctPo> bankAcctPos = bankAcctRepository
				.findByUserIdAndSignedFlg(userId, EFlagType.YES.getCode());
		if (null != bankAcctPos && bankAcctPos.size() > 0) {
			userInfoDto.setSignedFlag(EFlagType.YES.getCode());
			userInfoDto.setBankAcctNo(bankAcctPos.get(0).getBnkAcctNo());
		} else {
			bankAcctPos = bankAcctRepository.findByUserIdAndSignedFlg(userId,
					EFlagType.NO.getCode());
			userInfoDto.setSignedFlag(EFlagType.NO.getCode());
			if (null != bankAcctPos && bankAcctPos.size() > 0) {
				userInfoDto.setBankAcctNo(bankAcctPos.get(0).getBnkAcctNo());
			}
		}
		userInfoDto.setAcctNo(acctNo);
		dto.setUserInfo(userInfoDto);

		// 设置活期子账户信息
		CurrentAccountDto currentAccountDto = ConverterService.convert(
				currentAcctPo, CurrentAccountDto.class);
		currentAccountDto.setAcctStatus(acctPo.getAcctStatus().equals(
				EAcctStatus.NORMAL.getCode()) ? EAcctStatus.NORMAL
				: EAcctStatus.RECNOPAY);
		currentAccountDto.setBal(subAcctRepository
				.getAcctTotalBalByAcctNo(acctNo));
		BigDecimal totalPrincipal = positionService
				.getUserReceivableTotalPrincipal(userId);
		currentAccountDto.setTotalPrincipal(AmtUtils.processNegativeAmt(
				totalPrincipal, BigDecimal.ZERO));
		currentAccountDto.setTotalAssets(currentAccountDto.getBal().add(
				currentAccountDto.getTotalPrincipal()));
		// Risk level
		ERiskBearLevel bearLevel = investRiskBearService
				.getUserRiskBearLevel(userId);
		currentAccountDto.setRiskBearLevel(bearLevel);

		BigDecimal availableBal = caculateAvailableAmt(currentAcctPo);
		currentAccountDto.setAvailableBal(NumberUtil
				.formatToThousandsWithFractionDigits(availableBal));
		currentAccountDto.setCashableAmt(NumberUtil
				.formatToThousandsWithFractionDigits(fundAcctBalService
						.getUserCashableAmt(userId)));
		dto.setCurrentAccount(currentAccountDto);

		// 设置小微宝账户信息
		XwbAccountDto xwbAccountDto = new XwbAccountDto();
		xwbAccountDto.setBal(xwbaoAcctPo.getBal().setScale(2,
				RoundingMode.HALF_UP));
		xwbAccountDto.setRate(percent.format(CommonBusinessUtil.getXwbRate()));
		xwbAccountDto.setAccumCrAmt(getAccumCrAmt(acctPo));
		xwbAccountDto.setEarningsYesterday(getEarningsYesterday(xwbaoAcctPo));
		dto.setXwbAccount(xwbAccountDto);

		// 设置账户概览
		dto.setAccountOverview(getAccountOverview(acctNo, subAcctNo));

		// 设置账户明细

		// 设置平台资金池金额
		if (AppConfigUtil.isBnkEnabled() && securityContext.isPlatformUser()) {
			try {
				BankAcctAmtRsp rsp = bankAcctAgreementManageService
						.queryExchangeAcctAmt(
								bankAcctPos.get(0).getBnkAcctNo(), acctNo);
				dto.setTotalAmount(rsp.getAvailableAmt());
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			dto.setTotalAmount(new BigDecimal("956258422"));
		}

		return dto;
	}

	/**
	 * 
	 * Description: 获取投资收益.
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public BenifitDto getBenifitDetails(String userId) {
		AcctPo acctPo = acctService.getAcctByUserId(userId);
		if (acctPo == null) {
			throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
		}
		String acctNo = acctPo.getAcctNo();
		SubAcctPo currentAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctPo.getAcctNo(),
						ESubAcctNo.CURRENT.getCode());
		String subAcctNo = currentAcctPo.getSubAcctNo();
		BenifitDto dto = new BenifitDto();
		// 设置账户概览
		dto.setAccountOverview(getAccountOverview(acctNo, subAcctNo));
		// 设置投资收益
		dto.setInvestBenifit(getInvestBenifit(acctNo, subAcctNo));
		return dto;
	}

	/**
	 * Description: 设置账户概览.
	 * 
	 * @param percent
	 * @param acctNo
	 * @param subAcctNo
	 * @param dto
	 */

	private AccountOverviewDto getAccountOverview(String acctNo,
			String subAcctNo) {
		NumberFormat percent = NumberFormat.getPercentInstance();
		percent.setMinimumFractionDigits(1);
		AccountOverviewDto accountOverviewDto = new AccountOverviewDto();
		BigDecimal rechargeSum = subAcctTrxJnlRepository
				.getTrxAmtByAcctAndUseType(acctNo, subAcctNo,
						EFundUseType.RECHARGE);
		if (null == rechargeSum) {
			rechargeSum = BigDecimal.ZERO;
		}
		accountOverviewDto.setRechargeSum(NumberUtil
				.formatToThousandsWithFractionDigits(rechargeSum));
		accountOverviewDto.setRechargeNum(rechargeSum);
		BigDecimal withdrawSum = subAcctTrxJnlRepository
				.getTrxAmtByAcctAndUseType(acctNo, subAcctNo, EFundUseType.CASH);
		if (null == withdrawSum) {
			withdrawSum = BigDecimal.ZERO;
		}
		accountOverviewDto.setWithdrawSum(NumberUtil
				.formatToThousandsWithFractionDigits(withdrawSum));
		accountOverviewDto.setWithdrawNum(withdrawSum);
		BigDecimal interestSum = subAcctTrxJnlRepository
				.getTrxAmtByAcctAndUseType(acctNo, subAcctNo,
						EFundUseType.INTEREST);
		if (null == interestSum) {
			interestSum = BigDecimal.ZERO;
		}
		accountOverviewDto.setInterestSum(NumberUtil
				.formatToThousandsWithFractionDigits(interestSum));
		accountOverviewDto.setInterestNum(interestSum);
		BigDecimal investSum = subAcctTrxJnlRepository
				.getTrxAmtByAcctAndUseType(acctNo, subAcctNo,
						EFundUseType.SUBSCRIBE);
		if (null == investSum) {
			investSum = BigDecimal.ZERO;
		}
		accountOverviewDto.setInvestSum(NumberUtil
				.formatToThousandsWithFractionDigits(investSum));
		accountOverviewDto.setInvestNum(investSum);
		BigDecimal financeSum = subAcctTrxJnlRepository
				.getTrxAmtByAcctAndUseType(acctNo, subAcctNo,
						EFundUseType.FNCR_REPAYMENT);
		if (null == financeSum) {
			financeSum = BigDecimal.ZERO;
		}
		accountOverviewDto.setFinanceSum(NumberUtil
				.formatToThousandsWithFractionDigits(financeSum));
		accountOverviewDto.setFinanceNum(financeSum);
		BigDecimal sum = rechargeSum.add(withdrawSum).add(interestSum)
				.add(investSum).add(financeSum);

		if (sum.equals(BigDecimal.ZERO)) {
			sum = BigDecimal.ONE;
		}

		accountOverviewDto.setRechargeProp(percent.format(rechargeSum.divide(
				sum, 2, RoundingMode.HALF_UP)));
		accountOverviewDto.setWithdrawProp(percent.format(withdrawSum.divide(
				sum, 2, RoundingMode.HALF_UP)));
		accountOverviewDto.setInterestProp(percent.format(interestSum.divide(
				sum, 2, RoundingMode.HALF_UP)));
		accountOverviewDto.setInvestProp(percent.format(investSum.divide(sum,
				2, RoundingMode.HALF_UP)));
		accountOverviewDto.setFinanceProp(percent.format(financeSum.divide(sum,
				2, RoundingMode.HALF_UP)));
		return accountOverviewDto;
	}

	/**
	 * 获取当前活期账户详情.
	 * 
	 * @param userId
	 * @return
	 */
	public CurrentAccountDto getCurrentAccountDetail(String userId) {

		AcctPo acctPo = acctService.getAcctByUserId(userId);
		if (acctPo == null) {
			throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
		}
		SubAcctPo currentAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctPo.getAcctNo(),
						ESubAcctNo.CURRENT.getCode());

		// 设置活期子账户信息
		CurrentAccountDto currentAccountDto = ConverterService.convert(
				currentAcctPo, CurrentAccountDto.class);
		BigDecimal availableBal = caculateAvailableAmt(currentAcctPo);
		currentAccountDto.setAvailableBal(availableBal.toString());
		currentAccountDto.setCashableAmt(fundAcctBalService.getUserCashableAmt(
				userId).toString());

		return currentAccountDto;
	}

	private BigDecimal getEarningsYesterday(SubAcctPo xwbaoAcctPo) {
		BigDecimal result = BigDecimal.ZERO;
		Date yesteday = DateUtils.add(new Date(), Calendar.DATE, -1);
		try {
			Date start = DateUtils.getStartDate(yesteday);
			Date end = DateUtils.getEndDate(yesteday);
			result = subAcctTrxJnlRepository.getTrxAmtByAcctAndUseTypeAndDate(
					xwbaoAcctPo.getAcctNo(), ESubAcctNo.XWB.getCode(),
					EFundUseType.XWBINTEREST, start, end);
		} catch (ParseException e) {
			LOGGER.debug(e.getMessage(), e);
		}

		return null == result ? BigDecimal.ZERO.setScale(2) : result;

	}

	private BigDecimal getAccumCrAmt(AcctPo acctPo) {
		return AmtUtils.processNegativeAmt(
				subAcctTrxJnlRepository.getTrxAmtByAcctAndUseType(
						acctPo.getAcctNo(), ESubAcctNo.XWB.getCode(),
						EFundUseType.XWBINTEREST), BigDecimal.ZERO).setScale(2,
				BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 获取银行卡可充值金额.
	 * 
	 * @param userId
	 * @return
	 */
	public BnkCardBalDto getBnkCardBal(String userId, String bnkAcctNo) {

		BnkCardBalDto bnkCardBalDto = new BnkCardBalDto();

		AcctPo acctPo = acctService.getAcctByUserId(userId);
		if (acctPo == null) {
			throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
		}

		BankAcctAmtRsp amtRsp = null;

		try {
			amtRsp = bankAcctAgreementManageService.queryBankAcctAmt(bnkAcctNo,
					acctPo.getAcctNo());
			bnkCardBalDto.setSucceed(true);
			bnkCardBalDto.setCashableAmt(amtRsp.getCashableAmt());
		} catch (BizServiceException e) {
			bnkCardBalDto.setSucceed(false);
			bnkCardBalDto.setErrorMsg("查询银行卡可充值金额失败");
			LOGGER.debug("查询银行卡可充值金额失败", e);
		}

		return bnkCardBalDto;
	}

	public String getIconUrl(String userId) {
		UserPo userPo = userRepository.findOne(userId);
		String iconFileId = userPo.getIconFileId();
		if (StringUtils.isNotBlank(iconFileId))
			return FileUploadUtil.getTempFolder() + iconFileId;
		return null;
	}

	/**
	 * 计算可用金额.
	 * 
	 * @param currentAcctPo
	 * @return
	 */
	private BigDecimal caculateAvailableAmt(SubAcctPo currentAcctPo) {
		BigDecimal bal = AmtUtils.processNegativeAmt(currentAcctPo.getBal(),
				BigDecimal.ZERO);
		BigDecimal reseAmt = AmtUtils.processNegativeAmt(
				currentAcctPo.getReservedAmt(), BigDecimal.ZERO);
		BigDecimal freezeAmt = AmtUtils.processNegativeAmt(
				currentAcctPo.getFreezableAmt(), BigDecimal.ZERO);
		BigDecimal availableBal = AmtUtils.max(
				bal.subtract(reseAmt).subtract(freezeAmt), BigDecimal.ZERO);
		return availableBal;
	}

	/**
	 * 
	 * Description: 获取投资收益(十二周).
	 * 
	 * @param userId
	 * @return
	 */
	private List<InvestBenifitDto> getInvestBenifit(String acctNo,
			String subAcctNo) {
		List<InvestBenifitDto> result = iniResult();
		List<InvestBenifitDto> tempList = calculateBenifits(acctNo, subAcctNo);
		List<InvestBenifitDto> benifitList = new ArrayList<InvestBenifitDto>();
		if (null != tempList && tempList.size() > 0) {
			Map<Integer, BigDecimal> benifitMap = new ConcurrentHashMap<Integer, BigDecimal>();
			for (InvestBenifitDto dto : tempList) {
				benifitMap.put(Integer.valueOf(dto.getWeek()),
						dto.getInvestBenifit());
			}
			for (int i = 1; i < 13; i++) {
				if (benifitMap.containsKey(i)) {
					benifitList
							.add(new InvestBenifitDto("", benifitMap.get(i)));
				} else {
					benifitList.add(new InvestBenifitDto("", BigDecimal.ZERO));
				}
			}
			int count = 0;
			for (InvestBenifitDto dto : benifitList) {
				result.get(count).setInvestBenifit(
						result.get(count).getInvestBenifit()
								.add(dto.getInvestBenifit()));
				count++;
			}
		}
		return result;
	}

	/**
	 * 
	 * Description: 初始化日期.
	 * 
	 * @return
	 */
	private List<InvestBenifitDto> iniResult() {
		List<InvestBenifitDto> result = new ArrayList<InvestBenifitDto>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar sundayCalendar = getSundayOfFirstWeek();
		for (int i = 0; i < 11; i++) {
			String date = format.format(sundayCalendar.getTime());
			result.add(new InvestBenifitDto(date, BigDecimal.ZERO));
			sundayCalendar.add(Calendar.DATE, 7);
		}
		result.add(new InvestBenifitDto(format.format(new Date()),
				BigDecimal.ZERO));
		return result;
	}

	/**
	 * Description: 计算投资收益.
	 * 
	 * @param bkgId
	 * @return
	 */
	private List<InvestBenifitDto> calculateBenifits(String acctNo,
			String subAcctNo) {
		List<Object[]> pos = subAcctTrxJnlRepository.getInvestBeniftByAcct(
				acctNo, subAcctNo);
		List<InvestBenifitDto> result = new ArrayList<InvestBenifitDto>();
		for (Object[] o : pos) {
			InvestBenifitDto dto = new InvestBenifitDto();
			dto.setWeek(((BigDecimal) o[0]).toString());
			dto.setInvestBenifit((BigDecimal) o[1]);
			result.add(dto);
		}
		return result;
	}

	/**
	 * 得到第一周周一.
	 * 
	 * @return yyyy-MM-dd
	 */
	public Calendar getMondayOfFirstWeek() {
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		c.add(Calendar.DATE, -dayOfWeek + 1);
		c.add(Calendar.DATE, -11 * 7);
		return c;
	}

	/**
	 * 得到第一周周日.
	 * 
	 * @return yyyy-MM-dd
	 */
	public Calendar getSundayOfFirstWeek() {
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		c.add(Calendar.DATE, -dayOfWeek + 7);
		c.add(Calendar.DATE, -11 * 7);
		return c;
	}

}
