
/*
* Project Name: kmfex-platform
* File Name: InvalidTargetIdException.java
* Class Name: InvalidTargetIdException
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
 * Class Name: InvalidTargetIdException
 * Description: TODO
 * @author zhengqingye
 *
 */

public class InvalidTargetIdException extends DataPermissionException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new InvalidTargetIdException.
     */
    public InvalidTargetIdException() {
        super();
    }

    /**
     * Constructs a new InvalidTargetIdException.
     *
     * @param message the reason for the exception
     */
    public InvalidTargetIdException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidTargetIdException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidTargetIdException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidTargetIdException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidTargetIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
