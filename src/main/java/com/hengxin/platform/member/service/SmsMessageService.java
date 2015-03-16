package com.hengxin.platform.member.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.member.domain.SMSMessage;
import com.hengxin.platform.member.repository.SMSMessageRepository;

@Service
public class SmsMessageService {
    
    @Autowired
    private SMSMessageRepository smsMessageRepository;
	
    @Transactional(readOnly = true)
    public List<SMSMessage> getBatchSendSmsMessageList(Date lessThanSendTs){
   	 	List<SMSMessage> messages = smsMessageRepository.findByStatusAndSendTsLessThan("N", lessThanSendTs);
   	 	return messages;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAllSmsMessageSendSuccess(Date lessThanSendTs){
   	 	smsMessageRepository.updateAllMessagesStatus("Y", lessThanSendTs);
    }
	
}
