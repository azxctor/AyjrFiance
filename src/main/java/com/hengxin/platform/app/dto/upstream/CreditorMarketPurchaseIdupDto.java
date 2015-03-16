package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * CreditorMarketPurchaseIdupDto.
 *
 */
public class CreditorMarketPurchaseIdupDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "{member.error.field.empty}")
	private String id = ""; // 融资包编号

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}