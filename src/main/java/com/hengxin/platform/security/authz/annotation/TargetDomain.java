
/*
* Project Name: micro-finance
* File Name: TargetDomainObject.java
* Class Name: TargetDomainObject
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
	
package com.hengxin.platform.security.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hengxin.platform.security.authz.evaluator.DataPermissionEvaluator;


/**
 * Class Name: TargetDomainObject
 * Description: TODO
 * @author zhengqingye
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDomain {
    Class<? extends DataPermissionEvaluator> permEvaluaor();
}
