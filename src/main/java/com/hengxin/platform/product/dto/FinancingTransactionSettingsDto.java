/*
 * Project Name: kmfex-platform
 * File Name: ActionResult.java
 * Class Name: ActionResult
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
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Class Name: FinancingTransactionSettingsDto
 *
 * @author yingchangwang
 *
 */
public class FinancingTransactionSettingsDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
    @NotNull(message = "product.error.trans.contract.service.fee.empty")
    private BigDecimal performanceBondRate;
    private BigDecimal productQuota;
    private boolean itemized;
    private int itemCount;
    private boolean average;
    @Valid
    private List<ProductPackageDto> packageList;
    @Valid
    private List<ProductFeeDto> productFeeList;

    public String getProductId() {
        return productId;
    }

    public BigDecimal getPerformanceBondRate() {
        return performanceBondRate;
    }

    public void setPerformanceBondRate(BigDecimal performanceBondRate) {
        this.performanceBondRate = performanceBondRate;
    }

    public BigDecimal getProductQuota() {
        return productQuota;
    }

    public void setProductQuota(BigDecimal productQuota) {
        this.productQuota = productQuota;
    }

    public boolean isItemized() {
        return itemized;
    }

    public void setItemized(boolean itemized) {
        this.itemized = itemized;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isAverage() {
        return average;
    }

    public void setAverage(boolean average) {
        this.average = average;
    }

    public List<ProductPackageDto> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<ProductPackageDto> packageList) {
        this.packageList = packageList;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<ProductFeeDto> getProductFeeList() {
        return productFeeList;
    }

    public void setProductFeeList(List<ProductFeeDto> productFeeList) {
        this.productFeeList = productFeeList;
    }
}
