/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 
 *
 */
public class ProductListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;// 项目编号

	private String productName;// 项目简称

	private String lastMntTs; // 最后编辑时间
	
	private String acctNo; // 交易账号 

    private String applUserId; // userId
   
    private String financierName; // 融资人姓名

	private String guaranteeInstitution;// 担保机构

	private String guaranteeInstitutionShow;// 担保机构

	private Date applDate;// 申请日期

	private String applyDate;// 申请日期

	private String apprDate;// 审核时间

	private String evaluateDate;// 评级时间

	private String freezeDate;// 冻结时间

	private String publishDate;// 发布时间

	private BigDecimal appliedQuota;// 融资申请额
	
	private String appliedQuotaUnit;// 融资申请额(万元)

	private EWarrantyType warrantyType;// 风险保障

	private String finaceTerm;// 融资期限

	private Integer termLength; // 期限长度

	private ETermType termType; // 期限类型 D-Day,M-Month,Y-Year';

	private EPayMethodType payMethod;// 还款方式

	private EProductStatus status;// 项目状态

	private boolean canCheck; // 可查看

	private boolean canModify; // 可修改

	private boolean canWithdraw; // 可撤单(担保机构，交易经理)

	private boolean canEvaluate; // 可评级

	private boolean canApprove; // 可审核

	private boolean canFreeze; // 可冻结

	private boolean canPublish; // 可发布

	private boolean canUpdate; // 发布之后的修改

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

	public String getGuaranteeInstitution() {
		return guaranteeInstitution;
	}

	public void setGuaranteeInstitution(String guaranteeInstitution) {
		this.guaranteeInstitution = guaranteeInstitution;
	}

	public Date getApplDate() {
		return applDate;
	}

	public void setApplDate(Date applDate) {
		this.applDate = applDate;

		if (this.applDate != null) {
			this.applyDate = DateUtils.formatDate(this.applDate, "yyyy-MM-dd");
		}
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public BigDecimal getAppliedQuota() {
		return appliedQuota;
	}

	public void setAppliedQuota(BigDecimal appliedQuota) {
		this.appliedQuota = appliedQuota;
	}

	public EWarrantyType getWarrantyType() {
		return warrantyType;
	}

	public void setWarrantyType(EWarrantyType warrantyType) {
		this.warrantyType = warrantyType;
	}

	public String getFinaceTerm() {
		return finaceTerm;
	}

	public Integer getTermLength() {
		return termLength;
	}

	public void setTermLength(Integer termLength) {
		this.termLength = termLength;
	}

	public ETermType getTermType() {
		return termType;
	}

	public void setTermType(ETermType termType) {
		this.termType = termType;
		this.generateTerm();
	}

	public EPayMethodType getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(EPayMethodType payMethod) {
		this.payMethod = payMethod;
	}

	public EProductStatus getStatus() {
		return status;
	}

	public void setStatus(EProductStatus status) {
		this.status = status;
	}

	public String getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(String lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public String getFinancierName() {
		return financierName;
	}

	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}

	public String getApprDate() {
		return apprDate;
	}

	public void setApprDate(String apprDate) {
		this.apprDate = apprDate;
	}

	public String getFreezeDate() {
		return freezeDate;
	}

	public void setFreezeDate(String freezeDate) {
		this.freezeDate = freezeDate;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isCanCheck() {
		return canCheck;
	}

	public void setCanCheck(boolean canCheck) {
		this.canCheck = canCheck;
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	public boolean isCanWithdraw() {
		return canWithdraw;
	}

	public void setCanWithdraw(boolean canWithdraw) {
		this.canWithdraw = canWithdraw;
	}

	public boolean isCanEvaluate() {
		return canEvaluate;
	}

	public void setCanEvaluate(boolean canEvaluate) {
		this.canEvaluate = canEvaluate;
	}

	public boolean isCanApprove() {
		return canApprove;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}

	public boolean isCanFreeze() {
		return canFreeze;
	}

	public void setCanFreeze(boolean canFreeze) {
		this.canFreeze = canFreeze;
	}

	public boolean isCanPublish() {
		return canPublish;
	}

	public void setCanPublish(boolean canPublish) {
		this.canPublish = canPublish;
	}

	public String getEvaluateDate() {
		return evaluateDate;
	}

	public void setEvaluateDate(String evaluateDate) {
		this.evaluateDate = evaluateDate;
	}

	public boolean isCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	public String getAppliedQuotaUnit() {
        return appliedQuotaUnit;
    }

    public void setAppliedQuotaUnit(String appliedQuotaUnit) {
        this.appliedQuotaUnit = appliedQuotaUnit;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getApplUserId() {
        return applUserId;
    }

    public void setApplUserId(String applUserId) {
        this.applUserId = applUserId;
    }

    private void generateTerm() {
		if (this.termType == ETermType.DAY) {
			this.finaceTerm = this.termLength + " 天";
		} else if (this.termType == ETermType.MONTH) {
			this.finaceTerm = this.termLength + " 个月";
		} else if (this.termType == ETermType.YEAR) {
			this.finaceTerm = this.termLength + " 年";
		}
	}

	public String getGuaranteeInstitutionShow() {
		return guaranteeInstitutionShow;
	}

	public void setGuaranteeInstitutionShow(String guaranteeInstitutionShow) {
		this.guaranteeInstitutionShow = guaranteeInstitutionShow;
	}

}
