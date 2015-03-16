/*
 * Project Name: kmfex-platform
 * File Name: Agency.java
 * Class Name: Agency
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

import org.hibernate.validator.constraints.Length;

import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.validator.AuditCheck;

/**
 * Class name: Audit
 *
 * @author yingchangwang
 *
 */
@AuditCheck(passed = "passed", comments = "comments")
public class AuditDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appId;
    private String userId;
    private boolean passed;
    @Length(max=255,message="member.error.audit.reject.reason.length")
    private String comments;
    private EMemberType memberType;
    private String level;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getComments() {
        return comments;
    }

    /**
     *
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    public EMemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(EMemberType memberType) {
        this.memberType = memberType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
