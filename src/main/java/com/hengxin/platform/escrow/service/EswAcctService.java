package com.hengxin.platform.escrow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.repository.EswAcctRepository;

@Service
public class EswAcctService {

	@Autowired
	private EswAcctRepository eswAcctRepository;
	
	@Transactional(readOnly=true)
	public EswAcctPo getEscrowAccountByUserId(String userId) {
		return eswAcctRepository.findByUserId(userId);
	}
	
	@Transactional(readOnly=true)
	public EswAcctPo getEscrowAccountByAcctNo(String acctNo) {
		return eswAcctRepository.findOne(acctNo);
	}
}
 