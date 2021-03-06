
/*
* Project Name: kmfex-platform
* File Name: GenderEnumConverter.java
* Class Name: GenderEnumConverter
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
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EPackageStatus;

/**
 * Class Name: GenderEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author rczhan
 * 
 */
@Converter
public class PackageStatusConverter implements AttributeConverter<EPackageStatus, String> {

    @Override
    public String convertToDatabaseColumn(EPackageStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EPackageStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EPackageStatus.class, dbData);
    }

}
