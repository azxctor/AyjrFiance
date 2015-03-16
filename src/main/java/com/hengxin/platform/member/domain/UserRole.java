/*
 * Project Name: kmfex-platform
 * File Name: AgencyPo.java
 * Class Name: AgencyPo
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

package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hengxin.platform.security.entity.RolePo;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: UserRole
 * 
 * @author yingchangwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_USER_ROLE")
@IdClass(UserRolePk.class)
public class UserRole implements Serializable {
    @Id
    @Column(name = "USER_ID")
    private String userId;
    @Id
    @Column(name = "ROLE_ID")
    private String roleId;
    @Column(name = "CREATE_OPID")
    private String createOpId;
    @Column(name = "CREATE_TS")
    private Date createTs;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo userPo;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    private RolePo rolePo;

    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    /**
     * 
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreateOpId() {
        return createOpId;
    }

    /**
     * 
     * @param createOpId
     */
    public void setCreateOpId(String createOpId) {
        this.createOpId = createOpId;
    }

    public Date getCreateTs() {
        return createTs;
    }

    /**
     *
     */
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }
  
    public UserPo getUserPo() {
        return userPo;
    }

    public void setUserPo(UserPo userPo) {
        this.userPo = userPo;
    }

    public RolePo getRolePo() {
        return rolePo;
    }

    public void setRolePo(RolePo rolePo) {
        this.rolePo = rolePo;
    }

    

}
