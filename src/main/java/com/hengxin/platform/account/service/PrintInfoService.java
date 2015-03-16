package com.hengxin.platform.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.account.dto.PrintInfoSearchDto;
import com.hengxin.platform.account.dto.TransApplPriInfoSearchDto;
import com.hengxin.platform.account.dto.downstream.TransApplPriInfoDto;
import com.hengxin.platform.account.dto.upstream.PrintInfoDto;
import com.hengxin.platform.account.enums.ERoleType;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.entity.RechargeApplPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.entity.TransferApplPo;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.repository.PositionRepository;
import com.hengxin.platform.fund.repository.RechargeApplRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.repository.TransferApplRepository;
import com.hengxin.platform.fund.repository.WithdrawDepositApplRepository;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.UserRoleService;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.DebtBillPriInfoDto;
import com.hengxin.platform.product.dto.DebtBillPriInfoSearchDto;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductFeeRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * 打印信息服务
 * 
 * @author jishen
 * 
 */
@Service
public class PrintInfoService {

    @Autowired
    private BankAcctService bankAcctService;
    @Autowired
    private BankTrxJnlRepository bankTrxJnlRepository;
    @Autowired
    private BankAcctRepository bankAcctRepository;
    @Autowired
    private SubAcctRepository subAcctRepository;
    @Autowired
    private UserRoleService roleservice;
    @Autowired
    private WithdrawDepositApplRepository withdrawDepositApplRepository;
    @Autowired
    private RechargeApplRepository rechargeApplRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferApplRepository transferApplRepository;
    @Autowired
    private ProductPackageRepository productPackageRepository;
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ProductFeeRepository productFeeRepository;

    public static final int calcuScale = 20;
    public static final int saveScale = 2;
    public static final String RISKFEERT = "风险管理费";

    /**
     * 获取充值提现打印信息
     * 
     * @param searchDto
     * @return
     */
    public PrintInfoDto getPrintInfoDetail(PrintInfoSearchDto searchDto) {
        if (null != searchDto.getJnlNo()) {
            return getPrintInfoDetailByjnlNo(searchDto);
        } else {
            return getWithdrawApplPrintInfoDetail(searchDto);
        }
    }

    private PrintInfoDto getPrintInfoDetailByjnlNo(PrintInfoSearchDto searchDto) {
        BankTrxJnlPo bankTrxJnlpo = bankTrxJnlRepository.findByJnlNo(searchDto.getJnlNo());
        PrintInfoDto printInfoDto = ConverterService.convert(bankTrxJnlpo, PrintInfoDto.class);
        List<BankAcctPo> bankAcctPoList = bankAcctRepository.findBankAcctByUserId(bankTrxJnlpo.getUserId());
        if (null == bankAcctPoList || 0 == bankAcctPoList.size()) {
            throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST,"用户银行资料信息不存在");
        }
        printInfoDto.setRmbStr(NumberUtil.toRMB(printInfoDto.getTrxAmt().doubleValue()));
        BankAcctPo bankAcctPo = bankAcctPoList.get(0);
        printInfoDto.setBnkAcctName(bankAcctPo.getBnkAcctName());
        printInfoDto.setBnkAcctNo(bankAcctPo.getBnkAcctNo());
        BankAcct bankAcct = bankAcctService.findBankAcctByUserId(bankTrxJnlpo.getUserId()).get(0);
        String bankName = (null == bankAcct.getBnkCd() ? "" : SystemDictUtil.getDictByCategoryAndCode(
                EOptionCategory.BANK, bankAcct.getBnkCd()).getText());
        printInfoDto.setBnkName(bankName);
        SubAcctPo subAcctPo = subAcctRepository.findSubAcctByAcctNoAndSubAcctNo(bankTrxJnlpo.getAcctNo(),
                ESubAcctNo.CURRENT.getCode());
        printInfoDto.setBal(subAcctPo.getBal());
        printInfoDto.setRoleType(getRoleType(bankTrxJnlpo.getUserId()));

        if (ERechargeWithdrawFlag.WITHDRAW == bankTrxJnlpo.getRechargeWithdrawFlag()) {
            WithdrawDepositApplPo wPo = withdrawDepositApplRepository.findByRelBnkJnlNo(bankTrxJnlpo.getJnlNo());
            printInfoDto.setHandler(getNameByUserId(wPo.getApprOpid()));
            printInfoDto.setChecker(getNameByUserId(bankTrxJnlpo.getCreateOpid()));
        } else {
            RechargeApplPo rPo = rechargeApplRepository.findByRelBnkJnlNo(bankTrxJnlpo.getJnlNo());
            printInfoDto.setHandler(getNameByUserId(rPo.getCreateOpid()));
            printInfoDto.setChecker("");
        }

        return printInfoDto;
    }

    @SuppressWarnings("unused")
	private String acctNoProc(String acctNo) {
        StringBuffer buf = new StringBuffer();
        if (acctNo.length() > 8) {
            buf.append(acctNo.substring(0, acctNo.length() - 8));
            buf.append("****");
            buf.append(acctNo.substring(acctNo.length() - 4));
        } else {
            buf.append(acctNo);
        }
        return buf.toString();
    }

    private String getNameByUserId(String userId) {
        UserPo po = userRepository.findOne(userId);
        return po.getName() == null ? "" : po.getName();
    }

    private ERoleType getRoleType(String userid) {
        ERoleType roleType = null;

        boolean isInvester = (null == roleservice.getUserRole(userid, EBizRole.INVESTER.getCode()) ? false : true);
        boolean isFinancier = (null == roleservice.getUserRole(userid, EBizRole.FINANCIER.getCode()) ? false : true);
        boolean isProdServ = (null == roleservice.getUserRole(userid, EBizRole.PROD_SERV.getCode()) ? false : true);
        boolean isPlatfrom = userid.equals(CommonBusinessUtil.getExchangeUserId());

        if (isPlatfrom) {
            roleType = ERoleType.PLATFORM;
        } else if (isInvester && !isFinancier && !isProdServ) {
            roleType = ERoleType.INVESTOR;
        } else if (isInvester && isFinancier && !isProdServ) {
            roleType = ERoleType.INV_FIN;
        } else if (isInvester && !isFinancier && isProdServ) {
            roleType = ERoleType.INV_GUAR;
        } else if (!isInvester && isFinancier && !isProdServ) {
            roleType = ERoleType.FINANCER;
        } else if (!isInvester && isFinancier && isProdServ) {
            roleType = ERoleType.FIN_GUAR;
        } else if (!isInvester && !isFinancier && isProdServ) {
            roleType = ERoleType.GUARANTEE;
        } else {
            roleType = ERoleType.INV_FIN_GUAR;
        }
        return roleType;
    }

    /**
     * 获取资金划转申请打印信息
     * 
     * @param searchDto
     * @return
     */
    public TransApplPriInfoDto getTransApplPrintInfoDetail(TransApplPriInfoSearchDto searchDto) {
        TransferApplPo transferApplPo = transferApplRepository.findOne(searchDto.getApplNo());
        TransApplPriInfoDto transApplPriInfoDto = ConverterService.convert(transferApplPo, TransApplPriInfoDto.class);
        transApplPriInfoDto.setDt(DateUtils.formatDate(transferApplPo.getApplDt(), "yyyy-MM-dd"));
        List<BankAcctPo> payerBankAcctPoList = bankAcctRepository.findBankAcctByAcctNo(transferApplPo.getPayerAcctNo());
        if (null == payerBankAcctPoList || 0 == payerBankAcctPoList.size()) {
            throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST);
        }
        BankAcctPo payerBankAcctPo = payerBankAcctPoList.get(0);
        transApplPriInfoDto.setPayerBnkAcctNo(payerBankAcctPo.getBnkAcctNo());
        List<BankAcctPo> payeeBankAcctPoList = bankAcctRepository.findBankAcctByAcctNo(transferApplPo.getPayeeAcctNo());
        if (null == payeeBankAcctPoList || 0 == payeeBankAcctPoList.size()) {
            throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST);
        }
        BankAcctPo payeeBankAcctPo = payeeBankAcctPoList.get(0);
        transApplPriInfoDto.setPayeeBnkAcctNo(payeeBankAcctPo.getBnkAcctNo());
        if (EFundApplStatus.APPROVED == transferApplPo.getApplStatus()
                || EFundApplStatus.REJECT == transferApplPo.getApplStatus()) {
            transApplPriInfoDto.setHandler(getNameByUserId(transferApplPo.getCreateOpid()));
            transApplPriInfoDto.setChecker(getNameByUserId(transferApplPo.getApprOpid()));
            transApplPriInfoDto.setSupervisor("");// TODO 主管
        } else {
            transApplPriInfoDto.setHandler(getNameByUserId(transferApplPo.getCreateOpid()));
            transApplPriInfoDto.setSupervisor(""); // TODO 主管
            transApplPriInfoDto.setChecker("");
        }
        transApplPriInfoDto.setTrxAmtStr(NumberUtil.toRMB(transferApplPo.getTrxAmt().doubleValue()));
        return transApplPriInfoDto;
    }

    /**
     * 获取签约借据打印信息
     * 
     * @param searchDto
     * @return
     */
    public DebtBillPriInfoDto getDebtBillPriInfoDetail(DebtBillPriInfoSearchDto searchDto) {
        String pkgId = searchDto.getPkgId();
        DebtBillPriInfoDto priInfoDto = new DebtBillPriInfoDto();
        PaymentSchedule schedule = paymentScheduleRepository.getByPackageIdOrderBySequenceIdDesc(pkgId).get(0);
        Date debtEndDt = schedule.getPaymentDate();
        String debtEndDtStr = DateUtils.formatDate(debtEndDt, "yyyy-MM-dd");
        ProductPackage pkg = productPackageRepository.findOne(pkgId);
        Date signDt = pkg.getSigningDt();
        String signDtStr = DateUtils.formatDate(signDt, "yyyy-MM-dd");
        Product product = productRepository.findOne(pkg.getProductId());
        BankAcct bankAcct = bankAcctService.findBankAcctByUserId(product.getApplUserId()).get(0);
        String fncrOpenBnk = (null == bankAcct.getBnkCd() ? "" : SystemDictUtil.getDictByCategoryAndCode(
                EOptionCategory.BANK, bankAcct.getBnkCd()).getText());
        BigDecimal fncrAmt = AmtUtils.processNegativeAmt(pkg.getSupplyAmount(), BigDecimal.ZERO);
        BigDecimal intrAmt = BigDecimal.ZERO;
        BigDecimal intrRate = AmtUtils.processNegativeAmt(product.getRate(), BigDecimal.ZERO);
        ProductFee riskFeeEntity = productFeeRepository.getFeeByProductIdAndFeeName(product.getProductId(), RISKFEERT);
        BigDecimal riskFeeRate = AmtUtils.processNegativeAmt(riskFeeEntity.getFeeRt(), BigDecimal.ZERO);
        BigDecimal riskFee = BigDecimal.ZERO;
        Integer termLength = product.getTermLength();
        ETermType termType = product.getTermType();
        if (termType == ETermType.DAY) {
//            intrAmt = fncrAmt.multiply(intrRate).multiply(BigDecimal.valueOf(termLength))
//                    .divide(BigDecimal.valueOf(360L), saveScale, RoundingMode.DOWN);
            riskFee = fncrAmt.multiply(riskFeeRate).multiply(BigDecimal.valueOf(termLength))
                    .divide(BigDecimal.valueOf(30L), saveScale, RoundingMode.DOWN);
        } else if (termType == ETermType.MONTH) {
//            intrAmt = fncrAmt.multiply(intrRate).multiply(BigDecimal.valueOf(termLength))
//                    .divide(BigDecimal.valueOf(12L), saveScale, RoundingMode.DOWN);
            riskFee = fncrAmt.multiply(riskFeeRate).multiply(BigDecimal.valueOf(termLength));
        }
        
        // 获取融资包的所有应付利息
        intrAmt = getPkgTotalIntrAmt(pkgId);
        
        int count = positionRepository.getUsersCountByPkgId(pkgId);
        EPayMethodType payMethod = product.getPayMethod();

        Product prod = productRepository.findOne(pkg.getProductId());
        UserPo pkgCreator = userRepository.findUserByUserId(prod.getCreateOpid());
        StringBuffer fncrAcctNameStr = new StringBuffer();
        String creatorName = null;
        if (pkgCreator != null) {
            creatorName = pkgCreator.getName();
        }
        if (StringUtils.isBlank(creatorName)) {
            fncrAcctNameStr.append(bankAcct.getBnkAcctName());
        } else {
            fncrAcctNameStr.append(creatorName).append("-").append(bankAcct.getBnkAcctName());
        }
        StringBuffer abstractStr = new StringBuffer();
        abstractStr.append("融资");
        abstractStr.append(pkgId);
        abstractStr.append(" 应付利息（按");
        abstractStr.append(getTermStr(termType));
        abstractStr.append("计息（");
        abstractStr.append(product.getTermLength());
        abstractStr.append(termType.getText());
        abstractStr.append(") ");
        abstractStr.append(payMethod.getText());
        abstractStr.append(")");

        StringBuffer termTypeStr = new StringBuffer();
        termTypeStr.append("按");
        termTypeStr.append(getTermStr(termType));
        termTypeStr.append("计息");

        priInfoDto.setTitle(creatorName);
        priInfoDto.setSerialNo("");
        priInfoDto.setChecker("");
        priInfoDto.setDebtEndDt(debtEndDtStr);
        priInfoDto.setDebtId(pkgId);
        priInfoDto.setFncrAcctName(fncrAcctNameStr.toString());
        priInfoDto.setFncrAcctNo(bankAcct.getBnkAcctNo());
        priInfoDto.setFncrAmt(fncrAmt);
        priInfoDto.setFncrAmtStr(NumberUtil.toRMB(fncrAmt.doubleValue()));
        priInfoDto.setFncrOpenBnk(fncrOpenBnk);
        priInfoDto.setHandler("");
        priInfoDto.setIntrAmt(intrAmt);
        priInfoDto.setIntrAmtStr(NumberUtil.toRMB(intrAmt.doubleValue()));
        priInfoDto.setIntrRate(intrRate);
        priInfoDto.setInvrAcctName(String.valueOf(count));
        priInfoDto.setInvrAcctNo("");
        priInfoDto.setInvrOpenBnk("");
        priInfoDto.setPayMethod(payMethod);
        priInfoDto.setSignDt(signDtStr);
        priInfoDto.setTermLength(product.getTermLength());
        priInfoDto.setTermType(termType);
        priInfoDto.setTermTypeStr(termTypeStr.toString());
        priInfoDto.setTrxMemo(riskFee.toPlainString());
        priInfoDto.setAbstractStr(abstractStr.toString());
        return priInfoDto;
    }
    
    private BigDecimal getPkgTotalIntrAmt(String pkgId){
        BigDecimal value = BigDecimal.ZERO;
        List<PaymentSchedule> payList = paymentScheduleRepository.getByPackageId(pkgId);
        for(PaymentSchedule ps:payList){
        	value =  value.add(AmtUtils.processNullAmt(ps.getInterestAmt(), BigDecimal.ZERO));
        }
    	return value;
    }

    private String getTermStr(ETermType termType) {
        String termStr = "";
        switch (termType) {
        case DAY:
            termStr += "日";
            break;
        case YEAR:
            termStr += "年";
            break;
        case MONTH:
            termStr += "月";
            break;
        default:
            ;
        }
        return termStr;

    }

    public PrintInfoDto getApplPrintInfoDetail(PrintInfoSearchDto searchDto) {
        RechargeApplPo applPo = rechargeApplRepository.getByApplNo(searchDto.getApplNo());
        PrintInfoDto printInfoDto = new PrintInfoDto();
        printInfoDto.setTrxDt(applPo.getApplDt());
        printInfoDto.setAcctNo(applPo.getAcctNo());
        SubAcctPo subAcctPo = subAcctRepository.findSubAcctByAcctNoAndSubAcctNo(applPo.getAcctNo(),
                applPo.getSubAcctNo());
        printInfoDto.setBal(subAcctPo.getBal());
        BankAcct bankAcct = bankAcctService.findBankAcctByUserId(applPo.getUserId()).get(0);
        String bankName = (null == bankAcct.getBnkCd() ? "" : SystemDictUtil.getDictByCategoryAndCode(
                EOptionCategory.BANK, bankAcct.getBnkCd()).getText());
        printInfoDto.setBnkName(bankName);
        printInfoDto.setBnkAcctNo(bankAcct.getBnkAcctNo());
        printInfoDto.setBnkAcctName(bankAcct.getBnkAcctName());
        printInfoDto.setTrxAmt(applPo.getTrxAmt());
        printInfoDto.setRmbStr(NumberUtil.toRMB(printInfoDto.getTrxAmt().doubleValue()));
        printInfoDto.setRoleType(getRoleType(applPo.getUserId()));
        printInfoDto.setTrxMemo(applPo.getTrxMemo());
        printInfoDto.setChecker("");
        printInfoDto.setHandler("");
        return printInfoDto;
    }

    public PrintInfoDto getWithdrawApplPrintInfoDetail(PrintInfoSearchDto searchDto) {
        WithdrawDepositApplPo applPo = withdrawDepositApplRepository.findOne(searchDto.getApplNo());
        PrintInfoDto printInfoDto = new PrintInfoDto();
        printInfoDto.setTrxDt(applPo.getApplDt());
        printInfoDto.setAcctNo(applPo.getAcctNo());
        SubAcctPo subAcctPo = subAcctRepository.findSubAcctByAcctNoAndSubAcctNo(applPo.getAcctNo(),
                applPo.getSubAcctNo());
        printInfoDto.setBal(subAcctPo.getBal());
        BankAcct bankAcct = bankAcctService.findBankAcctByUserId(applPo.getUserId()).get(0);
        String bankName = (null == bankAcct.getBnkCd() ? "" : SystemDictUtil.getDictByCategoryAndCode(
                EOptionCategory.BANK, bankAcct.getBnkCd()).getText());
        printInfoDto.setBnkName(bankName);
        printInfoDto.setBnkAcctNo(bankAcct.getBnkAcctNo());
        printInfoDto.setBnkAcctName(bankAcct.getBnkAcctName());
        printInfoDto.setTrxAmt(applPo.getTrxAmt());
        printInfoDto.setRmbStr(NumberUtil.toRMB(printInfoDto.getTrxAmt().doubleValue()));
        printInfoDto.setRoleType(getRoleType(applPo.getUserId()));
        printInfoDto.setTrxMemo(applPo.getTrxMemo());
        printInfoDto.setChecker("");
        printInfoDto.setHandler(getNameByUserId(applPo.getApprOpid()));
        return printInfoDto;
    }
}
