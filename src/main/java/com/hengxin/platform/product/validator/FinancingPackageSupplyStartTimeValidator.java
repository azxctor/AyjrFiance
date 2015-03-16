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
public class FinancingPackageSupplyStartTimeValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageSupplyStartTimeCheck, ProductPackageDto> {
    private String supplyStartTime;
    private String messageTemplate;

    @Override
    public void initialize(FinancingPackageSupplyStartTimeCheck obj) {
        this.supplyStartTime = obj.supplyStartTime();
        this.messageTemplate=obj.message();
    }

    @Override
    public boolean isValid(ProductPackageDto productPackgeDto, ConstraintValidatorContext context) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        String supplyStartTime = productPackgeDto.getSupplyStartTime();
        String prePublicTime = productPackgeDto.getPrePublicTime();
        boolean instantPublish = productPackgeDto.getInstantPublish();
        messageTemplate = "";
        
        if(instantPublish){
            return true;
        }
        
        if(StringUtils.isBlank(supplyStartTime)){
            messageTemplate="{package.error.supplyStartTime.empty}";
        }

        if (StringUtils.isNotBlank(prePublicTime) && StringUtils.isNotBlank(supplyStartTime)) {
            Date dateStart = DateUtils.getDate(supplyStartTime, dateFormat);
            Date dateEnd = DateUtils.getDate(prePublicTime, dateFormat);
            if (dateEnd.compareTo(dateStart) > 0) {
                messageTemplate="{package.error.supplystarttime.greater.than.perpublishtime}";
            }
        }
        
        if(StringUtils.isNotBlank(messageTemplate)){
            bindNode(context, this.supplyStartTime, this.messageTemplate);
            return false;
        }

        return true;
    }

}
