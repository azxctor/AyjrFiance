/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;

/**
 * Class Name: ProductPledgeDetailsDto Description: 反担保情况质押产品
 * 
 * @author Ryan
 * 
 */
public class ProductPledgeDetailsDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 0, max = 25, message = "{product.error.owner.length}", groups = { SubmitProductApply.class })
    private String name;// 质押物名称

    @Length(min = 0, max = 20, message = "{product.error.brand.length}", groups = { SubmitProductApply.class })
    private String pledgeClass;// 品种

    @Length(min = 0, max = 20, message = "{product.error.brand.length}", groups = { SubmitProductApply.class })
    private String type;// 型号

    @Length(min = 0, max = 100, message = "{product.error.location.length}", groups = { SubmitProductApply.class })
    private String location;// 存放地

    @Length(min = 0, max = 100, message = "{product.error.location.length}", groups = { SubmitProductApply.class })
    private String notes;// 其他

    private Long count;// 数量

    @Digits(fraction = 4, integer = 14, message = "{product.error.debtAmount.length}", groups = { SubmitProductApply.class })
    private BigDecimal price;// 价格
    
    private String createOpid;

    private String lastMntOpid;

    private Date createTs;

    private Date lastMntTs;


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
    
    public Date getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs Set createTs value
     */
    
    public void setCreateTs(Date createTs) {
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

    /**
     * @return return the value of the var pledgeClass
     */

    public String getPledgeClass() {
        return pledgeClass;
    }

    /**
     * @param pledgeClass
     *            Set pledgeClass value
     */

    public void setPledgeClass(String pledgeClass) {
        this.pledgeClass = pledgeClass;
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
     * @return return the value of the var notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     *            Set notes value
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return return the value of the var count
     */

    public Long getCount() {
        return count;
    }

    /**
     * @param count
     *            Set count value
     */

    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * @return return the value of the var price
     */

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     *            Set price value
     */

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
