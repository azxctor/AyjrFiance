package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Class Name: BankAcctPo 会员银行卡资料表
 * 
 * @author jishen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AC_BANK_ACCT")
public class BankAcctPo implements Serializable {

    @Id
    @Column(name = "BNK_ACCT_NO")
    private String bnkAcctNo;

    @Column(name = "BNK_ACCT_NAME")
    private String bnkAcctName;

    @Column(name = "BNK_NAME")
    private String bnkName;

    @Column(name = "BNK_CD")
    private String bnkCd;

    @Column(name = "BNK_CARD_IMG")
    private String bnkCardImg;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "SIGNED_FLG")
    private String signedFlg;

    @Temporal(TemporalType.DATE)
    @Column(name = "SIGNED_DT")
    private Date signedDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_DT")
    private Date terminDt;

    @Column(name = "BNK_OPEN_PROV")
    private String bnkOpenProv;

    @Column(name = "BNK_OPEN_CITY")
    private String bnkOpenCity;

    @Column(name = "BNK_OPEN_BRCH")
    private String bnkBrch;

    @Column(name = "SIGNED_ADDL_AGRMNT")
    private String signedAddlAgrmnt;

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

    @Version
    @Column(name = "VERSION_CT")
    private Long versionCt;

    public Long getVersionCt() {
        return versionCt;
    }

    public void setVersionCt(Long versionCt) {
        this.versionCt = versionCt;
    }

    public String getBnkAcctNo() {
        return bnkAcctNo;
    }

    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }

    public String getBnkName() {
        return bnkName;
    }

    public void setBnkName(String bnkName) {
        this.bnkName = bnkName;
    }

    public String getBnkCd() {
        return bnkCd;
    }

    public void setBnkCd(String bnkCd) {
        this.bnkCd = bnkCd;
    }

    public String getBnkCardImg() {
        return bnkCardImg;
    }

    public void setBnkCardImg(String bnkCardImg) {
        this.bnkCardImg = bnkCardImg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignedFlg() {
        return signedFlg;
    }

    public void setSignedFlg(String signedFlg) {
        this.signedFlg = signedFlg;
    }

    public Date getSignedDt() {
        return signedDt;
    }

    public void setSignedDt(Date signedDt) {
        this.signedDt = signedDt;
    }

    public Date getTerminDt() {
        return terminDt;
    }

    public void setTerminDt(Date terminDt) {
        this.terminDt = terminDt;
    }

    public String getBnkAcctName() {
        return bnkAcctName;
    }

    public void setBnkAcctName(String bnkAcctName) {
        this.bnkAcctName = bnkAcctName;
    }

    public String getBnkOpenProv() {
        return bnkOpenProv;
    }

    public void setBnkOpenProv(String bnkOpenProv) {
        this.bnkOpenProv = bnkOpenProv;
    }

    public String getBnkOpenCity() {
        return bnkOpenCity;
    }

    public void setBnkOpenCity(String bnkOpenCity) {
        this.bnkOpenCity = bnkOpenCity;
    }

    public String getBnkBrch() {
        return bnkBrch;
    }

    public void setBnkBrch(String bnkBrch) {
        this.bnkBrch = bnkBrch;
    }

    public String getSignedAddlAgrmnt() {
        return signedAddlAgrmnt;
    }

    public void setSignedAddlAgrmnt(String signedAddlAgrmnt) {
        this.signedAddlAgrmnt = signedAddlAgrmnt;
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

}
