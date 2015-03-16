/*
 * Project Name: kmfex-platform
 * File Name: FinancingManageProductDetailsDownDto.java
 * Class Name: FinancingManageProductDetailsDownDto
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

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 融资项目详情下行DTO.
 * 
 * @author yicai
 *
 */
public class FinancingManageProductDetailsDownDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* 基本信息 */
	private String productId; // 项目编号
	private String productName; // 项目简称
	private String appliedQuota;// 融资申请额
	private String rate; // 年利率
	// <span id="funding-duration">#if($!{productDetails.termType.code} == "D")
	// $!{productDetails.termToDays} #else $!{productDetails.termLength}
	// #end</span><span
	// id="funding-duration-type">$!{productDetails.termType.text}</span>
	private String term; // 融资期限
	private ETermType termType; // 
	private String termToDays; // 
	private String termLength; // 
	private EPayMethodType payMethod; // 还款方式
	private String productLib; // 产品库
	// <span>#if($!{productDetails.guaranteeInstitution})
	// $!{productDetails.guaranteeInstitutionShow} #else $!defaultValue
	// #end</span>
	private Boolean canNotSeeGuarantors; // 是否看到担保机构的权限
	private String guaranteeInstitution; // 
	private String guaranteeInstitutionShow; // 担保机构
	private String contractTemplateName; // 合同模板
	private String createTs; // 申请日期
	private String acctNo; // 交易账号
	private String financierName; // 融资人
	private String financierMobile; // 手机号码
	private DynamicOption dfinancierIndustryType; // 行业
	private EWarrantyType warrantyType;// 风险保障
	private String loanPurpose; // 用途
	private String wrtrCreditDesc; // 担保公司简介
	
	private Boolean canNotSeeOverdue2CmpnsGracePeriod; // 
	private String overdue2CmpnsGracePeriod; // 逾期转代偿期限(天)
	private Boolean canSeeGrages; // 能否看到以下四个字段，融资项目级别、融资会员信用积分、担保机构信用积分、担保机构信用披露
	private String productGrageClass; // 融资项目级别
	private String financierGrageClass; // 融资会员信用积分
	private String warrantGrageClass; // 担保机构信用积分
	private String wrtrCreditFile; // 担保机构信用披露
	
	private Boolean canSeeDepositFee; // 能否看到，融资服务合同保证金、担保公司还款保证金
	private String cashDepositFinanceServiceFee; // 融资服务合同保证金（元）, cashDeposit.financeServiceFee
	private String cashDepositWarrantPercentage; // 担保公司还款保证金（元）, cashDeposit.warrantPercentage
	private Boolean canSeeFee; // 能否看到，借款合同履约保证金、融资服务费、风险管理费
	private String cashDepositContractFeePer; // 借款合同履约保证金（元）, cashDeposit.contractFeePer
	private String cashDepositServiceFee; // 融资服务费（元）, cashDeposit.serviceFee
	private String cashDepositRiskFee; // 风险管理费（元）, cashDeposit.riskFee
	private List<InnerProductPackageDto> productPackageDtoList; // 融资包列表
	
	
	
	
	
	/* 更多详情 */
	
	/* 保证金及费用参考 */
	private String financeFee; // 融资服务合同保证金
	private String warrantFee; // 担保公司还款保证金
	private String contractFee; // 借款合同履约保证金 
	private String serviceFee; // 融资服务费
	private String riskFee; // 风险管理费
	// <span>#if($!{productDetails.feeDto.endDt})到期日：$!{productDetails.feeDto.endDt} #elseif($!{productDetails.financierSeatFee})￥$!number.formatMoney($!{productDetails.financierSeatFee})元 #else $!defaultValue #end</span>
	private String seatFee; // 融资会员席位费
	
	/* 反担保情况 */
	/* 抵押产品  */
	private List<InnerProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList; // 房屋列表
	private List<InnerProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList; // 车辆列表
	private String otherMortgage;// 其他抵押
	/* 质押产品  */
    private List<InnerProductPledgeDetailsDto> productPledgeDetailsDtoList; // 动产质押列表
    private String otherPledge;// 其他质押
    /* 保证担保 */
    private List<InnerProductWarrantPersonDto> productWarrantPersonDtoList; // 自然人列表
    private List<InnerProductWarrantEnterpriseDto> productWarrantEnterpriseDtoList; // 法人列表
    
	/* 资产及负债 */
    private List<InnerProductAssetDto> productAssetDtoList;// 资产列表
    private List<InnerProductDebtDto> productDebtDtoList;// 负债列表
	/* 资料库 */
    private List<InnerProductAttachmentDetailsDto> productAttachmentDetailsDtoList;
	/* 备注 */
    private String notes; // 备注
    
    
    /* getter && setter */
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAppliedQuota() {
		return appliedQuota;
	}

	public void setAppliedQuota(String appliedQuota) {
		this.appliedQuota = appliedQuota;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public ETermType getTermType() {
		return termType;
	}

	public void setTermType(ETermType termType) {
		this.termType = termType;
	}

	public String getTermToDays() {
		return termToDays;
	}

	public void setTermToDays(String termToDays) {
		this.termToDays = termToDays;
	}

	public String getTermLength() {
		return termLength;
	}

	public void setTermLength(String termLength) {
		this.termLength = termLength;
	}

	public EPayMethodType getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(EPayMethodType payMethod) {
		this.payMethod = payMethod;
	}

	public String getProductLib() {
		return productLib;
	}

	public void setProductLib(String productLib) {
		this.productLib = productLib;
	}

	public String getGuaranteeInstitution() {
		return guaranteeInstitution;
	}

	public void setGuaranteeInstitution(String guaranteeInstitution) {
		this.guaranteeInstitution = guaranteeInstitution;
	}

	public String getGuaranteeInstitutionShow() {
		return guaranteeInstitutionShow;
	}

	public void setGuaranteeInstitutionShow(String guaranteeInstitutionShow) {
		this.guaranteeInstitutionShow = guaranteeInstitutionShow;
	}

	public String getContractTemplateName() {
		return contractTemplateName;
	}

	public void setContractTemplateName(String contractTemplateName) {
		this.contractTemplateName = contractTemplateName;
	}

	public Boolean getCanSeeGrages() {
		return canSeeGrages;
	}

	public void setCanSeeGrages(Boolean canSeeGrages) {
		this.canSeeGrages = canSeeGrages;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getFinancierName() {
		return financierName;
	}

	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}

	public Boolean getCanNotSeeGuarantors() {
		return canNotSeeGuarantors;
	}

	public void setCanNotSeeGuarantors(Boolean canNotSeeGuarantors) {
		this.canNotSeeGuarantors = canNotSeeGuarantors;
	}

	public Boolean getCanNotSeeOverdue2CmpnsGracePeriod() {
		return canNotSeeOverdue2CmpnsGracePeriod;
	}

	public void setCanNotSeeOverdue2CmpnsGracePeriod(
			Boolean canNotSeeOverdue2CmpnsGracePeriod) {
		this.canNotSeeOverdue2CmpnsGracePeriod = canNotSeeOverdue2CmpnsGracePeriod;
	}

	public String getFinancierMobile() {
		return financierMobile;
	}

	public void setFinancierMobile(String financierMobile) {
		this.financierMobile = financierMobile;
	}

	public DynamicOption getDfinancierIndustryType() {
		return dfinancierIndustryType;
	}

	public void setDfinancierIndustryType(DynamicOption dfinancierIndustryType) {
		this.dfinancierIndustryType = dfinancierIndustryType;
	}

	public EWarrantyType getWarrantyType() {
		return warrantyType;
	}

	public void setWarrantyType(EWarrantyType warrantyType) {
		this.warrantyType = warrantyType;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getOverdue2CmpnsGracePeriod() {
		return overdue2CmpnsGracePeriod;
	}

	public String getWrtrCreditDesc() {
		return wrtrCreditDesc;
	}

	public void setWrtrCreditDesc(String wrtrCreditDesc) {
		this.wrtrCreditDesc = wrtrCreditDesc;
	}

	public void setOverdue2CmpnsGracePeriod(String overdue2CmpnsGracePeriod) {
		this.overdue2CmpnsGracePeriod = overdue2CmpnsGracePeriod;
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

	public String getWarrantGrageClass() {
		return warrantGrageClass;
	}

	public void setWarrantGrageClass(String warrantGrageClass) {
		this.warrantGrageClass = warrantGrageClass;
	}

	public String getWrtrCreditFile() {
		return wrtrCreditFile;
	}

	public void setWrtrCreditFile(String wrtrCreditFile) {
		this.wrtrCreditFile = wrtrCreditFile;
	}

	public Boolean getCanSeeDepositFee() {
		return canSeeDepositFee;
	}

	public void setCanSeeDepositFee(Boolean canSeeDepositFee) {
		this.canSeeDepositFee = canSeeDepositFee;
	}

	public Boolean getCanSeeFee() {
		return canSeeFee;
	}

	public void setCanSeeFee(Boolean canSeeFee) {
		this.canSeeFee = canSeeFee;
	}

	public String getCashDepositFinanceServiceFee() {
		return cashDepositFinanceServiceFee;
	}

	public void setCashDepositFinanceServiceFee(String cashDepositFinanceServiceFee) {
		this.cashDepositFinanceServiceFee = cashDepositFinanceServiceFee;
	}

	public String getCashDepositContractFeePer() {
		return cashDepositContractFeePer;
	}

	public void setCashDepositContractFeePer(String cashDepositContractFeePer) {
		this.cashDepositContractFeePer = cashDepositContractFeePer;
	}

	public String getCashDepositServiceFee() {
		return cashDepositServiceFee;
	}

	public void setCashDepositServiceFee(String cashDepositServiceFee) {
		this.cashDepositServiceFee = cashDepositServiceFee;
	}

	public String getCashDepositWarrantPercentage() {
		return cashDepositWarrantPercentage;
	}

	public void setCashDepositWarrantPercentage(String cashDepositWarrantPercentage) {
		this.cashDepositWarrantPercentage = cashDepositWarrantPercentage;
	}

	public String getCashDepositRiskFee() {
		return cashDepositRiskFee;
	}

	public void setCashDepositRiskFee(String cashDepositRiskFee) {
		this.cashDepositRiskFee = cashDepositRiskFee;
	}

	public List<InnerProductPackageDto> getProductPackageDtoList() {
		return productPackageDtoList;
	}

	public void setProductPackageDtoList(
			List<InnerProductPackageDto> productPackageDtoList) {
		this.productPackageDtoList = productPackageDtoList;
	}

	public String getFinanceFee() {
		return financeFee;
	}

	public void setFinanceFee(String financeFee) {
		this.financeFee = financeFee;
	}

	public String getWarrantFee() {
		return warrantFee;
	}

	public void setWarrantFee(String warrantFee) {
		this.warrantFee = warrantFee;
	}

	public String getContractFee() {
		return contractFee;
	}

	public void setContractFee(String contractFee) {
		this.contractFee = contractFee;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getRiskFee() {
		return riskFee;
	}

	public void setRiskFee(String riskFee) {
		this.riskFee = riskFee;
	}

	public String getSeatFee() {
		return seatFee;
	}

	public void setSeatFee(String seatFee) {
		this.seatFee = seatFee;
	}

	public List<InnerProductMortgageResidentialDetailsDto> getProductMortgageResidentialDetailsDtoList() {
		return productMortgageResidentialDetailsDtoList;
	}

	public void setProductMortgageResidentialDetailsDtoList(
			List<InnerProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList) {
		this.productMortgageResidentialDetailsDtoList = productMortgageResidentialDetailsDtoList;
	}

	public List<InnerProductMortgageVehicleDetailsDto> getProductMortgageVehicleDetailsDtoList() {
		return productMortgageVehicleDetailsDtoList;
	}

	public void setProductMortgageVehicleDetailsDtoList(
			List<InnerProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList) {
		this.productMortgageVehicleDetailsDtoList = productMortgageVehicleDetailsDtoList;
	}

	public String getOtherMortgage() {
		return otherMortgage;
	}

	public void setOtherMortgage(String otherMortgage) {
		this.otherMortgage = otherMortgage;
	}

	public List<InnerProductPledgeDetailsDto> getProductPledgeDetailsDtoList() {
		return productPledgeDetailsDtoList;
	}

	public void setProductPledgeDetailsDtoList(
			List<InnerProductPledgeDetailsDto> productPledgeDetailsDtoList) {
		this.productPledgeDetailsDtoList = productPledgeDetailsDtoList;
	}

	public String getOtherPledge() {
		return otherPledge;
	}

	public void setOtherPledge(String otherPledge) {
		this.otherPledge = otherPledge;
	}

	public List<InnerProductWarrantPersonDto> getProductWarrantPersonDtoList() {
		return productWarrantPersonDtoList;
	}

	public void setProductWarrantPersonDtoList(
			List<InnerProductWarrantPersonDto> productWarrantPersonDtoList) {
		this.productWarrantPersonDtoList = productWarrantPersonDtoList;
	}

	public List<InnerProductWarrantEnterpriseDto> getProductWarrantEnterpriseDtoList() {
		return productWarrantEnterpriseDtoList;
	}

	public void setProductWarrantEnterpriseDtoList(
			List<InnerProductWarrantEnterpriseDto> productWarrantEnterpriseDtoList) {
		this.productWarrantEnterpriseDtoList = productWarrantEnterpriseDtoList;
	}

	public List<InnerProductAssetDto> getProductAssetDtoList() {
		return productAssetDtoList;
	}

	public void setProductAssetDtoList(List<InnerProductAssetDto> productAssetDtoList) {
		this.productAssetDtoList = productAssetDtoList;
	}

	public List<InnerProductDebtDto> getProductDebtDtoList() {
		return productDebtDtoList;
	}

	public void setProductDebtDtoList(List<InnerProductDebtDto> productDebtDtoList) {
		this.productDebtDtoList = productDebtDtoList;
	}

	public List<InnerProductAttachmentDetailsDto> getProductAttachmentDetailsDtoList() {
		return productAttachmentDetailsDtoList;
	}

	public void setProductAttachmentDetailsDtoList(
			List<InnerProductAttachmentDetailsDto> productAttachmentDetailsDtoList) {
		this.productAttachmentDetailsDtoList = productAttachmentDetailsDtoList;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * 融资包.
	 *
	 */
	public class InnerProductPackageDto implements Serializable {
		private String id; // 融资包编号
		private String packageName; // 融资包简称
		
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
	}
	
	/**
	 * InnerProductMortgageResidentialDetailsDto.
	 *
	 */
	public class InnerProductMortgageResidentialDetailsDto implements Serializable {
		// <span>#if($!{house.DmortgageType.text}) $!{house.DmortgageType.text} #else $!defaultValue #end</span>
		private String mortgageType; // 抵押类型
		private String premisesPermitNo; // 房产证号码, 产权证号
		private String owner;// 房产拥有者, 房屋所有权人
		private String ownerType; // 共有情况
		private String coOwner; // 房产共有人, 共有人姓名
		private String coOwnerAge; // 共有人年龄
		private String location; // 房屋坐落
		private String area; // 建筑面积(平方)
		private String registDate; // 登记时间
		private String purchasePrice; // 购买价格(万元)
		private String evaluatePrice; // 市场评估价格(万元)
		private String marketPrice; // 市场价格(万元)
		
		public String getMortgageType() {
			return mortgageType;
		}
		public void setMortgageType(String mortgageType) {
			this.mortgageType = mortgageType;
		}
		public String getPremisesPermitNo() {
			return premisesPermitNo;
		}
		public void setPremisesPermitNo(String premisesPermitNo) {
			this.premisesPermitNo = premisesPermitNo;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getOwnerType() {
			return ownerType;
		}
		public void setOwnerType(String ownerType) {
			this.ownerType = ownerType;
		}
		public String getCoOwner() {
			return coOwner;
		}
		public void setCoOwner(String coOwner) {
			this.coOwner = coOwner;
		}
		public String getCoOwnerAge() {
			return coOwnerAge;
		}
		public void setCoOwnerAge(String coOwnerAge) {
			this.coOwnerAge = coOwnerAge;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getRegistDate() {
			return registDate;
		}
		public void setRegistDate(String registDate) {
			this.registDate = registDate;
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
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductMortgageVehicleDetailsDto implements Serializable {
		private String owner; // 机动车所有权人
		private String registNo; // 机动车编号登记
		private String registInstitution; // 登记机关
		private String type; // 车辆型号
		private String registDt; // 登记时间
		private String brand; // 车辆品牌
		
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getRegistNo() {
			return registNo;
		}
		public void setRegistNo(String registNo) {
			this.registNo = registNo;
		}
		public String getRegistInstitution() {
			return registInstitution;
		}
		public void setRegistInstitution(String registInstitution) {
			this.registInstitution = registInstitution;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getRegistDt() {
			return registDt;
		}
		public void setRegistDt(String registDt) {
			this.registDt = registDt;
		}
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
	}
	
	/**
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductPledgeDetailsDto implements Serializable {
		private String name; // 质押物名称
		private String price; // 价格(万元)
		private String pledgeClass; // 品种
		private String location; // 存放地
		private String type; // 型号
		private String notes; // 其他
		private String count; // 数量
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getPledgeClass() {
			return pledgeClass;
		}
		public void setPledgeClass(String pledgeClass) {
			this.pledgeClass = pledgeClass;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
	}
	
	/**
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductWarrantPersonDto implements Serializable {
		private String name; // 姓名
		private String idNo; // 身份证号码
		private String djob; // 职业
		private String notes; // 其他必要描述
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIdNo() {
			return idNo;
		}
		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}
		public String getDjob() {
			return djob;
		}
		public void setDjob(String djob) {
			this.djob = djob;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
	}
	
	/**
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductWarrantEnterpriseDto implements Serializable {
		private String enterpriseName; // 企业名称
		private String denterpriseIndustry; // 行业
		private String enterpriseLicenceNo; // 营业执照号码
		private String enterpriseNotes; // 其他必要描述
		
		public String getEnterpriseName() {
			return enterpriseName;
		}
		public void setEnterpriseName(String enterpriseName) {
			this.enterpriseName = enterpriseName;
		}
		public String getDenterpriseIndustry() {
			return denterpriseIndustry;
		}
		public void setDenterpriseIndustry(String denterpriseIndustry) {
			this.denterpriseIndustry = denterpriseIndustry;
		}
		public String getEnterpriseLicenceNo() {
			return enterpriseLicenceNo;
		}
		public void setEnterpriseLicenceNo(String enterpriseLicenceNo) {
			this.enterpriseLicenceNo = enterpriseLicenceNo;
		}
		public String getEnterpriseNotes() {
			return enterpriseNotes;
		}
		public void setEnterpriseNotes(String enterpriseNotes) {
			this.enterpriseNotes = enterpriseNotes;
		}
	}
	
	/**
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductDebtDto implements Serializable {
		private String dtype; // 类型
		private String monthlyPayment; // 每月还款额(元)
		private String debtAmount; // 负债额(万元)
		private String notes; // 说明
		
		public String getDtype() {
			return dtype;
		}
		public void setDtype(String dtype) {
			this.dtype = dtype;
		}
		public String getMonthlyPayment() {
			return monthlyPayment;
		}
		public void setMonthlyPayment(String monthlyPayment) {
			this.monthlyPayment = monthlyPayment;
		}
		public String getDebtAmount() {
			return debtAmount;
		}
		public void setDebtAmount(String debtAmount) {
			this.debtAmount = debtAmount;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
	}
	
	/**
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductAssetDto implements Serializable {
		private String dtype; // 类型
		private String assertAmount; // 资产额(万元)
		private String notes; // 说明
		
		public String getDtype() {
			return dtype;
		}
		public void setDtype(String dtype) {
			this.dtype = dtype;
		}
		public String getAssertAmount() {
			return assertAmount;
		}
		public void setAssertAmount(String assertAmount) {
			this.assertAmount = assertAmount;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
	}
	/**
	 * 
	 * @author yicai
	 *
	 */
	public class InnerProductAttachmentDetailsDto implements Serializable {
		private String file; // 文件名
//		#if($stringUtil.isPdf($!{files.path}))
//			<img class="img-show financeapply-upload-img" src="$link.contextPath/assets/img/pdf.jpg" data-pdfpath="$!{files.path}"/>
//		#else
//			<img class="img-show financeapply-upload-img" src="$!{files.path}"/>
//		#end
		private String path; // 预览
		
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
	}
}
