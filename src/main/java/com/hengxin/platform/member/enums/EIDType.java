
/*
 * Project Name: kmfex
 * File Name: EIDType.java
 * Class Name: EIDType
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

import com.hengxin.platform.common.enums.PageEnum;


/**
 * Class Name: EIDType
 * Description: TODO
 * @author congzhou
 *
 */

public enum EIDType implements PageEnum{
    NULL("", ""), PERSORN("P01", "身份证号"), ORG("C09", "组织机构代码");
    
    private String code;

    private String text;

    EIDType(String code, String text) {
        this.code = code;
        this.text = text;
    }



    /* (non-Javadoc)
     * @see com.hengxin.platform.common.enums.DBEnum#getCode()
     */

    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return this.code;
    }


    /* (non-Javadoc)
     * @see com.hengxin.platform.common.enums.DBEnum#setCode(java.lang.String)
     */

    @Override
    public void setCode(String code) {
        this.code = code;

    }


    /* (non-Javadoc)
     * @see com.hengxin.platform.common.enums.PageEnum#getText()
     */

    @Override
    public String getText() {
        return this.text;
    }


    /* (non-Javadoc)
     * @see com.hengxin.platform.common.enums.PageEnum#setText(java.lang.String)
     */

    @Override
    public void setText(String text) {
        this.text = text;

    }

}
