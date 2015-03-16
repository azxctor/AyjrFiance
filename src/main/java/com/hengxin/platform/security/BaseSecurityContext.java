/*
 * Project Name: kmfex-platform-trunk
 * File Name: SecurityContext.java
 * Class Name: SecurityContext
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

package com.hengxin.platform.security;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.common.service.CommonBusinessService;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.dto.InvestorLevelDto;
import com.hengxin.platform.member.enums.EInvestorLevel;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.risk.enums.ERiskBearLevel;
import com.hengxin.platform.risk.service.InvestRiskBearService;
import com.hengxin.platform.security.authc.PassThroughAuthenticationToken;
import com.hengxin.platform.security.authc.ShiroJdbcRealm;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: SecurityContext Description: TODO
 * 
 * @author zhengqingye
 * 
 */
public class BaseSecurityContext {

    private static final String USER_KEY = "shiro.user";

    private static final String INVS_LEVEL_KEY = "shiro.invs.level";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSecurityContext.class);
    
    private static final String CACHE_NAME_AUTHZ = "cache.name.authz";
    
    private static final String CACHE_NAME_AUTHC = "cache.name.authc";

    /**
     * Description: 获取当前用户的ID
     * 
     * @return
     */
    public String getCurrentUserId() {
        User user = getCurrentUser();
        if(user!=null){
            return user.getUserId();
        }
        return null;
    }

    public String getCurrentOwnerId() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getOwnerId();
        }
        return null;
    }

    /**
     * Description: 获取当前的用户名
     * 
     * @return
     */
    public String getCurrentUserName() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    /**
     * Description: 获取当前用户的基本信息。
     * 
     * @return
     */
    public User getCurrentUser() {
        Subject subject = getSubject();
        if(subject==null){
            return null;
        }
        return (User) subject.getSession().getAttribute(USER_KEY);
    }
    
    public InvestorLevelDto getInvestorLevelInfo() {
    	Subject subject = getSubject();
        if(subject==null){
            return null;
        }
        InvestorLevelDto eLevel = null;
        if(this.hasPermission(Permissions.INVESTOR_MEMBER)){
        	Object levelObj = subject.getSession().getAttribute(INVS_LEVEL_KEY);
        	if (levelObj == null) {
	        	MemberService memberService = ApplicationContextUtil.getBean(MemberService.class);
	        	InvestorInfo invsInfo = memberService.getInvestorById(this.getCurrentUserId());
	        	String invsLevelCode = null;
	        	if (invsInfo != null) {
	        		invsLevelCode = invsInfo.getInvestorLevel();
	        		if (StringUtils.isNotBlank(invsLevelCode)) {
	        			CommonBusinessService businessService = ApplicationContextUtil.getBean(CommonBusinessService.class);
	        			Map<String, InvestorLevelDto> map = businessService.getInvestorLevelInfo();
	        			if (map.containsKey(invsLevelCode)) {
	        				eLevel = map.get(invsLevelCode);
						}
	        		}
	        	}
	        	if (eLevel!=null) {
	        		subject.getSession().setAttribute(INVS_LEVEL_KEY, eLevel);
	        	} else {
	        		eLevel = new InvestorLevelDto();
	        		eLevel.setLevel(EInvestorLevel.COMMON);
	        		eLevel.setRate(BigDecimal.ZERO);
	        		subject.getSession().setAttribute(INVS_LEVEL_KEY, eLevel);
	        	}
        	} else {
        		eLevel = (InvestorLevelDto) levelObj;
        	}
        	/** display investor risk level. **/
			InvestRiskBearService riskService = ApplicationContextUtil.getBean(InvestRiskBearService.class);
			ERiskBearLevel riskLevel = riskService.getUserRiskBearLevel(this.getCurrentUserId());
			eLevel.setRiskBearLevel(riskLevel);
//			if (ERiskBearLevel.ROOKIE != riskLevel) {
				eLevel.setDisplay(true);
//			}
        }
    	return eLevel;
    }
    
    /**
    * Description: 是否已登录
    *
    * @return
    */
    public boolean isAuthenticated(){
        Subject subject = getSubject();
        if(subject==null){
            return false;
        }
        return getSubject().isAuthenticated();
    }

    /**
     * Description: 登录验证该用户名, 登陆登陆成功后会创建新的会话。
     * 
     * @param userId
     * @param password
     * @return the new session
     * @throws IncorrectCredentialsException
     *             密码错误
     * @throws LockedAccountException
     *             登陆失败次数过多导致账户被锁
     */
    public static Session login(String userName, String password) throws IncorrectCredentialsException,
            LockedAccountException {
        long start = System.currentTimeMillis();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject currentUser = SecurityUtils.getSubject();
        // This is to prevent session fixation attack, see: https://issues.apache.org/jira/browse/SHIRO-170
        currentUser.getSession().stop();
        // this will create a new session by default in applications that allow session state:
        currentUser.login(token);
        Session session = currentUser.getSession();
        LOGGER.debug("User {} login successfully, session id {}", userName, session.getId());
        UserService userService = ApplicationContextUtil.getBean(UserService.class);
        User user = userService.getUserByUserName(userName);
        if(user.getLastLoginTs()==null){
        	user.setLastLoginTs(new Date());
        }
        session.setAttribute(USER_KEY, user);
        long end = System.currentTimeMillis();
        LOGGER.debug("login() completed for user {}, total time spent: {}ms", userName, end-start);
        return session;
    }

	/**
	 * 无需密码，模拟登陆
	 * 
	 * @param userName
	 */
	public static void simulateLogin(String userName) {
		PassThroughAuthenticationToken token = new PassThroughAuthenticationToken(
				userName);
		SecurityUtils.getSubject().login(token);
	}

	/**
     * 登出当前用户
     */
	public static void logout() {
		getSubject().logout();
	}

	/**
	 * 重新设置用户最新信息, 目前在修改登录名后使用.
	 * 
	 * @param userId
	 */
	public static void resetUser(String userId) {
		UserService userService = ApplicationContextUtil.getBean(UserService.class);
		User user = userService.getUserByUserId(userId);
		Subject currentUser = SecurityUtils.getSubject();
        currentUser.getSession().setAttribute(USER_KEY, user);
	}

    /**
     * Description: 清除指定用户的授权信息缓存。
     * 
     * @param userId
     */
    public static void clearAuthzCache(String userName) {
        RealmSecurityManager sm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        for (Realm realm : sm.getRealms()) {
            if (realm instanceof ShiroJdbcRealm) {
                ShiroJdbcRealm jdbcRealm = (ShiroJdbcRealm) realm;
                SimplePrincipalCollection spc = new SimplePrincipalCollection(userName, realm.getName());
                jdbcRealm.clearAuthorizationCache(spc);
            }
        }
        LOGGER.info("Authorization cache cleared for user: {}", userName);
    }
    
    /**
    * Description: 清除指定用户列表的授权信息缓存。
    *
    * @param users
    */
    public static void clearAuthzCache(List<String> users){
        for(String user :users){
            clearAuthzCache(user);
        }
    }
    
    /**
     * Description: 清除所有用户的授权信息缓存。
     *
     * @param users
     */
    public static void clearAllAuthzCache(){
        CacheManager cm  = (CacheManager)((CachingSecurityManager)SecurityUtils.getSecurityManager()).getCacheManager();
        cm.getCache(AppConfigUtil.getConfig(CACHE_NAME_AUTHZ)).clear();
    }
    
    /**
     * Description: 清除指定用户的认证信息缓存。
     * 
     * @param userId
     */
    public static void clearAuthcCache(String userName) {
        RealmSecurityManager sm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        for (Realm realm : sm.getRealms()) {
            if (realm instanceof ShiroJdbcRealm) {
                ShiroJdbcRealm jdbcRealm = (ShiroJdbcRealm) realm;
                SimplePrincipalCollection spc = new SimplePrincipalCollection(userName, realm.getName());
                jdbcRealm.clearAuthenticationCache(spc);
            }
        }
    }
    
    /**
    * Description: 清除指定用户list的认证信息缓存。
    *
    * @param users
    */
    public static void clearAuthcCache(List<String> users){
        for(String user :users){
            clearAuthcCache(user);
        }
    }
    
    /**
     * Description: 清除所有用户的认证信息缓存。
     *
     * @param users
     */
    public static void clearAllAuthcCache(){
        CacheManager cm  = (CacheManager)((CachingSecurityManager)SecurityUtils.getSecurityManager()).getCacheManager();
        cm.getCache(AppConfigUtil.getConfig(CACHE_NAME_AUTHC)).clear();
    }
    
    /**
     * Description: 清除当前用户的授权信息缓存
     * 
     */
    public void clearCurrentAuthzCache() {
        clearAuthzCache(getSubject().getPrincipal().toString());
    }

    /**
     * Description: 验证当前用户是否拥有该权限。
     * 
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        Subject subject = getSubject();
        return subject == null ? false : subject.isPermitted(permission);
    }

    /**
     * Description: 验证当前用户是否拥有所有以下权限。
     * 
     * @param permissions
     * @return
     */
    public boolean hasAllPermissions(String... permissions) {
        Subject subject = getSubject();
        return subject == null ? false : subject.isPermittedAll(permissions);
    }

    /**
     * Description: 验证当前用户是否拥有以下任意一个权限
     * 
     * @param permissions
     * @return
     */
    public static boolean hasAnyPermission(String[] permissions) {
        Subject subject = getSubject();
        if (subject != null && permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission != null && subject.isPermitted(permission.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查是否有权限，若无则抛出异常。
     * 
     * @see org.apache.shiro.subject.Subject#checkPermission(String permission)
     * @param permission
     * @throws AuthorizationException
     */
    public void checkPermission(String permission) throws AuthorizationException {
        Subject subject = getSubject();
        if(subject==null){
            throw new AuthorizationException("No permission as there is no subject bound.");
        }
        subject.checkPermission(permission);
    }


    /**
     * Description: 验证当前用户是否属于以下所有角色。请通过权限而不是角色做判断，比如hasPermission。
     * 
     * @param roles
     * @return
     */
    @Deprecated
    public boolean hasAllRoles(Collection<String> roles) {
        return getSubject().hasAllRoles(roles);
    }

    /**
     * Description: 验证当前用户是否属于以下任意一个角色。请通过权限而不是角色做判断，比如hasPermission。
     * 
     * @param roleNames
     * @return
     */
    @Deprecated
    public boolean hasAnyRoles(Collection<String> roleNames) {
        Subject subject = getSubject();
        if (subject != null && roleNames != null) {
            for (String role : roleNames) {
                if (role != null && subject.hasRole(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Subject getSubject() {
        try {
            return SecurityUtils.getSubject();
        } catch (Exception e) {
            LOGGER.warn("Failed to get Subject, maybe user is not login or session is lost:", e);
            return null;
        }
    }

}
