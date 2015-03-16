package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.id.ShortAcctIdUtil;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.biz.req.FundAcctCreateReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.EAcctType;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.UserAcctExistException;
import com.hengxin.platform.fund.exception.UserSubAcctExistException;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.service.AcctEscrowedModeService;
import com.hengxin.platform.fund.service.FundAcctManageService;
import com.hengxin.platform.security.enums.EBizRole;

@Service
@Qualifier("fundAcctManageService")
public class FundAcctManageServiceImpl implements FundAcctManageService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FundAcctManageServiceImpl.class);
	
	private final static List<EBizRole> specialAcctNoBizRoles = Arrays.asList(EBizRole.AUTHZ_CNTL, EBizRole.PROD_SERV);

	@Autowired
	private AcctRepository acctRepository;

	@Autowired
	private SubAcctRepository subAcctRepository;
	
	@Autowired
	private BankAcctRepository bankAcctRepository;
	
	@Autowired
	private AcctEscrowedModeService acctEscrowedModeService;

	@Override
	@Transactional
	public String createUserAcct(FundAcctCreateReq req) {
		if (StringUtils.isEmpty(req.getUserId())) {
			throw new NullPointerException("请求中的userId为空");
		}
		EBizRole role = req.getAssignRole();
		boolean specialGenerate = getSpecialGenerateWay(role);
		AcctPo acctPo = this.createFundAcct(req.getUserId(),
				EAcctType.DEBT.getCode(), specialGenerate);
		return acctPo.getAcctNo();
	}
	
	private boolean getSpecialGenerateWay(EBizRole role){
		return role!=null&&specialAcctNoBizRoles.contains(role);
	}

	@Override
	@Transactional
	public String createExchangeAcct(FundAcctCreateReq req) {
		if (StringUtils.isEmpty(req.getUserId())) {
			throw new NullPointerException("请求中的userId为空");
		}
		AcctPo acctPo = this.createFundAcct(req.getUserId(),
				EAcctType.ASSETS.getCode(), false);
		return acctPo.getAcctNo();
	}

	/**
	 * 根据账户类型创建综合账户
	 * 
	 * @param userId
	 * @param acctType
	 * @return
	 * @throws ServiceException
	 */
	private AcctPo createFundAcct(String userId, String acctType, boolean specialGenerate) {
		boolean exist = this.existsAcct(userId);
		if (exist) {
			throw new UserAcctExistException("此账户已存在");
		}
		ECashPool cashPool = generateECashPool(userId);
		if (LOG.isDebugEnabled()) {
			LOG.debug("开始创建用户" + userId + "的综合账户");
		}
		AcctPo acctPo = new AcctPo();
		if(specialGenerate){
			String acctNo = ShortAcctIdUtil.generateId(userId);
			acctPo.setAcctNo(acctNo);
		}
		acctPo.setUserId(userId);
		acctPo.setAcctStatus(EAcctStatus.NORMAL.getCode());
		acctPo.setAcctType(acctType);
		acctPo.setCashPool(cashPool.getCode());
		acctPo.setReservedAsset(BigDecimal.ZERO);
		acctPo.setFrzCt(Long.valueOf(0));
		acctPo.setCreateOpid(DictConsts.SYSTEM_OPID);
		acctPo.setCreateTs(new Date());
		acctPo = this.acctRepository.save(acctPo);
		if (LOG.isDebugEnabled()) {
			LOG.debug("创建综合账户子账户");
		}
		this.createSubAcct(acctPo.getAcctNo());
		return acctPo;
	}
	
	private ECashPool generateECashPool(String userId){
		ECashPool cashPool = null;
		boolean escrowMode = acctEscrowedModeService.isEscrowedMode();
		if(escrowMode){
			cashPool = acctEscrowedModeService.getCashPool(null, null);
		}
		else{
			// 根据银行卡类型判断资金池
			List<BankAcctPo> bnkList = bankAcctRepository.findBankAcctByUserId(userId);
			if(bnkList!=null&&!bnkList.isEmpty()){
				BankAcctPo bnkAcct = bnkList.get(0);
				cashPool = acctEscrowedModeService.getCashPool(bnkAcct.getSignedFlg(), bnkAcct.getBnkCd());
			}
		}
		cashPool = cashPool==null?ECashPool.CMB_COMMON:cashPool;
		return cashPool;
	}

	/**
	 * 创建综合账户子账户
	 * 
	 * @param req
	 * @param accountId
	 */
	private void createSubAcct(String acctNo) {
		this.createUserSubAcct(acctNo, ESubAcctNo.CURRENT.getCode(),
				ESubAcctType.CURRENT.getCode());
		this.createUserSubAcct(acctNo, ESubAcctNo.LOAN.getCode(),
				ESubAcctType.LOAN.getCode());
		this.createUserSubAcct(acctNo, ESubAcctNo.PLEDGE.getCode(),
				ESubAcctType.PLEDGE.getCode());
		this.createUserSubAcct(acctNo, ESubAcctNo.XWB.getCode(),
				ESubAcctType.XWB.getCode());
	}

	/**
	 * 通过子账户类型创建子账户
	 * 
	 * @param subAcctPo
	 * @param subNo
	 * @param subType
	 * @throws ServiceException
	 */
	private void createUserSubAcct(String acctNo, String subAcctNo,
			String subAcctType) {
		boolean exist = this.existsSubAcct(acctNo, subAcctNo);
		if (exist) {
			throw new UserSubAcctExistException("综合子账户" + subAcctType + "已经存在");
		}
		LOG.debug("开始创建综合子账户" + acctNo + "子账户" + subAcctType);
		SubAcctPo subAcctPo = new SubAcctPo();
		subAcctPo.setAcctNo(acctNo);
		subAcctPo.setSubAcctNo(subAcctNo);
		subAcctPo.setAcctType(subAcctType);
		subAcctPo.setAccruedIntrAmt(BigDecimal.ZERO);
		subAcctPo.setBal(BigDecimal.ZERO);
		subAcctPo.setMaxCashableAmt(BigDecimal.ZERO);
		subAcctPo.setDebtAmt(BigDecimal.ZERO);
		subAcctPo.setFreezableAmt(BigDecimal.ZERO);
		subAcctPo.setIntrBal(BigDecimal.ZERO);
		subAcctPo.setReservedAmt(BigDecimal.ZERO);
		subAcctPo.setCreateOpid(DictConsts.SYSTEM_OPID);
		subAcctPo.setCreateTs(new Date());
		this.subAcctRepository.save(subAcctPo);
	}

	@Override
	public boolean existsAcct(String userId) {
		AcctPo acctPo = this.acctRepository.findByUserId(userId);
		if (acctPo != null) {
			return true;
		}
		return false;
	}

	private boolean existsSubAcct(String acctNo, String subNo) {
		SubAcctPo subAcctPo = this.subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctNo, subNo);
		if (subAcctPo != null) {
			return true;
		}
		return false;
	}

}
