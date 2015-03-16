package com.hengxin.platform.escrow.service;

import java.math.BigDecimal;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.dto.EbcErrorResponse;
import com.hengxin.platform.ebc.dto.EbcTransferRequest;
import com.hengxin.platform.ebc.dto.EbcTransferResponse;
import com.hengxin.platform.ebc.enums.EEbcCommandType;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.dto.EswTransferOrderDto;
import com.hengxin.platform.escrow.dto.EswTransferOrderSearchDto;
import com.hengxin.platform.escrow.dto.EswTransferOrderSumDto;
import com.hengxin.platform.escrow.dto.tsfr.TransferRespDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswTransferOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswTransferOrderRepository;
import com.hengxin.platform.escrow.utils.ErrorUtils;
import com.hengxin.platform.escrow.utils.EswUtils;
import com.hengxin.platform.fund.util.DateUtils;

@Service
public class EswTransferService extends EswBaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswTransferService.class);
	
	@Autowired
	private EswAcctService eswAcctService;
	@Autowired
	private EswTransferOrderRepository tsfrRepo;
	@Autowired
	private EswTransferRequiresNewService transferRequiresNewService;

	@Transactional
	public void doTransfer(EswTransferOrderDto order, Date trxDate, String currOpId) {
		try {
			EswTransferOrderDto dto = transferRequiresNewService.getTransferOrderDto(order.getOrderId());
			if(isWaitProcess(dto)){
				// update order status to process
				transferRequiresNewService.updateTransferOrderStatusProcess(order.getOrderId(), currOpId);
				// call ebc transfer
				doEbcTransfer(order.getOrderId(), currOpId);
			}
		} catch (Exception ex) {
			LOGGER.error(
					"do ebc transfer unknow exception, orderId is {}, exception is {} ",
					order.getOrderId(), ex);
			TransferRespDto respDto = new TransferRespDto();
			respDto.setOrderId(order.getOrderId());
			respDto.setStatus(EOrderStatusEnum.UNKNOW);
			respDto.setRetCode(null);
			respDto.setRetMsg(ErrorUtils.getExceptionStack(ex, 200));
			transferRequiresNewService.updateTransferOrderResp(respDto, currOpId);
		}
	}
	
	private boolean isWaitProcess(EswTransferOrderDto dto){
		if(dto.getStatus()==EOrderStatusEnum.WAITING){
			return true;
		}
		return false;
	}

	private void doEbcTransfer(String orderId, String currOpId) {
		EbcTransferRequest req = buildTransferRequest();
		EswTransferOrderDto orderDto = transferRequiresNewService.getTransferOrderDto(orderId);
		EswAcctPo payerAcct = eswAcctService.getEscrowAccountByAcctNo(orderDto.getPayerNo());
		EswAcctPo payeeAcct = eswAcctService.getEscrowAccountByAcctNo(orderDto.getPayeeNo());
		req.setUserNo(payerAcct.getEswUserNo());
		req.setMediumNo(payerAcct.getEswAcctNo());
		req.setCardNo(payerAcct.getEswSubAcctNo());
		// set payPwd value ,and set autoMobile is null
		req.setPayPass(payerAcct.getEswPayPwd());
		req.setAutoMobile(null);
		req.setIncardNo(payeeAcct.getEswSubAcctNo());
		req.setAmount(orderDto.getTrxAmt());
		req.setDescribe(orderDto.getTrxMemo());
		req.setDsOrderId(orderDto.getOrderId());
		CommandResponse resp = req.execute();
		boolean success = super.isSuccessRespsonse(resp);
		if (success) {
			EbcTransferResponse tsfrResp = (EbcTransferResponse) resp;
			String retCode = tsfrResp.getReturnCode();
			String retMsg = tsfrResp.getErrText();
			TransferRespDto respDto = new TransferRespDto();
			respDto.setOrderId(orderId);
			respDto.setStatus(EOrderStatusEnum.SUCCESS);
			respDto.setRetCode(retCode);
			respDto.setRetMsg(StringUtils.defaultIfBlank(retMsg,"处理成功"));
			transferRequiresNewService.updateTransferOrderResp(respDto, currOpId);
		} 
		else {
			EbcErrorResponse errResp = (EbcErrorResponse) resp;
			String retCode = errResp.getReturnCode();
			String retMsg = errResp.getErrText();
			TransferRespDto respDto = new TransferRespDto();
			respDto.setOrderId(orderId);
			respDto.setStatus(EOrderStatusEnum.FAILED);
			respDto.setRetCode(retCode);
			respDto.setRetMsg(retMsg);
			transferRequiresNewService.updateTransferOrderResp(respDto, currOpId);
		}
	}

	private EbcTransferRequest buildTransferRequest() {
		EbcTransferRequest request = new EbcTransferRequest();
		request.setMerchNo(EswUtils.getEswMerChNo());
		request.setOwnerId(EswUtils.getEswServProv());
		request.setCurrency(EBCConsts.CURRENCY);
		request.setAccountType(EBCConsts.ACCOUNT_TYPE);
		request.setOrderSn(IdUtil.produce());
		request.setOrderType(EEbcCommandType.TRANSFER.getCode());
		request.setUserNo(null);
		request.setMediumNo(null);
		request.setCardNo(null);
		request.setIncardNo(null);
		request.setAmount(null);
		request.setAutoMobile(null);
		request.setPayPass(null);
		request.setDescribe(null);
		request.setDsOrderId(null);
		return request;
	}

	@Transactional(readOnly = true)
	public List<EswTransferOrderDto> getTransferOrderToTransfer(final Date trxDate,
			final EOrderStatusEnum status, final Integer rows) {
		Specification<EswTransferOrderPo> spec = new Specification<EswTransferOrderPo>() {
			@Override
			public Predicate toPredicate(Root<EswTransferOrderPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.equal(root.<Date> get("trxDate"), trxDate));
				expressions.add(cb.equal(root.<EOrderStatusEnum> get("status"), status));
				return predicate;
			}
		};
		Sort sort = new Sort(Direction.ASC, "createTs");
		PageRequest page = new PageRequest(0, rows, sort);
		Page<EswTransferOrderPo> poList = tsfrRepo.findAll(spec, page);
		List<EswTransferOrderDto> dtoList = new ArrayList<EswTransferOrderDto>();
		for (EswTransferOrderPo po : poList.getContent()) {
			EswTransferOrderDto dto = ConverterService.convert(po,
					EswTransferOrderDto.class);
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Transactional(readOnly = true)
	public List<EswTransferOrderDto> getTransferOrderDtoList(
			EswTransferOrderSearchDto searchDto) {
		Date trxDate = DateUtils.getDate(searchDto.getTrxDate(),"yyyy-MM-dd");
		List<EswTransferOrderPo> poList = tsfrRepo.findByTrxDateAndStatusOrderByCreateTsAsc(trxDate, searchDto.getStatus());
		List<EswTransferOrderDto> dtoList = new ArrayList<EswTransferOrderDto>();
		for (EswTransferOrderPo po : poList) {
			EswTransferOrderDto dto = ConverterService.convert(po,
					EswTransferOrderDto.class);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public Page<EswTransferOrderPo> getTransferOrderData(
			final EswTransferOrderSearchDto searchDto, 
			final List<EOrderStatusEnum> statusList) {
		final String trxDateStr = searchDto.getTrxDate();
		final EOrderStatusEnum status = searchDto.getStatus();
		final String payerAcctNo = searchDto.getPayerAcctNo();
		final String payeeAcctNo = searchDto.getPayeeAcctNo();
		Pageable pageRequest = buildPageRequest(searchDto);
		Specification<EswTransferOrderPo> spec = new Specification<EswTransferOrderPo>() {
			@Override
			public Predicate toPredicate(Root<EswTransferOrderPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				if (StringUtils.isNotBlank(trxDateStr)) {
					Date trxDate = DateUtils.getDate(trxDateStr, "yyyy-MM-dd");
					expressions.add(cb.equal(root.<Date> get("trxDate"),
							trxDate));
				}
				if (status != null && status!=EOrderStatusEnum.ALL) {
					expressions.add(cb.equal(
							root.<EOrderStatusEnum> get("status"), status));
				}
				else {
					expressions.add(root.<EOrderStatusEnum> get("status").in(statusList));
				}
				if (StringUtils.isNotBlank(payerAcctNo)) {
					expressions.add(cb.equal(root.<String> get("payerNo"),
							payerAcctNo));
				}
				if (StringUtils.isNotBlank(payeeAcctNo)) {
					expressions.add(cb.equal(root.<String> get("payeeNo"),
							payeeAcctNo));
				}
				return predicate;
			}

		};
		return tsfrRepo.findAll(spec, pageRequest);
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
	
	@Transactional(readOnly = true)
	public EswTransferOrderSumDto getTransferOrderSumAmt(EswTransferOrderSearchDto searchDto){
		String payerAcctNo = searchDto.getPayerAcctNo();
		String payeeAcctNo = searchDto.getPayeeAcctNo();
		Date trxDate = DateUtils.getDate(searchDto.getTrxDate(), "yyyy-MM-dd")  ;
		EOrderStatusEnum status = searchDto.getStatus();
		BigDecimal sumPayTrxAmt = tsfrRepo.getPayTransferOrderSumAmt(payerAcctNo, payeeAcctNo, trxDate, status);
		BigDecimal sumRecTrxAmt = tsfrRepo.getRecTransferOrderSumAmt(payerAcctNo, payeeAcctNo, trxDate, status);
		EswTransferOrderSumDto dto = new EswTransferOrderSumDto();
		dto.setTotalPayAmt(sumPayTrxAmt);
		dto.setTotalRecvAmt(sumRecTrxAmt);
		return dto;
	}

}
