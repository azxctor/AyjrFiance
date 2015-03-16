/*
 * Project Name: kmfex-platform
 * File Name: BankAccoutValidator.java
 * Class Name: BankAccoutValidator
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

import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.SecurityContext;

/**
 * BankAccoutValidator.
 * 
 * @author runchen
 * 
 */
@Component
public class BankAccoutValidator extends BaseValidator implements ConstraintValidator<ExistBankAccountCheck, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccoutValidator.class);

    private String userId;
    private String bankAccount;
    private String messageTemplete;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SecurityContext securityContext;
    
    @Autowired
    private BankAcctRepository bankAcctRepository;

    @Override
    public void initialize(ExistBankAccountCheck check) {
        this.userId = check.userId();
        this.bankAccount = check.bankAccount();
        this.messageTemplete = check.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            final String bankAccountNumber = BeanUtils.getProperty(object, this.bankAccount);
            final String userId = BeanUtils.getProperty(object, this.userId);
            if (StringUtils.isNotBlank(bankAccountNumber)) {
            	int result = 0;
            	/** add org bank care validation. **/
            	if (StringUtils.isNotBlank(userId)) {
            		result = bankAcctRepository.countByBankAccountAndUserIdNot(bankAccountNumber, userId);
            	} else {
            		result = bankAcctRepository.countByBankAccount(bankAccountNumber);
            	}
            	if (result > 0) {
            		bindNode(context, this.bankAccount, this.messageTemplete);
                    return false;
            	} else {
            		/** Maybe it is redundant. **/
					MemberApplication member = memberService.getMemberByBankAccout(userId, bankAccountNumber);
	                if (member != null) {
	                    bindNode(context, this.bankAccount, this.messageTemplete);
	                    return false;
	                }
				}
            }
        } catch (Exception e) {
            LOGGER.error("BankAccoutValidator throw error!", e);
        }
        return true;
    }

}
