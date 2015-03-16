/*
 * Project Name: kmfex-platform
 * File Name: UserRechargeRsp.java
 * Class Name: UserRechargeRsp
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hengxin.platform.common.constant.ApplicationConstant;

/**
 * Class Name: UserRechargeRsp Description: TODO
 * 
 * @author tingwang
 * 
 */

public class FundTransferRsp implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 银行流水号
     */
    @NotNull
    private String bankSerial;
    /**
     * 交易批次号
     */
    @NotNull
    private String jnlNo;
    /**
     * 银行账户号
     */
    @NotNull
    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP)
    private String bnkAcctNo;
    /**
     * 交易账户号
     */
    @NotNull
    private String accotNo;
    /**
     * 金额
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 响应代码
     */
    @NotNull
    private String respCode;
    /**
     * 响应信息
     */
    private String errMsg;

    public String getBankSerial() {
        return bankSerial;
    }

    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }

    public String getJnlNo() {
        return jnlNo;
    }

    public void setJnlNo(String jnlNo) {
        this.jnlNo = jnlNo;
    }

    public String getBnkAcctNo() {
        return bnkAcctNo;
    }

    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }

    public String getAccotNo() {
        return accotNo;
    }

    public void setAccotNo(String accotNo) {
        this.accotNo = accotNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "UserRechargeRsp [bankSerial=" + bankSerial + ", jnlNo=" + jnlNo + ", bnkAcctNo=" + bnkAcctNo
                + ", accotNo=" + accotNo + ", amount=" + amount + ", respCode=" + respCode + ", errMsg=" + errMsg + "]";
    }
    
    

}
