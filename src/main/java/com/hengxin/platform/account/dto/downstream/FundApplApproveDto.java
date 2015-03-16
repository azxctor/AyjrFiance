/*
 * Project Name: kmfex-platform
 * File Name: WithdDepApplApproveDto.java
 * Class Name: WithdDepApplApproveDto
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

package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;

/**
 * Class Name: FundApplApproveDto Description: TODO
 * 
 * @author tingwang
 * 
 */

public class FundApplApproveDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 申请编号
	 */
    private String appId;

    /**
     * 审批是否通过
     */
    private boolean passed;

    /**
     * 不通过理由
     */
    private String comments;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public void setComments(String comments) {
        this.comments = comments;
    }

}
