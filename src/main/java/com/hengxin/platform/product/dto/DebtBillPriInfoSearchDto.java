/*
 * Project Name: kmfex-platform
 * File Name: SignedDebtBillSearchDto.java
 * Class Name: SignedDebtBillSearchDto
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
 * Class Name: DebtBillPriInfoSearchDto.
 * 
 * @author tingwang
 * 
 */

public class DebtBillPriInfoSearchDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 融资包编号.
     */
    private String pkgId;

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

}
