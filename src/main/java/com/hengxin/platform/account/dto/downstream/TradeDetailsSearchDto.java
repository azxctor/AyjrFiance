
/*
* Project Name: kmfex-platform
* File Name: TradeDetailsDto.java
* Class Name: TradeDetailsDto
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
import com.hengxin.platform.fund.enums.EFundTrdType;


/**
 * Class Name: TradeDetailsDto
 * Description: TODO
 * @author congzhou
 *
 */

public class TradeDetailsSearchDto extends DataTablesRequestDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;
    
    private Date toDate;
    
    private EFundTrdType trdType;

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

    public EFundTrdType getTrdType() {
        return trdType;
    }

    public void setTrdType(EFundTrdType trdType) {
        this.trdType = trdType;
    }
    
}
