
/*
* Project Name: kmfex
* File Name: ETradeDirection.java
* Class Name: ETradeDirection
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
	
package com.hengxin.platform.account.enums;

import com.hengxin.platform.common.enums.PageEnum;


/**
 * Class Name: ETradeDirection
 * Description: TODO
 * @author congzhou
 *
 */

public enum ETradeDirection implements PageEnum {
    
    NULL("",""),BUY("01","买入"),SELL("02","卖出");

    private String code;

    private String text;

    ETradeDirection(String code, String text) {
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
