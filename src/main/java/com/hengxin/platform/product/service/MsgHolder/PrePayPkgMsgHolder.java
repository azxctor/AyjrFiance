/*
 * Project Name: kmfex-platform
 * File Name: PrePayPkgMsgHolder.java
 * Class Name: PrePayPkgMsgHolder
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
 * Class Name: PrePayPkgMsgHolder Description: TODO
 * 
 * @author tingwang
 * 
 */

public class PrePayPkgMsgHolder {
    // 实际付给投资人的金额
    private BigDecimal actualpayBuyerAmt;
    // 实际支付给平台的金额 = 冻结金额 - 实际支付给投资人的金额
    private BigDecimal actualpayPlatAmt;

    private BigDecimal totalTrxAmt;

    private List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
    private List<TransferInfo> payerList = new ArrayList<TransferInfo>();
    List<TransferInfo> profPayeeList = new ArrayList<TransferInfo>();
    List<TransferInfo> profPayerList = new ArrayList<TransferInfo>();

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

    public BigDecimal getTotalTrxAmt() {
        return totalTrxAmt;
    }

    public void setTotalTrxAmt(BigDecimal totalTrxAmt) {
        this.totalTrxAmt = totalTrxAmt;
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

}
