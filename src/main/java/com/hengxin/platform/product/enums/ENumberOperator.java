
/*
* Project Name: kmfex-platform
* File Name: ENumberOperator.java
* Class Name: ENumberOperator
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
	
package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;


/**
 * Class Name: ENumberOperator
 * @author yingchangwang
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum ENumberOperator implements PageEnum {
    NULL("", ""), 
    EQ(" = ", "等于"), 
    GREAT_THAN(" > ", "大于"),
    GREAT_EQ(" >= ", "大于等于"),
    LESS_THAN(" < ", "小于"),
    LESS_EQ(" <= ", "小于等于");

    private String code;
    private String text;
    
    private ENumberOperator(String code,String text) {
        this.code= code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
