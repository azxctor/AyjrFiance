
/*
 * Project Name: kmfex-platform
 * File Name: XmlConverter.java
 * Class Name: XmlConverter
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

package com.hengxin.platform.bnkdocking.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;

/**
 * Class Name: XmlConverter.
 * Description: Xml公用Service
 * @author congzhou
 *
 */
public class XmlCommonService {

    //key-msg_id,value-errorName
    private static Map<String, String> errorMap = new HashMap<String, String>();

    static {
        EBnkErrorCode[] codes = EBnkErrorCode.values();
        for (EBnkErrorCode code : codes) {
            errorMap.put(code.getMsg_id(), code.name());
        }
    }

	public static Map<String, String> getErrorMap() {
		return errorMap;
	}

}
