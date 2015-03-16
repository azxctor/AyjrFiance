
/*
* Project Name: micro-finance
* File Name: DataPermissionMissConfigException.java
* Class Name: DataPermissionMissConfigException
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
 * Class Name: DataPermissionMissConfigException
 * Description: TODO
 * @author zhengqingye
 *
 */

public class DataPermissionMissConfigException extends DataPermissionException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new DataPermissionMissConfigException.
     */
    public DataPermissionMissConfigException() {
        super();
    }

    /**
     * Constructs a new DataPermissionMissConfigException.
     *
     * @param message the reason for the exception
     */
    public DataPermissionMissConfigException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataPermissionMissConfigException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public DataPermissionMissConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DataPermissionMissConfigException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public DataPermissionMissConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
