/*
 * Project Name: kmfex-platform
 * File Name: ProductPackageInvestorDetailsDto.java
 * Class Name: ProductPackageInvestorDetailsDto
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
package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;

public class MarketProductDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId; // 融资项目编号

    private EWarrantyType warrantyType; // 风险保障/担保类型 A-本金担保, B-本息担保, C-资产监管, D-无担保

    private ETermType termType; // 期限类型 D-Day,M-Month,Y-Year';

    private Integer termLength; // 期限长度

    private int termToDays; // 期限转化为天数

    private BigDecimal rate; // 年利率

    private EPayMethodType payMethod; // 还款方式 01-到期一次还本付息,02-按月等额还息，到期一次还本, 03-按月等本等息

    private String warrantId; // 担保机构ID

    private String warrantIdShow; // 担保人(显示)

    private String warrantor; // 担保机构
    
    private String warrantorShow;

    @Deprecated
    private ERiskLevel riskLevel; // 风险等级
    
    private ERiskLevel productLevel; // 融资项目级别 （替换上边:风险等级）

    private String apprDate; // 发布时间

    public String getTerm() {
        return termLength + termType.getText();
    }

    public String getRatePercentage() {
        return rate == null ? "" : NumberUtil.getPercentStr(rate, 1, true);
    }

    /**
     * @return return the value of the var productId
     */

    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            Set productId value
     */

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return return the value of the var warrantyType
     */

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    /**
     * @param warrantyType
     *            Set warrantyType value
     */

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    /**
     * @return return the value of the var termType
     */

    public ETermType getTermType() {
        return termType;
    }

    /**
     * @param termType
     *            Set termType value
     */

    public void setTermType(ETermType termType) {
        this.termType = termType;
    }

    /**
     * @return return the value of the var termLength
     */

    public Integer getTermLength() {
        return termLength;
    }

    /**
     * @param termLength
     *            Set termLength value
     */

    public void setTermLength(Integer termLength) {
        this.termLength = termLength;
    }

    /**
     * @return return the value of the var termToDays
     */

    public int getTermToDays() {
        return termToDays;
    }

    /**
     * @param termToDays
     *            Set termToDays value
     */

    public void setTermToDays(int termToDays) {
        this.termToDays = termToDays;
    }

    /**
     * @return return the value of the var rate
     */

    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @param rate
     *            Set rate value
     */

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return return the value of the var payMethod
     */

    public EPayMethodType getPayMethod() {
        return payMethod;
    }

    /**
     * @param payMethod
     *            Set payMethod value
     */

    public void setPayMethod(EPayMethodType payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * @return return the value of the var warrantId
     */

    public String getWarrantId() {
        return warrantId;
    }

    /**
     * @param warrantId
     *            Set warrantId value
     */

    public void setWarrantId(String warrantId) {
        this.warrantId = warrantId;
    }

    /**
     * @return return the value of the var warrantor
     */

    public String getWarrantor() {
        return warrantor;
    }

    /**
     * @param warrantor
     *            Set warrantor value
     */

    public void setWarrantor(String warrantor) {
        this.warrantor = warrantor;
    }

    /**
     * @return return the value of the var riskLevel
     */

    public ERiskLevel getRiskLevel() {
        return riskLevel;
    }

    /**
     * @param riskLevel
     *            Set riskLevel value
     */

    public void setRiskLevel(ERiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

	/**
	 * @return the productLevel
	 */
	public ERiskLevel getProductLevel() {
		return productLevel;
	}

	/**
	 * @param productLevel the productLevel to set
	 */
	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}

	/**
     * @return return the value of the var apprDate
     */

    public String getApprDate() {
        return apprDate;
    }

    /**
     * @param apprDate
     *            Set apprDate value
     */

    public void setApprDate(String apprDate) {
        this.apprDate = apprDate;
    }

	public String getWarrantIdShow() {
		return warrantIdShow;
	}

	public void setWarrantIdShow(String warrantIdShow) {
		this.warrantIdShow = warrantIdShow;
	}

	public String getWarrantorShow() {
		return warrantorShow;
	}

	public void setWarrantorShow(String warrantorShow) {
		this.warrantorShow = warrantorShow;
	}

}
