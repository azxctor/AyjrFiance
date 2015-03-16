
/*
* Project Name: kmfex-platform
* File Name: UserRechargeTrxJnlsDto.java
* Class Name: UserRechargeTrxJnlsDto
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
	
package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.ECashPool;


/**
 * Class Name: UserRechargeTrxJnlsDto
 * Description: TODO
 * @author tingwang
 *
 */

public class UserRechargeTrxJnlsDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jnlNo;

    private Date trxDt;
    
    private String acctNo;
    
    private String relBnkId;
    
    private String createOpid;
    
    private BigDecimal trxAmt;
    
    private String trxMemo;
    
    private ECashPool cashPool;

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

    public BigDecimal getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(BigDecimal trxAmt) {
        this.trxAmt = trxAmt;
    }

    public String getRelBnkId() {
        return relBnkId;
    }

    public void setRelBnkId(String relBnkId) {
        this.relBnkId = relBnkId;
    }

    public String getCreateOpid() {
        return createOpid;
    }

    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}
   
}
