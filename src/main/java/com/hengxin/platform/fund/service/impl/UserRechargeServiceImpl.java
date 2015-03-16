package com.hengxin.platform.fund.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.account.dto.UnsignedRechargeInfoDto;
import com.hengxin.platform.account.dto.UserRechargeApplDto;
import com.hengxin.platform.account.dto.downstream.RechargeSearchDto;
import com.hengxin.platform.account.dto.downstream.UnsignedUserInfoSearchDto;
import com.hengxin.platform.account.dto.upstream.UnsignedUserInfoDto;
import com.hengxin.platform.account.dto.upstream.UserRechargeTrxJnlsDto;
import com.hengxin.platform.account.enums.EUserRoleType;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.bnkdocking.dto.biz.req.TransferReq;
import com.hengxin.platform.bnkdocking.dto.biz.rsp.TransferRsp;
import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.bnkdocking.service.TransferService;
import com.hengxin.platform.bnkdocking.service.impl.XmlCommonService;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.dto.biz.req.FundAcctRechargeReq;
import com.hengxin.platform.fund.dto.biz.req.UserRechargeReq;
import com.hengxin.platform.fund.dto.biz.rsp.FundTransferRsp;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.entity.RechargeApplPo;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.repository.RechargeApplRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.service.FundAcctRechargeService;
import com.hengxin.platform.fund.service.UserRechargeService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.enums.EIDType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.UserRoleService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;

/**
 * 会员充值服务 Class Name: UserRechargeServiceImpl
 * 
 * @author tingwang
 * 
 */
@Service
@Qualifier("userRechargeService")
public class UserRechargeServiceImpl implements UserRechargeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserRechargeService.class);

    @Autowired
    private AcctService acctService;
    @Autowired
    private UserService userService;
    @Autowired
    private BankAcctRepository bankAcctRepository;
    @Autowired
    private BankTrxJnlRepository bankTrxJnlRepository;
    @Autowired
    private FundAcctRechargeService fundAcctRechargeService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private BankAcctService bankAcctService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private JobWorkService jobWorkService;
    @Autowired
    private AcctRepository acctRepository;
    @Autowired
    private UserRoleService roleservice;
    @Autowired
    private RechargeApplRepository rechargeApplRepository;
    @Autowired
    private MemberMessageService memberMessageService;

    @Transactional
    public FundTransferRsp processSignedUserOnBankRecharge(UserRechargeReq req) throws BizServiceException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("处理银行发起的充值请求（银行发出）");
        }
        if (!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
        }
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        FundTransferRsp rsp = new FundTransferRsp();
        rsp.setAmount(req.getAmount());
        rsp.setBankSerial(req.getBkSerial());
        rsp.setBnkAcctNo(req.getBnkAcctNo());
        rsp.setAccotNo(req.getAcctNo());
        rsp.setJnlNo(req.getJnlNo());

        AcctPo acct = acctService.getAcctByAcctNo(req.getAcctNo());
        if (acct == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }
        User user = userService.getUserByUserId(acct.getUserId());
        if (user == null) {
            throw new BizServiceException(EErrorCode.USER_NOT_EXIST);
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

        BankAcctPo bankAcct = bankAcctRepository.findByBnkAcctNoAndUserId(req.getBnkAcctNo(), acct.getUserId());
        if (bankAcct == null) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_NO_EXIST);
        }
        if (!StringUtils.equals(req.getCustName(), bankAcct.getBnkAcctName())) {
            throw new BizServiceException(EErrorCode.NAME_NOT_MATCH);
        }
        if (StringUtils.equals(bankAcct.getSignedFlg(), EFlagType.NO.getCode())) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_ALREADY_UNSIGNED);
        }
        // 重发交易处理
        if (req.isAnewFlag()) {
            List<BankTrxJnlPo> rtj = bankTrxJnlRepository.findByRelBnkId(req.getBkSerial());
            if (rtj != null && rtj.size() > 0) {// 如果已经存在流水号直接返回成功
                LOGGER.debug("this is a anew transfer");
                rsp.setRespCode(EBnkErrorCode.SUCCESS.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.SUCCESS.getMsg());
                return rsp;
            }
        }

        UserRechargeReq urReq = new UserRechargeReq();
        urReq.setCurrentUser(user.getUserId());
        urReq.setCurrentDate(CommonBusinessUtil.getCurrentWorkDate());
        urReq.setUserName(user.getName());
        urReq.setAmount(req.getAmount());
        urReq.setBnkAcctNo(req.getBnkAcctNo());
        urReq.setBkSerial(req.getBkSerial());
        urReq.setUserId(user.getUserId());
        urReq.setAcctNo(req.getAcctNo());
        urReq.setPassword(req.getPassword());
        urReq.setMemo("会员充值");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("执行充值动作");
        }
        fundAcctRechargeService.rechargeAmt(createFundAcctRechargeReq(urReq));
        rsp.setRespCode(EBnkErrorCode.SUCCESS.getMsg_id());
        return rsp;
    }

    @Override
    @Transactional
    public void doSignedUserOnPlatRecharge(UserRechargeReq req) throws BizServiceException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("处理平台发起的充值请求（平台发出）");
        }
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_RECHARGE_MARKET_NOT_OPEN);
        }
        if (!userService.isValidPassward(req.getPassword(), req.getUserId())) {
            throw new BizServiceException(EErrorCode.ACCT_PWD_NOT_MATCH);
        }
        TransferRsp tfRsp = null;
        TransferReq tfReq = createRechargeReq(req);
        if (AppConfigUtil.isBnkEnabled()) {
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(tfReq.toString());
                }
                tfRsp = transferService.bankToExchangeFE(tfReq);
                req.setBkSerial(tfRsp.getBankSerial());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new BizServiceException(EErrorCode.ACCT_RECHARGE_FAILED, "招行支付接口：充值交易错误", e);
            }
        } else {
            tfRsp = mockRsp(tfReq);
            req.setBkSerial(tfRsp.getBankSerial());
        }
        EBnkErrorCode code = EBnkErrorCode.valueOf(XmlCommonService.getErrorMap().get(tfRsp.getRespCode()));
        if (!EBnkErrorCode.SUCCESS.getMsg_id().equals(code.getMsg_id())) {
            throw new BizServiceException(EErrorCode.ACCT_COMMON_RECHARGE_FAILED, "充值失败：" + tfRsp.getErrMsg());
        }
        FundAcctRechargeReq faReq = createFundAcctRechargeReq(req);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("执行充值");
        }
        fundAcctRechargeService.rechargeAmt(faReq);
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

    @Override
    @Transactional
    public void doUnSignedUserOnBankRechargeAppl(UserRechargeReq req) throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        /* 闭市之后，允许发起充值申请 
        if (!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_RECHARGE_APPLY_MARKET_NOT_OPEN);
        }*/
        AcctPo acct = acctService.getAcctByAcctNo(req.getAcctNo().trim());
        String userId = acct.getUserId();
        String acctNo = acct.getAcctNo();
        User user = userService.getUserByUserId(userId);
        List<BankAcctPo> bnkAcctList = bankAcctRepository.findBankAcctByUserId(userId);
        BankAcctPo bnkAcct = bnkAcctList.get(0);
        if (!StringUtils.equalsIgnoreCase(bnkAcct.getSignedFlg(), EFlagType.NO.getCode())) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_MATCH_UNSIGNED_USER_INFO, "此会员为签约会员");
        }
        if (StringUtils.isNotBlank(req.getBkSerial())) {
            Integer count = rechargeApplRepository.existsRecordByVoucherNo(req.getBkSerial());
            if (count != null && count.intValue() > 0) {
                throw new BizServiceException(EErrorCode.ACCT_RECHARGE_VOUCHER_NO_EXISTS, "充值回单号已存在");
            }
        }

        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        RechargeApplPo appl = new RechargeApplPo();
        appl.setAcctNo(acctNo);
        appl.setCashPool(EnumHelper.translate(ECashPool.class, acct.getCashPool()));
        appl.setApplDt(workDate);
        appl.setApplStatus(EFundApplStatus.WAIT_APPROVAL);
        appl.setApprOpid(null);
        appl.setApprTs(null);
        appl.setBnkAcctName(bnkAcct.getBnkAcctName());
        appl.setBnkAcctNo(bnkAcct.getBnkAcctNo());
        appl.setBnkCd(bnkAcct.getBnkCd());
        appl.setCreateOpid(req.getCurrentUser());
        appl.setCreateTs(new Date());
        appl.setDealMemo(null);
        appl.setDealStatus(EFundDealStatus.DEALING);
        appl.setEventId(IdUtil.produce());
        appl.setLastMntOpid(null);
        appl.setLastMntTs(null);
        appl.setRemFlg(EFlagType.NO.getCode());
        appl.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
        appl.setTrxAmt(req.getAmount());
        appl.setTrxDt(null);
        appl.setTrxMemo(req.getMemo());
        appl.setUserId(userId);
        appl.setUserName(user.getName());
        appl.setVoucherNo(req.getBkSerial());
        rechargeApplRepository.save(appl);
    }

    @Override
    public void doUnSignUserRechargeApplApprove(String applId, boolean passed, String comments, String opratorId)
            throws BizServiceException {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        RechargeApplPo appl = rechargeApplRepository.getByApplNo(applId);

        if (appl.getApplStatus() != EFundApplStatus.WAIT_APPROVAL) {
            throw new BizServiceException(EErrorCode.ACCT_RECHARGE_FAILED, "申请记录审批状态不为待审批，充值处理失败");
        }

        Date workDate = CommonBusinessUtil.getCurrentWorkDate();

        String bnkTrxJnlNo = null;

        if (passed) {
            FundAcctRechargeReq faReq = new FundAcctRechargeReq();
            faReq.setUserId(appl.getUserId());
            faReq.setCashPool(appl.getCashPool().getCode());
            faReq.setUserName(appl.getUserName());
            faReq.setSignedFlg(EFlagType.NO.getCode());
            faReq.setTrxAmt(appl.getTrxAmt());
            faReq.setWorkDate(workDate);
            faReq.setRelBnkId(appl.getVoucherNo());
            faReq.setTrxMemo(StringUtils.defaultIfEmpty(appl.getTrxMemo(), "会员充值"));
            faReq.setCurrOpid(opratorId);
            faReq.setEventId(appl.getEventId());
            faReq.setBizId(appl.getEventId());
            faReq.setBankCode(appl.getBnkCd());
            faReq.setBankAcctNo(appl.getBnkAcctNo());
            faReq.setBankAcctName(appl.getBnkAcctName());
            bnkTrxJnlNo = fundAcctRechargeService.rechargeAmt(faReq);
        }

        appl.setApplStatus(EFundApplStatus.REJECT);
        appl.setDealStatus(EFundDealStatus.FAILED);
        appl.setDealMemo(comments);
        if (passed) {
            appl.setApplStatus(EFundApplStatus.APPROVED);
            appl.setDealStatus(EFundDealStatus.SUCCED);
            appl.setDealMemo(StringUtils.defaultIfBlank(comments, "充值成功"));
        }
        appl.setApprOpid(opratorId);
        appl.setApprTs(new Date());
        appl.setTrxDt(workDate);
        appl.setRelBnkJnlNo(bnkTrxJnlNo);
        appl.setLastMntOpid(opratorId);
        appl.setLastMntTs(new Date());
        rechargeApplRepository.save(appl);
    }

    /**
     * 创建财务资金资金接口的充值请求
     * 
     * @param req
     * @return
     */
    private FundAcctRechargeReq createFundAcctRechargeReq(UserRechargeReq req) {
        BankAcctPo bankAcctPo = bankAcctRepository.findBankAcctByBnkAcctNo(req.getBnkAcctNo());
        FundAcctRechargeReq faReq = new FundAcctRechargeReq();
        faReq.setUserId(req.getUserId());
        faReq.setTrxAmt(req.getAmount());
        faReq.setWorkDate(req.getCurrentDate());
        faReq.setRelBnkId(req.getBkSerial());
        faReq.setTrxMemo(StringUtils.defaultIfEmpty(req.getMemo(), "会员充值"));
        faReq.setCurrOpid(req.getCurrentUser());
        faReq.setEventId(IdUtil.produce());
        faReq.setUserName(req.getUserName());
        faReq.setSignedFlg(bankAcctPo.getSignedFlg());
        faReq.setBankCode(bankAcctPo.getBnkCd());
        faReq.setBankAcctName(bankAcctPo.getBnkAcctName());
        faReq.setBankAcctNo(bankAcctPo.getBnkAcctNo());
        return faReq;
    }

    /**
     * 创建招行交易接口的充值请求
     * 
     * @param urReq
     * @return
     */
    private TransferReq createRechargeReq(UserRechargeReq urReq) {
        TransferReq req = new TransferReq();
        AcctPo acct = acctService.getAcctByUserId(urReq.getUserId());
        User user = userService.getUserByUserId(urReq.getUserId());
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
        req.setBankAccount(urReq.getBnkAcctNo());
        req.setFundAccount(acct.getAcctNo());
        req.setAmount(urReq.getAmount().toString());
        req.setPinBlock(user.getPassword());
        List<BankAcct> bankAccts = bankAcctService.findBankAcctByUserId(urReq.getUserId());
        req.setCustName(bankAccts.get(0).getBnkAcctName());
        return req;
    }

    @Override
    public DataTablesResponseDto<UserRechargeTrxJnlsDto> getUserRechargeTrxJnlList(final RechargeSearchDto searchDto,
            final String userId, final EFlagType signedFlag) {
        DataTablesResponseDto<UserRechargeTrxJnlsDto> result = new DataTablesResponseDto<UserRechargeTrxJnlsDto>();

        Specification<BankTrxJnlPo> spec = new Specification<BankTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<BankTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (!StringUtils.isBlank(userId)) {
                    expressions.add(cb.equal(root.get("userId"), userId));
                }
                if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
                    expressions.add(cb.equal(root.get("acctNo"), searchDto.getAcctNo()));
                }
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("trxDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("trxDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (signedFlag != null) {
                    expressions.add(cb.equal(root.<ERechargeWithdrawFlag> get("rechargeWithdrawFlag"),
                            ERechargeWithdrawFlag.RECHARGE));
                    expressions.add(cb.equal(root.<String> get("signedFlg"), EFlagType.NO.getCode()));
                }
                return predicate;
            }
        };
        Pageable pageRequest = buildPageRequest(searchDto, "jnlNo");
        Page<BankTrxJnlPo> pages = bankTrxJnlRepository.findAll(spec, pageRequest);
        List<UserRechargeTrxJnlsDto> dtos = new ArrayList<UserRechargeTrxJnlsDto>();
        if (!pages.getContent().isEmpty()) {
            for (BankTrxJnlPo po : pages.getContent()) {
                dtos.add(ConverterService.convert(po, UserRechargeTrxJnlsDto.class));
            }
        }
        result.setEcho(searchDto.getEcho());
        result.setData(dtos);
        result.setTotalDisplayRecords(pages.getTotalElements());
        result.setTotalRecords(pages.getTotalElements());
        return result;
    }

    private Pageable buildPageRequest(DataTablesRequestDto requestDto, String idString) {
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
            if (sortColumn.equals("applDt")) {
                sortColumn = "createTs";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir), idString));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    @Override
    public UnsignedUserInfoDto queryUnsignedUserInfo(UnsignedUserInfoSearchDto searchDto) {
        UnsignedUserInfoDto userInfoDto = new UnsignedUserInfoDto();
        List<UnsignedRechargeInfoDto> list = new ArrayList<UnsignedRechargeInfoDto>();
        UnsignedRechargeInfoDto unSignedInfo = new UnsignedRechargeInfoDto();
        AcctPo acctPo = null;
        List<BankAcctPo> bankAcctPoList = null;
        BankAcctPo bankAcctPo = null;
        if (StringUtils.isNotBlank(searchDto.getBnkAcctName())) {

            bankAcctPoList = bankAcctRepository.findByBnkAcctName(searchDto.getBnkAcctName());

            if (bankAcctPoList == null || bankAcctPoList.isEmpty()) {
                throw new BizServiceException(EErrorCode.ACCT_BNKACCTNAME_NOT_MATCH);
            }

            for (BankAcctPo bnkAcct : bankAcctPoList) {
                if (bnkAcct == null) {
                    continue;
                }
                if (!StringUtils.equalsIgnoreCase(bnkAcct.getSignedFlg(), EFlagType.NO.getCode())) {
                    continue;
                }
                acctPo = acctRepository.findByUserId(bnkAcct.getUserId());
                if (acctPo == null) {
                    continue;
                }
                bankAcctPo = bnkAcct;
                unSignedInfo = new UnsignedRechargeInfoDto();
                unSignedInfo.setAcctNo(acctPo.getAcctNo());
                unSignedInfo.setBnkAcctNo(bankAcctPo.getBnkAcctNo());
                unSignedInfo.setBnkAcctName(bankAcctPo.getBnkAcctName());
                unSignedInfo.setUserId(acctPo.getUserId());
                unSignedInfo.setUserType(getUserRoleType(acctPo.getUserId()).getText());
                setChannelValue(unSignedInfo, bankAcctPo);

                list.add(unSignedInfo);
            }
        } else {

            if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
                unSignedInfo.setAcctNo(searchDto.getAcctNo());
                acctPo = acctRepository.findByAcctNo(searchDto.getAcctNo());
                if (acctPo == null) {
                    throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
                }
                bankAcctPoList = bankAcctRepository.findBankAcctByUserId(acctPo.getUserId());
                if (bankAcctPoList != null && !bankAcctPoList.isEmpty()) {
                    bankAcctPo = bankAcctPoList.get(0);
                }
            } else if (StringUtils.isNotBlank(searchDto.getBnkAcctNo())) {
                unSignedInfo.setBnkAcctNo(searchDto.getBnkAcctNo());
                bankAcctPo = bankAcctRepository.findBankAcctByBnkAcctNo(searchDto.getBnkAcctNo());
                if (bankAcctPo == null) {
                    throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST);
                }
                acctPo = acctRepository.findByUserId(bankAcctPo.getUserId());
            }

            if (acctPo == null) {
                throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
            }
            if (bankAcctPo == null) {
                throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST);
            }
            if (StringUtils.equalsIgnoreCase(bankAcctPo.getSignedFlg(), EFlagType.NO.getCode())) {
                unSignedInfo.setAcctNo(acctPo.getAcctNo());
                unSignedInfo.setBnkAcctNo(bankAcctPo.getBnkAcctNo());
                unSignedInfo.setBnkAcctName(bankAcctPo.getBnkAcctName());
                unSignedInfo.setUserId(acctPo.getUserId());
                unSignedInfo.setUserType(getUserRoleType(acctPo.getUserId()).getText());
                setChannelValue(unSignedInfo, bankAcctPo);
                list.add(unSignedInfo);
            }
        }
        if (list.isEmpty()) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_MATCH_UNSIGNED_USER_INFO);
        }
        userInfoDto.setList(list);
        return userInfoDto;
    }

    private void setChannelValue(UnsignedRechargeInfoDto unsignedRechargeInfoDto, BankAcctPo bankAcctPo) {
        String bnkCd = bankAcctPo.getBnkCd();
        String signedFlg = bankAcctPo.getSignedFlg();
        if (null == bnkCd || null == signedFlg) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }

        if (EBankType.ICBC.getCode().equals(bnkCd)) {
            unsignedRechargeInfoDto.setChannel(ECashPool.ICBC_COMMON);
        } else if (EBankType.CMB.getCode().equals(bnkCd) && EFlagType.YES.getCode().equals(signedFlg)) {
            unsignedRechargeInfoDto.setChannel(ECashPool.CMB_SPECIAL);
        } else {
            unsignedRechargeInfoDto.setChannel(ECashPool.CMB_COMMON);
        }
    }

    private EUserRoleType getUserRoleType(String userid) {
        EUserRoleType roleType = null;

        boolean isInvester = (null == roleservice.getUserRole(userid, EBizRole.INVESTER.getCode()) ? false : true);
        boolean isFinancier = (null == roleservice.getUserRole(userid, EBizRole.FINANCIER.getCode()) ? false : true);
        boolean isProdServ = (null == roleservice.getUserRole(userid, EBizRole.PROD_SERV.getCode()) ? false : true);
        boolean isPlatfrom = userid.equals(CommonBusinessUtil.getExchangeUserId());

        if (isPlatfrom) {
            roleType = EUserRoleType.PLATFORM;
        } else if (isInvester && !isFinancier && !isProdServ) {
            roleType = EUserRoleType.INVESTOR;
        } else if (isInvester && isFinancier && !isProdServ) {
            roleType = EUserRoleType.INV_FIN;
        } else if (isInvester && !isFinancier && isProdServ) {
            roleType = EUserRoleType.INV_GUAR;
        } else if (!isInvester && isFinancier && !isProdServ) {
            roleType = EUserRoleType.FINANCER;
        } else if (!isInvester && isFinancier && isProdServ) {
            roleType = EUserRoleType.FIN_GUAR;
        } else if (!isInvester && !isFinancier && isProdServ) {
            roleType = EUserRoleType.GUARANTEE;
        } else {
            roleType = EUserRoleType.INV_FIN_GUAR;
        }
        return roleType;
    }

    @Override
    public DataTablesResponseDto<UserRechargeApplDto> getUserRechargeApplList(final RechargeSearchDto searchDto,
            final String userId) {

        DataTablesResponseDto<UserRechargeApplDto> result = new DataTablesResponseDto<UserRechargeApplDto>();

        Specification<RechargeApplPo> spec = new Specification<RechargeApplPo>() {

            @Override
            public Predicate toPredicate(Root<RechargeApplPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (StringUtils.isNotBlank(userId)) {
                    expressions.add(cb.equal(root.get("userId"), userId));
                }
                if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
                    expressions.add(cb.equal(root.get("acctNo"), searchDto.getAcctNo()));
                }
                if (searchDto.getApplStatus() != null && searchDto.getApplStatus() != EFundApplStatus.ALL) {
                    expressions.add(cb.equal(root.get("applStatus"), searchDto.getApplStatus()));
                }
                if (StringUtils.isNotBlank(searchDto.getCreateOpid())) {
                    expressions.add(cb.equal(root.get("createOpid"), searchDto.getCreateOpid()));
                }
                if (searchDto.getFromDate() != null) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("applDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (searchDto.getToDate() != null) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("applDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }

                if (searchDto.getCashPool() != null && searchDto.getCashPool() != ECashPool.ALL) {
                    expressions.add(cb.equal(root.get("cashPool"), searchDto.getCashPool()));
                }
                return predicate;
            }
        };
        Pageable pageRequest = buildPageRequest(searchDto, "applNo");
        Page<RechargeApplPo> pages = rechargeApplRepository.findAll(spec, pageRequest);
        List<UserRechargeApplDto> dtos = new ArrayList<UserRechargeApplDto>();
        if (!pages.getContent().isEmpty()) {
            for (RechargeApplPo po : pages.getContent()) {
                dtos.add(ConverterService.convert(po, UserRechargeApplDto.class));
            }
        }
        result.setEcho(searchDto.getEcho());
        result.setData(dtos);
        result.setTotalDisplayRecords(pages.getTotalElements());
        result.setTotalRecords(pages.getTotalElements());
        return result;
    }

}
