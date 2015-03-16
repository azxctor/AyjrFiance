package com.hengxin.platform.fund.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.dto.biz.req.FundAcctWithDrawReq;
import com.hengxin.platform.fund.dto.biz.req.UserWithdrawalReq;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.WithdrawDepositApplRepository;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

@Service
public class SignedUserWithdrawService {
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private AcctService acctService;

    @Autowired
    private SecurityContext securityContext;
    
    @Autowired
    private FundAcctService fundAcctService;

    @Autowired
    private BankAcctRepository bankAcctRepository;

    @Autowired
    private FundAcctWithdrawService fundAcctWithdrawService;

    @Autowired
    private WithdrawDepositApplRepository withdrawDepositApplRepository;
	
    /**
     * 签约会员发起提现，先保留提现金额
     * 独立事务处理
     * @param req
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public String signedUserWithdrawReserveAmt (UserWithdrawalReq req){
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		ReserveReq rwdReq = new ReserveReq();
        rwdReq.setUserId(req.getUserId());
        rwdReq.setTrxAmt(req.getAmount());
        rwdReq.setTrxMemo(StringUtils.defaultIfEmpty(req.getMemo(), "签约会员提现,提现资金保留"));
        rwdReq.setWorkDate(workDate);
        rwdReq.setCurrOpId(req.getCurrentUser());
        User user = userService.getUserByUserId(req.getUserId());
        String relFnrJnlNo = fundAcctWithdrawService.reserveWithdrawAmt(rwdReq);
        WithdrawDepositApplPo applData = createAcWithdrawDepositAppl(user, req, relFnrJnlNo, workDate);
        WithdrawDepositApplPo applPo = withdrawDepositApplRepository.save(applData);
        if (applPo == null) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
        return applPo.getApplNo();
	}

    /**
     * 创建并保存提现申请表 Description: TODO
     * 
     * @param user
     * @param req
     * @param relFnrJnlNo
     */
    private WithdrawDepositApplPo createAcWithdrawDepositAppl(User user, UserWithdrawalReq req, String relFnrJnlNo,
            Date currentDate) {
        WithdrawDepositApplPo appl = new WithdrawDepositApplPo();
        AcctPo acct = acctService.getAcctByUserId(req.getUserId());
        BankAcctPo bankAcct = bankAcctRepository.findBankAcctByBnkAcctNo(req.getBnkAcctNo());
        appl.setApplStatus(EFundApplStatus.WAIT_APPROVAL);
        appl.setUserId(user.getUserId());
        appl.setUserName(user.getName());
        appl.setApplDt(currentDate);
        appl.setAcctNo(acct.getAcctNo());
        appl.setCashPool(EnumHelper.translate(ECashPool.class, acct.getCashPool()));
        appl.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
        appl.setDealStatus(EFundDealStatus.DEALING);
        appl.setBnkAcctName(bankAcct.getBnkAcctName());
        appl.setBnkAcctNo(bankAcct.getBnkAcctNo());
        appl.setBnkOpenProv(bankAcct.getBnkOpenProv());
        appl.setBnkOpenCity(bankAcct.getBnkOpenCity());
        appl.setBnkName(req.getBnkOpenProv());
        appl.setBnkCd(bankAcct.getBnkCd());
        appl.setBnkOpenBrch(bankAcct.getBnkBrch());
        appl.setTrxAmt(req.getAmount());
        appl.setTrxMemo(StringUtils.defaultIfEmpty(req.getMemo(), "会员提现"));
        appl.setRemFlag(req.getRemFlg());
        appl.setRelFnrJnlNo(relFnrJnlNo);
        appl.setCreateOpid(req.getCurrentUser());
        appl.setCreateTs(new Date());
        return appl;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void signedUserWithdrawSucceedDeal(String applNo, String bnkSerial, UserWithdrawalReq req){
    	
    	Date workDate = CommonBusinessUtil.getCurrentWorkDate();

        WithdrawDepositApplPo applPo = withdrawDepositApplRepository.findByApplNo(applNo);
        if (applPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_WITHDRAWARL_APPL_NOT_EXIST);
        }
        String userId = securityContext.getCurrentUserId();
        EFundDealStatus dealStatus = EFundDealStatus.SUCCED;
        String msg = "签约会员提现成功";
    	updateWithdrawDepositAppl(applPo, workDate, dealStatus, userId, msg);
    	
    	String rsvJnlNo = applPo.getRelFnrJnlNo();
    	UnReserveReq rsvReq = new UnReserveReq();
    	rsvReq.setCurrOpId(userId);
    	rsvReq.setReserveJnlNo(rsvJnlNo);
    	rsvReq.setTrxMemo("签约会员提现解保留");
    	rsvReq.setUserId(userId);
    	rsvReq.setWorkDate(workDate);
    	fundAcctService.unReserveAmt(rsvReq);
    	
    	BankAcctPo bankAcct = bankAcctRepository.findBankAcctByBnkAcctNo(req.getBnkAcctNo());
        String eventId = IdUtil.produce();
        FundAcctWithDrawReq fawdReq = new FundAcctWithDrawReq();
        fawdReq.setUserId(req.getUserId());
        fawdReq.setUserName(req.getUserName());
        fawdReq.setSignedFlg(EFlagType.YES.getCode());
        fawdReq.setTrxAmt(req.getAmount());
        fawdReq.setTrxMemo(StringUtils.defaultIfEmpty(req.getMemo(), "签约会员在线提现"));
        fawdReq.setBizId(eventId);
        fawdReq.setCurrOpid(req.getCurrentUser());
        fawdReq.setWorkDate(workDate);
        fawdReq.setEventId(eventId);
        fawdReq.setBankCode(EBankType.CMB.getCode());
        fawdReq.setBnkAcctNo(bankAcct.getBnkAcctNo());
        fawdReq.setBnkAcctName(bankAcct.getBnkAcctName());
        fawdReq.setRelBnkId(bnkSerial);
        fundAcctWithdrawService.signedAcctWithdrawAmt(fawdReq);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void signedUserWithdrawFailedDeal(String applNo){
    	WithdrawDepositApplPo applPo = withdrawDepositApplRepository.findByApplNo(applNo);
        if (applPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_WITHDRAWARL_APPL_NOT_EXIST);
        }
        String userId = securityContext.getCurrentUserId();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String rsvJnlNo = applPo.getRelFnrJnlNo();
    	UnReserveReq rsvReq = new UnReserveReq();
    	rsvReq.setCurrOpId(userId);
    	rsvReq.setReserveJnlNo(rsvJnlNo);
    	rsvReq.setTrxMemo("签约会员提现失败，提现资金解保留");
    	rsvReq.setUserId(userId);
    	rsvReq.setWorkDate(workDate);
    	fundAcctService.unReserveAmt(rsvReq);
        
        applPo.setDealStatus(EFundDealStatus.FAILED);
        applPo.setApplStatus(EFundApplStatus.REJECT);
        applPo.setDealMemo("签约会员提现失败");
        applPo.setTrxDt(workDate);
        applPo.setLastMntOpid(userId);
        applPo.setLastMntTs(new Date());
        withdrawDepositApplRepository.save(applPo);
    }
    
    private void updateWithdrawDepositAppl(WithdrawDepositApplPo applPo, Date workDate, EFundDealStatus dealStatus,
            String currentUser, String msg) {
        applPo.setDealStatus(dealStatus);
        applPo.setApplStatus(EFundApplStatus.APPROVED);
        applPo.setDealMemo(msg);
        applPo.setTrxDt(workDate);
        applPo.setLastMntOpid(currentUser);
        applPo.setLastMntTs(new Date());
        withdrawDepositApplRepository.save(applPo);
    }

	
}
