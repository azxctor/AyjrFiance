/*
 * Project Name: kmfex-platform
 * File Name: RegisterDto.java
 * Class Name: RegisterDto
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
package com.hengxin.platform.common.dto.upstream;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.dto.upstream.RegisterDto.RegisterGroup;
import com.hengxin.platform.common.service.validator.ExistCaptchaCheck;
import com.hengxin.platform.common.service.validator.ExistUserNameCheck;
import com.hengxin.platform.common.service.validator.PasswordCheck;
import com.hengxin.platform.common.service.validator.SMSCheck;
import com.hengxin.platform.common.service.validator.UserNameLengthCheck;
import com.hengxin.platform.escrow.validator.MobileCheck;

/**
 * 
 * Class Name: RegisterDto
 * <p>
 * Description: the validation result for form.
 * 
 * @author runchen
 * 
 */
@PasswordCheck(password = "password", passwordConfirm = "passwordConfirm", message = "{member.error.password.confirm}", groups = { RegisterGroup.class })
@SMSCheck(mobile = "mobile", captcha = "code", message = "{member.error.captcha.invalid}", groups = { RegisterGroup.class })
public class RegisterDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{member.error.field.empty}", groups = { RegisterGroup.class })
//    @Length(groups = { RegisterGroup.class }, min = 4, max=15, message = "{member.error.username.length}")
    @Pattern(regexp=ApplicationConstant.USER_NAME_REGEXP, groups = { RegisterGroup.class }, message = "{member.error.username.format}")
    @ExistUserNameCheck(groups = { RegisterGroup.class })
    @UserNameLengthCheck(groups = { RegisterGroup.class })
    private String username;

    @NotEmpty(message = "{member.error.field.empty}", groups = { RegisterGroup.class })
    @Length(min = 1, max = 20, message = "{member.error.password.length}", groups = { RegisterGroup.class })
    private String password;

    @NotEmpty(message = "{member.error.passwordConfirm.empty}", groups = { RegisterGroup.class })
    @Length(min = 1, max = 20, message = "{member.error.password.length}", groups = { RegisterGroup.class })
    private String passwordConfirm;

    @NotEmpty(groups = { RegisterGroup.class }, message = "{member.error.field.empty}")
    @Pattern(regexp = ApplicationConstant.MOBILE_REGEXP, groups = { RegisterGroup.class }, message = "{member.error.mobile.invaild}")
    @MobileCheck(groups = { RegisterGroup.class })
    private String mobile;

//    @NotEmpty(groups = { RegisterGroup.class }, message = "{member.error.field.empty}")
    @Email(regexp = ApplicationConstant.EMAIL_REGEXP, groups = { RegisterGroup.class }, message = "{member.error.email.invaild}")
    private String email;

    @NotEmpty(message = "{member.error.captcha.empty}", groups = { RegisterGroup.class })
    @ExistCaptchaCheck(message = "{member.error.captcha.invalid}", groups = { RegisterGroup.class })
    private String captcha;
    
//    @NotEmpty(groups = { RegisterGroup.class }, message = "{member.error.field.empty}")
    private String code;


    public interface RegisterGroup {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
