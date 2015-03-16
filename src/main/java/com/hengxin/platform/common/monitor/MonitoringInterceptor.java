package com.hengxin.platform.common.monitor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.bull.javamelody.MonitoringSpringInterceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.FrozenUserException;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.BaseSecurityContext;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;

public class MonitoringInterceptor extends MonitoringSpringInterceptor {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringInterceptor.class);

    @Autowired
    private transient SecurityContext securityContext;
	
    @Autowired
    private transient UserRepository userRepository;
    
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long begin = System.currentTimeMillis();
		try {
			Method method = invocation.getMethod();
			String methodName = invocation.getMethod().getName();
			if (methodName.equals("pushTopics") || methodName.equals("purgeTopic") || method.isAnnotationPresent(Scheduled.class)) {
				
			} else {
				if (securityContext != null) {
					String userId = securityContext.getCurrentUserId();
					if (userId != null) {
						LOGGER.debug("Method {}, user id {}", methodName, userId);
						UserPo user = userRepository.findOne(userId);
						if (user != null) {
							String status = user.getStatus();
							if (status.equalsIgnoreCase("F")) {
								BaseSecurityContext.clearAuthcCache(userId);
								BaseSecurityContext.clearAuthzCache(userId);
//								BaseSecurityContext.logout();
								String message = MessageUtil.getMessage(EErrorCode.USER_FROZEN.getDisplayMsg());
								throw new FrozenUserException(EErrorCode.USER_FROZEN, message);
							}
						}
					}
				}
			}
			return super.invoke(invocation);
		} finally {
			long end = System.currentTimeMillis() - begin;
			RequestAttributes requestAttributes = RequestContextHolder
					.getRequestAttributes();
			if (requestAttributes != null) {
				HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
						.getRequest();
				LOGGER.debug(
						"[MON]{}.{}() completed in {}ms, request URI: {}, request params: {}",
						invocation.getMethod().getDeclaringClass()
								.getSimpleName(), invocation.getMethod()
								.getName(), end, request.getRequestURI(),
						mapToString(request.getParameterMap()));
			} else {
				LOGGER.debug("[MON]{}.{}() completed in {}ms", invocation
						.getMethod().getDeclaringClass().getSimpleName(),
						invocation.getMethod().getName(), end);
			}
		}
	}

	private String mapToString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Iterator<?> itor = map.entrySet().iterator();
		if (itor.hasNext()) {
			append(sb, (Entry<?, ?>) itor.next());
		}
		while (itor.hasNext()) {
			sb.append(",");
			append(sb, (Entry<?, ?>) itor.next());
		}
		sb.append("}");
		return sb.toString();
	}

	private void append(StringBuilder sb, Entry<?, ?> entry) {
		sb.append(entry.getKey());
		sb.append("=");
		sb.append(deepToString(entry.getValue()));
	}

	private String deepToString(Object object) {
		if (object instanceof Object[]) {
			return Arrays.deepToString((Object[]) object);
		} else {
			return String.valueOf(object);
		}
	}

}
