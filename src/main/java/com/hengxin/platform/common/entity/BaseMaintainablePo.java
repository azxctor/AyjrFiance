
/*
* Project Name: kmfex-platform-trunk
* File Name: BaseMaintainablePo.java
* Class Name: BaseMaintainablePo
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
 * Class Name: BaseMaintainablePo
 * Description: TODO
 * @author zhengqingye
 *
 */
@MappedSuperclass
public class BaseMaintainablePo extends BasePo {
    @Column(name="LAST_MNT_OPID")
    private String lastMntOpId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;

    public String getLastMntOpId() {
        return lastMntOpId;
    }

    public void setLastMntOpId(String lastMntOpId) {
        this.lastMntOpId = lastMntOpId;
    }

    public Date getLastMntTs() {
        return lastMntTs;
    }

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }
    
    
}
