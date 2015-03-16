
/*
* Project Name: kmfex-platform
* File Name: ProtocolRspuest.java
* Class Name: ProtocolRspuest
*
* Copyright 2014 Hengtian Software Inc
*
* Licensed under the Hengtiansoft
*
* http://www.hengtiansoft.com
*
* Unless Rspuired by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
	
package com.hengxin.platform.bnkdocking.dto.biz.rsp;

import java.io.Serializable;



/**
 * Class Name: TransferRsp
 * Description: TODO
 * @author congzhou
 *
 */
public class TransferRsp implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
    * Variables Name: serialVersionUID
    */
    	

    @Override
    public String toString() {
        return "TransferRsp [bankSerial=" + bankSerial + ", batchNo=" + batchNo + ", bankAccount=" + bankAccount
                + ", fundAccount=" + fundAccount + ", amount=" + amount + ", respCode=" + respCode + ", errMsg="
                + errMsg + "]";
    }
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
     * 响应代码
     */
    private String respCode;
    /**
     * 错误信息
     */
    private String errMsg;
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
    public String getRespCode() {
        return respCode;
    }
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
    public String getErrMsg() {
        return errMsg;
    }
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    
}
