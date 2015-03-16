
/*
* Project Name: kmfex
* File Name: AcctFreezeSumAmtDto.java
* Class Name: AcctFreezeSumAmtDto
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
import java.math.BigDecimal;


/**
 * Class Name: AcctFreezeSumAmtDto
 * Description: TODO
 * @author congzhou
 *
 */

public class AcctFreezeSumAmtDto implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer count;
    
    private BigDecimal sumAmount;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }
    
}
