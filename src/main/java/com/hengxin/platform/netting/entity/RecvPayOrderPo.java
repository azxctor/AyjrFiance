package com.hengxin.platform.netting.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.FundPayRecvFlagEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundUseTypeEnumConverter;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.netting.converter.NettingStatusEnumConverter;
import com.hengxin.platform.netting.enums.NettingStatusEnum;
import com.hengxin.platform.security.entity.SimpleUserPo;

@Entity
@Table(name = "AC_RECV_PY_ORD")
@EntityListeners(IdInjectionEntityListener.class)
public class RecvPayOrderPo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ORD_ID")
	private String orderId;

	@Column(name = "EVENT_ID")
	private String eventId;

	@Column(name = "USER_ID")
	private String userId;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDate;

	@Column(name = "PAY_RECV_FLG")
	@Convert(converter = FundPayRecvFlagEnumConverter.class)
	private EFundPayRecvFlag payRecvFlg;

	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;

	@Column(name = "TRX_MEMO")
	private String trxMemo;

	@Column(name = "USE_TYPE")
	@Convert(converter = FundUseTypeEnumConverter.class)
	private EFundUseType useType;

	@Column(name = "REL_BIZ_ID")
	private String relBizId;

	@Column(name = "PKG_ID")
	private String pkgId;

	@Column(name = "SEQ_ID")
	private String seqId;

	@Column(name = "STATUS")
	@Convert(converter = NettingStatusEnumConverter.class)
	private NettingStatusEnum nettingStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NETTING_TS")
	private Date nettingTs;

	@Column(name = "SERV_PROV")
	private String servProv;

	@Column(name = "CREATE_OPID")
	private String createOpId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;

	@Version
	@Column(name = "VERSION_CT")
	private Long version_ct;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private SimpleUserPo userPo;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}

	public EFundPayRecvFlag getPayRecvFlg() {
		return payRecvFlg;
	}

	public void setPayRecvFlg(EFundPayRecvFlag payRecvFlg) {
		this.payRecvFlg = payRecvFlg;
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

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public String getRelBizId() {
		return relBizId;
	}

	public void setRelBizId(String relBizId) {
		this.relBizId = relBizId;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	public Date getNettingTs() {
		return nettingTs;
	}

	public void setNettingTs(Date nettingTs) {
		this.nettingTs = nettingTs;
	}

	public String getServProv() {
		return servProv;
	}

	public void setServProv(String servProv) {
		this.servProv = servProv;
	}

	public String getCreateOpId() {
		return createOpId;
	}

	public void setCreateOpId(String createOpId) {
		this.createOpId = createOpId;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getLastMntOpId() {
		return lastMntOpId;
	}

	public void setLastMntOpId(String lastMntOpId) {
		this.lastMntOpId = lastMntOpId;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public Long getVersion_ct() {
		return version_ct;
	}

	public void setVersion_ct(Long version_ct) {
		this.version_ct = version_ct;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public NettingStatusEnum getNettingStatus() {
		return nettingStatus;
	}

	public void setNettingStatus(NettingStatusEnum nettingStatus) {
		this.nettingStatus = nettingStatus;
	}

	// public AcctPo getAcctPo() {
	// return acctPo;
	// }
	//
	// public void setAcctPo(AcctPo acctPo) {
	// this.acctPo = acctPo;
	// }

	public SimpleUserPo getUserPo() {
		return userPo;
	}

	public void setUserPo(SimpleUserPo userPo) {
		this.userPo = userPo;
	}

}
