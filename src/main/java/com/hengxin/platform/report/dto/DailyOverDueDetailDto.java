package com.hengxin.platform.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.product.enums.EWarrantyType;

public class DailyOverDueDetailDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sequenceId; // 序号
    private String signContractTime; // 签约日期
    private String id; // 融资包编号
    private String packageName;// 融资包简称
    private BigDecimal packageQuota;// 融资金额
    private String accountNo;// 融资人账号
    private String financierName;// 融资人姓名
    private String wrtrName;// 担保机构
    private String wrtrNameShow;
    private String payDate;// 应还日期
    private int overdueDay; // 逾期天数
    private EWarrantyType warrantyType;// 风险保障
    private String item;// 期数
    private BigDecimal monthPrincipal;// 当期应还款额

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getSignContractTime() {
        return signContractTime;
    }

    public void setSignContractTime(String signContractTime) {
        this.signContractTime = signContractTime;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getFinancierName() {
        return financierName;
    }

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    public String getWrtrName() {
        return wrtrName;
    }

    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public int getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(int overdueDay) {
        this.overdueDay = overdueDay;
    }

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getMonthPrincipal() {
        return monthPrincipal;
    }

    public void setMonthPrincipal(BigDecimal monthPrincipal) {
        this.monthPrincipal = monthPrincipal;
    }

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}

}
