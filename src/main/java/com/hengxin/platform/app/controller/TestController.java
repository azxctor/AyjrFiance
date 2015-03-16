/*
 * Project Name: kmfex-platform
 * File Name: TestController.java
 * Class Name: TestController
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
package com.hengxin.platform.app.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.hengxin.platform.common.util.AppConfigUtil;

/**
 * TestController.
 *
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestController.class);

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	private Map<String, HandlerMethod> handlerMap = new HashMap<String, HandlerMethod>();

	@PostConstruct
	public void init() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.handlerMapping
				.getHandlerMethods();
		for (Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods
				.entrySet()) {
			RequestMappingInfo mapping = item.getKey();
			HandlerMethod method = item.getValue();
			for (String url : mapping.getPatternsCondition().getPatterns()) {
				if (!url.startsWith(TestController.class.getAnnotation(
						RequestMapping.class).value()[0])) { // exclude "/test*"
					handlerMap.put(url, method);
				}
			}
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpSession session) {
		if (AppConfigUtil.isProdEnv()) {
			return "error/404";
		}
		session.setAttribute("urls", new HashSet<String>(handlerMap.keySet()));
		return "app/test";
	}

	@RequestMapping(value = "/sample")
	@ResponseBody
	public Object sample(@RequestParam String url) {
		if (handlerMap.containsKey(url)) {
			Map<String, Object> result = new HashMap<String, Object>();
			RequestMethod[] methods = handlerMap.get(url)
					.getMethodAnnotation(RequestMapping.class).method();
			RequestMethod method = methods.length == 1 ? methods[0]
					: RequestMethod.GET;
			result.put("method", method);
			for (MethodParameter param : handlerMap.get(url)
					.getMethodParameters()) {
				if (param.getParameterAnnotation(RequestBody.class) != null) {
					try {
						Object request = param.getParameterType().newInstance();
						result.put("request", request);
					} catch (Exception e) {
						LOGGER.debug(
								"error instantiating param class {}, caused by {}",
								param.getParameterType(), e);
					}
					break;
				}
			}
			return result;
		}
		return null;
	}

}
