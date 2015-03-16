/*
 * Project Name: kmfex-platform
 * File Name: ProductFeeDto.java
 * Class Name: ProductFeeDto
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

import javax.validation.constraints.NotNull;

import com.hengxin.platform.common.dto.FeeDto;
import com.hengxin.platform.product.enums.EFeePayMethodType;
import com.hengxin.platform.product.validator.FinancingPackageFeePayMethodCheck;

/**
 * Class Name: ProductFeeDto Description: TODO
 *
 * @author yingchangwang
 *
 */

@FinancingPackageFeePayMethodCheck(payMethod = "payMethod")
public class ProductFeeDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
    private Integer feeId;
    private EFeePayMethodType payMethod;
    @NotNull(message="{product.error.trans.fee.empty}")
    private BigDecimal feeRt;
    private FeeDto feeDto;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public EFeePayMethodType getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EFeePayMethodType payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getFeeRt() {
        return feeRt;
    }

    public void setFeeRt(BigDecimal feeRt) {
        this.feeRt = feeRt;
    }

    public FeeDto getFeeDto() {
        return feeDto;
    }

    public void setFeeDto(FeeDto feeDto) {
        this.feeDto = feeDto;
    }

}
