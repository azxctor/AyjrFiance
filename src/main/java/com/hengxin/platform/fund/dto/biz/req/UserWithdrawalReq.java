/*
 * Project Name: kmfex-platform
 * File Name: WithdrawalReq.java
 * Class Name: WithdrawalReq
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

package com.hengxin.platform.fund.dto.biz.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hengxin.platform.common.constant.ApplicationConstant;

/**
 * 会员提现请求 Class Name: UserWithdrawalReq Description: TODO
 * 
 * @author tingwang
 * 
 */
@SuppressWarnings("serial")
public class UserWithdrawalReq implements Serializable {

    /**
     * 用户id
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class })
    private String userId;
    /**
     * 密码
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class, SignUserFromBank.class })
    private String password;
    /**
     * 提现金额
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class, SignUserFromBank.class })
    private BigDecimal amount;
    /**
     * 备注信息
     */
    private String memo;
    /**
     * 是否短信提醒
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class })
    private String remFlg;
    /**
     * 银行账户号
     */
    @NotNull(groups = { SignUserFromPlat.class, SignUserFromBank.class })
    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP, groups = { SignUserFromPlat.class,
            UnSignUserFromPlat.class, SignUserFromBank.class })
    private String bnkAcctNo;
    /**
     * 交易账号
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class, SignUserFromBank.class })
    private String acctNo;
    /**
     * 银行交易流水号
     */
    @NotNull(groups = { SignUserFromBank.class })
    private String bkSerial;
    /**
     * 合作方流水号
     */
    private String coSerial;
    /**
     * 证件类型
     */
    private String iDType;
    /**
     * 身份证号
     */
    private String iDNo;
    /**
     * 客户姓名
     */
    private String custName;
    /**
     * 交易批次号
     */
    private String jnlNo;
    /**
     * 当前操作用户
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class, SignUserFromBank.class })
    private String currentUser;
    /**
     * 当前日期
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromPlat.class, SignUserFromBank.class })
    private Date currentDate;
    
    /**
     * 银行账户名
     */
    private String bnkAcctName;
    
    /**
     * 开户银行
     */
    private String bnkOpenProv;
    
    /**
     * 用户名
     */
    private String userName;
    /**
     * 是否冲正交易
     */
    private boolean reverseFlag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRemFlg() {
        return remFlg;
    }

    public void setRemFlg(String remFlg) {
        this.remFlg = remFlg;
    }

    public String getBnkAcctNo() {
        return bnkAcctNo;
    }

    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return return the value of the var bkSerial
     */

    public String getBkSerial() {
        return bkSerial;
    }

    /**
     * @param bkSerial
     *            Set bkSerial value
     */

    public void setBkSerial(String bkSerial) {
        this.bkSerial = bkSerial;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    
    /**
    * @return return the value of the var coSerial
    */
    	
    public String getCoSerial() {
        return coSerial;
    }

    
    /**
    * @param coSerial Set coSerial value
    */
    	
    public void setCoSerial(String coSerial) {
        this.coSerial = coSerial;
    }

	public String getBnkOpenProv() {
		return bnkOpenProv;
	}

	public void setBnkOpenProv(String bnkOpenProv) {
		this.bnkOpenProv = bnkOpenProv;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    public String getiDNo() {
        return iDNo;
    }

    public void setiDNo(String iDNo) {
        this.iDNo = iDNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getJnlNo() {
        return jnlNo;
    }

    public void setJnlNo(String jnlNo) {
        this.jnlNo = jnlNo;
    }

    public boolean isReverseFlag() {
        return reverseFlag;
    }

    public void setReverseFlag(boolean reverseFlag) {
        this.reverseFlag = reverseFlag;
    }

    public String getiDType() {
        return iDType;
    }

    public void setiDType(String iDType) {
        this.iDType = iDType;
    }

    /**
     * 签约用户在银行提现
     * <strong>Fields and methods should be before inner classes.</strong>
     */
    public interface SignUserFromBank {
    }

    /**
     * 签约用户在平台提现
     * <strong>Fields and methods should be before inner classes.</strong>
     */
    public interface SignUserFromPlat {
    }

    /**
     * 非签约用户在平台提现
     * <strong>Fields and methods should be before inner classes.</strong>
     */
    public interface UnSignUserFromPlat {
    }

}
