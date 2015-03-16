
/*
* Project Name: kmfex-platform
* File Name: AbstractSpringDataPermissionEvaluator.java
* Class Name: AbstractSpringDataPermissionEvaluator
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

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Class Name: AbstractSpringDataPermissionEvaluator
 * Description: TODO
 * @author zhengqingye
 *
 */

public abstract class AbstractSpringDataPermissionEvaluator extends AbstractPermEvaluator {
    @Autowired
    private SpringDataDataProvider springDataDp;
    
    /* (non-Javadoc)
     * @see com.hengxin.platform.security.authz.evaluator.AbstractDataPermissionEvaluator#getObject(java.io.Serializable, java.lang.String)
     */

    @Override
    protected Object getObject(Serializable targetId, String targetType) {
        return springDataDp.getTargetObject(targetId, targetType);
    }

}
