/*
 * Project Name: kmfex-platform
 * File Name: EGender.java
 * Class Name: EGender
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

package com.hengxin.platform.market.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * Class Name: EGender Description: TODO
 * 
 * @author rczhan
 * 
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EHistoryPeriod implements PageEnum {

    NULL("", ""), ONEWEEK("ONEWEEK", "近一周"), TWOWEEK("TWOWEEK", "近两周"), ONEMONTH("ONEMONTH", "近一个月"), ALL("ALL", "全部");

    private String code;

    private String text;

    EHistoryPeriod(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
