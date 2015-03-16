package com.hengxin.platform.escrow.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.escrow.entity.EswAcctPo;

@Repository
public interface EswAcctRepository extends
	JpaRepository<EswAcctPo, String>,
	JpaSpecificationExecutor<EswAcctPo> {

	public EswAcctPo findByUserId(String userId);
	
	public EswAcctPo findByEswAcctNo(String eswAcctNo);

	public EswAcctPo findByBankCardNo(String bankCardNo);

}
