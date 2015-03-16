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

package com.hengxin.platform.member.enums;

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
public enum EApplicationStatus implements PageEnum {

    NULL("", ""), UNCOMMITTED("U", "已保存"), PENDDING("P", "待审核"), ACCEPT("O", "审核通过"), REJECT("R", "审核拒绝"), ACTIVE("A",
            "激活"), AUDITED("Y", "已审核");

    private String code;

    private String text;

    EApplicationStatus(String code, String text) {
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
