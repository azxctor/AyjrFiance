/*
 * Project Name: kmfex-platform
 * File Name: ScheduleSearchDto.java
 * Class Name: ScheduleSearchDto
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
import java.math.BigDecimal;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.product.enums.ENumberOperator;

/**
 * @author shengzhou
 * 
 */
public class ScheduleSearchDto extends DataTablesRequestDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String packageId;

    private String packageName;

    private String financierName;

    private String startPaymentDate;

    private String endPaymentDate;

    private BigDecimal totalAmt;

    private ENumberOperator amtOperation;

    /**
     * @return return the value of the var packageId
     */

    public String getPackageId() {
        return packageId;
    }

    /**
     * @param packageId
     *            Set packageId value
     */

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    /**
     * @return return the value of the var packageName
     */

    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            Set packageName value
     */

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFinancierName() {
        return financierName;
    }

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    public String getStartPaymentDate() {
        return startPaymentDate;
    }

    public void setStartPaymentDate(String startPaymentDate) {
        this.startPaymentDate = startPaymentDate;
    }

    public String getEndPaymentDate() {
        return endPaymentDate;
    }

    public void setEndPaymentDate(String endPaymentDate) {
        this.endPaymentDate = endPaymentDate;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public ENumberOperator getAmtOperation() {
        return amtOperation;
    }

    public void setAmtOperation(ENumberOperator amtOperation) {
        this.amtOperation = amtOperation;
    }

}
