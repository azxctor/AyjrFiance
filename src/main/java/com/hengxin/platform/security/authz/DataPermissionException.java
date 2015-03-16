
/*
* Project Name: kmfex-platform
* File Name: DataPermissionException.java
* Class Name: DataPermissionException
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
	
package com.hengxin.platform.security.authz;


/**
 * Class Name: DataPermissionException
 * Description: TODO
 * @author zhengqingye
 *
 */

public class DataPermissionException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new DataPermissionException.
     */
    public DataPermissionException() {
        super();
    }

    /**
     * Constructs a new DataPermissionException.
     *
     * @param message the reason for the exception
     */
    public DataPermissionException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataPermissionException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public DataPermissionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DataPermissionException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public DataPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
