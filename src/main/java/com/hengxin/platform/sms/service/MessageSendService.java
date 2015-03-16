
/*
 * Project Name: kmfex
 * File Name: MessageSendService.java
 * Class Name: MessageSendService
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

package com.hengxin.platform.sms.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.member.domain.SMSMessage;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.sms.dto.SmsParamsDto;
import com.hengxin.platform.sms.service.impl.AyjrSmsService;
import com.hengxin.platform.sms.service.impl.KmfexSmsService;
import com.hengxin.platform.sms.utils.MessageUtils;


/**
 * Class Name: MessageSendService
 * @author congzhou
 */
@Service
public class MessageSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendService.class);
    
    private static final String SMS_ENABLE_FLAG = "sms.todo_enabled";
    private static final String SMS_CONTENT_SUFFIX = ".content.sms";
    private static final String SMS_CID = ".sms.cid";
	
    private static final String SMS_CUST_SERV = "sms.cust.serv";
	private static final String KMFEX_CUST_SERV = "KMFEX";
	private static final String AYJR_CUST_SERV = "AYJR";
    
    @Autowired
    private UserRepository userRepository;
    
    private final String getCustServ(){
    	return AppConfigUtil.getConfig(SMS_CUST_SERV);
    }
    
    private AbstractSmsService getSmsService(){
    	String servProv = getCustServ();
    	LOGGER.debug("短信客户服务为{}", servProv);
    	AbstractSmsService smsService = null;
    	if(StringUtils.equalsIgnoreCase(servProv, KMFEX_CUST_SERV)){
        	smsService = ApplicationContextUtil.getApplicationContext().getBean(KmfexSmsService.class);
    	}
    	else if(StringUtils.equalsIgnoreCase(servProv, AYJR_CUST_SERV)){
        	smsService = ApplicationContextUtil.getApplicationContext().getBean(AyjrSmsService.class);
    	}
    	return smsService;
    }
    
    /**
     * 发送注册短信.
     * @param mobile
     * @param content
     * @return
     */
    public boolean sendRegisterSMS(String mobile, String content, String cid, String ...params) {
    	if (isEmptyMobile(mobile)) {
			return false;
		}
    	return getSmsService().sendRegisterSms(mobile, content, cid, params);
    }
    
    public SmsParamsDto createSmsParamDto(String userId, String messageKey, Object ...args){
    	SmsParamsDto dto = null;
    	UserPo user = userRepository.findOne(userId);
    	if (user!=null&&StringUtils.isNotBlank(user.getMobile())) {
    		String mobile = user.getMobile();
    		String content = MessageUtil.getMessage(messageKey + SMS_CONTENT_SUFFIX);
    		String cid = MessageUtil.getMessage(messageKey + SMS_CID);
    		if(StringUtils.isBlank(cid)){
    			cid = null;
    			if(args!=null&&args.length>0){
    				content = MessageUtils.formatMessage(content, args);
    			}
    		}
    		String[] params = new String[]{};
    		if(args!=null&&args.length>0){
    			params = new String[args.length];
    			for(int k=0; k<args.length; k++){
    				params[k] = String.valueOf(args[k]);
    			}
    		}
    		dto = new SmsParamsDto();
    		dto.setCid(cid);
    		dto.setMobile(mobile);
    		dto.setContent(content);
    		dto.setParams(params);
		}
    	return dto;
    }

    /**
     * 
     * Description: 发送待办短信
     * 
     * @param mobile
     * @param content
     * @return
     */
    @Transactional(readOnly=true)
    public String sendSMS(String mobile, String content, String s1, String s2, String cid, String ...params) {
    	String smsFlag = AppConfigUtil.getConfig(SMS_ENABLE_FLAG);
    	if(StringUtils.equalsIgnoreCase(smsFlag, "true")){
    		return getSmsService().sendSms(mobile, content, cid, params);
        }
    	return null;
    }
    
    @Transactional(readOnly=true)
    public List<String> batchSend(List<SMSMessage> messages, String s1, String s2, String cid, String ...args) {
    	//TODO 暂时用不了
    	return null;
    	/*
    	List<String> failureList = new ArrayList<String>();
    	for (SMSMessage msg : messages) {
        	if (isEmptyMobile(msg.getMobile())) {
    			continue;
    		}
            try {
            	this.sendSMS(msg.getMobile(), msg.getMessage(), s1, s2, cid, args);
            } catch (Exception e) {
            	failureList.add(msg.getId());
             	e.printStackTrace();
            }
		}
    	return failureList;
    	*/
    }
    
    private boolean isEmptyMobile(String moblie){
    	return StringUtils.isBlank(moblie)
    			||StringUtils.equals("88888888888", moblie);
    }
	
}
