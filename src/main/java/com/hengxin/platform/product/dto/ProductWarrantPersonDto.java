/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;

/**
 * Class Name: ProductWarrantPersonDto Description: 融资产品担保信息自然人
 * 
 * @author Ryan
 * 
 */
public class ProductWarrantPersonDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 0, max = 25, message = "{product.error.owner.length}", groups = { SubmitProductApply.class })
    private String name;// 自然人姓名

    @Pattern(regexp = ApplicationConstant.ID_CARD_REGEXP, groups = { SubmitProductApply.class }, message = "{member.error.idcard.invaild}")
    private String idNo;// 自然人身份证号
    private String job;// 自然人职业

    @Length(min = 0, max = 200, message = "{product.error.notes.length}", groups = { SubmitProductApply.class })
    private String notes;// 自然人备注

    private String createOpid;

    private String lastMntOpid;

    private DynamicOption Djob;

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

    private Date createTs;

    private Date lastMntTs;

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
     * @return return the value of the var idNo
     */

    public String getIdNo() {
        return idNo;
    }

    /**
     * @param idNo
     *            Set idNo value
     */

    public void setIdNo(String idNo) {
        this.idNo = idNo;
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
     * @return return the value of the var job
     */

    public String getJob() {
        return job;
    }

    /**
     * @param job
     *            Set job value
     */

    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return return the value of the var djob
     */
    
    public DynamicOption getDjob() {
        return Djob;
    }

    /**
     * @param djob Set djob value
     */
    
    public void setDjob(DynamicOption djob) {
        Djob = djob;
    }

}
