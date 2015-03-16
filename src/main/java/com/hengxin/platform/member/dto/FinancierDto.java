/*
 * Project Name: kmfex-platform
 * File Name: FinancierInfo.java
 * Class Name: FinancierInfo
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

package com.hengxin.platform.member.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.service.validator.OrganizationCodeUniqueCheck;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinancierInfo
 *
 * @author runchen
 *
 */
@OrganizationCodeUniqueCheck(orgId = "userId", orgCode = "orgNumber", type = "P", groups = { SubmitOrgCode.class })
public class FinancierDto extends BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@NotEmpty(groups = { SubmitFinance.class, SubmitOrgFinance.class }, message = "{member.error.field.empty}")
    private String applicationImg1;

//  @NotEmpty(groups = { SubmitFinance.class, SubmitOrgFinance.class }, message = "{member.error.field.empty}")
    private String applicationImg2;

    @JsonProperty("credit_hist")
    @OptionCategory(EOptionCategory.CREDIT_HIST)
    private DynamicOption creditHist;

    @JsonProperty("residence")
    @OptionCategory(EOptionCategory.RESIDENCE)
    private DynamicOption residence;

    @JsonProperty("family_status")
    @OptionCategory(EOptionCategory.FAMILY_STATUS)
    private DynamicOption familyStatus;

    @JsonProperty("found_age")
    @OptionCategory(EOptionCategory.ORG_AGE)
    private DynamicOption orgAge;

    @JsonProperty("total_assert")
    @OptionCategory(EOptionCategory.TOTAL_ASSETS)
    private DynamicOption totalAssets;

    @JsonProperty("debt_radio")
    @OptionCategory(EOptionCategory.DEBT_RATIO)
    private DynamicOption debtRatio;

    @JsonProperty("income_debt_ratio")
    @OptionCategory(EOptionCategory.INCOME_DEBT_RATIO)
    private DynamicOption incomeDebtRatio;

    @JsonProperty("concentra")
    @OptionCategory(EOptionCategory.CONCENTRATION_RATIO)
    private DynamicOption concentra;

    @JsonProperty("profit_ratio")
    @OptionCategory(EOptionCategory.PROFIT_RATIO)
    private DynamicOption profitRatio;

    @JsonProperty("revenue_growth")
    @OptionCategory(EOptionCategory.REVENUE_GROWTH)
    private DynamicOption revenueGrowth;

    @JsonProperty("customer_dist")
    @OptionCategory(EOptionCategory.CUSTOMER_DIST)
    private DynamicOption customerDist;

    @JsonProperty("social_status")
    @OptionCategory(EOptionCategory.SOCIAL_STATUS)
    private DynamicOption socialStatus;

    @JsonProperty("brand")
    private String orgBrand;

//    @NotEmpty(groups = { SubmitOrgFinance.class }, message = "{member.error.field.empty}")
    @JsonProperty("org_number")
    private String orgNumber;

    @JsonProperty("org_number_file")
    private String orgNumberFile;

    @NotEmpty(groups = { SubmitOrgFinance.class }, message = "{member.error.field.empty}")
    @JsonProperty("licence_number")
    private String licenceNumber;

    @NotEmpty(groups = { SubmitOrgFinance.class }, message = "{member.error.field.empty}")
    @JsonProperty("licence_file")
    private String licenceFile;

    @JsonProperty("fax_register_number")
    private String faxRegNumber;

    @JsonProperty("fax_register_file")
    private String faxRegFile;

    @Pattern(regexp = ApplicationConstant.NUMBER_REGEXP, groups = { SubmitOrgFinance.class }, message = "{member.error.registrationCapital.invaild}")
    @JsonProperty("register_capital")
    private String registCap;

    @JsonProperty("stockholder")
    private String stockholder;

    @JsonProperty("auth_center")
    private AgencyDto authCenter;

    @JsonProperty("agent")
    private String agent; // 经办人

    @OptionCategory(EOptionCategory.FINANCIER_LEVEL)
    private DynamicOption financierLevel;

    // 页面的状态
    private EFormMode formMode = EFormMode.VIEW;

    // 是否已有基本信息
    private boolean hasMemberInfo = true;

    // 是否可编辑会员等级
    private boolean fixedLevel = true;

    private boolean canEdit;

    // 是否可编辑授权服务中心和经办人
    private boolean fixedAgency;

    // 只有代注册时显示授权服务中心和经办人
    private boolean showServiceCenter;

    //是否显示用户申请表.
    private MaskFixedDto fixedStatus = new MaskFixedDto();

    private FinancierDto oldFinancier;

    private boolean rejectLastTime;

    private String  applicationImg1Url;

    private String  applicationImg2Url;

    private String licenceFileUrl;

    private String orgNumberFileUrl;

    private String faxRegFileUrl;

    public boolean isEditMode() {
        return formMode == EFormMode.EDIT;
    }

    public boolean isViewMode() {
        return formMode == EFormMode.VIEW;
    }

    public boolean isPendding() {
        return getStatus() == EApplicationStatus.PENDDING;
    }

    public boolean isUncommit() {
        return getStatus() == null || getStatus() == EApplicationStatus.NULL
                || getStatus() == EApplicationStatus.UNCOMMITTED;
    }

    public boolean hasLevel(){
        return financierLevel != null && StringUtils.isNotBlank(financierLevel.getCode());
    }

    @JsonProperty("application_img_first_url")
    public String getApplicationImg1Url() {
        if (StringUtils.isNotBlank(applicationImg1)
                && !SecurityContext.getInstance().cannotViewApplicationForm(this.getUserId(), inCanViewPage)) {
            return FileUploadUtil.getTempFolder() + applicationImg1;
        }
        return "";
    }

    @JsonProperty("application_img_second_url")
    public String getApplicationImg2Url() {
        if (StringUtils.isNotBlank(applicationImg2)
                && !SecurityContext.getInstance().cannotViewApplicationForm(this.getUserId(), inCanViewPage)) {
            return FileUploadUtil.getTempFolder() + applicationImg2;
        }
        return "";
    }

    @JsonProperty("org_number_file_url")
    public String getOrgNumberFileUrl() {
        if (StringUtils.isNotBlank(orgNumberFile)) {
            return FileUploadUtil.getTempFolder() + orgNumberFile;
        }
        return "";
    }

    @JsonProperty("fax_register_file_url")
    public String getFaxRegFileUrl() {
        if (StringUtils.isNotBlank(faxRegFile)) {
            return FileUploadUtil.getTempFolder() + faxRegFile;
        }
        return "";
    }

    @JsonProperty("licence_file_url")
    public String getLicenceFileUrl() {
        if (StringUtils.isNotBlank(licenceFile)) {
            return FileUploadUtil.getTempFolder() + licenceFile;
        }
        return "";
    }

    /**
     * @return return the value of the var applicationImg1
     */

    public String getApplicationImg1() {
        return applicationImg1;
    }
    
    /**
     * @return return the mask value of the var applicationImg1
     */
    @JsonProperty("application_img_first")
    public String getMaskApplicationImg1() {
        if (SecurityContext.getInstance().cannotViewApplicationForm(this.getUserId(), inCanViewPage)) {
            applicationImg1 = "";
        }
        return applicationImg1;
    }

    /**
     * @param applicationImg1 Set applicationImg1 value
     */
    @JsonProperty("application_img_first")
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
     * @return return the mask value of the var applicationImg2
     */
    @JsonProperty("application_img_second")
    public String getMaskApplicationImg2() {
        if (SecurityContext.getInstance().cannotViewApplicationForm(this.getUserId(), inCanViewPage)) {
            applicationImg2 = "";
        }
        return applicationImg2;
    }

    /**
     * @param applicationImg2 Set applicationImg2 value
     */
    @JsonProperty("application_img_second")
    public void setApplicationImg2(String applicationImg2) {
        this.applicationImg2 = applicationImg2;
    }

    /**
     * @return return the value of the var creditHist
     */

    public DynamicOption getCreditHist() {
        return creditHist;
    }

    /**
     * @param creditHist Set creditHist value
     */

    public void setCreditHist(DynamicOption creditHist) {
        this.creditHist = creditHist;
    }

    /**
     * @return return the value of the var residence
     */

    public DynamicOption getResidence() {
        return residence;
    }

    /**
     * @param residence Set residence value
     */

    public void setResidence(DynamicOption residence) {
        this.residence = residence;
    }

    /**
     * @return return the value of the var familyStatus
     */

    public DynamicOption getFamilyStatus() {
        return familyStatus;
    }

    /**
     * @param familyStatus Set familyStatus value
     */

    public void setFamilyStatus(DynamicOption familyStatus) {
        this.familyStatus = familyStatus;
    }

    /**
     * @return return the value of the var orgAge
     */

    public DynamicOption getOrgAge() {
        return orgAge;
    }

    /**
     * @param orgAge Set orgAge value
     */

    public void setOrgAge(DynamicOption orgAge) {
        this.orgAge = orgAge;
    }

    /**
     * @return return the value of the var totalAssets
     */

    public DynamicOption getTotalAssets() {
        return totalAssets;
    }

    /**
     * @param totalAssets Set totalAssets value
     */

    public void setTotalAssets(DynamicOption totalAssets) {
        this.totalAssets = totalAssets;
    }

    /**
     * @return return the value of the var debtRatio
     */

    public DynamicOption getDebtRatio() {
        return debtRatio;
    }

    /**
     * @param debtRatio Set debtRatio value
     */

    public void setDebtRatio(DynamicOption debtRatio) {
        this.debtRatio = debtRatio;
    }

    /**
     * @return return the value of the var incomeDebtRatio
     */

    public DynamicOption getIncomeDebtRatio() {
        return incomeDebtRatio;
    }

    /**
     * @param incomeDebtRatio Set incomeDebtRatio value
     */

    public void setIncomeDebtRatio(DynamicOption incomeDebtRatio) {
        this.incomeDebtRatio = incomeDebtRatio;
    }

    /**
     * @return return the value of the var concentra
     */

    public DynamicOption getConcentra() {
        return concentra;
    }

    /**
     * @param concentra Set concentra value
     */

    public void setConcentra(DynamicOption concentra) {
        this.concentra = concentra;
    }

    /**
     * @return return the value of the var profitRatio
     */

    public DynamicOption getProfitRatio() {
        return profitRatio;
    }

    /**
     * @param profitRatio Set profitRatio value
     */

    public void setProfitRatio(DynamicOption profitRatio) {
        this.profitRatio = profitRatio;
    }

    /**
     * @return return the value of the var revenueGrowth
     */

    public DynamicOption getRevenueGrowth() {
        return revenueGrowth;
    }

    /**
     * @param revenueGrowth Set revenueGrowth value
     */

    public void setRevenueGrowth(DynamicOption revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    /**
     * @return return the value of the var customerDist
     */

    public DynamicOption getCustomerDist() {
        return customerDist;
    }

    /**
     * @param customerDist Set customerDist value
     */

    public void setCustomerDist(DynamicOption customerDist) {
        this.customerDist = customerDist;
    }

    /**
     * @return return the value of the var socialStatus
     */

    public DynamicOption getSocialStatus() {
        return socialStatus;
    }

    /**
     * @param socialStatus Set socialStatus value
     */

    public void setSocialStatus(DynamicOption socialStatus) {
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

    /**
     * @return return the value of the var formMode
     */

    public EFormMode getFormMode() {
        return formMode;
    }

    /**
     * @param formMode
     *            Set formMode value
     */

    public void setFormMode(EFormMode formMode) {
        this.formMode = formMode;
    }

    /**
     * @return return the value of the var hasMemberInfo
     */

    public boolean isHasMemberInfo() {
        return hasMemberInfo;
    }

    /**
     * @param hasMemberInfo
     *            Set hasMemberInfo value
     */

    public void setHasMemberInfo(boolean hasMemberInfo) {
        this.hasMemberInfo = hasMemberInfo;
    }

    /**
     * @return return the value of the var fixedLevel
     */

    public boolean isFixedLevel() {
        return fixedLevel;
    }

    /**
     * @param fixedLevel
     *            Set fixedLevel value
     */

    public void setFixedLevel(boolean fixedLevel) {
        this.fixedLevel = fixedLevel;
    }

    public DynamicOption getFinancierLevel() {
        return financierLevel;
    }

    public void setFinancierLevel(DynamicOption financierLevel) {
        this.financierLevel = financierLevel;
    }

    /**
     * @return return the value of the var authCenter
     */

    public AgencyDto getAuthCenter() {
        return authCenter;
    }

    /**
     * @param authCenter
     *            Set authCenter value
     */

    public void setAuthCenter(AgencyDto authCenter) {
        this.authCenter = authCenter;
    }

    public boolean isShowServiceCenter() {
        return showServiceCenter;
    }

    public void setShowServiceCenter(boolean showServiceCenter) {
        this.showServiceCenter = showServiceCenter;
    }

    public boolean isFixedAgency() {
        return fixedAgency;
    }

    public void setFixedAgency(boolean fixedAgency) {
        this.fixedAgency = fixedAgency;
    }


    /**
    * @return return the value of the var canEdit
    */

    public boolean isCanEdit() {
        return canEdit;
    }


    /**
    * @param canEdit Set canEdit value
    */

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    /**
     * @return return the value of the var oldFinancier
     */

    public FinancierDto getOldFinancier() {
        return oldFinancier;
    }

    /**
     * @param oldFinancier
     *            Set oldFinancier value
     */

    public void setOldFinancier(FinancierDto oldFinancier) {
        this.oldFinancier = oldFinancier;
    }

    public void setApplicationImg1Url(String applicationImg1Url) {
        this.applicationImg1Url = applicationImg1Url;
    }

    public void setLicenceFileUrl(String licenceFileUrl) {
        this.licenceFileUrl = licenceFileUrl;
    }

    public void setOrgNumberFileUrl(String orgNumberFileUrl) {
        this.orgNumberFileUrl = orgNumberFileUrl;
    }

    public void setFaxRegFileUrl(String faxRegFileUrl) {
        this.faxRegFileUrl = faxRegFileUrl;
    }

    public void setApplicationImg2Url(String applicationImg2Url) {
        this.applicationImg2Url = applicationImg2Url;
    }


    /**
    * @return return the value of the var rejectLastTime
    */

    public boolean isRejectLastTime() {
        return rejectLastTime;
    }


    /**
    * @param rejectLastTime Set rejectLastTime value
    */

    public void setRejectLastTime(boolean rejectLastTime) {
        this.rejectLastTime = rejectLastTime;
    }

	/**
	 * @return the fixedStatus
	 */
	public MaskFixedDto getFixedStatus() {
		return fixedStatus;
	}

	/**
	 * @param fixedStatus the fixedStatus to set
	 */
	public void setFixedStatus(MaskFixedDto fixedStatus) {
		this.fixedStatus = fixedStatus;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface SubmitFinance {

    }

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface SubmitOrgFinance {

    }
    
	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface SubmitOrgCode {
    	
    }
}
