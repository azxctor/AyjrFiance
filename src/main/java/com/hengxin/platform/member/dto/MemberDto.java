/*
 * Project Name: kmfex-platform
 * File Name: Member.java
 * Class Name: Member
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
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: Member Description: Domain of MemberApplicationPo and MemberPo, please use its children class, Person and
 * Organization. All create or update functions should only operate MemberApplicationPo, MemberPo is only used to load
 * data or merge info.<br/>
 * The fields end with <strong>original</strong> should keep original data.<br/>
 * These data will be stored at DIV 'hidden_original_data', and will be repost when the whole form submit.
 * 
 * @author shengzhou
 * 
 */
public class MemberDto extends BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("user_type")
    protected EUserType userType;

//    @Valid
    @OptionCategory(EOptionCategory.BANK)
    @JsonProperty("bank_name")
    protected DynamicOption bankShortName;

    @JsonProperty("bank_full_name")
    protected String bankFullName;

//    @NotEmpty(groups = { SubmitPerson.class, SubmitOrg.class }, message = "{member.error.field.empty}")
//    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP, groups = { SubmitPerson.class, SubmitOrg.class }, message = "{member.error.bank.invaild}")
    protected String bankAccount;

    /**
     * <Strong>original info instead of mask info when submit the whole form</strong>.
     */
    @JsonProperty("bank_account_original")
    protected String bankAccountOriginal;

//    @NotEmpty(groups = { SubmitPerson.class, SubmitOrg.class }, message = "{member.error.field.empty}")
    protected String bankCardFrontImg;

    /**
     * <Strong>original info instead of mask info when submit the whole form</strong>.
     */
    @JsonProperty("bank_card_front_img_original")
    protected String bankCardFrontImgOriginal;
    
//    @NotEmpty(groups = { SubmitPerson.class, SubmitOrg.class }, message = "{member.error.field.empty}")
    protected String bankAccountName;

//    @Valid
    @OptionCategory(EOptionCategory.REGION)
    @JsonProperty("bank_province")
    protected DynamicOption bankOpenProvince;

//    @Valid
    @OptionCategory(EOptionCategory.REGION)
    @JsonProperty("bank_city")
    protected DynamicOption bankOpenCity;
    
//    @NotEmpty(groups = { SubmitPerson.class, SubmitOrg.class }, message = "{member.error.field.empty}")
    @JsonProperty("bank_branch")
    protected String bankOpenBranch;

    // email和mobile只为第一次显示用，不要用于存数据
    protected String email;

    protected String mobile;

    private AccountDto account;

    // 显示会员类别
    @JsonProperty("member_type")
    private EMemberType memberType = EMemberType.GUEST;

    private String bankCardFrontImgUrl;

    @JsonProperty("bank_card_front_img_url")
    public String getBankCardFrontImgUrl() {
        if (StringUtils.isNotBlank(bankCardFrontImg)
                && !SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
            return FileUploadUtil.getTempFolder() + bankCardFrontImg;
        }
        return "";
    }

    // 用于在修改pending状态显示原信息
    private MemberDto oldMember;

    /* the following fields are used to manage page state. */
    // 页面的状态
    private EFormMode formMode = EFormMode.VIEW;

    // 特殊字段在编辑页面是否锁定
    private boolean fixedInput = true;

    //
    private MaskFixedDto fixedStatus = new MaskFixedDto();
    
    // 从其他页面跳转到basic编辑后需跳回原页面
    private String fromPage;

    private boolean canEdit;

    private boolean rejectLastTime;

    public boolean containsPerson() {
        return userType == null || userType == EUserType.NULL || getStatus() == EApplicationStatus.UNCOMMITTED
                || userType == EUserType.PERSON;
    }

    public boolean containsOrg() {
        return userType == null || userType == EUserType.NULL || getStatus() == EApplicationStatus.UNCOMMITTED
                || userType == EUserType.ORGANIZATION;
    }

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

    /**
     * @return return the value of the var userType
     */

    public EUserType getUserType() {
        return userType;
    }

    /**
     * @param userType
     *            Set userType value
     */

    public void setUserType(EUserType userType) {
        this.userType = userType;
    }

    /**
     * @return return the value of the var bankShortName
     */

    public DynamicOption getBankShortName() {
        return bankShortName;
    }

    /**
     * @param bankShortName
     *            Set bankShortName value
     */

    public void setBankShortName(DynamicOption bankShortName) {
        this.bankShortName = bankShortName;
    }

    /**
     * @return return the value of the var bankFullName
     */

    public String getBankFullName() {
        return bankFullName;
    }

    /**
     * @param bankFullName
     *            Set bankFullName value
     */

    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName;
    }

    /**
	 * @return the bankAccountOriginal
	 */
	public String getBankAccountOriginal() {
		return bankAccountOriginal;
	}

	/**
	 * @param bankAccountOriginal the bankAccountOriginal to set
	 */
	public void setBankAccountOriginal(String bankAccountOriginal) {
		this.bankAccountOriginal = bankAccountOriginal;
	}

	/**
     * @return return the value of the var bankAccount
     */
    public String getBankAccount() {
        return bankAccount;
    }
    
    /**
     * @return return mask the value of the var bankAccount
     */
    @JsonProperty("bank_account")
    public String getMakBankAccount() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
            bankAccount = MaskUtil.maskCardNumber(bankAccount);
        }
        return bankAccount;
    }

    /**
     * @param bankAccount
     *            Set bankAccount value
     */
    @JsonProperty("bank_account")
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        this.bankAccountOriginal = bankAccount;
    }

    /**
     * @return return the value of the var bankCardFrontImg
     */

    public String getBankCardFrontImg() {
        return bankCardFrontImg;
    }
    
    /**
     * @return return the mask value of the var bankCardFrontImg
     */
    @JsonProperty("bank_card_front_img")
    public String getMaskBankCardFrontImg() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
//            bankCardFrontImg = "";
        }
        return bankCardFrontImg;
    }

    /**
     * @param bankCardFrontImg
     *            Set bankCardFrontImg value
     */
    @JsonProperty("bank_card_front_img")
    public void setBankCardFrontImg(String bankCardFrontImg) {
        this.bankCardFrontImg = bankCardFrontImg;
        this.bankCardFrontImgOriginal = bankCardFrontImg;
    }

    /**
     * @return return the value of the var bankAccountName
     */

    public String getBankAccountName() {
        return bankAccountName;
    }
    
    /**
     * @return return the mask value of the var bankAccountName
     */
    @JsonProperty("bank_account_name")
    public String getMaskBankAccountName() {
        if (SecurityContext.getInstance().cannotViewRealName(this.getUserId(), inCanViewPage)) {
            bankAccountName = MaskUtil.maskChinsesName(bankAccountName);
        }
        return bankAccountName;
    }

    /**
     * @param bankAccountName
     *            Set bankAccountName value
     */
    @JsonProperty("bank_account_name")
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }



    /**
     * @return return the value of the var bankOpenBranch
     */

    public String getBankOpenBranch() {
        return bankOpenBranch;
    }

    /**
     * @param bankOpenBranch
     *            Set bankOpenBranch value
     */

    public void setBankOpenBranch(String bankOpenBranch) {
        this.bankOpenBranch = bankOpenBranch;
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

    public boolean isFixedInput() {
        return fixedInput;
    }

    public void setFixedInput(boolean fixedInput) {
        this.fixedInput = fixedInput;
    }

    /**
     * @return return the value of the var email
     */

    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            Set email value
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return return the value of the var mobile
     */

    public String getMobile() {
        return mobile;
    }
    
//    /**
//     * @return return the mask value of the var mobile
//     */
//
//    public String getMaskMobile() {
//        if (SecurityContext.getInstance().cannotViewRealPhoneNo(this.getUserId())) {
//            mobile = MaskUtil.maskPhone(mobile);
//        }
//        return mobile;
//    }

    /**
     * @param mobile
     *            Set mobile value
     */

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return return the value of the var fromPage
     */

    public String getFromPage() {
        return fromPage;
    }

    /**
     * @param fromPage
     *            Set fromPage value
     */

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
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

    /**
     * @return return the value of the var oldMember
     */

    public MemberDto getOldMember() {
        return oldMember;
    }

    /**
     * @param oldMember
     *            Set oldMember value
     */

    public void setOldMember(MemberDto oldMember) {
        this.oldMember = oldMember;
    }

    /**
     * @return return the value of the var account
     */

    public AccountDto getAccount() {
        return account;
    }

    /**
     * @param account
     *            Set account value
     */

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    public void setBankCardFrontImgUrl(String bankCardFrontImgUrl) {
        this.bankCardFrontImgUrl = bankCardFrontImgUrl;
    }

    /**
     * @return return the value of the var isRejectLastTime
     */

    public boolean isRejectLastTime() {
        return rejectLastTime;
    }

    /**
     * @param isRejectLastTime
     *            Set isRejectLastTime value
     */

    public void setRejectLastTime(boolean rejectLastTime) {
        this.rejectLastTime = rejectLastTime;
    }

    /**
     * @return return the value of the var memberType
     */

    public EMemberType getMemberType() {
        return memberType;
    }
    /**
     * @param memberType
     *            Set memberType value
     */

    public void setMemberType(EMemberType memberType) {
        this.memberType = memberType;
    }

	public DynamicOption getBankOpenProvince() {
		return bankOpenProvince;
	}

	public void setBankOpenProvince(DynamicOption bankOpenProvince) {
		this.bankOpenProvince = bankOpenProvince;
	}

	public DynamicOption getBankOpenCity() {
		return bankOpenCity;
	}

	public void setBankOpenCity(DynamicOption bankOpenCity) {
		this.bankOpenCity = bankOpenCity;
	}

	/**
	 * @return the bankCardFrontImgOriginal
	 */
	public String getBankCardFrontImgOriginal() {
		return bankCardFrontImgOriginal;
	}

	/**
	 * @param bankCardFrontImgOriginal the bankCardFrontImgOriginal to set
	 */
	public void setBankCardFrontImgOriginal(String bankCardFrontImgOriginal) {
		this.bankCardFrontImgOriginal = bankCardFrontImgOriginal;
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
    public interface BankExist {

    }
}
