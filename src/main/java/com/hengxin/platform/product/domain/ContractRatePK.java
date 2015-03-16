package com.hengxin.platform.product.domain;

import java.io.Serializable;

public class ContractRatePK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String contractId;

	private String productLevelId;

	public ContractRatePK() {

	};

	public ContractRatePK(String contractId, String productLevelId) {
		this.contractId = contractId;
		this.productLevelId = productLevelId;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = PRIME * result + ((productLevelId == null) ? 0 : productLevelId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final ContractRatePK other = (ContractRatePK) obj;
		if (contractId == null) {
			if (other.contractId != null) {
				return false;
			}
		} else if (!contractId.equals(other.contractId)) {
			return false;
		}
		if (productLevelId == null) {
			if (other.productLevelId != null) {
				return false;
			}
		} else if (!productLevelId.equals(other.productLevelId)) {
			return false;
		}
		return true;
	}

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
