package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.id.CompositeIdInjectionEntityListener;

@Entity
@Table(name = "FP_CONTRACT_RATE")
@IdClass(ContractRatePK.class)
@EntityListeners(CompositeIdInjectionEntityListener.class)
public class ContractRate extends BaseInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONTRACT_ID", insertable = true, updatable = false)
	private String contractId;

	@Id
	@Column(name = "LEVEL_ID", insertable = true, updatable = false)
	private String productLevelId;

	@Column(name = "FNCR_PREPAY_PENALTY_RT")
	private BigDecimal financierPrepaymentPenaltyRate; // 融资人提前还款违约金率

	@Column(name = "PLTFRM_PREPAY_PENALTY_RT")
	private BigDecimal platformPrepaymentPenaltyRate; // 平台提前还款违约金率

	@Column(name = "PAY_PENALTY_FINE_RT")
	private BigDecimal paymentPenaltyFineRate; // 还款违约时计算罚金的违约金率（滞纳金）

	@Column(name = "PREPAY_DEDUCT_INTR_FLG")
	private String prepayDeductIntrFlg; // 提前还款是否扣除当月利息,Y/N

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "CONTRACT_ID", insertable = false, updatable = false)
	private ProductContractTemplate contract;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public ProductContractTemplate getContract() {
		return contract;
	}

	public void setContract(ProductContractTemplate contract) {
		this.contract = contract;
	}

	public String getProductLevelId() {
		return productLevelId;
	}

	public void setProductLevelId(String productLevelId) {
		this.productLevelId = productLevelId;
	}

	public BigDecimal getFinancierPrepaymentPenaltyRate() {
		return financierPrepaymentPenaltyRate;
	}

	public void setFinancierPrepaymentPenaltyRate(BigDecimal financierPrepaymentPenaltyRate) {
		this.financierPrepaymentPenaltyRate = financierPrepaymentPenaltyRate;
	}

	public BigDecimal getPlatformPrepaymentPenaltyRate() {
		return platformPrepaymentPenaltyRate;
	}

	public void setPlatformPrepaymentPenaltyRate(BigDecimal platformPrepaymentPenaltyRate) {
		this.platformPrepaymentPenaltyRate = platformPrepaymentPenaltyRate;
	}

	public BigDecimal getPaymentPenaltyFineRate() {
		return paymentPenaltyFineRate;
	}

	public void setPaymentPenaltyFineRate(BigDecimal paymentPenaltyFineRate) {
		this.paymentPenaltyFineRate = paymentPenaltyFineRate;
	}

	public String getPrepayDeductIntrFlg() {
		return prepayDeductIntrFlg;
	}

	public void setPrepayDeductIntrFlg(String prepayDeductIntrFlg) {
		this.prepayDeductIntrFlg = prepayDeductIntrFlg;
	}

}