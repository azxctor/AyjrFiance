
/*
* Project Name: kmfex-platform
* File Name: PackageAIPGroup.java
* Class Name: PackageAIPGroup
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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.member.domain.SubscribeGroup;


/**
 * Class Name: PackageAIPGroup Description: 融资包定投组信息
 * 
 * @author huanbinzhang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PKG_AIP_GRP")
@IdClass(PackageAIPGroupPk.class)
public class PackageAIPGroup implements Serializable {
    @Id
    @Column(name = "PKG_ID")
    private ProductPackage packageId;

    @Id
    @ManyToOne
    @JoinColumn(name = "GRP_ID")
    private SubscribeGroup group;

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


    public ProductPackage getPackageId() {
        return packageId;
    }

    public void setPackageId(ProductPackage packageId) {
        this.packageId = packageId;
    }

    /**
     * @return return the value of the var group
     */

    public SubscribeGroup getGroup() {
        return group;
    }

    /**
     * @param group
     *            Set group value
     */

    public void setGroup(SubscribeGroup group) {
        this.group = group;
    }

    /**
     * @return return the value of the var createOpid
     */

    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid Set createOpid value
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
     * @param lastMntOpid Set lastMntOpid value
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
     * @param createTs Set createTs value
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
     * @param lastMntTs Set lastMntTs value
     */

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

}
