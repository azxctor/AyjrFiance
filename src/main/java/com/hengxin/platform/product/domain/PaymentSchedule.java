package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.product.domain.converter.EPayStatusConverter;
import com.hengxin.platform.product.enums.EPayStatus;

/**
 * 还款计划
 * 
 * @author jishen
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PKG_SCHD_PAY")
@IdClass(PaymentSchedulePk.class)
public class PaymentSchedule extends BaseInfo implements Serializable {

    @Id
    @Column(name = "PKG_ID")
    private String packageId;// 融资包编号

    @Id
    @Column(name = "SEQ_ID")
    private int sequenceId; // 序列号

    @Id
    @Column(name = "PROD_ID")
    private String productId;

    @Temporal(TemporalType.DATE)
    @Column(name = "PAY_DT")
    private Date paymentDate; // 支付日期

    @Column(name = "FNCR_ID")
    private String financerId;// 融资人

    @Column(name = "PAY_STATUS")
    @Convert(converter = EPayStatusConverter.class)
    private EPayStatus status; // 支付状态

    @Column(name = "LAST_FLG")
    private String lastFlag; // 最后一期标志 Y/N

    @Column(name = "PRIN_PY_AMT")
    private BigDecimal principalAmt; // 应付本金

    @Column(name = "INTR_PY_AMT")
    private BigDecimal interestAmt; // 应付利息

    @Column(name = "FEE_PY_AMT")
    private BigDecimal feeAmt; // 应付费用

    @Column(name = "PRIN_FINE_PY_AMT")
    private BigDecimal principalForfeit; // 应付本金罚金

    @Column(name = "INTR_FINE_PY_AMT")
    private BigDecimal interestForfeit; // 应付利息罚金

    @Column(name = "FEE_FINE_PY_AMT")
    private BigDecimal feeForfeit; // 应付费用罚金

    @Column(name = "PRIN_PD_AMT")
    private BigDecimal payPrincipalAmt; // 实付本金

    @Column(name = "INTR_PD_AMT")
    private BigDecimal payInterestAmt; // 实付利息

    @Column(name = "FEE_PD_AMT")
    private BigDecimal payAmt; // 实付费用

    @Column(name = "PRIN_FINE_PD_AMT")
    private BigDecimal payPrincipalForfeit; // 实付本金罚金

    @Column(name = "INTR_FINE_PD_AMT")
    private BigDecimal payInterestForfeit; // 实付利息罚金

    @Column(name = "FEE_FINE_PD_AMT")
    private BigDecimal payFeeForfeit; // 实付费用罚金

    @Column(name = "WRTR_PRIN_FINE_PY_AMT")
    private BigDecimal wrtrPrinForfeit; // 担保方应付本金罚金

    @Column(name = "WRTR_PRIN_FINE_PD_AMT")
    private BigDecimal payWrtrPrinForfeit; // 担保方实付本金罚金

    @Column(name = "WRTR_INTR_FINE_PY_AMT")
    private BigDecimal wrtrInterestForfeit; // 担保方应付利息罚金

    @Column(name = "WRTR_INTR_FINE_PD_AMT")
    private BigDecimal payWrtrInterestForfeit; // 担保方实付利息罚金

    @Column(name = "WRTR_ID")
    private String warrantId; // 担保方

    @Column(name = "CMPNS_PY_AMT")
    private BigDecimal cmpnsPyAmt; // 应付代偿金额

    @Column(name = "CMPNS_FINE_PY_AMT")
    private BigDecimal cmpnsFinePyAmt; // 应付代偿金额罚金

    @Column(name = "CMPNS_PD_AMT")
    private BigDecimal cmpnsPdAmt; // 实付代偿金额

    @Column(name = "CMPNS_FINE_PD_AMT")
    private BigDecimal cmpnsFinePdAmt; // 实付代偿金额罚金

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CMPNS_TS")
    private Date cmpnsTs; // 代偿时间

    @Column(name = "PRIN_FRZ_ID")
    private String prinFrzId; // 本息费用冻结ID

    @Column(name = "CMPNS_FRZ_ID")
    private String cmpnsFrzId; // 代偿欠款冻结ID

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_PAY_TS")
    private Date lastPayTs; // 最近一次还款时间

    @Temporal(TemporalType.DATE)
    @Column(name = "PRCD_DT")
    private Date prcdDt; // 处理时间

    @Column(name = "PRCD_MSG")
    private String prcdMsg; // 处理信息

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private FinancingPackageView financingPackageView;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private PackagePaymentView packagePaymentView;

    /**
     * @return return the value of the var packageId
     */
    public String getPackageId() {
        return packageId;
    }

    /**
     * @param packageId
     *            Set packageId value
     */
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    /**
     * @return return the value of the var sequenceId
     */
    public int getSequenceId() {
        return sequenceId;
    }

    /**
     * @param sequenceId
     *            Set sequenceId value
     */
    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    /**
     * @return return the value of the var paymentDate
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate
     *            Set paymentDate value
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return return the value of the var status
     */
    public EPayStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            Set status value
     */
    public void setStatus(EPayStatus status) {
        this.status = status;
    }

    /**
     * @return return the value of the var lastFlag
     */
    public String getLastFlag() {
        return lastFlag;
    }

    /**
     * @param lastFlag
     *            Set lastFlag value
     */
    public void setLastFlag(String lastFlag) {
        this.lastFlag = lastFlag;
    }

    /**
     * @return return the value of the var principalAmt
     */
    public BigDecimal getPrincipalAmt() {
        return principalAmt;
    }

    /**
     * @param principalAmt
     *            Set principalAmt value
     */
    public void setPrincipalAmt(BigDecimal principalAmt) {
        this.principalAmt = principalAmt;
    }

    /**
     * @return return the value of the var interestAmt
     */
    public BigDecimal getInterestAmt() {
        return interestAmt;
    }

    /**
     * @param interestAmt
     *            Set interestAmt value
     */
    public void setInterestAmt(BigDecimal interestAmt) {
        this.interestAmt = interestAmt;
    }

    /**
     * @return return the value of the var feeAmt
     */
    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    /**
     * @param feeAmt
     *            Set feeAmt value
     */
    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    /**
     * @return return the value of the var principalForfeit
     */
    public BigDecimal getPrincipalForfeit() {
        return principalForfeit;
    }

    /**
     * @param principalForfeit
     *            Set principalForfeit value
     */
    public void setPrincipalForfeit(BigDecimal principalForfeit) {
        this.principalForfeit = principalForfeit;
    }

    /**
     * @return return the value of the var interestForfeit
     */
    public BigDecimal getInterestForfeit() {
        return interestForfeit;
    }

    /**
     * @param interestForfeit
     *            Set interestForfeit value
     */
    public void setInterestForfeit(BigDecimal interestForfeit) {
        this.interestForfeit = interestForfeit;
    }

    /**
     * @return return the value of the var feeForfeit
     */
    public BigDecimal getFeeForfeit() {
        return feeForfeit;
    }

    /**
     * @param feeForfeit
     *            Set feeForfeit value
     */
    public void setFeeForfeit(BigDecimal feeForfeit) {
        this.feeForfeit = feeForfeit;
    }

    public BigDecimal getPayPrincipalAmt() {
        return payPrincipalAmt;
    }

    public void setPayPrincipalAmt(BigDecimal payPrincipalAmt) {
        this.payPrincipalAmt = payPrincipalAmt;
    }

    public BigDecimal getPayInterestAmt() {
        return payInterestAmt;
    }

    public void setPayInterestAmt(BigDecimal payInterestAmt) {
        this.payInterestAmt = payInterestAmt;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public BigDecimal getPayPrincipalForfeit() {
        return payPrincipalForfeit;
    }

    public void setPayPrincipalForfeit(BigDecimal payPrincipalForfeit) {
        this.payPrincipalForfeit = payPrincipalForfeit;
    }

    public BigDecimal getPayInterestForfeit() {
        return payInterestForfeit;
    }

    public void setPayInterestForfeit(BigDecimal payInterestForfeit) {
        this.payInterestForfeit = payInterestForfeit;
    }

    public BigDecimal getPayFeeForfeit() {
        return payFeeForfeit;
    }

    public void setPayFeeForfeit(BigDecimal payFeeForfeit) {
        this.payFeeForfeit = payFeeForfeit;
    }

    /**
     * @return return the value of the var warrantId
     */
    public String getWarrantId() {
        return warrantId;
    }

    /**
     * @param warrantId
     *            Set warrantId value
     */
    public void setWarrantId(String warrantId) {
        this.warrantId = warrantId;
    }

    /**
     * @return return the value of the var prinFrzId
     */
    public String getPrinFrzId() {
        return prinFrzId;
    }

    /**
     * @param prinFrzId
     *            Set prinFrzId value
     */
    public void setPrinFrzId(String prinFrzId) {
        this.prinFrzId = prinFrzId;
    }

    /**
     * @return return the value of the var cmpnsFrzId
     */
    public String getCmpnsFrzId() {
        return cmpnsFrzId;
    }

    /**
     * @param cmpnsFrzId
     *            Set cmpnsFrzId value
     */
    public void setCmpnsFrzId(String cmpnsFrzId) {
        this.cmpnsFrzId = cmpnsFrzId;
    }

    public Date getPrcdDt() {
        return prcdDt;
    }

    public void setPrcdDt(Date prcdDt) {
        this.prcdDt = prcdDt;
    }

    public String getPrcdMsg() {
        return prcdMsg;
    }

    public void setPrcdMsg(String prcdMsg) {
        this.prcdMsg = prcdMsg;
    }

    /**
     * @return return the value of the var financerId
     */

    public String getFinancerId() {
        return financerId;
    }

    /**
     * @param financerId
     *            Set financerId value
     */

    public void setFinancerId(String financerId) {
        this.financerId = financerId;
    }

    /**
     * @return return the value of the var financingPackageView
     */

    public FinancingPackageView getFinancingPackageView() {
        return financingPackageView;
    }

    /**
     * @param financingPackageView
     *            Set financingPackageView value
     */

    public void setFinancingPackageView(FinancingPackageView financingPackageView) {
        this.financingPackageView = financingPackageView;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getCmpnsPyAmt() {
        return cmpnsPyAmt;
    }

    public void setCmpnsPyAmt(BigDecimal cmpnsPyAmt) {
        this.cmpnsPyAmt = cmpnsPyAmt;
    }

    public BigDecimal getCmpnsPdAmt() {
        return cmpnsPdAmt;
    }

    public void setCmpnsPdAmt(BigDecimal cmpnsPdAmt) {
        this.cmpnsPdAmt = cmpnsPdAmt;
    }

    public BigDecimal getCmpnsFinePyAmt() {
        return cmpnsFinePyAmt;
    }

    public void setCmpnsFinePyAmt(BigDecimal cmpnsFinePyAmt) {
        this.cmpnsFinePyAmt = cmpnsFinePyAmt;
    }

    public BigDecimal getCmpnsFinePdAmt() {
        return cmpnsFinePdAmt;
    }

    public void setCmpnsFinePdAmt(BigDecimal cmpnsFinePdAmt) {
        this.cmpnsFinePdAmt = cmpnsFinePdAmt;
    }

    public Date getLastPayTs() {
        return lastPayTs;
    }

    public void setLastPayTs(Date lastPayTs) {
        this.lastPayTs = lastPayTs;
    }

    public BigDecimal getWrtrPrinForfeit() {
        return wrtrPrinForfeit;
    }

    public void setWrtrPrinForfeit(BigDecimal wrtrPrinForfeit) {
        this.wrtrPrinForfeit = wrtrPrinForfeit;
    }

    public BigDecimal getPayWrtrPrinForfeit() {
        return payWrtrPrinForfeit;
    }

    public void setPayWrtrPrinForfeit(BigDecimal payWrtrPrinForfeit) {
        this.payWrtrPrinForfeit = payWrtrPrinForfeit;
    }

    public BigDecimal getWrtrInterestForfeit() {
        return wrtrInterestForfeit;
    }

    public void setWrtrInterestForfeit(BigDecimal wrtrInterestForfeit) {
        this.wrtrInterestForfeit = wrtrInterestForfeit;
    }

    public BigDecimal getPayWrtrInterestForfeit() {
        return payWrtrInterestForfeit;
    }

    public void setPayWrtrInterestForfeit(BigDecimal payWrtrInterestForfeit) {
        this.payWrtrInterestForfeit = payWrtrInterestForfeit;
    }

    public Date getCmpnsTs() {
        return cmpnsTs;
    }

    public void setCmpnsTs(Date cmpnsTs) {
        this.cmpnsTs = cmpnsTs;
    }

    public PackagePaymentView getPackagePaymentView() {
        return packagePaymentView;
    }

    public void setPackagePaymentView(PackagePaymentView packagePaymentView) {
        this.packagePaymentView = packagePaymentView;
    }

}
