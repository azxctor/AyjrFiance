
/*
* Project Name: kmfex-platform-trunk
* File Name: NoEnoughBalanceServiceException.java
* Class Name: NoEnoughBalanceServiceException
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
	
package com.hengxin.platform.fund.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;

/**
 * Class Name: AcctNotEnoughAvlBalException
 * Description: TODO
 * @author zhengqingye
 *
 */
public class AvlBalNotEnoughException extends BizServiceException {
	
    private static final long serialVersionUID = 1L;
    
    private static EErrorCode errorCode = EErrorCode.ACCT_NOT_ENOUGH_AVL_BAL;
    

    public AvlBalNotEnoughException(){
        super(errorCode);
    	errorCode.setArgs(new Object[]{""});
    }
    
    public AvlBalNotEnoughException(String acctNo){
        super(errorCode);
    	errorCode.setArgs(new Object[]{acctNo});
    }
    
    public AvlBalNotEnoughException(String acctNo, String message){
        super(errorCode, message);
    	errorCode.setArgs(new Object[]{acctNo});
    }
    
    public AvlBalNotEnoughException(String acctNo, String message, Throwable throwable){
        super(errorCode, message, throwable);
    	errorCode.setArgs(new Object[]{acctNo});
    }
}
