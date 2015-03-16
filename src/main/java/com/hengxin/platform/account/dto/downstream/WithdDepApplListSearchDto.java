/*
 * Project Name: kmfex-platform
 * File Name: WithdrawDepositApplListSearchDto.java
 * Class Name: WithdrawDepositApplListSearchDto
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
import com.hengxin.platform.fund.enums.EFundDealStatus;

/**
 * Class Name: WithdrawDepositApplListSearchDto Description: TODO
 * 
 * @author tingwang
 * 
 */

public class WithdDepApplListSearchDto extends DataTablesRequestDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;

    private Date toDate;

    private EFundApplStatus applStatus;

    private EFundDealStatus dealStatus;

    private ECashPool cashPool;
    
    /**
     * 开户银行
     */
    private String bnkCd;
    
    /**
     * 银行卡号
     */
    private String bnkAcctNo;
    
    /**
     * 查询日期
     */
    private Date searchDate;
    

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public EFundApplStatus getApplStatus() {
        return applStatus;
    }

    public void setApplStatus(EFundApplStatus applStatus) {
        this.applStatus = applStatus;
    }

    public EFundDealStatus getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(EFundDealStatus dealStatus) {
        this.dealStatus = dealStatus;
    }

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

}
