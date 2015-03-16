
/*
* Project Name: kmfex-platform-trunk
* File Name: BrowserDetectionInterceptor.java
* Class Name: BrowserDetectionInterceptor
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
	
package com.hengxin.platform.common.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hengxin.platform.common.constant.UrlConstant;

/**
 * Class Name: BrowserDetectionInterceptor
 * Description: TODO
 * @author zhengqingye
 *
 */
public class BrowserDetectionInterceptor extends HandlerInterceptorAdapter {
    
    private static final String[] FORBIDEN_BROWSERS = {"MSIE 6","MSIE 7","MSIE 8"};
	private static final String[] BROWSERS_PERMIT_URLS = { UrlConstant.RECHARGE_OPEN_IE_TO_BANK_URL };
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
    	String requestURI = request.getRequestURI();
        if(permitUrl(requestURI)){
        	return true;
        }
        String userAgent = request.getHeader("User-Agent");
        for(String browser : FORBIDEN_BROWSERS){
            if(userAgent.contains(browser)){
                response.sendRedirect(request.getContextPath()+"/web/"+UrlConstant.ERROR_BROWSER_COMPATABILITY);
                response.flushBuffer();
                return false;
            }
        }
        return true;
    }
    
    private boolean permitUrl(String reqUrl){
        if(reqUrl.contains(UrlConstant.ERROR_BROWSER_COMPATABILITY)){
            return true;
        }
        for(String url : BROWSERS_PERMIT_URLS){
			if (reqUrl.contains(url)) {
        		return true;
        	}
        }
    	return false;
    }
}
