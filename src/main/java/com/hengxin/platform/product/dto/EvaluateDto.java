/*
 * Project Name: kmfex-platform
 * File Name: EvaluateDto.java
 * Class Name: EvaluateDto
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.product.enums.ERiskLevel;

/**
 * Class Name: EvaluateDto Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

public class EvaluateDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;

    @Deprecated
    private BigDecimal productGrage;

    @Deprecated
    private BigDecimal financierGrage;

    @Deprecated
    private BigDecimal warrantGrage;

    @Deprecated
    private BigDecimal totalGrage;// Should be calculated at backend.

    @Deprecated
    private String productGrageComment; // 产品评分说明

    @Deprecated
    private String financierGrageComment; // 融资人评分说明

    @Deprecated
    private String warrantGrageComment; // 担保人评分说明

    @Deprecated
    private String totalGrageComment; // 总体评分说明

    private ERiskLevel productLevel; // 融资项目级别

    @Pattern(regexp = ApplicationConstant.PROD_LEVEL_REGEXP, message = "{product.error.level_invalid}")
	private String financierLevel; // 融资会员信用积分

    @Pattern(regexp = ApplicationConstant.PROD_LEVEL_REGEXP, message = "{product.error.level_invalid}")
	private String warrantorLevel; // 担保机构信用积分
    
//    @NotEmpty(message = "{member.error.field.empty}")
    @Deprecated
    private String wrtrCreditFile;

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
     * @return return the value of the var productGrage
     */

    public BigDecimal getProductGrage() {
        return productGrage;
    }

    /**
     * @param productGrage
     *            Set productGrage value
     */

    public void setProductGrage(BigDecimal productGrage) {
        this.productGrage = productGrage;
    }

    /**
     * @return return the value of the var financierGrage
     */

    public BigDecimal getFinancierGrage() {
        return financierGrage;
    }

    /**
     * @param financierGrage
     *            Set financierGrage value
     */

    public void setFinancierGrage(BigDecimal financierGrage) {
        this.financierGrage = financierGrage;
    }

    /**
     * @return return the value of the var warrantGrage
     */

    public BigDecimal getWarrantGrage() {
        return warrantGrage;
    }

    /**
     * @param warrantGrage
     *            Set warrantGrage value
     */

    public void setWarrantGrage(BigDecimal warrantGrage) {
        this.warrantGrage = warrantGrage;
    }

    /**
     * @return return the value of the var totalGrage
     */

    public BigDecimal getTotalGrage() {
        return totalGrage;
    }

    /**
     * @param totalGrage
     *            Set totalGrage value
     */

    public void setTotalGrage(BigDecimal totalGrage) {
        this.totalGrage = totalGrage;
    }

    /**
     * @return return the value of the var productGrageComment
     */

    public String getProductGrageComment() {
        return productGrageComment;
    }

    /**
     * @param productGrageComment
     *            Set productGrageComment value
     */

    public void setProductGrageComment(String productGrageComment) {
        this.productGrageComment = productGrageComment;
    }

    /**
     * @return return the value of the var financierGrageComment
     */

    public String getFinancierGrageComment() {
        return financierGrageComment;
    }

    /**
     * @param financierGrageComment
     *            Set financierGrageComment value
     */

    public void setFinancierGrageComment(String financierGrageComment) {
        this.financierGrageComment = financierGrageComment;
    }

    /**
     * @return return the value of the var warrantGrageComment
     */

    public String getWarrantGrageComment() {
        return warrantGrageComment;
    }

    /**
     * @param warrantGrageComment
     *            Set warrantGrageComment value
     */

    public void setWarrantGrageComment(String warrantGrageComment) {
        this.warrantGrageComment = warrantGrageComment;
    }

    /**
     * @return return the value of the var totalGrageComment
     */

    public String getTotalGrageComment() {
        return totalGrageComment;
    }

    /**
     * @param totalGrageComment
     *            Set totalGrageComment value
     */

    public void setTotalGrageComment(String totalGrageComment) {
        this.totalGrageComment = totalGrageComment;
    }

	public String getFinancierLevel() {
		return financierLevel;
	}

	public void setFinancierLevel(String financierLevel) {
		this.financierLevel = financierLevel;
	}

	public String getWarrantorLevel() {
		return warrantorLevel;
	}

	public void setWarrantorLevel(String warrantorLevel) {
		this.warrantorLevel = warrantorLevel;
	}

	public ERiskLevel getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}

	/**
	 * @return the wrtrCreditFile
	 */
	public String getWrtrCreditFile() {
		return wrtrCreditFile;
	}

	/**
	 * @param wrtrCreditFile the wrtrCreditFile to set
	 */
	@JsonProperty("wrtrCreditFile")
	public void setWrtrCreditFile(String wrtrCreditFile) {
		this.wrtrCreditFile = wrtrCreditFile;
	}

}
