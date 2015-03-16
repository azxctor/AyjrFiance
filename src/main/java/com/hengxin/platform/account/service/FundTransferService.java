package com.hengxin.platform.account.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hengxin.platform.account.dto.BatchTransferExcelRowMsgDto;
import com.hengxin.platform.account.dto.FundTransferDto;
import com.hengxin.platform.account.dto.TransferAccountDto;
import com.hengxin.platform.account.dto.TransferApplApproveDto;
import com.hengxin.platform.account.dto.TransferApplDto;
import com.hengxin.platform.account.dto.downstream.TransferDetailsSearchDto;
import com.hengxin.platform.account.dto.upstream.BatchTransferExcelMsgDto;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.ExcelCellsTypeRecognizer;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.entity.TransferApplPo;
import com.hengxin.platform.fund.enums.EAcctType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.repository.TransferApplRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.FundBatchTransferService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.SecurityContext;

/**
 * 资金划转.
 * 
 * @author jishen
 * 
 */
@Service
public class FundTransferService {

	@Autowired
	private AcctRepository acctRepository;
	@Autowired
	private SubAcctRepository subAcctRepository;
	@Autowired
	private FundAcctBalService fundAcctBalService;
	@Autowired
	private BankAcctRepository bankAcctRepository;
	@Autowired
	private TransferApplRepository transferApplRepository;
	@Autowired
	private JobWorkService jobWorkService;
	@Autowired
	private AcctService acctService;
	@Autowired
	private FundBatchTransferService fundBatchTransferService;
	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private ExcelManagerService excelService;

	/**
	 * 资金划转申请
	 * 
	 * @param req
	 */
	@Transactional
	public void transferAppl(FundTransferDto fundTransferDto) {
		TransferApplPo transferApplPo = ConverterService.convert(
				fundTransferDto, TransferApplPo.class);
		transferApplPo.setCreateOpid(securityContext.getCurrentUserId());
		transferApplPo.setCreateTs(new Date());
		transferApplPo.setApplDt(CommonBusinessUtil.getCurrentWorkDate());
		transferApplPo.setEventId(IdUtil.produce());
		transferApplPo.setApplStatus(EFundApplStatus.WAIT_APPROVAL);
		transferApplRepository.save(transferApplPo);
	}

	/**
	 * 资金划转
	 * 
	 * @param req
	 */
	@Transactional
	public void transferAccount(TransferApplApproveDto transferApplApproveDto) {
		if (jobWorkService.isBatchBizTaskProcessing()) {
			throw new BizServiceException(
					EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
		}
		String curOpId = securityContext.getCurrentUserId();
		TransferApplPo transferApplPo = null;
		Boolean canPay = null;
		AcctPo payerAcctPo = null;
		AcctPo payeeAcctPo = null;
		for (String applNo : transferApplApproveDto.getApplNos()) {
			transferApplPo = transferApplRepository.findOne(applNo);
			if (EFundApplStatus.APPROVED == transferApplApproveDto
					.getApplStatus()) {
				payerAcctPo = acctService.getAcctByAcctNo(transferApplPo
						.getPayerAcctNo());
				canPay = acctService.acctCanPay(payerAcctPo.getUserId());
				if (!canPay) {
					EErrorCode errorCode2 = EErrorCode.ACCT_CAN_NOT_PAY_IN_BATCH_TRANSFER;
					errorCode2
							.setArgs(new Object[] { payerAcctPo.getAcctNo() });
					throw new BizServiceException(errorCode2);
				}
				payeeAcctPo = acctService.getAcctByAcctNo(transferApplPo
						.getPayeeAcctNo());
				TransferInfo payerInfo = new TransferInfo();
				payerInfo.setRelZQ(false);
				payerInfo.setTrxAmt(transferApplPo.getTrxAmt());
				payerInfo.setTrxMemo(transferApplPo.getTrxMemo());
				payerInfo.setUserId(payerAcctPo.getUserId());
				payerInfo.setUseType(transferApplPo.getUseType());

				TransferInfo payeeInfo = new TransferInfo();
				payeeInfo.setRelZQ(false);
				payeeInfo.setTrxAmt(transferApplPo.getTrxAmt());
				payeeInfo.setTrxMemo(transferApplPo.getTrxMemo());
				payeeInfo.setUserId(payeeAcctPo.getUserId());
				payeeInfo.setUseType(transferApplPo.getUseType());

				fundBatchTransferService.batchTransferAmt(IdUtil.produce(), 
						Arrays.asList(payerInfo), Arrays.asList(payeeInfo), 
						false, transferApplPo.getApplNo(), 
						securityContext.getCurrentUserId(), 
						CommonBusinessUtil.getCurrentWorkDate());
			}

			transferApplPo
					.setApplStatus(transferApplApproveDto.getApplStatus());
			transferApplPo.setApprOpid(curOpId);
			transferApplPo.setApprTs(new Date());
			transferApplPo.setLastMntOpid(curOpId);
			transferApplPo.setLastMntTs(new Date());
			transferApplPo.setDealMemo(transferApplApproveDto.getDealMemo());
			transferApplRepository.save(transferApplPo);
		}
	}

	/**
	 * 获取账户详情
	 * 
	 * @param userId
	 * @return
	 */
	public ResultDto getCurrentAccountDetail(String acctNo, EAcctType acctType) {

		if (acctNo.equals(CommonBusinessUtil.getExchangeAccountNo())
				&& EAcctType.DEBT == acctType) {
			return ResultDtoFactory.toNack(MessageUtil.getMessage(
					"myaccount.acctno.not.match", acctNo));
		}
		AcctPo acctPo = acctRepository.findAcctByAcctNoAndAcctType(acctNo,
				acctType.getCode());
		if (acctPo == null) {
			return ResultDtoFactory.toNack(MessageUtil.getMessage(
					"myaccount.acctno.not.exist", acctNo));
		}

		SubAcctPo currentAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctPo.getAcctNo(),
						ESubAcctNo.CURRENT.getCode());

		TransferAccountDto transferAccountDto = new TransferAccountDto();
		BigDecimal availableBal = caculateAvailableAmt(currentAcctPo);
		transferAccountDto.setAvailableBal(availableBal);
		transferAccountDto.setUserId(acctPo.getUserId());

		List<BankAcctPo> list = bankAcctRepository.findBankAcctByUserId(acctPo
				.getUserId());
		if (null == list || list.isEmpty()) {
			return ResultDtoFactory.toNack("用户银行资料信息不存在");
        }
		BankAcctPo bankAcctPo = list.get(0);
		transferAccountDto.setBnkAcctName(bankAcctPo.getBnkAcctName());
		transferAccountDto.setBnkAcctNo(bankAcctPo.getBnkAcctNo());
		transferAccountDto.setBnkName(null == bankAcctPo.getBnkCd() ? ""
				: SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.BANK,
						bankAcctPo.getBnkCd()).getText());

		ResultDto resultDto = ResultDtoFactory.toAck(MessageUtil.getMessage(
				"myaccount.acctno.query.success", acctNo), transferAccountDto);
		resultDto.setCode("");

		return resultDto;
	}

	/**
	 * 计算可用金额
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
	 * 获取转账交易记录
	 * 
	 * @param query
	 * @return
	 */
	public DataTablesResponseDto<TransferApplDto> getTransferDetails(
			final TransferDetailsSearchDto searchDto) {
		Specification<TransferApplPo> specification = new Specification<TransferApplPo>() {

			@Override
			public Predicate toPredicate(Root<TransferApplPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();

				if (null != searchDto.getUseType()) {
					expressions.add(cb.equal(root.get("useType"),
							searchDto.getUseType()));
				}
				if (null != searchDto.getEventId()
						&& !"".equals(searchDto.getEventId())) {
					expressions.add(cb.equal(root.get("eventId"),
							searchDto.getEventId()));
				}
				if (null != searchDto.getPayerAcctNo()
						&& !"".equals(searchDto.getPayerAcctNo())) {
					expressions.add(cb.equal(root.get("payerAcctNo"),
							searchDto.getPayerAcctNo()));
				}
				if (null != searchDto.getPayeeAcctNo()
						&& !"".equals(searchDto.getPayeeAcctNo())) {
					expressions.add(cb.equal(root.get("payeeAcctNo"),
							searchDto.getPayeeAcctNo()));
				}
				if (null != searchDto.getFromDate()) {
					try {
						expressions
								.add(cb.greaterThanOrEqualTo(root.get("applDt")
										.as(Date.class), DateUtils
										.getStartDate(searchDto.getFromDate())));
					} catch (ParseException e) {
						throw new BizServiceException(
								EErrorCode.COMM_INTERNAL_ERROR,"",e);
					}
				}
				if (null != searchDto.getToDate()) {
					try {
						expressions.add(cb.lessThanOrEqualTo(root.get("applDt")
								.as(Date.class), DateUtils.getEndDate(searchDto
								.getToDate())));
					} catch (ParseException e) {
						throw new BizServiceException(
								EErrorCode.COMM_INTERNAL_ERROR,"",e);
					}
				}
				if (null != searchDto.getApplStatus()
						&& EFundApplStatus.ALL != searchDto.getApplStatus()) {
					expressions.add(cb.equal(root.get("applStatus"),
							searchDto.getApplStatus()));
				}
				return predicate;
			}
		};
		Pageable pageable = this.buildPageRequest(searchDto);

		Page<TransferApplPo> subAcctTrxJnlPos = transferApplRepository.findAll(
				specification, pageable);
		return packAccountDetailsDtos(subAcctTrxJnlPos);
	}

	private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
		Sort sort = null;
		List<Integer> sortedColumns = requestDto.getSortedColumns();
		List<String> sortDirections = requestDto.getSortDirections();
		List<String> dataProps = requestDto.getDataProps();

		for (Integer item : sortedColumns) {
			if ("0".equals(dataProps.get(item))) {
				item++;
			}
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
			if ("applDt".equals(sortColumn)) {
				sortColumn = "createTs";
			}

			String sortDir = sortDirections.get(0);
			sort = new Sort(Direction.fromString(sortDir), sortColumn);
			sort = sort.and(new Sort(Direction.fromString(sortDir), "applNo"));
		}

		PageRequest page = new PageRequest(requestDto.getDisplayStart()
				/ requestDto.getDisplayLength(), requestDto.getDisplayLength(),
				sort);
		return page;
	}

	/**
	 * 包装为返回Dto
	 * 
	 * @param transferAppls
	 * @return
	 */
	private DataTablesResponseDto<TransferApplDto> packAccountDetailsDtos(
			Page<TransferApplPo> transferAppls) {
		DataTablesResponseDto<TransferApplDto> result = new DataTablesResponseDto<TransferApplDto>();
		List<TransferApplDto> transferDetailsDtos = new ArrayList<TransferApplDto>();
		for (TransferApplPo transferAppl : transferAppls) {
			TransferApplDto transferDetailsDto = ConverterService.convert(
					transferAppl, TransferApplDto.class);
			transferDetailsDtos.add(transferDetailsDto);
		}
		result.setData(transferDetailsDtos);
		result.setTotalDisplayRecords(transferAppls.getTotalElements());
		result.setTotalRecords(transferAppls.getTotalElements());
		return result;
	}

	@Transactional
	@Deprecated
	public void fundBatchTransfer(BatchTransferExcelMsgDto msgDto,
			String currOpId) {
		if (jobWorkService.isBatchBizTaskProcessing()) {
			throw new BizServiceException(
					EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
		}
		String eventId = IdUtil.produce();
		String bizId = IdUtil.produce();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		List<BatchTransferExcelRowMsgDto> transferMsgs = msgDto
				.getTransferMsgs();
		EFundUseType fundUseType = msgDto.getUseType();
		Map<String, TransferInfo> payMap = new HashMap<String, TransferInfo>();
		Map<String, List<TransferInfo>> receiveMap = new HashMap<String, List<TransferInfo>>();
		EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
		if (!transferMsgs.isEmpty()) {
			String exchangeAcctNo = CommonBusinessUtil.getExchangeAccountNo();
			for (BatchTransferExcelRowMsgDto msg : transferMsgs) {
				String payerAcctNo = msg.getPayerAcctNo();
				String payerName = msg.getPayerName();
				String receiverAcctNo = msg.getReceiverAcctNo();
				String receiverName = msg.getReceiverName();
				if (StringUtils.isEmpty(payerAcctNo)
						|| StringUtils.isEmpty(receiverAcctNo)) {
					errorCode.setArgs(new Object[] { "收款方或付款方账号不能为空" });
					throw new BizServiceException(errorCode);
				}
				if (StringUtils.isEmpty(receiverName)) {
					errorCode.setArgs(new Object[] { "收款方名称不能为空" });
					throw new BizServiceException(errorCode);
				}
				if (StringUtils.isEmpty(msg.getTrxAmt())) {
					errorCode.setArgs(new Object[] { "交易金额不能为空" });
					throw new BizServiceException(errorCode);
				}
				if (StringUtils.equals(payerAcctNo, receiverAcctNo)) {
					errorCode.setArgs(new Object[] { "收、付方账号相同，均为"
							+ payerAcctNo });
					throw new BizServiceException(errorCode);
				}
				if (fundUseType == EFundUseType.TRANSFERMM) {
					if (StringUtils.equals(exchangeAcctNo, payerAcctNo)
							|| StringUtils.equals(exchangeAcctNo,
									receiverAcctNo)) {
						errorCode.setArgs(new Object[] { "内部转账，收、付方均不能为平台账户"
								+ exchangeAcctNo });
						throw new BizServiceException(errorCode);
					}
				} else if (fundUseType == EFundUseType.TRANSFERPM) {
					if (!StringUtils.equals(exchangeAcctNo, payerAcctNo)
							&& !StringUtils.equals(exchangeAcctNo,
									receiverAcctNo)) {
						errorCode.setArgs(new Object[] { "平台转账，收、付方必须有一方为平台账户"
								+ exchangeAcctNo });
						throw new BizServiceException(errorCode);
					}
				}
				BigDecimal trxAmt = BigDecimal.valueOf(Double.valueOf(msg
						.getTrxAmt()));
				if (trxAmt.compareTo(BigDecimal.ZERO) <= 0) {
					continue;
				}
				if (payMap.containsKey(payerAcctNo)) {
					TransferInfo payinfo = payMap.get(payerAcctNo);
					payinfo.setTrxAmt(payinfo.getTrxAmt().add(trxAmt));
				} else {
					AcctPo acct = acctService.getAcctByAcctNo(payerAcctNo);
					if (acct == null) {
						errorCode.setArgs(new Object[] { "付款方账号" + payerAcctNo
								+ "不存在" });
						throw new BizServiceException(errorCode);
					}
					if (!acctService.isAcctMatchUser(acct.getAcctNo(),
							payerName)) {
						errorCode.setArgs(new Object[] { "付款方账号" + payerAcctNo
								+ "和付款人" + payerName + "不匹配" });
						throw new BizServiceException(errorCode);
					}
					boolean canPay = acctService.acctCanPay(acct);
					if (!canPay) {
						EErrorCode errorCode2 = EErrorCode.ACCT_CAN_NOT_PAY_IN_BATCH_TRANSFER;
						errorCode2.setArgs(new Object[] { acct.getAcctNo() });
						throw new BizServiceException(errorCode2);
					}
					TransferInfo payinfo = new TransferInfo();
					payinfo.setRelZQ(false);
					payinfo.setTrxAmt(trxAmt);
					payinfo.setTrxMemo(msg.getMemo());
					payinfo.setUserId(acct.getUserId());
					payinfo.setUseType(fundUseType);
					payMap.put(payerAcctNo, payinfo);
				}
				AcctPo acct = acctService.getAcctByAcctNo(receiverAcctNo);
				if (acct == null) {
					errorCode.setArgs(new Object[] { "收款方账号" + receiverAcctNo
							+ "不存在" });
					throw new BizServiceException(errorCode);
				}
				if (!acctService
						.isAcctMatchUser(acct.getAcctNo(), receiverName)) {
					errorCode.setArgs(new Object[] { "收款方账号" + receiverAcctNo
							+ "和收款人" + receiverName + "不匹配" });
					throw new BizServiceException(errorCode);
				}
				TransferInfo recvMap = new TransferInfo();
				recvMap.setRelZQ(false);
				recvMap.setTrxAmt(trxAmt);
				recvMap.setTrxMemo(msg.getMemo());
				recvMap.setUserId(acct.getUserId());
				recvMap.setUseType(fundUseType);
				if (receiveMap.containsKey(receiverAcctNo)) {
					receiveMap.get(receiverAcctNo).add(recvMap);
				} else {
					List<TransferInfo> infos = new ArrayList<TransferInfo>();
					infos.add(recvMap);
					receiveMap.put(receiverAcctNo, infos);
				}
			}
		}
		if (payMap.values().size() > 0) {
			Iterator<TransferInfo> ir = payMap.values().iterator();
			while (ir.hasNext()) {
				TransferInfo info = ir.next();
				// 融资人可用余额（加小微宝）
				BigDecimal acctAvlBal = fundAcctBalService
						.getUserCurrentAcctAvlBal(info.getUserId(), true);
				if (acctAvlBal.compareTo(info.getTrxAmt()) < 0) {
					AcctPo acct = acctService.getAcctByUserId(info.getUserId());
					EErrorCode errorCode2 = EErrorCode.ACCT_AVL_NOT_ENOUGH_IN_BATCH_TRANSFER;
					errorCode2.setArgs(new Object[] { acct.getAcctNo() });
					throw new BizServiceException(errorCode2);
				}
			}
		}
		List<TransferInfo> payerList = new ArrayList<TransferInfo>();
		List<TransferInfo> receiveList = new ArrayList<TransferInfo>();
		if (!payMap.isEmpty()) {
			payerList.addAll(payMap.values());
		}
		if (!receiveMap.isEmpty()) {
			Iterator<List<TransferInfo>> ir = receiveMap.values().iterator();
			while (ir.hasNext()) {
				receiveList.addAll(ir.next());
			}
		}
		if (!payerList.isEmpty() && !receiveList.isEmpty()) {
			fundBatchTransferService.batchTransferAmt(eventId, payerList,
					receiveList, true, bizId, currOpId, workDate);
		}
	}

	public List<BatchTransferExcelRowMsgDto> resolveExcelToTransferMsg(
			MultipartFile file) {
		if (file.isEmpty()) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_MSG_EMPTY,
					"EXCEL文件内容为空");
		}
		String templatePath = AppConfigUtil.getExcelBatchTransferTemplatePath();
		Workbook templateWorkbook;
		try {
			templateWorkbook = excelService.createWorkbook(excelService
					.buildExcelFilePath(templatePath));
		} catch (IOException e) {
			EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
			errorCode.setArgs(new Object[] { "模板文件解析错误" });
			throw new BizServiceException(errorCode,"",e);
		}
		Workbook importWorkbook;
		try {
			importWorkbook = excelService.createWorkbook(file.getInputStream(),
					file.getOriginalFilename());
		} catch (IllegalArgumentException e1) {
			EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
			errorCode.setArgs(new Object[] { "只支持xls/xlsx格式的文件" });
			throw new BizServiceException(errorCode,"",e1);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_RESOLVE_FAILED,
					"EXCEL文件解析失败",e);
		}
		checkWithTemplate(importWorkbook, templateWorkbook, 6);
		List<BatchTransferExcelRowMsgDto> importMsg = resolveExcelToTransferMsg(
				importWorkbook, false);
		if (importMsg.isEmpty()) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_MSG_EMPTY,
					"EXCEL文件内容为空");
		}
		return importMsg;
	}

	public List<BatchTransferExcelRowMsgDto> resolveExcelToTransferMsg(
			String filePath, boolean needTitle) {
		Workbook workbook;
		try {
			workbook = excelService.createWorkbook(filePath);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_RESOLVE_FAILED,
					"EXCEL文件解析失败", e);
		}
		return resolveExcelToTransferMsg(workbook, needTitle);
	}

	/**
	 * Description: TODO
	 * 
	 * @param importMsg
	 * @param templateMsg
	 */

	private void checkWithTemplate(Workbook importBook, Workbook templateBook,
			int rowLength) {
		if (templateBook == null) {
			return;
		}
		Sheet templateSheet = excelService.getSheet(templateBook, 0);
		Sheet importSheet = excelService.getSheet(importBook, 0);
		Object[] templateObject = null;
		Object[] importObject = null;
		try {
			Row templateRow = templateSheet.getRow(templateSheet
					.getFirstRowNum());
			templateObject = excelService.listFromRow(templateRow, rowLength);
		} catch (Exception e) {
			EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
			errorCode.setArgs(new Object[] { "模板文件解析错误" });
			throw new BizServiceException(errorCode,"",e);
		}
		try {
			Row importRow = importSheet.getRow(importSheet.getFirstRowNum());
			importObject = excelService.listFromRow(importRow, rowLength);
		} catch (Exception e) {
			EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
			errorCode.setArgs(new Object[] { "导入文件与模板不符" });
			throw new BizServiceException(errorCode,"",e);
		}
		for (int c = 0; c < rowLength; c++) {
			if (!StringUtils.equals(templateObject[0].toString(),
					importObject[0].toString())) {
				EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
				errorCode.setArgs(new Object[] { "导入文件与模板不符" });
				throw new BizServiceException(errorCode);
			}
		}
	}

	public List<BatchTransferExcelRowMsgDto> resolveExcelToTransferMsg(
			Workbook workbook, boolean needTitle) {
		if (workbook == null) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_RESOLVE_FAILED,
					"EXCEL文件解析失败");
		}
		Sheet sheet = null;
		try {
			sheet = excelService.getSheet(workbook, 0);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_MSG_EMPTY,
					"EXCEL文件内容为空",e);
		}
		List<BatchTransferExcelRowMsgDto> msgs = resolveExcelToTransferMsg(sheet);
		if (msgs.isEmpty()) {
			throw new BizServiceException(EErrorCode.ACCT_EXCEL_MSG_EMPTY,
					"EXCEL文件内容为空");
		}
		if (!needTitle) {
			// 移除表头
			msgs.remove(0);
		}
		return msgs;
	}

	public List<BatchTransferExcelRowMsgDto> resolveExcelToTransferMsg(
			Sheet sheet) {
		List<BatchTransferExcelRowMsgDto> transferMsgs = new ArrayList<BatchTransferExcelRowMsgDto>();
		int[] cellTypes = new int[] { Cell.CELL_TYPE_STRING,
				Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_STRING,
				Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC,
				Cell.CELL_TYPE_STRING };
		ExcelCellsTypeRecognizer recognizer = new ExcelCellsTypeRecognizer(
				cellTypes);
		List<Object[]> objects = null;
		try {
			objects = excelService.listFromSheet(sheet, true, 6, recognizer);
		} catch (Exception e) {
			EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
			errorCode.setArgs(new Object[] { "转账金额只支持数字" });
			throw new BizServiceException(errorCode);
		}
		if (objects != null && !objects.isEmpty()) {
			for (Object[] obj : objects) {
				if (obj.length == 6) {
					BatchTransferExcelRowMsgDto msg = new BatchTransferExcelRowMsgDto();
					if (obj[0] != null) {
						msg.setPayerAcctNo(obj[0].toString());
					}
					if (obj[1] != null) {
						msg.setPayerName(obj[1].toString());
					}
					if (obj[2] != null) {
						msg.setReceiverAcctNo(obj[2].toString());
					}
					if (obj[3] != null) {
						msg.setReceiverName(obj[3].toString());
					}
					if (obj[4] != null) {
						msg.setTrxAmt(obj[4].toString());
					}
					if (obj[5] != null) {
						msg.setMemo(obj[5].toString());
					}
					transferMsgs.add(msg);
				}
			}
		}
		return transferMsgs;
	}

	/**
	 * 生成批量转账申请表
	 * 
	 * @param msgDto
	 * @param currOpId
	 */
	@Transactional
	public void createTransferAppl(BatchTransferExcelMsgDto msgDto,
			String currOpId) {
		if (jobWorkService.isBatchBizTaskProcessing()) {
			throw new BizServiceException(
					EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
		}
		String eventId = IdUtil.produce();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		List<BatchTransferExcelRowMsgDto> transferMsgs = msgDto
				.getTransferMsgs();
		EFundUseType fundUseType = msgDto.getUseType();
		String fileName = msgDto.getFileName();
		EErrorCode errorCode = EErrorCode.ACCT_EXCEL_MSG_ERROR;
		if (transferMsgs == null || transferMsgs.isEmpty()) {
			errorCode.setArgs(new Object[] { "EXCEL文件内容为空" });
			throw new BizServiceException(errorCode);
		}
		List<TransferApplPo> appls = new ArrayList<TransferApplPo>();
		String exchangeAcctNo = CommonBusinessUtil.getExchangeAccountNo();
		for (BatchTransferExcelRowMsgDto msg : transferMsgs) {
			String payerAcctNo = msg.getPayerAcctNo();
			String payerName = msg.getPayerName();
			String receiverAcctNo = msg.getReceiverAcctNo();
			String receiverName = msg.getReceiverName();
			String trxMemo = msg.getMemo();
			if (StringUtils.isEmpty(payerAcctNo)
					|| StringUtils.isEmpty(receiverAcctNo)) {
				errorCode.setArgs(new Object[] { "收款方或付款方账号不能为空" });
				throw new BizServiceException(errorCode);
			}
			if (StringUtils.isEmpty(receiverName)) {
				errorCode.setArgs(new Object[] { "收款方名称不能为空" });
				throw new BizServiceException(errorCode);
			}
			if (StringUtils.isEmpty(msg.getTrxAmt())) {
				errorCode.setArgs(new Object[] { "交易金额不能为空" });
				throw new BizServiceException(errorCode);
			}
			if (StringUtils.equals(payerAcctNo, receiverAcctNo)) {
				errorCode.setArgs(new Object[] { "收、付方账号相同，均为" + payerAcctNo });
				throw new BizServiceException(errorCode);
			}
			if (fundUseType == EFundUseType.TRANSFERMM) {
				if (StringUtils.equals(exchangeAcctNo, payerAcctNo)
						|| StringUtils.equals(exchangeAcctNo, receiverAcctNo)) {
					errorCode.setArgs(new Object[] { "内部转账，收、付方均不能为平台账户"
							+ exchangeAcctNo });
					throw new BizServiceException(errorCode);
				}
			} else if (fundUseType == EFundUseType.TRANSFERPM) {
				if (!StringUtils.equals(exchangeAcctNo, payerAcctNo)
						&& !StringUtils.equals(exchangeAcctNo, receiverAcctNo)) {
					errorCode.setArgs(new Object[] { "平台转账，收、付方必须有一方为平台账户"
							+ exchangeAcctNo });
					throw new BizServiceException(errorCode);
				}
			}
			BigDecimal trxAmt = BigDecimal.valueOf(Double.valueOf(msg
					.getTrxAmt()));
			if (trxAmt.compareTo(BigDecimal.ZERO) <= 0) {
				errorCode.setArgs(new Object[] { "付款方" + payerAcctNo + "收款方"
						+ receiverAcctNo + "交易金额必须大于0" });
				throw new BizServiceException(errorCode);
			}
			AcctPo payerAcct = acctService.getAcctByAcctNo(payerAcctNo);
			if (payerAcct == null) {
				errorCode
						.setArgs(new Object[] { "付款方账号" + payerAcctNo + "不存在" });
				throw new BizServiceException(errorCode);
			}
			if (!acctService.isAcctMatchUser(payerAcct.getAcctNo(), payerName)) {
				errorCode.setArgs(new Object[] { "付款方账号" + payerAcctNo + "和付款人"
						+ payerName + "不匹配" });
				throw new BizServiceException(errorCode);
			}
			boolean canPay = acctService.acctCanPay(payerAcct);
			if (!canPay) {
				EErrorCode errorCode2 = EErrorCode.ACCT_CAN_NOT_PAY_IN_BATCH_TRANSFER;
				errorCode2.setArgs(new Object[] { payerAcct.getAcctNo() });
				throw new BizServiceException(errorCode2);
			}
			AcctPo receiverAcct = acctService.getAcctByAcctNo(receiverAcctNo);
			if (receiverAcct == null) {
				errorCode.setArgs(new Object[] { "收款方账号" + receiverAcctNo
						+ "不存在" });
				throw new BizServiceException(errorCode);
			}
			if (!acctService.isAcctMatchUser(receiverAcct.getAcctNo(),
					receiverName)) {
				errorCode.setArgs(new Object[] { "收款方账号" + receiverAcctNo
						+ "和收款人" + receiverName + "不匹配" });
				throw new BizServiceException(errorCode);
			}
			TransferApplPo appl = new TransferApplPo();
			appl.setApplDt(workDate);
			appl.setApplStatus(EFundApplStatus.WAIT_APPROVAL);
			appl.setApprOpid(null);
			appl.setApprTs(null);
			appl.setCreateOpid(currOpId);
			appl.setCreateTs(new Date());
			appl.setDealMemo(null);
			appl.setEventId(eventId);
			appl.setImportFileName(fileName);
			appl.setLastMntOpid(currOpId);
			appl.setLastMntTs(new Date());
			appl.setPayeeAcctNo(receiverAcctNo);
			appl.setPayeeName(receiverName);
			appl.setPayerAcctNo(payerAcctNo);
			appl.setPayerName(payerName);
			appl.setTrxAmt(trxAmt);
			appl.setTrxMemo(trxMemo);
			appl.setUseType(fundUseType);
			appls.add(appl);
		}
		transferApplRepository.save(appls);
	}

}
