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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.repository.UserRoleRepository;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.RolePo;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.repository.RoleRepository;

/**
 * Class Name: AuthorizationService Description: TODO
 * 
 * @author zhengqingye
 * 
 */
@Service
@Transactional
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void assignRole(EBizRole role, String userId) {
        Date currentDate = new Date();
        RolePo newRole = roleRepo.findByName(role.getCode());

        UserRole currentUserRole = userRoleRepository.findByUserIdAndRoleId(userId, newRole.getRoleId());
        if (currentUserRole != null) {
            return;
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(newRole.getRoleId());
        userRole.setCreateTs(currentDate);
        userRoleRepository.save(userRole);
        
        String userName = userRepository.findUserByUserId(userId).getUsername();
        SecurityContext.clearAuthzCache(userName);
    }

    /**
     * 
     * Description: 判断制定用户是否为投资会员
     * 
     * @param userId
     * @return
     */
    public boolean isInvestor(String userId) {
        List<Object> roleByPerm = userRoleRepository.getRoleByPerm(Permissions.INVESTOR_MEMBER, userId);
        return roleByPerm != null && roleByPerm.size() > 0;
    }

    /**
     * 
     * Description: 判断制定用户是否为融资会员
     * 
     * @param userId
     * @return
     */
    public boolean isFinancier(String userId) {
        List<Object> roleByPerm = userRoleRepository.getRoleByPerm(Permissions.FINANCIER_MEMBER, userId);
        return roleByPerm != null && roleByPerm.size() > 0;
    }

    /**
     * 
     * Description: 判断制定用户是否为担保机构
     * 
     * @param userId
     * @return
     */
    public boolean isProdServ(String userId) {
        List<Object> roleByPerm = userRoleRepository.getRoleByPerm(Permissions.PROD_SERV_MEMBER, userId);
        return roleByPerm != null && roleByPerm.size() > 0;
    }

    /**
     * 
     * Description: 判断制定用户是否为服务中心
     * 
     * @param userId
     * @return
     */
    public boolean isAthdServCenter(String userId) {
        List<Object> roleByPerm = userRoleRepository.getRoleByPerm(Permissions.ATHD_CENTER_MEMBER, userId);
        return roleByPerm != null && roleByPerm.size() > 0;
    }

    @Override
    public void revokeRole(EBizRole role, String userId) {
        RolePo deleteRole = roleRepo.findByName(role.getCode());

        UserRole currentUserRole = userRoleRepository.findByUserIdAndRoleId(userId, deleteRole.getRoleId());
        if (currentUserRole == null) {
            return;
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(deleteRole.getRoleId());
        userRoleRepository.delete(userRole);

        String userName = userRepository.findUserByUserId(userId).getUsername();
        SecurityContext.clearAuthzCache(userName);

    }

    /**
     * 投融资游客
     */
    @Override
    public boolean isTourist(String userId) {
        List<Object> roleByPerm = userRoleRepository.getRoleByPerm(Permissions.TOURIST_MEMBER, userId);
        return roleByPerm != null && roleByPerm.size() > 0;
    }

    /**
     * 合作机构游客
     */

    @Override
    public boolean isAgencyTourist(String userId) {
        List<Object> roleByPerm = userRoleRepository.getRoleByPerm(Permissions.TOURIST_AGENCY_MEMBER, userId);
        return roleByPerm != null && roleByPerm.size() > 0;
    }
}
