
/*
* Project Name: kmfex-platform-trunk
* File Name: SessionTimeoutAuthenticationFilter.java
* Class Name: SessionTimeoutAuthenticationFilter
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
	
package com.hengxin.platform.security.authc;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.ResultCode;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: SessionTimeoutAuthenticationFilter
 * Description: TODO
 * @author zhengqingye
 *
 */
public class SessionTimeoutAuthenticationFilter extends FormAuthenticationFilter {
    
    private static final Logger Log = LoggerFactory.getLogger(SessionTimeoutAuthenticationFilter.class);
    
    public static final String SESSION_TIMEOUT = SessionTimeoutAuthenticationFilter.class + "_session_timeout";
    
    private static final String SESSION_TIMEOUT_MSG = "error.common.session_timeout";
    
    //only part of session id is enough for identification
    private static final int SESSION_ID_SECTION_LEN = 15;

    private static final String DATE_FORMAT = "MM-dd HH:mm:ss";
    
    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        saveRequest(request);
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        if(WebUtil.isAjaxRequest(req)){
            ObjectMapper objectMapper = new ObjectMapper();
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); 
            ResultDto error = new ResultDto();
            error.setCode(ResultCode.SESSION_TIME_OUT);
            error.setMessage(MessageUtil.getMessage(SESSION_TIMEOUT_MSG));
            objectMapper.writeValue(response.getWriter(), error);
            Log.debug("session time out for ajax request:{}", req.getRequestURI());
        }else{
            Log.debug("session time out for request:{}", req.getRequestURI());
            req.getSession().setAttribute(SESSION_TIMEOUT, true);
            redirectToLogin(request, response);
        }
        HttpSession session = req.getSession(false);
        if (session != null) {
			Log.debug("session time out with id: {}, is sesion new:{}, started: {}, last accessed: {}, request headers: {}",
					session.getId(),
					session.isNew(),
					DateFormatUtils.format(session.getCreationTime(), DATE_FORMAT),
					DateFormatUtils.format(session.getLastAccessedTime(), DATE_FORMAT),
					getHeaderString(request));
        } else {
        	Log.debug("session time out, no session available for current request");
        }
    }
    
    private String getHeaderString(ServletRequest request ){
        HttpServletRequest req = (HttpServletRequest)request;
        StringBuilder sb = new StringBuilder();
        Enumeration<?> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            sb.append("[").append(headerName).append("=").append(req.getHeader(headerName)).append("]");
        }
        return sb.toString();
    }
    
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        boolean res = super.onPreHandle(request, response, mappedValue);
        if(SecurityContext.getInstance().isAuthenticated()){
            MDC.put(ApplicationConstant.LOG_USER_NAME, SecurityContext.getInstance().getCurrentUserName());
        }
        String sessionId = ((HttpServletRequest)request).getSession(false).getId();
        if(sessionId!=null){
            MDC.put(ApplicationConstant.LOG_SESSION_ID, sessionId.substring(sessionId.length()-SESSION_ID_SECTION_LEN));
        }
        return res;
    }
    
    @Override
    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws ServletException, IOException {
        MDC.remove(ApplicationConstant.LOG_USER_NAME);
        MDC.remove(ApplicationConstant.LOG_SESSION_ID);
        super.cleanup(request, response, existing);
    }
    
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
            ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest)request;
        Log.info("{} login complete, session ID: {} ", subject.getPrincipal(), subject.getSession().getId());
        StringBuilder sb = new StringBuilder();
        Enumeration<?> attriNames = req.getAttributeNames();
        while (attriNames.hasMoreElements()) {
            String attriName = (String) attriNames.nextElement();
            sb.append("[").append(attriName).append("=").append(req.getAttribute(attriName)).append("]");
        }
        Log.info("session attributes: {}", sb);
        return super.onLoginSuccess(token, subject, request, response);
    }
    
}
