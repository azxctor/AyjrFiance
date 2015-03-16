/*
 * Project Name: kmfex-platform
 * File Name: ProductFeeDto.java
 * Class Name: ProductFeeDto
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
import java.util.Map;

/**
 * Class Name: ProductSystemFeeDto Description: TODO
 *
 * @author Ryan
 *
 */

public class ProductSystemFeeDto implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String financierSeatFee;// 融资会员席位费（参考值）
    private String financeFee; //融资服务合同保证金
    
    private String contractFee; // 借款合同履约保证金
    
    private String warrantFee; // 担保公司-还款保证金

    private String riskFee;// 风险管理费

    private Map<String,BigDecimal> feeMap ;//// 融资服务费 ,风险管理费

    /**
     * @return return the value of the var financierSeatFee
     */
    
    public String getFinancierSeatFee() {
        return financierSeatFee;
    }

    /**
     * @param financierSeatFee Set financierSeatFee value
     */
    
    public void setFinancierSeatFee(String financierSeatFee) {
        this.financierSeatFee = financierSeatFee;
    }

    /**
     * @return return the value of the var contractFee
     */
    
    public String getContractFee() {
        return contractFee;
    }

    /**
     * @param contractFee Set contractFee value
     */
    
    public void setContractFee(String contractFee) {
        this.contractFee = contractFee;
    }

    /**
     * @return return the value of the var warrantFee
     */
    
    public String getWarrantFee() {
        return warrantFee;
    }

    /**
     * @param warrantFee Set warrantFee value
     */
    
    public void setWarrantFee(String warrantFee) {
        this.warrantFee = warrantFee;
    }

    /**
     * @return return the value of the var riskFee
     */
    
    public String getRiskFee() {
        return riskFee;
    }

    /**
     * @param riskFee Set riskFee value
     */
    
    public void setRiskFee(String riskFee) {
        this.riskFee = riskFee;
    }

    /**
     * @return return the value of the var feeMap
     */
    
    public Map<String, BigDecimal> getFeeMap() {
        return feeMap;
    }

    /**
     * @param feeMap Set feeMap value
     */
    
    public void setFeeMap(Map<String, BigDecimal> feeMap) {
        this.feeMap = feeMap;
    }

    /**
     * @return return the value of the var financeFee
     */
    
    public String getFinanceFee() {
        return financeFee;
    }

    /**
     * @param financeFee Set financeFee value
     */
    
    public void setFinanceFee(String financeFee) {
        this.financeFee = financeFee;
    }
    
    


}
