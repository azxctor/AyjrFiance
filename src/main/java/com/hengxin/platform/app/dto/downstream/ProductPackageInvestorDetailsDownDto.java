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
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.product.enums.ERiskLevel;

/**
 * ProductPackageInvestorDetailsDownDto: 债权融资包详情.
 *
 * @author yicai
 * 
 */
public class ProductPackageInvestorDetailsDownDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* 头部详情 */
	private String lastAmount; // 剩余本息合计
	private String nextPayDay; // 下期收益日 
	private String nextPayAmount; // 下期预期收益额（元）
	
	private String packageName; // 融资包名称
	private String limitTime; // 融资期限
	private String annualProfit; // 年利率
	
	/* 基本详情 */
	private String financedAmount; // 融资额（元）
	private String riskSafeguard; // 风险保障：本息担保、本金担保、无担保、资产监管
	private String financier; // 融资人
	private String industry; // 行业
	private String guaranteeInstitution; // 担保机构
	private String productGrageClass; // 产品评分
	private String financierGrageClass; // 融资人评分
	private String repaymentWay; // 还款方式
	private String financePurpose; // 借款用途
	private String guaranteeLicenseNoImg; //
	private String guaranteeLicenseNoImgUrl; // 担保机构营业执照
	private String warrantGrageClass; // 担保人（担保机构）评分
	// 新增雷达图三个参数
	private ERiskLevel productLevel; // 融资项目级别
	private String financierLevel; // 融资会员信用积分
	private String warrantorLevel; // 担保机构信用积分
	
	/* 反担保详情 */
	/* 反担保详情-抵押产品 */
	private List<ProductMortgageResidentialDetailsDownDto> buildingList; // 房产抵押
	private List<ProductMortgageVehicleDetailsDownDto> carList; // 车辆抵押
	/* 反担保详情-质押产品 */
	private List<ProductPledgeDetailsDownDto> chattelMortgageList; // 动产质押
	/* 反担保详情-保证担保 */
	private List<ProductWarrantPersonDownDto> warrantPersonList; // 自然人列表
	private List<ProductWarrantEnterpriseDownDto> warrantEnterpriseList;// 法人列表

	/* 资产及负债 */
	private List<ProductAssetDownDto> personalAssetList; // 个人资产情况列表
	private List<ProductDebtDownDto> personalLiabilityList; // 个人负债情况列表
	
	public String getGuaranteeLicenseNoImgUrl() {
        if (StringUtils.isNotBlank(guaranteeLicenseNoImg)) {
            return FileUploadUtil.getTempFolder() + guaranteeLicenseNoImg;
        }
        return "";
    }
	
	public String getLastAmount() {
		return lastAmount;
	}
	public void setLastAmount(String lastAmount) {
		this.lastAmount = lastAmount;
	}
	public String getNextPayDay() {
		return nextPayDay;
	}
	public void setNextPayDay(String nextPayDay) {
		this.nextPayDay = nextPayDay;
	}
	public String getNextPayAmount() {
		return nextPayAmount;
	}
	public void setNextPayAmount(String nextPayAmount) {
		this.nextPayAmount = nextPayAmount;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(String limitTime) {
		this.limitTime = limitTime;
	}
	public String getAnnualProfit() {
		return annualProfit;
	}
	public void setAnnualProfit(String annualProfit) {
		this.annualProfit = annualProfit;
	}
	public String getFinancedAmount() {
		return financedAmount;
	}
	public void setFinancedAmount(String financedAmount) {
		this.financedAmount = financedAmount;
	}
	public String getRiskSafeguard() {
		return riskSafeguard;
	}
	public void setRiskSafeguard(String riskSafeguard) {
		this.riskSafeguard = riskSafeguard;
	}
	public String getFinancier() {
		return financier;
	}
	public void setFinancier(String financier) {
		this.financier = financier;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getGuaranteeInstitution() {
		return guaranteeInstitution;
	}
	public void setGuaranteeInstitution(String guaranteeInstitution) {
		this.guaranteeInstitution = guaranteeInstitution;
	}
	public String getProductGrageClass() {
		return productGrageClass;
	}
	public void setProductGrageClass(String productGrageClass) {
		this.productGrageClass = productGrageClass;
	}
	public String getFinancierGrageClass() {
		return financierGrageClass;
	}
	public void setFinancierGrageClass(String financierGrageClass) {
		this.financierGrageClass = financierGrageClass;
	}
	public String getRepaymentWay() {
		return repaymentWay;
	}
	public void setRepaymentWay(String repaymentWay) {
		this.repaymentWay = repaymentWay;
	}
	public String getFinancePurpose() {
		return financePurpose;
	}
	public void setFinancePurpose(String financePurpose) {
		this.financePurpose = financePurpose;
	}
	public String getGuaranteeLicenseNoImg() {
		return guaranteeLicenseNoImg;
	}
	public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
		this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
	}
	public String getWarrantGrageClass() {
		return warrantGrageClass;
	}
	public void setWarrantGrageClass(String warrantGrageClass) {
		this.warrantGrageClass = warrantGrageClass;
	}
	public List<ProductMortgageResidentialDetailsDownDto> getBuildingList() {
		return buildingList;
	}
	public void setBuildingList(
			List<ProductMortgageResidentialDetailsDownDto> buildingList) {
		this.buildingList = buildingList;
	}
	public List<ProductMortgageVehicleDetailsDownDto> getCarList() {
		return carList;
	}
	public void setCarList(List<ProductMortgageVehicleDetailsDownDto> carList) {
		this.carList = carList;
	}
	public List<ProductPledgeDetailsDownDto> getChattelMortgageList() {
		return chattelMortgageList;
	}
	public void setChattelMortgageList(
			List<ProductPledgeDetailsDownDto> chattelMortgageList) {
		this.chattelMortgageList = chattelMortgageList;
	}
	public List<ProductWarrantPersonDownDto> getWarrantPersonList() {
		return warrantPersonList;
	}
	public void setWarrantPersonList(
			List<ProductWarrantPersonDownDto> warrantPersonList) {
		this.warrantPersonList = warrantPersonList;
	}
	public List<ProductWarrantEnterpriseDownDto> getWarrantEnterpriseList() {
		return warrantEnterpriseList;
	}
	public void setWarrantEnterpriseList(
			List<ProductWarrantEnterpriseDownDto> warrantEnterpriseList) {
		this.warrantEnterpriseList = warrantEnterpriseList;
	}
	public List<ProductAssetDownDto> getPersonalAssetList() {
		return personalAssetList;
	}
	public void setPersonalAssetList(List<ProductAssetDownDto> personalAssetList) {
		this.personalAssetList = personalAssetList;
	}
	public List<ProductDebtDownDto> getPersonalLiabilityList() {
		return personalLiabilityList;
	}
	public void setPersonalLiabilityList(
			List<ProductDebtDownDto> personalLiabilityList) {
		this.personalLiabilityList = personalLiabilityList;
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
	public String getFinancierLevel() {
		return financierLevel;
	}
	public void setFinancierLevel(String financierLevel) {
		this.financierLevel = financierLevel;
	}
	public String getWarrantorLevel() {
		return warrantorLevel;
	}
	public void setWarrantorLevel(String warrantorLevel) {
		this.warrantorLevel = warrantorLevel;
	}
	
	/* 内部类 */
	/**
	 * 房产抵押.
	 */
	public class ProductMortgageResidentialDetailsDownDto implements Serializable {
		private String area; // 建筑面积（平方）
		private String purchasePrice; // 购买价格（万元）
		private String evaluatePrice; // 市场评估值（万元）
		private String marketPrice; // 市场价格（万元）
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getPurchasePrice() {
			return purchasePrice;
		}
		public void setPurchasePrice(String purchasePrice) {
			this.purchasePrice = purchasePrice;
		}
		public String getEvaluatePrice() {
			return evaluatePrice;
		}
		public void setEvaluatePrice(String evaluatePrice) {
			this.evaluatePrice = evaluatePrice;
		}
		public String getMarketPrice() {
			return marketPrice;
		}
		public void setMarketPrice(String marketPrice) {
			this.marketPrice = marketPrice;
		}
	}

	/**
	 * 车辆抵押.
	 */
	public class ProductMortgageVehicleDetailsDownDto implements Serializable {
		private String brand; // 车辆品牌
		private String type; // 车辆型号
		
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}

	/**
	 * 动产质押.
	 */
	public class ProductPledgeDetailsDownDto implements Serializable {
		private String name; //质押物名称
		private String pledgeClass; //品种
		private String type; //型号
		private String count; //数量
		private String price; //价格（万元）
		private String location; //存放地
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPledgeClass() {
			return pledgeClass;
		}
		public void setPledgeClass(String pledgeClass) {
			this.pledgeClass = pledgeClass;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
	}

	/**
	 * 自然人.
	 */
	public class ProductWarrantPersonDownDto implements Serializable {
		private String warrantPersonStr;

		public String getWarrantPersonStr() {
			return warrantPersonStr;
		}
		public void setWarrantPersonStr(String warrantPersonStr) {
			this.warrantPersonStr = warrantPersonStr;
		}
	}

	/**
	 *  法人. 
	 */
	public class ProductWarrantEnterpriseDownDto implements Serializable {
		private String warrantEnterpriseStr;

		public String getWarrantEnterpriseStr() {
			return warrantEnterpriseStr;
		}
		public void setWarrantEnterpriseStr(String warrantEnterpriseStr) {
			this.warrantEnterpriseStr = warrantEnterpriseStr;
		}
	}

	/**
	 *  负债. 
	 */
	public class ProductDebtDownDto implements Serializable {
		private String type; // 类型
		private String debtAmount; // 负债额
		private String monthlyPayment; // 每月还款额
		private String notes; // 说明
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDebtAmount() {
			return debtAmount;
		}
		public void setDebtAmount(String debtAmount) {
			this.debtAmount = debtAmount;
		}
		public String getMonthlyPayment() {
			return monthlyPayment;
		}
		public void setMonthlyPayment(String monthlyPayment) {
			this.monthlyPayment = monthlyPayment;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
	}

	/**
	 *  资产. 
	 */
	public class ProductAssetDownDto implements Serializable {
		String type; // 类型
		BigDecimal assertAmount; // 资产总额
		String notes; // 说明
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public BigDecimal getAssertAmount() {
			return assertAmount;
		}
		public void setAssertAmount(BigDecimal assertAmount) {
			this.assertAmount = assertAmount;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
	}
}