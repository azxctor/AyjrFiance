package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.constant.ApplicationConstant;

/**
 * Class Name: BankAcct
 *
 * @author liudc
 *
 */
@SuppressWarnings("serial")
public class BankAcct implements Serializable {

    /**
     * 银行账户号
     */
    @NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP)
    private String bnkAcctNo;
    /**
     * 旧银行账户号
     */
    @NotNull(groups = { UpdateBankAcct.class })
    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP)
    private String oldBnkAcctNo;
    /**
     * 开户行全称
     */
    private String bnkName;
    /**
     * 银行类型
     */
    //@NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    private String bnkCd;
    /**
     * 银行卡正面扫描图
     */
    //@NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    private String bnkCardImg;
    /**
     * 会员编号
     */
    @NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    private String userId;
    /**
     * 是否签约
     */
    private String signedFlg;
    /**
     * 签约日期
     */
    private Date signedDt;
    /**
     * 解约日期
     */
    private Date terminDt;
    /**
     * 银行账户名
     */
    @NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    private String bnkAcctName;
    /**
     * 银行账户开户省(直辖市)
     */
    private String bnkOpenProv;
    /**
     * 银行账户开户市(县)
     */
    private String bnkOpenCity;
    /**
     * 银行账户开户银行网点
     */
    private String bnkBrch;
    /**
     * 是否签署过补充协议
     */
    private String signedAddlAgrmnt;
    /**
     * 创建用户
     */
    @NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    private String createOpid;
    /**
     * 创建时间
     */
    @NotNull(groups = { CreateBankAcct.class, UpdateBankAcct.class })
    private Date createTs;
    /**
     * 更新用户
     */
    @NotNull(groups = { UpdateBankAcct.class })
    private String lastMntOpid;
    /**
     * 更新时间
     */
    @NotNull(groups = { UpdateBankAcct.class })
    private Date lastMntTs;

    public String getBnkAcctNo() {
        return bnkAcctNo;
    }

    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }

    public String getBnkName() {
    	if(StringUtils.isBlank(bnkName)){
    		return bnkBrch;
    	}
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

    public String getOldBnkAcctNo() {
        return oldBnkAcctNo;
    }

    public void setOldBnkAcctNo(String oldBnkAcctNo) {
        this.oldBnkAcctNo = oldBnkAcctNo;
    }
    /**
     * 新增银行卡账户group
     * <strong>Fields and methods should be before inner classes.</strong>
     */
    public interface CreateBankAcct {
    }

    /**
     * 更新银行卡账户group
     * <strong>Fields and methods should be before inner classes.</strong>
     */
    public interface UpdateBankAcct {
    }
}
