package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

import com.hengxin.platform.account.dto.WithdrawalApplSumAmtDto;
import com.hengxin.platform.account.dto.downstream.WithdConfirmDto;
import com.hengxin.platform.account.dto.downstream.WithdDepApplListSearchDto;
import com.hengxin.platform.account.dto.upstream.WithdrawalApplDetailDto;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.bnkdocking.dto.biz.req.TransferReq;
import com.hengxin.platform.bnkdocking.dto.biz.rsp.TransferRsp;
import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.bnkdocking.service.TransferService;
import com.hengxin.platform.bnkdocking.service.impl.XmlCommonService;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.domain.WithdrawDepositAppl;
import com.hengxin.platform.fund.dto.biz.req.FundAcctWithDrawReq;
import com.hengxin.platform.fund.dto.biz.req.UserReverseReq;
import com.hengxin.platform.fund.dto.biz.req.UserWithdrawalReq;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.dto.biz.rsp.FundTransferRsp;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.repository.WithdrawDepositApplRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.FundAcctWithdrawService;
import com.hengxin.platform.fund.service.SignedUserWithdrawService;
import com.hengxin.platform.fund.service.UserFundAcctReverseService;
import com.hengxin.platform.fund.service.UserWithdrawalService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.enums.EIDType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: UserWithdrawalServiceImpl.
 * 
 * @author tingwang
 * 
 */
@Service
public class UserWithdrawalServiceImpl implements UserWithdrawalService {

    @Autowired
    private AcctService acctService;

    @Autowired
    private BankAcctRepository bankAcctRepository;
    
    @Autowired
    private SignedUserWithdrawService signedUserWithdrawService;

    @Autowired
    private WithdrawDepositApplRepository withdrawDepositApplRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FundAcctBalService fundAcctBalService;

    @Autowired
    private FundAcctWithdrawService fundAcctWithdrawService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private BankTrxJnlRepository bankTrxJnlRepository;

    @Autowired
    private ActionHistoryService actionHistoryService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private JobWorkService jobWorkService;

    @Autowired
    private UserFundAcctReverseService userFundAcctReverseService;

    @Autowired
    private MemberMessageService memberMessageService;

    @Autowired
    private BankAcctService bankAcctService;

    @Override
    @Transactional
    public void doSignUserWithdrawalOnPlat(UserWithdrawalReq req) throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (!userService.isValidPassward(req.getPassword(), req.getUserId())) {
            throw new BizServiceException(EErrorCode.ACCT_PWD_NOT_MATCH);
        }
        if (!acctService.acctCanPay(req.getUserId())) {
            throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY);
        }
        if (!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
        }
        User user = userService.getUserByUserId(req.getUserId());
        BigDecimal cashableBal = fundAcctBalService.getUserCashableAmt(req.getUserId());
        if (cashableBal.compareTo(req.getAmount()) < 0) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_ENOUGH_CASHABLE_BAL);
        }
        
        // 先保留该笔提现金额， 再调用银行接口，银行接口 反馈成功之后， 再进行解保留 扣款
        String applNo = signedUserWithdrawService.signedUserWithdrawReserveAmt(req);
        
        TransferRsp rsp = doUserWithdrawal(user, req.getAmount(), req.getBnkAcctNo());
        EBnkErrorCode code = EBnkErrorCode.valueOf(XmlCommonService.getErrorMap().get(rsp.getRespCode()));
        if (!EBnkErrorCode.SUCCESS.getMsg_id().equals(code.getMsg_id())) {
            // 银行反馈失败，解保留 
        	signedUserWithdrawService.signedUserWithdrawFailedDeal(applNo);
        	throw new BizServiceException(EErrorCode.ACCT_WITHDRAWAL_FAILED, "提现失败：" + rsp.getErrMsg());
        }
        
        // 银行调用成功，提现解保留，扣款处理
        signedUserWithdrawService.signedUserWithdrawSucceedDeal(applNo, rsp.getBankSerial(), req);
    }

    @Override
    @Transactional
    public void doUnSignUserWithdrawalOnPlat(UserWithdrawalReq req) throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
        }
        if (!acctService.acctCanPay(req.getUserId())) {
            throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY);
        }
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        BigDecimal cashableBal = fundAcctBalService.getUserCashableAmt(req.getUserId());
        if (cashableBal.compareTo(req.getAmount()) < 0) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_ENOUGH_CASHABLE_BAL);
        }
        ReserveReq rwdReq = new ReserveReq();
        rwdReq.setUserId(req.getUserId());
        rwdReq.setTrxAmt(req.getAmount());
        rwdReq.setTrxMemo(StringUtils.defaultIfEmpty(req.getMemo(), "会员提现"));
        rwdReq.setWorkDate(workDate);
        rwdReq.setCurrOpId(req.getCurrentUser());
        String relFnrJnlNo = fundAcctWithdrawService.reserveWithdrawAmt(rwdReq);
        User user = userService.getUserByUserId(req.getUserId());
        WithdrawDepositApplPo applData = createAcWithdrawDepositAppl(user, req, relFnrJnlNo, workDate);
        WithdrawDepositApplPo applPo = withdrawDepositApplRepository.save(applData);
        if (applPo == null) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional
    public void doUnSignUserWithdrawalAfterApprove(String applNo, String currentUser, Date curDate)
            throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        WithdrawDepositApplPo applPo = withdrawDepositApplRepository.findByApplNo(applNo);
        WithdrawDepositAppl appl = ConverterService.convert(applPo, WithdrawDepositAppl.class);
        if (applPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_WITHDRAWARL_APPL_NOT_EXIST);
        }
        if (!EFundDealStatus.DEALING.equals(appl.getDealStatus())) {
            throw new BizServiceException(EErrorCode.ACCT_WITHDRAWARL_APPL_HAS_DEAL);
        }

        List<BankAcctPo> bankAcctList = bankAcctRepository.findBankAcctByUserId(appl.getUserId());
        if (bankAcctList == null || bankAcctList.isEmpty()) {
            throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST);
        } 

        User user = userService.getUserByUserId(appl.getUserId());
        EFundDealStatus dealStatus = EFundDealStatus.SUCCED;
        String msg = "提现成功";
        FundAcctWithDrawReq fawdReq = new FundAcctWithDrawReq();
        fawdReq.setUserId(appl.getUserId());
        fawdReq.setTrxAmt(appl.getTrxAmt());
        fawdReq.setResvJnlNo(appl.getRelFnrJnlNo());
        fawdReq.setTrxMemo(StringUtils.defaultIfEmpty(appl.getTrxMemo(), "会员提现"));
        fawdReq.setBizId(appl.getRelFnrJnlNo());
        fawdReq.setCurrOpid(currentUser);
        fawdReq.setSignedFlg(EFlagType.NO.getCode());
        fawdReq.setRelBnkId(null);
        fawdReq.setUserName(user.getName());
        fawdReq.setWorkDate(workDate);
        fawdReq.setEventId(IdUtil.produce());
        // 非签约会员提现，所属资金池是 发生提现时间点综合账户所属的资金池。
        fawdReq.setCashPool(null); 
        fawdReq.setSignedFlg(EFlagType.NO.getCode());
        fawdReq.setBankCode(bankAcctList.get(0).getBnkCd());
        fawdReq.setBnkAcctNo(applPo.getBnkAcctNo());
        fawdReq.setBnkAcctName(applPo.getBnkAcctName());
        String relBnkJnlNo = fundAcctWithdrawService.unSignedAcctWithdrawAmt(fawdReq);
        applPo.setRelBnkJnlNo(relBnkJnlNo);
        updateWithdrawDepositAppl(applPo, workDate, dealStatus, currentUser, msg);
    }

    /**
     * Description: TODO
     * 
     * @param applPo
     * @param wdrwDealStatusFailed
     * @param msg
     */
    private void updateWithdrawDepositAppl(WithdrawDepositApplPo applPo, Date workDate, EFundDealStatus dealStatus,
            String currentUser, String msg) {
        applPo.setDealStatus(dealStatus);
        applPo.setDealMemo(msg);
        applPo.setTrxDt(workDate);
        applPo.setLastMntOpid(currentUser);
        applPo.setLastMntTs(new Date());
        withdrawDepositApplRepository.save(applPo);
    }

    /**
     * 提现金额解保留 Description: TODO
     * 
     * @param appl
     * @throws ServiceException
     */
    private void unReserveWithdraw(WithdrawDepositAppl appl, Date workDate, String currentUser)
            throws BizServiceException {
        UnReserveReq urwReq = new UnReserveReq();
        urwReq.setUserId(appl.getUserId());
        urwReq.setTrxMemo(StringUtils.defaultIfEmpty(appl.getTrxMemo(), "会员提现资金解保留"));
        urwReq.setReserveJnlNo(appl.getRelFnrJnlNo());
        urwReq.setWorkDate(workDate);
        urwReq.setCurrOpId(currentUser);
        fundAcctWithdrawService.unReserveWithdrawAmt(urwReq);
    }

    /**
     * 处理签约用户和非签约用户在平台提现请求 Description: TODO
     * 
     * @param user
     * @param amount
     * @param bnkAcctNo
     * @return
     * @throws ServiceException
     */
    private TransferRsp doUserWithdrawal(User user, BigDecimal amount, String bnkAcctNo) throws BizServiceException {
        TransferReq req = createPlatformToBankWithdrawalReq(user, amount, bnkAcctNo);
        TransferRsp rsp = null;
        if (AppConfigUtil.isBnkEnabled()) {
            try {
                rsp = transferService.exchangeToBankFE(req);
            } catch (Exception e) {
                throw new BizServiceException(EErrorCode.ACCT_RECHARGE_FAILED, "招行支付接口：提现交易错误", e);
            }
        } else {
            rsp = mockRsp(req);
        }
        return rsp;
    }

    /**
     * mock bnk method
     * 
     * @param transferReq
     * @return
     */

    private TransferRsp mockRsp(TransferReq transferReq) {
        TransferRsp transferRsp = new TransferRsp();
        transferRsp.setAmount(transferReq.getAmount());
        transferRsp.setBankAccount(transferReq.getBankAccount());
        transferRsp.setBankSerial(UUID.randomUUID().toString());
        transferRsp.setBatchNo(transferReq.getBatchNo());
        transferRsp.setErrMsg("");
        transferRsp.setFundAccount(transferReq.getFundAccount());
        transferRsp.setRespCode("0000");
        return transferRsp;
    }

    /**
     * 生成提现请求（平台发出） Description: TODO
     * 
     * @param user
     * @param amount
     * @param bnkAcctNo
     * @return
     */
    private TransferReq createPlatformToBankWithdrawalReq(User user, BigDecimal amount, String bnkAcctNo) {
        TransferReq req = new TransferReq();
        EUserType type = user.getType();
        if (securityContext.isAuthServiceCenter() || securityContext.isProdServ()) {
            Agency agency = memberService.getAgencyByUserId(user.getUserId());
            req.setiDNo(agency.getOrgNo());
            req.setiDType(EIDType.ORG.getCode());
        } else {
            if (type.equals(EUserType.PERSON)) {
                Member person = memberService.getMemberByUserId(user.getUserId());
                req.setiDNo(person.getPersonIdCardNumber());
                req.setiDType(EIDType.PERSORN.getCode());
            } else {
                Member person = memberService.getMemberByUserId(user.getUserId());
                if (securityContext.isFinancier()) {
                    FinancierInfo fncr = memberService.getFinancierById(user.getUserId());
                    req.setiDNo(fncr.getOrgNumber());
                } else if (securityContext.isInvestor()) {
                    req.setiDNo(person.getPersonIdCardNumber());
                }
                req.setiDType(EIDType.ORG.getCode());
            }
        }
        AcctPo acct = acctService.getAcctByUserId(user.getUserId());
        req.setBankAccount(bnkAcctNo);
        req.setFundAccount(acct.getAcctNo());
        req.setAmount(amount.toString());
        req.setPinBlock(user.getPassword());
        List<BankAcct> bankAccts = bankAcctService.findBankAcctByUserId(user.getUserId());
        req.setCustName(bankAccts.get(0).getBnkAcctName());
        return req;
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
        /** Notice: this should be bank name rather than full name. **/
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

    @Transactional
    public FundTransferRsp processSignUserWithdrawalOnBank(UserWithdrawalReq req) {
        if (!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
        }
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        String eventId = IdUtil.produce();
        String bizId = req.getAcctNo();

        FundTransferRsp rsp = new FundTransferRsp();
        rsp.setAccotNo(req.getAcctNo());
        rsp.setAmount(req.getAmount());
        rsp.setBnkAcctNo(req.getBnkAcctNo());
        rsp.setBankSerial(req.getBkSerial());

        AcctPo acct = acctService.getAcctByAcctNo(req.getAcctNo());
        if (acct == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }
        if (!acctService.acctCanPay(acct)) {
            throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY);
        }

        User user = userService.getUserByUserId(acct.getUserId());

        if (user == null) {
            throw new BizServiceException(EErrorCode.USER_NOT_EXIST);
        }
        if (!userService.isValidPassward(req.getPassword(), user.getUserId())) {
            throw new BizServiceException(EErrorCode.ACCT_PWD_NOT_MATCH);
        }

        if (StringUtils.equals(req.getiDType(), "P01")) {
            if (!user.getType().equals(EUserType.PERSON)) {
                throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
            }
        } else {
            if (!user.getType().equals(EUserType.ORGANIZATION)) {
                throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
            }
        }
        String iDNo = null;
        switch (user.getType()) {
        case NULL:
            break;
        case ORGANIZATION:
            Agency agency = memberService.getAgencyByUserId(user.getUserId());
            if (null != agency) {
                iDNo = agency.getOrgNo();
            } else {
                FinancierInfo fncr = memberService.getFinancierById(user.getUserId());
                iDNo = fncr.getOrgNumber();
            }
            break;
        case PERSON:
            Member member = memberService.getMemberByUserId(user.getUserId());
            iDNo = member.getPersonIdCardNumber();
            break;
        default:
            break;
        }
        if (!StringUtils.equals(req.getiDNo(), iDNo)) {
            throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
        }

        BankAcctPo bankAcct = bankAcctRepository.findBankAcctByBnkAcctNo(req.getBnkAcctNo());
        if (bankAcct == null) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_NO_EXIST);
        }
        if (!StringUtils.equals(req.getCustName(), bankAcct.getBnkAcctName())) {
            throw new BizServiceException(EErrorCode.NAME_NOT_MATCH);
        }
        if (!StringUtils.equals(bankAcct.getUserId(), acct.getUserId())) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_CONFILICT_WITH_ACCT);
        }
        if (StringUtils.equals(bankAcct.getSignedFlg(), EFlagType.NO.getCode())) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_ALREADY_UNSIGNED);
        }
        // 冲正交易处理
        if (req.isReverseFlag()) {
            List<BankTrxJnlPo> result = bankTrxJnlRepository.findByRelBnkId(req.getBkSerial());
            if (result != null && result.size() > 0) {
                if (1 == result.size()) {
                    BankTrxJnlPo bankTrxJnl = result.get(0);
                    UserReverseReq reverseReq = new UserReverseReq();
                    reverseReq.setCurrOpid(req.getCurrentUser());
                    reverseReq.setRvsBnkJnlNo(bankTrxJnl.getJnlNo());
                    reverseReq.setTrxMemo(req.getMemo());
                    reverseReq.setWorkDate(req.getCurrentDate());
                    reverseReq.setDealMemo("提现冲正");
                    try {
                        userFundAcctReverseService.reverseWithdrawAmtOnBank(eventId, reverseReq);
                    } catch (Exception e) {
                    	throw new BizServiceException(EErrorCode.FUND_ACCT_REVERSE_FAILED, "", e);
                    }
                    rsp.setRespCode(EBnkErrorCode.SUCCESS.getMsg_id());
                    return rsp;
                } else {
                    rsp.setRespCode(EBnkErrorCode.SUCCESS.getMsg_id());
                    return rsp;
                }
            }
        }

        // 执行提现动作 TODO 是否需要判断余额是否足
        BigDecimal amount = req.getAmount();
        if (amount.compareTo(fundAcctBalService.getUserCashableAmt(acct.getUserId())) > 0) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_ENOUGH_CASHABLE_BAL);
        }

        FundAcctWithDrawReq fawdReq = new FundAcctWithDrawReq();
        fawdReq.setUserId(acct.getUserId());
        fawdReq.setUserName(user.getName());
        fawdReq.setSignedFlg(EFlagType.YES.getCode());
        fawdReq.setTrxAmt(req.getAmount());
        fawdReq.setTrxMemo("会员提现：" + req.getAmount() + "元");
        fawdReq.setBizId(bizId);
        fawdReq.setCurrOpid(req.getCurrentUser());
        fawdReq.setWorkDate(req.getCurrentDate());
        fawdReq.setEventId(eventId);
        fawdReq.setCashPool(acct.getCashPool());
        fawdReq.setBankCode(EBankType.CMB.getCode());
        fawdReq.setBnkAcctNo(bankAcct.getBnkAcctNo());
        fawdReq.setBnkAcctName(bankAcct.getBnkAcctName());
        fawdReq.setRelBnkId(req.getBkSerial());
        fundAcctWithdrawService.signedAcctWithdrawAmt(fawdReq);

        rsp.setJnlNo(req.getJnlNo());
        rsp.setRespCode(EBnkErrorCode.SUCCESS.getMsg_id());
        return rsp;
    }

    @Override
    public DataTablesResponseDto<WithdrawalApplDetailDto> getWithdrawDepositApplList(final String userId,
            final WithdDepApplListSearchDto searchDto, boolean approve, final boolean confirm)
            throws BizServiceException {

        Pageable pageRequest = buildPageRequest(searchDto);
        Specification<WithdrawDepositApplPo> specification = new Specification<WithdrawDepositApplPo>() {

            @Override
            public Predicate toPredicate(Root<WithdrawDepositApplPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (!StringUtils.isBlank(userId)) {
                    expressions.add(cb.equal(root.<String> get("userId"), userId));
                }
                if (searchDto != null) {
                    EFundDealStatus dealStatus = searchDto.getDealStatus();
                    EFundApplStatus applStatus = searchDto.getApplStatus();
                    ECashPool cashPool = searchDto.getCashPool();
                    if (null == applStatus) {
                        if (dealStatus != null) {
                            switch (dealStatus) {
                            case ALL:
                                if (confirm) {
                                    expressions.add(cb.notEqual(root.<EFundApplStatus> get("applStatus"),
                                            EFundApplStatus.WAIT_APPROVAL));
                                    expressions.add(root.<EFundDealStatus> get("dealStatus").in(
                                            DictConsts.CONFIRM_OPTION));
                                }
                                break;
                            case DEALING:
                                if (confirm) {
                                    expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
                                            EFundApplStatus.APPROVED));
                                } else {
                                    expressions.add(root.<EFundApplStatus> get("applStatus").in(
                                            EFundApplStatus.WAIT_APPROVAL, EFundApplStatus.APPROVED));
                                }
                                break;
                            case SUCCED:
                                expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
                                        EFundApplStatus.APPROVED));
                                break;
                            case FAILED:
                                expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
                                        EFundApplStatus.REJECT));
                                break;
                            default:
                                break;
                            }
                        }
                    } else if (applStatus != EFundApplStatus.ALL) {
                        expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"), applStatus));
                    }
                    if (dealStatus != null && dealStatus != EFundDealStatus.NULL && dealStatus != EFundDealStatus.ALL) {
                        expressions.add(cb.equal(root.<EFundDealStatus> get("dealStatus"), dealStatus));
                    }
                    if (cashPool != null && cashPool != ECashPool.ALL) {
                        expressions.add(cb.equal(root.<ECashPool> get("cashPool"), cashPool));
                    }
                    if (null != searchDto.getFromDate()) {
                        try {
                            expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("applDt"),
                                    DateUtils.getStartDate(searchDto.getFromDate())));
                        } catch (ParseException e) {
                            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                        }
                    }
                    if (null != searchDto.getToDate()) {
                        try {
                            expressions.add(cb.lessThanOrEqualTo(root.<Date> get("applDt"),
                                    DateUtils.getEndDate(searchDto.getToDate())));
                        } catch (ParseException e) {
                            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                        }
                    }
                    if (null != searchDto.getBnkCd() && !"".equals(searchDto.getBnkCd())) {
                        expressions.add(cb.equal(root.<String> get("bnkCd"), searchDto.getBnkCd()));
                    }
                    if (null != searchDto.getBnkAcctNo() && !"".equals(searchDto.getBnkAcctNo())) {
                        expressions.add(cb.equal(root.<String> get("bnkAcctNo"), searchDto.getBnkAcctNo()));
                    }
                }
                return predicate;
            }
        };

        Page<WithdrawDepositApplPo> pages = withdrawDepositApplRepository.findAll(specification, pageRequest);
        List<WithdrawalApplDetailDto> dtoList = new ArrayList<WithdrawalApplDetailDto>();
        if (!pages.getContent().isEmpty()) {
            for (WithdrawDepositApplPo po : pages.getContent()) {
                WithdrawalApplDetailDto dto = ConverterService.convert(po, WithdrawalApplDetailDto.class);
                dto.setCanApprove(approve && EFundApplStatus.WAIT_APPROVAL.equals(po.getApplStatus()));
                dto.setCanConfirm(confirm && EFundApplStatus.APPROVED.equals(po.getApplStatus())
                        && EFundDealStatus.DEALING.equals(po.getDealStatus()));
                StringBuilder bank = new StringBuilder();
                String province = "";
        		if (po.getBnkOpenProv() != null) {
        			DynamicOption p = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, po.getBnkOpenProv());
        			if (p != null) {
        				province = p.getText();
					}
				}
        		String city = "";
        		if (po.getBnkOpenCity() != null) {
        			DynamicOption c = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, po.getBnkOpenCity());
        			if (c != null) {
        				city = c.getText();
					}
				}
        		String name = dto.getBnkName() == null ? "" : dto.getBnkName();
        		String branch = po.getBnkOpenBrch() == null ? "" : po.getBnkOpenBrch();
        		bank.append(province).append(" ").append(city).append(" ").append(name).append(" ").append(branch);
                dto.setBnkName(bank.toString());
                dtoList.add(dto);
            }
        }
        DataTablesResponseDto<WithdrawalApplDetailDto> responseDto = new DataTablesResponseDto<WithdrawalApplDetailDto>();
        responseDto.setData(dtoList);
        responseDto.setTotalDisplayRecords(pages.getTotalElements());
        responseDto.setTotalRecords(pages.getTotalElements());
        responseDto.setEcho(searchDto.getEcho());
        return responseDto;
    }
    @Override
    public List<WithdrawalApplDetailDto> getWithdrawDepositApplExList(final String userId,
    		final WithdDepApplListSearchDto searchDto, boolean approve, final boolean confirm)
    				throws BizServiceException {
    	 
    	Specification<WithdrawDepositApplPo> specification = new Specification<WithdrawDepositApplPo>() {
    		
    		@Override
    		public Predicate toPredicate(Root<WithdrawDepositApplPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    			Predicate predicate = cb.conjunction();
    			List<Expression<Boolean>> expressions = predicate.getExpressions();
    			if (searchDto != null) {
    				EFundDealStatus dealStatus = searchDto.getDealStatus();
    				EFundApplStatus applStatus = searchDto.getApplStatus();
    				ECashPool cashPool = searchDto.getCashPool();
    				if (null == applStatus) {
    					if (dealStatus != null) {
    						switch (dealStatus) {
    						case ALL:
    							if (confirm) {
    								expressions.add(cb.notEqual(root.<EFundApplStatus> get("applStatus"),
    										EFundApplStatus.WAIT_APPROVAL));
    								expressions.add(root.<EFundDealStatus> get("dealStatus").in(
    										DictConsts.CONFIRM_OPTION));
    							}
    							break;
    						case DEALING:
    							if (confirm) {
    								expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
    										EFundApplStatus.APPROVED));
    							} else {
    								expressions.add(root.<EFundApplStatus> get("applStatus").in(
    										EFundApplStatus.WAIT_APPROVAL, EFundApplStatus.APPROVED));
    							}
    							break;
    						case SUCCED:
    							expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
    									EFundApplStatus.APPROVED));
    							break;
    						case FAILED:
    							expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
    									EFundApplStatus.REJECT));
    							break;
    						default:
    							break;
    						}
    					}
    				} else if (applStatus != EFundApplStatus.ALL) {
    					expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"), applStatus));
    				}
    				if (dealStatus != null && dealStatus != EFundDealStatus.NULL && dealStatus != EFundDealStatus.ALL) {
    					expressions.add(cb.equal(root.<EFundDealStatus> get("dealStatus"), dealStatus));
    				}
    				if (cashPool != null && cashPool != ECashPool.ALL) {
    					expressions.add(cb.equal(root.<ECashPool> get("cashPool"), cashPool));
    				}
    				if (null != searchDto.getFromDate()) {
    					try {
    						expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("applDt"),
    								DateUtils.getStartDate(searchDto.getFromDate())));
    					} catch (ParseException e) {
    						throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
    					}
    				}
    				if (null != searchDto.getToDate()) {
    					try {
    						expressions.add(cb.lessThanOrEqualTo(root.<Date> get("applDt"),
    								DateUtils.getEndDate(searchDto.getToDate())));
    					} catch (ParseException e) {
    						throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
    					}
    				}
    				if (null != searchDto.getBnkCd() && !"".equals(searchDto.getBnkCd())) {
    					expressions.add(cb.equal(root.<String> get("bnkCd"), searchDto.getBnkCd()));
    				}
    				if (null != searchDto.getBnkAcctNo() && !"".equals(searchDto.getBnkAcctNo())) {
    					expressions.add(cb.equal(root.<String> get("bnkAcctNo"), searchDto.getBnkAcctNo()));
    				}
    			}
    			return predicate;
    		}
    	};
        Sort sort = new Sort(Direction.fromString("desc"), "createTs");
        sort = sort.and(new Sort(Direction.fromString("desc"), "applNo"));
    	List<WithdrawDepositApplPo> datas = withdrawDepositApplRepository.findAll(specification,sort);
    	List<WithdrawalApplDetailDto> dtoList = new ArrayList<WithdrawalApplDetailDto>();
       for (WithdrawDepositApplPo po : datas) {
    			WithdrawalApplDetailDto dto = ConverterService.convert(po, WithdrawalApplDetailDto.class);
    			dto.setCanApprove(approve && EFundApplStatus.WAIT_APPROVAL.equals(po.getApplStatus()));
    			dto.setCanConfirm(confirm && EFundApplStatus.APPROVED.equals(po.getApplStatus())
    					&& EFundDealStatus.DEALING.equals(po.getDealStatus()));
     			StringBuilder bank = new StringBuilder();
    			String province = "";
    			if (po.getBnkOpenProv() != null) {
    				DynamicOption p = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, po.getBnkOpenProv());
    				if (p != null) {
    					province = p.getText();
    				}
    			}
    			String city = "";
    			if (po.getBnkOpenCity() != null) {
    				DynamicOption c = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, po.getBnkOpenCity());
    				if (c != null) {
    					city = c.getText();
    				}
    			}
    			String name = dto.getBnkName() == null ? "" : dto.getBnkName();
    			String branch = po.getBnkOpenBrch() == null ? "" : po.getBnkOpenBrch();
    			bank.append(province).append(" ").append(city).append(" ").append(name).append(" ").append(branch);
    			dto.setBnkName(bank.toString()); 
       		    if(approve){dto.setApplStatus(searchDto.getApplStatus());}
       		    if(confirm){dto.setDealStatus(searchDto.getDealStatus());}
    		 
    			dtoList.add(dto);
    		} 
 
    	return dtoList;
    }
    public WithdrawalApplSumAmtDto getWithdrawApplSumAmtDto(final String userId,
            final WithdDepApplListSearchDto searchDto, boolean approve, final boolean confirm)
            throws BizServiceException {
        WithdrawalApplSumAmtDto dto = new WithdrawalApplSumAmtDto();

        Specification<WithdrawDepositApplPo> specification = new Specification<WithdrawDepositApplPo>() {

            @Override
            public Predicate toPredicate(Root<WithdrawDepositApplPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (!StringUtils.isBlank(userId)) {
                    expressions.add(cb.equal(root.<String> get("userId"), userId));
                }
                if (searchDto != null) {
                    EFundDealStatus dealStatus = searchDto.getDealStatus();
                    EFundApplStatus applStatus = searchDto.getApplStatus();
                    ECashPool cashPool = searchDto.getCashPool();
                    if (null == applStatus) {
                        if (dealStatus != null) {
                            switch (dealStatus) {
                            case ALL:
                                if (confirm) {
                                    expressions.add(cb.notEqual(root.<EFundApplStatus> get("applStatus"),
                                            EFundApplStatus.WAIT_APPROVAL));
                                    expressions.add(root.<EFundDealStatus> get("dealStatus").in(
                                            DictConsts.CONFIRM_OPTION));
                                }
                                break;
                            case DEALING:
                                if (confirm) {
                                    expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
                                            EFundApplStatus.APPROVED));
                                } else {
                                    expressions.add(root.<EFundApplStatus> get("applStatus").in(
                                            EFundApplStatus.WAIT_APPROVAL, EFundApplStatus.APPROVED));
                                }
                                break;
                            case SUCCED:
                                expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
                                        EFundApplStatus.APPROVED));
                                break;
                            case FAILED:
                                expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"),
                                        EFundApplStatus.REJECT));
                                break;
                            default:
                                break;
                            }
                        }
                    } else if (applStatus != EFundApplStatus.ALL) {
                        expressions.add(cb.equal(root.<EFundApplStatus> get("applStatus"), applStatus));
                    }
                    if (dealStatus != null && dealStatus != EFundDealStatus.NULL && dealStatus != EFundDealStatus.ALL) {
                        expressions.add(cb.equal(root.<EFundDealStatus> get("dealStatus"), dealStatus));
                    }
    				if (cashPool != null && cashPool != ECashPool.ALL) {
    					expressions.add(cb.equal(root.<ECashPool> get("cashPool"), cashPool));
    				}
                    if (null != searchDto.getFromDate()) {
                        try {
                            expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("applDt"),
                                    DateUtils.getStartDate(searchDto.getFromDate())));
                        } catch (ParseException e) {
                            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                        }
                    }
                    if (null != searchDto.getToDate()) {
                        try {
                            expressions.add(cb.lessThanOrEqualTo(root.<Date> get("applDt"),
                                    DateUtils.getEndDate(searchDto.getToDate())));
                        } catch (ParseException e) {
                            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                        }
                    }
                    if (null != searchDto.getBnkCd() && !"".equals(searchDto.getBnkCd())) {
                        expressions.add(cb.equal(root.<String> get("bnkCd"), searchDto.getBnkCd()));
                    }
                    if (null != searchDto.getBnkAcctNo() && !"".equals(searchDto.getBnkAcctNo())) {
                        expressions.add(cb.equal(root.<String> get("bnkAcctNo"), searchDto.getBnkAcctNo()));
                    }
                }
                return predicate;
            }
        };

        List<WithdrawDepositApplPo> list = withdrawDepositApplRepository.findAll(specification);
        BigDecimal sumAmt = BigDecimal.ZERO;
        for (WithdrawDepositApplPo appl : list) {
            sumAmt = sumAmt.add(appl.getTrxAmt());
        }
        dto.setSumAmt(sumAmt);
        return dto;
    }

    @Override
    @Transactional
    public void approvalWithdrawDepositAppl(String applId, boolean passed, String comments, String opratorId,
            Date workDate) throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        WithdrawDepositApplPo appl = withdrawDepositApplRepository.findOne(applId);

        if (appl.getApplStatus() == EFundApplStatus.WAIT_APPROVAL && appl.getDealStatus() == EFundDealStatus.DEALING) {
            EFundApplStatus applstatus = passed ? EFundApplStatus.APPROVED : EFundApplStatus.REJECT;
            EFundDealStatus dealStatus = passed ? EFundDealStatus.DEALING : EFundDealStatus.FAILED;
            ActionResult actionResult = passed ? ActionResult.FUND_WITHDDEPAPPL_APPROVED
                    : ActionResult.FUND_WITHDDEPAPPL_REJECT;
            if (EFundApplStatus.REJECT == applstatus) {
                unReserveWithdraw(ConverterService.convert(appl, WithdrawDepositAppl.class), workDate, opratorId);
            }
            appl.setApplStatus(applstatus);
            appl.setDealStatus(dealStatus);
            appl.setLastMntOpid(opratorId);
            appl.setLastMntTs(workDate);
            appl.setDealMemo(comments);
            appl.setApprOpid(opratorId);
            appl.setApprTs(workDate);

            withdrawDepositApplRepository.save(appl);

            actionHistoryService.save(EntityType.FUND, applId, ActionType.APPLAPPROVAL, actionResult, comments);
        }

    }

    @Override
    public WithdrawDepositApplPo getWithdrawDepositApplByApplNo(String applNo) {
        return withdrawDepositApplRepository.findByApplNo(applNo);
    }

    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
                indexOf = sortColumn.lastIndexOf(".text");
            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
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

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    @Override
    public List<WithdrawDepositApplPo> getWithdrawDepositApplByCashPoolAndStatus(EFundApplStatus applStatus,
            EFundDealStatus dealStatus, ECashPool cashPool) {
        return withdrawDepositApplRepository.findByCashPoolAndApplStatusAndDealStatus(applStatus, dealStatus, cashPool);
    }

    @Override
    @Transactional
    public void doBatchUnSignUserWithdrawalApprove(List<WithdrawDepositApplPo> list, String currentUser, Date curDate)
            throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        for (WithdrawDepositApplPo po : list) {
            if (EFundApplStatus.WAIT_APPROVAL == po.getApplStatus()) {
                this.approvalWithdrawDepositAppl(po.getApplNo(), true, "批量审批通过", currentUser, curDate);
            }
        }
    }

    @Override
    @Transactional
    public void doBatchUnSignUserWithdrawalConfirm(List<WithdrawDepositApplPo> list, String currentUser, Date curDate)
            throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        for (WithdrawDepositApplPo po : list) {
            if (EFundApplStatus.APPROVED == po.getApplStatus() && EFundDealStatus.DEALING == po.getDealStatus()) {
                doUnSignUserWithdrawalAfterApprove(po.getApplNo(), currentUser, curDate);
            }
        }
    }

    @Override
    @Transactional
    public void doUnSignUserWithdrawalReject(WithdConfirmDto dto, String currentUser, Date workDate) {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        WithdrawDepositApplPo applPo = withdrawDepositApplRepository.findByApplNo(dto.getApplId());
        if (applPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_WITHDRAWARL_APPL_NOT_EXIST);
        }
        WithdrawDepositAppl appl = ConverterService.convert(applPo, WithdrawDepositAppl.class);
        if (!EFundDealStatus.DEALING.equals(appl.getDealStatus())) {
            throw new BizServiceException(EErrorCode.ACCT_WITHDRAWARL_APPL_HAS_DEAL);
        }

        unReserveWithdraw(ConverterService.convert(appl, WithdrawDepositAppl.class), workDate, currentUser);

        applPo.setDealStatus(EFundDealStatus.FAILED);
        applPo.setApplStatus(EFundApplStatus.REJECT);
        applPo.setDealMemo(dto.getMemo());
        applPo.setTrxDt(workDate);
        applPo.setLastMntOpid(currentUser);
        applPo.setLastMntTs(new Date());
        withdrawDepositApplRepository.save(applPo);

        ActionResult actionResult = ActionResult.FUND_WITHDDEPAPPL_REJECT;

        actionHistoryService.save(EntityType.FUND, applPo.getAcctNo(), ActionType.APPLAPPROVAL, actionResult,
                dto.getMemo());
    }

}
