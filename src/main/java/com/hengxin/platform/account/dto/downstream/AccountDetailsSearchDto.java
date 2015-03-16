
/*
* Project Name: kmfex-platform
* File Name: AccountDetailsSearchDto.java
* Class Name: AccountDetailsSearchDto
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
import com.hengxin.platform.fund.enums.EFundUseType;


/**
 * Class Name: AccountDetailsSearchDto
 * Description: TODO
 * @author congzhou
 *
 */

public class AccountDetailsSearchDto extends DataTablesRequestDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;
    
    private Date toDate;
    
    private EFundUseType useType;

    private Date createDate;
    
    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

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

    public EFundUseType getUseType() {
        return useType;
    }

    public void setUseType(EFundUseType useType) {
        this.useType = useType;
    }

    
}
