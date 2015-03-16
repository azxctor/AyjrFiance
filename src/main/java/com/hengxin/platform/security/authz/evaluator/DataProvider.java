
/*
* Project Name: kmfex-platform
* File Name: DataProvider.java
* Class Name: DataProvider
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


/**
 * Class Name: DataProvider
 * Description: TODO
 * @author zhengqingye
 *
 */

public interface DataProvider {
    Object getTargetObject(Serializable targetId, String targetType);
}
