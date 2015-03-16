
/*
* Project Name: kmfex-platform
* File Name: XWBTransferReq.java
* Class Name: XWBTransferReq
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
	
package com.hengxin.platform.account.dto.biz.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;


/**
 * Class Name: XWBTransferReq
 * Description: TODO
 * @author tingwang
 *
 */

public class XWBTransferReq implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank
    private String userId;
    @NotBlank
    private BigDecimal amount;
//    @NotBlank
//    private String password;
    @NotBlank
    private String currentOpId;
    
    private String memo;
    @NotBlank
    private Date workDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getCurrentOpId() {
        return currentOpId;
    }

    public void setCurrentOpId(String currentOpId) {
        this.currentOpId = currentOpId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
    
    
}
