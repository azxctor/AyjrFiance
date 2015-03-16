
/*
* Project Name: kmfex
* File Name: RegisterService.java
* Class Name: RegisterService
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
	
package com.hengxin.platform.member.service;

import java.text.MessageFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.PhoneMessageUtil;
import com.hengxin.platform.member.domain.SMSAuthentic;
import com.hengxin.platform.member.repository.SMSAuthenticRepository;
import com.hengxin.platform.sms.service.MessageSendService;


/**
 * Class Name: RegisterService
 * Description: TODO
 * @author congzhou
 *
 */
@Service
public class RegisterService {
    
	private static final String SMS_CID = "reg.sms.cid";
	private static final String SMS_CONTENT = "reg.sms.content";

    @Autowired
    MessageSendService messageSendService;
    
    @Autowired
    SMSAuthenticRepository sMSAuthenticRepository;
    
    @Transactional
    public boolean smsVerify(String phone) {
        String captcha = PhoneMessageUtil.getCaptcha();
        SMSAuthentic sMSAuthentic = sMSAuthenticRepository.findByMobile(phone);
        if(null == sMSAuthentic)sMSAuthentic = new SMSAuthentic();
        sMSAuthentic.setCode(captcha);
        sMSAuthentic.setMobile(phone);
        sMSAuthentic.setCreateTs(new Date());
        sMSAuthentic.setCreatorOpId("SYS");
        sMSAuthenticRepository.save(sMSAuthentic);
        String cid = AppConfigUtil.getConfig(SMS_CID);
        String template = AppConfigUtil.getConfig(SMS_CONTENT);
        String builder = MessageFormat.format(template, captcha);
        return messageSendService.sendRegisterSMS(phone, builder.toString(), cid, captcha);
    }

}
