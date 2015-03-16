
/*
* Project Name: kmfex-platform
* File Name: CustAgrmntStatePo.java
* Class Name: CustAgrmntStatePo
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
	
package com.hengxin.platform.fund.entity;

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

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.FundTxOptEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundTxTypeEnumConverter;
import com.hengxin.platform.fund.enums.EFundCurCode;
import com.hengxin.platform.fund.enums.EFundTxOpt;
import com.hengxin.platform.fund.enums.EFundTxType;


/**
 * Description: 账户类交易对账明细表
 * @author congzhou
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "AC_RECON_ACCT_STE")
@EntityListeners(IdInjectionEntityListener.class)
public class ReconAcctStePo implements Serializable {

    /**
     * 交易批次号
     */
    @Column(name = "BATCH_NO")
    private String batchNo;
    
    /**
     * 银行编号
     */
    @Column(name = "BANK_ID")
    private String bankId;
    
    /**
     * 合作方编号
     */
    @Column(name = "DEAL_ID")
    private String dealId;
    
    /**
     * 合作方机构号
     */
    @Column(name = "DEAL_ORG_NO")
    private String organizetionNo;
    
    /**
     * 交易日期
     */
    @Column(name = "TRX_DATE")
    private String trxDate;
    
    /**
     * 交易时间
     */
    @Column(name = "TRX_TIME")
    private String trxTime;
    
    /**
     * 银行流水号
     */
    @Id
    @Column(name = "BNK_SERIAL")
    private String bkSerial;
    
    /**
     * 合作方流水号
     */
    @Column(name = "CO_SERIAL")
    private String coSerial;
    
    /**
     * 银行账号
     */
    @Column(name = "BNK_ACCT_NO")
    private String bankAcctNo;
    
    /**
     * 交易所交易账号
     */
    @Column(name = "FUND_ACCT_NO")
    private String fundAcctNo;
    
    /**
     * 客户姓名
     */
    @Column(name = "CUST_NAME")
    private String custName;
    
    /**
     * 交易发起方
     */
    @Column(name = "TRX_OPT")
    @Convert(converter = FundTxOptEnumConverter.class)
    private EFundTxOpt txOpt;
    
    /**
     * 交易类型
     */
    @Column(name = "TRX_TYPE")
    @Convert(converter = FundTxTypeEnumConverter.class)
    private EFundTxType txType;
    
    /**
     * 货币代码
     */
    @Column(name = "CUR_CODE")
    private EFundCurCode curCode;
    
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getOrganizetionNo() {
        return organizetionNo;
    }

    public void setOrganizetionNo(String organizetionNo) {
        this.organizetionNo = organizetionNo;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public String getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(String trxTime) {
        this.trxTime = trxTime;
    }

    public String getBkSerial() {
        return bkSerial;
    }

    public void setBkSerial(String bkSerial) {
        this.bkSerial = bkSerial;
    }

    public String getCoSerial() {
        return coSerial;
    }

    public void setCoSerial(String coSerial) {
        this.coSerial = coSerial;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public String getFundAcctNo() {
        return fundAcctNo;
    }

    public void setFundAcctNo(String fundAcctNo) {
        this.fundAcctNo = fundAcctNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public EFundTxOpt getTxOpt() {
        return txOpt;
    }

    public void setTxOpt(EFundTxOpt txOpt) {
        this.txOpt = txOpt;
    }

    public EFundTxType getTxType() {
        return txType;
    }

    public void setTxType(EFundTxType txType) {
        this.txType = txType;
    }

    public EFundCurCode getCurCode() {
        return curCode;
    }

    public void setCurCode(EFundCurCode curCode) {
        this.curCode = curCode;
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