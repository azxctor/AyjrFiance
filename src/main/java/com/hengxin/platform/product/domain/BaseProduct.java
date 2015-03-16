/*
 * Project Name: kmfex-platform
 * File Name: BaseApplicationPo.java
 * Class Name: BaseApplicationPo
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;


/**
 * Class Name: BaseProduct
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
@IdClass(ProductPk.class)
public class BaseProduct extends BaseInfo implements Serializable {
    @Id
    @Column(name = "PROD_ID")
    private String productId;

    @Id
    @Column(name = "SEQ_ID")
    private int seqId;// 序号,001-999

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
     * @return return the value of the var seqId
     */

    public int getSeqId() {
        return seqId;
    }

    /**
     * @param seqId
     *            Set seqId value
     */

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

}
