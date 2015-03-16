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

import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: UserNameValidator
 * <p>
 * Description: the user name exist validator
 * 
 * @author minhuang
 * 
 */

public class UserNameValidator extends BaseValidator implements ConstraintValidator<ExistUserNameCheck, String> {

    @Autowired
    private UserService userService;
    
    @Autowired
    private transient SecurityContext securityContext;
    
    @Override
    public void initialize(ExistUserNameCheck check) {
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
            return !userService.isExistingUser(userName.trim());
    }
}
