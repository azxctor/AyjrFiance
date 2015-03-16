
/*
* Project Name: kmfex-platform-trunk
* File Name: PermissionPo.java
* Class Name: PermissionPo
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.BaseMaintainablePo;


/**
 * Class Name: PermissionPo
 * Description: TODO
 * @author zhengqingye
 *
 */
@Entity
@Table(name="UM_PERM")
public class PermissionPo extends BaseMaintainablePo{
    @Id
    @Column(name="PERM_ID")
    private String permId;
    
    @Column(name="PERM_NAME")
    private String name;
    
    @Column(name="PERM_DESC")
    private String description;

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
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
    
}
