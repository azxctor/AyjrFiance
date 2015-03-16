/*
 * Project Name: kmfex-platform
 * File Name: FilePo.java
 * Class Name: FilePo
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

package com.hengxin.platform.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class Name: FilePo
 * 
 * @author shengzhou
 * 
 */
@SuppressWarnings("serial")
@Entity(name = "GL_FILE")
public class SysFilePo implements Serializable {

    @Id
    @Column(name = "FILE_ID")
    private String id;
    
    @Column(name = "PATH")
    private String path;
    
    @Column(name = "CREATE_OPID")
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createdTs;
    
    @Column(name = "ORG_FILE_NAME")
    private String orgFileName;
    
    /**
     * @return return the value of the var id
     */
    
    public String getId() {
        return id;
    }

    /**
     * @param id Set id value
     */
    
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return return the value of the var path
     */
    
    public String getPath() {
        return path;
    }

    /**
     * @param path Set path value
     */
    
    public void setPath(String path) {
        this.path = path;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return return the value of the var createdTs
     */
    
    public Date getCreatedTs() {
        return createdTs;
    }

    /**
     * @param createdTs Set createdTs value
     */
    
    public void setCreatedTs(Date createdTs) {
        this.createdTs = createdTs;
    }

    /**
     * @return return the value of the var orgFileName
     */
    
    public String getOrgFileName() {
        return orgFileName;
    }

    /**
     * @param orgFileName Set orgFileName value
     */
    
    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

}
