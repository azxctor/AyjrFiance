
/*
* Project Name: kmfex-platform-trunk
* File Name: BizServiceException.java
* Class Name: BizServiceException
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
	
package com.hengxin.platform.common.exception;



/**
 * Base exception class for services
 * 
 * @author zhengqingye
 *
 */
public class BizServiceException extends KmfexBaseException {
    private static final long serialVersionUID = 1L;
    
    public BizServiceException(){
        super();
    }
    
    public BizServiceException(DisplayableError error){
        super(error);
    }
    
    public BizServiceException(DisplayableError error, String message){
        super(error, message);
    }
    
    public BizServiceException(DisplayableError error, String message, Throwable cause){
        super(error, message, cause);
    }
}
