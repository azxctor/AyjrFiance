
/*
* Project Name: micro-finance
* File Name: DataPermissionEvaluator.java
* Class Name: DataPermissionEvaluator
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

import com.hengxin.platform.security.authz.DataPermissionType;


/**
 * Class Name: DataPermissionEvaluator
 * Description: TODO
 * @author zhengqingye
 *
 */

public interface DataPermissionEvaluator extends DataProvider{
    boolean hasPermission(Subject subject, Serializable targetId, String targetType, DataPermissionType permType);
    boolean hasPermission(Subject subject, Object targetObject, DataPermissionType permType);
}
