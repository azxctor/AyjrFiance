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

import com.hengxin.platform.common.dto.DataTablesRequestDto;

/**
 * Class name: AuditLog
 * 
 * @author yingchangwang
 * 
 */
public class AuditLogDto extends DataTablesRequestDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;

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

}
