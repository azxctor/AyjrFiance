
/*
* Project Name: kmfex
* File Name: SMSAuthentic.java
* Class Name: SMSAuthentic
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
	
package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;


/**
 * Class Name: SMSAuthentic
 * Description: TODO
 * @author congzhou
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_SMS_AUTHC")
@EntityListeners(IdInjectionEntityListener.class)
public class SMSAuthentic implements Serializable {
    
    @Id
    @Column(name = "ID")
    private String id;
    
    @Column(name = "MOBILE")
    private String mobile;
    
    @Column(name = "CODE")
    private String code;
    
    @Column(name="CREATE_OPID", insertable = true, updatable = false)
    private String creatorOpId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", insertable = true, updatable = true)
    private Date createTs;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
