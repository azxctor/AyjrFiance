package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CreditorAssignmentMarketPurchaseUpDto.
 * 
 */
public class CreditorAssignmentMarketPurchaseUpDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private BigDecimal amount;

    private BigDecimal fee;

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
     * @return return the value of the var fee
     */

    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @param fee
     *            Set fee value
     */

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

}
