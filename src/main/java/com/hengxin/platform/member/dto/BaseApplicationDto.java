/*
 * Project Name: kmfex-platform
 * File Name: BaseApplication.java
 * Class Name: BaseApplication
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

package com.hengxin.platform.member.dto;

import java.io.Serializable;

import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: BaseApplication
 * 
 * @author runchen
 * 
 */
public class BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appId;

    private String userId;

    private String comments;

    private EApplicationStatus status = EApplicationStatus.NULL;

    private String creator;

    private String latestTs; // CreateTS

    private String lastMntOpid;

    private String lastMntTs;

    private String createTs;

    protected boolean inCanViewPage;

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
     * @return return the value of the var latestTs
     */

    public String getLatestTs() {
        return latestTs;
    }

    /**
     * @param latestTs
     *            Set latestTs value
     */

    public void setLatestTs(String latestTs) {
        this.latestTs = latestTs;
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

    public String getLastMntTs() {
        return lastMntTs;
    }

    /**
     * @param lastMntTs
     *            Set lastMntTs value
     */

    public void setLastMntTs(String lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * @return return the value of the var createTs
     */

    public String getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs
     *            Set createTs value
     */

    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    /**
     * @return return the value of the var inCanViewPage
     */

    public boolean isInCanViewPage() {
        return inCanViewPage;
    }

    /**
     * @param inCanViewPage
     *            Set inCanViewPage value
     */

    public void setInCanViewPage(boolean inCanViewPage) {
        this.inCanViewPage = inCanViewPage;
    }

}
