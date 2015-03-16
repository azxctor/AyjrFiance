/*
 * Project Name: kmfex-platform
 * File Name: AuthzdCenterValidator.java
 * Class Name: AuthzdCenterValidator
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

package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: AuthzdCenterValidator Description: TODO
 * 
 * @author runchen
 * 
 */
@Component
public class AuthzdCenterValidator extends BaseValidator implements ConstraintValidator<ExistAuthzdCenterCheck, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzdCenterValidator.class);

    private String userId;
    private String authzdCenter;
    private String messageTemplete;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SecurityContext securityContext;

    @Override
    public void initialize(ExistAuthzdCenterCheck check) {
        this.userId = check.userId();
        this.authzdCenter = check.authzdCenter();
        this.messageTemplete = check.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            final String authzdCenterName = BeanUtils.getProperty(object, this.authzdCenter);
            final String userId = BeanUtils.getProperty(object, this.userId);
            if (StringUtils.isNotBlank(authzdCenterName)) {
                Agency agency = memberService.getAgencyByNameAndUserIdNot(userId, authzdCenterName);
                if (agency != null) {
                    bindNode(context, this.authzdCenter, this.messageTemplete);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("AuthzdCenterValidator throw error!", e);
        }
        return true;
    }

}
