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

import org.apache.commons.beanutils.BeanUtils;

/**
 * Class Name: PasswordValidator
 * <p>
 * Description: the password comfirm validator, <br>
 * the single blank check of field not be dealed in here
 * 
 * @author minhuang
 * 
 */

public class PasswordValidator extends BaseValidator implements ConstraintValidator<PasswordCheck, Object> {
    private String password;
    private String passwordConfirm;
    private String messageTemplete;

    @Override
    public void initialize(PasswordCheck check) {
        this.password = check.password();
        this.passwordConfirm = check.passwordConfirm();
        this.messageTemplete = check.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            final String password = BeanUtils.getProperty(object, this.password);
            final String passwordConfirm = BeanUtils.getProperty(object, this.passwordConfirm);
            // should input same
            if (password.equals(passwordConfirm)) {
                return true;
            } else {
                bindNode(context, this.passwordConfirm, this.messageTemplete);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
