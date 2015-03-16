
/*
* Project Name: kmfex-platform
* File Name: BaseDo.java
* Class Name: BaseDo
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
	
package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Class Name: BaseInfo Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

@SuppressWarnings("serial")
@MappedSuperclass
public class BaseInfo implements Serializable {
    @Column(name = "CREATE_OPID")
    private String createOpid;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;
    
    @Version
    @Column(name = "VERSION_CT")
    private Long versionCt;

    /**
     * @return return the value of the var createOpid
     */

    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid
     *            Set createOpid value
     */

    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    /**
     * @return return the value of the var lastMntOpid
     */

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    /**
     * @param lastMntOpid
     *            Set lastMntOpid value
     */

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    /**
     * @return return the value of the var createTs
     */

    public Date getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs
     *            Set createTs value
     */

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    /**
     * @return return the value of the var lastMntTs
     */

    public Date getLastMntTs() {
        return lastMntTs;
    }

    /**
     * @param lastMntTs
     *            Set lastMntTs value
     */

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }
    

    /**
     * @return return the value of the var versionCt
     */

    public Long getVersionCt() {
        return versionCt;
    }

    /**
     * @param versionCt
     *            Set versionCt value
     */

    public void setVersionCt(Long versionCt) {
        this.versionCt = versionCt;
    }
}
