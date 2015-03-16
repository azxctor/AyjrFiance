
/*
* Project Name: kmfex-platform
* File Name: WdrwDealStatusEnumConverter.java
* Class Name: WdrwDealStatusEnumConverter
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
	
package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundDealStatus;


/**
 * Class Name: FundDealStatusEnumConverter
 * @author tingwang
 *
 */

public class FundDealStatusEnumConverter implements AttributeConverter<EFundDealStatus, String> {

    	
    @Override
    public String convertToDatabaseColumn(EFundDealStatus attribute) {
        return attribute.getCode();
    }

    
    @Override
    public EFundDealStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EFundDealStatus.class, dbData);
    }

}
