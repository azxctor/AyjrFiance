/*
 * Project Name: kmfex-platform
 * File Name: ActionHistoryPo.java
 * Class Name: ActionHistoryPo
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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.converter.ActionTypeEnumConverter;
import com.hengxin.platform.common.entity.converter.EntityTypeEnumConverter;
import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: ActionHistoryPo
 * 
 * @author chunlinwang
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "GL_ACT_HIST")
@EntityListeners(IdInjectionEntityListener.class)
public class ActionHistoryPo implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EVENT_ID")
    private String eventId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date operateTime;

    @Column(name = "CREATE_OPID")
    private String operateUser;

    @Column(name = "ENTITY_ID")
    private String entityId;

    @Column(name = "ENTITY_TYPE")
    @Convert(converter = EntityTypeEnumConverter.class)
    private EntityType entityType;

    @Column(name = "ACTION")
    @Convert(converter = ActionTypeEnumConverter.class)
    private ActionType action;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "ENTITY_FIELD")
    private String entityField;

    @Column(name = "OLD_VALUE")
    private String oldValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Column(name = "COMMENTS")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "CREATE_OPID", insertable = false, updatable = false)
    private UserPo user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEntityField() {
        return entityField;
    }

    public void setEntityField(String entityField) {
        this.entityField = entityField;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserPo getUser() {
        return user;
    }

    public void setUser(UserPo user) {
        this.user = user;
    }

}
