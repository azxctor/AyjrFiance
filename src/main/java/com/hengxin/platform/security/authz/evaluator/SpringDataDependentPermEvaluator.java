
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Class Name: DependentDataPermissionEvaluator
 * Description: TODO
 * @author zhengqingye
 *
 */
@Component
public class SpringDataDependentPermEvaluator extends AbstractDependentPermEvaluator {

    @Autowired
    private SpringDataDataProvider springDataDp;
    /* (non-Javadoc)
     * @see com.hengtiansoft.vmibatis.showcase.security.authz.aop.AbstractDataPermissionEvaluator#getTargetObject(java.lang.Object, java.lang.String)
     */

    @Override
    public Object getObject(Serializable  targetId, String targetType) {
        return springDataDp.getTargetObject(targetId, targetType);
    }

}
