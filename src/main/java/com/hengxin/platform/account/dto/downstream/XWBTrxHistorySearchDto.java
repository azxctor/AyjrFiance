
/*
* Project Name: kmfex-platform
* File Name: XWBHistorySearchDto.java
* Class Name: XWBHistorySearchDto
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

import com.hengxin.platform.account.enums.EXWBTradeType;
import com.hengxin.platform.common.dto.DataTablesRequestDto;


/**
 * Class Name: XWBHistorySearchDto
 * Description: TODO
 * @author tingwang
 *
 */

public class XWBTrxHistorySearchDto extends DataTablesRequestDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;
    
    private Date toDate;
    
    private EXWBTradeType tradeType;

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

    public EXWBTradeType getTradeType() {
        return tradeType;
    }
  	
    public void setTradeType(EXWBTradeType tradeType) {
        this.tradeType = tradeType;
    }
    
}
