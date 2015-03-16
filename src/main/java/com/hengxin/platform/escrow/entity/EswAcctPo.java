/*
 * Project Name: ayjr-platform
 * File Name: EswAcctPo.java
 * Class Name: EswAcctPo
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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 
 * Class Name: EswAcctPo
 * 
 * @author chenwulou
 * 
 */

@Entity
@SuppressWarnings("serial")
@Table(name = "AC_ESW_ACCT")
public class EswAcctPo implements Serializable {

	@Id
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

	@Column(name = "ESW_USER_TYPE")
	private String eswUserType;

	@Column(name = "ESW_LOGIN_PWD")
	private String eswLoginPwd;

	@Column(name = "ESW_PAY_PWD")
	private String eswPayPwd;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CURR")
	private String curr;

	@Column(name = "SERV_PROV")
	private String servProv;

	@Column(name = "BANK_CARD_NO")
	private String bankCardNo;

	@Column(name = "BANK_CARD_NAME")
	private String bankCardName;

	@Column(name = "BANK_ASSETS_ID")
	private String bankAssetsId;

	@Column(name = "BANK_ID")
	private String bankId;

	@Column(name = "BANK_CODE")
	private String bankCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_TYPE_CODE")
	private String bankTypeCode;

	@Column(name = "PROV_CODE")
	private String provCode;

	@Column(name = "CITY_CODE")
	private String cityCode;

	@Column(name = "CREATE_OPID")
	private String createOpId;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TS")
	private Date createTs;

	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpId;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;

	@Version
	@Column(name = "VERSION_CT")
	private Long versionCt;

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

	public String getEswUserType() {
		return eswUserType;
	}

	public void setEswUserType(String eswUserType) {
		this.eswUserType = eswUserType;
	}

	public String getEswLoginPwd() {
		return eswLoginPwd;
	}

	public void setEswLoginPwd(String eswLoginPwd) {
		this.eswLoginPwd = eswLoginPwd;
	}

	public String getEswPayPwd() {
		return eswPayPwd;
	}

	public void setEswPayPwd(String eswPayPwd) {
		this.eswPayPwd = eswPayPwd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public String getServProv() {
		return servProv;
	}

	public void setServProv(String servProv) {
		this.servProv = servProv;
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

	public String getBankAssetsId() {
		return bankAssetsId;
	}

	public void setBankAssetsId(String bankAssetsId) {
		this.bankAssetsId = bankAssetsId;
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

	public String getBankTypeCode() {
		return bankTypeCode;
	}

	public void setBankTypeCode(String bankTypeCode) {
		this.bankTypeCode = bankTypeCode;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
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

}
