package com.hengxin.platform.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.product.domain.PackageContract;

@Repository
public interface PackageContractRepository extends JpaRepository<PackageContract, String>, JpaSpecificationExecutor<PackageContract> {

}
