/*
 * Project Name: kmfex-platform
 * File Name: FeePayMethodTypeMenuConverter.java
 * Class Name: FeePayMethodTypeMenuConverter
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

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EFeePayMethodType;

/**
 * Class Name: FeePayMethodTypeMenuConverter Description: TODO
 *
 * @author yingchangwang
 *
 */

public class FeePayMethodTypeMenuConverter implements AttributeConverter<EFeePayMethodType, String> {

    @Override
    public String convertToDatabaseColumn(EFeePayMethodType attribute) {
        return attribute.getCode();
    }

    @Override
    public EFeePayMethodType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EFeePayMethodType.class, dbData);
    }

}
