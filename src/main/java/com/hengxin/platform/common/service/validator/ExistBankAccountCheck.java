/*
 * Project Name: standard-code-base
 * File Name: DistrictCheck.java
 * Class Name: DistrictCheck
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Class Name: BankAccoutValidator
 * <p>
 * Description: the user name exist check.
 * 
 * @author runchen
 * 
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BankAccoutValidator.class)
@Documented
public @interface ExistBankAccountCheck {

    String message() default "{member.error.bankaccount.duplicate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String bankAccount();

    String userId();

}
