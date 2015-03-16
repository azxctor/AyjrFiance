
/*
* Project Name: kmfex-platform
* File Name: BenifitDto.java
* Class Name: BenifitDto
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
import java.util.List;

import com.hengxin.platform.account.dto.AccountOverviewDto;
import com.hengxin.platform.account.dto.InvestBenifitDto;


/**
 * Class Name: BenifitDto
 * Description: TODO
 * @author congzhou
 *
 */

public class BenifitDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 账户概览
     */
    private AccountOverviewDto accountOverview;
    
    /**
     * 投资收益
     */
    private List<InvestBenifitDto> investBenifit;

    public AccountOverviewDto getAccountOverview() {
        return accountOverview;
    }

    public void setAccountOverview(AccountOverviewDto accountOverview) {
        this.accountOverview = accountOverview;
    }

    public List<InvestBenifitDto> getInvestBenifit() {
        return investBenifit;
    }

    public void setInvestBenifit(List<InvestBenifitDto> investBenifit) {
        this.investBenifit = investBenifit;
    }
    
}
