/*
 * Project Name: kmfex-platform
 * File Name: WithdConfirmDto.java
 * Class Name: WithdConfirmDto
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

import javax.validation.constraints.NotNull;

/**
 * Class Name: WithdConfirmDto Description: TODO
 * 
 * @author tingwang
 * 
 */

public class WithdConfirmDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
    private String applId;
    
    private String memo;

    public String getApplId() {
        return applId;
    }

    public void setApplId(String applId) {
        this.applId = applId;
    }
	
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
