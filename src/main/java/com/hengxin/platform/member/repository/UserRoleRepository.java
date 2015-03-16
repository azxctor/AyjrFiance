/*
 * Project Name: kmfex-platform
 * File Name: AgencyRepository.java
 * Class Name: AgencyRepository
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

package com.hengxin.platform.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.domain.UserRolePk;

/**
 * Class Name: AgencyRepository Description: TODO
 * 
 * @author chunlinwang
 * 
 */

public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePk> {

    public List<UserRole> findByUserId(String userId);

    public UserRole findByUserIdAndRoleId(String userId, String roleId);
    
    public List<UserRole> findByRoleId(String roleId);

    @Query(value = "select ROLE_NAME from um_role a inner join um_user_role b on a.ROLE_ID = b.ROLE_ID inner join um_role_perm c on c.ROLE_ID = a.ROLE_ID inner join um_perm d on c.PERM_ID = d.PERM_ID where d.PERM_NAME= ?1 and b.USER_ID =?2", nativeQuery = true)
    public List<Object> getRoleByPerm(String permId, String userId);

}
