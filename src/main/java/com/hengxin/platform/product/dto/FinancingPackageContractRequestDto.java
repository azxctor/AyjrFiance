/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackageContractRequestDto.java
 * Class Name: FinancingPackageContractRequestDto
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

import com.hengxin.platform.common.dto.DataTablesRequestDto;

/**
 * Class Name: FinancingPackageContractRequestDto Description: TODO
 * 
 * @author yingchangwang
 * 
 */

public class FinancingPackageContractRequestDto extends DataTablesRequestDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String packageId;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

}
