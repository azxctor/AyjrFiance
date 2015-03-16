/*
 * Project Name: standard-code-base
 * File Name: ErrorMessage.java
 * Class Name: ErrorMessage
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.common.controller.exception;

import org.apache.commons.lang.StringUtils;

/**
 * Class Name: ErrorMessage Description: Error Messages used in the whole system.
 * 
 * @author chunlinwang
 * 
 */

public enum ErrorMessage {

    TEST_ERROR("error.test", "This is just a testing error...");

    /**
     * The value of this field should have a corresponding record in the resource file
     */
    private String errorCode;

    /**
     * Normally, the user will not see this message, it will be used by logger, because the displayed error message will
     * be defined in resource files
     */
    private String defaultMessage;

    private ErrorMessage(final String errorCode, final String defaultMessage) {
        if (StringUtils.isEmpty(errorCode) || StringUtils.isEmpty(defaultMessage)) {
            throw new RuntimeException("errorCode and defaultMessage are mandatory in " + this.getClass() + "!");
        }
        this.errorCode = errorCode;
        this.defaultMessage = defaultMessage;

    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
