
/*
* Project Name: kmfex-platform
* File Name: ProductFeeConverter.java
* Class Name: ProductFeeConverter
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

package com.hengxin.platform.product.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.dto.ProductFeeDto;


/**
 * Class Name: ProductFeeConverter
 * Description: TODO
 * @author yingchangwang
 *
 */

public class ProductFeeConverter implements ObjectConverter<ProductFeeDto, ProductFee>  {

    @Override
    public void convertFromDomain(ProductFee domain, ProductFeeDto dto) {
        if(domain!=null){
            dto.setFeeId(domain.getFeeId());
            dto.setFeeRt(domain.getFeeRt().multiply(new BigDecimal(100)).setScale(6,RoundingMode.DOWN));
            dto.setPayMethod(domain.getPayMethod());
        }

    }

    @Override
    public void convertToDomain(ProductFeeDto dto, ProductFee domain) {
        if(dto!=null){
            domain.setFeeId(dto.getFeeId());
            domain.setFeeRt(dto.getFeeRt().divide(new BigDecimal(100)));
            domain.setPayMethod(dto.getPayMethod());
        }

    }

}
