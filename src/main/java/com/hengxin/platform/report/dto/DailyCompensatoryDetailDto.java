package com.hengxin.platform.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DailyCompensatoryDetailDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sequenceId; // 序列号
    private String lastPayTs; // 还款日
    private String signTs; // 签约日
    private String paymentDate; // 应还日
    private String wrtrName;// 担保机构
    private String wrtrNameShow;
    private String packageId;// 融资包编号
    private String packageName;// 融资包Name
    private String cmpnsTs; // 代偿时间
    private BigDecimal cmpnsPdAmt; // 实付代偿金额
    private String item; // 期数

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getLastPayTs() {
        return lastPayTs;
    }

    public void setLastPayTs(String lastPayTs) {
        this.lastPayTs = lastPayTs;
    }

    public String getSignTs() {
        return signTs;
    }

    public void setSignTs(String signTs) {
        this.signTs = signTs;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getWrtrName() {
        return wrtrName;
    }

    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCmpnsTs() {
        return cmpnsTs;
    }

    public void setCmpnsTs(String cmpnsTs) {
        this.cmpnsTs = cmpnsTs;
    }

    public BigDecimal getCmpnsPdAmt() {
        return cmpnsPdAmt;
    }

    public void setCmpnsPdAmt(BigDecimal cmpnsPdAmt) {
        this.cmpnsPdAmt = cmpnsPdAmt;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}

}
