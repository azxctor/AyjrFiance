/*
 * Project Name: kmfex-platform
 * File Name: MyFinancierPackagesDetailV3DownDto.java
 * Class Name: MyFinancierPackagesDetailV3DownDto
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

import com.hengxin.platform.product.enums.ERiskLevel;

/**
 * MyFinancierPackagesListDownDto：pad，融资包管理，融资包详情下行DTO.
 * 
 * @author yicai
 *
 */
public class MyFinancierPackagesDetailV3DownDto implements Serializable {

	private static final long serialVersionUID = 5L;

	/* 融资包信息 */
	private String id; // 融资包编号
	private String packageName; // 融资包简称
	private String productName; // 项目简称
    private String accountNo;// 交易账号
    private String packageQuota;// 融资包金额
    private String financierName;// 融资人名字
    private String rate; // 年利率
    private String guaranteeLicenseNoImg; // 营业执照中间量
    private String guaranteeLicenseNoImgUrl; // 营业执照
    private String duration; // 融资期限
    private String warrantyType; // 风险保障
    private String payMethod; // 还款方式
    private String loanPurpose; // 用途
    private String wrtrName; // 担保机构 (无"/")
    private String overdue2CmpnsDays; // 逾期转代偿期限
    /*判断是否为待放款审批及以后状态（涉及显示：签约日期，合同模板）*/
    private String statusAfterWaitLoadApproal;
    private String contractTemplateId;// 合同模板ID
    private ERiskLevel productLevel; // 融资项目级别
    private String aipFlag;// 是否定投
    private String autoSubsFlag;// 是否自动申购
    private String supplyStartTime;// 申购起始时间
    private String supplyEndTime;// 申购截止时间
    private String supplyUserCount;// 已申购人数
    private String subsPercent;// 申购进度
    private String supplyAmount; // 实际申购额
    
    
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	/**
	 * @return the packageQuota
	 */
	public String getPackageQuota() {
		return packageQuota;
	}
	/**
	 * @param packageQuota the packageQuota to set
	 */
	public void setPackageQuota(String packageQuota) {
		this.packageQuota = packageQuota;
	}
	/**
	 * @return the financierName
	 */
	public String getFinancierName() {
		return financierName;
	}
	/**
	 * @param financierName the financierName to set
	 */
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}
	/**
	 * @return the guaranteeLicenseNoImg
	 */
	public String getGuaranteeLicenseNoImg() {
		return guaranteeLicenseNoImg;
	}
	/**
	 * @param guaranteeLicenseNoImg the guaranteeLicenseNoImg to set
	 */
	public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
		this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
	}
	/**
	 * @return the guaranteeLicenseNoImgUrl
	 */
	public String getGuaranteeLicenseNoImgUrl() {
		return guaranteeLicenseNoImgUrl;
	}
	/**
	 * @param guaranteeLicenseNoImgUrl the guaranteeLicenseNoImgUrl to set
	 */
	public void setGuaranteeLicenseNoImgUrl(String guaranteeLicenseNoImgUrl) {
		this.guaranteeLicenseNoImgUrl = guaranteeLicenseNoImgUrl;
	}
	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * @return the warrantyType
	 */
	public String getWarrantyType() {
		return warrantyType;
	}
	/**
	 * @param warrantyType the warrantyType to set
	 */
	public void setWarrantyType(String warrantyType) {
		this.warrantyType = warrantyType;
	}
	/**
	 * @return the payMethod
	 */
	public String getPayMethod() {
		return payMethod;
	}
	/**
	 * @param payMethod the payMethod to set
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * @return the loanPurpose
	 */
	public String getLoanPurpose() {
		return loanPurpose;
	}
	/**
	 * @param loanPurpose the loanPurpose to set
	 */
	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}
	/**
	 * @return the wrtrName
	 */
	public String getWrtrName() {
		return wrtrName;
	}
	/**
	 * @param wrtrName the wrtrName to set
	 */
	public void setWrtrName(String wrtrName) {
		this.wrtrName = wrtrName;
	}
	/**
	 * @return the overdue2CmpnsDays
	 */
	public String getOverdue2CmpnsDays() {
		return overdue2CmpnsDays;
	}
	/**
	 * @param overdue2CmpnsDays the overdue2CmpnsDays to set
	 */
	public void setOverdue2CmpnsDays(String overdue2CmpnsDays) {
		this.overdue2CmpnsDays = overdue2CmpnsDays;
	}
	/**
	 * @return the statusAfterWaitLoadApproal
	 */
	public String getStatusAfterWaitLoadApproal() {
		return statusAfterWaitLoadApproal;
	}
	/**
	 * @param statusAfterWaitLoadApproal the statusAfterWaitLoadApproal to set
	 */
	public void setStatusAfterWaitLoadApproal(String statusAfterWaitLoadApproal) {
		this.statusAfterWaitLoadApproal = statusAfterWaitLoadApproal;
	}
	/**
	 * @return the contractTemplateId
	 */
	public String getContractTemplateId() {
		return contractTemplateId;
	}
	/**
	 * @param contractTemplateId the contractTemplateId to set
	 */
	public void setContractTemplateId(String contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
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
	 * @return the aipFlag
	 */
	public String getAipFlag() {
		return aipFlag;
	}
	/**
	 * @param aipFlag the aipFlag to set
	 */
	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}
	/**
	 * @return the autoSubsFlag
	 */
	public String getAutoSubsFlag() {
		return autoSubsFlag;
	}
	/**
	 * @param autoSubsFlag the autoSubsFlag to set
	 */
	public void setAutoSubsFlag(String autoSubsFlag) {
		this.autoSubsFlag = autoSubsFlag;
	}
	/**
	 * @return the supplyUserCount
	 */
	public String getSupplyUserCount() {
		return supplyUserCount;
	}
	/**
	 * @param supplyUserCount the supplyUserCount to set
	 */
	public void setSupplyUserCount(String supplyUserCount) {
		this.supplyUserCount = supplyUserCount;
	}
	/**
	 * @return the subsPercent
	 */
	public String getSubsPercent() {
		return subsPercent;
	}
	/**
	 * @param subsPercent the subsPercent to set
	 */
	public void setSubsPercent(String subsPercent) {
		this.subsPercent = subsPercent;
	}
	/**
	 * @return the supplyAmount
	 */
	public String getSupplyAmount() {
		return supplyAmount;
	}
	/**
	 * @param supplyAmount the supplyAmount to set
	 */
	public void setSupplyAmount(String supplyAmount) {
		this.supplyAmount = supplyAmount;
	}
	/**
	 * @return the supplyStartTime
	 */
	public String getSupplyStartTime() {
		return supplyStartTime;
	}
	/**
	 * @param supplyStartTime the supplyStartTime to set
	 */
	public void setSupplyStartTime(String supplyStartTime) {
		this.supplyStartTime = supplyStartTime;
	}
	/**
	 * @return the supplyEndTime
	 */
	public String getSupplyEndTime() {
		return supplyEndTime;
	}
	/**
	 * @param supplyEndTime the supplyEndTime to set
	 */
	public void setSupplyEndTime(String supplyEndTime) {
		this.supplyEndTime = supplyEndTime;
	}
}
