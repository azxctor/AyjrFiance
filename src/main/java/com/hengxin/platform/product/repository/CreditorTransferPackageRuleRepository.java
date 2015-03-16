package com.hengxin.platform.product.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.CreditorTransferPackageRule;

public interface CreditorTransferPackageRuleRepository extends PagingAndSortingRepository<CreditorTransferPackageRule, String>{

    CreditorTransferPackageRule findByPackageId(String packageId);
    
    List<CreditorTransferPackageRule> findByPackageIdNotNull();
    
    List<CreditorTransferPackageRule> findByPackageTypeNotNull();
    
    List<CreditorTransferPackageRule> findByPackageIdAndAllowType(String packageId,String allowType);
    
    List<CreditorTransferPackageRule> findByPackageTypeAndAllowType(String packageType,String allowType);
}
