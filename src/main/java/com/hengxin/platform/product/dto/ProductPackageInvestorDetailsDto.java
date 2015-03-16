/*
 * Project Name: kmfex-platform
 * File Name: ProductPackageInvestorDetailsDto.java
 * Class Name: ProductPackageInvestorDetailsDto
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

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.util.FileUploadUtil;

/**
 * Class Name: ProductPackageInvestorDetailsDto
 * 
 * @author shengzhou
 * 
 */
public class ProductPackageInvestorDetailsDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProductPackageDetailsDto packageDetailsDto;

    private ProductDetailsDto productDetailsDto;

    // 剩余本息合计
    private BigDecimal lastAmount;

    // 报价
    private BigDecimal amount;

    // 期数
    private Integer count;

    // 最后还款日
    private String lastPayDay;

    // 剩余融资额
    private BigDecimal leftAmount;

    // 判断是否为待放款审批及以后状态（涉及显示：签约日期，合同模板）
    private boolean statusAfterWaitLoadApproal;
    
    private String contractTemplateId;
    
    private String lotId;
    
    private String guaranteeLicenseNoImg;

    @SuppressWarnings("unused")
	private String guaranteeLicenseNoImgUrl;

    /**
     * @return return the value of the var packageDetailsDto
     */

    public ProductPackageDetailsDto getPackageDetailsDto() {
        return packageDetailsDto;
    }

    /**
     * @param packageDetailsDto
     *            Set packageDetailsDto value
     */

    public void setPackageDetailsDto(ProductPackageDetailsDto packageDetailsDto) {
        this.packageDetailsDto = packageDetailsDto;
    }

    /**
     * @return return the value of the var productDetailsDto
     */

    public ProductDetailsDto getProductDetailsDto() {
        return productDetailsDto;
    }

    /**
     * @param productDetailsDto
     *            Set productDetailsDto value
     */

    public void setProductDetailsDto(ProductDetailsDto productDetailsDto) {
        this.productDetailsDto = productDetailsDto;
    }

    /**
     * @return return the value of the var lastAmount
     */

    public BigDecimal getLastAmount() {
        return lastAmount;
    }

    /**
     * @param lastAmount
     *            Set lastAmount value
     */

    public void setLastAmount(BigDecimal lastAmount) {
        this.lastAmount = lastAmount;
    }

    /**
     * @return return the value of the var amount
     */

    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     *            Set amount value
     */

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return return the value of the var count
     */

    public Integer getCount() {
        return count;
    }

    /**
     * @param count
     *            Set count value
     */

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return return the value of the var lastPayDay
     */

    public String getLastPayDay() {
        return lastPayDay;
    }

    /**
     * @param lastPayDay
     *            Set lastPayDay value
     */

    public void setLastPayDay(String lastPayDay) {
        this.lastPayDay = lastPayDay;
    }

    /**
     * @return return the value of the var leftAmount
     */

    public BigDecimal getLeftAmount() {
        return leftAmount;
    }

    /**
     * @param leftAmount
     *            Set leftAmount value
     */

    public void setLeftAmount(BigDecimal leftAmount) {
        this.leftAmount = leftAmount;
    }

    public boolean isStatusAfterWaitLoadApproal() {
        return statusAfterWaitLoadApproal;
    }

    public void setStatusAfterWaitLoadApproal(boolean statusAfterWaitLoadApproal) {
        this.statusAfterWaitLoadApproal = statusAfterWaitLoadApproal;
    }

    public String getContractTemplateId() {
        return contractTemplateId;
    }

    public void setContractTemplateId(String contractTemplateId) {
        this.contractTemplateId = contractTemplateId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }
    public String getGuaranteeLicenseNoImg() {
        return guaranteeLicenseNoImg;
    }

    public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
        this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
    }

    public String getGuaranteeLicenseNoImgUrl() {
        if (StringUtils.isNotBlank(guaranteeLicenseNoImg)) {
            return FileUploadUtil.getTempFolder() + guaranteeLicenseNoImg;
        }
        return "";
    }
}
