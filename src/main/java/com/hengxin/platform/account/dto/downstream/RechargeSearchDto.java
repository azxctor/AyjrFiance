
/*
* Project Name: kmfex-platform
* File Name: UserRechargeTrxJnlsSearchDto.java
* Class Name: UserRechargeTrxJnlsSearchDto
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
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;


/**
 * Class Name: RechargeSearchDto
 * @author tingwang
 *
 */

public class RechargeSearchDto extends DataTablesRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String acctNo;
	
	private Date fromDate;
    
    private Date toDate;
    
    private EFundApplStatus applStatus;
    
    private ECashPool cashPool;
    
    private String createOpid;

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public EFundApplStatus getApplStatus() {
		return applStatus;
	}

	public void setApplStatus(EFundApplStatus applStatus) {
		this.applStatus = applStatus;
	}

    public String getCreateOpid() {
        return createOpid;
    }
	
    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }
	
    public ECashPool getCashPool() {
        return cashPool;
    }
	
    public void setCashPool(ECashPool cashPool) {
        this.cashPool = cashPool;
    }
	
	
}
