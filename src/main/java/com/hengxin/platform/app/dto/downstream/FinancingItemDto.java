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
import java.util.List;

/**
 * FinancingItemDto.
 * 
 *
 */
public class FinancingItemDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String packageName; // 融资包名称
	private String state; // 债权的三种状态：申购中、待申购、已过期
	private String stateCode; // 状态的标识：("WS", "待申购"), ("S", "申购中"), ("WN", "已过期")
//	private String riskLevel;
	
	private String riskSafeguard; // 风险保障：本息担保、本金担保、无担保、资产监
	private String progress; // 申购进度
	private String annualProfit; // 预期年化收益率
	private String limitTime; // 融资期限，如：13天，1个月，6个月
	
	/* 详情中显示的时间是动态判断的，待申购的融资包需要显示开始时间，其他显示结束时间 */
	private String startTime; // 开始时间
	private String endTime; // 结束时间
	private String financedAmount; // 融资额（元）
	
	private String financier; // 融资人
	private String repaymentWay; // 还款方式
	private String industry; // 行业
	private String financePurpose; // 借款用途说明
	private String guaranteeInstitution; //担保机构
	private String wrtrCreditDesc; //公司简介
	
	private String guaranteeLicenseNoImg = "";
	private String guaranteeLicenseNoImgName;
	
	private List<BuildingDto> buildingList; // 抵押产品列表-房产抵押列表
	private List<CarDto> carList; // 抵押产品列表-车辆抵押列表
	private List<ChattelMortgageDto> chattelMortgageList; // 动产质押列表
	
	private WarrantorDto warrantorDto; // 保证担保
	private FicoScoreDto ficoScoreDto; // 评分
	private List<PersonalAssetDto> personalAssetList; // 人资产情况
	private List<PersonalLiabilityDto> personalLiabilityList; // 个人负债情况
	
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
//	public String getRiskLevel() {
//		return riskLevel;
//	}
//	public void setRiskLevel(String riskLevel) {
//		this.riskLevel = riskLevel;
//	}
	public String getRiskSafeguard() {
		return riskSafeguard;
	}
	public void setRiskSafeguard(String riskSafeguard) {
		this.riskSafeguard = riskSafeguard;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public String getAnnualProfit() {
		return annualProfit;
	}
	public void setAnnualProfit(String annualProfit) {
		this.annualProfit = annualProfit;
	}
	public String getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(String limitTime) {
		this.limitTime = limitTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFinancedAmount() {
		return financedAmount;
	}
	public void setFinancedAmount(String financedAmount) {
		this.financedAmount = financedAmount;
	}
	public String getFinancier() {
		return financier;
	}
	public void setFinancier(String financier) {
		this.financier = financier;
	}
	public String getRepaymentWay() {
		return repaymentWay;
	}
	public void setRepaymentWay(String repaymentWay) {
		this.repaymentWay = repaymentWay;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getFinancePurpose() {
		return financePurpose;
	}
	public void setFinancePurpose(String financePurpose) {
		this.financePurpose = financePurpose;
	}
	public String getGuaranteeInstitution() {
		return guaranteeInstitution;
	}
	public void setGuaranteeInstitution(String guaranteeInstitution) {
		this.guaranteeInstitution = guaranteeInstitution;
	}
	public List<BuildingDto> getBuildingList() {
		return buildingList;
	}
	public void setBuildingList(List<BuildingDto> buildingList) {
		this.buildingList = buildingList;
	}
	public List<CarDto> getCarList() {
		return carList;
	}
	public void setCarList(List<CarDto> carList) {
		this.carList = carList;
	}
	public List<ChattelMortgageDto> getChattelMortgageList() {
		return chattelMortgageList;
	}
	public void setChattelMortgageList(List<ChattelMortgageDto> chattelMortgageList) {
		this.chattelMortgageList = chattelMortgageList;
	}
	public WarrantorDto getWarrantorDto() {
		return warrantorDto;
	}
	public void setWarrantorDto(WarrantorDto warrantorDto) {
		this.warrantorDto = warrantorDto;
	}
	public FicoScoreDto getFicoScoreDto() {
		return ficoScoreDto;
	}
	public void setFicoScoreDto(FicoScoreDto ficoScoreDto) {
		this.ficoScoreDto = ficoScoreDto;
	}
	public List<PersonalAssetDto> getPersonalAssetList() {
		return personalAssetList;
	}
	public void setPersonalAssetList(List<PersonalAssetDto> personalAssetList) {
		this.personalAssetList = personalAssetList;
	}
	public List<PersonalLiabilityDto> getPersonalLiabilityList() {
		return personalLiabilityList;
	}
	public void setPersonalLiabilityList(
			List<PersonalLiabilityDto> personalLiabilityList) {
		this.personalLiabilityList = personalLiabilityList;
	}
	public String getGuaranteeLicenseNoImg() {
		return guaranteeLicenseNoImg;
	}
	public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
		this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
	}
	public String getGuaranteeLicenseNoImgName() {
		return guaranteeLicenseNoImgName;
	}
	public void setGuaranteeLicenseNoImgName(String guaranteeLicenseNoImgName) {
		this.guaranteeLicenseNoImgName = guaranteeLicenseNoImgName;
	}
	public String getWrtrCreditDesc() {
		return wrtrCreditDesc;
	}
	public void setWrtrCreditDesc(String wrtrCreditDesc) {
		this.wrtrCreditDesc = wrtrCreditDesc;
	}

}
