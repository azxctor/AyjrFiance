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

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;

/**
 * Class Name: ProductAssetVehicleDetailsDto Description: 资产车辆
 * 
 * @author Ryan
 * 
 */
public class ProductAssetDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    private String type; // 类型

    @Digits(fraction = 4, integer = 14, message = "{product.error.debtAmount.length}", groups = { SubmitProductApply.class })
    private BigDecimal assertAmount; // 资产总额

    @Length(min = 0, max = 200, message = "{product.error.notes.length}", groups = { SubmitProductApply.class })
    private String notes; // 说明

    private String createOpid;
    private String lastMntOpid;
    

    private Date createTs;

    private Date lastMntTs;

    @SuppressWarnings("unused")
	private DynamicOption dtype;

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

    public Date getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs
     *            Set createTs value
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
     * @param lastMntTs
     *            Set lastMntTs value
     */

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * @return return the value of the var assertAmount
     */

    public BigDecimal getAssertAmount() {
        return assertAmount;
    }

    /**
     * @param assertAmount
     *            Set assertAmount value
     */

    public void setAssertAmount(BigDecimal assertAmount) {
        this.assertAmount = assertAmount;
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
     * @return return the value of the var dtype
     */

    public DynamicOption getDtype() {
        return SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.ASSET_TYPE, type);
    }

    /**
     * @param dtype
     *            Set dtype value
     */

    public void setDtype(DynamicOption dtype) {
        this.dtype = dtype;
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

}
