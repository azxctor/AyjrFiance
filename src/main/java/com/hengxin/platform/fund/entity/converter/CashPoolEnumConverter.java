/*
 * Project Name: kmfex-platform
 * File Name: CashPoolEnumConverter.java
 * Class Name: CashPoolEnumConverter
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
import com.hengxin.platform.fund.enums.ECashPool;

/**
 * Class Name: CashPoolEnumConverter
 * 
 * @author tingwang
 * 
 */

public class CashPoolEnumConverter implements AttributeConverter<ECashPool, String> {

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
     */

    @Override
    public String convertToDatabaseColumn(ECashPool attribute) {
        return attribute.getCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
     */

    @Override
    public ECashPool convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(ECashPool.class, dbData);
    }

}
