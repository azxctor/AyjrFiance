package com.hengxin.platform.common.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.hengxin.platform.common.dto.EmailDto;

//@Service
public class EmailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
	
//	@Autowired
	private JavaMailSender mailSender;
	
//	@Autowired
	private VelocityEngine velocityEngine;

//	@Autowired
	private String sender;
	
//	@Autowired
	private String webAddress;
	
	/**
	 * send mail.
	 * @param email
	 */
	public void sendMessage(EmailDto email) {
		email.setUrl(this.accessUrl(email.getUserId()));
		send(sender, email);
	}

	private void send(final String sender, final EmailDto email) {
		LOGGER.info("sendMessage() invoked");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setSubject("小薇金融欢迎您");
				message.setTo(email.getEmail());
				message.setFrom(sender);
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("user", email);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mail_template.vm", "UTF-8", model);
				message.setText(text, true);
			}
		};
		this.mailSender.send(preparator);
		LOGGER.debug("sendMessage() completed");
	}
	
	private String accessUrl(String userId) {
		StringBuilder sb = new StringBuilder();
		sb.append(webAddress).append("/mail/active/").append(userId);
		return sb.toString();
	}
}
