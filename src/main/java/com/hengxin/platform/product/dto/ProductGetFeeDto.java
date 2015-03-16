package com.hengxin.platform.product.dto;

import java.io.Serializable;

public class ProductGetFeeDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appliedQuota;

    /**
     * @return return the value of the var appliedQuota
     */

    public String getAppliedQuota() {
        return appliedQuota;
    }

    /**
     * @param appliedQuota
     *            Set appliedQuota value
     */

    public void setAppliedQuota(String appliedQuota) {
        this.appliedQuota = appliedQuota;
    }

}
