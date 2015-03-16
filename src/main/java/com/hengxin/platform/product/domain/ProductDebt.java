/*
 * Project Name: kmfex-platform
 * File Name: ProductAssetVehicle.java
 * Class Name: ProductAssetVehicle
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

package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class Name: ProductAssetVehicle Description: 融资产品资产信息
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_DEBT")
public class ProductDebt extends BaseProduct implements Serializable {

    @Column(name = "DEBT_TYPE")
    private String type; // 类型

    @Column(name = "DEBT_AMT")
    private BigDecimal debtAmount; // 负债额

    @Column(name = "DEBT_MTH_PAY")
    private BigDecimal monthlyPayment; // 每月还款额

    @Column(name = "DEBT_DESC")
    private String notes; // 说明


    /**
     * @return return the value of the var type
     */
    
    public String getType() {
        return type;
    }

    /**
     * @param type Set type value
     */
    
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return return the value of the var debtAmount
     */

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    /**
     * @param debtAmount
     *            Set debtAmount value
     */

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }

    /**
     * @return return the value of the var monthlyPayment
     */

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    /**
     * @param monthlyPayment
     *            Set monthlyPayment value
     */

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    /**
     * @return return the value of the var notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     *            Set notes value
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
