
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
import java.math.BigDecimal;


/**
 * Class Name: XWBDetailDto
 * Description: TODO
 * @author tingwang
 *
 */

public class XWBOverviewDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal totalAmount;
    
    private BigDecimal totalProfit;
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    @Override
    public String toString() {
        return "XWBOverviewDto [totalAmount=" + totalAmount + ", totalProfit=" + totalProfit + "]";
    }
    
    
    
}
