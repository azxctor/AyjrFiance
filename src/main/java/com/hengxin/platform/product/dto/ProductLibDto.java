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

package com.hengxin.platform.product.dto;

import java.io.Serializable;

/**
 * Class Name: ProductLibDto Description: 融资产品库
 * 
 * @author Ryan
 * 
 */
public class ProductLibDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer libId; // 类型

    private String libName; // 说明

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

}
