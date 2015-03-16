/*
 * Project Name: kmfex-platform
 * File Name: AgencyPo.java
 * Class Name: AgencyPo
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.BaseMaintainablePo;

/**
 * Class Name: AgencyPo
 * 
 * @author shengzhou,chunlinwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_ORG_INFO")
public class Agency extends BaseMaintainablePo implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "O_NAME")
    private String name;

    @Column(name = "O_EMAIL")
    private String email;

    @Column(name = "O_MOBILE")
    private String mobile;

    @Column(name = "O_REGION_CD")
    private String region;

    @Column(name = "O_NATURE_CD")
    private String orgNature;

    @Column(name = "O_PHONE")
    private String orgPhone;

    @Column(name = "O_ADDRESS")
    private String orgAddress;

    @Column(name = "O_ZIP")
    private String orgPostCode;

    @Column(name = "O_FAX")
    private String fax;

    @Column(name = "O_QQ")
    private String orgQQ;

    @Column(name = "O_REP")
    private String orgLegalPerson;

    @Column(name = "O_REP_ID_CARD_NO")
    private String orgLegalPersonIdCardNumber;

    @Column(name = "O_REP_ID_CARD_IMG1")
    private String orgLegalPersonIdCardFrontImg;

    @Column(name = "O_REP_ID_CARD_IMG2")
    private String orgLegalPersonIdCardBackImg;

    @Column(name = "O_REP_PHONE")
    private String orgLeagalPersonPhone;

    @Column(name = "O_REP_MOBILE")
    private String orgLeagalPersonMobile;

    @Column(name = "O_REP_QQ")
    private String orgLeagalPersonQQ;

    @Column(name = "O_REP_EMAIL")
    private String orgLeagalPersonEmail;

    @Column(name = "O_CONTACT")
    private String contact;

    @Column(name = "O_CONTACT_PHONE")
    private String contactPhone;

    @Column(name = "O_CONTACT_MOBILE")
    private String contactMobile;

    @Column(name = "O_CONTACT_FAX")
    private String contactFax;

    @Column(name = "O_CONTACT_QQ")
    private String contactQQ;

    @Column(name = "O_CONTACT_EMAIL")
    private String contactEmail;

    @Column(name = "O_ORG_NO")
    private String orgNo;

    @Column(name = "O_ORG_NO_IMG")
    private String orgNoImg;

    @Column(name = "O_LICENCE_NO")
    private String licenceNo;

    @Column(name = "O_LICENCE_NO_IMG")
    private String licenceNoImg;

    @Column(name = "O_TAX_NO")
    private String taxNo;

    @Column(name = "O_TAX_NO_IMG")
    private String taxNoImg;

    /**
     * @return return the value of the var userId
     */

    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            Set userId value
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOrgNature() {
        return orgNature;
    }

    public void setOrgNature(String orgNature) {
        this.orgNature = orgNature;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgPostCode() {
        return orgPostCode;
    }

    public void setOrgPostCode(String orgPostCode) {
        this.orgPostCode = orgPostCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOrgQQ() {
        return orgQQ;
    }

    public void setOrgQQ(String orgQQ) {
        this.orgQQ = orgQQ;
    }

    public String getOrgLegalPerson() {
        return orgLegalPerson;
    }

    public void setOrgLegalPerson(String orgLegalPerson) {
        this.orgLegalPerson = orgLegalPerson;
    }

    public String getOrgLegalPersonIdCardNumber() {
        return orgLegalPersonIdCardNumber;
    }

    public void setOrgLegalPersonIdCardNumber(String orgLegalPersonIdCardNumber) {
        this.orgLegalPersonIdCardNumber = orgLegalPersonIdCardNumber;
    }

    public String getOrgLegalPersonIdCardFrontImg() {
        return orgLegalPersonIdCardFrontImg;
    }

    public void setOrgLegalPersonIdCardFrontImg(String orgLegalPersonIdCardFrontImg) {
        this.orgLegalPersonIdCardFrontImg = orgLegalPersonIdCardFrontImg;
    }

    public String getOrgLegalPersonIdCardBackImg() {
        return orgLegalPersonIdCardBackImg;
    }

    public void setOrgLegalPersonIdCardBackImg(String orgLegalPersonIdCardBackImg) {
        this.orgLegalPersonIdCardBackImg = orgLegalPersonIdCardBackImg;
    }

    public String getOrgLeagalPersonPhone() {
        return orgLeagalPersonPhone;
    }

    public void setOrgLeagalPersonPhone(String orgLeagalPersonPhone) {
        this.orgLeagalPersonPhone = orgLeagalPersonPhone;
    }

    public String getOrgLeagalPersonMobile() {
        return orgLeagalPersonMobile;
    }

    public void setOrgLeagalPersonMobile(String orgLeagalPersonMobile) {
        this.orgLeagalPersonMobile = orgLeagalPersonMobile;
    }

    public String getOrgLeagalPersonQQ() {
        return orgLeagalPersonQQ;
    }

    public void setOrgLeagalPersonQQ(String orgLeagalPersonQQ) {
        this.orgLeagalPersonQQ = orgLeagalPersonQQ;
    }

    public String getOrgLeagalPersonEmail() {
        return orgLeagalPersonEmail;
    }

    public void setOrgLeagalPersonEmail(String orgLeagalPersonEmail) {
        this.orgLeagalPersonEmail = orgLeagalPersonEmail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public String getContactQQ() {
        return contactQQ;
    }

    public void setContactQQ(String contactQQ) {
        this.contactQQ = contactQQ;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getOrgNoImg() {
        return orgNoImg;
    }

    public void setOrgNoImg(String orgNoImg) {
        this.orgNoImg = orgNoImg;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getLicenceNoImg() {
        return licenceNoImg;
    }

    public void setLicenceNoImg(String licenceNoImg) {
        this.licenceNoImg = licenceNoImg;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getTaxNoImg() {
        return taxNoImg;
    }

    public void setTaxNoImg(String taxNoImg) {
        this.taxNoImg = taxNoImg;
    }

}
