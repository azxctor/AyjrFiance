package com.hengxin.platform.product.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.dto.ProductPackageDto;

@Component
public class FinancingPackageAIPEndTimeValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageAIPEndTimeCheck, ProductPackageDto> {

    private String aipEndTime;
    private String messageTemplate;

    @Override
    public void initialize(FinancingPackageAIPEndTimeCheck obj) {
        this.aipEndTime = obj.aipEndTime();
        this.messageTemplate = obj.message();
    }

    @Override
    public boolean isValid(ProductPackageDto productPackageDto, ConstraintValidatorContext context) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        messageTemplate = "";
        Date currentDate = new Date();
        Boolean aipFlag = productPackageDto.getAipFlag();
        Date aipStartTime = DateUtils.getDate(productPackageDto.getAipStartTime(), dateFormat);
        Date aipEndTime = DateUtils.getDate(productPackageDto.getAipEndTime(), dateFormat);
        Date SupplyEndTime = DateUtils.getDate(productPackageDto.getSupplyEndTime(), dateFormat);
        Boolean instantPublish = productPackageDto.getInstantPublish();
        if(instantPublish){
            aipStartTime = DateUtils.getDate(DateUtils.formatDate(currentDate, dateFormat), dateFormat);
        }
        
        if (aipFlag) {
            if (aipEndTime.compareTo(aipStartTime) < 0) {
                messageTemplate = "{package.error.aipendtime.invalid}";
            }
            if (aipEndTime.compareTo(SupplyEndTime) > 0) {
                messageTemplate = "{package.error.aipendtime.less.than.supplyendtime}";
            }
        }
        if (StringUtils.isNotBlank(messageTemplate)) {
            bindNode(context, this.aipEndTime, this.messageTemplate);
            return false;
        }

        return true;
    }
}
