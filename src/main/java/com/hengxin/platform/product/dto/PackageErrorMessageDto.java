/*
 * Project Name: kmfex-platform
 * File Name: ErrorMessageDto.java
 * Class Name: ErrorMessageDto
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

package com.hengxin.platform.product.dto;

import java.io.Serializable;

/**
 * Class Name: ErrorMessageDto Description: TODO
 *
 * @author yingchangwang
 *
 */

public class PackageErrorMessageDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
