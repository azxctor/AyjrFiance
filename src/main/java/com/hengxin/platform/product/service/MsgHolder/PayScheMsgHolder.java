/*
 * Project Name: kmfex-platform
 * File Name: PayScheMsgHolder.java
 * Class Name: PayScheMsgHolder
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

package com.hengxin.platform.product.service.MsgHolder;

import java.math.BigDecimal;

/**
 * Class Name: PayScheMsgHolder Description: TODO
 * 
 * @author tingwang
 * 
 */

public class PayScheMsgHolder {

    // 本金
    private BigDecimal prinPay = BigDecimal.ZERO;

    // 利息
    private BigDecimal intrPay = BigDecimal.ZERO;

    // 费用
    private BigDecimal feePay = BigDecimal.ZERO;

    // 本金罚金
    private BigDecimal prinFinePay = BigDecimal.ZERO;

    // 利息罚金
    private BigDecimal intrFinePay = BigDecimal.ZERO;

    // 费用罚金
    private BigDecimal feeFinePay = BigDecimal.ZERO;

    // 融资人总付金额
    private BigDecimal fncrPayTotalAmt = BigDecimal.ZERO;

    private BigDecimal principal = BigDecimal.ZERO;
    private BigDecimal interest = BigDecimal.ZERO;
    private BigDecimal fee = BigDecimal.ZERO;

    public BigDecimal getPrinPay() {
        return prinPay;
    }

    public void setPrinPay(BigDecimal prinPay) {
        this.prinPay = prinPay;
    }

    public BigDecimal getIntrPay() {
        return intrPay;
    }

    public void setIntrPay(BigDecimal intrPay) {
        this.intrPay = intrPay;
    }

    public BigDecimal getFeePay() {
        return feePay;
    }

    public void setFeePay(BigDecimal feePay) {
        this.feePay = feePay;
    }

    public BigDecimal getPrinFinePay() {
        return prinFinePay;
    }

    public void setPrinFinePay(BigDecimal prinFinePay) {
        this.prinFinePay = prinFinePay;
    }

    public BigDecimal getIntrFinePay() {
        return intrFinePay;
    }

    public void setIntrFinePay(BigDecimal intrFinePay) {
        this.intrFinePay = intrFinePay;
    }

    public BigDecimal getFeeFinePay() {
        return feeFinePay;
    }

    public void setFeeFinePay(BigDecimal feeFinePay) {
        this.feeFinePay = feeFinePay;
    }

    public BigDecimal getFncrPayTotalAmt() {
        return fncrPayTotalAmt;
    }

    public void setFncrPayTotalAmt(BigDecimal fncrPayTotalAmt) {
        this.fncrPayTotalAmt = fncrPayTotalAmt;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

}
