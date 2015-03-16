/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;

import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.member.enums.EUserType;

/**
 * Class Name: ProductUserDto
 * 
 * @author Ryan
 * 
 */
public class ProductUserDto implements Serializable {

    // private String username; //userId

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EUserType type = EUserType.NULL;

    private String name;

    private String maskChineseName;

    private ProductSeatFeeDto seatFee;

    /**
     * @return return the value of the var type
     */

    public EUserType getType() {
        return type;
    }

    /**
     * @param type
     *            Set type value
     */

    public void setType(EUserType type) {
        this.type = type;
    }

    /**
     * @return return the value of the var name
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     *            Set name value
     */

    public void setName(String name) {
        this.name = name;
    }

    public String getMaskChineseName() {
        return maskChineseName;
    }

    public void setMaskChineseName(String maskChineseName) {
        this.maskChineseName = maskChineseName;
    }

    public String getMaskName() {
        return MaskUtil.maskChinsesName(name);
    }

    /**
     * @return return the value of the var seatFee
     */

    public ProductSeatFeeDto getSeatFee() {
        return seatFee;
    }

    /**
     * @param seatFee
     *            Set seatFee value
     */

    public void setSeatFee(ProductSeatFeeDto seatFee) {
        this.seatFee = seatFee;
    }

}
