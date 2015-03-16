/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;

/**
 * Class Name: ProductSeatFeeDto Description: 
 * 
 * @author Ryan
 * 
 */
public class ProductSeatFeeDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String endDt;

    /**
     * @return return the value of the var endDt
     */
    
    public String getEndDt() {
        return endDt;
    }

    /**
     * @param endDt Set endDt value
     */
    
    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }
    
}
