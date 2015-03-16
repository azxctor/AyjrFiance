
/*
* Project Name: kmfex-platform
* File Name: ProductMortgageVehicle.java
* Class Name: ProductMortgageVehicle
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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class Name: ProductMortgageVehicle Description: 融资产品车辆抵押信息
 * 
 * @author huanbinzhang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_MTGE_V")
public class ProductMortgageVehicle extends BaseProduct implements Serializable {
    @Column(name = "OWNER")
    private String owner; // 车辆所有人

    @Column(name = "REGIST_NO")
    private String registNo; // 车辆登记编号

    @Column(name = "REGIST_INST")
    private String registInstitution; // 车辆登记机关

    @Temporal(TemporalType.DATE)
    @Column(name = "REGIST_DT")
    private Date registDt; // 车辆登记日期

    @Column(name = "BRAND")
    private String brand; // 车辆品牌

    @Column(name = "TYPE")
    private String type; // 车辆型号

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRegistNo() {
        return registNo;
    }

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    public String getRegistInstitution() {
        return registInstitution;
    }

    public void setRegistInstitution(String registInstitution) {
        this.registInstitution = registInstitution;
    }

    public Date getRegistDt() {
        return registDt;
    }

    public void setRegistDt(Date registDt) {
        this.registDt = registDt;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
