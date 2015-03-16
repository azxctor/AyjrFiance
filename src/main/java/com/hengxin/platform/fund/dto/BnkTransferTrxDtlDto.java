
/*
* Project Name: kmfex
* File Name: BnkTransferTrxDtlDto.java
* Class Name: BnkTransferTrxDtlDto
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
	
package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Class Name: BnkTransferTrxDtlDto
 * Description: TODO
 * @author congzhou
 *
 */

public class BnkTransferTrxDtlDto implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
     * 银行充值笔数
     */
    private Integer BnkRechargeCount;
    
    /*
     * 银行提现笔数
     */
    private Integer BnkWithdrawalCount;
    
    /*
     * 平台充值笔数
     */
    private Integer PlatformRechargeCount;
    
    /*
     * 平台提现笔数
     */
    private Integer PlatformWithdrawalCount;
    
    /*
     * 银行充值金额
     */
    private BigDecimal BnkRechargeAmt;
    
    /*
     * 银行提现金额
     */
    private BigDecimal BnkWithdrawalAmt;
    
    /*
     * 平台充值金额
     */
    private BigDecimal PlatformRechargeAmt;
    
    /*
     * 平台提现金额
     */
    private BigDecimal PlatformWithdrawalAmt;

	public Integer getBnkRechargeCount() {
		return BnkRechargeCount;
	}

	public void setBnkRechargeCount(Integer bnkRechargeCount) {
		BnkRechargeCount = bnkRechargeCount;
	}

	public Integer getBnkWithdrawalCount() {
		return BnkWithdrawalCount;
	}

	public void setBnkWithdrawalCount(Integer bnkWithdrawalCount) {
		BnkWithdrawalCount = bnkWithdrawalCount;
	}

	public Integer getPlatformRechargeCount() {
		return PlatformRechargeCount;
	}

	public void setPlatformRechargeCount(Integer platformRechargeCount) {
		PlatformRechargeCount = platformRechargeCount;
	}

	public Integer getPlatformWithdrawalCount() {
		return PlatformWithdrawalCount;
	}

	public void setPlatformWithdrawalCount(Integer platformWithdrawalCount) {
		PlatformWithdrawalCount = platformWithdrawalCount;
	}

	public BigDecimal getBnkRechargeAmt() {
		return BnkRechargeAmt;
	}

	public void setBnkRechargeAmt(BigDecimal bnkRechargeAmt) {
		BnkRechargeAmt = bnkRechargeAmt;
	}

	public BigDecimal getBnkWithdrawalAmt() {
		return BnkWithdrawalAmt;
	}

	public void setBnkWithdrawalAmt(BigDecimal bnkWithdrawalAmt) {
		BnkWithdrawalAmt = bnkWithdrawalAmt;
	}

	public BigDecimal getPlatformRechargeAmt() {
		return PlatformRechargeAmt;
	}

	public void setPlatformRechargeAmt(BigDecimal platformRechargeAmt) {
		PlatformRechargeAmt = platformRechargeAmt;
	}

	public BigDecimal getPlatformWithdrawalAmt() {
		return PlatformWithdrawalAmt;
	}

	public void setPlatformWithdrawalAmt(BigDecimal platformWithdrawalAmt) {
		PlatformWithdrawalAmt = platformWithdrawalAmt;
	}
    
}
