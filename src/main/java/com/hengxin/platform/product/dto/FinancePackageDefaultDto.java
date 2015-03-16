/*
 * Project Name: kmfex-platform
 * File Name: FinancePackageDefaultDto.java
 * Class Name: FinancePackageDefaultDto
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
import java.util.Date;

import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;

/**
 * Class Name: FinancePackageDefaultDto
 *
 * @author shengzhou
 *
 */
public class FinancePackageDefaultDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 融资包编号
    private String packageId;

    // 产品编号
    private String productId;

    // 融资人编号
    private String financerId;
    
    // 融资人姓名
    private String financierName;

    // 实际申购额
    private BigDecimal packageQuota;

    // 融资包简称
    private String packageName;

    // 担保机构编号
    private String warrantId;
    
    // 担保机构名字
    private String wrtrName;
    
    private String warrantIdSw;
    
    private String wrtrNameShow;
    
    // 还款期数
    private int sequenceId;
    
    private Date paymentDate;
    
    // 融资包状态
    private EPackageStatus status;
    
    // 还款计划状态
    private EPayStatus payStatus;
    
    // 欠款额
    private BigDecimal debt;

    // 融资人可还款额
    private BigDecimal financerAmount;

    // 担保机构可还款额
    private BigDecimal warrantAmount;
    
    //合计可还款额
    private BigDecimal totalAmount;
    
    private boolean cleared;
    /**
     * @return return the value of the var packageId
     */
    
    public String getPackageId() {
        return packageId;
    }
    /**
     * @param packageId Set packageId value
     */
    
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
    /**
     * @return return the value of the var productId
     */
    
    public String getProductId() {
        return productId;
    }
    /**
     * @param productId Set productId value
     */
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    /**
     * @return return the value of the var financerId
     */
    
    public String getFinancerId() {
        return financerId;
    }
    /**
     * @param financerId Set financerId value
     */
    
    public void setFinancerId(String financerId) {
        this.financerId = financerId;
    }
    /**
     * @return return the value of the var packageQuota
     */
    
    public BigDecimal getPackageQuota() {
        return packageQuota;
    }
    /**
     * @param packageQuota Set packageQuota value
     */
    
    public void setPackageQuota(BigDecimal packageQuota) {
        this.packageQuota = packageQuota;
    }
    /**
     * @return return the value of the var packageName
     */
    
    public String getPackageName() {
        return packageName;
    }
    /**
     * @param packageName Set packageName value
     */
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    /**
     * @return return the value of the var warrantId
     */
    
    public String getWarrantId() {
        return warrantId;
    }
    /**
     * @param warrantId Set warrantId value
     */
    
    public void setWarrantId(String warrantId) {
        this.warrantId = warrantId;
    }
    /**
     * @return return the value of the var wrtrName
     */
    
    public String getWrtrName() {
        return wrtrName;
    }
    /**
     * @param wrtrName Set wrtrName value
     */
    
    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }
    /**
     * @return return the value of the var sequenceId
     */
    
    public int getSequenceId() {
        return sequenceId;
    }
    /**
     * @param sequenceId Set sequenceId value
     */
    
    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
    /**
     * @return return the value of the var status
     */
    
    public EPackageStatus getStatus() {
        return status;
    }
    /**
     * @param status Set status value
     */
    
    public void setStatus(EPackageStatus status) {
        this.status = status;
    }
    /**
     * @return return the value of the var debt
     */
    
    public BigDecimal getDebt() {
        return debt;
    }
    /**
     * @param debt Set debt value
     */
    
    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }
    /**
     * @return return the value of the var financerAmount
     */
    
    public BigDecimal getFinancerAmount() {
        return financerAmount;
    }
    /**
     * @param financerAmount Set financerAmount value
     */
    
    public void setFinancerAmount(BigDecimal financerAmount) {
        this.financerAmount = financerAmount;
    }
    /**
     * @return return the value of the var warrantAmount
     */
    
    public BigDecimal getWarrantAmount() {
        return warrantAmount;
    }
    /**
     * @param warrantAmount Set warrantAmount value
     */
    
    public void setWarrantAmount(BigDecimal warrantAmount) {
        this.warrantAmount = warrantAmount;
    }
    /**
     * @return return the value of the var financierName
     */
    
    public String getFinancierName() {
        return financierName;
    }
    /**
     * @param financierName Set financierName value
     */
    
    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }
    /**
     * @return return the value of the var totalAmount
     */
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    /**
     * @param totalAmount Set totalAmount value
     */
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    /**
     * @return return the value of the var cleared
     */
    
    public boolean isCleared() {
        return cleared;
    }
    /**
     * @param cleared Set cleared value
     */
    
    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
	public EPayStatus getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(EPayStatus payStatus) {
		this.payStatus = payStatus;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getWarrantIdSw() {
		return warrantIdSw;
	}
	public void setWarrantIdSw(String warrantIdSw) {
		this.warrantIdSw = warrantIdSw;
	}
	public String getWrtrNameShow() {
		return wrtrNameShow;
	}
	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}

}
