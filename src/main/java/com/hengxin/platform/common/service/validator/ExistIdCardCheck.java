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

import com.hengxin.platform.member.validator.IdCardValidator;

/**
 * Class Name: ExistUserNameCheck
 * <p>
 * Description: the user name exist check.
 * 
 * @author minhuang
 * 
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdCardValidator.class)
@Documented
public @interface ExistIdCardCheck {

    String message() default "{member.error.idcard.duplicate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String idCardNumber();

    String userId();
}
