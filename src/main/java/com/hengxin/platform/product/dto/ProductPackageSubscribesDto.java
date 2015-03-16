package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: ProductPackageSubscribesDto Description: TODO
 * 
 * @author junwei
 * 
 */

public class ProductPackageSubscribesDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    /**
     * 序号
     */
    private String number;
    /**
     * 申购时间
     */
    private String createTs;
    /**
     * 申购用户
     */
    private String userName;
    /**
     * 申购金额
     */
    private BigDecimal supplyAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCreateTs() {
        return createTs;
    }

    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
