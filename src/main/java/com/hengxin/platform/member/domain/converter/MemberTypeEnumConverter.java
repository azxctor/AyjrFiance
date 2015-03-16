
/*
 * Project Name: kmfex-platform
 * File Name: MemberTypeEnumConverter.java
 * Class Name: MemberTypeEnumConverter
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
import com.hengxin.platform.member.enums.EMemberType;

/**
 * Class Name: MemberTypeEnumConverter Description: TODO
 * 
 * @author shengzhou
 * 
 */
@Converter
public class MemberTypeEnumConverter implements AttributeConverter<EMemberType, String> {

    @Override
    public String convertToDatabaseColumn(EMemberType attribute) {
        return attribute.getCode();
    }

    @Override
    public EMemberType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EMemberType.class, dbData);
    }

}
