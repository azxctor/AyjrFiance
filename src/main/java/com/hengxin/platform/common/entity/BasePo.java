
/*
* Project Name: kmfex-platform-trunk
* File Name: BasePo.java
* Class Name: BasePo
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
	
package com.hengxin.platform.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Class Name: BasePo
 * Description: TODO
 * @author zhengqingye
 *
 */
@MappedSuperclass
public class BasePo {
	
    @Column(name="CREATE_OPID", insertable = true, updatable = false)
    private String creatorOpId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", insertable = true, updatable = false)
    private Date createTs;

    public String getCreatorOpId() {
        return creatorOpId;
    }

    public void setCreatorOpId(String creatorOpId) {
        this.creatorOpId = creatorOpId;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }
    
    
}
