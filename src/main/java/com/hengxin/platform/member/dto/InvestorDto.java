/*
 * Project Name: kmfex-platform
 * File Name: InvestorInfo.java
 * Class Name: InvestorInfo
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

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: InvestorInfo
 *
 * @author runchen
 *
 */
public class InvestorDto extends BaseApplicationDto implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//    @NotEmpty(groups = { SubmitInvest.class }, message = "{member.error.field.empty}")
    private String applicationImg1;

//    @NotEmpty(groups = { SubmitInvest.class }, message = "{member.error.field.empty}")
    private String applicationImg2;

    @JsonProperty("agent")
    private String agent; // 经办人

    @JsonProperty("auth_center")
    private AgencyDto authCenter; // 授权服务中心

    @JsonProperty("agentName")
    private String agentName;  // 介绍人

    @OptionCategory(EOptionCategory.INVESTOR_LEVEL)
    private DynamicOption investorLevel;

    // 页面的状态
    private EFormMode formMode = EFormMode.VIEW;

    // 是否已有基本信息
    private boolean hasMemberInfo = true;

    // 是否可编辑授权服务中心和经办人
    private boolean fixedAgency = true;

    // 是否可编辑会员等级
    private boolean fixedLevel = true;

    //是否显示用户申请表.
    private MaskFixedDto fixedStatus = new MaskFixedDto();

    private boolean canEdit;

    private boolean registerByAgentScenario;

    private InvestorDto oldInvestor;

    private boolean rejectLastTime;

    private String applicationImg1Url;

    private String applicationImg2Url;

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

    public boolean hasLevel() {
        return investorLevel != null && StringUtils.isNotBlank(investorLevel.getCode());
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

    /**
     * @return return the value of the var ApplicationImg1
     */

    public String getApplicationImg1() {
        return applicationImg1;
    }
    
    /**
     * @return return the mask value of the var ApplicationImg1
     */
    @JsonProperty("application_img_first")
    public String getMaskApplicationImg1() {
        if (SecurityContext.getInstance().cannotViewApplicationForm(this.getUserId(), inCanViewPage)) {
            applicationImg1 = "";
        }
        return applicationImg1;
    }

    /**
     * @param ApplicationImg1
     *            Set ApplicationImg1 value
     */
    @JsonProperty("application_img_first")
    public void setApplicationImg1(String ApplicationImg1) {
        this.applicationImg1 = ApplicationImg1;
    }

    /**
     * @return return the value of the var ApplicationImg2
     */

    public String getApplicationImg2() {
        return applicationImg2;
    }
    
    /**
     * @return return the mask value of the var ApplicationImg2
     */
    @JsonProperty("application_img_second")
    public String getMaskApplicationImg2() {
        if (SecurityContext.getInstance().cannotViewApplicationForm(this.getUserId(), inCanViewPage)) {
            applicationImg2 = "";
        }
        return applicationImg2;
    }

    /**
     * @param applicationImg2
     *            Set ApplicationImg2 value
     */
    @JsonProperty("application_img_second")
    public void setApplicationImg2(String applicationImg2) {
        this.applicationImg2 = applicationImg2;
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
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public AgencyDto getAuthCenter() {
        return authCenter;
    }

    public void setAuthCenter(AgencyDto authCenter) {
        this.authCenter = authCenter;
    }

    /**
     * @return return the value of the var formMode
     */

    public EFormMode getFormMode() {
        return formMode;
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
     * @param formMode
     *            Set formMode value
     */

    public void setFormMode(EFormMode formMode) {
        this.formMode = formMode;
    }

    /**
     * @return return the value of the var fixedAgency
     */

    public boolean isFixedAgency() {
        return fixedAgency;
    }

    /**
     * @param fixedAgency
     *            Set fixedAgency value
     */

    public void setFixedAgency(boolean fixedAgency) {
        this.fixedAgency = fixedAgency;
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

    public DynamicOption getInvestorLevel() {
        return investorLevel;
    }

    public void setInvestorLevel(DynamicOption investorLevel) {
        this.investorLevel = investorLevel;
    }

    /**
     * @return return the value of the var canEdit
     */

    public boolean isCanEdit() {
        return canEdit;
    }

    /**
     * @param canEdit
     *            Set canEdit value
     */

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public InvestorDto getOldInvestor() {
        return oldInvestor;
    }

    public void setOldInvestor(InvestorDto oldInvestor) {
        this.oldInvestor = oldInvestor;
    }

    public void setApplicationImg1Url(String applicationImg1Url) {
        this.applicationImg1Url = applicationImg1Url;
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
     * @param rejectLastTime
     *            Set rejectLastTime value
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
	 * @return the registerByAgentScenario
	 */
	public boolean isRegisterByAgentScenario() {
		return registerByAgentScenario;
	}

	/**
	 * @param registerByAgentScenario the registerByAgentScenario to set
	 */
	public void setRegisterByAgentScenario(boolean registerByAgentScenario) {
		this.registerByAgentScenario = registerByAgentScenario;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 * @author tingyu
	 *
	 */
    public interface SubmitInvest {

    }
}
