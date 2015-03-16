package com.hengxin.platform.escrow.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.dto.EbcErrorResponse;
import com.hengxin.platform.ebc.dto.EbcWithdrawalReconciliationRequest;
import com.hengxin.platform.ebc.dto.EbcWithdrawalReconciliationResponse;
import com.hengxin.platform.ebc.dto.EbcWithdrawalRequest;
import com.hengxin.platform.ebc.dto.EbcWithdrawalResponse;
import com.hengxin.platform.ebc.enums.EEbcCommandType;
import com.hengxin.platform.ebc.util.EbcNumberUtil;
import com.hengxin.platform.ebc.util.MD5;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.dto.AcctBalnceInfoDto;
import com.hengxin.platform.escrow.dto.EswCashOrderDto;
import com.hengxin.platform.escrow.dto.EswWithdrawalDto;
import com.hengxin.platform.escrow.dto.EswWithdrawalInfoDto;
import com.hengxin.platform.escrow.dto.EswWithdrawalSearchDto;
import com.hengxin.platform.escrow.dto.withdrawal.WithdrawalRespDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswCashOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswCashOrderRepository;
import com.hengxin.platform.escrow.utils.EswUtils;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

/**
 * 提现相关
 * 
 * @author chenwulou
 * 
 */
@Service
public class EswWithdrawalService extends EswBaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswWithdrawalService.class);

	@Autowired
	private UserService userService;
	@Autowired
	private EswAcctService eswAcctService;
	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private FundAcctBalService fundAcctBalService;
	@Autowired
	private EswCashOrderRepository eswCashOrderRepository;
	@Autowired
	private EswWithdrawalRequiresNewService withdrawalRequiresNewService;
	@Autowired
	private EswAcctBalanceService eswAcctBalanceService;
	@Autowired
	private JobWorkService jobWorkService;

	/**
	 * 提现
	 * 
	 * @param userId
	 * @param eswWithdrawalDto
	 */
	@Transactional
	public void withdraw(String userId, EswWithdrawalDto eswWithdrawalDto) {
		LOGGER.info("withdraw() invoked");
		if (!CommonBusinessUtil.isMarketOpen())
			throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
		if (jobWorkService.isBatchBizTaskProcessing()) {
			throw new BizServiceException(
					EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
		}
		EbcWithdrawalRequest request = buildWithdraw();
		String payPwd = MD5.md5(eswWithdrawalDto.getPayPwd());
		String authCode = eswWithdrawalDto.getAuthCode();
		BigDecimal amount = eswWithdrawalDto.getAmount();
		String trxMemo = eswWithdrawalDto.getTrxMemo();
		// 根据userId获取提现订单信息（托管账户表中取得）
		EswAcctPo eswAcctPo = eswAcctService.getEscrowAccountByUserId(userId);
		String userNo = eswAcctPo.getEswUserNo();// 会员托管编号
		String cardNo = eswAcctPo.getEswSubAcctNo();// 电子账户号
		String bankCardNo = eswAcctPo.getBankCardNo();// 银行卡号
		String payeeName = eswAcctPo.getBankCardName();// 收款人姓名
		String idCard = userService.getIdNo(userId);
		String acctNo = eswAcctPo.getAcctNo();
		String eswCustAcctNo = eswAcctPo.getEswCustAcctNo();
		String eswAcctNo = eswAcctPo.getEswAcctNo();// 钱包介质ID
		String eswSubAcctNo = eswAcctPo.getEswSubAcctNo();
		String curr = request.getCurrency();
		Date trxDate = CommonBusinessUtil.getCurrentWorkDate();// 提现日期

		eswWithdrawalDto.setBankCardName(payeeName);// 银行账户名
		eswWithdrawalDto.setBankCardNo(bankCardNo);// 银行账号

		EswCashOrderDto cashOrderDto = new EswCashOrderDto();
		cashOrderDto.setTrxDt(trxDate);
		cashOrderDto.setAcctNo(acctNo);
		cashOrderDto.setUserId(userId);
		cashOrderDto.setEswCustAcctNo(eswCustAcctNo);
		cashOrderDto.setEswAcctNo(eswAcctNo);
		cashOrderDto.setEswSubAcctNo(eswSubAcctNo);
		cashOrderDto.setEswUserNo(userNo);
		cashOrderDto.setBankCardNo(bankCardNo);
		cashOrderDto.setBankCardName(payeeName);
		cashOrderDto.setCurr(curr);
		cashOrderDto.setTrxAmt(amount);
		cashOrderDto.setTrxMemo(trxMemo);
		cashOrderDto.setAcctType(request.getAccountType());
		cashOrderDto.setOrdType(request.getOrderType());
		cashOrderDto.setCreateOpId(userId);// 创建用户
		cashOrderDto.setCreateTs(new Date());// 创建时间
		cashOrderDto.setStatus(EOrderStatusEnum.WAITING);// 待处理
		// 保存提现指令,返回带流水号的指令
		EswCashOrderDto retDto = withdrawalRequiresNewService
				.saveNewWithdrawalOrder(cashOrderDto);

		request.setDsOrderId(retDto.getOrderId());// 电商订单号
		request.setUserNo(userNo);
		request.setMediumNo(eswAcctNo);
		request.setCardNo(cardNo);
		request.setInCardNo(bankCardNo);
		request.setUserName(payeeName);
		request.setAmount(amount);
		request.setIdCard(idCard);
		request.setAutoMobile(authCode);
		request.setPayPass(payPwd);
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcWithdrawalResponse response = (EbcWithdrawalResponse) rawResponse;
			WithdrawalRespDto respDto = new WithdrawalRespDto();
			respDto.setOrderId(retDto.getOrderId());
			respDto.setRetCode(response.getReturnCode());
			respDto.setRetMsg(StringUtils.defaultIfBlank(response.getErrText(),
					"处理成功"));
			respDto.setBalance(EbcNumberUtil.formatMoney(response.getBalance()));
			respDto.setCashId(response.getCashId());
			respDto.setStatus(EOrderStatusEnum.SUCCESS);// 成功;
			respDto.setOutOrderNo(response.getOrderId());
			withdrawalRequiresNewService.saveWithdrawalOrderSuccess(respDto,
					userId);
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			WithdrawalRespDto respDto = new WithdrawalRespDto();
			respDto.setOrderId(retDto.getOrderId());
			respDto.setRetCode(response.getReturnCode());
			respDto.setRetMsg(response.getErrText());
			respDto.setStatus(EOrderStatusEnum.FAILED);// 失败
			withdrawalRequiresNewService.saveWithdrawalOrderFailed(respDto,
					userId);
			EErrorCode errorCode = EErrorCode.EBC_WITHDRAWAL_ERROR;
			errorCode.setArgs(new Object[] { response.getErrText() });
			throw new BizServiceException(errorCode);
		}
		LOGGER.debug("withdraw() completed");
	}

	private EbcWithdrawalRequest buildWithdraw() {
		EbcWithdrawalRequest request = new EbcWithdrawalRequest();
		request.setOrderSn(IdUtil.produce());
		request.setMerchNo(EswUtils.getEswMerChNo());
		request.setOwnerId(EswUtils.getEswServProv());
		request.setCurrency(EBCConsts.CURRENCY);
		request.setAccountType(EBCConsts.ACCOUNT_TYPE);
		request.setOrderType(EEbcCommandType.WITHDRAWAL.getText());
		return request;
	}

	/**
	 * 提现信息查询(分页)
	 * 
	 * @param userId
	 * @param searchDto
	 * @return
	 * @throws BizServiceException
	 */
	@Transactional
	public DataTablesResponseDto<EswWithdrawalInfoDto> getEswWithdrawalInfoList(
			final String userId, final EswWithdrawalSearchDto searchDto)
			throws BizServiceException {
		Pageable pageRequest = PaginationUtil.buildPageRequest(searchDto);
		Specification<EswCashOrderPo> specification = new Specification<EswCashOrderPo>() {
			@Override
			public Predicate toPredicate(Root<EswCashOrderPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				if (!securityContext.isPlatformUser()) {
					expressions.add(cb.equal(root.<String> get("userId"),
							userId));
				}
				if (searchDto != null) {
					EOrderStatusEnum orderStatus = searchDto
							.geteOrderStatusEnum();
					String cashId = searchDto.getCashId();// 交易编号
					String acctNo = searchDto.getAcctNo();
					Date fromDate = searchDto.getFromDate();
					Date toDate = searchDto.getToDate();
					if (StringUtils.isNotBlank(acctNo)) {
						expressions.add(cb.equal(root.<String> get("acctNo"),
								acctNo));
					}
					if (null != orderStatus
							&& EOrderStatusEnum.ALL != orderStatus) {
						expressions.add(cb.equal(
								root.<EOrderStatusEnum> get("status"),
								orderStatus));
					}
					if (StringUtils.isNotBlank(cashId)) {
						expressions.add(cb.equal(root.<String> get("cashId"),
								cashId));
					}
					if (null != fromDate) {
						try {
							expressions.add(cb.greaterThanOrEqualTo(
									root.<Date> get("trxDt"),
									DateUtils.getStartDate(fromDate)));
						} catch (ParseException e) {
							throw new BizServiceException(
									EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
					if (null != toDate) {
						try {
							expressions.add(cb.lessThanOrEqualTo(
									root.<Date> get("trxDt"),
									DateUtils.getEndDate(toDate)));
						} catch (ParseException e) {
							throw new BizServiceException(
									EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
				}
				return predicate;
			}
		};
		Page<EswCashOrderPo> pages = eswCashOrderRepository.findAll(
				specification, pageRequest);
		DataTablesResponseDto<EswWithdrawalInfoDto> withdrawalInfoView = new DataTablesResponseDto<EswWithdrawalInfoDto>();
		List<EswWithdrawalInfoDto> withdrawalInfoList = new ArrayList<EswWithdrawalInfoDto>();
		for (EswCashOrderPo eswCashOrderPo : pages) {
			EswWithdrawalInfoDto dto = ConverterService.convert(eswCashOrderPo,
					EswWithdrawalInfoDto.class);
			withdrawalInfoList.add(dto);
		}
		withdrawalInfoView.setEcho(searchDto.getEcho());
		withdrawalInfoView.setData(withdrawalInfoList);
		withdrawalInfoView.setTotalDisplayRecords(pages.getTotalElements());
		withdrawalInfoView.setTotalRecords(pages.getTotalElements());
		return withdrawalInfoView;
	}

	/**
	 * 提现结果查询
	 * 
	 * @param userId
	 * @param cashId
	 * @return
	 */
	@Transactional
	public EswWithdrawalInfoDto getWithdrawalState(String userId,
			String orderId, String cashId) {
		LOGGER.info("getWithdrawalState() invoked,cashId {}", cashId);
		EswWithdrawalInfoDto dto = new EswWithdrawalInfoDto();
		EswCashOrderPo cashOrder = eswCashOrderRepository.findOne(orderId);
		EbcWithdrawalReconciliationRequest request = new EbcWithdrawalReconciliationRequest();
		request = (EbcWithdrawalReconciliationRequest) buildRequest(request);
		request.setOrderSn(IdUtil.produce());
		request.setMediumNo(cashOrder.getEswAcctNo());
		request.setCashId(cashId);
		request.setUserNo(cashOrder.getEswUserNo());
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcWithdrawalReconciliationResponse response = (EbcWithdrawalReconciliationResponse) rawResponse;
			dto.setBankCardName(cashOrder.getBankCardName());
			dto.setBankCardNo(cashOrder.getBankCardNo());
			dto.setTrxAmt(cashOrder.getTrxAmt());
			dto.setTrxDt(cashOrder.getTrxDt());
			dto.setOrderId(cashOrder.getOrderId());
			dto.setRetCode(response.getReturnCode());
			dto.setRetMsg(response.getErrText());
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			dto.setRetCode(response.getReturnCode());
			dto.setRetMsg(response.getErrText());
		}
		return dto;
	}

	/**
	 * 提现金额是否超过余额（账户余额以及第三方托管账户中余额）
	 * 
	 * @param amount
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isBalEnough(BigDecimal amount, String userId) {
		boolean enough = false;
		BigDecimal balance = fundAcctBalService.getUserCashableAmt(userId);
		enough = balance.compareTo(amount) >= 0;
		if (enough) {
			EswAcctPo eswAcct = eswAcctService.getEscrowAccountByUserId(userId);
			String acctNo = eswAcct.getAcctNo();
			AcctBalnceInfoDto dto = eswAcctBalanceService
					.getEswAcctBalanceInfo(acctNo);
			BigDecimal acctBal = dto.getBalance();
			enough = acctBal.compareTo(amount) >= 0;
		}
		return enough;
	}

}