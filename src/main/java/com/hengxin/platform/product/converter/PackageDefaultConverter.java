/*
 * Project Name: kmfex-platform
 * File Name: PackageDefaultConverter.java
 * Class Name: PackageDefaultConverter
 *
 * Copyright 2014 KMFEX Inc
 *
 *
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.product.converter;

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.dto.FinancePackageDefaultDto;

/**
 * Class Name: PackageDefaultConverter Description:
 *
 * @author shengzhou
 * @param <F>
 *
 */
public class PackageDefaultConverter implements ObjectConverter<FinancePackageDefaultDto, PaymentSchedule> {

    @Override
    public void convertFromDomain(PaymentSchedule domain, FinancePackageDefaultDto dto) {
        if (domain != null && domain.getFinancingPackageView() != null) {
            dto.setPackageName(domain.getFinancingPackageView().getPackageName());
            dto.setPackageQuota(domain.getFinancingPackageView().getSupplyAmount());
            dto.setStatus(domain.getFinancingPackageView().getStatus());
            dto.setPayStatus(domain.getStatus());
            dto.setProductId(domain.getFinancingPackageView().getProductId());
            dto.setFinancierName(domain.getFinancingPackageView().getFinancierName());
            dto.setWrtrName(domain.getFinancingPackageView().getWrtrName());
            dto.setWarrantIdSw(domain.getFinancingPackageView().getWatridSw());
            dto.setWrtrNameShow(domain.getFinancingPackageView().getWrtrNameShow());
            dto.setProductId(domain.getFinancingPackageView().getProductId());
        }
    }

    @Override
    public void convertToDomain(FinancePackageDefaultDto dto, PaymentSchedule domain) {

    }
}
