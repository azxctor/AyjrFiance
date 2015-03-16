/*
 * Project Name: kmfex-platform
 * File Name: MemberService.java
 * Class Name: MemberService
 *
 * Copyright 2014 KMFEX Inc
 *
 *
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.repository.UserRoleRepository;
import com.hengxin.platform.security.entity.RolePo;
import com.hengxin.platform.security.repository.RoleRepository;

/**
 * Class Name: UserRoleService
 * 
 * @author Ryan
 * 
 */
@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userrepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserRole getUserRole(String userId, String name) {

        return userrepository.findByUserIdAndRoleId(userId, roleRepository.findByName(name).getRoleId());

    }

    public List<UserRole> getUserRoleByUserId(String userId) {

        return userrepository.findByUserId(userId);

    }

    public RolePo getRoleById(String userId) {

        return roleRepository.findOne(userId);

    }
}
