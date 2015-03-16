package com.hengxin.platform.member.dto.upstream;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.SubmitAgency;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.service.validator.DateRangeCheck;

@DateRangeCheck(startDate = "cooperationBeginDate", endDate = "cooperationEndDate", message = "{member.error.daterange.invalid}", groups = {SubmitAgency.class})
public class ServiceCenterDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
	@JsonProperty("cooperation_form")
    private String cooperationForm;
	
	@NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
	@JsonProperty("cooperation_begin_date")
    private String cooperationBeginDate;

	@NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
	@JsonProperty("cooperation_end_date")
    private String cooperationEndDate;

	@JsonProperty("registration_place")
    private String registrationPlace;

	@JsonProperty("registration_date")
    private String registrationDate;

	@Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.NUMBER_REGEXP, message = "{member.error.registrationCapital.invaild}")
	@JsonProperty("registration_capital")
    private String registrationCapital;

	@Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.NUMBER_REGEXP, message = "{member.error.employeenumber.invaild}")
	@JsonProperty("employee_no")
    private String employeeNo;

	@JsonProperty("agent")
    private String agent;
	
    @OptionCategory(EOptionCategory.SERVICE_CENTER_LEVEL)
    private DynamicOption level;

	@JsonProperty("agencyApplication")
	private AgencyApplicationCommonDto agencyApplication;

	private String userId;

	private String serviceCenterDesc;
	
    public ServiceCenterDto() {

    }

    public ServiceCenterDto(String userId, String name) {
        agencyApplication = new AgencyApplicationCommonDto();
        agencyApplication.setUserId(userId);
        agencyApplication.setName(name);
    }

	@Override
	public String toString() {
		return "ServiceCenterDto [cooperationForm=" + cooperationForm
				+ ", cooperationBeginDate=" + cooperationBeginDate
				+ ", cooperationEndDate=" + cooperationEndDate
				+ ", registrationPlace=" + registrationPlace
				+ ", registrationDate=" + registrationDate
				+ ", registrationCapital=" + registrationCapital
				+ ", employeeNo=" + employeeNo + ", agent=" + agent
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * @return the cooperationForm
	 */
	public String getCooperationForm() {
		return cooperationForm;
	}

	/**
	 * @param cooperationForm the cooperationForm to set
	 */
	public void setCooperationForm(String cooperationForm) {
		this.cooperationForm = cooperationForm;
	}

	/**
	 * @return the cooperationBeginDate
	 */
	public String getCooperationBeginDate() {
		return cooperationBeginDate;
	}

	/**
	 * @param cooperationBeginDate the cooperationBeginDate to set
	 */
	public void setCooperationBeginDate(String cooperationBeginDate) {
		this.cooperationBeginDate = cooperationBeginDate;
	}

	/**
	 * @return the cooperationEndDate
	 */
	public String getCooperationEndDate() {
		return cooperationEndDate;
	}

	/**
	 * @param cooperationEndDate the cooperationEndDate to set
	 */
	public void setCooperationEndDate(String cooperationEndDate) {
		this.cooperationEndDate = cooperationEndDate;
	}

	/**
	 * @return the registrationPlace
	 */
	public String getRegistrationPlace() {
		return registrationPlace;
	}

	/**
	 * @param registrationPlace the registrationPlace to set
	 */
	public void setRegistrationPlace(String registrationPlace) {
		this.registrationPlace = registrationPlace;
	}

	/**
	 * @return the registrationDate
	 */
	public String getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the registrationCapital
	 */
	public String getRegistrationCapital() {
		return registrationCapital;
	}

	/**
	 * @param registrationCapital the registrationCapital to set
	 */
	public void setRegistrationCapital(String registrationCapital) {
		this.registrationCapital = registrationCapital;
	}

	/**
	 * @return the employeeNo
	 */
	public String getEmployeeNo() {
		return employeeNo;
	}

	/**
	 * @param employeeNo the employeeNo to set
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

    /**
     * @deprecated
     * @return return the value of the var agencyApplication
     */
    
    @Deprecated
    public AgencyApplicationCommonDto getAgencyApplication() {
        return agencyApplication;
    }

    /**
     * @deprecated
     * @param agencyApplication Set agencyApplication value
     */
    
    @Deprecated
    public void setAgencyApplication(AgencyApplicationCommonDto agencyApplication) {
        this.agencyApplication = agencyApplication;
    }

	/**
	 * @return the level
	 */
	public DynamicOption getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(DynamicOption level) {
		this.level = level;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the serviceCenterDesc
	 */
	public String getServiceCenterDesc() {
		return serviceCenterDesc;
	}

	/**
	 * @param serviceCenterDesc the serviceCenterDesc to set
	 */
	public void setServiceCenterDesc(String serviceCenterDesc) {
		this.serviceCenterDesc = serviceCenterDesc;
	}

}
