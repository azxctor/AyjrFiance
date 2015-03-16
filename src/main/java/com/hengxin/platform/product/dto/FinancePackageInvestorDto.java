/*
 * Project Name: kmfex-platform
 * File Name: FinancePackageInvestorDto.java
 * Class Name: FinancePackageInvestorDto
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

import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * Class Name: FinancePackageInvestorDto
 * 
 * @author shengzhou
 * 
 */
public class FinancePackageInvestorDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 融资包编号
    private String pkgId;

    // 份额编号
    private String lotId;

    // 产品编号
    private String productId;

    // 用户编号
    private String userId;

    // 融资包简称
    private String packageName;

    // 签约时间
    private String signContractTime;

    // 申购时间
    private String subsDate;

    // 年利率
    private BigDecimal rate;

    // 期限
    private String term;

    // 协议本金
    private BigDecimal amount;

    // 剩余本息合计
    private BigDecimal lastAmount;
    
    // 剩余本金
    private BigDecimal residualPrincipalAmt;
    
    // 剩余利息
    private BigDecimal residualInterestAmt;

    // 下一收益日
    private String nextPayDay;

    // 下一收益额
    private BigDecimal nextPayAmount;

    // 状态
    private EPackageStatus status;

    // 最后还款日
    private String lastPayDay;

    // 系统利率
    private BigDecimal sysRate;

    // 还款利息（包括罚息）平台扣除比例
    private BigDecimal paymentRate;

    // 融资人
    private String financierName;

    // 担保机构
    private String wrtrName;
    
    private String wrtrNameShow;

    // 是否定投
    private String aipFlag;
    
    // 成本价
    private BigDecimal costPrice;
    
    // 剩余期数
    private Integer remTermLength;
    
    // 还款方式
    private EPayMethodType payMethod;
    
    // 担保方式
    private EWarrantyType warrantyType;

    private boolean canTransferCancel;
    private boolean canCreditorRightsTransfer;
    private boolean fromTransfer;

    private BigDecimal transferPrice;

    private boolean deductFlag;
    
    private BigDecimal transferMaxPrice;
    private BigDecimal transferMinPrice;
    
    public BigDecimal getTransferMinPrice() {
    	return transferMinPrice;
    }
    
    /**
	 * @return the transferMaxPrice
	 */
	public BigDecimal getTransferMaxPrice() {
		return transferMaxPrice;
	}

	/**
	 * @param transferMaxPrice the transferMaxPrice to set
	 */
	public void setTransferMaxPrice(BigDecimal transferMaxPrice) {
		this.transferMaxPrice = transferMaxPrice;
	}

	/**
     * @return return the value of the var pkgId
     */

    public String getPkgId() {
        return pkgId;
    }

    /**
     * @param pkgId
     *            Set pkgId value
     */

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    /**
     * @return return the value of the var lotId
     */

    public String getLotId() {
        return lotId;
    }

    /**
     * @param lotId
     *            Set lotId value
     */

    public void setLotId(String lotId) {
        this.lotId = lotId;
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
     * @return return the value of the var userId
     */

    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            Set userId value
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return return the value of the var packageName
     */

    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            Set packageName value
     */

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return return the value of the var signContractTime
     */

    public String getSignContractTime() {
        return signContractTime;
    }

    /**
     * @param signContractTime
     *            Set signContractTime value
     */

    public void setSignContractTime(String signContractTime) {
        this.signContractTime = signContractTime;
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
     * @return return the value of the var term
     */

    public String getTerm() {
        return term;
    }

    /**
     * @param term
     *            Set term value
     */

    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return return the value of the var amount
     */

    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     *            Set amount value
     */

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return return the value of the var lastAmount
     */

    public BigDecimal getLastAmount() {
        return lastAmount;
    }

    /**
     * @param lastAmount
     *            Set lastAmount value
     */

    public void setLastAmount(BigDecimal lastAmount) {
        this.lastAmount = lastAmount;
    }

    /**
     * @return return the value of the var nextPayDay
     */

    public String getNextPayDay() {
        return nextPayDay;
    }

    /**
     * @param nextPayDay
     *            Set nextPayDay value
     */

    public void setNextPayDay(String nextPayDay) {
        this.nextPayDay = nextPayDay;
    }

    /**
     * @return return the value of the var nextPayAmount
     */

    public BigDecimal getNextPayAmount() {
        return nextPayAmount;
    }

    /**
     * @param nextPayAmount
     *            Set nextPayAmount value
     */

    public void setNextPayAmount(BigDecimal nextPayAmount) {
        this.nextPayAmount = nextPayAmount;
    }

    /**
     * @return return the value of the var status
     */

    public EPackageStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            Set status value
     */

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    /**
     * @return return the value of the var lastPayDay
     */

    public String getLastPayDay() {
        return lastPayDay;
    }

    /**
     * @param lastPayDay
     *            Set lastPayDay value
     */

    public void setLastPayDay(String lastPayDay) {
        this.lastPayDay = lastPayDay;
    }

    /**
     * @return return the value of the var sysRate
     */

    public BigDecimal getSysRate() {
        return sysRate;
    }

    /**
     * @param sysRate
     *            Set sysRate value
     */

    public void setSysRate(BigDecimal sysRate) {
        this.sysRate = sysRate;
    }

    public boolean isCanTransferCancel() {
        return canTransferCancel;
    }

    public void setCanTransferCancel(boolean canTransferCancel) {
        this.canTransferCancel = canTransferCancel;
    }

    public boolean isCanCreditorRightsTransfer() {
        return canCreditorRightsTransfer;
    }

    public void setCanCreditorRightsTransfer(boolean canCreditorRightsTransfer) {
        this.canCreditorRightsTransfer = canCreditorRightsTransfer;
    }

    /**
     * @return return the value of the var subsDate
     */

    public String getSubsDate() {
        return subsDate;
    }

    /**
     * @param subsDate
     *            Set subsDate value
     */

    public void setSubsDate(String subsDate) {
        this.subsDate = subsDate;
    }

    public String getFinancierName() {
        return financierName;
    }

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    public String getWrtrName() {
        return wrtrName;
    }

    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }

    /**
     * @return return the value of the var paymentRate
     */

    public BigDecimal getPaymentRate() {
        return paymentRate;
    }

    /**
     * @param paymentRate
     *            Set paymentRate value
     */

    public void setPaymentRate(BigDecimal paymentRate) {
        this.paymentRate = paymentRate;
    }

    public BigDecimal getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }

    /**
     * @return return the value of the var aipFlag
     */

    public String getAipFlag() {
        return aipFlag;
    }

    /**
     * @param aipFlag
     *            Set aipFlag value
     */

    public void setAipFlag(String aipFlag) {
        this.aipFlag = aipFlag;
    }

    public boolean isDeductFlag() {
        return deductFlag;
    }

    public void setDeductFlag(boolean deductFlag) {
        this.deductFlag = deductFlag;
    }

	public BigDecimal getResidualPrincipalAmt() {
		return residualPrincipalAmt;
	}

	public void setResidualPrincipalAmt(BigDecimal residualPrincipalAmt) {
		this.residualPrincipalAmt = residualPrincipalAmt;
	}

	public BigDecimal getResidualInterestAmt() {
		return residualInterestAmt;
	}

	public void setResidualInterestAmt(BigDecimal residualInterestAmt) {
		this.residualInterestAmt = residualInterestAmt;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public boolean isFromTransfer() {
		return fromTransfer;
	}

	public void setFromTransfer(boolean fromTransfer) {
		this.fromTransfer = fromTransfer;
	}

	public Integer getRemTermLength() {
		return remTermLength;
	}

	public void setRemTermLength(Integer remTermLength) {
		this.remTermLength = remTermLength;
	}

	public EPayMethodType getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(EPayMethodType payMethod) {
		this.payMethod = payMethod;
	}

	public EWarrantyType getWarrantyType() {
		return warrantyType;
	}

	public void setWarrantyType(EWarrantyType warrantyType) {
		this.warrantyType = warrantyType;
	}

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}

	/**
	 * @param transferMinPrice the transferMinPrice to set
	 */
	public void setTransferMinPrice(BigDecimal transferMinPrice) {
		this.transferMinPrice = transferMinPrice;
	}

}
