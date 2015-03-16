/*
 * Project Name: kmfex-platform
 * File Name: ProductProviderApplicationPo.java
 * Class Name: ProductProviderApplicationPo
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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class Name: ProductPledge 融资产品质押信息
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_PLEDGE")
public class ProductPledge extends BaseProduct implements Serializable {

    @Column(name = "P_NAME")
    private String name;// 质押物名称

    @Column(name = "P_CLASS")
    private String pledgeClass;// 品种

    @Column(name = "P_TYPE")
    private String type;// 型号

    @Column(name = "P_LOCATION")
    private String location;// 存放地

    @Column(name = "P_NOTES")
    private String notes;// 备注

    @Column(name = "P_COUNT")
    private Long count;// 数量

    @Column(name = "P_PRICE")
    private BigDecimal price;// 价格

    /**
     * @return return the value of the var name
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     *            Set name value
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return return the value of the var pClass
     */

    public String getpClass() {
        return pledgeClass;
    }

    /**
     * @param pClass
     *            Set pClass value
     */

    public void setpClass(String pledgeClass) {
        this.pledgeClass = pledgeClass;
    }

    /**
     * @return return the value of the var type
     */

    public String getType() {
        return type;
    }

    /**
     * @param type
     *            Set type value
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return return the value of the var location
     */

    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            Set location value
     */

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return return the value of the var notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     *            Set notes value
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return return the value of the var pledgeClass
     */
    
    public String getPledgeClass() {
        return pledgeClass;
    }

    /**
     * @param pledgeClass Set pledgeClass value
     */
    
    public void setPledgeClass(String pledgeClass) {
        this.pledgeClass = pledgeClass;
    }

    /**
     * @return return the value of the var count
     */
    
    public Long getCount() {
        return count;
    }

    /**
     * @param count Set count value
     */
    
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * @return return the value of the var price
     */
    
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price Set price value
     */
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return return the value of the var count
     */

}
