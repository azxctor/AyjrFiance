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
public class PackageDefaultViewConverter implements ObjectConverter<FinancePackageDefaultDto, PaymentSchedule> {

    @Override
    public void convertFromDomain(PaymentSchedule domain, FinancePackageDefaultDto dto) {
        if (domain != null && domain.getPackagePaymentView() != null) {
            dto.setPackageName(domain.getPackagePaymentView().getPackageName());
            dto.setPackageQuota(domain.getPackagePaymentView().getSupplyAmount());
            dto.setStatus(domain.getPackagePaymentView().getStatus());
            dto.setPayStatus(domain.getStatus());
            dto.setProductId(domain.getPackagePaymentView().getProductId());
            dto.setFinancierName(domain.getPackagePaymentView().getFinancierName());
            dto.setWrtrName(domain.getPackagePaymentView().getWrtrName());
            dto.setProductId(domain.getPackagePaymentView().getProductId());
        }
    }

    @Override
    public void convertToDomain(FinancePackageDefaultDto dto, PaymentSchedule domain) {

    }
}
