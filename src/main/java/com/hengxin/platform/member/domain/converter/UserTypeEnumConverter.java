
/*
 * Project Name: kmfex-platform
 * File Name: UserTypeEnumConverter.java
 * Class Name: UserTypeEnumConverter
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
	
package com.hengxin.platform.member.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.member.enums.EUserType;

/**
 * Class Name: UserTypeEnumConverter Description: TODO
 * 
 * @author rczhan
 * 
 */
@Converter
public class UserTypeEnumConverter implements AttributeConverter<EUserType, String> {

    @Override
    public String convertToDatabaseColumn(EUserType attribute) {
        return attribute.getCode();
    }

    @Override
    public EUserType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EUserType.class, dbData);
    }

}
