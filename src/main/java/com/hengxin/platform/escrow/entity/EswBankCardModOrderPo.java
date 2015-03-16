package com.hengxin.platform.escrow.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.escrow.converter.EOrderStatusEnumConverter;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

/**
 * 
 * @author juhuahuang
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "AC_ESW_CARD_MOD_ORD")
@EntityListeners(IdInjectionEntityListener.class)
public class EswBankCardModOrderPo implements Serializable{
	//	指令流水号	
	@Id
    @Column(name = "ORD_ID")
	private String orderId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDate;
	
	//	会员综合账户	
	@Column(name = "ACCT_NO")
	private String acctNo;
	
	//	会员编号	
	@Column(name = "USER_ID")
	private String userId;
	
	//	托管商户账号	
	@Column(name = "ESW_CUST_ACCT_NO")
	private String eswCustAcctNo;
	
	//	托管会员账号	
	@Column(name = "ESW_ACCT_NO")
	private String eswAcctNo;
	
	//	托管子账户编号
	@Column(name = "ESW_SUB_ACCT_NO")
	private String eswSubAcctNo;
	
	//	托管会员编号	
	@Column(name = "ESW_USER_NO")
	private String eswUserNo;
	
	//	银行卡资产编号	
	@Column(name = "BANK_ASSETS_ID")
	private String bankAssetsId;
	
	//	银行卡号	
	@Column(name = "BANK_CARD_NO")
	private String bankCardNo;
	
	//	持卡人姓名	
	@Column(name = "BANK_CARD_NAME")
	private String bankCardName;
	
	//	总行联行号	
	@Column(name = "BANK_ID")
	private String bankId;
	
	//	分行联行号	
	@Column(name = "BANK_CODE")
	private String bankCode;
	
	//	发卡行名称	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	//	运营商	
	@Column(name = "SERV_PROV")
	private String servProv;
	
	//	状态	STATUS
	@Column(name = "STATUS")
	@Convert(converter = EOrderStatusEnumConverter.class)
	private EOrderStatusEnum status;
	
	//	返回编码	
	@Column(name = "RET_CODE")
	private String retCode;
	
	//	返回信息	
	@Column(name = "RET_MSG")
	private String retMsg;
	
	//	反馈时间	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESP_TS")
	private Date respTs;
	
	//	创建用户
	@Column(name = "CREATE_OPID")
	private String createOpid;
	
	//	创建时间
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
	private Date createTs;
	
	//	更新用户	
	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;
	
	//	更新时间
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
	private Date lastMntTs;
	
	//	版本字段
	@Version
	@Column(name = "VERSION_CT")
	private Long versionCt;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEswCustAcctNo() {
		return eswCustAcctNo;
	}

	public void setEswCustAcctNo(String eswCustAcctNo) {
		this.eswCustAcctNo = eswCustAcctNo;
	}

	public String getEswAcctNo() {
		return eswAcctNo;
	}

	public void setEswAcctNo(String eswAcctNo) {
		this.eswAcctNo = eswAcctNo;
	}

	public String getEswSubAcctNo() {
		return eswSubAcctNo;
	}

	public void setEswSubAcctNo(String eswSubAcctNo) {
		this.eswSubAcctNo = eswSubAcctNo;
	}

	public String getEswUserNo() {
		return eswUserNo;
	}

	public void setEswUserNo(String eswUserNo) {
		this.eswUserNo = eswUserNo;
	}

	public String getBankAssetsId() {
		return bankAssetsId;
	}

	public void setBankAssetsId(String bankAssetsId) {
		this.bankAssetsId = bankAssetsId;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardName() {
		return bankCardName;
	}

	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getServProv() {
		return servProv;
	}

	public void setServProv(String servProv) {
		this.servProv = servProv;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Date getRespTs() {
		return respTs;
	}

	public void setRespTs(Date respTs) {
		this.respTs = respTs;
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

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}

}
