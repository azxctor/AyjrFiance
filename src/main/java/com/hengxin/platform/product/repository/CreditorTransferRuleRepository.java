package com.hengxin.platform.product.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.CreditorTransferRule;

public interface CreditorTransferRuleRepository extends PagingAndSortingRepository<CreditorTransferRule, String> {
    List<CreditorTransferRule> findByRuleIdNotNull();
}
