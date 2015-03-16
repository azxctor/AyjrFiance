/*
 * Project Name: kmfex-platform
 * File Name: FreezeDto.java
 * Class Name: FreezeDto
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

/**
 * Class Name: FreezeDto Description: TODO
 * 
 * @author chunlinwang
 * 
 */

public class FreezeDto implements Serializable {

    /**
     * Variables Name: serialVersionUID Description: TODO Value Description: TODO
     */

    private static final long serialVersionUID = 1L;

    private String productId;

    private BigDecimal servMrgnAmt; // 融资服务合同保证金金额

    private BigDecimal wrtrMrgnRt; // 担保机构还款保证金比例

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
     * @return return the value of the var servMrgnAmt
     */

    public BigDecimal getServMrgnAmt() {
        return servMrgnAmt;
    }

    /**
     * @param servMrgnAmt
     *            Set servMrgnAmt value
     */

    public void setServMrgnAmt(BigDecimal servMrgnAmt) {
        this.servMrgnAmt = servMrgnAmt;
    }

    /**
     * @return return the value of the var wrtrMrgnRt
     */

    public BigDecimal getWrtrMrgnRt() {
        return wrtrMrgnRt;
    }

    /**
     * @param wrtrMrgnRt
     *            Set wrtrMrgnRt value
     */

    public void setWrtrMrgnRt(BigDecimal wrtrMrgnRt) {
        this.wrtrMrgnRt = wrtrMrgnRt;
    }

}
