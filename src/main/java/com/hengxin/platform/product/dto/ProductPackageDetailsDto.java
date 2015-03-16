package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.account.enums.ETradeType;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * Class Name: ProductPackageMetailsDto Description: TODO
 * 
 * @author junwei
 * 
 */

public class ProductPackageDetailsDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;// 融资包编号

    private String packageName;// 融资包简称

    private BigDecimal packageQuota;// 融资包金额

    private String supplyStartTime;// 申购起始时间

    private String supplyEndTime;// 申购截止时间

    private String subsPercent;// 申购进度

    private BigDecimal supplyUserCount;// 已申购人数

    private String signContractTime;// 签约时间

    private transient BigDecimal rate;// 年利率    

    private String duration;// 融资期限

    private EPayMethodType payMethod;// 还款方式

    // 与交易账号一起显示
    private String financierName;// 融资人名字

    private String accountNo;// 交易账号

    private EWarrantyType warrantyType;// 风险保障

    private String wrtrName;// 担保机构
    
    private String wrtrNameShow;

    private String loanPurpose;// 用途

    private String contractTemplateId;// 合同模板ID

    private String aipFlag;// 是否定投

    private String autoSubsFlag;// 是否自动申购

    private BigDecimal supplyAmount; // 实际申购额

    private EPackageStatus status;// 融资包状态

    // 是否显示申购时间
    private boolean canDisplaySupplyTime;

    // 判断是否为待放款审批及以后状态（涉及显示：签约日期，合同模板）
    private boolean statusAfterWaitLoadApproal;

    // 判断是否为申购中及以后状态（申购进度，申购人数，实际申购额）
    private boolean statusAfterSubscribe;

    private String nextPayDate; // 下一个还款日期

    private String itemsString; // 期数

    private String productId;// 融资项目编号

    private String productName; // 项目简称

    private BigDecimal restQuota;

    private String signingDt;

    private ETradeType tradeStatus;

    private boolean canLoanApprove;
    private boolean canLoanApproveConfirm;
    private boolean canStopFinancingPackage;
    private boolean canWithDraw;
    private boolean canViewPayTable;
    private boolean canManualPay;
    private boolean canprepayment;
    
    private String guaranteeLicenseNoImg;

    @SuppressWarnings("unused")
	private String guaranteeLicenseNoImgUrl;
    
    private Long overdue2CmpnsDays;

    private ERiskLevel productLevel;

    public String getRiskLevel() {
    	return productLevel != null ? productLevel.getText() : "";
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getPackageQuota() {
        return packageQuota;
    }

    public void setPackageQuota(BigDecimal packageQuota) {
        this.packageQuota = packageQuota;
    }

    public String getSupplyStartTime() {
        return supplyStartTime;
    }

    public void setSupplyStartTime(String supplyStartTime) {
        this.supplyStartTime = supplyStartTime;
    }

    public String getSupplyEndTime() {
        return supplyEndTime;
    }

    public void setSupplyEndTime(String supplyEndTime) {
        this.supplyEndTime = supplyEndTime;
    }

    public String getSubsPercent() {
        return subsPercent;
    }

    public void setSubsPercent(String subsPercent) {
        this.subsPercent = subsPercent;
    }

    public BigDecimal getSupplyUserCount() {
        if (supplyUserCount == null) {
            return BigDecimal.ZERO;
        }
        return supplyUserCount;
    }

    public void setSupplyUserCount(BigDecimal supplyUserCount) {
        this.supplyUserCount = supplyUserCount;
    }

    public String getSignContractTime() {
        return signContractTime;
    }

    public void setSignContractTime(String signContractTime) {
        this.signContractTime = signContractTime;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRateString() {
        if (rate != null) {
            return rate.toString() + "%";
        }
        return "0.0%";
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public EPayMethodType getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EPayMethodType payMethod) {
        this.payMethod = payMethod;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getWrtrName() {
        return wrtrName;
    }

    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getContractTemplateId() {
        return contractTemplateId;
    }

    public void setContractTemplateId(String contractTemplateId) {
        this.contractTemplateId = contractTemplateId;
    }

    public String getAipFlag() {
        return aipFlag;
    }

    public void setAipFlag(String aipFlag) {
        this.aipFlag = aipFlag;
    }

    public String getAutoSubsFlag() {
        return autoSubsFlag;
    }

    public void setAutoSubsFlag(String autoSubsFlag) {
        this.autoSubsFlag = autoSubsFlag;
    }

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public boolean isCanDisplaySupplyTime() {
        return canDisplaySupplyTime;
    }

    public void setCanDisplaySupplyTime(boolean canDisplaySupplyTime) {
        this.canDisplaySupplyTime = canDisplaySupplyTime;
    }

    public String getFinancierName() {
        return financierName;
    }

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    public boolean isStatusAfterWaitLoadApproal() {
        return statusAfterWaitLoadApproal;
    }

    public void setStatusAfterWaitLoadApproal(boolean statusAfterWaitLoadApproal) {
        this.statusAfterWaitLoadApproal = statusAfterWaitLoadApproal;
    }

    public boolean isStatusAfterSubscribe() {
        return statusAfterSubscribe;
    }

    public void setStatusAfterSubscribe(boolean statusAfterSubscribe) {
        this.statusAfterSubscribe = statusAfterSubscribe;
    }

    public EPackageStatus getStatus() {
        return status;
    }

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    public String getNextPayDate() {
        return nextPayDate;
    }

    public void setNextPayDate(String nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    public String getItemsString() {
        return itemsString;
    }

    public void setItemsString(String itemsString) {
        this.itemsString = itemsString;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

	public ERiskLevel getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}

	public BigDecimal getRestQuota() {
        restQuota = BigDecimal.ZERO;
        if (getPackageQuota().compareTo(getSupplyAmount()) >= 0) {
            restQuota = getPackageQuota().subtract(getSupplyAmount());
        }
        return restQuota;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isCanLoanApprove() {
        return canLoanApprove;
    }

    public void setCanLoanApprove(boolean canLoanApprove) {
        this.canLoanApprove = canLoanApprove;
    }

    public boolean isCanLoanApproveConfirm() {
        return canLoanApproveConfirm;
    }

    public void setCanLoanApproveConfirm(boolean canLoanApproveConfirm) {
        this.canLoanApproveConfirm = canLoanApproveConfirm;
    }

    public boolean isCanStopFinancingPackage() {
        return canStopFinancingPackage;
    }

    public void setCanStopFinancingPackage(boolean canStopFinancingPackage) {
        this.canStopFinancingPackage = canStopFinancingPackage;
    }

    public boolean isCanWithDraw() {
        return canWithDraw;
    }

    public void setCanWithDraw(boolean canWithDraw) {
        this.canWithDraw = canWithDraw;
    }

    public boolean isCanViewPayTable() {
        return canViewPayTable;
    }

    public void setCanViewPayTable(boolean canViewPayTable) {
        this.canViewPayTable = canViewPayTable;
    }

    public boolean isCanManualPay() {
        return canManualPay;
    }

    public void setCanManualPay(boolean canManualPay) {
        this.canManualPay = canManualPay;
    }

    public boolean isCanprepayment() {
        return canprepayment;
    }

    public void setCanprepayment(boolean canprepayment) {
        this.canprepayment = canprepayment;
    }

    public String getSigningDt() {
        return signingDt;
    }

    public void setSigningDt(String signingDt) {
        this.signingDt = signingDt;
    }

    public ETradeType getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(ETradeType tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
    public String getGuaranteeLicenseNoImg() {
        return guaranteeLicenseNoImg;
    }

    public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
        this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
    }

    public String getGuaranteeLicenseNoImgUrl() {
        if (StringUtils.isNotBlank(guaranteeLicenseNoImg)) {
            return FileUploadUtil.getTempFolder() + guaranteeLicenseNoImg;
        }
        return "";
    }

    public Long getOverdue2CmpnsDays() {
        return overdue2CmpnsDays;
    }

    public void setOverdue2CmpnsDays(Long overdue2CmpnsDays) {
        this.overdue2CmpnsDays = overdue2CmpnsDays;
    }

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}
}
