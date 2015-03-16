
/*
* Project Name: kmfex-platform
* File Name: NumberTool.java
* Class Name: NumberTool
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
	
package com.hengxin.platform.common.util.velocity;

import org.apache.commons.lang.StringUtils;

/**
 * Class Name: StringUtilTool Description: TODO
 * 
 * @author rczhan
 * 
 */
@org.apache.velocity.tools.config.DefaultKey(value = "stringUtil")
public class StringUtilTool extends org.apache.velocity.tools.generic.FormatConfig {

    public String getSuffix(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            String[] split = filePath.split("\\.");
            return split[split.length - 1];
        }
        return "";
    }

    public boolean isPdf(String filePath) {
        return "pdf".equalsIgnoreCase(getSuffix(filePath));
    }

    public boolean isDoc(String filePath) {
        String suffix = getSuffix(filePath);
        return "doc".equalsIgnoreCase(suffix) || "docx".equalsIgnoreCase(suffix);
    }

}
