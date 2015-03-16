
/*
* Project Name: kmfex-platform
* File Name: XWBDetailDto.java
* Class Name: XWBDetailDto
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

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.fund.domain.SubAcctTrxJnl;


/**
 * Class Name: XWBDetailDto
 * Description: TODO
 * @author tingwang
 *
 */

public class XWBDetailDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	XWBOverviewDto xwbOverview;
    
    DataTablesResponseDto<SubAcctTrxJnl> trxJnlList;

    public XWBOverviewDto getXwbOverview() {
        return xwbOverview;
    }

    public void setXwbOverview(XWBOverviewDto xwbOverview) {
        this.xwbOverview = xwbOverview;
    }

    public DataTablesResponseDto<SubAcctTrxJnl> getTrxJnlList() {
        return trxJnlList;
    }

    public void setTrxJnlList(DataTablesResponseDto<SubAcctTrxJnl> trxJnlList) {
        this.trxJnlList = trxJnlList;
    }
    
    
}
