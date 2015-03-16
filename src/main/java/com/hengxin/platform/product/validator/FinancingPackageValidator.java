package com.hengxin.platform.product.validator;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.dto.FinancingTransactionSettingsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.service.ProductService;

@Component
public class FinancingPackageValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageCheck, FinancingTransactionSettingsDto> {

    @Autowired
    private ProductService productService;

    private String average;

    @Override
    public void initialize(FinancingPackageCheck obj) {
        this.average = obj.average();
    }

    @Override
    public boolean isValid(FinancingTransactionSettingsDto transSettingsDto, ConstraintValidatorContext context) {
        String productId = transSettingsDto.getProductId();
        Product product = productService.getProductById(productId);
        if (product == null) {
            return false;
        }
        List<ProductPackageDto> packageList = transSettingsDto.getPackageList();
        BigDecimal totalQuato = BigDecimal.ZERO;
        for (ProductPackageDto productPackageDto : packageList) {
            totalQuato = totalQuato.add(productPackageDto.getPackageQuota());
        }
        if (product.getAppliedQuota().compareTo(totalQuato) != 0) {
            bindNode(context, this.average, null);
            return false;
        }
        return true;
    }

}
