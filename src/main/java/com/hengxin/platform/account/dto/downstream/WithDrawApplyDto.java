
/*
* Project Name: kmfex-platform
* File Name: WithDrawApplyDto.java
* Class Name: WithDrawApplyDto
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


/**
 * Class Name: WithDrawApplyDto
 * Description: TODO
 * @author congzhou
 *
 */

public class WithDrawApplyDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 银行账户名
     */
    private String bnkAcctName;
    /**
     * 银行账号
     */
    private String bnkAcctNo;
    /**
     * 开户银行全称
     */
    private String bnkOpenProv;
    
    /**
     *  开户银行简称
     */
    private String bnkName;
    
    /**
     * 备注
     */
    private String memo;
    
    public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getBnkAcctName() {
        return bnkAcctName;
    }
    public void setBnkAcctName(String bnkAcctName) {
        this.bnkAcctName = bnkAcctName;
    }
    public String getBnkAcctNo() {
        return bnkAcctNo;
    }
    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }
    public String getBnkOpenProv() {
        return bnkOpenProv;
    }
    public void setBnkOpenProv(String bnkOpenProv) {
        this.bnkOpenProv = bnkOpenProv;
    }
	public String getBnkName() {
		return bnkName;
	}
	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}
    
}
