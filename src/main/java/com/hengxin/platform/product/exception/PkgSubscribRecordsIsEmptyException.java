
/*
* Project Name: kmfex-platform
* File Name: PkgSubscribRecordsIsEmptyException.java
* Class Name: PkgSubscribRecordsIsEmptyException
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
 * Class Name: PkgSubscribRecordsIsEmptyException
 * Description: TODO
 * @author tingwang
 *
 */

public class PkgSubscribRecordsIsEmptyException extends BizServiceException{

    
    /**
    * Variables Name: serialVersionUID
    * Description: TODO
    * Value Description: TODO
    */
    	
    private static final long serialVersionUID = 1L;
    
    public PkgSubscribRecordsIsEmptyException(){
        super(EErrorCode.PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD);
    }
    public PkgSubscribRecordsIsEmptyException(String message){
        super(EErrorCode.PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD, message);
    }
    
    public PkgSubscribRecordsIsEmptyException(String message, Throwable throwable){
        super(EErrorCode.PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD, message, throwable);
    }

}
