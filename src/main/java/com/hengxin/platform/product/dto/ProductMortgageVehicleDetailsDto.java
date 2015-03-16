/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;
import com.hengxin.platform.product.validator.ProductMortgageVehicleCertificateNoCheck;

/**
 * Class Name: ProductMortgageVehicleDetailsDto Description: 反担保情况抵押产品车辆
 * 
 * @author Ryan
 * 
 */
public class ProductMortgageVehicleDetailsDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 0, max = 25, message = "{product.error.owner.length}", groups = { SubmitProductApply.class })
    private String owner; // 车辆所有人

    @Length(min = 0, max = 12, message = "{product.error.mortgagevehicleNo}", groups = { SubmitProductApply.class })
    @ProductMortgageVehicleCertificateNoCheck(groups = { SubmitProductApply.class })
    private String registNo; // 车辆登记编号

    @Length(min = 0, max = 40, message = "{member.error.password.length}", groups = { SubmitProductApply.class })
    private String registInstitution; // 车辆登记机关

    private String registDt; // 车辆登记日期

    @Length(min = 0, max = 20, message = "{product.error.brand.length}", groups = { SubmitProductApply.class })
    private String brand; // 车辆品牌

    @Length(min = 0, max = 20, message = "{product.error.brand.length}", groups = { SubmitProductApply.class })
    private String type; // 车辆型号
    
    private String createOpid;

    private String lastMntOpid;

    private String createTs;

    private Date lastMntTs;


    /**
     * @return return the value of the var owner
     */

    public String getOwner() {
        return owner;
    }

    /**
     * @param owner
     *            Set owner value
     */

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return return the value of the var registNo
     */

    public String getRegistNo() {
        return registNo;
    }

    /**
     * @param registNo
     *            Set registNo value
     */

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    /**
     * @return return the value of the var registInstitution
     */

    public String getRegistInstitution() {
        return registInstitution;
    }

    /**
     * @param registInstitution
     *            Set registInstitution value
     */

    public void setRegistInstitution(String registInstitution) {
        this.registInstitution = registInstitution;
    }


    /**
     * @return return the value of the var registDt
     */
    
    public String getRegistDt() {
        return registDt;
    }

    /**
     * @param registDt Set registDt value
     */
    
    public void setRegistDt(String registDt) {
        this.registDt = registDt;
    }

    /**
     * @return return the value of the var brand
     */

    public String getBrand() {
        return brand;
    }

    /**
     * @param brand
     *            Set brand value
     */

    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return return the value of the var type
     */

    public String getType() {
        return type;
    }

    /**
     * @param type
     *            Set type value
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return return the value of the var createOpid
     */
    
    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid Set createOpid value
     */
    
    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    /**
     * @return return the value of the var lastMntOpid
     */
    
    public String getLastMntOpid() {
        return lastMntOpid;
    }

    /**
     * @param lastMntOpid Set lastMntOpid value
     */
    
    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }


    /**
     * @return return the value of the var createTs
     */
    
    public String getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs Set createTs value
     */
    
    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    /**
     * @return return the value of the var lastMntTs
     */
    
    public Date getLastMntTs() {
        return lastMntTs;
    }

    /**
     * @param lastMntTs Set lastMntTs value
     */
    
    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

}
