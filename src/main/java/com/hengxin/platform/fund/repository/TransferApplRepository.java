package com.hengxin.platform.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.TransferApplPo;
import com.hengxin.platform.fund.enums.EFundApplStatus;

@Repository
public interface TransferApplRepository extends JpaRepository<TransferApplPo, String>,
	JpaSpecificationExecutor<TransferApplPo> {
	
	public List<TransferApplPo> findByImportFileNameAndApplStatusIn(String importFileName, List<EFundApplStatus> status);
	
}
