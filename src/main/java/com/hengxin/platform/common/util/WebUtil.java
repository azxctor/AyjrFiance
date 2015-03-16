package com.hengxin.platform.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hengxin.platform.security.xss.XssSantizeJsonSerializer;

@Component
public class WebUtil {
    
    private static ObjectMapper objectMapper;

    public HttpServletRequest getThreadRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    public HttpSession getThreadSession() {
        return getThreadRequest().getSession(true);
    }
    
    public boolean isAjaxRequest(){
        return isAjaxRequest(getThreadRequest());
    }
    
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith)
                : false;
    }
    
    public String getFullUrlBasedOn(String path){
        StringBuilder targetUrl = new StringBuilder();
        if (path.startsWith("/")) {
            // Do not apply context path to relative URLs.
            targetUrl.append(getThreadRequest().getContextPath());
        }
        targetUrl.append(path);
        return targetUrl.toString();
    }
    
    public static final ObjectMapper getObjectMapper(){
        if(objectMapper==null){
            objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("kmfex Custom Serializer", new Version(1, 0, 0, "FINAL"));
            module.addSerializer(new XssSantizeJsonSerializer());
            module.addSerializer(new PageEnumSerializer());
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }

}
