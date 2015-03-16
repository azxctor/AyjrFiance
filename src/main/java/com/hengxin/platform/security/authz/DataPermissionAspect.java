/*
* Project Name: micro-finance
* File Name: DataPermissionAspect.java
* Class Name: DataPermissionAspect
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
	
package com.hengxin.platform.security.authz;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.hengxin.platform.security.authz.annotation.CheckDataPermission;
import com.hengxin.platform.security.authz.annotation.IntermediumObject;
import com.hengxin.platform.security.authz.annotation.TargetDomain;
import com.hengxin.platform.security.authz.annotation.TargetDomainId;
import com.hengxin.platform.security.authz.evaluator.DataPermissionEvaluator;


/**
 * Class Name: DataPermissionAspect
 * Description: TODO
 * @author zhengqingye
 *
 */
@Aspect
@Component
public class DataPermissionAspect implements ApplicationContextAware{

    private ApplicationContext appContext;

    @Around("@annotation(com.hengxin.platform.security.authz.annotation.CheckDataPermission)")
    public Object checkDataPermission(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            throw new RuntimeException("No target domain found in the method invocation!");
        } else {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            DataPermissionType requiredPermType = method.getAnnotation(CheckDataPermission.class).requiredPermType();
            boolean targetFound =  checkPermissionOnDomainObject(args, requiredPermType, method);
            if(!targetFound){
                targetFound = checkPermissionOnDomainId(args, requiredPermType, method);
            }
            if(!targetFound){
                throw new DataPermissionMissConfigException("The target object must be annotated as TargetDomain or TargetDomainId!");
            }else{
                return joinPoint.proceed();
            }
        }
    }
    private boolean checkPermissionOnDomainObject(Object[] args, DataPermissionType requiredPermission, Method method) {
        boolean targetObjFound = false;
        for (Object arg : args) {
            TargetDomain targetDomain = arg.getClass().getAnnotation(TargetDomain.class);
            if (targetDomain != null) {
                targetObjFound=true;
                DataPermissionEvaluator permEvaluator = appContext.getBean(targetDomain.permEvaluaor());
                if (!permEvaluator.hasPermission(SecurityUtils.getSubject(), arg, requiredPermission)) {
                    throw new UnauthorizedDataException("Access denied on the current object: " + arg.toString());
                }
            } else {
                IntermediumObject intermeduim = arg.getClass().getAnnotation(IntermediumObject.class);
                if(intermeduim!=null){
                    List<Field> fields =PermEvalReflectionUtils.getAllFields(arg.getClass());
                    for(Field field: fields){
                        TargetDomainId targetDomainId = field.getAnnotation(TargetDomainId.class);
                        if(targetDomainId!=null){
                            targetObjFound = true;
                            try {
                                checkAnnotatedTargetId(FieldUtils.readField(field, arg, true), targetDomainId, requiredPermission);
                            }catch (IllegalAccessException e) {
                                throw new DataPermissionMissConfigException(e);
                            }
                            break;
                        }
                    }
                }else{
                    continue;
                }
            }
        }
        return targetObjFound;
    }

    private boolean checkPermissionOnDomainId(Object[] params, DataPermissionType requiredPermission, Method method) {
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        boolean idFound = false;
        for (int i = 0; i < paramAnnotations.length; i++) {
            if (paramAnnotations[i].length == 0) {
                continue;
            }
            for (int j = 0; j < paramAnnotations[i].length; j++) {
                if (paramAnnotations[i][j] instanceof TargetDomainId) {
                    idFound = true;
                    checkAnnotatedTargetId(params[i],(TargetDomainId) paramAnnotations[i][j], requiredPermission);
                    return true;
                }
            }
        }
        return idFound;
    }
    
    private void checkAnnotatedTargetId( Object obj, TargetDomainId targetDomainId, DataPermissionType requiredPermission  ){
        Serializable targetId;
        if(obj instanceof Serializable){
            targetId = (Serializable)obj;
        }else{
            throw new DataPermissionMissConfigException("The target Id parameter is nor Serialiazable!");
        }
        TargetDomain targetDomain = targetDomainId.targetClass().getAnnotation(TargetDomain.class);
        if(targetDomain==null){
            throw new DataPermissionMissConfigException("Wrong configuration: The target domain object has not been annotated as TargetDomain yet!");
        }
        DataPermissionEvaluator permEvaluator = appContext.getBean(targetDomain.permEvaluaor());
        if (!permEvaluator.hasPermission(SecurityUtils.getSubject(), targetId, targetDomainId.targetClass().getName(),
                requiredPermission)) {
            throw new UnauthorizedDataException("Access denied on the current object, the Id: " + targetId.toString());
        } 
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
    
}
