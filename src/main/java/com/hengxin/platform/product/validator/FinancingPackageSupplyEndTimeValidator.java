/**
 *
 */
package com.hengxin.platform.product.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.dto.ProductPackageDto;

/**
 * @author yingchangwang
 *
 */
@Component
public class FinancingPackageSupplyEndTimeValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageSupplyEndTimeCheck, ProductPackageDto> {
    private String supplyEndTime;
    private String messageTemplate;

    @Override
    public void initialize(FinancingPackageSupplyEndTimeCheck obj) {
        this.supplyEndTime = obj.supplyEndTime();
        this.messageTemplate=obj.message();
    }

    @Override
    public boolean isValid(ProductPackageDto productPackgeDto, ConstraintValidatorContext context) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        Date currentDate = new Date();
        String supplyStartTime = productPackgeDto.getSupplyStartTime();
        String supplyEndTime = productPackgeDto.getSupplyEndTime();
        Boolean instantPublish = productPackgeDto.getInstantPublish();
        if(instantPublish){
            supplyStartTime = DateUtils.formatDate(currentDate, dateFormat);
        }
        messageTemplate = "";
        
        if(StringUtils.isBlank(supplyEndTime)){
            messageTemplate="{package.error.supplyEndTime.empty}";
        }

        if (StringUtils.isNotBlank(supplyStartTime) && StringUtils.isNotBlank(supplyEndTime)) {
            Date dateStart = DateUtils.getDate(supplyStartTime, dateFormat);
            Date dateEnd = DateUtils.getDate(supplyEndTime, dateFormat);
            if (dateEnd.compareTo(dateStart) <= 0) {
                messageTemplate="{package.error.supplyendtime.invalid}";
            }
        }
        
        if(StringUtils.isNotBlank(messageTemplate)){
            bindNode(context, this.supplyEndTime, this.messageTemplate);
            return false;
        }

        return true;
    }

}
