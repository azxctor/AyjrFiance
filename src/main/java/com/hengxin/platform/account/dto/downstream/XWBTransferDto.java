
/*
* Project Name: kmfex-platform
* File Name: XWBTransferDto.java
* Class Name: XWBTransferDto
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
import java.math.BigDecimal;

import com.sun.istack.NotNull;


/**
 * Class Name: XWBTransferDto
 * Description: TODO
 * @author tingwang
 *
 */

public class XWBTransferDto implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
    private BigDecimal amount;
    
//    private String password;
    
    private String memo;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
