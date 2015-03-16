/**
 *
 */
package com.hengxin.platform.product.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.service.FinancingPackageService;

/**
 * @author yingchangwang
 * 
 */
@Component
public class FinancingPackagePrepublishTimeValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackagePrepublishTimeCheck, ProductPackageDto> {

    @Autowired
    private FinancingPackageService financingPackageService;
    private String prePublicTime;
    private String messageTemplate;

    @Override
    public void initialize(FinancingPackagePrepublishTimeCheck obj) {
        this.prePublicTime = obj.prePublicTime();
        this.messageTemplate = obj.message();
    }

    @Override
    public boolean isValid(ProductPackageDto productPackgeDto, ConstraintValidatorContext context) {
        String prePublicTime = productPackgeDto.getPrePublicTime();
        String packageId = productPackgeDto.getId();
        Boolean instantPublish = productPackgeDto.getInstantPublish();
        if(instantPublish){
            return true;
        }
        boolean check = true;
        if (StringUtils.isNotBlank(packageId)) {
            ProductPackage productPackage = financingPackageService.getProductPackageById(packageId);
            String dateFormat = "yyyy-MM-dd HH:mm";
            Date prepublishDate = DateUtils.getDate(prePublicTime, dateFormat);
            EPackageStatus status = productPackage.getStatus();
            if (status != EPackageStatus.PRE_PUBLISH && status != EPackageStatus.WAIT_SUBSCRIBE
                    || productPackage.getPrePublicTime().compareTo(prepublishDate) == 0) {
                check = false;
            }
        }
        if (check) {
            messageTemplate = "";
            String dateFormat = "yyyy-MM-dd HH:mm";
            Date dateEnd = DateUtils.getDate(prePublicTime, dateFormat);
            Date currentDate = new Date();
            if (currentDate.compareTo(dateEnd) > 0) {
                messageTemplate = "{package.error.prepublishtime.invalid}";
            }
            if (StringUtils.isNotBlank(messageTemplate)) {
                bindNode(context, this.prePublicTime, this.messageTemplate);
                return false;
            }
        }
        return true;
    }

}
