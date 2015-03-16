/*
 * Project Name: kmfex-platform
 * File Name: AbnormalWithdrawDto.java
 * Class Name: AbnormalWithdrawDto
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

package com.hengxin.platform.member.dto;

import java.io.Serializable;

/**
 * Class Name: AbnormalWithdrawDto Description: 系统管理员异常撤单DTO
 *
 * @author yingchangwang
 *
 */

public class AbnormalWithdrawDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String packageId;
    private String comments;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
