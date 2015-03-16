/*
 * Project Name: kmfex-platform
 * File Name: UserRepository.java
 * Class Name: UserRepository
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

package com.hengxin.platform.member.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: UserRepository
 * 
 * @author shmilywen
 * 
 */

public interface UserRepository extends JpaRepository<UserPo, String>, JpaSpecificationExecutor<UserPo> {

    UserPo findUserByUserId(String userId);

    UserPo findUserByUsername(String username);

    List<UserPo> findUserByNameIgnoreCase(String name);
    
    UserPo findUserByUsernameIgnoreCase(String username);

    List<UserPo> findUserByMobile(String mobile);
    
    UserPo findUserByUserIdNotAndUsernameIgnoreCase(String userId, String username);

    UserPo findUserByUserIdAndPassword(String userId, String password);
    
    //FIXME yingchang pagination demo
    @Override
    Page<UserPo> findAll(Pageable pageable);
    
    Page<UserPo> findAll(Specification<UserPo> spec, Pageable pageable);
    
    @Modifying
    @Query("UPDATE UserPo Set password = :pw, lastMntTs = :date,failureCount = 0 WHERE username = :username")
    void updatePassword(@Param("username") String username, @Param("pw") String password, @Param("date") Date currentDate);
    
    @Modifying
    @Query("UPDATE UserPo Set username = :username, lastMntTs = :date WHERE userId = :userId")
    void updateUserName(@Param("userId") String userId, @Param("username") String username, @Param("date") Date currentDate);

    
}
