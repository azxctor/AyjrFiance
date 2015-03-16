package com.hengxin.platform.product.domain;

import java.io.Serializable;

/**
 * Product Package表的主键
 * @author tiexiyu
 *
 */

@SuppressWarnings("serial")
public class ProcuctPackagePK implements Serializable{
		
	private String packageId;
	
	private String productId;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
