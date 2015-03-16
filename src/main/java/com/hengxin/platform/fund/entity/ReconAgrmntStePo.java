
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
import com.hengxin.platform.fund.entity.converter.FundAgrmntStatusEnumConverter;
import com.hengxin.platform.fund.enums.EAgrmntStatus;
import com.hengxin.platform.fund.enums.EFundCurCode;


/**
 * Description: 客户协议状态表
 * @author congzhou
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "AC_RECON_AGRMNT_STE")
@EntityListeners(IdInjectionEntityListener.class)
public class ReconAgrmntStePo implements Serializable {

    /**
     * 协议状态编号
     */
    @Id
    @Column(name = "AGRMNT_STE_NO")
    private String agrmntStateNo;
    
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
     * 货币代码
     */
    @Column(name = "CUR_CODE")
    private EFundCurCode curCode;
    
    /**
     * 协议状态
     */
    @Column(name = "AGRMNT_STE")
    @Convert(converter = FundAgrmntStatusEnumConverter.class)
    private EAgrmntStatus agrmntStatus;
    
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

    public String getAgrmntStateNo() {
        return agrmntStateNo;
    }

    public void setAgrmntStateNo(String agrmntStateNo) {
        this.agrmntStateNo = agrmntStateNo;
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

    public EFundCurCode getCurCode() {
        return curCode;
    }

    public void setCurCode(EFundCurCode curCode) {
        this.curCode = curCode;
    }

    public EAgrmntStatus getAgrmntStatus() {
        return agrmntStatus;
    }

    public void setAgrmntStatus(EAgrmntStatus agrmntStatus) {
        this.agrmntStatus = agrmntStatus;
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
