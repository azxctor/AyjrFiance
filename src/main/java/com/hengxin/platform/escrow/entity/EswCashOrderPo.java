/*
 * Project Name: ayjr-platform
 * File Name: EswCashOrderPo.java
 * Class Name: EswCashOrderPo
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
 * 
 * Class Name EswCashOrderPo
 * 
 * @author chenwulou
 * 
 */

@Entity
@SuppressWarnings("serial")
@Table(name = "AC_ESW_CASH_ORD")
@EntityListeners(IdInjectionEntityListener.class)
public class EswCashOrderPo implements Serializable {

	@Id
	@Column(name = "ORD_ID")
	private String orderId;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;

	@Column(name = "ACCT_NO")
	private String acctNo;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "ESW_CUST_ACCT_NO")
	private String eswCustAcctNo;

	@Column(name = "ESW_ACCT_NO")
	private String eswAcctNo;

	@Column(name = "ESW_SUB_ACCT_NO")
	private String eswSubAcctNo;

	@Column(name = "ESW_USER_NO")
	private String eswUserNo;

	@Column(name = "BANK_CARD_NO")
	private String bankCardNo;

	@Column(name = "BANK_CARD_NAME")
	private String bankCardName;

	@Column(name = "STATUS")
	@Convert(converter = EOrderStatusEnumConverter.class)
	private EOrderStatusEnum status;

	@Column(name = "CURR")
	private String curr;

	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;
	
	@Column(name = "TRX_MEMO")
	private String trxMemo;

	@Column(name = "ACCT_TYPE")
	private String acctType;

	@Column(name = "ORD_TYPE")
	private String ordType;

	@Column(name = "RET_CODE")
	private String retCode;

	@Column(name = "RET_MSG")
	private String retMsg;

	@Column(name = "TRX_NO")
	private String trxNo;

	@Column(name = "BAL")
	private BigDecimal bal;

	@Column(name = "CASH_ID")
	private String cashId;

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
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

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getOrdType() {
		return ordType;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
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

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
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

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

}
