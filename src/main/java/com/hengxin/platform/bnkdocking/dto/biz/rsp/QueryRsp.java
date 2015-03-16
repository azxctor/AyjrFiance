
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



/**
 * Class Name: ProtocolRspuest
 * Description: TODO
 * @author congzhou
 *
 */

public class QueryRsp {
    @Override
	public String toString() {
		return "QueryRsp [bankSerial=" + bankSerial + ", BankAccount="
				+ BankAccount + ", fundAccount=" + fundAccount + ", fundBal="
				+ fundBal + ", fundUse=" + fundUse + ", respCode=" + respCode
				+ ", errMsg=" + errMsg + "]";
	}
	/**
     * 银行流水号
     */
    private String bankSerial;
    /**
     * 银行账号
     */
    private String BankAccount;
    /**
     * 交易账号
     */
    private String fundAccount;
    /**
     * 可取余额
     */
    private String fundBal;
    /**
     * 可用余额
     */
    private String fundUse;
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
    public String getBankAccount() {
        return BankAccount;
    }
    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }
    public String getFundAccount() {
        return fundAccount;
    }
    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }
    public String getFundBal() {
        return fundBal;
    }
    public void setFundBal(String fundBal) {
        this.fundBal = fundBal;
    }
    public String getFundUse() {
        return fundUse;
    }
    public void setFundUse(String fundUse) {
        this.fundUse = fundUse;
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
