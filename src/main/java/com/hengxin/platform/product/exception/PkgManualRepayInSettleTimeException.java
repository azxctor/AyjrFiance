
/*
* Project Name: kmfex-platform
* File Name: PkgManualRepayNotBeforeSettleTimeException.java
* Class Name: PkgManualRepayNotBeforeSettleTimeException
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
	
package com.hengxin.platform.product.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;


/**
 * Class Name: PkgManualRepayNotBeforeSettleTimeException
 * Description: TODO
 * @author tingwang
 *
 */

public class PkgManualRepayInSettleTimeException extends BizServiceException{
    
    
    /**
    * Variables Name: serialVersionUID
    * Description: TODO
    * Value Description: TODO
    */
    	
    private static final long serialVersionUID = 1L;

    public PkgManualRepayInSettleTimeException(){
        super(EErrorCode.PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD);
    }
    public PkgManualRepayInSettleTimeException(String message){
        super(EErrorCode.PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD, message);
    }
    
    public PkgManualRepayInSettleTimeException(String message, Throwable throwable){
        super(EErrorCode.PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD, message, throwable);
    }

}
