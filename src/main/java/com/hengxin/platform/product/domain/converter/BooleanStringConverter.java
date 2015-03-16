/*
 * Project Name: kmfex-platform
 * File Name: BooleanStringConverter.java
 * Class Name: BooleanStringConverter
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

package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;

/**
 * Class Name: BooleanStringConverter Description: TODO
 *
 * @author yingchangwang
 *
 */

public class BooleanStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if ("Y".equalsIgnoreCase(dbData)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
