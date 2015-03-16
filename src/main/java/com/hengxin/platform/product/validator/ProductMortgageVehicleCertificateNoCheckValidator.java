package com.hengxin.platform.product.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductMortgageVehicle;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductMortgageVehicleRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.FinancingPackageService;

@Component
public class ProductMortgageVehicleCertificateNoCheckValidator extends BaseValidator implements
        ConstraintValidator<ProductMortgageVehicleCertificateNoCheck, String> {

    @Autowired
    private ProductMortgageVehicleRepository repsotory;

    @Autowired
    private FinancingPackageService service;

    @Autowired
    private ProductRepository productRepsotory;

    @Override
    public void initialize(ProductMortgageVehicleCertificateNoCheck obj) {
    }

    @Override
    public boolean isValid(String registNo, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(registNo)){
            return true;
        }

        List<ProductMortgageVehicle> list = repsotory.findAll();
        for (ProductMortgageVehicle mor : list) {
            if (registNo.equals(mor.getRegistNo())) {
                Product p = productRepsotory.findByProductId(mor.getProductId());
                if (!(EProductStatus.STANDBY == p.getStatus() || EProductStatus.NULL == p.getStatus()||EProductStatus.ABANDONED==p.getStatus())) {
                    List<ProductPackage> plist = service.getProductPackageListByProductId(mor.getProductId());
                    if (plist.size() == 0) {
                        return false;
                    }
                    for (ProductPackage productPackage : plist) {
                        if (!(EPackageStatus.END == productPackage.getStatus())) {
                            return false;
                        }
                        return true;

                    }
                }
            }
        }
        return true;
    }
}
