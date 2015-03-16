/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;
import com.hengxin.platform.product.enums.EProductCommonCondition;
import com.hengxin.platform.product.validator.ProductMortgageRealEsateCertificateNoCheck;

/**
 * Class Name: ProductMortgageResidentialDetailsDto Description: 反担保情况抵押产品房产
 * 
 * @author Ryan
 * 
 */
public class ProductMortgageResidentialDetailsDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    private String mortgageType;// 抵押类型

    @Length(min = 0, max = 20, message = "{product.error.mortgagerealestateNo}", groups = { SubmitProductApply.class })
    @ProductMortgageRealEsateCertificateNoCheck(groups = { SubmitProductApply.class })
    private String premisesPermitNo;// 房产证号码

    @Length(min = 0, max = 25, message = "{product.error.owner.length}", groups = { SubmitProductApply.class })
    private String owner;// 房产拥有者

    @Length(min = 0, max = 25, message = "{product.error.owner.length}", groups = { SubmitProductApply.class })
    private String coOwner;// 房产共有人

    private EProductCommonCondition ownerType = EProductCommonCondition.NULL;// 拥有类型

    @Digits(fraction = 0, integer = 3, message = "{product.error.age}", groups = { SubmitProductApply.class })
    @Min(value = 1, message = "{product.error.age}", groups = { SubmitProductApply.class })
    @Max(value = 200, message = "{product.error.age}", groups = { SubmitProductApply.class })
    private Integer coOwnerAge;// 房产共有人年龄

    @Length(min = 0, max = 100, message = "{product.error.location.length}", groups = { SubmitProductApply.class })
    private String location;// 地址

    private String registDate;// 房产登记日期

    // @Digits(fraction = 2, integer = 8, message = "{product.error.area.length}", groups = { SubmitProductApply.class
    // })
    private BigDecimal area;// 房屋面积

    @Digits(fraction = 4, integer = 14, message = "{product.error.debtAmount.length}", groups = { SubmitProductApply.class })
    private BigDecimal purchasePrice;// 房屋购买价格
    @Digits(fraction = 4, integer = 14, message = "{product.error.debtAmount.length}", groups = { SubmitProductApply.class })
    private BigDecimal evaluatePrice;// 房屋评估价格
    @Digits(fraction = 4, integer = 14, message = "{product.error.debtAmount.length}", groups = { SubmitProductApply.class })
    private BigDecimal marketPrice;// 房屋市场价格

    private String createOpid;

    private String lastMntOpid;

    private String createTs;

    private Date lastMntTs;

    private DynamicOption DmortgageType;

    /**
     * @return return the value of the var createOpid
     */

    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid
     *            Set createOpid value
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
     * @param lastMntOpid
     *            Set lastMntOpid value
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
     * @param createTs
     *            Set createTs value
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
     * @param lastMntTs
     *            Set lastMntTs value
     */

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * @return return the value of the var premisesPermitNo
     */

    public String getPremisesPermitNo() {
        return premisesPermitNo;
    }

    /**
     * @param premisesPermitNo
     *            Set premisesPermitNo value
     */

    public void setPremisesPermitNo(String premisesPermitNo) {
        this.premisesPermitNo = premisesPermitNo;
    }

    /**
     * @return return the value of the var coOwner
     */

    public String getCoOwner() {
        return coOwner;
    }

    /**
     * @param coOwner
     *            Set coOwner value
     */

    public void setCoOwner(String coOwner) {
        this.coOwner = coOwner;
    }

    /**
     * @return return the value of the var coOwnerAge
     */

    public Integer getCoOwnerAge() {
        return coOwnerAge;
    }

    /**
     * @param coOwnerAge
     *            Set coOwnerAge value
     */

    public void setCoOwnerAge(Integer coOwnerAge) {
        this.coOwnerAge = coOwnerAge;
    }

    /**
     * @return return the value of the var registDate
     */

    public String getRegistDate() {
        return registDate;
    }

    /**
     * @param registDate
     *            Set registDate value
     */

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    /**
     * @return return the value of the var area
     */

    public BigDecimal getArea() {
        return area;
    }

    /**
     * @param area
     *            Set area value
     */

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    /**
     * @return return the value of the var purchasePrice
     */

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * @param purchasePrice
     *            Set purchasePrice value
     */

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * @return return the value of the var evaluatePrice
     */

    public BigDecimal getEvaluatePrice() {
        return evaluatePrice;
    }

    /**
     * @param evaluatePrice
     *            Set evaluatePrice value
     */

    public void setEvaluatePrice(BigDecimal evaluatePrice) {
        this.evaluatePrice = evaluatePrice;
    }

    /**
     * @return return the value of the var marketPrice
     */

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    /**
     * @param marketPrice
     *            Set marketPrice value
     */

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

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
     * @return return the value of the var location
     */

    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            Set location value
     */

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return return the value of the var mortgageType
     */

    public String getMortgageType() {
        return mortgageType;
    }

    /**
     * @param mortgageType
     *            Set mortgageType value
     */

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }

    /**
     * @return return the value of the var dmortgageType
     */

    public DynamicOption getDmortgageType() {
        return DmortgageType;
    }

    /**
     * @param dmortgageType
     *            Set dmortgageType value
     */

    public void setDmortgageType(DynamicOption dmortgageType) {
        DmortgageType = dmortgageType;
    }

    /**
     * @return return the value of the var ownerType
     */

    public EProductCommonCondition getOwnerType() {
        return ownerType;
    }

    /**
     * @param ownerType
     *            Set ownerType value
     */

    public void setOwnerType(EProductCommonCondition ownerType) {
        this.ownerType = ownerType;
    }

}
