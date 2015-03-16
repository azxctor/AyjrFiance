package com.hengxin.platform.fund.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.AcctFreezeLogDto;
import com.hengxin.platform.fund.dto.AcctFreezeSearchDto;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.entity.UserAcctFreezeDtlView;


/**
 * 资金账户只收不付管理服务
 * @author dcliu
 *
 */
public interface FundAcctFreezeMgtService {
	
	/**
	 * 会员资金账户冻结
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	public String freezeAcct(FreezeReq req, boolean isBizFreeze)throws BizServiceException;
	
	/**
	 * 会员资金账户解冻
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	public BigDecimal unFreezeAcct(UnFreezeReq req, boolean isBizFreeze)throws BizServiceException;

	/**
	 * 
	* Description: 解冻账户列表
	*
	* @param accountFrozenSearchDto
	* @return
	 */
	public Page<UserAcctFreezeDtlView> getUserAcctFreezeList(AcctFreezeSearchDto accountFrozenSearchDto);
	
	/**
     * 
    * Description: 账户冻结日志
    *
    * @param accountFrozenSearchDto
    * @return
     */
    public Page<UserAcctFreezeDtlView> getUserAcctFreezeLogList(AcctFreezeLogDto acctFreezeLogDto);
}
