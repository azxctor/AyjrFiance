
/*
* Project Name: kmfex-platform
* File Name: SubAcctAmtRsp.java
* Class Name: SubAcctAmtRsp
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
	
package com.hengxin.platform.fund.dto.biz.rsp;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;


/**
 * Class Name: SubAcctAmtRsp
 * Description: TODO
 * @author tingwang
 *
 */

public class SubAcctAmtRsp implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal availableAmt;
    
    private BigDecimal cashableAmt;
    
    private EBnkErrorCode errorCode;

    public BigDecimal getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(BigDecimal availableAmt) {
        this.availableAmt = availableAmt;
    }

    public BigDecimal getCashableAmt() {
        return cashableAmt;
    }

    public void setCashableAmt(BigDecimal cashableAmt) {
        this.cashableAmt = cashableAmt;
    }
	
    public EBnkErrorCode getErrorCode() {
        return errorCode;
    }
	
    public void setErrorCode(EBnkErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    
}
