/*
 * Project Name: ayjr-platform
 * File Name: EswTransferOrderPo.java
 * Class Name: EswTransferOrderPo
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.escrow.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * Class Name: EswTransferOrderPo
 * 
 * @author chenwulou
 * 
 */

@Entity
@SuppressWarnings("serial")
@Table(name = "AC_ESW_TSFR_ORD")
@EntityListeners(IdInjectionEntityListener.class)
public class EswTransferOrderPo implements Serializable {

	@Id
	@Column(name = "ORD_ID")
	private String orderId;

	@Column(name = "EVENT_ID")
	private String eventId;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDate;

	@Column(name = "PAYER_NO")
	private String payerNo;

	@Column(name = "PAYEE_NO")
	private String payeeNo;

	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;

	@Column(name = "TRX_MEMO")
	private String trxMemo;

	@Column(name = "REL_BIZ_ID")
	private String relBizId;

	@Column(name = "PKG_ID")
	private String pkgId;

	@Column(name = "SEQ_ID")
	private String seqId;

	@Column(name = "SERV_PROV")
	private String servProv;

	@Column(name = "STATUS")
	@Convert(converter = EOrderStatusEnumConverter.class)
	private EOrderStatusEnum status;

	@Column(name = "RET_CODE")
	private String retCode;

	@Column(name = "RET_MSG")
	private String retMsg;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESP_TS")
	private Date respTs;

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
	private Long versionCt;

//	@OneToOne(fetch = FetchType.EAGER, optional = false)
//	@JoinColumn(name = "PAYER_NO", insertable = false, updatable = false)
//	private EswAcctPo payerAcctPo;
//
//	@OneToOne(fetch = FetchType.EAGER, optional = false)
//	@JoinColumn(name = "PAYEE_NO", insertable = false, updatable = false)
//	private EswAcctPo payeeAcctPo;

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

	public String getPayerNo() {
		return payerNo;
	}

	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}

	public String getPayeeNo() {
		return payeeNo;
	}

	public void setPayeeNo(String payeeNo) {
		this.payeeNo = payeeNo;
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
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
