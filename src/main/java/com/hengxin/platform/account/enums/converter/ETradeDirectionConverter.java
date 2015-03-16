
/*
* Project Name: kmfex
* File Name: ETradeDirectionConverter.java
* Class Name: ETradeDirectionConverter
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
	
package com.hengxin.platform.account.enums.converter;

import javax.persistence.AttributeConverter;

import com.hengxin.platform.account.enums.ETradeDirection;
import com.hengxin.platform.common.util.EnumHelper;


/**
 * Class Name: ETradeDirectionConverter
 * Description: TODO
 * @author congzhou
 *
 */

public class ETradeDirectionConverter implements AttributeConverter<ETradeDirection, String> {

    @Override
    public String convertToDatabaseColumn(ETradeDirection attribute) {
        return attribute.getCode();
    }

    @Override
    public ETradeDirection convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(ETradeDirection.class, dbData);
    }
}
