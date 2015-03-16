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
package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

import com.hengxin.platform.product.enums.ERiskLevel;

/**
 * MyFinancierPackagesListDownDto：我的债务融资包管理列表下行DTO.
 * 
 * @author yicai
 *
 */
public class MyFinancierPackagesDetailDownDto implements Serializable {

	private static final long serialVersionUID = 5L;

	/* 融资包信息 */
	private String id; // 融资包编号
	private String packageName; // 融资包名称
	private String rate; // 年利率
	private String duration; // 融资期限
	private String signContractTime; // 签约时间
	private String packageQuota; // 融资额（元）
	private String supplyAmount; // 申购金额（元）
	private String overdue2CmpnsDays; // 逾期转代偿期限
	/* 基本详情 */
	private String financierName; // 融资人名字
	private String accountNo; // 交易账号
	private String wrtrName; // 担保机构 (无"/")
	private String payMethod; // 还款方式
	private String loanPurpose; // 用途
	private String guaranteeLicenseNoImg; // 营业执照中间量
    private String guaranteeLicenseNoImgUrl; // 营业执照
	private String warrantyType; // 风险保障
	private ERiskLevel productLevel; // 融资项目级别
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSignContractTime() {
		return signContractTime;
	}
	public void setSignContractTime(String signContractTime) {
		this.signContractTime = signContractTime;
	}
	public String getPackageQuota() {
		return packageQuota;
	}
	public void setPackageQuota(String packageQuota) {
		this.packageQuota = packageQuota;
	}
    public String getSupplyAmount() {
		return supplyAmount;
	}
	public void setSupplyAmount(String supplyAmount) {
		this.supplyAmount = supplyAmount;
	}
	public String getOverdue2CmpnsDays() {
		return overdue2CmpnsDays;
	}
	public void setOverdue2CmpnsDays(String overdue2CmpnsDays) {
		this.overdue2CmpnsDays = overdue2CmpnsDays;
	}
	public String getFinancierName() {
		return financierName;
	}
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getWrtrName() {
		return wrtrName;
	}
	public void setWrtrName(String wrtrName) {
		this.wrtrName = wrtrName;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getLoanPurpose() {
		return loanPurpose;
	}
	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}
	public String getGuaranteeLicenseNoImg() {
		return guaranteeLicenseNoImg;
	}
	public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
		this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
	}
	public String getGuaranteeLicenseNoImgUrl() {
		return guaranteeLicenseNoImgUrl;
	}
	public void setGuaranteeLicenseNoImgUrl(String guaranteeLicenseNoImgUrl) {
		this.guaranteeLicenseNoImgUrl = guaranteeLicenseNoImgUrl;
	}
	public ERiskLevel getProductLevel() {
		return productLevel;
	}
	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}
	public String getWarrantyType() {
		return warrantyType;
	}
	public void setWarrantyType(String warrantyType) {
		this.warrantyType = warrantyType;
	}
}
