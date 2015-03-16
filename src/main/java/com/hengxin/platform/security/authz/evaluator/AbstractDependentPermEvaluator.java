
/*
* Project Name: micro-finance
* File Name: DependentDataPermissionEvaluator.java
* Class Name: DependentDataPermissionEvaluator
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
	
package com.hengxin.platform.security.authz.evaluator;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.shiro.subject.Subject;

import com.hengxin.platform.security.authz.DataPermissionMissConfigException;
import com.hengxin.platform.security.authz.DataPermissionType;
import com.hengxin.platform.security.authz.PermEvalReflectionUtils;
import com.hengxin.platform.security.authz.annotation.DataOwnerDomain;
import com.hengxin.platform.security.authz.annotation.DataOwnerDomainId;
import com.hengxin.platform.security.authz.annotation.TargetDomain;


/**
 * Class Name: DependentDataPermissionEvaluator
 * Description: TODO
 * @author zhengqingye
 *
 */

public abstract class AbstractDependentPermEvaluator extends AbstractPermEvaluator {

    /* (non-Javadoc)
     * @see com.hengtiansoft.vmibatis.showcase.security.authz.aop.DataPermissionEvaluator#hasPermission(org.apache.shiro.subject.Subject, java.lang.Object, com.hengtiansoft.vmibatis.showcase.security.authz.aop.DataPermissionType)
     */
    @Override
    public boolean hasPermission(Subject subject, Object targetObject, DataPermissionType permType) {
        boolean annotationFound = false;
        for (Field field : PermEvalReflectionUtils.getAllFields(targetObject.getClass())) {
            if (field.isAnnotationPresent(DataOwnerDomain.class)) {
                annotationFound = true;
                field.setAccessible(true);
                
                try {
                    return getNextPermissionEvaluator(targetObject.getClass()).hasPermission(subject, field.get(targetObject), permType);
                } catch (IllegalArgumentException e) {
                    throw new DataPermissionMissConfigException(e);
                } catch (IllegalAccessException e) {
                    throw new DataPermissionMissConfigException(e);
                }
            } else if (field.isAnnotationPresent(DataOwnerDomainId.class)) {
                annotationFound = true;
                DataOwnerDomainId dataOwnerId = field.getAnnotation(DataOwnerDomainId.class);
                field.setAccessible(true);
                Object ownerIdField;
                try {
                    ownerIdField = field.get(targetObject);
                } catch (IllegalArgumentException e) {
                    throw new DataPermissionMissConfigException(e);
                } catch (IllegalAccessException e) {
                    throw new DataPermissionMissConfigException(e);
                }
                if(ownerIdField instanceof Serializable){
                    Serializable targetId = (Serializable)ownerIdField;
                    Class<?> dataOwnerClass = dataOwnerId.targetClass();
                    DataPermissionEvaluator permEval = getNextPermissionEvaluator(dataOwnerClass);
                    return permEval.hasPermission(subject, permEval.getTargetObject(targetId, dataOwnerClass.getName()), permType);
                }else{
                    throw new DataPermissionMissConfigException("All the domain Id field must be Serializable!");
                }
            }
        }
        if (!annotationFound) {
            throw new DataPermissionMissConfigException(
                    "A class annotated with a dependent perm evaluator need to be specified its data owner with @DataOwnerDomain or @DataOwnerDomainId annotations!");
        }
        return false;
    }
    
    
    protected DataPermissionEvaluator getNextPermissionEvaluator(Class<?> targetClass){
        TargetDomain targetDomain = targetClass.getAnnotation(TargetDomain.class);
        if (targetDomain == null) {
            throw new IllegalArgumentException("The target object must be annotated as TargetDomainObject!");
        }
        return appContext.getBean(targetDomain.permEvaluaor());
    }


}
