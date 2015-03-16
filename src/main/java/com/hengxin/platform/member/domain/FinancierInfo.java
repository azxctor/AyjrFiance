/*
 * Project Name: kmfex-platform
 * File Name: FinancierInfoPo.java
 * Class Name: FinancierInfoPo
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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class Name: FinancierInfoPo
 * 
 * @author runchen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_FNCR_INFO")
public class FinancierInfo implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CREATE_OPID")
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", updatable = false)
    private Date createTs;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;

    @Column(name = "APP_FORM_IMG1")
    private String applicationImg1;

    @Column(name = "APP_FORM_IMG2")
    private String applicationImg2;

    @Column(name = "CREDIT_HIST_CD")
    private String creditHist;

    @Column(name = "RESIDENCE_CD")
    private String residence;

    @Column(name = "FAMILY_STATUS_CD")
    private String familyStatus;

    @Column(name = "ORG_AGE_CD")
    private String orgAge;

    @Column(name = "TOTAL_ASSETS_CD")
    private String totalAssets;

    @Column(name = "DEBT_RATIO_CD")
    private String debtRatio;

    @Column(name = "INCOME_DEBT_RATIO_CD")
    private String incomeDebtRatio;

    @Column(name = "CONCENTRATION_RATIO_CD")
    private String concentra;

    @Column(name = "PROFIT_RATIO_CD")
    private String profitRatio;

    @Column(name = "REVENUE_GROWTH_CD")
    private String revenueGrowth;

    @Column(name = "CUSTOMER_DIST_CD")
    private String customerDist;

    @Column(name = "SOCIAL_STATUS_CD")
    private String socialStatus;

    @Column(name = "AGENT")
    private String agent;

    @Column(name = "O_BRAND")
    private String orgBrand;

    @Column(name = "O_ORG_NO")
    private String orgNumber;

    @Column(name = "O_ORG_NO_IMG")
    private String orgNumberFile;

    @Column(name = "O_LICENCE_NO")
    private String licenceNumber;

    @Column(name = "O_LICENCE_NO_IMG")
    private String licenceFile;

    @Column(name = "O_TAX_NO")
    private String faxRegNumber;

    @Column(name = "O_TAX_NO_IMG")
    private String faxRegFile;

    @Column(name = "O_REGIST_CAP")
    private String registCap;

    @Column(name = "O_STOCKHOLDER")
    private String stockholder;

    @Column(name = "FNCR_LVL")
    private String financierLevel;

    @Column(name = "AUTHZD_CTR_ID")
    private String authCenterId;

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

    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     *            Set creator value
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    public Date getLastMntTs() {
        return lastMntTs;
    }

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * @return return the value of the var applicationImg1
     */

    public String getApplicationImg1() {
        return applicationImg1;
    }

    /**
     * @param applicationImg1
     *            Set applicationImg1 value
     */

    public void setApplicationImg1(String applicationImg1) {
        this.applicationImg1 = applicationImg1;
    }

    /**
     * @return return the value of the var applicationImg2
     */

    public String getApplicationImg2() {
        return applicationImg2;
    }

    /**
     * @param applicationImg2
     *            Set applicationImg2 value
     */

    public void setApplicationImg2(String applicationImg2) {
        this.applicationImg2 = applicationImg2;
    }

    /**
     * @return return the value of the var creditHist
     */

    public String getCreditHist() {
        return creditHist;
    }

    /**
     * @param creditHist
     *            Set creditHist value
     */

    public void setCreditHist(String creditHist) {
        this.creditHist = creditHist;
    }

    /**
     * @return return the value of the var residence
     */

    public String getResidence() {
        return residence;
    }

    /**
     * @param residence
     *            Set residence value
     */

    public void setResidence(String residence) {
        this.residence = residence;
    }

    /**
     * @return return the value of the var familyStatus
     */

    public String getFamilyStatus() {
        return familyStatus;
    }

    /**
     * @param familyStatus
     *            Set familyStatus value
     */

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    /**
     * @return return the value of the var orgAge
     */

    public String getOrgAge() {
        return orgAge;
    }

    /**
     * @param orgAge
     *            Set orgAge value
     */

    public void setOrgAge(String orgAge) {
        this.orgAge = orgAge;
    }

    /**
     * @return return the value of the var totalAssets
     */

    public String getTotalAssets() {
        return totalAssets;
    }

    /**
     * @param totalAssets
     *            Set totalAssets value
     */

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    /**
     * @return return the value of the var debtRatio
     */

    public String getDebtRatio() {
        return debtRatio;
    }

    /**
     * @param debtRatio
     *            Set debtRatio value
     */

    public void setDebtRatio(String debtRatio) {
        this.debtRatio = debtRatio;
    }

    /**
     * @return return the value of the var incomeDebtRatio
     */

    public String getIncomeDebtRatio() {
        return incomeDebtRatio;
    }

    /**
     * @param incomeDebtRatio
     *            Set incomeDebtRatio value
     */

    public void setIncomeDebtRatio(String incomeDebtRatio) {
        this.incomeDebtRatio = incomeDebtRatio;
    }

    /**
     * @return return the value of the var concentra
     */

    public String getConcentra() {
        return concentra;
    }

    /**
     * @param concentra
     *            Set concentra value
     */

    public void setConcentra(String concentra) {
        this.concentra = concentra;
    }

    /**
     * @return return the value of the var profitRatio
     */

    public String getProfitRatio() {
        return profitRatio;
    }

    /**
     * @param profitRatio
     *            Set profitRatio value
     */

    public void setProfitRatio(String profitRatio) {
        this.profitRatio = profitRatio;
    }

    /**
     * @return return the value of the var revenueGrowth
     */

    public String getRevenueGrowth() {
        return revenueGrowth;
    }

    /**
     * @param revenueGrowth
     *            Set revenueGrowth value
     */

    public void setRevenueGrowth(String revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    /**
     * @return return the value of the var customerDist
     */

    public String getCustomerDist() {
        return customerDist;
    }

    /**
     * @param customerDist
     *            Set customerDist value
     */

    public void setCustomerDist(String customerDist) {
        this.customerDist = customerDist;
    }

    /**
     * @return return the value of the var socialStatus
     */

    public String getSocialStatus() {
        return socialStatus;
    }

    /**
     * @param socialStatus
     *            Set socialStatus value
     */

    public void setSocialStatus(String socialStatus) {
        this.socialStatus = socialStatus;
    }

    /**
     * @return return the value of the var agent
     */

    public String getAgent() {
        return agent;
    }

    /**
     * @param agent
     *            Set agent value
     */

    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * @return return the value of the var orgBrand
     */

    public String getOrgBrand() {
        return orgBrand;
    }

    /**
     * @param orgBrand
     *            Set orgBrand value
     */

    public void setOrgBrand(String orgBrand) {
        this.orgBrand = orgBrand;
    }

    /**
     * @return return the value of the var orgNumber
     */

    public String getOrgNumber() {
        return orgNumber;
    }

    /**
     * @param orgNumber
     *            Set orgNumber value
     */

    public void setOrgNumber(String orgNumber) {
        this.orgNumber = orgNumber;
    }

    /**
     * @return return the value of the var orgNumberFile
     */

    public String getOrgNumberFile() {
        return orgNumberFile;
    }

    /**
     * @param orgNumberFile
     *            Set orgNumberFile value
     */

    public void setOrgNumberFile(String orgNumberFile) {
        this.orgNumberFile = orgNumberFile;
    }

    /**
     * @return return the value of the var licenceNumber
     */

    public String getLicenceNumber() {
        return licenceNumber;
    }

    /**
     * @param licenceNumber
     *            Set licenceNumber value
     */

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    /**
     * @return return the value of the var licenceFile
     */

    public String getLicenceFile() {
        return licenceFile;
    }

    /**
     * @param licenceFile
     *            Set licenceFile value
     */

    public void setLicenceFile(String licenceFile) {
        this.licenceFile = licenceFile;
    }

    /**
     * @return return the value of the var faxRegNumber
     */

    public String getFaxRegNumber() {
        return faxRegNumber;
    }

    /**
     * @param faxRegNumber
     *            Set faxRegNumber value
     */

    public void setFaxRegNumber(String faxRegNumber) {
        this.faxRegNumber = faxRegNumber;
    }

    /**
     * @return return the value of the var faxRegFile
     */

    public String getFaxRegFile() {
        return faxRegFile;
    }

    /**
     * @param faxRegFile
     *            Set faxRegFile value
     */

    public void setFaxRegFile(String faxRegFile) {
        this.faxRegFile = faxRegFile;
    }

    /**
     * @return return the value of the var registCap
     */

    public String getRegistCap() {
        return registCap;
    }

    /**
     * @param registCap
     *            Set registCap value
     */

    public void setRegistCap(String registCap) {
        this.registCap = registCap;
    }

    /**
     * @return return the value of the var stockholder
     */

    public String getStockholder() {
        return stockholder;
    }

    /**
     * @param stockholder
     *            Set stockholder value
     */

    public void setStockholder(String stockholder) {
        this.stockholder = stockholder;
    }

    /**
     * @return return the value of the var financierLevel
     */

    public String getFinancierLevel() {
        return financierLevel;
    }

    /**
     * @param financierLevel
     *            Set financierLevel value
     */
    public void setFinancierLevel(String financierLevel) {
        this.financierLevel = financierLevel;
    }

    /**
     * @return return the value of the var authCenterId
     */

    public String getAuthCenterId() {
        return authCenterId;
    }

    /**
     * @param authCenterId
     *            Set authCenterId value
     */

    public void setAuthCenterId(String authCenterId) {
        this.authCenterId = authCenterId;
    }

}
