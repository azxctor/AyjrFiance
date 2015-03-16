package com.hengxin.platform.product.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MortgageResidentialPK implements Serializable{

	private String productId;

	private int sequenceId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

}
