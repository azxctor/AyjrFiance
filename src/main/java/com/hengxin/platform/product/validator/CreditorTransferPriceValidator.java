package com.hengxin.platform.product.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.fund.service.PositionLotService;
import com.hengxin.platform.product.dto.FinancePackageInvestorDto;
import com.hengxin.platform.product.dto.TransferPriceDto;

@Component
public class CreditorTransferPriceValidator extends BaseValidator implements
        ConstraintValidator<CreditorTransferPriceCheck, TransferPriceDto> {

    private String transPrice;
    private String messageTemplete;
    
    @Autowired
    private PositionLotService positionLotService;
    
    @Override
    public void initialize(CreditorTransferPriceCheck constraintAnnotation) {
        this.transPrice = constraintAnnotation.transPrice();
        this.messageTemplete = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(TransferPriceDto value, ConstraintValidatorContext context) {
		FinancePackageInvestorDto financePackageInvestorDto = positionLotService.getFullPositionLotById(value.getLotId());
        if (value.getTransPrice() != null &&value.getTransPrice().compareTo(financePackageInvestorDto.getTransferMaxPrice()) >0) {
            bindNode(context, this.transPrice, this.messageTemplete);
            return false;
        } else {
            return true;
        }
    }
    
}
