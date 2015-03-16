/*
 * Project Name: standard-code-base
 * File Name: DistrictValidator.java
 * Class Name: DistrictValidator
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.security.util.KaptchaSupport;

/**
 * Class Name: CaptchaValidator
 * <p>
 * Description: the captcha exist validator
 * 
 * @author minhuang
 * 
 */
@Component
public class CaptchaValidator extends BaseValidator implements ConstraintValidator<ExistCaptchaCheck, String> {
    
    @Autowired
    private WebUtil webUtil;
    
    @Autowired
    public KaptchaSupport ks;

    @Override
    public void initialize(ExistCaptchaCheck check) {
    }

    @Override
    public boolean isValid(String captcha, ConstraintValidatorContext context) {
        return ks.validateCaptcha(captcha,  webUtil.getThreadSession(), webUtil.getThreadRequest());
    }
}
