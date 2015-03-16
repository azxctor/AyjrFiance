/*
 * Project Name: kmfex-platform
 * File Name: ActionTypeEnumConverter.java
 * Class Name: ActionTypeEnumConverter
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

import com.hengxin.platform.common.enums.ENoticeType;
import com.hengxin.platform.common.util.EnumHelper;
 
@Converter
public class NoticeTypeEnumConverter implements AttributeConverter<ENoticeType, String> {

    @Override
    public String convertToDatabaseColumn(ENoticeType type) {
        return type.getCode();
    }

    @Override
    public ENoticeType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(ENoticeType.class, dbData);
    }

}
