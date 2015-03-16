/*
 * Project Name: kmfex-platform
 * File Name: EMemberType.java
 * Class Name: EMemberType
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
 * Class Name: EMemberType Description: TODO
 * 
 * @author rczhan
 * 
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EMemberType implements PageEnum {

    NULL("", ""), BASIC("B", "基本信息"), INVESTOR("T", "投资"), FINANCER("R", "融资"), INVESTOR_FINANCER("TR", "投融资"),FINANCER_INVESTOR("RT", "投融资"), GUEST("G", "游客"), AUTHZDCENTER(
            "F", "服务中心"), PRODUCTSERVICE("D", "担保机构"), PRODSERV_AUTHZD("DF", "服务中心/担保机构"),AUTHZD_PRODSERV("FD", "服务中心/担保机构") ;

    private String code;

    private String text;

    EMemberType(String code, String text) {
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
