package com.hengxin.platform.fund.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.account.dto.BankTrxDtlDto;
import com.hengxin.platform.account.dto.downstream.FundApplApproveDto;
import com.hengxin.platform.account.dto.downstream.ReverseApplSearchDto;
import com.hengxin.platform.account.dto.downstream.ReverseSearchDto;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.dto.biz.req.UserReverseReq;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.entity.ReverseApplPo;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.repository.ReverseApplRepository;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.support.AcctUtils;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * 会员资金账户冲正交易服务
 * 
 * @author dcliu
 * 
 */
@Service
public class UserFundAcctReverseService {
	
	public final static String APPL = "01";
	public final static String APPROVE = "02";
	public final static String REVERSED = "03";

	@Autowired
	private AcctService acctService;
	@Autowired
	private FundAcctService fundAcctService;
	@Autowired
	private ReverseApplRepository reverseApplRepository;
	@Autowired
	private BankTrxJnlRepository bankTrxJnlRepository;
	@Autowired
	private FundAcctReverseService fundAcctReverseService;
	@Autowired
	private JobWorkService jobWorkService;
	
	
	/**
	 * 充值提现处理
	 * @param rwFlag
	 * @param req
	 * @throws BizServiceException
	 */
	@Transactional
	public void doReverseAppl(UserReverseReq req)throws BizServiceException {
	    if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
		this.reverseAppl(req);
	}
	
	private String reverseAppl(UserReverseReq req)throws BizServiceException {
		
		String rvsJnlNo = req.getRvsBnkJnlNo();
		
		BankTrxJnlPo bnkTrxJnl = bankTrxJnlRepository.findByJnlNoAndTrxStatus(rvsJnlNo, EBnkTrxStatus.NORMAL);
		if(bnkTrxJnl==null){
			throw new BizServiceException(EErrorCode.FUND_ACCT_REVERSE_FAILED,
					"充值明细不存在或者已冲正");
		}
		if(bnkTrxJnl.getRvsFlg()==EFlagType.YES){
			throw new BizServiceException(EErrorCode.FUND_ACCT_REVERSE_FAILED,
					"只能针对非冲正交易进行冲正");
		}
		
		BigDecimal rvsAmt = AmtUtils.processNegativeAmt(bnkTrxJnl.getTrxAmt(), BigDecimal.ZERO);
		
		EFundUseType useType = null;
		if(ERechargeWithdrawFlag.RECHARGE == bnkTrxJnl.getRechargeWithdrawFlag()){
			useType = EFundUseType.RECHARGE_REVERSE;
		}else{
			rvsAmt = BigDecimal.ZERO;
			useType = EFundUseType.CASH_REVERSE;
		}
		
		ReserveReq resvReq = new ReserveReq();
		resvReq.setAddXwb(true);
		resvReq.setBizId(rvsJnlNo);
		resvReq.setCurrOpId(req.getCurrOpid());
		resvReq.setTrxAmt(rvsAmt);
		resvReq.setTrxMemo(req.getTrxMemo());
		resvReq.setUserId(bnkTrxJnl.getUserId());
		resvReq.setUseType(useType);
		resvReq.setWorkDate(req.getWorkDate());
		String fnrJnlNo = fundAcctService.specificReserveAmt(resvReq);
		
		ReverseApplPo appl = new ReverseApplPo();
		appl.setApplDt(req.getWorkDate());
		appl.setApplNo(null);
		appl.setApplStatus(EFundApplStatus.WAIT_APPROVAL);
		appl.setApprOpid(null);
		appl.setApprTs(null);
		appl.setCreateOpid(req.getCurrOpid());
		appl.setCreateTs(new Date());
		appl.setDealMemo(null);
		appl.setLastMntOpid(null);
		appl.setLastMntTs(null);
		appl.setUserId(bnkTrxJnl.getUserId());
		appl.setRvsFrzJnlNo(fnrJnlNo);
		appl.setRvsJnlNo(rvsJnlNo);
		appl.setTrxDt(null);
		appl.setTrxMemo(req.getTrxMemo());
		ReverseApplPo retAppl = reverseApplRepository.save(appl);
		
		bnkTrxJnl.setRvsApplNo(retAppl.getApplNo());
		bnkTrxJnl.setTrxStatus(EBnkTrxStatus.DEALING);
		bnkTrxJnl.setDealMemo(req.getTrxMemo());
		bnkTrxJnl.setLastMntOpid(req.getCurrOpid());
		bnkTrxJnl.setLastMntTs(new Date());
		bankTrxJnlRepository.save(bnkTrxJnl);
		return retAppl.getApplNo();
	}
	
	@Transactional
	public void doReverseApprove(FundApplApproveDto req, String currOpId)throws BizServiceException {
		
	    if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
	    
		ReverseApplPo appl = reverseApplRepository.findOne(req.getAppId());
		if(appl==null||EFundApplStatus.WAIT_APPROVAL!=appl.getApplStatus()){
			throw new BizServiceException(EErrorCode.FUND_ACCT_REVERSE_FAILED,
					"充值明细不存在或者已冲正");
		}
		
		BankTrxJnlPo bnkTrxJnl = bankTrxJnlRepository.findByJnlNoAndTrxStatus(appl.getRvsJnlNo(),EBnkTrxStatus.DEALING);
		if(bnkTrxJnl==null){
			throw new BizServiceException(EErrorCode.FUND_ACCT_REVERSE_FAILED,
					"充值明细不存在或者已冲正");
		}
		if(bnkTrxJnl.getRvsFlg()==EFlagType.YES){
			throw new BizServiceException(EErrorCode.FUND_ACCT_REVERSE_FAILED,
					"只能针对非冲正交易进行冲正");
		}
		
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		String comments = req.getComments();
		String trxMemo = appl.getTrxMemo();
		appl.setApplStatus(EFundApplStatus.REJECT);
		if(req.isPassed()){
			appl.setApplStatus(EFundApplStatus.APPROVED);
			comments = StringUtils.defaultIfBlank(comments, "同意");
			trxMemo = "【申请理由】："+trxMemo+"。 【复核理由】： "+comments;
		}else{
			comments = StringUtils.defaultIfBlank(comments, "否决")
					    +"。出入金明细已恢复正常状态。";
			trxMemo = "【申请理由】："+trxMemo+"。【复核理由】："+comments;
		}
		appl.setDealMemo(AcctUtils.substr(comments, 200));
		appl.setApprOpid(currOpId);
		appl.setApprTs(new Date());
		appl.setLastMntOpid(currOpId);
		appl.setLastMntTs(new Date());
		appl.setTrxDt(workDate);
		reverseApplRepository.save(appl);
		
		UnReserveReq unReq = new UnReserveReq();
		unReq.setCurrOpId(currOpId);
		unReq.setReserveJnlNo(appl.getRvsFrzJnlNo());
		unReq.setTrxMemo(AcctUtils.substr(trxMemo, 200));
		unReq.setUserId(bnkTrxJnl.getUserId());
		unReq.setWorkDate(workDate);
		fundAcctService.unReserveAmt(unReq);
		
		if(req.isPassed()){
			String eventId = IdUtil.produce();
			
			AcctPo acct = acctService.getAcctByAcctNo(bnkTrxJnl.getAcctNo());
			BankTrxJnlPo rvsTrxJnl = new BankTrxJnlPo();
			rvsTrxJnl.setAcctNo(bnkTrxJnl.getAcctNo());
			rvsTrxJnl.setBankCode(bnkTrxJnl.getBankCode());
			rvsTrxJnl.setBnkAcctName(bnkTrxJnl.getBnkAcctName());
			rvsTrxJnl.setBnkAcctNo(bnkTrxJnl.getBnkAcctNo());
			rvsTrxJnl.setCashPool(EnumHelper.translate(ECashPool.class, acct.getCashPool()));
			rvsTrxJnl.setCreateOpid(currOpId);
			rvsTrxJnl.setCreateTs(new Date());
			rvsTrxJnl.setDealMemo(trxMemo);
			rvsTrxJnl.setEventId(eventId);
			rvsTrxJnl.setLastMntOpid(null);
			rvsTrxJnl.setLastMntTs(null);
			rvsTrxJnl.setRechargeWithdrawFlag(bnkTrxJnl.getRechargeWithdrawFlag());
			rvsTrxJnl.setRelBnkId(bnkTrxJnl.getRelBnkId());
			rvsTrxJnl.setRvsApplNo(bnkTrxJnl.getRvsApplNo());
			rvsTrxJnl.setRvsDt(workDate);
			rvsTrxJnl.setRvsFlg(EFlagType.YES);
			rvsTrxJnl.setRvsJnlNo(bnkTrxJnl.getJnlNo());
			rvsTrxJnl.setSignedFlg(bnkTrxJnl.getSignedFlg());
			rvsTrxJnl.setSubAcctNo(bnkTrxJnl.getSubAcctNo());
			rvsTrxJnl.setTrxAmt(bnkTrxJnl.getTrxAmt().multiply(BigDecimal.valueOf(-1)));
			rvsTrxJnl.setTrxDt(workDate);
			rvsTrxJnl.setTrxMemo(trxMemo);
			rvsTrxJnl.setTrxStatus(EBnkTrxStatus.NORMAL);
			rvsTrxJnl.setUserId(bnkTrxJnl.getUserId());
			rvsTrxJnl.setUserName(bnkTrxJnl.getUserName());
			BankTrxJnlPo rvsRt = bankTrxJnlRepository.save(rvsTrxJnl);
			
			ERechargeWithdrawFlag rwFlag = bnkTrxJnl.getRechargeWithdrawFlag();
			UserReverseReq rvsReq = new UserReverseReq();
			rvsReq.setCurrOpid(currOpId);
			rvsReq.setRvsBnkJnlNo(bnkTrxJnl.getJnlNo());
			rvsReq.setTrxMemo(AcctUtils.substr(trxMemo, 200));
			rvsReq.setWorkDate(workDate);
			rvsReq.setDealMemo(AcctUtils.substr(trxMemo, 200));
			if(ERechargeWithdrawFlag.RECHARGE == rwFlag){
				this.reverseRechargeAmt(eventId, rvsReq, rvsRt.getJnlNo());
			}else if(ERechargeWithdrawFlag.WITHDRAW == rwFlag){
				this.reverseWithdrawAmt(eventId, rvsReq, rvsRt.getJnlNo());
			}
		}else{
			bnkTrxJnl.setTrxStatus(EBnkTrxStatus.NORMAL);
			bnkTrxJnl.setDealMemo(AcctUtils.substr(trxMemo, 200));
			bnkTrxJnl.setLastMntOpid(currOpId);
			bnkTrxJnl.setLastMntTs(new Date());
			bankTrxJnlRepository.save(bnkTrxJnl);
		}
	}

	/**
	 * 充值冲正
	 * 
	 * @param eventId
	 * @param req
	 * @throws BizServiceException
	 */
	@Transactional
	public void reverseRechargeAmt(String eventId, UserReverseReq req, String bizId)throws BizServiceException {
		
	    if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
		String bankJnlNo = req.getRvsBnkJnlNo();

		BankTrxJnlPo bnkTrxJnl = bankTrxJnlRepository.findByJnlNo(bankJnlNo);

		bnkTrxJnl.setEventId(eventId);
		bnkTrxJnl.setTrxStatus(EBnkTrxStatus.REVERSED);
		bnkTrxJnl.setRvsDt(req.getWorkDate());
		bnkTrxJnl.setDealMemo(req.getDealMemo());
		bnkTrxJnl.setLastMntOpid(req.getCurrOpid());
		bnkTrxJnl.setLastMntTs(new Date());
		bnkTrxJnl.setTrxMemo(AcctUtils.substr(bnkTrxJnl.getTrxMemo() + "，"
				+ req.getTrxMemo(), 200));
		bankTrxJnlRepository.save(bnkTrxJnl);

		TransferInfo payerInfo = new TransferInfo();
		payerInfo.setRelZQ(false);
		payerInfo.setTrxAmt(bnkTrxJnl.getTrxAmt());
		payerInfo.setTrxMemo(req.getTrxMemo());
		payerInfo.setUserId(bnkTrxJnl.getUserId());
		payerInfo.setUseType(EFundUseType.RECHARGE_REVERSE);
		fundAcctReverseService.reverseRechargeAmt(eventId, payerInfo,
				bizId, req.getCurrOpid(), req.getWorkDate());
	}

	/**
	 * 提现冲正
	 * 
	 * @param eventId
	 * @param req
	 * @throws BizServiceException
	 */
	@Transactional
	public void reverseWithdrawAmt(String eventId, UserReverseReq req, String bizId)
			throws BizServiceException {
		
	    if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
	    
		String bankJnlNo = req.getRvsBnkJnlNo();

		BankTrxJnlPo bnkTrxJnl = bankTrxJnlRepository.findByJnlNo(bankJnlNo);

		bnkTrxJnl.setEventId(eventId);
		bnkTrxJnl.setTrxStatus(EBnkTrxStatus.REVERSED);
		bnkTrxJnl.setRvsDt(req.getWorkDate());
		bnkTrxJnl.setDealMemo(req.getDealMemo());
		bnkTrxJnl.setLastMntOpid(req.getCurrOpid());
		bnkTrxJnl.setLastMntTs(new Date());
		bnkTrxJnl.setTrxMemo(AcctUtils.substr(bnkTrxJnl.getTrxMemo() + "，"
				+ req.getTrxMemo(), 200));
		bankTrxJnlRepository.save(bnkTrxJnl);

		TransferInfo payeeInfo = new TransferInfo();
		payeeInfo.setRelZQ(false);
		payeeInfo.setTrxAmt(bnkTrxJnl.getTrxAmt());
		payeeInfo.setTrxMemo(req.getTrxMemo());
		payeeInfo.setUserId(bnkTrxJnl.getUserId());
		payeeInfo.setUseType(EFundUseType.CASH_REVERSE);
		fundAcctReverseService.reverseWithdrawAmt(eventId, payeeInfo,
				bizId, req.getCurrOpid(), req.getWorkDate());
	}
	


	/**
	 * 提现冲正
	 * 
	 * @param eventId
	 * @param req
	 * @throws BizServiceException
	 */
	@Transactional
	public void reverseWithdrawAmtOnBank(String eventId, UserReverseReq req)
			throws BizServiceException {
		
	    if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
	    
		String bankJnlNo = req.getRvsBnkJnlNo();
		BankTrxJnlPo bnkTrxJnl = bankTrxJnlRepository.findByJnlNo(bankJnlNo);
		bnkTrxJnl.setEventId(eventId);
		bnkTrxJnl.setTrxStatus(EBnkTrxStatus.REVERSED);
		bnkTrxJnl.setRvsDt(req.getWorkDate());
		bnkTrxJnl.setDealMemo(req.getDealMemo());
		bnkTrxJnl.setLastMntOpid(req.getCurrOpid());
		bnkTrxJnl.setLastMntTs(new Date());
		bnkTrxJnl.setTrxMemo(AcctUtils.substr(bnkTrxJnl.getTrxMemo() + "，"
				+ req.getTrxMemo(), 200));
		bankTrxJnlRepository.save(bnkTrxJnl);
		
		//银行发起的充值冲正，增加一笔冲正的正常记录
		AcctPo acct = acctService.getAcctByAcctNo(bnkTrxJnl.getAcctNo());
		BankTrxJnlPo rvsTrxJnl = new BankTrxJnlPo();
		rvsTrxJnl.setAcctNo(bnkTrxJnl.getAcctNo());
		rvsTrxJnl.setBankCode(bnkTrxJnl.getBankCode());
		rvsTrxJnl.setBnkAcctName(bnkTrxJnl.getBnkAcctName());
		rvsTrxJnl.setBnkAcctNo(bnkTrxJnl.getBnkAcctNo());
		rvsTrxJnl.setCashPool(EnumHelper.translate(ECashPool.class, acct.getCashPool()));
		rvsTrxJnl.setCreateOpid(req.getCurrOpid());
		rvsTrxJnl.setCreateTs(new Date());
		rvsTrxJnl.setDealMemo(req.getDealMemo());
		rvsTrxJnl.setEventId(eventId);
		rvsTrxJnl.setLastMntOpid(null);
		rvsTrxJnl.setLastMntTs(null);
		rvsTrxJnl.setRechargeWithdrawFlag(bnkTrxJnl.getRechargeWithdrawFlag());
		rvsTrxJnl.setRelBnkId(bnkTrxJnl.getRelBnkId());
		rvsTrxJnl.setRvsApplNo(bnkTrxJnl.getRvsApplNo());
		rvsTrxJnl.setRvsDt(req.getWorkDate());
		rvsTrxJnl.setRvsFlg(EFlagType.YES);
		rvsTrxJnl.setRvsJnlNo(bnkTrxJnl.getJnlNo());
		rvsTrxJnl.setSignedFlg(bnkTrxJnl.getSignedFlg());
		rvsTrxJnl.setSubAcctNo(bnkTrxJnl.getSubAcctNo());
		rvsTrxJnl.setTrxAmt(bnkTrxJnl.getTrxAmt().multiply(BigDecimal.valueOf(-1)));
		rvsTrxJnl.setTrxDt(req.getWorkDate());
		rvsTrxJnl.setTrxMemo(AcctUtils.substr(bnkTrxJnl.getTrxMemo() + "，"
				+ req.getTrxMemo(), 200));
		rvsTrxJnl.setTrxStatus(EBnkTrxStatus.NORMAL);
		rvsTrxJnl.setUserId(bnkTrxJnl.getUserId());
		rvsTrxJnl.setUserName(bnkTrxJnl.getUserName());
		BankTrxJnlPo rvsRt = bankTrxJnlRepository.save(rvsTrxJnl);

		TransferInfo payeeInfo = new TransferInfo();
		payeeInfo.setRelZQ(false);
		payeeInfo.setTrxAmt(bnkTrxJnl.getTrxAmt());
		payeeInfo.setTrxMemo(req.getTrxMemo());
		payeeInfo.setUserId(bnkTrxJnl.getUserId());
		payeeInfo.setUseType(EFundUseType.CASH_REVERSE);
		fundAcctReverseService.reverseWithdrawAmt(eventId, payeeInfo,
				rvsRt.getJnlNo(), req.getCurrOpid(), req.getWorkDate());
	}
	
	public DataTablesResponseDto<BankTrxDtlDto> getBnkTrxDtlList(final ReverseSearchDto searchDto){

        DataTablesResponseDto<BankTrxDtlDto> result = new DataTablesResponseDto<BankTrxDtlDto>();

        Specification<BankTrxJnlPo> spec = new Specification<BankTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<BankTrxJnlPo> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if(searchDto.getTrxStatus()!=null&&searchDto.getTrxStatus()!=EBnkTrxStatus.ALL){
                	expressions.add(cb.equal(root.get("trxStatus"), searchDto.getTrxStatus()));
                }
                if(searchDto.getRvsFlg()!=null&&searchDto.getRvsFlg()!=EFlagType.ALL){
                	expressions.add(cb.equal(root.get("rvsFlg"), searchDto.getRvsFlg()));
                }
                if(StringUtils.isNotBlank(searchDto.getAcctNo())){
                    expressions.add(cb.equal(root.get("acctNo"), searchDto.getAcctNo()));
                }
                if(StringUtils.isNotBlank(searchDto.getBnkAcctNo())){
                	expressions.add(cb.equal(root.get("bnkAcctNo"), searchDto.getBnkAcctNo()));
                }
                if(StringUtils.isNotBlank(searchDto.getBnkAcctName())){
                	expressions.add(cb.like(cb.lower(root.<String> get("bnkAcctName")), 
                			searchDto.getBnkAcctName().toLowerCase() + "%"));
                }
                if(StringUtils.isNotBlank(searchDto.getRelBnkId())){
                	expressions.add(cb.like(cb.lower(root.<String> get("relBnkId")), 
                			searchDto.getRelBnkId().toLowerCase() + "%"));
                }
                if(searchDto.getRechargeWithdrawFlag()!=null
                		&&searchDto.getRechargeWithdrawFlag()!=ERechargeWithdrawFlag.ALL){
                    expressions.add(cb.equal(root.get("rechargeWithdrawFlag"), searchDto.getRechargeWithdrawFlag()));
                }
                if (searchDto.getFromDate()!=null) {
                    try {
                        expressions
                        .add(cb.greaterThanOrEqualTo(root.get("trxDt")
                                .as(Date.class), DateUtils
                                .getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(
                                EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (searchDto.getToDate()!=null) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("trxDt")
                                .as(Date.class), DateUtils.getEndDate(searchDto
                                        .getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(
                                EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                return predicate;
            }
        };
        Pageable pageRequest = buildPageRequest(searchDto);
        Page<BankTrxJnlPo> pages = bankTrxJnlRepository.findAll(spec,
                pageRequest);
        List<BankTrxDtlDto> dtos = new ArrayList<BankTrxDtlDto>();
        if (!pages.getContent().isEmpty()) {
            for(BankTrxJnlPo po : pages.getContent()) {
            	BankTrxDtlDto dto = ConverterService.convert(po, BankTrxDtlDto.class);
            	BigDecimal rvsableAmt = BigDecimal.ZERO;
            	if(po.getRvsFlg()!=EFlagType.YES){
	            	if(po.getTrxStatus()==EBnkTrxStatus.DEALING
	            			||po.getTrxStatus()==EBnkTrxStatus.NORMAL){
		            	if(ERechargeWithdrawFlag.RECHARGE == dto.getRechargeWithdrawFlag()){
			            	if(StringUtils.isNotBlank(dto.getUserId())){
			            		rvsableAmt = fundAcctService.getUserCurrentAndXwbSubAcctAvlAmtIgnoreFrzAmt(dto.getUserId());
			            	}
		            	}
	            	}
            	}
            	dto.setAcctReversableAmt(rvsableAmt);
            	dto.setRvsApplNo(StringUtils.defaultIfBlank(po.getRvsApplNo(), " "));
            	dtos.add(dto);
            }
        }
        result.setEcho(searchDto.getEcho());
        result.setData(dtos);
        result.setTotalDisplayRecords(pages.getTotalElements());
        result.setTotalRecords(pages.getTotalElements());
        return result;
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
            if (sortColumn.equals("trxDt")) {
                sortColumn = "createTs";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir), "jnlNo"));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart()
                / requestDto.getDisplayLength(), requestDto.getDisplayLength(),
                sort);
        return page;
    }
    
    public DataTablesResponseDto<ReverseApplPo> getReverseApplListByRvsJnlNo(ReverseApplSearchDto searchDto){
    	DataTablesResponseDto<ReverseApplPo> result = new DataTablesResponseDto<ReverseApplPo>();
    	List<ReverseApplPo> list = reverseApplRepository.findByRvsJnlNo(searchDto.getRvsJnlNo());
    	result.setEcho(searchDto.getEcho());
        result.setData(list);
        result.setTotalDisplayRecords(searchDto.getDisplayLength());
        result.setTotalRecords(100);
    	return result;
    }
}
