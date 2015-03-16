/*
 * Project Name: kmfex-platform
 * File Name: ProductAssetVehicle.java
 * Class Name: ProductAssetVehicle
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
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class Name: ProductAssetVehicle Description: 融资产品资产信息
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_ASSET")
public class ProductAsset extends BaseProduct implements Serializable {

    @Column(name = "ASSET_TYPE")
    private String type; // 类型
    
    @Column(name = "ASSET_AMT")
    private BigDecimal assertAmount; // 资产总额
    
    @Column(name = "ASSET_DESC")
    private String notes; // 说明
    


    /**
     * @return return the value of the var type
     */
    
    public String getType() {
        return type;
    }

    /**
     * @param type Set type value
     */
    
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return return the value of the var assertAmount
     */
    
    public BigDecimal getAssertAmount() {
        return assertAmount;
    }

    /**
     * @param assertAmount Set assertAmount value
     */
    
    public void setAssertAmount(BigDecimal assertAmount) {
        this.assertAmount = assertAmount;
    }

    /**
     * @return return the value of the var notes
     */
    
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes Set notes value
     */
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    

}
