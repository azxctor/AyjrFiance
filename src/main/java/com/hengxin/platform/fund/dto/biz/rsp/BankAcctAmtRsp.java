/*
 * Project Name: kmfex-platform
 * File Name: BankAcctAmtRsp.java
 * Class Name: BankAcctAmtRsp
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

package com.hengxin.platform.fund.dto.biz.rsp;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: BankAcctAmtRsp Description: TODO
 * 
 * @author tingwang
 * 
 */

public class BankAcctAmtRsp implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 可取余额
     */
    private BigDecimal cashableAmt;
    /**
     * 可用余额
     */
    private BigDecimal availableAmt;
    /**
     * 错误信息
     */
    private String errMsg;

    public BigDecimal getCashableAmt() {
        return cashableAmt;
    }

    public void setCashableAmt(BigDecimal cashableAmt) {
        this.cashableAmt = cashableAmt;
    }

    public BigDecimal getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(BigDecimal availableAmt) {
        this.availableAmt = availableAmt;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
