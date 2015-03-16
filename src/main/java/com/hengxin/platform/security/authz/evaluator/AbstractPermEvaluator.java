
/*
* Project Name: micro-finance
* File Name: AbstractDataPermissionEvaluator.java
* Class Name: AbstractDataPermissionEvaluator
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

import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hengxin.platform.security.authz.DataPermissionType;
import com.hengxin.platform.security.authz.InvalidTargetIdException;


/**
 * Class Name: AbstractDataPermissionEvaluator
 * Description: TODO
 * @author zhengqingye
 *
 */
public abstract class AbstractPermEvaluator implements DataPermissionEvaluator, ApplicationContextAware {

    protected ApplicationContext appContext;
    
    protected abstract Object getObject(Serializable targetId, String targetType);
    /* (non-Javadoc)
     * @see com.hengtiansoft.vmibatis.showcase.security.authz.aop.DataPermissionEvaluator#hasPermission(org.apache.shiro.subject.Subject, java.lang.Object, java.lang.String, com.hengtiansoft.vmibatis.showcase.security.authz.aop.DataPermissionType)
     */

    @Override
    public boolean hasPermission(Subject subject, Serializable targetId, String targetType, DataPermissionType permType) {
        return hasPermission(subject, getTargetObject(targetId, targetType) , permType);
    }
    
    public Object getTargetObject(Serializable targetId, String targetType){
        Object targetObj = getObject(targetId, targetType);
        if(targetObj==null){
            throw new InvalidTargetIdException("No data exists for this target Id: "+ targetId);
        }
        return targetObj;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

}
