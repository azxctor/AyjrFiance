/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;

/**
 * Class Name: ProductWarrantEnterpriseDto Description: 融资产品担保信息法人
 * 
 * @author Ryan
 * 
 */
public class ProductWarrantEnterpriseDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 0, max = 60, message = "{product.error.enterpriseName.length}", groups = { SubmitProductApply.class })
    private String enterpriseName;// 企业名称
    @Length(min = 0, max = 50, message = "{product.error.enterpriseLicenceNo.length}", groups = { SubmitProductApply.class })
    private String enterpriseLicenceNo;// 企业营业执照号

    private String enterpriseIndustry;// 企业行业
    @Length(min = 0, max = 1000, message = "{product.error.enterpriseNotes.length}", groups = { SubmitProductApply.class })
    private String enterpriseNotes;// 企业备注

    private String createOpid;

    private String lastMntOpid;

    private Date createTs;

    private Date lastMntTs;

    private DynamicOption DenterpriseIndustry;

    /**
     * @return return the value of the var enterpriseName
     */

    public String getEnterpriseName() {
        return enterpriseName;
    }

    /**
     * @param enterpriseName
     *            Set enterpriseName value
     */

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    /**
     * @return return the value of the var enterpriseLicenceNo
     */

    public String getEnterpriseLicenceNo() {
        return enterpriseLicenceNo;
    }

    /**
     * @param enterpriseLicenceNo
     *            Set enterpriseLicenceNo value
     */

    public void setEnterpriseLicenceNo(String enterpriseLicenceNo) {
        this.enterpriseLicenceNo = enterpriseLicenceNo;
    }

    /**
     * @return return the value of the var enterpriseNotes
     */

    public String getEnterpriseNotes() {
        return enterpriseNotes;
    }

    /**
     * @param enterpriseNotes
     *            Set enterpriseNotes value
     */

    public void setEnterpriseNotes(String enterpriseNotes) {
        this.enterpriseNotes = enterpriseNotes;
    }

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
     * @return return the value of the var enterpriseIndustry
     */

    public String getEnterpriseIndustry() {
        return enterpriseIndustry;
    }

    /**
     * @param enterpriseIndustry
     *            Set enterpriseIndustry value
     */

    public void setEnterpriseIndustry(String enterpriseIndustry) {
        this.enterpriseIndustry = enterpriseIndustry;
    }

    /**
     * @return return the value of the var denterpriseIndustry
     */
    
    public DynamicOption getDenterpriseIndustry() {
        return DenterpriseIndustry;
    }

    /**
     * @param denterpriseIndustry Set denterpriseIndustry value
     */
    
    public void setDenterpriseIndustry(DynamicOption denterpriseIndustry) {
        DenterpriseIndustry = denterpriseIndustry;
    }

}
