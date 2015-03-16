/*
 * Project Name: kmfex-platform
 * File Name: EntityTypeEnumConverter.java
 * Class Name: EntityTypeEnumConverter
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

package com.hengxin.platform.common.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.util.EnumHelper;

/**
 * Class Name: EntityTypeEnumConverter Description: TODO
 * 
 * @author chunlinwang
 * 
 */
@Converter
public class EntityTypeEnumConverter implements AttributeConverter<EntityType, String> {

    @Override
    public String convertToDatabaseColumn(EntityType attribute) {
        return attribute.getCode();
    }

    @Override
    public EntityType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EntityType.class, dbData);
    }

}
