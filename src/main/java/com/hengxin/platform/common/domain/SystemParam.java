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

package com.hengxin.platform.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.product.domain.BaseInfo;

/**
 * Class Name: ProductSystemParam Description: 系统参数表
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "GL_SYS_PARAM")
public class SystemParam extends BaseInfo implements Serializable {
    
    @Id
    @Column(name = "PARAM_KEY")
    private String paramKey; // 参数名
    
    @Column(name = "PARAM_VALUE")
    private String paramValue; // 参数值
    
    @Column(name = "PARAM_DESC")
    private String paramDesc; // 参数说明

    /**
     * @return return the value of the var paramKey
     */
    
    public String getParamKey() {
        return paramKey;
    }

    /**
     * @param paramKey Set paramKey value
     */
    
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * @return return the value of the var paramValue
     */
    
    public String getParamValue() {
        return paramValue;
    }

    /**
     * @param paramValue Set paramValue value
     */
    
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * @return return the value of the var paramDesc
     */
    
    public String getParamDesc() {
        return paramDesc;
    }

    /**
     * @param paramDesc Set paramDesc value
     */
    
    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }
    

}
