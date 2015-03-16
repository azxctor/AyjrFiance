
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
 * Class Name: PoolCheckDtlDto
 * Description: TODO
 * @author linuxp
 *
 */

public class PoolCheckDtlDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String accDate = "";//会计日期
    
    String pool = "";//资金池
    
    BigDecimal yestAmt = BigDecimal.ZERO;//昨日余额
    
    BigDecimal currRecv = BigDecimal.ZERO;//当日收
    
    BigDecimal currPay = BigDecimal.ZERO;//当日付
    
    BigDecimal currAmt = BigDecimal.ZERO;//当日余额
    
    BigDecimal checkAmt = BigDecimal.ZERO;//核对金额
    
    BigDecimal mistake = BigDecimal.ZERO;//差额
    
    String result = "";//核对结果
    
    boolean total = false;//是否为合计记录
    
	public String getAccDate() {
		return accDate;
	}

	public void setAccDate(String accDate) {
		this.accDate = accDate;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public BigDecimal getYestAmt() {
		return yestAmt;
	}

	public void setYestAmt(BigDecimal yestAmt) {
		this.yestAmt = yestAmt;
	}

	public BigDecimal getCurrRecv() {
		return currRecv;
	}

	public void setCurrRecv(BigDecimal currRecv) {
		this.currRecv = currRecv;
	}

	public BigDecimal getCurrPay() {
		return currPay;
	}

	public void setCurrPay(BigDecimal currPay) {
		this.currPay = currPay;
	}

	public BigDecimal getCurrAmt() {
		return currAmt;
	}

	public void setCurrAmt(BigDecimal currAmt) {
		this.currAmt = currAmt;
	}

	public BigDecimal getCheckAmt() {
		return checkAmt;
	}

	public void setCheckAmt(BigDecimal checkAmt) {
		this.checkAmt = checkAmt;
	}
	
	public BigDecimal getMistake() {
		return mistake;
	}

	public void setMistake(BigDecimal mistake) {
		this.mistake = mistake;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}
}
