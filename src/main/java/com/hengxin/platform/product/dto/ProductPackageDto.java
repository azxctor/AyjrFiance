/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductController.java
 * Class Name: FinanceProductController
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
import java.util.List;

import javax.validation.constraints.NotNull;

import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.validator.FinancingPackageAIPEndTimeCheck;
import com.hengxin.platform.product.validator.FinancingPackageAIPStartTimeCheck;
import com.hengxin.platform.product.validator.FinancingPackagePrepublishTimeCheck;
import com.hengxin.platform.product.validator.FinancingPackageQuotaCheck;
import com.hengxin.platform.product.validator.FinancingPackageSupplyEndTimeCheck;
import com.hengxin.platform.product.validator.FinancingPackageSupplyStartTimeCheck;

/**
 * 
 * Class Name: ProductPackageDto
 * 
 */
@FinancingPackageSupplyEndTimeCheck(supplyEndTime = "supplyEndTime")
@FinancingPackageAIPEndTimeCheck(aipEndTime = "aipEndTime")
@FinancingPackageAIPStartTimeCheck(aipStartTime = "aipStartTime")
@FinancingPackageSupplyStartTimeCheck(supplyStartTime="supplyStartTime")
@FinancingPackagePrepublishTimeCheck(prePublicTime="prePublicTime")
@FinancingPackageQuotaCheck(packageQuota="packageQuota")
public class ProductPackageDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String productId;
    private String packageName;
    private String userName;
    @NotNull(message = "{package.error.quota.empty}")
    private BigDecimal packageQuota;// 融资包金额
    private Integer unit; // 份数
    private BigDecimal unitAmount; // 每份金额
    private BigDecimal supplyUserCount;// 已申购人数
    private BigDecimal supplyAmount;// 已申购金额
    private EPackageStatus status = EPackageStatus.NULL;

    private String finaceFreezeKeepId;// 资金冻结/保留流水号

    private String createOperatorId;
    private String createTime;
    private String lastOperatorId;
    private String lastTime;
    private String lastSubsTime;
    @NotNull(message = "package.error.supplyStartTime.empty")
    private String supplyStartTime;
    @NotNull(message = "package.error.supplyEndTime.empty")
    private String supplyEndTime;

    private Boolean aipFlag;
    private String aipStartTime;
    private String aipEndTime;
    private String aipGroupIds;
    
    private String lastSignDate;

    private int index;

    private Boolean autoSubscribeFlag;
    private boolean instantPublish;

    private String signContractTime;
    @NotNull(message = "package.error.prePublicTime.empty")
    private String prePublicTime;

    private String duration;

    private String subsPercent;
    private List<ProductAIPGroupDto> productAIPDtoList;
    private List<AIPGroupDto> aipGroupList;
    private String financierName;
    private String wrtrName;
    private String wrtrNameShow;
    private EWarrantyType warrantyType;// 风险保障

    private String nextPayDate; // 下一个还款日期
    private String itemsString; // 期数
    private String accountNo; // 交易账号

    // permission
    private boolean canCancelPackage;
    private boolean canStopPackage;
    private boolean canSignContract;
    private boolean canViewRepayTable;
    private boolean canPrepayment;
    private boolean canPrintDebtInfo;
    
    private String signingDt;

    private int updateStatus;
    private boolean isMarketOpen;
    private Long versionCount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return return the value of the var id
     */

    public String getId() {
        return id;
    }

    /**
     * @param id
     *            Set id value
     */

    public void setId(String id) {
        this.id = id;
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
     * @return return the value of the var packageQuota
     */

    public BigDecimal getPackageQuota() {
        return packageQuota;
    }
    

    /**
     * @param packageQuota
     *            Set packageQuota value
     */

    public void setPackageQuota(BigDecimal packageQuota) {
        this.packageQuota = packageQuota;
    }

    /**
     * @return return the value of the var unit
     */

    public Integer getUnit() {
        return unit;
    }

    /**
     * @param unit
     *            Set unit value
     */

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    /**
     * @return return the value of the var unitAmount
     */

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    /**
     * @param unitAmount
     *            Set unitAmount value
     */

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    /**
     * @return return the value of the var supplyUserCount
     */

    public BigDecimal getSupplyUserCount() {
        if (supplyUserCount == null) {
            return BigDecimal.ZERO;
        }
        return supplyUserCount;
    }

    /**
     * @param supplyUserCount
     *            Set supplyUserCount value
     */

    public void setSupplyUserCount(BigDecimal supplyUserCount) {
        this.supplyUserCount = supplyUserCount;
    }
    

    public String getLastSignDate() {
        return lastSignDate;
    }

    public void setLastSignDate(String lastSignDate) {
        this.lastSignDate = lastSignDate;
    }

    /**
     * @return return the value of the var supplyAmount
     */

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    /**
     * @param supplyAmount
     *            Set supplyAmount value
     */

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
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
     * @return return the value of the var finaceFreezeKeepId
     */

    public String getFinaceFreezeKeepId() {
        return finaceFreezeKeepId;
    }

    /**
     * @param finaceFreezeKeepId
     *            Set finaceFreezeKeepId value
     */

    public void setFinaceFreezeKeepId(String finaceFreezeKeepId) {
        this.finaceFreezeKeepId = finaceFreezeKeepId;
    }

    /**
     * @return return the value of the var createOperatorId
     */

    public String getCreateOperatorId() {
        return createOperatorId;
    }

    /**
     * @param createOperatorId
     *            Set createOperatorId value
     */

    public void setCreateOperatorId(String createOperatorId) {
        this.createOperatorId = createOperatorId;
    }

    /**
     * @return return the value of the var createTime
     */

    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            Set createTime value
     */

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return return the value of the var lastOperatorId
     */

    public String getLastOperatorId() {
        return lastOperatorId;
    }

    /**
     * @param lastOperatorId
     *            Set lastOperatorId value
     */

    public void setLastOperatorId(String lastOperatorId) {
        this.lastOperatorId = lastOperatorId;
    }

    /**
     * @return return the value of the var lastTime
     */

    public String getLastTime() {
        return lastTime;
    }

    /**
     * @param lastTime
     *            Set lastTime value
     */

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @return return the value of the var lastSubsTime
     */

    public String getLastSubsTime() {
        return lastSubsTime;
    }

    /**
     * @param lastSubsTime
     *            Set lastSubsTime value
     */

    public void setLastSubsTime(String lastSubsTime) {
        this.lastSubsTime = lastSubsTime;
    }

    /**
     * @return return the value of the var supplyStartTime
     */

    public String getSupplyStartTime() {
        return supplyStartTime;
    }

    /**
     * @param supplyStartTime
     *            Set supplyStartTime value
     */

    public void setSupplyStartTime(String supplyStartTime) {
        this.supplyStartTime = supplyStartTime;
    }

    /**
     * @return return the value of the var supplyEndTime
     */

    public String getSupplyEndTime() {
        return supplyEndTime;
    }

    /**
     * @param supplyEndTime
     *            Set supplyEndTime value
     */

    public void setSupplyEndTime(String supplyEndTime) {
        this.supplyEndTime = supplyEndTime;
    }

    /**
     * @return return the value of the var aipStartTime
     */

    public String getAipStartTime() {
        return aipStartTime;
    }

    /**
     * @param aipStartTime
     *            Set aipStartTime value
     */

    public void setAipStartTime(String aipStartTime) {
        this.aipStartTime = aipStartTime;
    }

    /**
     * @return return the value of the var aipEndTime
     */

    public String getAipEndTime() {
        return aipEndTime;
    }

    /**
     * @param aipEndTime
     *            Set aipEndTime value
     */

    public void setAipEndTime(String aipEndTime) {
        this.aipEndTime = aipEndTime;
    }

    /**
     * @return return the value of the var aipFlag
     */

    public Boolean getAipFlag() {
        return aipFlag;
    }

    /**
     * @param aipFlag
     *            Set aipFlag value
     */

    public void setAipFlag(Boolean aipFlag) {
        this.aipFlag = aipFlag;
    }

    /**
     * @return return the value of the var index
     */

    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *            Set index value
     */

    public void setIndex(int index) {
        this.index = index;
    }

    public Boolean getAutoSubscribeFlag() {
        return autoSubscribeFlag;
    }

    public void setAutoSubscribeFlag(Boolean autoSubscribeFlag) {
        this.autoSubscribeFlag = autoSubscribeFlag;
    }

    public String getAipGroupIds() {
        return aipGroupIds;
    }

    public void setAipGroupIds(String aipGroupIds) {
        this.aipGroupIds = aipGroupIds;
    }

    /**
     * @return return the value of the var productAIPDtoList
     */

    public List<ProductAIPGroupDto> getProductAIPDtoList() {
        return productAIPDtoList;
    }

    /**
     * @param productAIPDtoList
     *            Set productAIPDtoList value
     */

    public void setProductAIPDtoList(List<ProductAIPGroupDto> productAIPDtoList) {
        this.productAIPDtoList = productAIPDtoList;
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

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public boolean isCanCancelPackage() {
        return canCancelPackage;
    }

    public void setCanCancelPackage(boolean canCancelPackage) {
        this.canCancelPackage = canCancelPackage;
    }

    public boolean isCanStopPackage() {
        return canStopPackage;
    }

    public void setCanStopPackage(boolean canStopPackage) {
        this.canStopPackage = canStopPackage;
    }

    public boolean isCanSignContract() {
        return canSignContract;
    }

    public void setCanSignContract(boolean canSignContract) {
        this.canSignContract = canSignContract;
    }

    public boolean isCanViewRepayTable() {
        return canViewRepayTable;
    }

    public void setCanViewRepayTable(boolean canViewRepayTable) {
        this.canViewRepayTable = canViewRepayTable;
    }

    public boolean getInstantPublish() {
        return instantPublish;
    }

    public void setInstantPublish(boolean instantPublish) {
        this.instantPublish = instantPublish;
    }

    public String getSignContractTime() {
        return signContractTime;
    }

    public void setSignContractTime(String signContractTime) {
        this.signContractTime = signContractTime;
    }

    public String getPrePublicTime() {
        return prePublicTime;
    }

    public void setPrePublicTime(String prePublicTime) {
        this.prePublicTime = prePublicTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSubsPercent() {
        return subsPercent;
    }

    public void setSubsPercent(String subsPercent) {
        this.subsPercent = subsPercent;
    }

    public List<AIPGroupDto> getAipGroupList() {
        return aipGroupList;
    }

    public void setAipGroupList(List<AIPGroupDto> aipGroupList) {
        this.aipGroupList = aipGroupList;
    }

    public String getNextPayDate() {
        return nextPayDate;
    }

    public void setNextPayDate(String nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    public String getItemsString() {
        return itemsString;
    }

    public void setItemsString(String itemsString) {
        this.itemsString = itemsString;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    public boolean isCanPrepayment() {
        return canPrepayment;
    }

    public void setCanPrepayment(boolean canPrepayment) {
        this.canPrepayment = canPrepayment;
    }

    public String getSigningDt() {
        return signingDt;
    }

    public void setSigningDt(String signingDt) {
        this.signingDt = signingDt;
    }

    public boolean isMarketOpen() {
        return isMarketOpen;
    }

    public void setMarketOpen(boolean isMarketOpen) {
        this.isMarketOpen = isMarketOpen;
    }

    public Long getVersionCount() {
        return versionCount;
    }

    public void setVersionCount(Long versionCount) {
        this.versionCount = versionCount;
    }
	
    public boolean isCanPrintDebtInfo() {
        return canPrintDebtInfo;
    }

    public void setCanPrintDebtInfo(boolean canPrintDebtInfo) {
        this.canPrintDebtInfo = canPrintDebtInfo;
    }

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}
}
