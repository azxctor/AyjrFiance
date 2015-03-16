package com.hengxin.platform.escrow.service;

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
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.consts.HttpUrlConsts;
import com.hengxin.platform.ebc.dto.EbcRechargeBankListRequest;
import com.hengxin.platform.ebc.dto.EbcRechargeBankListResponse;
import com.hengxin.platform.ebc.dto.bank.RechargeBank;
import com.hengxin.platform.ebc.enums.EEbcCommandType;
import com.hengxin.platform.ebc.service.EswMsgLogService;
import com.hengxin.platform.ebc.util.EbcSignUtil;
import com.hengxin.platform.escrow.dto.EswChargeDto;
import com.hengxin.platform.escrow.dto.EswRechargeOrderDto;
import com.hengxin.platform.escrow.dto.EswRechargeRequestDto;
import com.hengxin.platform.escrow.dto.EswRechargeSearchDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswRechargeOrderPo;
import com.hengxin.platform.escrow.enums.EEbcUserType;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswAcctRepository;
import com.hengxin.platform.escrow.repository.EswRechargeOrderRepository;
import com.hengxin.platform.escrow.utils.EswUtils;
import com.hengxin.platform.fund.util.DateUtils;

@Service
public class EswRechargeService extends EswBaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswRechargeService.class);

	@Autowired
	private EswAcctRepository eswAcctRepository;

	@Autowired
	private EswRechargeOrderRepository eroRepository;

	@Autowired
	private EswRechargeRequiresNewService rechargeRequiresNewService;

	@Autowired(required = false)
	private EswMsgLogService eswMsgLogService;

	@Autowired
	private JobWorkService jobWorkService;

	/**
	 * 获取充值银行列表
	 * 
	 * @return
	 */
	@Transactional
	public List<RechargeBank> getRechargeBankList(EEbcUserType userType) {
		List<RechargeBank> result = new ArrayList<RechargeBank>();
		EbcRechargeBankListRequest request = buildEbcRechargeBankListRequest();
		request.setUserType(userType.getCode());
		EbcRechargeBankListResponse response = (EbcRechargeBankListResponse) request
				.execute();
		result.addAll(response.getBankList());
		return result;
	}

	private EbcRechargeBankListRequest buildEbcRechargeBankListRequest() {
		EbcRechargeBankListRequest request = new EbcRechargeBankListRequest();
		request.setMerchNo(EswUtils.getEswMerChNo());
		return request;
	}

	/**
	 * 充值操作（异步操作）
	 * 
	 * @param userId
	 * @param chargeDto
	 *            ChargeDto
	 * @return
	 */
	@Transactional
	public EswRechargeRequestDto recharge(String userId, EswChargeDto chargeDto) {
		LOGGER.info("recharge() invoked, {}", userId);
		if (!CommonBusinessUtil.isMarketOpen()){
			throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
		}
		if (jobWorkService.isBatchBizTaskProcessing()) {
			throw new BizServiceException(
					EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
		}
		EswAcctPo eswAcctPo = eswAcctRepository.findByUserId(userId);
		EswRechargeRequestDto request = buildRechargeRequestDto(chargeDto, eswAcctPo);

		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		EswRechargeOrderDto newRechargeOrderDto = new EswRechargeOrderDto();
		newRechargeOrderDto.setAcctNo(eswAcctPo.getAcctNo()); // 会员综合账户
		newRechargeOrderDto.setTrxDt(workDate);
		newRechargeOrderDto.setUserId(userId);
		newRechargeOrderDto.setEswCustAcctNo(eswAcctPo.getEswCustAcctNo()); // 托管商户账号
		newRechargeOrderDto.setEswAcctNo(eswAcctPo.getEswAcctNo()); // 托管会员账号
		newRechargeOrderDto.setEswSubAcctNo(eswAcctPo.getEswSubAcctNo()); // 托管子账户编号
		newRechargeOrderDto.setEswUserNo(eswAcctPo.getEswUserNo()); // 托管会员编号
		newRechargeOrderDto.setTrxAmt(chargeDto.getAmount());
		newRechargeOrderDto.setTrxMemo(chargeDto.getProductDesc()); // 交易备注
		newRechargeOrderDto.setOpenBankId(chargeDto.getEbcBankId()); // 发卡行编号
		newRechargeOrderDto.setOrdType(EEbcCommandType.RECHARGE.getCode()); // 订单类型
		newRechargeOrderDto.setCreateOpId(userId);
		newRechargeOrderDto.setCreateTs(new Date());
		newRechargeOrderDto.setStatus(EOrderStatusEnum.PROCESS);
		EswRechargeOrderDto orderDto = rechargeRequiresNewService
				.addNewRechargeOrder(newRechargeOrderDto);

		request.setUsertype(chargeDto.getUserType().getCode());
		request.setDsorderid(orderDto.getOrderId()); // P2P的订单号
		String sign = EbcSignUtil.getSign(request);
		request.setDstbdatasign(sign); 
		rechargeRequiresNewService.updateRechargeOrderReqSignVal(
				orderDto.getOrderId(), sign);
		ObjectMapper mapper = new ObjectMapper();
		try {
			eswMsgLogService.saveRechargeLog(
					mapper.writeValueAsString(request), request);
		} catch (Exception e) {
			LOGGER.debug("error while saveMsgLog", e);
		}
		return request;
	}

	private EswRechargeRequestDto buildRechargeRequestDto(EswChargeDto chargeDto, EswAcctPo eswAcctPo) {
		EswRechargeRequestDto request = new EswRechargeRequestDto();
		request.setMerchno(EswUtils.getEswMerChNo());
		request.setOrdersn(IdUtil.produce());
		request.setOwnerid(EswUtils.getEswServProv());
		request.setCurrency(EBCConsts.CURRENCY);
		// request.setProductdesc(chargeDto.getProductDesc()); // 产品描述
		request.setAmount(chargeDto.getAmount());
		request.setUserno(eswAcctPo.getEswUserNo()); // 用户ID
		request.setMediumno(eswAcctPo.getEswAcctNo()); // 钱包介质id
		request.setCardno(eswAcctPo.getEswSubAcctNo()); // 电子账户
		request.setEbcbankid(chargeDto.getEbcBankId()); // ebc发卡行
		request.setOrdertype(EEbcCommandType.RECHARGE.getCode()); // 订单类型
		request.setDsyburl(getRechargeAsyncRespUrl()); // 电商异步通知地址
		request.setDstburl(getRechargeSyncRespUrl()); // 电商同步通知地址
		return request;
	}

	private String getRechargeAsyncRespUrl() {
		StringBuffer url = new StringBuffer();
		url.append(CommonBusinessUtil.getEbcRespUrl());
		url.append("/");
		url.append(HttpUrlConsts.RECHARGE_ASYNC_RESPONSE_URL);
		return url.toString();
	}

	/**
	 * 获取同步地址
	 * 
	 * @return
	 */
	private String getRechargeSyncRespUrl() {
		return CommonBusinessUtil.getEbcRechargeSuccessRedirectUrl();
	}

	public Page<EswRechargeOrderPo> getEbcRechargeTransList(
			final String userId, final EswRechargeSearchDto searchDto,
			final boolean isPlatformUser) {

		final Date fromDate = searchDto.getFromDate();
		final Date toDate = searchDto.getToDate();
		final String acctNo = searchDto.getAcctNo();
		final EOrderStatusEnum status = searchDto.geteOrderStatusEnum();
		final String vouchNo = searchDto.getVouchNo();
		Pageable pageRequest = buildPageRequest(searchDto);
		Specification<EswRechargeOrderPo> spec = new Specification<EswRechargeOrderPo>() {
			@Override
			public Predicate toPredicate(Root<EswRechargeOrderPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				if (StringUtils.isNotBlank(acctNo)) {
					expressions.add(cb.equal(root.<String> get("acctNo"),
							acctNo));
				}
				if (!isPlatformUser) {
					expressions.add(cb.equal(root.<String> get("userId"),
							userId));
				}
				if (null != status && EOrderStatusEnum.ALL != status) {
					expressions.add(cb.equal(
							root.<EOrderStatusEnum> get("status"), status));
				}
				if (StringUtils.isNotBlank(vouchNo)) {
					expressions.add(cb.equal(root.<String> get("vouchNo"),
							vouchNo));
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
				return predicate;
			}
		};
		return eroRepository.findAll(spec, pageRequest);
	}

	private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
		Sort sort = null;
		List<Integer> sortedColumns = requestDto.getSortedColumns();
		List<String> sortDirections = requestDto.getSortDirections();
		List<String> dataProps = requestDto.getDataProps();
		for (Integer item : sortedColumns) {
			String sortColumn = dataProps.get(item);
			int indexOf = 0;
			if (StringUtils.isNotBlank(sortColumn)
					&& sortColumn.endsWith(".text")) {
				indexOf = sortColumn.lastIndexOf(".text");
			} else if (StringUtils.isNotBlank(sortColumn)
					&& sortColumn.endsWith(".fullText")) {
				indexOf = sortColumn.lastIndexOf(".fullText");
			}
			if (indexOf > 0) {
				sortColumn = sortColumn.substring(0, indexOf);
			}
			String sortDir = sortDirections.get(0);
			sort = new Sort(Direction.fromString(sortDir), sortColumn);
			sort = sort.and(new Sort(Direction.fromString(sortDir), "orderId"));
		}

		PageRequest page = new PageRequest(requestDto.getDisplayStart()
				/ requestDto.getDisplayLength(), requestDto.getDisplayLength(),
				sort);
		return page;
	}

	@Transactional
	public void abolishRechargeOrder(String orderId) {
		EswRechargeOrderPo orderPo = eroRepository.findOne(orderId);
		if(orderPo.getStatus()!=EOrderStatusEnum.SUCCESS){
			orderPo.setStatus(EOrderStatusEnum.ABANDON);
			eroRepository.save(orderPo);
		}
	}
}
