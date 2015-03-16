
/*
* Project Name: kmfex-platform-trunk
* File Name: RoleRepository.java
* Class Name: RoleRepository
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
	
package com.hengxin.platform.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.security.entity.RolePo;


/**
 * Class Name: RoleRepository
 * Description: TODO
 * @author zhengqingye
 *
 */

public interface RoleRepository extends JpaRepository<RolePo, String> {
    
    public RolePo findByName(String name);
    
}
