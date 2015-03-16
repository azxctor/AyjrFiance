
/*
 * Project Name: kmfex
 * File Name: SMSValidator.java
 * Class Name: SMSValidator
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

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.member.domain.SMSAuthentic;
import com.hengxin.platform.member.repository.SMSAuthenticRepository;
import com.hengxin.platform.sms.consts.SmsConsts;


/**
 * Class Name: SMSValidator
 * Description: TODO
 * @author congzhou
 *
 */

public class SMSValidator extends BaseValidator implements ConstraintValidator<SMSCheck, Object>{

    private String mobile;
    private String captcha;
    private String messageTemplete;

    @Autowired
    SMSAuthenticRepository sMSAuthenticRepository;


    @Override
    public void initialize(SMSCheck check) {
        this.mobile = check.mobile();
        this.captcha = check.captcha();
        this.messageTemplete = check.message();

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        //短信验证开关
        if(StringUtils.equals(AppConfigUtil.getConfig(SmsConsts.REG_SMS_VERIF_ENABLE_FLAG), "true")) {
            try {
                final String mobile = BeanUtils.getProperty(object, this.mobile);
                final String captcha = BeanUtils.getProperty(object, this.captcha);
                if (captcha == null || captcha.isEmpty()) {
					bindNode(context, "code", "member.error.field.empty");
					return false;
				}
                SMSAuthentic sMSAuthentic = sMSAuthenticRepository.findByMobile(mobile);
                final long differ = (new Date().getTime() - sMSAuthentic.getCreateTs().getTime())/1000;
                if (sMSAuthentic.getCode().equals(captcha) 
                		&& differ <= Long.valueOf(AppConfigUtil.getConfig(SmsConsts.REG_SMS_EFFECT_TIME))) {
                    return true;
                } else {
                    String message = null;
                    if(!sMSAuthentic.getCode().equals(captcha)) {
                        message = this.messageTemplete;
                    } else if(differ > Long.valueOf(AppConfigUtil.getConfig("sms.effictive.time"))) {
                        message = "{member.error.captcha.expired}";
                    }
                    bindNode(context, "code", message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

}
