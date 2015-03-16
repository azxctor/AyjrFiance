
/*
* Project Name: kmfex-platform
* File Name: ProtocolRequest.java
* Class Name: ProtocolRequest
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
	
package com.hengxin.platform.bnkdocking.dto.biz.req;




/**
 * Class Name: ProtocolReq
 * Description: TODO
 * @author congzhou
 *
 */

public class ProtocolReq {
    /**
     * 银行流水号
     */
    private String bankSerial;
    /**
     * 交易批次号
     */
    private String batchNo;
    /**
     * 银行账号
     */
    private String bankAccount;
    /**
     * 交易账号
     */
    private String fundAccount;
    /**
     * 交易账号密码
     */
    private String pinBlock;
    /**
     * 证件类别
     */
    private String iDType;
    /**
     * 证件号码
     */
    private String iDNo;
    /**
     * 客户名称
     */
    private String custName;
    /**
     * 客户性别
     */
    private String sex;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 传真号码
     */
    private String fax;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 邮政编码
     */
    private String postCode;
    /**
     * 通讯地址
     */
    private String address;
    public String getBankSerial() {
        return bankSerial;
    }
    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    public String getFundAccount() {
        return fundAccount;
    }
    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
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
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return "ProtocolReq [bankSerial=" + bankSerial + ", batchNo=" + batchNo + ", bankAccount=" + bankAccount
                + ", fundAccount=" + fundAccount + ", iDNo=" + iDNo + ", custName=" + custName + ", sex=" + sex
                + ", phone=" + phone + ", mobile=" + mobile + ", fax=" + fax + ", email=" + email + ", postCode="
                + postCode + ", address=" + address + "]";
    }
    public String getPinBlock() {
        return pinBlock;
    }
    public void setPinBlock(String pinBlock) {
        this.pinBlock = pinBlock;
    }
    public String getiDType() {
        return iDType;
    }
    public void setiDType(String iDType) {
        this.iDType = iDType;
    }
    
  
}
