/*
 * Project Name: kmfex-platform
 * File Name: File.java
 * Class Name: File
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

package com.hengxin.platform.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Class Name: File
 * 
 * @author shengzhou
 * 
 */
@SuppressWarnings("serial")
public class SysFile implements Serializable {

    private String id;
    
    private String path;
    
    private String creator;

    private Date createdTs;
    
    private String orgFileName;
    
    private String uploadPath;

    public SysFile(){
        
    }
    
    public SysFile(String id, String path) {
        this.id = id;
        this.path = path;
    }
    
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

    /**
     * @return return the value of the var uploadPath
     */

    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * @param uploadPath
     *            Set uploadPath value
     */

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

}
