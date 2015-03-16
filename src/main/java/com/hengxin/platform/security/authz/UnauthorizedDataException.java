
/*
* Project Name: kmfex-platform
* File Name: UnauthorizedDataException.java
* Class Name: UnauthorizedDataException
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
 * Class Name: UnauthorizedDataException
 * Description: TODO
 * @author zhengqingye
 *
 */

public class UnauthorizedDataException extends DataPermissionException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new UnauthorizedDataException.
     */
    public UnauthorizedDataException() {
        super();
    }

    /**
     * Constructs a new UnauthorizedDataException.
     *
     * @param message the reason for the exception
     */
    public UnauthorizedDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthorizedDataException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnauthorizedDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnauthorizedDataException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnauthorizedDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
