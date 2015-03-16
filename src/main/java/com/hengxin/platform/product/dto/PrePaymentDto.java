/*
 * Project Name: kmfex-platform
 * File Name: PrePaymentDto.java
 * Class Name: PrePaymentDto
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

/**
 * Class Name: PrePaymentDto Description: TODO
 * 
 * @author yingchangwang
 * 
 */

public class PrePaymentDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String packageId;
    private BigDecimal totalAmt;
    private BigDecimal totalPrincipaBalance;
    private BigDecimal totalInterestAmt;
    private BigDecimal feeAmt;
    private BigDecimal totalPenalty;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public BigDecimal getTotalPrincipaBalance() {
        return totalPrincipaBalance;
    }

    public void setTotalPrincipaBalance(BigDecimal totalPrincipaBalance) {
        this.totalPrincipaBalance = totalPrincipaBalance;
    }

    public BigDecimal getTotalInterestAmt() {
        return totalInterestAmt;
    }

    public void setTotalInterestAmt(BigDecimal totalInterestAmt) {
        this.totalInterestAmt = totalInterestAmt;
    }

    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    public BigDecimal getTotalPenalty() {
        return totalPenalty;
    }

    public void setTotalPenalty(BigDecimal totalPenalty) {
        this.totalPenalty = totalPenalty;
    }

}
