package com.hengxin.platform.member.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.Message;
import com.hengxin.platform.member.domain.SMSMessage;
import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.dto.MessageCriteria;
import com.hengxin.platform.member.dto.MessageDto;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.repository.MemberRepository;
import com.hengxin.platform.member.repository.MessageRepository;
import com.hengxin.platform.member.repository.UserRoleRepository;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.RolePo;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.repository.RoleRepository;
import com.hengxin.platform.sms.dto.SmsParamsDto;
import com.hengxin.platform.sms.service.MessageSendService;

/**
 * Send or Save inner message. 
 *
 */
@Service
public class MemberMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMessageService.class);
    private static final String TITLE_SUFFIX = ".title";
    private static final String CONTENT_SUFFIX = ".content";
    private static final String RICH_CONTENT_SUFFIX = ".content.rich";
    private static final String URL_SUFFIX = ".url";
    
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MessageSendService messageSendService;
    
    @Autowired
    private SmsMessageService smsMessageService;
    
    @Autowired
    private SecurityContext securityContext;

    @Transactional(readOnly = true)
    public DataTablesResponseDto<MessageDto> getMessages(final MessageCriteria searchCriteria) {
        LOGGER.info("getMessages() invoked");
        Pageable pageRequest = PaginationUtil.buildPageRequest(searchCriteria);
        Specification<Message> specification = new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (searchCriteria != null) {
                    expressions.add(cb.equal(root.<String> get("userId"), searchCriteria.getUserId()));
                    if (searchCriteria.isReadable() == true) {
                        expressions.add(cb.equal(root.<String> get("read"), false));
                    }
                    if (StringUtils.isNotBlank(searchCriteria.getCategory())) {
                        if ("1".equalsIgnoreCase(searchCriteria.getCategory())) {
                            expressions.add(cb.equal(root.<String> get("type"), EMessageType.MESSAGE));
                        } else if ("2".equalsIgnoreCase(searchCriteria.getCategory())) {
                            expressions.add(cb.equal(root.<String> get("type"), EMessageType.TODO));
                        } else if ("3".equalsIgnoreCase(searchCriteria.getCategory())) {
                            expressions.add(cb.notEqual(root.<String> get("type"), EMessageType.SMS));
                        }
                    }
                }
                return predicate;
            }
        };
        Page<Message> messages = messageRepository.findAll(specification, pageRequest);
        DataTablesResponseDto<MessageDto> msgDtos = new DataTablesResponseDto<MessageDto>();
        List<MessageDto> msgList = new ArrayList<MessageDto>();
        msgDtos.setEcho(searchCriteria.getEcho());
        for (Message msg : messages) {
            MessageDto msgDto = new MessageDto();
            msgDto.setMessageId(msg.getMessageId());
            msgDto.setType(msg.getType().getCode());
            msgDto.setTitle(msg.getTitle());
            msgDto.setMessage(msg.getMessage());
            msgDto.setRead(msg.getRead());
            msgDto.setCreateTs(ProductUtil.formatDate(msg.getCreateTs(),"yyyy-MM-dd HH:mm:ss"));
            msgList.add(msgDto);
        }
        msgDtos.setData(msgList);
        msgDtos.setTotalDisplayRecords(messages.getTotalElements());
        msgDtos.setTotalRecords(messages.getTotalElements());
        LOGGER.debug("getMessages() completed");
        return msgDtos;
    }

    @Transactional
    public Message updateMessage(String messageId) {
        Message message = messageRepository.findOne(messageId);
        message.setRead(true);
        return this.messageRepository.save(message);
    }

    @Transactional
    public boolean updateMessageList(List<String> messageIdList) {
        List<Message> messageList = new ArrayList<Message>();
        for (String messageId : messageIdList) {
            Message message = messageRepository.findOne(messageId);
            message.setRead(true);
            messageList.add(message);
        }
        this.messageRepository.save(messageList);
        return true;
    }

    @Transactional
    public boolean deleteMessageList(List<String> messageIdList) {
        List<Message> messageList = new ArrayList<Message>();
        for (String messageId : messageIdList) {
            Message message = messageRepository.findOne(messageId);
            messageList.add(message);
        }
        this.messageRepository.delete(messageList);
        return true;
    }
    
    @Transactional
    public boolean updateMessageListForUnRead(List<String> messageIdList) {
        List<Message> messageList = new ArrayList<Message>();
        for (String messageId : messageIdList) {
            Message message = messageRepository.findOne(messageId);
            message.setRead(false);
            messageList.add(message);
        }
        this.messageRepository.save(messageList);
        return true;
    }

    @Transactional(readOnly = true)
    public Message getMessage(String messageId) {
        Message message = this.messageRepository.findOne(messageId);
        return message;
    }

    @Transactional(readOnly = true)
    public int getMessageByUserIdAndRead(String userId) {
        return this.messageRepository.findByUserIdAndRead(userId, false, EMessageType.SMS);
    }

    @Transactional(readOnly = true)
    public int getMessagePageNumber(String userId, String messageId) {
    	int count = this.messageRepository.findMessagePageByUserIdAndMessageId(userId, messageId);
    	int page = count / 10 + 1;
    	return page;
    }
    
    /**
     * 发送给部门，管理员等（如风控部门下的所有人）
     * @param messageType
     * @param messageKey
     * @param role a group of user.
     * @param content  Pay attention here :  If rich text, the first place holder in message should be URL.<br/>
     * 		  For example : Hello User{1}, please welcome to <a href="{0}">DEMO!</a>.
     */
    @Transactional
    public void sendMessage(EMessageType messageType, String messageKey, List<EBizRole> roles, Object... args) {
    	LOGGER.info("sendMessageToGroup() invoked");
    	try {
    		List<Message> messageList = new ArrayList<Message>();
    		List<UserRole> users = new ArrayList<UserRole>();
    		for (EBizRole role : roles) {
    			RolePo rolePo = roleRepository.findByName(role.getCode());
            	if (rolePo == null) {
        			LOGGER.warn("Illegal Role {}", role.getName());
            		continue;
        		}
            	users.addAll(userRoleRepository.findByRoleId(rolePo.getRoleId()));
			}
            for (UserRole userRole : users) {
            	Message message = this.createMessage(messageType, messageKey, userRole.getUserId(), args);
            	messageList.add(message);
            }
        	if (EMessageType.SMS != messageType) {
                this.messageRepository.save(messageList);
        	} else {
        		// 发送短信
                List<String> phoneList = new ArrayList<String>();
                for (Message message : messageList) {
                    Member member = memberRepository.findByUserId(message.getUserId());
                    phoneList.add(member == null ? StringUtils.EMPTY : member.getContactMobile());
                }
                String[] phones = new String[phoneList.size()];
                System.arraycopy(phoneList.toArray(), 0, phones, 0, phoneList.size());
//                 batchSend(phones, content, null, PhoneMessageUtil.formatDate(new Date()));
        	}
		} catch (RuntimeException e) {
			LOGGER.error("Ex {}", e);
		}
    }
    
    /**
     * 发送给部门，管理员等（如风控部门下的所有人）
     * @param messageType
     * @param messageKey
     * @param role a group of user.
     * @param content  Pay attention here :  If rich text, the first place holder in message should be URL.<br/>
     * 		  For example : Hello User{1}, please welcome to <a href="{0}">DEMO!</a>.
     */
    @Transactional
    public void sendMessage(EMessageType messageType, String messageKey, EBizRole role, Object... args) {
    	LOGGER.info("sendMessageToGroup() invoked");
    	try {
        	RolePo rolePo = roleRepository.findByName(role.getCode());
        	if (rolePo == null) {
    			LOGGER.warn("Illegal Role {}", role.getName());
        		return;
    		}
            List<UserRole> userRoleList = userRoleRepository.findByRoleId(rolePo.getRoleId());
            List<Message> messageList = new ArrayList<Message>();
            for (UserRole userRole : userRoleList) {
            	Message message = this.createMessage(messageType, messageKey, userRole.getUserId(), args);
            	messageList.add(message);
            }
        	if (EMessageType.SMS != messageType) {
                this.messageRepository.save(messageList);
        	} else {
        		// 发送短信
                List<String> phoneList = new ArrayList<String>();
                for (Message message : messageList) {
                    Member member = memberRepository.findByUserId(message.getUserId());
                    phoneList.add(member == null ? StringUtils.EMPTY : member.getContactMobile());
                }
                String[] phones = new String[phoneList.size()];
                System.arraycopy(phoneList.toArray(), 0, phones, 0, phoneList.size());
//                 batchSend(phones, content, null, PhoneMessageUtil.formatDate(new Date()));
        	}
		} catch (RuntimeException e) {
			LOGGER.error("Ex {}", e);
		}
    }
    
    /**
     * 发送给单个用户.
     * @param messageType
     * @param messageKey message key.
     * @param userId individual user.
     * @param objects content. Pay attention here :  If rich text, the first place holder in message should be URL.<br/>
     * 		  For example : Hello User{1}, please welcome to <a href="{0}">DEMO!</a>.
     */
    @Transactional
    public void sendMessage(EMessageType messageType, String messageKey, String userId, Object... args) {
    	LOGGER.info("sendMessageToIndividual() invoked");
    	try {
    		if (EMessageType.SMS != messageType) {
            	Message message = createMessage(messageType, messageKey, userId, args);
            	this.messageRepository.save(message);
            	this.messageRepository.flush();
            } 
    		else {
            	SmsParamsDto smsDto = messageSendService.createSmsParamDto(userId, messageKey, args);
            	if(smsDto!=null){
            		this.send(smsDto.getMobile(), smsDto.getContent(), 
            				null, null, smsDto.getCid(), smsDto.getParams());
            	}
            	else{
            		LOGGER.debug(" return smsParamsDto is null, userId is {}, messageKey is {}", userId, messageKey);
            	}
            }
		} catch (RuntimeException e) {
			LOGGER.error("Ex {}", e);
		} catch (Exception ex) {
			LOGGER.error("Ex {}", ex);
		}
    }
    

    /**
     * create Message.
     * @param messageType
     * @param messageKey
     * @param richText
     * @param userId
     * @param content
     * @return
     */
	private Message createMessage(EMessageType messageType, String messageKey, String userId, Object... args) {
		String title = MessageUtil.getMessage(messageKey + TITLE_SUFFIX);
		String messageTitle = this.generateMessage(title, args);
		String messageContent = null;
		if (EMessageType.MESSAGE == messageType) {
			String contentMsg = MessageUtil.getMessage(messageKey + CONTENT_SUFFIX);
			messageContent = this.generateMessage(contentMsg, args);
		} else if (EMessageType.SMS == messageType) {
			String contentMsg = MessageUtil.getMessage(messageKey + "SMS_"+CONTENT_SUFFIX);
			messageContent = this.generateMessage(contentMsg, args);
		} else if (EMessageType.TODO == messageType) {
			String url = MessageUtil.getMessage(messageKey + URL_SUFFIX);
			String contentMsg = MessageUtil.getMessage(messageKey + RICH_CONTENT_SUFFIX);
			Object[] newArray = new Object[args.length + 1];
			newArray[0] = url;
			for (int i = 1; i < newArray.length; i++) {
				newArray[i] = args[i - 1];
			}
			messageContent = this.generateMessage(contentMsg, newArray);
		}
		return createMessagePo(userId, messageType, messageTitle, messageContent);
	}
    
    private Message createMessagePo(String userId, EMessageType messageType, String messageTitle, String content) {
        if (EMessageType.NULL != messageType) {
        	String temp = null;
        	try {
        		temp = securityContext.getCurrentUserId();
			} catch (Exception e) {
				temp = "SysUser";
			}
            Message message = new Message();
            message.setUserId(userId);
            message.setType(messageType);
            message.setTitle(messageTitle);
            message.setMessage(content);
            message.setCreateTs(new Date());
            message.setCreatorOpId(temp);
            message.setRead(false);
            return message;
        }
        return null;
    }

    /**
     * 
     * Description: 发短信
     * 
     * @param Mobile
     *            接收短信的手机号码 以数组的形式传入，最多支持100个号码
     * @param Content
     *            短信内容
     * @param Cell
     * @param SendTime
     *            定时发送时间，小于当前时间时立即发送. 定时时间标准格式为:yyyy-MM-dd HH:mm:ss，空为当前时间
     * @return
     */
     private String send(String mobile, String content, String Cell, String SendTime, String cid, String ...params) {
    	 return messageSendService.sendSMS(mobile, content, Cell, SendTime, cid, params);
     }
     
     /**
      * Send SMS by asynchronous mode.
      */
     //TODO 批量短信暂时无法使用
     @Transactional
     public void sendMessageViaJob() {
    	 
    	 LOGGER.info("sendMessageViaJob() invoked");
    	 
    	 Date currDate = new Date();
    	 
    	 List<SMSMessage> messages = smsMessageService.getBatchSendSmsMessageList(currDate);
    	 
    	 // update all to Complete no matter its status....
    	 smsMessageService.updateAllSmsMessageSendSuccess(currDate);
    	 
    	 // call sms api to send message
    	 List<String> failureList = this.messageSendService.batchSend(messages, null, null, null, null);
    	 
     }

    /**
     * 
     * Description: 替换message里面的参数，生成最终的消息
     * 
     * @param 源消息
     * @param 要设置的参数，可以传多个
     * @return
     */
    private String generateMessage(String message, Object... objects) {
    	return MessageFormat.format(message, objects);
    }
}
