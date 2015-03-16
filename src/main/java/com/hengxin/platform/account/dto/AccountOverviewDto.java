
/*
* Project Name: kmfex-platform
* File Name: AccountOverviewDto.java
* Class Name: AccountOverviewDto
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
 * Class Name: AccountOverviewDto
 * Description: TODO
 * @author congzhou
 *
 */

public class AccountOverviewDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 充值
     */
    private String rechargeSum;
    /**
     * 充值数值
     */
    private BigDecimal rechargeNum;
    /**
     * 充值比例
     */
    private String rechargeProp;
    /**
     * 提现
     */
    private String withdrawSum;
    /**
     * 提现数值
     */
    private BigDecimal withdrawNum;
    /**
     * 提现比例
     */
    private String withdrawProp;
    /**
     * 活期结息
     */
    private String interestSum;
    /**
     * 活期数值
     */
    private BigDecimal interestNum;
    /**
     * 活期结息比例
     */
    private String interestProp;
    /**
     * 投资申购
     */
    private String investSum;
    /**
     * 投资申购数值
     */
    private BigDecimal investNum;
    /**
     * 投资申购比例
     */
    private String investProp;
    /**
     * 融资还款
     */
    private String financeSum;
    /**
     * 融资还款数值
     */
    private BigDecimal financeNum;
    /**
     * 融资还款比例
     */
    private String financeProp;
    public String getRechargeSum() {
        return rechargeSum;
    }
    public void setRechargeSum(String rechargeSum) {
        this.rechargeSum = rechargeSum;
    }
    public String getRechargeProp() {
        return rechargeProp;
    }
    public void setRechargeProp(String rechargeProp) {
        this.rechargeProp = rechargeProp;
    }
    public String getWithdrawSum() {
        return withdrawSum;
    }
    public void setWithdrawSum(String withdrawSum) {
        this.withdrawSum = withdrawSum;
    }
    public String getWithdrawProp() {
        return withdrawProp;
    }
    public void setWithdrawProp(String withdrawProp) {
        this.withdrawProp = withdrawProp;
    }
    public String getInterestSum() {
        return interestSum;
    }
    public void setInterestSum(String interestSum) {
        this.interestSum = interestSum;
    }
    public String getInterestProp() {
        return interestProp;
    }
    public void setInterestProp(String interestProp) {
        this.interestProp = interestProp;
    }
    public String getInvestSum() {
        return investSum;
    }
    public void setInvestSum(String investSum) {
        this.investSum = investSum;
    }
    public String getInvestProp() {
        return investProp;
    }
    public void setInvestProp(String investProp) {
        this.investProp = investProp;
    }
    public String getFinanceSum() {
        return financeSum;
    }
    public void setFinanceSum(String financeSum) {
        this.financeSum = financeSum;
    }
    public String getFinanceProp() {
        return financeProp;
    }
    public void setFinanceProp(String financeProp) {
        this.financeProp = financeProp;
    }
    public BigDecimal getRechargeNum() {
        return rechargeNum;
    }
    public void setRechargeNum(BigDecimal rechargeNum) {
        this.rechargeNum = rechargeNum;
    }
    public BigDecimal getWithdrawNum() {
        return withdrawNum;
    }
    public void setWithdrawNum(BigDecimal withdrawNum) {
        this.withdrawNum = withdrawNum;
    }
    public BigDecimal getInterestNum() {
        return interestNum;
    }
    public void setInterestNum(BigDecimal interestNum) {
        this.interestNum = interestNum;
    }
    public BigDecimal getInvestNum() {
        return investNum;
    }
    public void setInvestNum(BigDecimal investNum) {
        this.investNum = investNum;
    }
    public BigDecimal getFinanceNum() {
        return financeNum;
    }
    public void setFinanceNum(BigDecimal financeNum) {
        this.financeNum = financeNum;
    }
    
}
