/*
 * Project Name: kmfex-platform
 * File Name: CashDeposit.java
 * Class Name: CashDeposit
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

package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: CashDepositDto Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

public class CashDepositDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal financeServiceFee; // 融资服务合同保证金

    private BigDecimal warrantFee; // 担保公司-还款保证金

    private String warrantPercentage; // 担保公司-还款保证金百分比

    private BigDecimal contractFee; // 借款合同履约保证金

    private String contractFeePer; // 借款合同履约保证金百分比

    private BigDecimal serviceFee; // 融资服务费

    private BigDecimal riskFee; // 风险管理费

    /**
     * @return return the value of the var financeServiceFee
     */
    
    public BigDecimal getFinanceServiceFee() {
        return financeServiceFee;
    }

    /**
     * @param financeServiceFee Set financeServiceFee value
     */
    
    public void setFinanceServiceFee(BigDecimal financeServiceFee) {
        this.financeServiceFee = financeServiceFee;
    }

    /**
     * @return return the value of the var warrantFee
     */
    
    public BigDecimal getWarrantFee() {
        return warrantFee;
    }

    /**
     * @param warrantFee Set warrantFee value
     */
    
    public void setWarrantFee(BigDecimal warrantFee) {
        this.warrantFee = warrantFee;
    }

    /**
     * @return return the value of the var warrantPercentage
     */
    
    public String getWarrantPercentage() {
        return warrantPercentage;
    }

    /**
     * @param warrantPercentage Set warrantPercentage value
     */
    
    public void setWarrantPercentage(String warrantPercentage) {
        this.warrantPercentage = warrantPercentage;
    }

    /**
     * @return return the value of the var contractFee
     */
    
    public BigDecimal getContractFee() {
        return contractFee;
    }

    /**
     * @param contractFee Set contractFee value
     */
    
    public void setContractFee(BigDecimal contractFee) {
        this.contractFee = contractFee;
    }

    /**
     * @return return the value of the var contractFeePer
     */
    
    public String getContractFeePer() {
        return contractFeePer;
    }

    /**
     * @param contractFeePer Set contractFeePer value
     */
    
    public void setContractFeePer(String contractFeePer) {
        this.contractFeePer = contractFeePer;
    }

    /**
     * @return return the value of the var serviceFee
     */
    
    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    /**
     * @param serviceFee Set serviceFee value
     */
    
    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    /**
     * @return return the value of the var riskFee
     */
    
    public BigDecimal getRiskFee() {
        return riskFee;
    }

    /**
     * @param riskFee Set riskFee value
     */
    
    public void setRiskFee(BigDecimal riskFee) {
        this.riskFee = riskFee;
    }
}
