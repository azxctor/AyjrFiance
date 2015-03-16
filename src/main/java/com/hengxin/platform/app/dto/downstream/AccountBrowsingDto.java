/*
 * Project Name: kmfex-platform
 * File Name: AccountBrowsingDto.java
 * Class Name: AccountBrowsingDto
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
package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户总览下行DTO.
 * @author yicai
 *
 */
public class AccountBrowsingDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String totalAsset = "";
	private String availableAsset = "";
	private String recharge = "";
	private String withdrawal = "";
	private String currentSettlement = "";
	private String investment = "";
	private String financingRepayment = "";
	private List<InvestBenefitDto> investBenefit = new ArrayList<InvestBenefitDto>();

	public String getRecharge() {
		return recharge;
	}

	public void setRecharge(String recharge) {
		this.recharge = recharge;
	}

	public String getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(String withdrawal) {
		this.withdrawal = withdrawal;
	}

	public String getCurrentSettlement() {
		return currentSettlement;
	}

	public void setCurrentSettlement(String currentSettlement) {
		this.currentSettlement = currentSettlement;
	}

	public String getInvestment() {
		return investment;
	}

	public void setInvestment(String investment) {
		this.investment = investment;
	}

	public String getFinancingRepayment() {
		return financingRepayment;
	}

	public void setFinancingRepayment(String financingRepayment) {
		this.financingRepayment = financingRepayment;
	}

	public List<InvestBenefitDto> getInvestBenefit() {
		return investBenefit;
	}

	public void setInvestBenefit(List<InvestBenefitDto> investBenefit) {
		this.investBenefit = investBenefit;
	}

	public String getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}

	public String getAvailableAsset() {
		return availableAsset;
	}

	public void setAvailableAsset(String availableAsset) {
		this.availableAsset = availableAsset;
	}

}
