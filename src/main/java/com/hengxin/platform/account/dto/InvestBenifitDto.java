
/*
* Project Name: kmfex-platform
* File Name: InvestBenifitDto.java
* Class Name: InvestBenifitDto
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
	
package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Class Name: InvestBenifitDto
 * Description: TODO
 * @author congzhou
 *
 */

public class InvestBenifitDto implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 周数
     */
    private String week;
    /**
     * 投资收益
     */
    private BigDecimal investBenifit;
    
    /**
    * InvestBenifitDto Constructor
    *
    */
    	
    public InvestBenifitDto() {
        super();
    }

    
    
    /**
    * InvestBenifitDto Constructor
    *
    * @param week
    * @param investBenifit
    */
    	
    public InvestBenifitDto(String week, BigDecimal investBenifit) {
        super();
        this.week = week;
        this.investBenifit = investBenifit;
    }


    public String getWeek() {
        return week;
    }
    public void setWeek(String week) {
        this.week = week;
    }

    public BigDecimal getInvestBenifit() {
        return investBenifit;
    }

    public void setInvestBenifit(BigDecimal investBenifit) {
        this.investBenifit = investBenifit;
    }

    
}
