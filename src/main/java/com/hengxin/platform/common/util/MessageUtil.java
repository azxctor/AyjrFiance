package com.hengxin.platform.common.util;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

public final class MessageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtil.class);

    private static MessageSource messageSource;

    private static Locale defaultLocale = Locale.SIMPLIFIED_CHINESE;

    /**
     * 
     * Description: Get simplified Chinese error message
     * 
     * @param code
     * @return
     */
    public static String getMessage(String code) {
        return getMessage(code, defaultLocale);
    }

    public static String getMessage(String code, Object... args) {
        return getMessage(code, defaultLocale, args);
    }

    public static String getMessage(String code, Locale locale, Object... args) {
        LOGGER.debug("getMessage() invoked,  Message code: " + code);
        return messageSource.getMessage(code, args, "Unknown message, code: " + code, locale);
    }

    public static void setMessageSource(MessageSource messageSource) {
        MessageUtil.messageSource = messageSource;
    }

}
