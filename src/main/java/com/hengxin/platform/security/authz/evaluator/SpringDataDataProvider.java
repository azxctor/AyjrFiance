
/*
* Project Name: kmfex-platform
* File Name: SpringDataDataProvider.java
* Class Name: SpringDataDataProvider
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

import org.apache.commons.lang.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;

import com.hengxin.platform.security.authz.DataPermissionMissConfigException;


/**
 * Class Name: SpringDataDataProvider
 * Description: TODO
 * @author zhengqingye
 *
 */
@Component
public class SpringDataDataProvider implements DataProvider, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataDataProvider.class);
    private static final String METHOD_FIND_BY_ID = "findOne";
    
    private ApplicationContext appContext;
    
    private Repositories repos;
    
    /* (non-Javadoc)
     * @see com.hengxin.platform.security.authz.evaluator.DataProvider#getTargetObject(java.io.Serializable, java.lang.String)
     */

    @Override
    public Object getTargetObject(Serializable targetId, String targetType) {
        Class<?> targetClass;
        try {
            targetClass = Class.forName(targetType);
            return MethodUtils.invokeMethod(repos.getRepositoryFor(targetClass),METHOD_FIND_BY_ID, targetId);
        } catch (Exception e) {
            LOGGER.error("Failed to get the target object with Id:{}, target type: {}", targetId, targetType );
            throw new DataPermissionMissConfigException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
        repos = new Repositories(appContext);
    }

    //for unit test
    public void setRepos(Repositories repos) {
        this.repos = repos;
    }

}
