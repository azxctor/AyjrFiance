
/*
* Project Name: kmfex-platform
* File Name: ProtocolRequest.java
* Class Name: ProtocolRequest
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
	
package com.hengxin.platform.bnkdocking.dto.biz.req;

import java.io.Serializable;




/**
 * Class Name: TransferReq
 * Description: TODO
 * @author congzhou
 *
 */
public class TransferReq implements Serializable{
    
    /**
    * Variables Name: serialVersionUID
    */
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 银行流水号
     */
    private String bankSerial;
    /**
     * 交易批次号
     */
    private String batchNo;
    /**
     * 银行账号
     */
    private String bankAccount;
    /**
     * 交易账号
     */
    private String fundAccount;
    /**
     * 转账金额
     */
    private String amount;
    /**
     * 交易账号密码
     */
    private String pinBlock;
    /**
     * 证件类型
     */
    private String iDType;
    /**
     * 证件号码
     */
    private String iDNo;
    /**
     * 客户名称
     */
    private String custName;
    @Override
	public String toString() {
		return "TransferReq [bankSerial=" + bankSerial + ", batchNo=" + batchNo
				+ ", bankAccount=" + bankAccount + ", fundAccount="
				+ fundAccount + ", amount=" + amount + ", pinBlock=" + pinBlock
				+ ", iDNo=" + iDNo + ", custName=" + custName + "]";
	}
	public String getBankSerial() {
        return bankSerial;
    }
    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    public String getFundAccount() {
        return fundAccount;
    }
    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getPinBlock() {
        return pinBlock;
    }
    public void setPinBlock(String pinBlock) {
        this.pinBlock = pinBlock;
    }
    public String getiDNo() {
        return iDNo;
    }
    public void setiDNo(String iDNo) {
        this.iDNo = iDNo;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getiDType() {
        return iDType;
    }
    public void setiDType(String iDType) {
        this.iDType = iDType;
    }
    
}
