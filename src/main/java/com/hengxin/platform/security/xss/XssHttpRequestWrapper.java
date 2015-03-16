
/*
* Project Name: kmfex-platform-trunk
* File Name: XssHttpRequestWrapper.java
* Class Name: XssHttpRequestWrapper
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
	
package com.hengxin.platform.security.xss;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Name: XssHttpRequestWrapper
 * Description: wrap http request for sanitizing XSS attack parameters
 * @author zhengqingye
 *
 */
public class XssHttpRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(XssHttpRequestWrapper.class);


    private Map<String, String[]> sanitized;
    private Map<String, String[]> orig;
    
    @SuppressWarnings("unchecked")
    public XssHttpRequestWrapper(HttpServletRequest request) {
        super(request);
        orig = request.getParameterMap();  
        sanitized = getParameterMap();
        if (logger.isDebugEnabled())
                snzLog();
    }
    
   
    @Override
    public String getParameter(String name)
    {              
            String[] vals = getParameterMap().get(name);
            if (vals != null && vals.length > 0)
                    return vals[0];
            else        
                    return null;        
    }


    @Override
    public Map<String, String[]> getParameterMap()
    {      
            if (sanitized==null)
                    sanitized = sanitizeParamMap(orig);
            return sanitized;                      

    }

    @Override
    public String[] getParameterValues(String name)
    {      
            return getParameterMap().get(name);
    }


    private  Map<String, String[]> sanitizeParamMap(Map<String, String[]> raw)
    {              
            Map<String, String[]> res = new HashMap<String, String[]>();
            if (raw==null)
                    return res;
   
            for (String key : (Set<String>) raw.keySet())
            {                      
                    String[] rawVals = raw.get(key);
                    String[] snzVals = new String[rawVals.length];
                    for (int i=0; i < rawVals.length; i++)
                    {
                            PolicyFactory policy = Sanitizers.FORMATTING;
                            snzVals[i] = policy.sanitize(rawVals[i]);
                    }
                    res.put(key, snzVals);
            }                      
            return res;
    }


    private void snzLog()
    {
            for (String key : (Set<String>) orig.keySet())
            {
                    String[] rawVals = orig.get(key);
                    String[] snzVals = sanitized.get(key);
                    if (rawVals !=null && rawVals.length>0)
                    {
                            for (int i=0; i < rawVals.length; i++)
                            {
                                    if (rawVals[i].equals(snzVals[i]))                                                                                                                      
                                            logger.trace("Sanitization. Param seems safe: " + key + "[" + i + "]=" + snzVals[i]);                          
                                    else
                                            logger.debug("Sanitization. Param modified: " + key + "[" + i + "]=" + snzVals[i]);
                            }              
                    }
            }
    }

    

}
