package com.hengxin.platform.product.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.product.domain.MortgageResidential;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductRealEstateMortgageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.FinancingPackageService;

@Component
public class ProductMortgageRealEsateCertificateNoCheckValidator extends BaseValidator implements
        ConstraintValidator<ProductMortgageRealEsateCertificateNoCheck, String> {

    @Autowired
    private ProductRealEstateMortgageRepository repsotory;

    @Autowired
    private FinancingPackageService service;

    @Autowired
    private ProductRepository productRepsotory;

    @Override
    public void initialize(ProductMortgageRealEsateCertificateNoCheck obj) {
    }

    @Override
    public boolean isValid(String premisesPermitNo, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(premisesPermitNo)){
            return true;
        }
        List<MortgageResidential> list = repsotory.findAll();
        for (MortgageResidential mor : list) {
            if (premisesPermitNo.equals(mor.getPremisesPermitNo())) {
                Product p = productRepsotory.findByProductId(mor.getProductId());
                if (!(EProductStatus.STANDBY == p.getStatus() || EProductStatus.NULL == p.getStatus()||EProductStatus.ABANDONED==p.getStatus())) {
                   // MortgageResidential pm = repsotory.findByPremisesPermitNo(premisesPermitNo);
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
