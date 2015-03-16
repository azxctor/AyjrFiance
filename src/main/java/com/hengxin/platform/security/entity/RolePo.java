
/*
* Project Name: kmfex-platform-trunk
* File Name: RolePo.java
* Class Name: RolePo
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
	
package com.hengxin.platform.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.BaseMaintainablePo;


/**
 * Class Name: RolePo
 * Description: TODO
 * @author zhengqingye
 *
 */
@Entity
@Table(name="UM_ROLE")
public class RolePo extends BaseMaintainablePo{
    @Id
    @Column(name="ROLE_ID")
    private String roleId;
    
    @Column(name="ROLE_NAME")
    private String name;
    
    @Column(name="ROLE_DESC")
    private String description;
    
    @OneToMany
    @JoinTable(name = "UM_ROLE_PERM", joinColumns = @JoinColumn(name = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "PERM_ID"))
    private List<PermissionPo> permissions;
    
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionPo> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionPo> permissions) {
        this.permissions = permissions;
    }

}
