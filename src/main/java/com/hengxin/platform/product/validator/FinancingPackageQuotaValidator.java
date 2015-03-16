/**
 *
 */
package com.hengxin.platform.product.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.product.dto.ProductPackageDto;

/**
 * @author yingchangwang
 * 
 */
@Component
public class FinancingPackageQuotaValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageQuotaCheck, ProductPackageDto> {
    private String packageQuota;
    private String messageTemplate;

    @Override
    public void initialize(FinancingPackageQuotaCheck obj) {
        this.packageQuota = obj.packageQuota();
        this.messageTemplate = obj.message();
    }

    @Override
    public boolean isValid(ProductPackageDto productPackgeDto, ConstraintValidatorContext context) {
        BigDecimal quota = productPackgeDto.getPackageQuota();
        messageTemplate = "";
        if (quota != null) {
            String quotaString = quota.toPlainString();
            if(quotaString.indexOf('.')>0){
                quotaString = quotaString.substring(0, quotaString.indexOf('.'));
            }
            if (!SubscribeUtils.isValidFaceValue(quotaString)) {
                // 数字格式，faceValue的整数倍
                messageTemplate = "{package.error.quota.invalid}";
            } else if (quota.compareTo(ProductUtil.getMinQuotaValue()) < 0) {
                messageTemplate = "{package.error.quota.greater.than}";
            }
        }

        if (StringUtils.isNotBlank(messageTemplate)) {
            bindNode(context, this.packageQuota, this.messageTemplate);
            return false;
        }

        return true;
    }

}
