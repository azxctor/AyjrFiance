
/*
* Project Name: kmfex
* File Name: FrezzeReserveDtlDto.java
* Class Name: FrezzeReserveDtlDto
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
	
package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.member.enums.EMemberType;


/**
 * Class Name: FrezzeReserveDtlDto
 * @author congzhou
 *
 */

public class FreezeReserveDtlSearchDto extends DataTablesRequestDto implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;
    
    private Date toDate;
    
    private String acctNo;
    
    private String name;
    
    private EFnrStatus status;
    
    private EMemberType userType;
    
    private EFundUseType fundUseType;

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

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public EMemberType getUserType() {
        return userType;
    }

    public void setUserType(EMemberType userType) {
        this.userType = userType;
    }

    public EFundUseType getFundUseType() {
        return fundUseType;
    }

    public void setFundUseType(EFundUseType fundUseType) {
        this.fundUseType = fundUseType;
    }

	public EFnrStatus getStatus() {
		return status;
	}

	public void setStatus(EFnrStatus status) {
		this.status = status;
	}
  
}
