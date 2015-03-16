
/*
* Project Name: kmfex
* File Name: ETradeType.java
* Class Name: ETradeType
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;


/**
 * Class Name: ETradeType
 * Description: TODO
 * @author congzhou
 *
 */

@JsonSerialize(using = PageEnumSerializer.class)
public enum ETradeType implements PageEnum {
    NULL("", ""),WAIT_SIGN("01","待签约"),SIGN_SUCCESS("02","签约成功"),CANCELD("03","已撤单"),TRANSFERD("04","已转让"),BUY_SUCESS("05","买入成功"),END("06","还款结束"),
    TRANSFERING("T","还款中（转让中）"),PAYING("PN","还款中");
    
    
    private String code;

    private String text;

    ETradeType(String code, String text) {
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
