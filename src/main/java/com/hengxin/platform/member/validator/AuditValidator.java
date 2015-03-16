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

import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.member.dto.AuditDto;

/**
 * @author yingchangwang
 * 
 */
@Component
public class AuditValidator extends BaseValidator implements ConstraintValidator<AuditCheck, AuditDto> {

    private String comments;

    @Override
    public void initialize(AuditCheck obj) {
        this.comments = obj.comments();
    }

    @Override
    public boolean isValid(AuditDto audit, ConstraintValidatorContext context) {
        boolean isPassed = audit.isPassed();
        String inputComments = audit.getComments();
        if (!isPassed && StringUtils.isEmpty(inputComments)) {
            bindNode(context, this.comments, null);
            return false;
        }
        return true;
    }

}
