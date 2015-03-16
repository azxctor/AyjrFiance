/*
 * Project Name: kmfex-platform
 * File Name: CleaningPkgMsgHolder.java
 * Class Name: CleaningPkgMsgHolder
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
import java.util.ArrayList;
import java.util.List;

import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;

/**
 * Class Name: CleaningPkgMsgHolder Description: TODO
 * 
 * @author tingwang
 * 
 */

public class CleaningPkgMsgHolder {

    private boolean canFinish = false;

    private BigDecimal actualpayBuyerAmt = BigDecimal.ZERO;

    private BigDecimal actualpayPlatAmt = BigDecimal.ZERO;

    private BigDecimal totalTrxAmt = BigDecimal.ZERO;

    private BigDecimal prinPay = BigDecimal.ZERO;

    private BigDecimal prinFinePay = BigDecimal.ZERO;

    private BigDecimal intrPay = BigDecimal.ZERO;

    private BigDecimal intrFinePay = BigDecimal.ZERO;

    private BigDecimal feePay = BigDecimal.ZERO;

    private BigDecimal feeFinePay = BigDecimal.ZERO;

    private BigDecimal wrtrPrinForfPay = BigDecimal.ZERO;

    private BigDecimal wrtrIntrForfPay = BigDecimal.ZERO;

    private List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
    private List<TransferInfo> payerList = new ArrayList<TransferInfo>();
    private List<TransferInfo> profPayeeList = new ArrayList<TransferInfo>();
    private List<TransferInfo> profPayerList = new ArrayList<TransferInfo>();

    public BigDecimal getActualpayBuyerAmt() {
        return actualpayBuyerAmt;
    }

    public void setActualpayBuyerAmt(BigDecimal actualpayBuyerAmt) {
        this.actualpayBuyerAmt = actualpayBuyerAmt;
    }

    public BigDecimal getActualpayPlatAmt() {
        return actualpayPlatAmt;
    }

    public void setActualpayPlatAmt(BigDecimal actualpayPlatAmt) {
        this.actualpayPlatAmt = actualpayPlatAmt;
    }

    public List<TransferInfo> getPayeeList() {
        return payeeList;
    }

    public void setPayeeList(List<TransferInfo> payeeList) {
        this.payeeList = payeeList;
    }

    public List<TransferInfo> getPayerList() {
        return payerList;
    }

    public void setPayerList(List<TransferInfo> payerList) {
        this.payerList = payerList;
    }

    public boolean isCanFinish() {
        return canFinish;
    }

    public void setCanFinish(boolean canFinish) {
        this.canFinish = canFinish;
    }

    public BigDecimal getTotalTrxAmt() {
        return totalTrxAmt;
    }

    public void setTotalTrxAmt(BigDecimal totalTrxAmt) {
        this.totalTrxAmt = totalTrxAmt;
    }

    public BigDecimal getPrinPay() {
        return prinPay;
    }

    public void setPrinPay(BigDecimal prinPay) {
        this.prinPay = prinPay;
    }

    public BigDecimal getPrinFinePay() {
        return prinFinePay;
    }

    public void setPrinFinePay(BigDecimal prinFinePay) {
        this.prinFinePay = prinFinePay;
    }

    public BigDecimal getIntrPay() {
        return intrPay;
    }

    public void setIntrPay(BigDecimal intrPay) {
        this.intrPay = intrPay;
    }

    public BigDecimal getIntrFinePay() {
        return intrFinePay;
    }

    public void setIntrFinePay(BigDecimal intrFinePay) {
        this.intrFinePay = intrFinePay;
    }

    public BigDecimal getFeePay() {
        return feePay;
    }

    public void setFeePay(BigDecimal feePay) {
        this.feePay = feePay;
    }

    public BigDecimal getFeeFinePay() {
        return feeFinePay;
    }

    public void setFeeFinePay(BigDecimal feeFinePay) {
        this.feeFinePay = feeFinePay;
    }

    public List<TransferInfo> getProfPayeeList() {
        return profPayeeList;
    }

    public void setProfPayeeList(List<TransferInfo> profPayeeList) {
        this.profPayeeList = profPayeeList;
    }

    public List<TransferInfo> getProfPayerList() {
        return profPayerList;
    }

    public void setProfPayerList(List<TransferInfo> profPayerList) {
        this.profPayerList = profPayerList;
    }

    public BigDecimal getWrtrPrinForfPay() {
        return wrtrPrinForfPay;
    }

    public void setWrtrPrinForfPay(BigDecimal wrtrPrinForfPay) {
        this.wrtrPrinForfPay = wrtrPrinForfPay;
    }

    public BigDecimal getWrtrIntrForfPay() {
        return wrtrIntrForfPay;
    }

    public void setWrtrIntrForfPay(BigDecimal wrtrIntrForfPay) {
        this.wrtrIntrForfPay = wrtrIntrForfPay;
    }
    
    

}
