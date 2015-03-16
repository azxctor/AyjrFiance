/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;

/**
 * Class Name: ProductAssetVehicleDetailsDto Description: 资产车辆
 * 
 * @author Ryan
 * 
 */
public class ProductContractTemplateDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String templateId; // 模板ID

    private String templateName; // 合同模板名

    /**
     * @return return the value of the var templateName
     */

    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName
     *            Set templateName value
     */

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

}
