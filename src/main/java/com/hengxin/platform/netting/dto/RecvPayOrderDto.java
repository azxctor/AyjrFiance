package com.hengxin.platform.netting.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.netting.enums.NettingStatusEnum;

public class RecvPayOrderDto {
	// 指令编号
	private String orderId;
	// 事件编号
	private String eventId;
	// 会员编号
	private String userId;
	// 会员名称
	private String userName;
	// 交易账号
	private String acctNo;
	// 交易日期
	private Date trxDate;
	// 收付标识
	private EFundPayRecvFlag payRecvFlg;
	// 交易金额
	private BigDecimal trxAmt;
	// 交易备注
	private String trxMemo;
	// 资金用途
	private EFundUseType useType;
	// 业务编号
	private String relBizId;
	// 融资包编号
	private String pkgId;
	// 还款期号
	private String seqId;
	// 轧差状态
	private NettingStatusEnum nettingStatus;
	// 轧差时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date nettingTs;
	private String nettingTsStr;
	// 服务商
	private String servProv;
	private String createOpId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTs;
	private String lastMntOpId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastMntTs;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
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
	public NettingStatusEnum getNettingStatus() {
		return nettingStatus;
	}
	public void setNettingStatus(NettingStatusEnum nettingStatus) {
		this.nettingStatus = nettingStatus;
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
	public String getNettingTsStr() {
		if(nettingTs!=null){
			return DateUtils.formatDate(nettingTs, "yyyy-MM-dd HH:mm:ss");
		}
		return nettingTsStr;
	}
	public void setNettingTsStr(String nettingTsStr) {
		this.nettingTsStr = nettingTsStr;
	}
	
}
