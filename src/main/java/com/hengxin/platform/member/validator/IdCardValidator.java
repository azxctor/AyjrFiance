/*
 * Project Name: kmfex-platform
 * File Name: UserCreateValidator.java
 * Class Name: UserCreateValidator
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

package com.hengxin.platform.member.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.common.service.validator.ExistIdCardCheck;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberService;

/**
 * Class Name: UserCreateValidator Description: TODO
 * 
 * @author runchen
 * 
 */
@Component
public class IdCardValidator extends BaseValidator implements ConstraintValidator<ExistIdCardCheck, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdCardValidator.class);

    private String userId;
    private String idCardNumber;
    private String messageTemplete;

    @Autowired
    private MemberService memberService;

    @Override
    public void initialize(ExistIdCardCheck check) {
        this.userId = check.userId();
        this.idCardNumber = check.idCardNumber();
        this.messageTemplete = check.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            final String idCardNum = BeanUtils.getProperty(object, this.idCardNumber);
            final String username = BeanUtils.getProperty(object, this.userId);
            if (StringUtils.isNotBlank(idCardNum)) {
                MemberApplication member = memberService.getMemberByIdCardNum(username, EUserType.PERSON.getCode(),
                        idCardNum);
                if (member != null) {
                    bindNode(context, this.idCardNumber, this.messageTemplete);
                	//FIXME temporarily user json property instead of field name. It will throw ex, but it can bind error correctly...
                	//At method getJsonName of ExceptionHandler, it does not support get json property from method, currently it only get it from field.
//                    bindNode(context, "id_card_number", this.messageTemplete);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("IdCardValidator throw error!", e);
        }
		return true;
	}

}
