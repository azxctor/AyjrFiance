
/*
* Project Name: kmfex-platform
* File Name: TransferMsg.java
* Class Name: TransferMsg
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
	
package com.hengxin.platform.account.dto;

import java.io.Serializable;


/**
 * Class Name: BatchTransferMsg
 * Description: TODO
 * @author tingwang
 *
 */

public class BatchTransferExcelRowMsgDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String payerAcctNo;
    
    private String payerName;
    
    private String receiverAcctNo;
    
    private String receiverName;
    
    private String trxAmt;
    
    private String memo;

    public String getPayerAcctNo() {
        return payerAcctNo;
    }

    public void setPayerAcctNo(String payerAcctNo) {
        this.payerAcctNo = payerAcctNo;
    }

    public String getReceiverAcctNo() {
        return receiverAcctNo;
    }

    public void setReceiverAcctNo(String receiverAcctNo) {
        this.receiverAcctNo = receiverAcctNo;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(String trxAmt) {
        this.trxAmt = trxAmt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPayerName() {
        return payerName;
    }
	
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
    
}
