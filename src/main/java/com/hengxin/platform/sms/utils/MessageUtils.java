package com.hengxin.platform.sms.utils;

import java.text.MessageFormat;

public final class MessageUtils {

    public static String formatMessage(String message, Object... objects) {
    	try {
    		return MessageFormat.format(message, objects);
    	} catch(Exception ex){
    		return message;
    	}
    }
}
