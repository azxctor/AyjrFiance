/*
 * Project Name: kmfex-platform
 * File Name: ProductFeePk.java
 * Class Name: ProductFeePk
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

/**
 * Class Name: ProductFeePk Description: TODO
 * 
 * @author tingwang
 * 
 */

@SuppressWarnings("serial")
public class ProductFeePk implements Serializable {

    private String productId;

    private Integer feeId;

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

}
