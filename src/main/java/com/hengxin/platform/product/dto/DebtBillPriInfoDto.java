/*
 * Project Name: kmfex-platform
 * File Name: DebtBillPriInfoDto.java
 * Class Name: DebtBillPriInfoDto
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

import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ETermType;

/**
 * Class Name: DebtBillPriInfoDto
 * 
 * @author tingwang
 * 
 */

public class DebtBillPriInfoDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 单位名称简称
     */
    private String title;
    /**
     * 号次
     */
    private String serialNo;
    /**
     * 签发日期
     */
    private String signDt;
    /**
     * 借据编号
     */
    private String debtId;
    /**
     * 融资人户名
     */
    private String fncrAcctName;
    /**
     * 融资人账号
     */
    private String fncrAcctNo;
    /**
     * 融资人开户银行
     */
    private String fncrOpenBnk;
    /**
     * 投资人户名
     */
    private String invrAcctName;
    /**
     * 投资人账号
     */
    private String invrAcctNo;
    /**
     * 投资人开户银行
     */
    private String invrOpenBnk;
    /**
     * 融资额
     */
    private BigDecimal fncrAmt;
    /**
     * 大写融资额
     */
    private String fncrAmtStr;
    /**
     * 利息金额
     */
    private BigDecimal intrAmt;
    /**
     * 大写利息金额
     */
    private String intrAmtStr;
    /**
     * 利率
     */
    private BigDecimal intrRate;
    /**
     * 贷放到期日
     */
    private String debtEndDt;
    /**
     * 还款方式
     */
    private EPayMethodType payMethod;
    /**
     * 计息方式
     */
    private ETermType termType;
    /**
     * 计息方式字符串
     */
    private String termTypeStr;
    /**
     * 期限长度
     */
    private Integer termLength;
    /**
     * 备注
     */
    private String trxMemo;
    /**
     * 摘要
     */
    private String abstractStr;
    /**
     * 经办人
     */
    private String handler;
    /**
     * 复核人
     */
    private String checker;

    public String getSignDt() {
        return signDt;
    }

    public void setSignDt(String signDt) {
        this.signDt = signDt;
    }

    public String getDebtId() {
        return debtId;
    }

    public void setDebtId(String debtId) {
        this.debtId = debtId;
    }

    public String getFncrAcctName() {
        return fncrAcctName;
    }

    public void setFncrAcctName(String fncrAcctName) {
        this.fncrAcctName = fncrAcctName;
    }

    public String getFncrAcctNo() {
        return fncrAcctNo;
    }

    public void setFncrAcctNo(String fncrAcctNo) {
        this.fncrAcctNo = fncrAcctNo;
    }

    public String getFncrOpenBnk() {
        return fncrOpenBnk;
    }

    public void setFncrOpenBnk(String fncrOpenBnk) {
        this.fncrOpenBnk = fncrOpenBnk;
    }

    public String getInvrAcctName() {
        return invrAcctName;
    }

    public void setInvrAcctName(String invrAcctName) {
        this.invrAcctName = invrAcctName;
    }

    public String getInvrAcctNo() {
        return invrAcctNo;
    }

    public void setInvrAcctNo(String invrAcctNo) {
        this.invrAcctNo = invrAcctNo;
    }

    public String getInvrOpenBnk() {
        return invrOpenBnk;
    }

    public void setInvrOpenBnk(String invrOpenBnk) {
        this.invrOpenBnk = invrOpenBnk;
    }

    public BigDecimal getFncrAmt() {
        return fncrAmt;
    }

    public void setFncrAmt(BigDecimal fncrAmt) {
        this.fncrAmt = fncrAmt;
    }

    public String getFncrAmtStr() {
        return fncrAmtStr;
    }

    public void setFncrAmtStr(String fncrAmtStr) {
        this.fncrAmtStr = fncrAmtStr;
    }

    public BigDecimal getIntrAmt() {
        return intrAmt;
    }

    public void setIntrAmt(BigDecimal intrAmt) {
        this.intrAmt = intrAmt;
    }

    public String getIntrAmtStr() {
        return intrAmtStr;
    }

    public void setIntrAmtStr(String intrAmtStr) {
        this.intrAmtStr = intrAmtStr;
    }

    public BigDecimal getIntrRate() {
        return intrRate;
    }

    public void setIntrRate(BigDecimal intrRate) {
        this.intrRate = intrRate;
    }

    public String getDebtEndDt() {
        return debtEndDt;
    }

    public void setDebtEndDt(String debtEndDt) {
        this.debtEndDt = debtEndDt;
    }

    public EPayMethodType getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EPayMethodType payMethod) {
        this.payMethod = payMethod;
    }

    public ETermType getTermType() {
        return termType;
    }

    public void setTermType(ETermType termType) {
        this.termType = termType;
    }

    public Integer getTermLength() {
        return termLength;
    }

    public void setTermLength(Integer termLength) {
        this.termLength = termLength;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getAbstractStr() {
        return abstractStr;
    }

    public void setAbstractStr(String abstractStr) {
        this.abstractStr = abstractStr;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTermTypeStr() {
        return termTypeStr;
    }
	
    public void setTermTypeStr(String termTypeStr) {
        this.termTypeStr = termTypeStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
