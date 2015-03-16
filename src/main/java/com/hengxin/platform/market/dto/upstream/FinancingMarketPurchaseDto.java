package com.hengxin.platform.market.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.common.service.validator.SubscribeAmountCheck;
import com.hengxin.platform.market.dto.upstream.FinancingMarketPurchaseDto.PurchasePackage;

@SubscribeAmountCheck(groups = PurchasePackage.class, amount = "amount", pkgId = "id")
public class FinancingMarketPurchaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private BigDecimal amount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface PurchasePackage {

    }
}
