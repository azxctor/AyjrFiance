/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductController.java
 * Class Name: FinanceProductController
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
 * 
 * Class Name: ProductAIPDto
 * 
 */
public class ProductAIPGroupDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @return return the value of the var groupName
     */
    
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName Set groupName value
     */
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

   

    

}
