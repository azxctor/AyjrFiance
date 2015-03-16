/*
 * Project Name: kmfex-platform
 * File Name: FinancierApplicationPo.java
 * Class Name: FinancierApplicationPo
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
import javax.persistence.Table;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.enums.EOptionCategory;

/**
 * Class Name: FinancierApplicationPo
 * 
 * @author runchen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_FNCR_APPL")
public class FinancierApplication extends BaseApplication implements Serializable {

    @BusinessName("会员申请表第一页")
    @Column(name = "APP_FORM_IMG1")
    private String applicationImg1;

    @BusinessName("会员申请表第二页")
    @Column(name = "APP_FORM_IMG2")
    private String applicationImg2;

    @BusinessName(value = "征信记录", optionCategory=EOptionCategory.CREDIT_HIST)
    @Column(name = "CREDIT_HIST_CD")
    private String creditHist;

    @BusinessName(value = "经营所在地居住时间", optionCategory=EOptionCategory.RESIDENCE)
    @Column(name = "RESIDENCE_CD")
    private String residence;

    @BusinessName(value = "家庭状况", optionCategory=EOptionCategory.FAMILY_STATUS)
    @Column(name = "FAMILY_STATUS_CD")
    private String familyStatus;

    @BusinessName(value = "企业成立年限/核心管理层从业经营", optionCategory=EOptionCategory.ORG_AGE)
    @Column(name = "ORG_AGE_CD")
    private String orgAge;

    @BusinessName(value = "借款人家庭及企业资产总值", optionCategory=EOptionCategory.TOTAL_ASSETS)
    @Column(name = "TOTAL_ASSETS_CD")
    private String totalAssets;

    @BusinessName(value = "借款人家庭及企业资产负债比", optionCategory=EOptionCategory.DEBT_TYPE)
    @Column(name = "DEBT_RATIO_CD")
    private String debtRatio;

    @BusinessName(value = "授信期限内主营业务收入与期限内到期债务比", optionCategory=EOptionCategory.DEBT_RATIO)
    @Column(name = "INCOME_DEBT_RATIO_CD")
    private String incomeDebtRatio;

    @BusinessName(value = "单一客户集中度", optionCategory=EOptionCategory.CONCENTRATION_RATIO)
    @Column(name = "CONCENTRATION_RATIO_CD")
    private String concentra;

    @BusinessName(value = "利润率", optionCategory=EOptionCategory.PROFIT_RATIO)
    @Column(name = "PROFIT_RATIO_CD")
    private String profitRatio;

    @BusinessName(value = "主营业务收入增长率", optionCategory=EOptionCategory.REVENUE_GROWTH)
    @Column(name = "REVENUE_GROWTH_CD")
    private String revenueGrowth;

    @BusinessName(value = "主营业务下游客户分布的地域范围", optionCategory=EOptionCategory.CUSTOMER_DIST)
    @Column(name = "CUSTOMER_DIST_CD")
    private String customerDist;

    @BusinessName(value = "社会地位", optionCategory=EOptionCategory.SOCIAL_STATUS)
    @Column(name = "SOCIAL_STATUS_CD")
    private String socialStatus;

    @BusinessName("经办人")
    @Column(name = "AGENT")
    private String agent;

    @BusinessName("品牌名称")
    @Column(name = "O_BRAND")
    private String orgBrand;

    @BusinessName("组织机构代码")
    @Column(name = "O_ORG_NO")
    private String orgNumber;

    @BusinessName("组织机构代码扫描图")
    @Column(name = "O_ORG_NO_IMG")
    private String orgNumberFile;

    @BusinessName("营业执照代码")
    @Column(name = "O_LICENCE_NO")
    private String licenceNumber;

    @BusinessName("营业执照扫描图")
    @Column(name = "O_LICENCE_NO_IMG")
    private String licenceFile;

    @BusinessName("税务登记号码")
    @Column(name = "O_TAX_NO")
    private String faxRegNumber;

    @BusinessName("税务登记证扫描图")
    @Column(name = "O_TAX_NO_IMG")
    private String faxRegFile;

    @BusinessName("注册资本金")
    @Column(name = "O_REGIST_CAP")
    private String registCap;

    @BusinessName("股东信息及占股比例")
    @Column(name = "O_STOCKHOLDER")
    private String stockholder;

    @BusinessName(value = "会员等级", optionCategory = EOptionCategory.FINANCIER_LEVEL)
    @Column(name = "FNCR_LVL")
    private String financierLevel;

    @BusinessName("授权服务中心")
    @Column(name = "AUTHZD_CTR_ID")
    private String authCenterId;

    @Override
    public FinancierApplication clone() {
        FinancierApplication clone = null;
        try {
            clone = (FinancierApplication) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * @return return the value of the var applicationImg1
     */

    public String getApplicationImg1() {
        return applicationImg1;
    }

    /**
     * @param applicationImg1 Set applicationImg1 value
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
     * @param applicationImg2 Set applicationImg2 value
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
     * @param creditHist Set creditHist value
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
     * @param residence Set residence value
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
     * @param familyStatus Set familyStatus value
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
     * @param orgAge Set orgAge value
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
     * @param totalAssets Set totalAssets value
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
     * @param debtRatio Set debtRatio value
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
     * @param incomeDebtRatio Set incomeDebtRatio value
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
     * @param concentra Set concentra value
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
     * @param profitRatio Set profitRatio value
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
     * @param revenueGrowth Set revenueGrowth value
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
     * @param customerDist Set customerDist value
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
     * @param socialStatus Set socialStatus value
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
     * @param agent Set agent value
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
     * @param orgBrand Set orgBrand value
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
     * @param orgNumber Set orgNumber value
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
     * @param orgNumberFile Set orgNumberFile value
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
     * @param licenceNumber Set licenceNumber value
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
     * @param licenceFile Set licenceFile value
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
     * @param faxRegNumber Set faxRegNumber value
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
     * @param faxRegFile Set faxRegFile value
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
     * @param registCap Set registCap value
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
     * @param stockholder Set stockholder value
     */

    public void setStockholder(String stockholder) {
        this.stockholder = stockholder;
    }

    public String getFinancierLevel() {
        return financierLevel;
    }

    /**
     * @param financierLevel
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
