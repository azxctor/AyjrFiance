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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class Name: ProductLib Description: 融资产品库
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_LIB")
public class ProductLib extends BaseInfo implements Serializable {
    @Id
    @Column(name = "LIB_ID")
    private Integer libId; // 类型

    @Column(name = "LIB_NAME")
    private String libName; // 说明
    
    @Column(name = "REAL_CASHABLE_FLG")
    private String realCashableFlg;

    /**
     * @return return the value of the var libId
     */

    public Integer getLibId() {
        return libId;
    }

    /**
     * @param libId
     *            Set libId value
     */

    public void setLibId(Integer libId) {
        this.libId = libId;
    }

    /**
     * @return return the value of the var libName
     */

    public String getLibName() {
        return libName;
    }

    /**
     * @param libName
     *            Set libName value
     */

    public void setLibName(String libName) {
        this.libName = libName;
    }

	public String getRealCashableFlg() {
		return realCashableFlg;
	}

	public void setRealCashableFlg(String realCashableFlg) {
		this.realCashableFlg = realCashableFlg;
	}

}
