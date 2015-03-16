/*
 * Project Name: kmfex-platform
 * File Name: CanclePkgMsgHolder.java
 * Class Name: CanclePkgMsgHolder
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

import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;

/**
 * Class Name: CanclePkgMsgHolder Description: TODO
 * 
 * @author tingwang
 * 
 */

public class CanclePkgMsgHolder {
    // 实际付给投资人的金额
    private BigDecimal actualpayBuyerAmt = BigDecimal.ZERO;
    // 实际支付给平台的金额 = 冻结金额 - 实际支付给投资人的金额
    private BigDecimal actualpayPlatAmt = BigDecimal.ZERO;

    private List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
    private List<DedicatedTransferInfo> payerList = new ArrayList<DedicatedTransferInfo>();

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

    public List<DedicatedTransferInfo> getPayerList() {
        return payerList;
    }

    public void setPayerList(List<DedicatedTransferInfo> payerList) {
        this.payerList = payerList;
    }
}
