package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.BaseMaintainablePo;

@Entity
@Table(name = "UM_INVSTR_RULE")
public class UserInvestRule extends BaseMaintainablePo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INVSTR_LVL")
	private String investorLevel;

	@Column(name = "MIN_SUBS_AMT")
	private BigDecimal minSubscribeAmount;

	@Column(name = "MAX_SUBS_RT")
	private BigDecimal maxSubscribeRate;

	@Version
	@Column(name = "VERSION_CT")
	private Long versionCount;

	public String getInvestorLevel() {
		return investorLevel;
	}

	public void setInvestorLevel(String investorLevel) {
		this.investorLevel = investorLevel;
	}

	public BigDecimal getMinSubscribeAmount() {
		return minSubscribeAmount;
	}

	public void setMinSubscribeAmount(BigDecimal minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}

	public BigDecimal getMaxSubscribeRate() {
		return maxSubscribeRate;
	}

	public void setMaxSubscribeRate(BigDecimal maxSubscribeRate) {
		this.maxSubscribeRate = maxSubscribeRate;
	}

	public Long getVersionCount() {
		return versionCount;
	}

	public void setVersionCount(Long versionCount) {
		this.versionCount = versionCount;
	}

}
