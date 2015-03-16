/*
 * Project Name: kmfex-platform
 * File Name: UserIdMatchBnkAcctNoValidator.java
 * Class Name: UserIdMatchBnkAcctNoValidator
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

package com.hengxin.platform.fund.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.repository.BankAcctRepository;

/**
 * Class Name: BnkAcctNoMatchUserIdValidator Description: TODO
 * 
 * @author tingwang
 * 
 */

public class BnkAcctNoMatchUserIdValidator extends BaseValidator implements
        ConstraintValidator<BnkAcctNoMatchUserIdCheck, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BnkAcctNoMatchUserIdValidator.class);

    private String userId;

    private String bnkAcctNo;

    private String message;

    @Autowired
    BankAcctRepository bankAcctRepository;

    @Override
    public void initialize(BnkAcctNoMatchUserIdCheck constraintAnnotation) {
        this.userId = constraintAnnotation.userId();
        this.bnkAcctNo = constraintAnnotation.bnkAcctNo();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String userId = BeanUtils.getProperty(value, this.userId);
            String bnkAcctNo = BeanUtils.getProperty(value, this.bnkAcctNo);
            BankAcct bankAcct = ConverterService.convert(bankAcctRepository.findBankAcctByUserId(userId),
                    BankAcct.class);
            if (bankAcct != null && bnkAcctNo.equals(bankAcct.getBnkAcctNo())) {
                return true;
            }
            bindNode(context, this.bnkAcctNo, this.message);
        } catch (Exception e) {
            LOGGER.error("BnkAcctNoMatchUserIdValidator.isValid has error", e);
        }
        return false;
    }

}
