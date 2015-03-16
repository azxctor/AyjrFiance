/*
 * Project Name: kmfex-platform
 * File Name: InvestorPackageConverter.java
 * Class Name: InvestorPackageConverter
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

import java.util.Date;

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.dto.FinancePackageInvestorDto;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * Class Name: InvestorPackageConverter.
 * 
 * @author shengzhou
 * 
 */
public class InvestorPackageConverter implements ObjectConverter<FinancePackageInvestorDto, PositionLotPo> {

    @Override
    public void convertFromDomain(PositionLotPo domain, FinancePackageInvestorDto dto) {
        if (domain != null) {
            dto.setLotId(domain.getLotId());
            dto.setAmount(domain.getLotBuyPrice());
            if (domain.getPosition() != null) {
            	dto.setPayMethod(domain.getPosition().getProductPackage().getProduct().getPayMethod());
            	dto.setWarrantyType(domain.getPosition().getProductPackage().getProduct().getWarrantyType());
                dto.setUserId(domain.getPosition().getUserId());
                dto.setPkgId(domain.getPosition().getPkgId());
                if (domain.getSubsDt() != null) {
                	Date subDate = domain.getSubsDt();
                    dto.setSubsDate(DateUtils.formatDate(subDate, "yyyy-MM-dd"));
                }
                FinancingPackageView packageView = domain.getPosition().getFinancingPackageView();
                if (packageView != null) {
                    dto.setPkgId(packageView.getId());
                    dto.setProductId(packageView.getProductId());
                    if (packageView.getSignContractTime() != null && packageView.getSigningDt() != null) {
                        dto.setSignContractTime(processSignContractTime(packageView.getSigningDt(),
                                packageView.getSignContractTime()));
                    }
                    dto.setStatus(packageView.getStatus());
                    dto.setPackageName(packageView.getPackageName());
                    dto.setRate(FormatRate.formatRate(packageView.getRate().multiply(NumberUtil.getHundred())));
                    dto.setTerm(packageView.getDuration());
                    dto.setFinancierName(packageView.getFinancierName());
                    if (packageView.getWarrantyType() == EWarrantyType.MONITORASSETS
                            || packageView.getWarrantyType() == EWarrantyType.NOTHING) {
                        dto.setWrtrName("--");
                        dto.setWrtrNameShow("--");
                    } else {
                        dto.setWrtrName(packageView.getWrtrName());
                        dto.setWrtrNameShow(packageView.getWrtrNameShow());
                    }
                    // 判断是否为定投
                    if (packageView.getAipEndTime() != null && new Date().before(packageView.getAipEndTime())
                            && "Y".equals(packageView.getAipFlag())) {
                        dto.setAipFlag("Y");
                    } else {
                        dto.setAipFlag("N");
                    }
                }
            }
        }
    }

    private String processSignContractTime(Date signingDate, Date signContractTime) {
        if (signingDate != null && signContractTime != null) {
            String formatDate = DateUtils.formatDate(signingDate, "yyyy-MM-dd");
            String formatTime = DateUtils.formatDate(signContractTime, "HH:mm");

            return formatDate + " " + formatTime;
        }
        return null;
    }

    @Override
    public void convertToDomain(FinancePackageInvestorDto dto, PositionLotPo domain) {

    }
}
