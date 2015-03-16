/*
 * Project Name: kmfex-platform-trunk
 * File Name: ReportUtil.java
 * Class Name: ReportUtil
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

package com.hengxin.platform.common.util;

import org.codehaus.jackson.map.ObjectMapper;

import com.hengxin.platform.report.ReportParameter;

/**
 * Class Name: ReportUtil Description: TODO
 * 
 * @author zhengqingye
 * 
 */
public class ReportUtil {

    private static final String AES_KEY = "report.crypto.key";

    public static String buildReportCriteriaToken(ReportParameter parameter) {
        ObjectMapper mapper = new ObjectMapper();
        String roKeyByteString = null;
        try {
            roKeyByteString = mapper.writeValueAsString(parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CryptoUtil.encrypt(roKeyByteString, AppConfigUtil.getConfig(AES_KEY));
    }

}
