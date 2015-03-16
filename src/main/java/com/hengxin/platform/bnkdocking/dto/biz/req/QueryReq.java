
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



/**
 * Class Name: QueryReq
 * Description: TODO
 * @author congzhou
 *
 */

public class QueryReq {
    /**
     * 合作方流水号
     */
    private String coSerial;
    /**
     * 銀行流水號
     */
    private String bankSerial;
    /**
     * 银行账号
     */
    private String bankAccount;
    /**
     * 交易账号
     */
    private String fundAccount;
    
    public String getBankSerial() {
        return bankSerial;
    }
    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }
    public String getCoSerial() {
        return coSerial;
    }
    public void setCoSerial(String coSerial) {
        this.coSerial = coSerial;
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
	@Override
	public String toString() {
		return "QueryReq [bankSerial=" + bankSerial + ", bankAccount="
				+ bankAccount + ", fundAccount=" + fundAccount + "]";
	}
    
}
