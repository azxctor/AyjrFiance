/*
 * Project Name: kmfex-platform
 * File Name: RechargeReq.java
 * Class Name: RechargeReq
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
 * 用户充值请求 .
 * 
 * @author tingwang
 * 
 */
@SuppressWarnings("serial")
public class UserRechargeReq implements Serializable {

    /**
     * 用户编号.
     */
    @NotNull(groups = { SignUserFromBank.class, SignUserFromPlat.class})
    private String userId;
    
    /**
     * 用户名.
     */
    private String userName;
    /**
     * 充值金额.
     */
    @NotNull(groups = { SignUserFromBank.class, SignUserFromPlat.class, UnSignUserFromBank.class })
    private BigDecimal amount;
    /**
     * 银行交易流水号.
     */
    @NotNull(groups = { SignUserFromBank.class, UnSignUserFromBank.class })
    private String bkSerial;
    /**
     * 银行账户号.
     */
    @NotNull(groups = { SignUserFromBank.class, SignUserFromPlat.class, UnSignUserFromBank.class })
    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP, groups = { SignUserFromBank.class, SignUserFromPlat.class, UnSignUserFromBank.class })
    private String bnkAcctNo;
    /**
     * 交易账户号.
     */
    @NotNull(groups = { SignUserFromBank.class, UnSignUserFromBank.class })
    private String acctNo;
    /**
     * 交易账户密码.
     */
    @NotNull(groups = { SignUserFromBank.class })
    private String password;
    /**
     * 证件类型.
     */
    private String iDType;
    /**
     * 身份证号.
     */
    private String iDNo;
    /**
     * 客户姓名.
     */
    private String custName;
    /**
     * 交易批次号.
     */
    @NotNull(groups = { SignUserFromBank.class })
    private String jnlNo;
    /**
     * 当前操作用户.
     */
    @NotNull(groups = { SignUserFromBank.class, SignUserFromPlat.class, UnSignUserFromBank.class })
    private String currentUser;
    /**
     * 当前时间.
     */
    @NotNull(groups = { SignUserFromPlat.class, UnSignUserFromBank.class })
    private Date currentDate;
    /**
     * 备注信息.
     */
    private String memo;
    /**
     * 是否重发交易.
     */
    private boolean anewFlag;

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

    public String getBkSerial() {
        return bkSerial;
    }

    public void setBkSerial(String bkSerial) {
        this.bkSerial = bkSerial;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getBnkAcctNo() {
        return bnkAcctNo;
    }

    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJnlNo() {
        return jnlNo;
    }
    
    public void setJnlNo(String jnlNo) {
        this.jnlNo = jnlNo;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
	
    public Date getCurrentDate() {
        return currentDate;
    }
	
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
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

    public boolean isAnewFlag() {
        return anewFlag;
    }

    public void setAnewFlag(boolean anewFlag) {
        this.anewFlag = anewFlag;
    }

    public String getiDType() {
        return iDType;
    }

    public void setiDType(String iDType) {
        this.iDType = iDType;
    }
    
    /**
     * 签约用户在银行充值
     */
    public interface SignUserFromBank {
    }

    /**
     * 签约用户在平台充值
     */
    public interface SignUserFromPlat {
    }

    /**
     * 非签约用户在银行充值
     */
    public interface UnSignUserFromBank {
    }
}
