package com.hengxin.platform.fund.service;

import java.util.List;

import com.hengxin.platform.fund.domain.BankAcct;

/**
 * BankAcctService.
 *
 */
public interface BankAcctService {

	/**
	 * 创建会员银行卡信息.
	 * 
	 * @param bnkAcct
	 * @throws ServiceException
	 */
	void createBankAcct(BankAcct bnkAcct);

	/**
	 * 更新会员银行卡信息.
	 * 
	 * @param bnkAcct
	 * @throws ServiceException
	 */
	void updateBankAcct(BankAcct bnkAcct);

	/**
	 * 根据会员编号查询银行卡信息.
	 * 
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	List<BankAcct> findBankAcctByUserId(String userId);

	/**
	 * 根据会员编号和签约标记查询银行卡信息.
	 * 
	 * @param userId
	 * @param signedFlg
	 * @return
	 * @throws ServiceException
	 */
	List<BankAcct> findBankAcctByUserIdAndSignedFlg(String userId,
			String signedFlg);

	/**
	 * 通过银行卡号获取银行卡信息.
	 * 
	 * @param bankAcctNo
	 * @return
	 */
	BankAcct findBankAcctByBnkAcctNo(String bankAcctNo);
	
	/**
	 * 根据会员编号查询银行卡信息且不检查银行资料是否存在.
	 * @param userId
	 * @return
	 */
	List<BankAcct> findBankAcctByUserIdWihoutCheck(String userId);

}
