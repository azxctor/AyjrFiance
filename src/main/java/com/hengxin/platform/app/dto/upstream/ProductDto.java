package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * ProductDto.
 *
 */
public class ProductDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "{member.error.field.empty}")
	private String productId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	

}
