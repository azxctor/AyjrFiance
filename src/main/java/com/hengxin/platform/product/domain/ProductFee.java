/*
 * Project Name: kmfex-platform
 * File Name: ProductFee.java
 * Class Name: ProductFee
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

package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.hengxin.platform.product.domain.converter.FeePayMethodTypeMenuConverter;
import com.hengxin.platform.product.enums.EFeePayMethodType;

/**
 * Class Name: ProductFee Description: TODO
 *
 * @author tingwang
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_FEE")
@IdClass(ProductFeePk.class)
public class ProductFee extends BaseInfo implements Serializable {

    @Id
    @Column(name = "PROD_ID")
    private String productId;

    @Id
    @Column(name = "FEE_ID")
    private Integer feeId;
    /**
     * 费率
     */
    @Column(name = "FEE_RT")
    private BigDecimal feeRt;
    /**
     * 支付方式
     */
    @Column(name = "PAY_METHOD")
    @Convert(converter = FeePayMethodTypeMenuConverter.class)
    private EFeePayMethodType payMethod;

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

    public BigDecimal getFeeRt() {
        return feeRt;
    }

    public void setFeeRt(BigDecimal feeRt) {
        this.feeRt = feeRt;
    }

    public EFeePayMethodType getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EFeePayMethodType payMethod) {
        this.payMethod = payMethod;
    }

}
