package com.hengxin.platform.product.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.dto.ProductPackageDto;

@Component
public class FinancingPackageAIPStartTimeValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageAIPStartTimeCheck, ProductPackageDto> {

    private String aipStartTime;

    @Override
    public void initialize(FinancingPackageAIPStartTimeCheck obj) {
        this.aipStartTime = obj.aipStartTime();
    }

    @Override
    public boolean isValid(ProductPackageDto productPackageDto, ConstraintValidatorContext context) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        Date currentDate = new Date();
        Boolean aipFlag = productPackageDto.getAipFlag();
        Boolean instantPublish = productPackageDto.getInstantPublish();
        Date aipStartTime = DateUtils.getDate(productPackageDto.getAipStartTime(), dateFormat);
        Date supplyStartTime = DateUtils.getDate(productPackageDto.getSupplyStartTime(), dateFormat);
        if(instantPublish){
            supplyStartTime =  DateUtils.getDate( DateUtils.formatDate(currentDate, dateFormat) , dateFormat);
            aipStartTime = DateUtils.getDate( DateUtils.formatDate(currentDate, dateFormat) , dateFormat);
        }
        if (aipFlag) {
            if (aipStartTime.compareTo(supplyStartTime) < 0) {
                bindNode(context, this.aipStartTime, null);
                return false;
            }
        }

        return true;
    }
}
