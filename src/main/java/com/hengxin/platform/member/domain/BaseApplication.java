/*
 * Project Name: kmfex-platform
 * File Name: BaseApplicationPo.java
 * Class Name: BaseApplicationPo
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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.member.domain.converter.ApplicationStatusEnumConverter;
import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: BaseApplicationPo
 *
 * @author runchen
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
@EntityListeners(IdInjectionEntityListener.class)
public class BaseApplication implements Serializable, Cloneable {

    @Id
    @Column(name = "APPL_ID")
    @BusinessName(ignore = true)
    private String appId;

    @Column(name = "USER_ID")
    @BusinessName(ignore = true)
    private String userId;

    @Column(name = "COMMENTS")
    @BusinessName(ignore = true)
    private String comments;

    @Column(name = "STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    @BusinessName(ignore = true)
    private EApplicationStatus status;

    @Column(name = "CREATE_OPID", insertable = true, updatable = false)
    @BusinessName(ignore = true)
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", insertable = true, updatable = false)
    @BusinessName(ignore = true)
    private Date createTs;

    @Column(name = "LAST_MNT_OPID")
    @BusinessName(ignore = true)
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    @BusinessName(ignore = true)
    private Date lastMntTs;

    /**
     * @return return the value of the var appId
     */

    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     *            Set appId value
     */

    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return return the value of the var userId
     */

    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            Set userId value
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return return the value of the var comments
     */

    public String getComments() {
        return comments;
    }

    /**
     * @param comments
     *            Set comments value
     */

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return return the value of the var status
     */

    public EApplicationStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            Set status value
     */

    public void setStatus(EApplicationStatus status) {
        this.status = status;
    }

    /**
     * @return return the value of the var creator
     */

    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     *            Set creator value
     */

    public void setCreator(String creator) {
        this.creator = creator;
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

}
