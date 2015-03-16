package com.hengxin.platform.common.dto.upstream;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.UNAndPW;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.service.validator.ExistUserNameCheck;
import com.hengxin.platform.common.service.validator.MemberTypeNullCheck;
import com.hengxin.platform.common.service.validator.PasswordCheck;
import com.hengxin.platform.common.service.validator.UserNameLengthCheck;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationCommonDto;
import com.hengxin.platform.member.dto.upstream.ProductProviderDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.enums.EMemberType;

/**
 * 
 * @author tingyu
 *
 */
@PasswordCheck(password = "password", passwordConfirm = "passwordConfirm", message = "{member.error.password.confirm}", groups = {UNAndPW.class})
public class AgencyRegisterInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

    @NotEmpty(groups = { UNAndPW.class }, message = "{member.error.field.empty}")
//    @Length(groups = { UNAndPW.class }, min = 4, max=15, message = "{member.error.username.length}")
    @Pattern(regexp=ApplicationConstant.USER_NAME_REGEXP, groups = { UNAndPW.class }, message = "{member.error.username.format}")
    @ExistUserNameCheck(groups = { UNAndPW.class})
    @UserNameLengthCheck(groups = { UNAndPW.class })
    @JsonProperty("username")
    private String username;

    @NotEmpty(groups = { UNAndPW.class }, message = "{member.error.field.empty}")
//    @Pattern(groups = { UNAndPW.class }, regexp = ApplicationConstant.MIN6_REGEXP, message = "{member.error.password.length}")
    @Length(min = 1, max = 20, message = "{member.error.password.length}", groups = { UNAndPW.class })
    @JsonProperty("password")
    private String password;

    @NotEmpty(groups = { UNAndPW.class }, message = "{member.error.field.empty}")
//    @Pattern(groups = { UNAndPW.class }, regexp = ApplicationConstant.MIN6_REGEXP, message = "{member.error.password.length}")
    @Length(min = 1, max = 20, message = "{member.error.password.length}", groups = { UNAndPW.class })
    @JsonProperty("passwordConfirm")
    private String passwordConfirm;

    @MemberTypeNullCheck(groups = { UNAndPW.class})
    @JsonProperty("type")
    private EMemberType type;
   
    @JsonProperty("shadowType")
    private String shadowType;

    // 显示会员类别
    @JsonProperty("member_type")
    private EMemberType memberType = EMemberType.GUEST;
    
    /** 交易账号. **/
    private AccountDto account;

    @Valid
    @JsonProperty("common")
    private AgencyApplicationCommonDto common;
    
    @Valid
    @JsonProperty("serviceCenter")
    private ServiceCenterDto serviceCenter;

    @Valid
    @JsonProperty("productProvider")
    private ProductProviderDto productProvider;

    private boolean serviceReject;
    
    private boolean productReject;
    
    private boolean serviceFixedAgent;
    
    private boolean productFixedAgent;
    
    private String comments;
    
    private boolean showSaveButton;
    
 // 特殊字段在编辑页面是否锁定
    private boolean fixedInput;
    
    private EFormMode formMode = EFormMode.VIEW;
    
    private boolean providerPendding;
    
    private boolean servicePendding;
    
    @NotEmpty(groups = { Company.class }, message = "{member.error.field.empty}")
    @JsonProperty("companyId")
    private String companyId;
    
    public boolean isEditMode() {
        return formMode == EFormMode.EDIT;
    }

    public boolean isViewMode() {
        return formMode == EFormMode.VIEW;
    }
    
    public boolean containsProvider() {
    	return EMemberType.PRODUCTSERVICE == type;
    }
    
    public boolean containsService() {
    	return EMemberType.AUTHZDCENTER == type;
    }
    
    public boolean displayProviderLevel() {
    	if (this.productProvider != null && this.productProvider.getLevel() != null) {
			if (this.productProvider.getLevel().getCode() != null
					&& !this.productProvider.getLevel().getCode().isEmpty()) {
				if (this.productReject) {
					return false;
				} else {
					return true;
				}
			}
		}
    	return false;
    }
    
    public boolean displayServiceLevel() {
    	if (this.serviceCenter != null && this.serviceCenter.getLevel() != null) {
			if (this.serviceCenter.getLevel().getCode() != null
					&& !this.serviceCenter.getLevel().getCode().isEmpty()) {
				if (this.serviceReject) {
					return false;
				} else {
					return true;
				}
			}
		}
    	return false;
    }

	/**
	 * @return the memberType
	 */
	public EMemberType getMemberType() {
		return memberType;
	}

	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(EMemberType memberType) {
		this.memberType = memberType;
	}

	/**
	 * @return the account
	 */
	public AccountDto getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(AccountDto account) {
		this.account = account;
	}

	/**
	 * @return the shadowType
	 */
	public String getShadowType() {
		return shadowType;
	}

	/**
	 * @param shadowType the shadowType to set
	 */
	public void setShadowType(String shadowType) {
		this.shadowType = shadowType;
	}

	/**
	 * @return the common
	 */
	public AgencyApplicationCommonDto getCommon() {
		return common;
	}

	/**
	 * @param common the common to set
	 */
	public void setCommon(AgencyApplicationCommonDto common) {
		this.common = common;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * @param passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	/**
	 * @return the type
	 */
	public EMemberType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EMemberType type) {
		this.type = type;
	}

	/**
	 * @return the serviceCenter
	 */
	public ServiceCenterDto getServiceCenter() {
		return serviceCenter;
	}

	/**
	 * @param serviceCenter the serviceCenter to set
	 */
	public void setServiceCenter(ServiceCenterDto serviceCenter) {
		this.serviceCenter = serviceCenter;
	}

	/**
	 * @return the productProvider
	 */
	public ProductProviderDto getProductProvider() {
		return productProvider;
	}

	/**
	 * @param productProvider the productProvider to set
	 */
	public void setProductProvider(ProductProviderDto productProvider) {
		this.productProvider = productProvider;
	}

    /**
     * @return return the value of the var fixedInput
     */
    
    public boolean isFixedInput() {
        return fixedInput;
    }

    /**
     * @param fixedInput Set fixedInput value
     */
    
    public void setFixedInput(boolean fixedInput) {
        this.fixedInput = fixedInput;
    }

    /**
     * @return return the value of the var formMode
     */
    
    public EFormMode getFormMode() {
        return formMode;
    }

    /**
     * @param formMode Set formMode value
     */
    
    public void setFormMode(EFormMode formMode) {
        this.formMode = formMode;
    }

    /**
	 * @return the serviceReject
	 */
	public boolean isServiceReject() {
		return serviceReject;
	}

	/**
	 * @param serviceReject the serviceReject to set
	 */
	public void setServiceReject(boolean serviceReject) {
		this.serviceReject = serviceReject;
	}

	/**
	 * @return the productReject
	 */
	public boolean isProductReject() {
		return productReject;
	}

	/**
	 * @param productReject the productReject to set
	 */
	public void setProductReject(boolean productReject) {
		this.productReject = productReject;
	}

	/**
	 * @return the serviceFixedAgent
	 */
	public boolean isServiceFixedAgent() {
		return serviceFixedAgent;
	}

	/**
	 * @param serviceFixedAgent the serviceFixedAgent to set
	 */
	public void setServiceFixedAgent(boolean serviceFixedAgent) {
		this.serviceFixedAgent = serviceFixedAgent;
	}

	/**
	 * @return the productFixedAgent
	 */
	public boolean isProductFixedAgent() {
		return productFixedAgent;
	}

	/**
	 * @param productFixedAgent the productFixedAgent to set
	 */
	public void setProductFixedAgent(boolean productFixedAgent) {
		this.productFixedAgent = productFixedAgent;
	}

	/**
     * @return return the value of the var comments
     */
    
    public String getComments() {
        return comments;
    }

    /**
     * @param comments Set comments value
     */
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return return the value of the var providerPendding
     */
    
    public boolean isProviderPendding() {
        return providerPendding;
    }

    /**
     * @param providerPendding Set providerPendding value
     */
    
    public void setProviderPendding(boolean providerPendding) {
        this.providerPendding = providerPendding;
    }

    /**
     * @return return the value of the var servicePendding
     */
    
    public boolean isServicePendding() {
        return servicePendding;
    }

    /**
     * @param servicePendding Set servicePendding value
     */
    
    public void setServicePendding(boolean servicePendding) {
        this.servicePendding = servicePendding;
    }

    /**
     * @return return the value of the var showSaveButton
     */
    
    public boolean isShowSaveButton() {
        return showSaveButton;
    }

    /**
     * @param showSaveButton Set showSaveButton value
     */
    
    public void setShowSaveButton(boolean showSaveButton) {
        this.showSaveButton = showSaveButton;
    }
    
	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface SubmitAgency {

    }

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface SaveAgency {

    }

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface UNAndPW {
    	
    }
    
    /**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface Company {
    	
    }
    
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
