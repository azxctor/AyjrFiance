package com.hengxin.platform.product.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductPk implements Serializable {

    private String productId;

    private int seqId;

    /**
     * @return return the value of the var productId
     */

    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            Set productId value
     */

    public void setProductId(String productId) {
        this.productId = productId;
    }

    
    /**
    * @return return the value of the var seqId
    */
    	
    public int getSeqId() {
        return seqId;
    }

    
    /**
    * @param seqId Set seqId value
    */
    	
    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

}
