
/*
* Project Name: kmfex-platform-trunk
* File Name: AuthorizationService.java
* Class Name: AuthorizationService
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
	
package com.hengxin.platform.security.service;


import com.hengxin.platform.security.enums.EBizRole;


/**
 * Class Name: AuthorizationService.
 * Description: TODO
 * @author zhengqingye
 *
 */

public interface AuthorizationService {
    
    /**
    * Description: 为指定用户赋角色.
    *
    * @param role
    * @param userName
    */
    	
    void assignRole(EBizRole role, String userName);
    
    
    /**
     * Description: 去除指定用户角色.
     *
     * @param role
     * @param userName
     */
         
    void revokeRole(EBizRole role, String userName);
    
    boolean isInvestor(String userId);
    boolean isFinancier(String userId);
    boolean isProdServ(String userId);
    boolean isAthdServCenter(String userId);
    boolean isTourist(String userId);
    boolean isAgencyTourist(String userId);
}
