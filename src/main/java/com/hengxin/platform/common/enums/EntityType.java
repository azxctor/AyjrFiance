/*
 * Project Name: kmfex-platform
 * File Name: EntityType.java
 * Class Name: EntityType
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

package com.hengxin.platform.common.enums;

/**
 * Class Name: EntityType Description: TODO
 * 
 * @author chunlinwang
 * 
 */

public enum EntityType implements DBEnum {

    USER("USER"), PRODUCT("PRODUCT"), PRODUCTPACKAGE("PRODUCTPACKAGE"), FUND("FUND");
    private String code;

    EntityType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

}
