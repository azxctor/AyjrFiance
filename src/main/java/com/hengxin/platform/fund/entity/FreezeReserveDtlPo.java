package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.EFnrOperTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.EFnrStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundUseTypeEnumConverter;
import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: FreezeReserveDtlPo
 * 
 * @author tingwang 会员账户冻结解保留日志表
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AC_FNR_DTL")
@EntityListeners(IdInjectionEntityListener.class)
public class FreezeReserveDtlPo implements Serializable {
    @Id
    @Column(name = "JNL_NO")
    private String jnlNo;

    @Column(name = "ACCT_NO")
    private String acctNo;

    @Column(name = "SUB_ACCT_NO")
    private String subAcctNo;

    @Column(name = "USE_TYPE")
    @Convert(converter = FundUseTypeEnumConverter.class)
    private EFundUseType useType;

    @Column(name = "OPER_TYPE")
    @Convert(converter = EFnrOperTypeEnumConverter.class)
    private EFnrOperType operType;

    @Column(name = "STATUS")
    @Convert(converter = EFnrStatusEnumConverter.class)
    private EFnrStatus status;

    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECT_DT")
    private Date effectDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRE_DT")
    private Date expireDt;

    @Column(name = "TRX_AMT")
    private BigDecimal trxAmt;

    @Column(name = "TRX_MEMO")
    private String trxMemo;

    @Column(name = "REL_BIZ_ID")
    private String relBizId;

    @Column(name = "CREATE_OPID")
    private String createOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;

    @ManyToOne
    @JoinColumn(name = "ACCT_NO", insertable = false, updatable = false)
    private AcctPo acctPo;

    @Version
    @Column(name = "VERSION_CT")
    private Long versionCt;

    public Long getVersionCt() {
        return versionCt;
    }

    public void setVersionCt(Long versionCt) {
        this.versionCt = versionCt;
    }

    public String getJnlNo() {
        return jnlNo;
    }

    public void setJnlNo(String jnlNo) {
        this.jnlNo = jnlNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getSubAcctNo() {
        return subAcctNo;
    }

    public void setSubAcctNo(String subAcctNo) {
        this.subAcctNo = subAcctNo;
    }

    public EFnrOperType getOperType() {
        return operType;
    }

    public void setOperType(EFnrOperType operType) {
        this.operType = operType;
    }

    public EFnrStatus getStatus() {
        return status;
    }

    public void setStatus(EFnrStatus status) {
        this.status = status;
    }

    public Date getEffectDt() {
        return effectDt;
    }

    public void setEffectDt(Date effectDt) {
        this.effectDt = effectDt;
    }

    public Date getExpireDt() {
        return expireDt;
    }

    public void setExpireDt(Date expireDt) {
        this.expireDt = expireDt;
    }

    public BigDecimal getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(BigDecimal trxAmt) {
        this.trxAmt = trxAmt;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

    public String getRelBizId() {
        return relBizId;
    }

    public void setRelBizId(String relBizId) {
        this.relBizId = relBizId;
    }

    public String getCreateOpid() {
        return createOpid;
    }

    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    public Date getLastMntTs() {
        return lastMntTs;
    }

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    public EFundUseType getUseType() {
        return useType;
    }

    public void setUseType(EFundUseType useType) {
        this.useType = useType;
    }

    public AcctPo getAcctPo() {
        return acctPo;
    }

    public void setAcctPo(AcctPo acctPo) {
        this.acctPo = acctPo;
    }

}
