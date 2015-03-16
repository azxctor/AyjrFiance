package com.hengxin.platform.product.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class ContractRatePKDto extends DataTablesRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String contractId;

	private String productLevelId;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getProductLevelId() {
		return productLevelId;
	}

	public void setProductLevelId(String productLevelId) {
		this.productLevelId = productLevelId;
	}

}
