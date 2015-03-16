/*
 * Project Name: kmfex-platform
 * File Name: SignInDto.java
 * Class Name: SignInDto
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

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.service.validator.ExistCaptchaCheck;

/**
 * 
 * Class Name: SignInDto
 * <p>
 * Description: the validation result for form.
 * 
 * @author runchen
 * 
 */
public class SignInDto implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{member.error.field.empty}")
    private String userName;

    @NotEmpty(message = "{member.error.field.empty}")
    private String password;

    @NotEmpty(message = "{member.error.field.empty}")
    @ExistCaptchaCheck(message = "{member.error.captcha.invalid}")
    private String captcha;

    private boolean rememberMe;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCaptcha() {
        return captcha;
    }
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
    public boolean isRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
