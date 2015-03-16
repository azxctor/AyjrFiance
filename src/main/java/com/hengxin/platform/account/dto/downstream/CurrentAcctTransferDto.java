
/*
* Project Name: kmfex-platform
* File Name: CurrentAccountRechargeDto.java
* Class Name: CurrentAccountRechargeDto
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
	
package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;


/**
 * Class Name: CurrentAccountRechargeDto
 * Description: TODO
 * @author tingwang
 *
 */

public class CurrentAcctTransferDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull
    private BigDecimal amount;
    @NotNull
    private String password;
    @NotNull
    private String bankAcctNo;
    
    private String memo;
    
    private String remFlg;


	public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRemFlg() {
        return remFlg;
    }

    public void setRemFlg(String remFlg) {
        this.remFlg = remFlg;
    }
	
    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }   
    
}
