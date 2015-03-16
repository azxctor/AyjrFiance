/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackageConverter.java
 * Class Name: FinancingPackageConverter
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


import org.codehaus.plexus.util.StringUtils;

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;

/**
 * Class Name: FinancingPackageConverte
 *
 * @author yingchangwang
 *
 */

public class FinancingPackageConverter implements ObjectConverter<ProductPackageDto, ProductPackage> {

    private static final String DATA_FORMMAT = "yyyy-MM-dd HH:mm";

    @Override
    public void convertFromDomain(ProductPackage domain, ProductPackageDto dto) {
        if (domain != null) {
            dto.setId(domain.getId());
            dto.setPackageName(domain.getPackageName());
            dto.setPackageQuota(domain.getPackageQuota());
            if (domain.getPrePublicTime() != null) {
                dto.setPrePublicTime(DateUtils.formatDate(domain.getPrePublicTime(), DATA_FORMMAT));
            }
            if (domain.getSupplyStartTime() != null) {
                dto.setSupplyStartTime(DateUtils.formatDate(domain.getSupplyStartTime(), DATA_FORMMAT));
            }
            if (domain.getSupplyEndTime() != null) {
                dto.setSupplyEndTime(DateUtils.formatDate(domain.getSupplyEndTime(), DATA_FORMMAT));
            }
            if (EAutoSubscribeStatus.ALLOW == domain.getAutoSubscribeFlag()) {
            	dto.setAutoSubscribeFlag(true);	
			} else if (EAutoSubscribeStatus.DOESNOT == domain.getAutoSubscribeFlag()) {
				dto.setAutoSubscribeFlag(false);
			} else {
				dto.setAutoSubscribeFlag(false);
			}
            dto.setAipFlag(domain.getAipFlag());
            if (domain.getAipStartTime() != null) {
                dto.setAipStartTime(DateUtils.formatDate(domain.getAipStartTime(), DATA_FORMMAT));
            }
            if (domain.getAipEndTime() != null) {
                dto.setAipEndTime(DateUtils.formatDate(domain.getAipEndTime(), DATA_FORMMAT));
            }
            dto.setVersionCount(domain.getVersionCount());
        }

    }

    @Override
    public void convertToDomain(ProductPackageDto dto, ProductPackage domain) {
        if (dto != null) {
            domain.setId(dto.getId());
            domain.setPackageName(dto.getPackageName());
            domain.setPackageQuota(dto.getPackageQuota());
            if (StringUtils.isNotBlank(dto.getPrePublicTime())) {
                domain.setPrePublicTime(DateUtils.getDate(dto.getPrePublicTime(), DATA_FORMMAT));
            }
            if (StringUtils.isNotBlank(dto.getSupplyStartTime())) {
                domain.setSupplyStartTime(DateUtils.getDate(dto.getSupplyStartTime(), DATA_FORMMAT));
            }
            if (StringUtils.isNotBlank(dto.getSupplyEndTime())) {
                domain.setSupplyEndTime(DateUtils.getDate(dto.getSupplyEndTime(), DATA_FORMMAT));
            }
            if(dto.getAutoSubscribeFlag()==null){
            	dto.setAutoSubscribeFlag(Boolean.FALSE);
            }
            if (dto.getAutoSubscribeFlag()) {
            	domain.setAutoSubscribeFlag(EAutoSubscribeStatus.ALLOW );	
			} else if (!dto.getAutoSubscribeFlag()) {
				domain.setAutoSubscribeFlag(EAutoSubscribeStatus.DOESNOT);
			}
            
            domain.setAipFlag(dto.getAipFlag());
            if (StringUtils.isNotBlank(dto.getAipStartTime())) {
                domain.setAipStartTime(DateUtils.getDate(dto.getAipStartTime(), DATA_FORMMAT));
            }
            if (StringUtils.isNotBlank(dto.getAipEndTime())) {
                domain.setAipEndTime(DateUtils.getDate(dto.getAipEndTime(), DATA_FORMMAT));
            }
            domain.setVersionCount(dto.getVersionCount());
        }

    }

}
