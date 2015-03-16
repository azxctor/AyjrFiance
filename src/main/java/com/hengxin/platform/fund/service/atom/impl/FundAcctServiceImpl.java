/*
 * Project Name: kmfex-platform
 * File Name: AcctService.java
 * Class Name: AcctService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.fund.service.atom.impl;

import static com.hengxin.platform.common.enums.EErrorCode.FUND_ACCT_REVERSE_FAILED;
import static com.hengxin.platform.common.enums.EErrorCode.TECH_DATA_INVALID;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.InternalAcctTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.FreezeReserveDtlPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.FreezeReserveDtlRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.repository.SubAcctTrxJnlRepository;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.atom.comparator.ComparatorTransferInfo;
import com.hengxin.platform.fund.service.support.AcctUtils;
import com.hengxin.platform.fund.service.support.BusinessAcctUtils;
import com.hengxin.platform.fund.service.support.ServiceCenterUtils;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * FundAcctService.
 *
 */
@Service
@Qualifier("fundAcctService")
public class FundAcctServiceImpl implements FundAcctService {

	private static final Logger LOG = LoggerFactory.getLogger(FundAcctServiceImpl.class);

	private static final Long INCREASE_COUNT = Long.valueOf(1);
	private static final Long DECREASE_COUNT = Long.valueOf(-1);
	private static final int MAX_LENGTH_200 = 200;

	@Autowired
	private FreezeReserveDtlRepository freezeReserveDtlRepository;

	@Autowired
	private SubAcctTrxJnlRepository subAcctTrxJnlRepository;

	@Autowired
	private SubAcctRepository subAcctRepository;

	@Autowired
	private AcctRepository acctRepository;

	@Override
	public String freezeAmt(FreezeReq req) throws BizServiceException {
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		return this.freezeAmt(req, acctPo.getAcctNo(), EFnrOperType.FREEZE);
	}

	private AcctPo getAcctByUserId(String userId)
			throws BizServiceException {
		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = acctRepository.findByUserId(userId);
		if (acctPo == null) {
			throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST,
					"会员综合账户不存在");
		}
		return acctPo;
	}

	private SubAcctPo getSubAcctByAcctNoAndSubAcctNo(String acctNo,
			String subAcctNo) throws BizServiceException {

		SubAcctPo subAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctNo, subAcctNo);
		if (subAcctPo == null) {
			throw new BizServiceException(EErrorCode.SUB_ACCT_NOT_EXIST,
					subAcctNo + "子账户不存在");
		}

		return subAcctPo;
	}

	private String freezeAmt(FreezeReq req, String acctNo,
			EFnrOperType frzOperType) throws BizServiceException {

		String userId = req.getUserId();
		EFundUseType useType = req.getUseType();
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
				BigDecimal.ZERO);
		String trxMemo = req.getTrxMemo();
		String relBizId = req.getBizId();
		String currOpId = req.getCurrOpId();
		Date workDate = req.getWorkDate();
		String workDateStr = DateUtils.formatDate(workDate,
				DictConsts.WORK_DATE_FORMAT);
		Date currDate = new Date();

		if (LOG.isDebugEnabled()) {
			LOG.debug("会员活期子账户资金冻结");
			LOG.debug("useId" + userId);
			LOG.debug("useType" + req.getUseType().getText());
			LOG.debug("trxAmt" + trxAmt.doubleValue());
			LOG.debug("trxMemo" + trxMemo);
			LOG.debug("relBizId" + relBizId);
			LOG.debug("currOpId" + currOpId);
			LOG.debug("workDate" + workDateStr);
		}

		if (trxAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 获取该会员的 活期子账户信息
			SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acctNo,
					ESubAcctNo.CURRENT.getCode());

			// 计算应冻结金额
			BigDecimal freezeAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getFreezableAmt(), BigDecimal.ZERO);
			BigDecimal newFreezeAmt = freezeAmt.add(trxAmt);

			// 更新子账户相关金额
			SubAcctPo newSubAcctPo = ConverterService.convert(subAcctPo,
					SubAcctPo.class);
			newSubAcctPo.setFreezableAmt(newFreezeAmt);
			newSubAcctPo.setLastMntOpid(currOpId);
			newSubAcctPo.setLastMntTs(workDate);
			subAcctRepository.save(newSubAcctPo);

			if (LOG.isDebugEnabled()) {
				LOG.debug("冻结金额" + trxAmt.doubleValue());
				LOG.debug("原应冻结金额" + freezeAmt.doubleValue());
				LOG.debug("现应冻结金额" + newFreezeAmt.doubleValue());
			}
		}

		FreezeReserveDtlPo acFnrDtlPo = new FreezeReserveDtlPo();
		acFnrDtlPo.setAcctNo(acctNo);
		acFnrDtlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		acFnrDtlPo.setUseType(useType);
		acFnrDtlPo.setOperType(frzOperType);
		acFnrDtlPo.setStatus(EFnrStatus.ACTIVE);
		acFnrDtlPo.setEffectDt(workDate);
		acFnrDtlPo
				.setExpireDt(DateUtils.getDate(
						DictConsts.DEFAULT_MAX_DATE_VALUE,
						DictConsts.WORK_DATE_FORMAT));
		acFnrDtlPo.setTrxAmt(trxAmt);
		acFnrDtlPo.setTrxMemo(trxMemo);
		acFnrDtlPo.setRelBizId(relBizId);
		acFnrDtlPo.setCreateOpid(currOpId);
		acFnrDtlPo.setCreateTs(currDate);
		FreezeReserveDtlPo retObj = freezeReserveDtlRepository.save(acFnrDtlPo);
		// 流水号在保存的时候自动生成返回
		if (LOG.isDebugEnabled()) {
			LOG.debug("新增保证金冻结明细");
			LOG.debug("更新冻结金额为" + trxAmt.doubleValue());
			LOG.debug("返回的冻结明细流水号为" + retObj.getJnlNo());
		}

		return retObj.getJnlNo();
	}

	@Override
	public BigDecimal unFreezeAmt(UnFreezeReq req) throws BizServiceException {
		// 通过会员编号获取综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		return this.unFreezeAmt(req, acctPo);
	}

	private BigDecimal unFreezeAmt(UnFreezeReq req, AcctPo acctPo)
			throws BizServiceException {

		String frzJnlNo = req.getFreezeJnlNo();
		String trxMemo = req.getTrxMemo();
		String currOpId = req.getCurrOpId();
		Date workDate = req.getWorkDate();
		Date currDate = new Date();

		String acctNo = acctPo.getAcctNo();

		FreezeReserveDtlPo unFreezePo = freezeReserveDtlRepository
				.findFreezeReserveDtlByJnlNo(frzJnlNo);

		// 检查明细状态是否被关闭
		BusinessAcctUtils.checkDataPreUnFreeze(unFreezePo);
		
		if(!StringUtils.equalsIgnoreCase(unFreezePo.getAcctNo(), acctPo.getAcctNo())){
			throw new BizServiceException(TECH_DATA_INVALID,"冻结明细所属综合账户与会员编号不一致");
		}

		// 流水号对应的被解冻金额
		BigDecimal hasFrzAmt = AmtUtils.processNegativeAmt(
				unFreezePo.getTrxAmt(), BigDecimal.ZERO);

		if (hasFrzAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 获取该会员的 冻结的子账户信息
			SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acctNo,
					unFreezePo.getSubAcctNo());

			// 原应冻结金额
			BigDecimal oldFrzAbleAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getFreezableAmt(), BigDecimal.ZERO);

			// 新的应冻结金额 = Max(原冻结金额 - 流水号对应的被解冻金额,0)
			BigDecimal newFreezableAmt = AmtUtils.processNegativeAmt(
					oldFrzAbleAmt.subtract(hasFrzAmt), BigDecimal.ZERO);

			if (LOG.isDebugEnabled()) {
				LOG.debug(" 应冻结金额计算 ");
				LOG.debug(" 新的应冻结金额 = Max(原冻结金额 - 流水号对应的被解冻金额,0)  ");
				LOG.debug(" 计算结果= " + newFreezableAmt.doubleValue());
			}

			// 原最大可提现金额
			BigDecimal oldMaxCashAbleAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);

			// 资金冻结日期
			Date frzDate = unFreezePo.getEffectDt();
			Long frzDateLong = DateUtils.date2yyyyMMddLong(frzDate);
			Long workDateLong = DateUtils.date2yyyyMMddLong(workDate);

			BigDecimal newMaxCashAbleAmt = BigDecimal.ZERO;
			// 冻结日期 < 当前工作日期，则把解冻的金额加上最大可提现金额上
			if (frzDateLong.compareTo(workDateLong) < 0) {
				newMaxCashAbleAmt = newMaxCashAbleAmt.add(hasFrzAmt);
			}
			// 新最大可提现金额
			newMaxCashAbleAmt = newMaxCashAbleAmt.add(oldMaxCashAbleAmt);

			// 更新子账户金额
			if (LOG.isDebugEnabled()) {
				LOG.debug("更新子账户金额");
			}
			SubAcctPo target = ConverterService.convert(subAcctPo,
					SubAcctPo.class);
			target.setMaxCashableAmt(newMaxCashAbleAmt);
			target.setFreezableAmt(newFreezableAmt);
			target.setLastMntOpid(currOpId);
			target.setLastMntTs(currDate);
			subAcctRepository.save(target);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("解冻该笔保证金冻结明细");
		}

		String oldTrxMemo = StringUtils.defaultIfEmpty(unFreezePo.getTrxMemo(), "");
		// 解冻该笔保证金冻结明细
		FreezeReserveDtlPo targetPo = ConverterService.convert(unFreezePo,
				FreezeReserveDtlPo.class);
		targetPo.setExpireDt(workDate);
		targetPo.setTrxMemo(AcctUtils.substr(oldTrxMemo + " " + trxMemo, MAX_LENGTH_200));
		targetPo.setLastMntOpid(currOpId);
		targetPo.setLastMntTs(currDate);
		targetPo.setStatus(EFnrStatus.CLOSE);
		freezeReserveDtlRepository.save(targetPo);

		return hasFrzAmt;
	}

	/**
	 * 资金保留.
	 * 
	 * @throws AmtParamInvalidException
	 * @throws AcctNotExistException
	 * @throws AcctStatusIllegalException
	 * @throws SubAcctNotExistException
	 * @throws AvlBalNotEnoughException
	 */
	@Override
	public String reserveAmt(ReserveReq req) throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("金额保留");
			LOG.debug("会员编号:" + req.getUserId());
			LOG.debug("资金用途:" + req.getUseType().getText());
			LOG.debug("保留金额:" + req.getTrxAmt());
			LOG.debug("备 注:" + req.getTrxMemo());
			LOG.debug("关联业务编号编号:" + req.getBizId());
			LOG.debug("当前操作用户编号:" + req.getCurrOpId());
			LOG.debug("当前工作日期:" + req.getWorkDate());
			LOG.debug("是否使用小微宝:" + req.isAddXwb());
		}

		// 被保留金额
		BigDecimal reservedAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
				BigDecimal.ZERO);

		// currentDate
		Date currDate = new Date();

		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());

		if (reservedAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 获取该会员的 活期子账户信息
			SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(
					acctPo.getAcctNo(), ESubAcctNo.CURRENT.getCode());
			// 账户余额
			BigDecimal bal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
					BigDecimal.ZERO);
			// 原保留金额
			BigDecimal oldReservedAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getReservedAmt(), BigDecimal.ZERO);
			// 原冻结金额
			BigDecimal oldFreezableAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getFreezableAmt(), BigDecimal.ZERO);
			// 可用余额 = 账户余额 - 保留金额 - 冻结金额 。 此处不与0.00做大值处理
			BigDecimal currAvlAmt = bal.subtract(oldReservedAmt).subtract(
					oldFreezableAmt);
			// 判断可用余额是否大于被保留金额
			int result = currAvlAmt.compareTo(reservedAmt);

			if (result < 0) {

				if (!req.isAddXwb()) {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(),"账户"
							+ acctPo.getAcctNo() + "可用余额不足");
				}
				// 差额
				BigDecimal netAmt = reservedAmt.subtract(currAvlAmt);

				SubAcctPo xwbSubAcct = getSubAcctByAcctNoAndSubAcctNo(
						acctPo.getAcctNo(), ESubAcctNo.XWB.getCode());
				// 账户余额
				BigDecimal xwbBal = AmtUtils.processNegativeAmt(
						xwbSubAcct.getBal(), BigDecimal.ZERO);
				// 原保留金额
				BigDecimal oldXwbReservedAmt = AmtUtils.processNegativeAmt(
						xwbSubAcct.getReservedAmt(), BigDecimal.ZERO);
				// 原冻结金额
				BigDecimal oldXwbFreezableAmt = AmtUtils.processNegativeAmt(
						xwbSubAcct.getFreezableAmt(), BigDecimal.ZERO);
				BigDecimal xwbAvlAmt = AmtUtils.max(
						xwbBal.subtract(oldXwbReservedAmt).subtract(
								oldXwbFreezableAmt), BigDecimal.ZERO);
				int cmp = xwbAvlAmt.compareTo(netAmt);
				if (cmp < 0) {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户"
							+ acctPo.getAcctNo() + "可用余额不足");
				}
				// 处理小微宝金额
				InternalAcctTransferInfo interReq = new InternalAcctTransferInfo();
				interReq.setBizId(req.getBizId());
				interReq.setCurrOpId(req.getCurrOpId());
				interReq.setEventId(req.getBizId());
				interReq.setFromAcctType(ESubAcctType.XWB);
				interReq.setToAcctType(ESubAcctType.CURRENT);
				interReq.setTrxAmt(netAmt);
				interReq.setTrxMemo(req.getTrxMemo());
				interReq.setUserId(req.getUserId());
				interReq.setUseType(EFundUseType.INTERNAL);
				interReq.setWorkDate(req.getWorkDate());
				this.internalAcctTransferPayerDeal(acctPo, interReq);
				this.internalAcctTransferPayeeDeal(acctPo, acctPo.getCashPool(), interReq);

				// 获取划转之后，活期子账户信息
				subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acctPo.getAcctNo(),
						ESubAcctNo.CURRENT.getCode());
				// 账户余额
				bal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
						BigDecimal.ZERO);
				// 原保留金额
				oldReservedAmt = AmtUtils.processNegativeAmt(
						subAcctPo.getReservedAmt(), BigDecimal.ZERO);
				// 原冻结金额
				oldFreezableAmt = AmtUtils.processNegativeAmt(
						subAcctPo.getFreezableAmt(), BigDecimal.ZERO);
				// 可用余额 = 账户余额 - 保留金额 - 冻结金额 。 此处不与0.00做大值处理
				/**
				 * Dead store to local variable. 
				 *
				 * currAvlAmt = AmtUtils.max(bal.subtract(oldReservedAmt)
				 *		.subtract(oldFreezableAmt), BigDecimal.ZERO);
				 */
			}

			// 新保留金额 = 原保留金额 + 保留金额
			BigDecimal newReserveAmt = oldReservedAmt.add(reservedAmt);

			// 更新会员活期账户
			SubAcctPo targetAcSubAcctPo = ConverterService.convert(subAcctPo,
					SubAcctPo.class);
			targetAcSubAcctPo.setReservedAmt(newReserveAmt);
			targetAcSubAcctPo.setLastMntOpid(req.getCurrOpId());
			targetAcSubAcctPo.setLastMntTs(currDate);
			subAcctRepository.save(targetAcSubAcctPo);
		}

		// 添加账户冻结保留日志
		FreezeReserveDtlPo acFreezeReserveDtlPo = new FreezeReserveDtlPo();
		acFreezeReserveDtlPo.setAcctNo(acctPo.getAcctNo());
		acFreezeReserveDtlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		acFreezeReserveDtlPo.setOperType(EFnrOperType.RESERVE);
		acFreezeReserveDtlPo.setStatus(EFnrStatus.ACTIVE);
		acFreezeReserveDtlPo.setEffectDt(req.getWorkDate());
		acFreezeReserveDtlPo
				.setExpireDt(DateUtils.getDate(
						DictConsts.DEFAULT_MAX_DATE_VALUE,
						DictConsts.WORK_DATE_FORMAT));
		acFreezeReserveDtlPo.setUseType(req.getUseType());
		acFreezeReserveDtlPo.setTrxAmt(req.getTrxAmt());
		acFreezeReserveDtlPo.setTrxMemo(req.getTrxMemo());
		acFreezeReserveDtlPo.setRelBizId(req.getBizId());
		acFreezeReserveDtlPo.setCreateOpid(req.getCurrOpId());
		acFreezeReserveDtlPo.setCreateTs(currDate);
		FreezeReserveDtlPo retAcFreezeReserveDtlPo = freezeReserveDtlRepository
				.save(acFreezeReserveDtlPo);
		return retAcFreezeReserveDtlPo.getJnlNo();
	}

	/**
	 * 资金解保留.
	 * 
	 * @throws AcctNotExistException
	 * @throws SubAcctNotExistException
	 * @throws FreezeReserveDtlOperTypeInvalidException
	 * @throws FreezeReserveDtlCloseException
	 * @throws AmtParamInvalidException
	 */
	@Override
	public BigDecimal unReserveAmt(UnReserveReq req) throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("提现金额解保留");
			LOG.debug("会员编号:" + req.getUserId());
			LOG.debug("备 注:" + req.getTrxMemo());
			LOG.debug("保留日志流水:" + req.getReserveJnlNo());
			LOG.debug("当前操作用户编号:" + req.getCurrOpId());
			LOG.debug("当前工作日期:" + req.getWorkDate());
		}

		// currentDate
		Date currDate = new Date();

		// 获取原保留日志
		FreezeReserveDtlPo acFreezeReserveDtlPo = freezeReserveDtlRepository
				.findFreezeReserveDtlByJnlNo(req.getReserveJnlNo());

		// 检查明细状态是否被关闭
		BusinessAcctUtils.checkDataPreUnReserve(acFreezeReserveDtlPo);

		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		
		if(!StringUtils.equalsIgnoreCase(acFreezeReserveDtlPo.getAcctNo(), acctPo.getAcctNo())){
			throw new BizServiceException(TECH_DATA_INVALID,"保留明细所属综合账户与会员编号不一致");
		}

		// 解保留金额
		BigDecimal hasReserveAmt = AmtUtils.processNegativeAmt(
				acFreezeReserveDtlPo.getTrxAmt(), BigDecimal.ZERO);

		if (hasReserveAmt.compareTo(BigDecimal.ZERO) > 0) {

			// 获取该会员的 活期子账户信息
			SubAcctPo acSubAcctPo = getSubAcctByAcctNoAndSubAcctNo(
					acctPo.getAcctNo(), acFreezeReserveDtlPo.getSubAcctNo());

			// 原保留金额
			BigDecimal oldReserveAmt = AmtUtils.processNegativeAmt(
					acSubAcctPo.getReservedAmt(), BigDecimal.ZERO);

			// 新保留金额 = 原保留金额 - 解保留金额
			BigDecimal newReserveAmt = AmtUtils.processNegativeAmt(
					oldReserveAmt.subtract(hasReserveAmt), BigDecimal.ZERO);

			// 原可提现金额
			BigDecimal oldCashAbleAmt = AmtUtils.processNegativeAmt(
					acSubAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);

			// 新可提现金额
			BigDecimal newCashAbleAmt = BigDecimal.ZERO;
			if (DateUtils.isGreaterAndEqualsyyyyMMdd(acFreezeReserveDtlPo.getEffectDt(),
					req.getWorkDate())) {
				newCashAbleAmt = oldCashAbleAmt;
			} else {
				newCashAbleAmt = oldCashAbleAmt.add(hasReserveAmt);
			}

			// 更新会员活期账户
			SubAcctPo targetAcSubAcctPo = ConverterService.convert(acSubAcctPo,
					SubAcctPo.class);
			targetAcSubAcctPo.setReservedAmt(newReserveAmt);
			targetAcSubAcctPo.setMaxCashableAmt(newCashAbleAmt);
			targetAcSubAcctPo.setLastMntOpid(req.getCurrOpId());
			targetAcSubAcctPo.setLastMntTs(currDate);
			subAcctRepository.save(targetAcSubAcctPo);
		}

		String resverMemo = StringUtils.defaultIfEmpty(
				acFreezeReserveDtlPo.getTrxMemo(), "");
		// 更新账户冻结保留日志
		FreezeReserveDtlPo targetFreezeReserveDtlPo = ConverterService.convert(
				acFreezeReserveDtlPo, FreezeReserveDtlPo.class);
		targetFreezeReserveDtlPo.setStatus(EFnrStatus.CLOSE);
		targetFreezeReserveDtlPo.setExpireDt(req.getWorkDate());
		targetFreezeReserveDtlPo.setTrxMemo(AcctUtils.substr(resverMemo + " "
				+ req.getTrxMemo(), MAX_LENGTH_200));
		targetFreezeReserveDtlPo.setLastMntOpid(req.getCurrOpId());
		targetFreezeReserveDtlPo.setLastMntTs(currDate);
		freezeReserveDtlRepository.save(targetFreezeReserveDtlPo);
		return hasReserveAmt;
	}

	@Override
	public void internalAcctTransferAmt(InternalAcctTransferInfo req)
			throws BizServiceException {

		String from = req.getFromAcctType().getCode();
		String to = req.getToAcctType().getCode();
		// 校验内部账户转账是否有效
		BusinessAcctUtils.checkInternalAcctValidTransfer(from, to);

		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		String acctNo = acctPo.getAcctNo();

		if (LOG.isDebugEnabled()) {
			LOG.debug("综合账户资金内部转账处理");
			LOG.debug("acctNo" + acctNo);
			LOG.debug("from subAcctNo " + from);
			LOG.debug("to subAcctNo " + to);
			LOG.debug("eventId" + req.getEventId());
			LOG.debug("trxAmt" + req.getTrxAmt().doubleValue());
			LOG.debug("trxMemo" + req.getTrxMemo());
		}

		internalAcctTransferPayerDeal(acctPo, req);

		internalAcctTransferPayeeDeal(acctPo, acctPo.getCashPool(), req);

	}

	/**
	 * 综合账户内部子账户资金划转，付款子账户资金处理.
	 * 
	 * @param acctNo
	 * @param payerSubAcctNo
	 * @param req
	 * @throws SubAcctNotExistException
	 */
	private void internalAcctTransferPayerDeal(AcctPo acct,	InternalAcctTransferInfo req) throws BizServiceException {
		// 划转金额
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
				BigDecimal.ZERO);
		
		String userId = req.getUserId();
				
		Date currDate = new Date();

		// 付款子账户编号
		String payerSubAcctNo = BusinessAcctUtils.getSubAcctNoBySubAcctType(req
				.getFromAcctType().getCode());

		if (LOG.isDebugEnabled()) {
			LOG.debug("付款子账户资金处理 " + payerSubAcctNo);
		}

		// 查询付款子账户
		SubAcctPo payerSubAcctPo = getSubAcctByAcctNoAndSubAcctNo(
				acct.getAcctNo(), payerSubAcctNo);

		// 获取子账户可用余额
		BigDecimal payerAvlAmt = AcctUtils.getSubAcctAvlBal(payerSubAcctPo);
		if (payerAvlAmt.compareTo(BigDecimal.ZERO) <= 0) {
			throw new AvlBalNotEnoughException(acct.getAcctNo(), "子账户 " + payerSubAcctNo
					+ " 的可用余额小于等于0.00");
		}
		// 判断可用余额是否足
		if (payerAvlAmt.compareTo(trxAmt) < 0) {
			throw new AvlBalNotEnoughException(acct.getAcctNo(), "子账户 " + payerSubAcctNo
					+ " 的可用余额不足");
		}

		// 原金额
		BigDecimal payerOldBalAmt = AmtUtils.processNegativeAmt(
				payerSubAcctPo.getBal(), BigDecimal.ZERO);
		BigDecimal payerOldIntrBalAmt = AmtUtils.processNegativeAmt(
				payerSubAcctPo.getIntrBal(), BigDecimal.ZERO);
		BigDecimal payerOldMaxCashableAmt = AmtUtils.processNegativeAmt(
				payerSubAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);

		// 新金额
		BigDecimal payerNewBalAmt = AmtUtils.max(
				payerOldBalAmt.subtract(trxAmt), BigDecimal.ZERO);

		BigDecimal payerNewIntrBalAmt = BigDecimal.ZERO;
		// 余额宝，优先划转 不可计息余额
		if (AcctUtils.isXWBSubAcctNo(payerSubAcctNo)) {
			payerNewIntrBalAmt = BusinessAcctUtils.calXWBNowIntrBalAmtForPay(
					payerOldIntrBalAmt, payerOldBalAmt, trxAmt);
		} else {
			payerNewIntrBalAmt = AmtUtils.processNegativeAmt(
					payerOldIntrBalAmt.subtract(trxAmt), BigDecimal.ZERO);
		}

		BigDecimal payerNewMaxCashableAmt = BigDecimal.ZERO;
		BigDecimal unCashAmt = AmtUtils.max(
				payerAvlAmt.subtract(payerOldMaxCashableAmt), BigDecimal.ZERO);
		if (unCashAmt.compareTo(trxAmt) >= 0) {
			payerNewMaxCashableAmt = payerOldMaxCashableAmt;
		} else {
			BigDecimal netAmt = AmtUtils.processNegativeAmt(
					trxAmt.subtract(unCashAmt), BigDecimal.ZERO);
			payerNewMaxCashableAmt = AmtUtils.max(
					payerOldMaxCashableAmt.subtract(netAmt), BigDecimal.ZERO);
		}
		// 更新付款子账户相关金额信息
		SubAcctPo payerSubAcctNewAmt = ConverterService.convert(payerSubAcctPo,
				SubAcctPo.class);
		payerSubAcctNewAmt.setBal(payerNewBalAmt);
		payerSubAcctNewAmt.setIntrBal(payerNewIntrBalAmt);
		payerSubAcctNewAmt.setMaxCashableAmt(payerNewMaxCashableAmt);
		payerSubAcctNewAmt.setLastMntOpid(req.getCurrOpId());
		payerSubAcctNewAmt.setLastMntTs(currDate);
		subAcctRepository.save(payerSubAcctNewAmt);

		// 记录子账户付款交易日志
		SubAcctTrxJnlPo payerSubAcctJnl = new SubAcctTrxJnlPo();
		payerSubAcctJnl.setAcctNo(acct.getAcctNo());
		payerSubAcctJnl.setEventId(req.getEventId());
		payerSubAcctJnl.setPayRecvFlg(EFundPayRecvFlag.PAY);
		payerSubAcctJnl.setSubAcctNo(payerSubAcctPo.getSubAcctNo());
		payerSubAcctJnl.setTrxAmt(trxAmt);
		payerSubAcctJnl.setBal(payerNewBalAmt);
		payerSubAcctJnl.setCashPool(EnumHelper.translate(ECashPool.class, acct.getCashPool()));
		payerSubAcctJnl.setTrxDt(req.getWorkDate());
		payerSubAcctJnl.setTrxMemo(req.getTrxMemo());
		payerSubAcctJnl.setUseType(req.getUseType());
		payerSubAcctJnl.setRelBizId(req.getBizId());
		payerSubAcctJnl.setAuthzdCtrId(getAuthzdCtrId(userId));
		payerSubAcctJnl.setCreateOpid(req.getCurrOpId());
		payerSubAcctJnl.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(payerSubAcctJnl);
	}

	/**
	 * 综合账户内部子账户划转，收款子账户资金处理.
	 * 
	 * @param acctNo
	 * @param payeeSubAcctNo
	 * @param req
	 * @throws AmtParamInvalidException
	 * @throws SubAcctNotExistException
	 * @throws ServiceException
	 */
	private void internalAcctTransferPayeeDeal(AcctPo acct, String cashPool, 
			InternalAcctTransferInfo req) throws BizServiceException {
		// 划转金额
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
				BigDecimal.ZERO);

		Date currDate = new Date();

		// 收款子账户
		String payeeSubAcctNo = BusinessAcctUtils.getSubAcctNoBySubAcctType(req
				.getToAcctType().getCode());
		// 收款子账户信息
		if (LOG.isDebugEnabled()) {
			LOG.debug("收款子账户资金处理 " + payeeSubAcctNo);
		}

		internalSubAcctRecvAmt(acct, cashPool, payeeSubAcctNo, trxAmt, req, currDate);
	}

	private void internalSubAcctRecvAmt(AcctPo acct, String cashPool, String subAcctNo,
			BigDecimal toAmt, InternalAcctTransferInfo req, Date currDate)
			throws BizServiceException {
		
		String userId = req.getUserId();
		
		SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acct.getAcctNo(),
				subAcctNo);

		BigDecimal oldBalAmt = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);
		BigDecimal oldIntrBalAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getIntrBal(), BigDecimal.ZERO);

		// 计算收款子账户相关金额
		BigDecimal newBalAmt = oldBalAmt.add(toAmt);
		BigDecimal newIntrBalAmt = BigDecimal.ZERO;
		// 余额宝可计息金额计算，则需按特殊业务处理
		if (AcctUtils.isXWBSubAcctNo(subAcctNo)) {
			newIntrBalAmt = BusinessAcctUtils.calXWBNowIntrBalAmtForRecv(
					oldBalAmt, toAmt, currDate);
		} else {
			newIntrBalAmt = oldIntrBalAmt.add(toAmt);
		}

		SubAcctPo subAcctNewAmt = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		subAcctNewAmt.setBal(newBalAmt);
		subAcctNewAmt.setIntrBal(newIntrBalAmt);
		subAcctNewAmt.setLastMntOpid(req.getCurrOpId());
		subAcctNewAmt.setLastMntTs(currDate);
		subAcctRepository.save(subAcctNewAmt);

		// 记录子账户收款交易日志
		SubAcctTrxJnlPo trxJnl = new SubAcctTrxJnlPo();
		trxJnl.setAcctNo(acct.getAcctNo());
		trxJnl.setCashPool(EnumHelper.translate(ECashPool.class, cashPool));
		trxJnl.setEventId(req.getEventId());
		trxJnl.setPayRecvFlg(EFundPayRecvFlag.RECV);
		trxJnl.setSubAcctNo(subAcctPo.getSubAcctNo());
		trxJnl.setTrxAmt(toAmt);
		trxJnl.setBal(newBalAmt);
		trxJnl.setTrxDt(req.getWorkDate());
		trxJnl.setTrxMemo(req.getTrxMemo());
		trxJnl.setUseType(req.getUseType());
		trxJnl.setRelBizId(req.getBizId());
		trxJnl.setAuthzdCtrId(getAuthzdCtrId(userId));
		trxJnl.setCreateOpid(req.getCurrOpId());
		trxJnl.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(trxJnl);
	}

	/**
	 * 资金划转.
	 * 
	 * @throws AmtRecvPayNotEqualsException
	 * @throws AmtParamInvalidException
	 * @throws PayerOrPayeeCannotEmptyException
	 * @throws AvlBalNotEnoughException
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 */
	@Override
	public void transferAmt(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException {
		// 判断资金划转双方汇总金额是否匹配
		checkTransferAmt(payerList, payeeList);

		// 收款方资金划转
		payeeTransfer(eventId, payeeList, bizId, currOpId, workDate);

		// 付款方资金划转
		payerTransfer(eventId, payerList, isAddXwbPay, bizId, currOpId,
				workDate);
	}

	/**
	 * 收款方资金划转.
	 * 
	 * @param eventId
	 * @param payeeList
	 * @param bizId
	 * @param isRelZQ
	 * @param currOpId
	 * @param workDate
	 * @param useType
	 * @throws AmtParamInvalidException
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 */
	private void payeeTransfer(String eventId, List<TransferInfo> payeeList,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		if (payeeList == null || payeeList.isEmpty()){
			return;
		}
		Collections.sort(payeeList, new ComparatorTransferInfo());
		for (TransferInfo payeeInfo : payeeList) {
			this.payeeTransfer(eventId, payeeInfo, bizId, currOpId, workDate);
		}
	}

	/**
	 * 判断收款方是否含有投转贷，计算质押账户需要划转的金额.
	 * 
	 * @param isRelZQ
	 * @param payeeInfo
	 * @return
	 * @throws AmtParamInvalidException
	 */
	private BigDecimal calToPledgeAmt(SubAcctPo pledgeAcctPo, BigDecimal trxAmt)
			throws BizServiceException {

		BigDecimal toPledgeAmt = BigDecimal.ZERO;

		// 冻结金额
		BigDecimal freezeAmt = AmtUtils.processNegativeAmt(
				pledgeAcctPo.getFreezableAmt(), BigDecimal.ZERO);

		// 账户余额
		BigDecimal bal = AmtUtils.processNegativeAmt(pledgeAcctPo.getBal(),
				BigDecimal.ZERO);

		// 质押账户的应冻结金额 与 余额 比较，
		if (bal.compareTo(freezeAmt) < 0) {

			// 需转入质押账户的金额为 应冻结金额 - 余额
			BigDecimal requireAmt = freezeAmt.subtract(bal);

			if (requireAmt.compareTo(trxAmt) <= 0) {
				toPledgeAmt = requireAmt;
			} else {
				toPledgeAmt = trxAmt;
			}

		} else {
			toPledgeAmt = BigDecimal.ZERO;
		}
		return toPledgeAmt;
	}

	/**
	 * 付款方资金划转.
	 * 
	 * @param eventId
	 * @param payerList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws AmtParamInvalidException
	 * @throws AvlBalNotEnoughException
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 */
	private void payerTransfer(String eventId, List<TransferInfo> payerList,
			boolean isAddXwbPay, String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		if (payerList == null || payerList.isEmpty()){
			return;
		}
		Collections.sort(payerList, new ComparatorTransferInfo());
		for (TransferInfo payerInfo : payerList) {
			this.payerTransfer(eventId, payerInfo, isAddXwbPay, bizId,
					currOpId, workDate);
		}
	}

	private void payerTransfer(String eventId, TransferInfo payerInfo,
			boolean isAddXwbPay, String bizId, String currOpId, Date workDate)
			throws BizServiceException {

		String userId = payerInfo.getUserId();
		
		AcctPo acctPo = this.getAcctByUserId(userId);
		
		String cashPool = acctPo.getCashPool();
		String pool = AcctUtils.getValidCashPool(payerInfo.getCashPool());
		if(StringUtils.isNotBlank(pool)){
			cashPool = pool;
		}
		// 获取付款方会员活期子账户
		SubAcctPo subAcct = this.getSubAcctByAcctNoAndSubAcctNo(
				acctPo.getAcctNo(), ESubAcctNo.CURRENT.getCode());

		// 付款方原账户余额
		BigDecimal oldBal1 = AmtUtils.processNegativeAmt(subAcct.getBal(),
				BigDecimal.ZERO);

		// 付款方原保留金额
		BigDecimal oldReserveAmt1 = AmtUtils.processNegativeAmt(
				subAcct.getReservedAmt(), BigDecimal.ZERO);

		// 付款方原冻结金额
		BigDecimal oldFreezeAmt1 = AmtUtils.processNegativeAmt(
				subAcct.getFreezableAmt(), BigDecimal.ZERO);

		// 付款方原可用余额=Max(余额-保留-冻结,0)
		BigDecimal oldAvlBal = AmtUtils.max(oldBal1.subtract(oldReserveAmt1)
				.subtract(oldFreezeAmt1), BigDecimal.ZERO);

		// 付款金额
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(payerInfo.getTrxAmt(),
				BigDecimal.ZERO);

		BigDecimal fromXwbAmt = BigDecimal.ZERO;
		BigDecimal netTrxAmt = oldAvlBal.subtract(trxAmt);
		if (netTrxAmt.compareTo(BigDecimal.ZERO) < 0) {
			if (isAddXwbPay) {
				fromXwbAmt = netTrxAmt.abs();
				SubAcctPo xwbSubAcct = this.getSubAcctByAcctNoAndSubAcctNo(
						subAcct.getAcctNo(), ESubAcctNo.XWB.getCode());
				BigDecimal xwbAvlAmt = AcctUtils.getSubAcctAvlBal(xwbSubAcct);
				if (xwbAvlAmt.compareTo(fromXwbAmt) < 0) {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员活期与小微宝子账户可用余额总和不足");
				}
			} else {
				throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员活期子账户余额不足");
			}
		}

		if (fromXwbAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 处理小微宝金额
			InternalAcctTransferInfo interReq = new InternalAcctTransferInfo();
			interReq.setBizId(StringUtils.defaultIfBlank(payerInfo.getBizId(), bizId));
			interReq.setCurrOpId(currOpId);
			interReq.setEventId(eventId);
			interReq.setFromAcctType(ESubAcctType.XWB);
			interReq.setToAcctType(ESubAcctType.CURRENT);
			interReq.setTrxAmt(fromXwbAmt);
			interReq.setTrxMemo(payerInfo.getTrxMemo());
			interReq.setUserId(payerInfo.getUserId());
			interReq.setUseType(EFundUseType.INTERNAL);
			interReq.setWorkDate(workDate);

			this.internalAcctTransferPayerDeal(acctPo, interReq);

			this.internalAcctTransferPayeeDeal(acctPo, cashPool, interReq);
		}

		SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(
				subAcct.getAcctNo(), ESubAcctNo.CURRENT.getCode());

		BigDecimal oldBal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);

		// 新的余额 = Max(原余额 - 付款金额,0)
		BigDecimal newBal = AmtUtils.max(oldBal.subtract(trxAmt),
				BigDecimal.ZERO);

		// 付款方原最大可提现金额
		BigDecimal oldMaxCashAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);
		// 新最大可提现金额
		BigDecimal newMaxCashAbleAmt = BigDecimal.ZERO;
		if (EFundUseType.CASH == payerInfo.getUseType()
				|| EFundUseType.RECHARGE_REVERSE == payerInfo.getUseType()) {
			newMaxCashAbleAmt = AmtUtils.max(oldMaxCashAmt.subtract(trxAmt),
					BigDecimal.ZERO);
		} else {
			// 付款方原非可提现金额 = Max(原可用金额-原最大可提现金额,0)
			BigDecimal oldUnCashAbleAmt = AmtUtils.max(
					oldAvlBal.subtract(oldMaxCashAmt), BigDecimal.ZERO);
			if (trxAmt.compareTo(oldUnCashAbleAmt) > 0) {
				// 付款金额大于非可提现金额
				// 需要从最大可提现金额中划去的金额 = Max(付款金额 - 原非可提现金额,0)
				BigDecimal diffAmt = AmtUtils.max(
						trxAmt.subtract(oldUnCashAbleAmt), BigDecimal.ZERO);

				// 新最大可提现金额 = Max(原最大可提现金额 - 需要从最大可提现金额中划去的金额,0)
				newMaxCashAbleAmt = AmtUtils.max(
						oldMaxCashAmt.subtract(diffAmt), BigDecimal.ZERO);
			} else {
				// 新最大可提现金额 = 原最大可提现金额
				newMaxCashAbleAmt = oldMaxCashAmt;
			}
		}

		// 原可计息余额
		BigDecimal oldIntrBal = AmtUtils.processNegativeAmt(
				subAcctPo.getIntrBal(), BigDecimal.ZERO);

		// 新可计息余额 = Max(原可计息余额 - 付款金额, 0)
		BigDecimal newIntrBal = AmtUtils.max(oldIntrBal.subtract(trxAmt),
				BigDecimal.ZERO);

		Date currDate = new Date();

		// 更新付款人活期子账户相关余额
		SubAcctPo newAmtPayerSubAcctPo = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		newAmtPayerSubAcctPo.setBal(newBal);
		newAmtPayerSubAcctPo.setMaxCashableAmt(newMaxCashAbleAmt);
		newAmtPayerSubAcctPo.setIntrBal(newIntrBal);
		newAmtPayerSubAcctPo.setLastMntOpid(currOpId);
		newAmtPayerSubAcctPo.setLastMntTs(currDate);
		subAcctRepository.save(newAmtPayerSubAcctPo);

		// 记录子账户支付交易日志
		SubAcctTrxJnlPo trxJnlPo = new SubAcctTrxJnlPo();
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setAcctNo(subAcctPo.getAcctNo());
		trxJnlPo.setSubAcctNo(subAcctPo.getSubAcctNo());
		trxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, cashPool));
		trxJnlPo.setPayRecvFlg(EFundPayRecvFlag.PAY);
		trxJnlPo.setTrxDt(workDate);
		trxJnlPo.setTrxAmt(payerInfo.getTrxAmt());
		trxJnlPo.setBal(newBal);
		trxJnlPo.setTrxMemo(payerInfo.getTrxMemo());
		trxJnlPo.setUseType(payerInfo.getUseType());
		trxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payerInfo.getBizId(), bizId));
		trxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		trxJnlPo.setCreateOpid(currOpId);
		trxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(trxJnlPo);
	}

	/**
	 * 获取会员子账户.
	 * 
	 * @param payerInfo
	 * @param subAcctNo
	 * @return
	 * @throws AcctNotExistException
	 * @throws SubAcctNotExistException
	 */
	private SubAcctPo getSubAcctPo(String userId, String subAcctNo)
			throws BizServiceException {

		AcctPo acctPo = this.getAcctByUserId(userId);

		String acctNo = acctPo.getAcctNo();

		// 获取会员的子账户信息
		SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(acctNo,
				subAcctNo);

		return subAcctPo;
	}

	/**
	 * 判断资金划转双方汇总金额是否匹配.
	 * 
	 * @param payerList
	 * @param payeeList
	 * @param isRelZQ
	 * @throws PayerOrPayeeCannotEmptyException
	 * @throws AmtParamInvalidException
	 * @throws AmtRecvPayNotEqualsException
	 */
	private void checkTransferAmt(List<TransferInfo> payerList,
			List<TransferInfo> payeeList) throws BizServiceException {

		// 单边付款或者收款，则不需要判断双方的余额总和是否相等
		if ((payerList == null || payerList.isEmpty())
				&& (payeeList == null || payeeList.isEmpty())) {
			throw new BizServiceException(TECH_DATA_INVALID, "付款方列表和收款方列表不能都为空");
		}

		int both = 0;
		BigDecimal sumPayerAmt = BigDecimal.ZERO;
		if (payerList != null && !payerList.isEmpty()) {
			for (TransferInfo payerInfo : payerList) {
				sumPayerAmt = sumPayerAmt.add(AmtUtils.processNegativeAmt(
						payerInfo.getTrxAmt(), BigDecimal.ZERO));
			}
			both++;
		}

		BigDecimal sumPayeeAmt = BigDecimal.ZERO;
		if (payeeList != null && !payeeList.isEmpty()) {
			for (TransferInfo payeeInfo : payeeList) {
				sumPayeeAmt = sumPayeeAmt.add(AmtUtils.processNegativeAmt(
						payeeInfo.getTrxAmt(), BigDecimal.ZERO));
			}
			both++;
		}

		if (both > 1 && 0 != sumPayerAmt.compareTo(sumPayeeAmt)) {
			throw new BizServiceException(TECH_DATA_INVALID,
					"sum(付款方金额)!=sum(收款方金额)");
		}
	}

	@Override
	public String transferAmtForFinancing(String eventId,
			List<DedicatedTransferInfo> payerList, TransferInfo payeeInfo,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		Date currDate = new Date();

		// 判断交割双方金额
		checkDeliverAmt(payerList, payeeInfo);

		// 付款方融资包交割处理
		payerDeliverProcess(eventId, payerList, bizId, currOpId, workDate,
				currDate);

		// 收款方融资包交割处理 并返回冻结明细流水
		String jnlNo = payeeDeliverProcess(eventId, payeeInfo, bizId, currOpId,
				workDate, currDate);

		return jnlNo;
	}

	/**
	 * 判断交割双方金额.
	 * 
	 * @param payerList
	 * @param payeeInfo
	 * @throws PayerOrPayeeCannotEmptyException
	 * @throws AmtParamInvalidException
	 * @throws JnlNoParamDuplicateException
	 * @throws AmtRecvPayNotEqualsException
	 */
	private void checkDeliverAmt(List<DedicatedTransferInfo> payerList,
			TransferInfo payeeInfo) throws BizServiceException {
		if (null == payerList || payerList.isEmpty()) {
			throw new BizServiceException(TECH_DATA_INVALID, "付款方列表不能为空");
		}

		if (null == payeeInfo) {
			throw new BizServiceException(TECH_DATA_INVALID, "收款方不能为空");
		}

		Set<String> duplicateFnrJnlNoCheck = new HashSet<String>();

		BigDecimal sumAmt = BigDecimal.ZERO;
		for (DedicatedTransferInfo info : payerList) {
			sumAmt = sumAmt.add(AmtUtils.processNegativeAmt(info.getTrxAmt(),
					BigDecimal.ZERO));
			if (StringUtils.isNotEmpty(info.getFnrJnlNo())) {
				if (duplicateFnrJnlNoCheck.contains(info.getFnrJnlNo())) {
					throw new BizServiceException(TECH_DATA_INVALID,
							"付款列表冻结保留明细编号不能重复");
				} else {
					duplicateFnrJnlNoCheck.add(info.getFnrJnlNo());
				}
			}
		}

		if (sumAmt.compareTo(payeeInfo.getTrxAmt()) != 0) {
			throw new BizServiceException(TECH_DATA_INVALID,
					"sum(付款方金额)!=sum(收款方金额)");
		}
	}

	/**
	 * 收款方融资包交割处理.
	 * 
	 * @param eventId
	 * @param payeeInfo
	 * @param useType
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @param currDate
	 * @return
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 * @throws AmtParamInvalidException
	 */
	private String payeeDeliverProcess(String eventId, TransferInfo payeeInfo,
			String bizId, String currOpId, Date workDate, Date currDate)
			throws BizServiceException {
		
		String userId = payeeInfo.getUserId();
		
		AcctPo acctPo = this.getAcctByUserId(payeeInfo.getUserId());

		// 获取收款方会员借款子账户
		SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(
				acctPo.getAcctNo(), ESubAcctNo.LOAN.getCode());

		// 原账户余额
		BigDecimal oldBal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);

		// 收款金额
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(payeeInfo.getTrxAmt(),
				BigDecimal.ZERO);

		// 新账户余额 = 原账户余额 + 收款金额
		BigDecimal newBal = oldBal.add(trxAmt);

		// 原可计息余额
		BigDecimal oldIntrBal = AmtUtils.processNegativeAmt(
				subAcctPo.getIntrBal(), BigDecimal.ZERO);

		// 新可计息余额 = 原可计息余额 + 收款金额
		BigDecimal newIntrBal = oldIntrBal.add(trxAmt);

		// 计算保留金额
		BigDecimal resvAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getReservedAmt(), BigDecimal.ZERO);
		BigDecimal newResvAmt = resvAmt.add(trxAmt);

		// 更新子账户信息
		SubAcctPo targetSubAcctPo = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		targetSubAcctPo.setBal(newBal);
		targetSubAcctPo.setIntrBal(newIntrBal);
		targetSubAcctPo.setReservedAmt(newResvAmt);
		targetSubAcctPo.setLastMntOpid(currOpId);
		targetSubAcctPo.setLastMntTs(currDate);
		subAcctRepository.save(targetSubAcctPo);

		// 记录子账户收款交易日志
		SubAcctTrxJnlPo trxJnlPo = new SubAcctTrxJnlPo();
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setAcctNo(subAcctPo.getAcctNo());
		trxJnlPo.setSubAcctNo(subAcctPo.getSubAcctNo());
		trxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, acctPo.getCashPool()));
		trxJnlPo.setPayRecvFlg(EFundPayRecvFlag.RECV);
		trxJnlPo.setTrxDt(workDate);
		trxJnlPo.setTrxAmt(payeeInfo.getTrxAmt());
		trxJnlPo.setBal(newBal);
		trxJnlPo.setTrxMemo(payeeInfo.getTrxMemo());
		trxJnlPo.setUseType(payeeInfo.getUseType());
		trxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payeeInfo.getBizId(), bizId));
		trxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		trxJnlPo.setCreateOpid(currOpId);
		trxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(trxJnlPo);

		// 添加冻结保留明细日志
		FreezeReserveDtlPo acFnrDtlPo = new FreezeReserveDtlPo();
		acFnrDtlPo.setAcctNo(subAcctPo.getAcctNo());
		acFnrDtlPo.setSubAcctNo(subAcctPo.getSubAcctNo());
		acFnrDtlPo.setUseType(payeeInfo.getUseType());
		acFnrDtlPo.setOperType(EFnrOperType.RESERVE);
		acFnrDtlPo.setStatus(EFnrStatus.ACTIVE);
		acFnrDtlPo.setEffectDt(workDate);
		acFnrDtlPo
				.setExpireDt(DateUtils.getDate(
						DictConsts.DEFAULT_MAX_DATE_VALUE,
						DictConsts.WORK_DATE_FORMAT));
		acFnrDtlPo.setTrxAmt(trxAmt);
		acFnrDtlPo.setTrxMemo(payeeInfo.getTrxMemo());
		acFnrDtlPo.setRelBizId(StringUtils.defaultIfBlank(payeeInfo.getBizId(), bizId));
		acFnrDtlPo.setCreateOpid(currOpId);
		acFnrDtlPo.setCreateTs(currDate);
		FreezeReserveDtlPo retObj = freezeReserveDtlRepository.save(acFnrDtlPo);
		return retObj.getJnlNo();
	}

	/**
	 * 付款方融资包交割处理.
	 * 
	 * @param eventId
	 * @param payerList
	 * @param useType
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws FreezeReserveDtlOperTypeInvalidException
	 * @throws FreezeReserveDtlCloseException
	 * @throws AmtParamInvalidException
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 * @throws AvlBalNotEnoughException
	 */
	private void payerDeliverProcess(String eventId,
			List<DedicatedTransferInfo> payerList, String bizId,
			String currOpId, Date workDate, Date currDate)
			throws BizServiceException {
		Collections.sort(payerList, new ComparatorTransferInfo());
		for (DedicatedTransferInfo fiexdInfo : payerList) {
			
			String userId = fiexdInfo.getUserId();
			// 获取原保留日志
			FreezeReserveDtlPo acFreezeReserveDtlPo = freezeReserveDtlRepository
					.findFreezeReserveDtlByJnlNo(fiexdInfo.getFnrJnlNo());

			BusinessAcctUtils.checkDataPreUnReserve(acFreezeReserveDtlPo);

			AcctPo acctPo = this.getAcctByUserId(fiexdInfo.getUserId());

			// 获取该会员的 活期子账户信息
			SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(
					acctPo.getAcctNo(), ESubAcctNo.CURRENT.getCode());

			// 原保留金额
			BigDecimal oldReserveAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getReservedAmt(), BigDecimal.ZERO);

			// 被保留金额
			BigDecimal reserveAmt = AmtUtils.processNegativeAmt(
					acFreezeReserveDtlPo.getTrxAmt(), BigDecimal.ZERO);

			// 新保留金额 = Max(原保留金额 - 被保留金额 ，0)
			BigDecimal newReserveAmt = AmtUtils.max(
					oldReserveAmt.subtract(reserveAmt), BigDecimal.ZERO);

			// 原账户金额
			BigDecimal oldBal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
					BigDecimal.ZERO);

			// 划转金额
			BigDecimal trxAmt = AmtUtils.processNegativeAmt(
					fiexdInfo.getTrxAmt(), BigDecimal.ZERO);

			// 判断划转金额是否大于账户金额
			if (trxAmt.compareTo(oldBal) > 0) {
				throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户余额不足，资金划转失败");
			}

			// 账户新金额 = Max(原账户金额 - 被保留金额，0）
			BigDecimal newBal = AmtUtils.max(oldBal.subtract(trxAmt),
					BigDecimal.ZERO);

			// 原冻结金额
			BigDecimal oldFreezeAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getFreezableAmt(), BigDecimal.ZERO);

			// 现可用余额=Max(原账户余额-新保留金额-原冻结,0)
			BigDecimal avlBal = AmtUtils.max(oldBal.subtract(newReserveAmt)
					.subtract(oldFreezeAmt), BigDecimal.ZERO);

			// 原最大可提现金额
			BigDecimal oldMaxCashableAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);

			// 非可提现金额 = Max(原可用余额 - 原最大可提现金额,0)
			BigDecimal unCashAbleAmt = AmtUtils.max(
					avlBal.subtract(oldMaxCashableAmt), BigDecimal.ZERO);

			// 新最大可提现金额
			BigDecimal newMaxCashAbleAmt = BigDecimal.ZERO;

			if (unCashAbleAmt.compareTo(trxAmt) >= 0) {
				// 新最大可提现金额 = 原最大可提现金额
				newMaxCashAbleAmt = oldMaxCashableAmt;
			} else {
				// 付款金额大于非可提现金额
				// 需要从最大可提现金额中划去的金额 = Max(付款金额 - 原非可提现金额,0)
				BigDecimal diffAmt = AmtUtils.max(
						trxAmt.subtract(unCashAbleAmt), BigDecimal.ZERO);

				// 新最大可提现金额 = Max(原最大可提现金额 - 需要从最大可提现金额中划去的金额,0)
				newMaxCashAbleAmt = AmtUtils.max(
						oldMaxCashableAmt.subtract(diffAmt), BigDecimal.ZERO);
			}

			// 原计息余额
			BigDecimal oldIntrBal = AmtUtils.processNegativeAmt(
					subAcctPo.getIntrBal(), BigDecimal.ZERO);

			// 新计息余额 = Max(原计息余额 -被保留金额，0 )
			BigDecimal newIntrBal = AmtUtils.max(oldIntrBal.subtract(trxAmt),
					BigDecimal.ZERO);

			// 更新付款人活期子账户相关余额
			SubAcctPo newAmtPayerSubAcctPo = ConverterService.convert(
					subAcctPo, SubAcctPo.class);
			newAmtPayerSubAcctPo.setBal(newBal);
			newAmtPayerSubAcctPo.setReservedAmt(newReserveAmt);
			newAmtPayerSubAcctPo.setMaxCashableAmt(newMaxCashAbleAmt);
			newAmtPayerSubAcctPo.setIntrBal(newIntrBal);
			newAmtPayerSubAcctPo.setLastMntOpid(currOpId);
			newAmtPayerSubAcctPo.setLastMntTs(currDate);
			subAcctRepository.save(newAmtPayerSubAcctPo);

			String resverMemo = StringUtils.defaultIfEmpty(
					acFreezeReserveDtlPo.getTrxMemo(), "");
			// 更新账户冻结保留日志
			FreezeReserveDtlPo targetFreezeReserveDtlPo = ConverterService
					.convert(acFreezeReserveDtlPo, FreezeReserveDtlPo.class);
			targetFreezeReserveDtlPo.setStatus(EFnrStatus.CLOSE);
			targetFreezeReserveDtlPo.setExpireDt(workDate);
			targetFreezeReserveDtlPo.setTrxMemo(AcctUtils.substr(resverMemo
					+ fiexdInfo.getTrxMemo(), MAX_LENGTH_200));
			targetFreezeReserveDtlPo.setLastMntOpid(currOpId);
			targetFreezeReserveDtlPo.setLastMntTs(currDate);
			freezeReserveDtlRepository.save(targetFreezeReserveDtlPo);

			// 记录子账户支付交易日志
			SubAcctTrxJnlPo trxJnlPo = new SubAcctTrxJnlPo();
			trxJnlPo.setEventId(eventId);
			trxJnlPo.setAcctNo(subAcctPo.getAcctNo());
			trxJnlPo.setSubAcctNo(subAcctPo.getSubAcctNo());
			trxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, acctPo.getCashPool()));
			trxJnlPo.setPayRecvFlg(EFundPayRecvFlag.PAY);
			trxJnlPo.setTrxDt(workDate);
			trxJnlPo.setTrxAmt(fiexdInfo.getTrxAmt());
			trxJnlPo.setBal(newBal);
			trxJnlPo.setTrxMemo(fiexdInfo.getTrxMemo());
			trxJnlPo.setUseType(fiexdInfo.getUseType());
			trxJnlPo.setRelBizId(StringUtils.defaultIfBlank(fiexdInfo.getBizId(), bizId));
			trxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
			trxJnlPo.setCreateOpid(currOpId);
			trxJnlPo.setCreateTs(currDate);
			subAcctTrxJnlRepository.save(trxJnlPo);
		}
	}

	@Override
	public void transferAmtFromFnr(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException,
			AvlBalNotEnoughException {

		// 校验付款方总金额 与 收款方总金额是否相等,并返回总金额
		getTransferTotalAmtAndCheckBalance(payerList, payeeList);

		// 收款方处理 
		payeeTransferFromFnr(eventId, payeeList, bizId, currOpId, workDate);

		// 付款方处理
		payerTransferFromFnr(eventId, payerList, isAddXwbPay, bizId, currOpId,
				workDate);
	}

	/**
	 * 校验付款列表与收款列表总金额是否平，且返回交易总金额.
	 * 
	 * @param payerList
	 * @param payeeList
	 * @return
	 * @throws PayerOrPayeeCannotEmptyException
	 * @throws AmtParamInvalidException
	 * @throws JnlNoParamDuplicateException
	 * @throws AmtRecvPayNotEqualsException
	 * @throws ServiceException
	 */
	private BigDecimal getTransferTotalAmtAndCheckBalance(
			List<DedicatedTransferInfo> payerList, List<TransferInfo> payeeList)
			throws BizServiceException {
		
		// 单边付款或者收款，则不需要判断双方的余额总和是否相等
		if ((payerList == null || payerList.isEmpty())
				&& (payeeList == null || payeeList.isEmpty())) {
			throw new BizServiceException(TECH_DATA_INVALID, "付款方列表和收款方列表不能都为空");
		}

		int both = 0;
		BigDecimal payTotalAmt = BigDecimal.ZERO;
		if (payerList != null && !payerList.isEmpty()) {
			
			Set<String> duplicateFnrJnlNoCheck = new HashSet<String>();
			for (DedicatedTransferInfo payer : payerList) {
				BigDecimal trxAmt = AmtUtils.processNegativeAmt(payer.getTrxAmt(), BigDecimal.ZERO);
				payTotalAmt = payTotalAmt.add(trxAmt);
				String jnlNo = payer.getFnrJnlNo();
				if (StringUtils.isNotEmpty(jnlNo)) {
					if (duplicateFnrJnlNoCheck.contains(jnlNo)) {
						throw new BizServiceException(TECH_DATA_INVALID,
								"付款列表冻结明细编号不能重复");
					} else {
						duplicateFnrJnlNoCheck.add(jnlNo);
					}
				}
			}
			
			both++;
		}

		BigDecimal recvTotalAmt = BigDecimal.ZERO;
		if (payeeList != null && !payeeList.isEmpty()) {
			for (TransferInfo payee : payeeList) {
				BigDecimal trxAmt = AmtUtils.processNegativeAmt(payee.getTrxAmt(), BigDecimal.ZERO);
				recvTotalAmt = recvTotalAmt.add(trxAmt);
			}
			both++;
		}

		if (both > 1 && payTotalAmt.compareTo(recvTotalAmt) != 0) {
			throw new BizServiceException(TECH_DATA_INVALID,
					"付款列表总金额与收款列表总金额不相等");
		}
		return payTotalAmt;
	}

	private void payerTransferFromFnr(String eventId,
			List<DedicatedTransferInfo> payerList, boolean isAddXwbPay,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		if(payerList==null||payerList.isEmpty()){
			return;
		}
		Collections.sort(payerList, new ComparatorTransferInfo());
		for (DedicatedTransferInfo payerInfo : payerList) {
			payerTransferFromFnr(eventId, payerInfo, isAddXwbPay, bizId,
					currOpId, workDate);
		}
	}

	private void payeeTransferFromFnr(String eventId,
			List<TransferInfo> payeeList, String bizId, String currOpId,
			Date workDate) throws BizServiceException {
		if(payeeList==null||payeeList.isEmpty()){
			return;
		}
		Collections.sort(payeeList, new ComparatorTransferInfo());
		for (TransferInfo payeeInfo : payeeList) {
			this.payeeTransfer(eventId, payeeInfo, bizId, currOpId, workDate);
		}
	}

	private void payerTransferFromFnr(String eventId,
			DedicatedTransferInfo payerInfo, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException,
			AvlBalNotEnoughException {
		String fnrJnlNo = payerInfo.getFnrJnlNo();
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(payerInfo.getTrxAmt(),
				BigDecimal.ZERO);
		String trxMemo = payerInfo.getTrxMemo();
		String userId = payerInfo.getUserId();

		FreezeReserveDtlPo freezeDtlPo = freezeReserveDtlRepository
				.findFreezeReserveDtlByJnlNo(fnrJnlNo);

		// 检查明细状态是否被关闭
		BusinessAcctUtils.checkDataPreUnFnr(freezeDtlPo);

		boolean isFreeze = BusinessAcctUtils.isFreezeDtl(freezeDtlPo
				.getOperType());

		if (LOG.isDebugEnabled()) {
			LOG.debug("更新冻结保留明细");
			LOG.debug("将冻结保留明细解冻或者解保留");
		}
		Date currDate = new Date();
		String fnrTrxMemo = freezeDtlPo.getTrxMemo();
		FreezeReserveDtlPo unfnrDtlPo = ConverterService.convert(freezeDtlPo,
				FreezeReserveDtlPo.class);
		unfnrDtlPo.setExpireDt(workDate);
		unfnrDtlPo.setStatus(EFnrStatus.CLOSE);
		unfnrDtlPo
				.setTrxMemo(AcctUtils.substr(fnrTrxMemo + " " + trxMemo, MAX_LENGTH_200));
		unfnrDtlPo.setLastMntOpid(currOpId);
		unfnrDtlPo.setLastMntTs(currDate);
		freezeReserveDtlRepository.save(unfnrDtlPo);

		BigDecimal toBalAmt = BigDecimal.ZERO;
		BigDecimal fromBalAmt = BigDecimal.ZERO;
		BigDecimal fnrAmt = AmtUtils.processNegativeAmt(
				freezeDtlPo.getTrxAmt(), BigDecimal.ZERO);
		
		//冻结保留生效日期
		Date fnrEffectDate = unfnrDtlPo.getEffectDt();

		// 判断交易金额 与 冻结保留明细上的金额是否相等
		if (fnrAmt.compareTo(trxAmt) > 0) {
			toBalAmt = fnrAmt.subtract(trxAmt);
		} else if (fnrAmt.compareTo(trxAmt) < 0) {
			fromBalAmt = trxAmt.subtract(fnrAmt);
		}

		AcctPo acctPo = this.getAcctByUserId(payerInfo.getUserId());

		// 获取冻结保留明细上子账户编号
		String subAcctNo = freezeDtlPo.getSubAcctNo();

		// 通过子账户编号获取子账户信息
		SubAcctPo subAcct = this.getSubAcctByAcctNoAndSubAcctNo(
				acctPo.getAcctNo(), subAcctNo);

		BigDecimal oldBalAmt1 = AmtUtils.processNegativeAmt(subAcct.getBal(),
				BigDecimal.ZERO);
		BigDecimal oldReserveAmt1 = AmtUtils.processNegativeAmt(
				subAcct.getReservedAmt(), BigDecimal.ZERO);

		BigDecimal fromXwbAmt = BigDecimal.ZERO;
		// 原余额 < 划转金额，则提示余额不足
		if (isFreeze) {
			// 如果要划转冻结金额，则 要确保 账户上 (余额-保留金额) >= 要划转的冻结金额
			BigDecimal netAmt = AmtUtils.processNegativeAmt(
					oldBalAmt1.subtract(oldReserveAmt1), BigDecimal.ZERO);
			BigDecimal netTrxAmt = netAmt.subtract(trxAmt);
			if (netTrxAmt.compareTo(BigDecimal.ZERO) < 0) {
				if (isAddXwbPay) {
					fromXwbAmt = netTrxAmt.abs();
					SubAcctPo xwbSubAcct = this.getSubAcctByAcctNoAndSubAcctNo(
							subAcct.getAcctNo(), ESubAcctNo.XWB.getCode());
					BigDecimal xwbAvlAmt = AcctUtils
							.getSubAcctAvlBal(xwbSubAcct);
					if (xwbAvlAmt.compareTo(fromXwbAmt) < 0) {
						throw new AvlBalNotEnoughException(acctPo.getAcctNo(), 
								"会员活期与小微宝子账户可用余额总和不足");
					}
				} else {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员编号为" + userId
							+ "的子账户" + subAcctNo + "上要划转冻结金额的余额不足");
				}
			}
		} else {
			// 如果要划转保留金额，则要 余额>=保留金额
			BigDecimal netAmt = oldBalAmt1.subtract(trxAmt);
			if (netAmt.compareTo(BigDecimal.ZERO) < 0) {
				if (isAddXwbPay) {
					fromXwbAmt = netAmt.abs();
					SubAcctPo xwbSubAcct = this.getSubAcctByAcctNoAndSubAcctNo(
							subAcct.getAcctNo(), ESubAcctNo.XWB.getCode());
					BigDecimal xwbAvlAmt = AcctUtils
							.getSubAcctAvlBal(xwbSubAcct);
					if (xwbAvlAmt.compareTo(fromXwbAmt) < 0) {
						throw new AvlBalNotEnoughException(acctPo.getAcctNo(), 
								"会员活期与小微宝子账户可用余额总和不足");
					}
				} else {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员编号为" + userId
							+ "的子账户" + subAcctNo + "上要划转保留金额的余额不足");
				}
			}

			// 新的保留金额 = 原保留金额 - 明细保留金额
			BigDecimal newResvAmt = AmtUtils.processNegativeAmt(
					oldReserveAmt1.subtract(fnrAmt), BigDecimal.ZERO);
			// 忽略冻结金额的可用余额 = 原余额 - 新保留
			BigDecimal netAmt2 = AmtUtils.processNegativeAmt(
					oldBalAmt1.subtract(newResvAmt), BigDecimal.ZERO);
			// 差额 = 忽略冻结金额的可用余额 - 交易金额
			BigDecimal netTrxAmt2 = netAmt2.subtract(trxAmt);

			if (netTrxAmt2.compareTo(BigDecimal.ZERO) < 0) {
				if (isAddXwbPay) {
					fromXwbAmt = netTrxAmt2.abs();
					SubAcctPo xwbSubAcct = this.getSubAcctByAcctNoAndSubAcctNo(
							subAcct.getAcctNo(), ESubAcctNo.XWB.getCode());
					BigDecimal xwbAvlAmt = AcctUtils
							.getSubAcctAvlBal(xwbSubAcct);
					if (xwbAvlAmt.compareTo(fromXwbAmt) < 0) {
						throw new AvlBalNotEnoughException(acctPo.getAcctNo(), 
								"会员活期与小微宝子账户可用余额总和不足");
					}
				} else {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员编号为" + userId
							+ "的子账户" + subAcctNo + "上要划转保留金额的余额不足");
				}
			}
		}

		if (fromXwbAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 处理小微宝金额
			InternalAcctTransferInfo interReq = new InternalAcctTransferInfo();
			interReq.setBizId(StringUtils.defaultIfBlank(payerInfo.getBizId(), bizId));
			interReq.setCurrOpId(currOpId);
			interReq.setEventId(eventId);
			interReq.setFromAcctType(ESubAcctType.XWB);
			interReq.setToAcctType(ESubAcctType.CURRENT);
			interReq.setTrxAmt(fromXwbAmt);
			interReq.setTrxMemo(payerInfo.getTrxMemo());
			interReq.setUserId(payerInfo.getUserId());
			interReq.setUseType(EFundUseType.INTERNAL);
			interReq.setWorkDate(workDate);
			this.internalAcctTransferPayerDeal(acctPo, interReq);
			this.internalAcctTransferPayeeDeal(acctPo, acctPo.getCashPool(), interReq);
		}

		SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(
				subAcct.getAcctNo(), subAcctNo);

		BigDecimal oldBalAmt = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);
		BigDecimal oldIntrAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getIntrBal(), BigDecimal.ZERO);
		BigDecimal oldFreezeAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getFreezableAmt(), BigDecimal.ZERO);
		BigDecimal oldReserveAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getReservedAmt(), BigDecimal.ZERO);
		BigDecimal oldMaxCashableAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);

		if (LOG.isDebugEnabled()) {
			LOG.debug("会员编号为" + userId);
			LOG.debug("子账户编号为" + subAcctNo);
			LOG.debug("原余额为" + oldBalAmt.doubleValue());
			LOG.debug("原计息金额为" + oldIntrAmt.doubleValue());
			LOG.debug("原冻结金额为" + oldFreezeAmt.doubleValue());
			LOG.debug("原保留金额为" + oldReserveAmt.doubleValue());
			LOG.debug("原最大可提现金额为" + oldMaxCashableAmt.doubleValue());
			LOG.debug("冻结保留明细上金额为" + fnrAmt.doubleValue());
			LOG.debug("此次待划转的金额为" + trxAmt.doubleValue());
			LOG.debug("fromBalAmt为" + fromBalAmt.doubleValue());
			LOG.debug("toBalAmt为" + toBalAmt.doubleValue());
			LOG.debug("交易备注为" + trxMemo);
		}

		// 新余额 = Max(原余额-划转金额,0)
		BigDecimal newBalAmt = AmtUtils.processNegativeAmt(
				oldBalAmt.subtract(trxAmt), BigDecimal.ZERO);
		// 新计息金额 = Max(原计息金额-划转金额,0)
		BigDecimal newIntrAmt = AmtUtils.processNegativeAmt(
				oldIntrAmt.subtract(trxAmt), BigDecimal.ZERO);
		BigDecimal newFreezeAmt = BigDecimal.ZERO;
		BigDecimal newReserveAmt = BigDecimal.ZERO;
		// 若为冻结 则 扣应冻结金额，若为保留，则扣保留金额
		if (isFreeze) {
			newFreezeAmt = AmtUtils.processNegativeAmt(
					oldFreezeAmt.subtract(fnrAmt), BigDecimal.ZERO);
			newReserveAmt = oldReserveAmt;
		} else {
			newFreezeAmt = oldFreezeAmt;
			newReserveAmt = AmtUtils.processNegativeAmt(
					oldReserveAmt.subtract(fnrAmt), BigDecimal.ZERO);
		}

		// 最大可提现金额计算 , 优先使用非可提现金额
		BigDecimal newMaxCashableAmt = BigDecimal.ZERO;
		// 原可用余额
		BigDecimal avlAmt = AmtUtils.max(oldBalAmt.subtract(oldFreezeAmt)
				.subtract(oldReserveAmt), BigDecimal.ZERO);
		
		if(EFundUseType.CASH == payerInfo.getUseType() 
				|| EFundUseType.RECHARGE_REVERSE == payerInfo.getUseType()){
			// 如果保留明细产生的数据为当日，则扣减最大可提现金额，否则不变
			if(DateUtils.isGreaterAndEqualsyyyyMMdd(fnrEffectDate, workDate)){
				newMaxCashableAmt = AmtUtils.max(
						oldMaxCashableAmt.subtract(trxAmt), BigDecimal.ZERO);
			}else{
				newMaxCashableAmt = oldMaxCashableAmt;
			}
		}else{
			// 非可提现金额
			BigDecimal unCashableAmt = AmtUtils.max(
					avlAmt.subtract(oldMaxCashableAmt), BigDecimal.ZERO);
	
			if (unCashableAmt.compareTo(trxAmt) >= 0) {
				newMaxCashableAmt = oldMaxCashableAmt;
			} else {
				BigDecimal netAmt = trxAmt.subtract(unCashableAmt);
				newMaxCashableAmt = AmtUtils.max(
						oldMaxCashableAmt.subtract(netAmt), BigDecimal.ZERO);
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("更新子账户金额");
			LOG.debug("会员编号为" + userId);
			LOG.debug("子账户编号为" + subAcctNo);
			LOG.debug("新余额为" + newBalAmt.doubleValue());
			LOG.debug("新计息金额为" + newIntrAmt.doubleValue());
			LOG.debug("新冻结金额为" + newFreezeAmt.doubleValue());
			LOG.debug("新保留金额为" + newReserveAmt.doubleValue());
			LOG.debug("新最大可提现金额为" + newMaxCashableAmt.doubleValue());
		}
		// 更新付款会员金额
		SubAcctPo subAcctNewAmt = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		subAcctNewAmt.setBal(newBalAmt);
		subAcctNewAmt.setFreezableAmt(newFreezeAmt);
		subAcctNewAmt.setIntrBal(newIntrAmt);
		subAcctNewAmt.setMaxCashableAmt(newMaxCashableAmt);
		subAcctNewAmt.setReservedAmt(newReserveAmt);
		subAcctNewAmt.setLastMntOpid(currOpId);
		subAcctNewAmt.setLastMntTs(currDate);
		subAcctRepository.save(subAcctNewAmt);

		if (LOG.isDebugEnabled()) {
			LOG.debug("记录付款交易日志");
		}
		// 记录子账户支付交易日志
		SubAcctTrxJnlPo trxJnlPo = new SubAcctTrxJnlPo();
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setAcctNo(subAcctPo.getAcctNo());
		trxJnlPo.setSubAcctNo(subAcctNo);
		trxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, acctPo.getCashPool()));
		trxJnlPo.setPayRecvFlg(EFundPayRecvFlag.PAY);
		trxJnlPo.setTrxDt(workDate);
		trxJnlPo.setTrxAmt(trxAmt);
		trxJnlPo.setBal(newBalAmt);
		trxJnlPo.setTrxMemo(trxMemo);
		trxJnlPo.setUseType(payerInfo.getUseType());
		trxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payerInfo.getBizId(), bizId));
		trxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		trxJnlPo.setCreateOpid(currOpId);
		trxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(trxJnlPo);
	}

	@Override
	public BigDecimal getUserSubAcctAvlAmt(String userId,
			ESubAcctType subAcctType) throws BizServiceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("获取可用余额会员编号为:" + userId);
		}

		String subAcctNo = BusinessAcctUtils
				.getSubAcctNoBySubAcctType(subAcctType.getCode());

		BigDecimal avlAmt = subAcctRepository.getUserSubAcctAvlAmt(userId,
				subAcctNo);

		avlAmt = AmtUtils.processNullAmt(avlAmt, BigDecimal.ZERO);

		avlAmt = AmtUtils.max(avlAmt, BigDecimal.ZERO);

		if (LOG.isDebugEnabled()) {
			LOG.debug("可用余额为:" + avlAmt.doubleValue());
		}

		return avlAmt;
	}

	@Override
	public BigDecimal getSubAcctCashableAmt(String acctNo, ESubAcctNo subAcctNo)
			throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("获取会员可提现金额综合账户编号为:" + acctNo);
		}

		// 可提现金额
		BigDecimal cashAbleAmt = BigDecimal.ZERO;

		// 获取该会员的 活期子账户信息
		SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acctNo,
				ESubAcctNo.CURRENT.getCode());

		// 冻结金额
		BigDecimal freezeableAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getFreezableAmt(), BigDecimal.ZERO);

		// 保留金额
		BigDecimal reservedAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getReservedAmt(), BigDecimal.ZERO);

		// 账户余额
		BigDecimal bal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);

		// 可用余额 = max(账户余额额-保留金额-冻结金额,0)
		BigDecimal avlAmt = AmtUtils.max(
				bal.subtract(reservedAmt).subtract(freezeableAmt),
				BigDecimal.ZERO);

		// 可提现金额 = MIN(最大可提现金额， 可用余额)
		cashAbleAmt = AmtUtils.min(subAcctPo.getMaxCashableAmt(), avlAmt);

		return cashAbleAmt;
	}

	@Override
	public String specificReserveAmt(ReserveReq req)
			throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("特定场景下的金额保留");
			LOG.debug("会员编号:" + req.getUserId());
			LOG.debug("资金用途:" + req.getUseType());
			LOG.debug("保留金额:" + req.getTrxAmt());
			LOG.debug("备 注:" + req.getTrxMemo());
			LOG.debug("关联业务编号编号:" + req.getBizId());
			LOG.debug("当前操作用户编号:" + req.getCurrOpId());
			LOG.debug("当前工作日期:" + req.getWorkDate());
			LOG.debug("是否使用小微宝余额:" + req.isAddXwb());
		}

		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		// 获取该会员的 活期子账户信息
		SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(
				acctPo.getAcctNo(), ESubAcctNo.CURRENT.getCode());

		// 账户余额
		BigDecimal bal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);
		// 原保留金额
		BigDecimal oldReservedAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getReservedAmt(), BigDecimal.ZERO);
		// 被保留金额
		BigDecimal reservedAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
				BigDecimal.ZERO);
		// 差额
		BigDecimal currAvlAmt = AmtUtils.processNegativeAmt(
				bal.subtract(oldReservedAmt), BigDecimal.ZERO);
		// 判断可用余额是否大于被保留金额
		int result = currAvlAmt.compareTo(reservedAmt);

		if (result < 0) {

			if (!req.isAddXwb()) {
				throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户可用余额不足");
			}
			// 差额
			BigDecimal netAmt = reservedAmt.subtract(currAvlAmt);

			SubAcctPo xwbSubAcct = getSubAcctByAcctNoAndSubAcctNo(
					acctPo.getAcctNo(), ESubAcctNo.XWB.getCode());
			// 账户余额
			BigDecimal xwbBal = AmtUtils.processNegativeAmt(
					xwbSubAcct.getBal(), BigDecimal.ZERO);
			// 原保留金额
			BigDecimal oldXwbReservedAmt = AmtUtils.processNegativeAmt(
					xwbSubAcct.getReservedAmt(), BigDecimal.ZERO);
			// 原冻结金额
			BigDecimal oldXwbFreezableAmt = AmtUtils.processNegativeAmt(
					xwbSubAcct.getFreezableAmt(), BigDecimal.ZERO);
			BigDecimal xwbAvlAmt = AmtUtils.max(
					xwbBal.subtract(oldXwbReservedAmt).subtract(
							oldXwbFreezableAmt), BigDecimal.ZERO);
			int cmp = xwbAvlAmt.compareTo(netAmt);
			if (cmp < 0) {
				throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户可用余额不足");
			}
			// 处理小微宝金额
			InternalAcctTransferInfo interReq = new InternalAcctTransferInfo();
			interReq.setBizId(req.getBizId());
			interReq.setCurrOpId(req.getCurrOpId());
			interReq.setEventId(req.getBizId());
			interReq.setFromAcctType(ESubAcctType.XWB);
			interReq.setToAcctType(ESubAcctType.CURRENT);
			interReq.setTrxAmt(netAmt);
			interReq.setTrxMemo(req.getTrxMemo());
			interReq.setUserId(req.getUserId());
			interReq.setUseType(EFundUseType.INTERNAL);
			interReq.setWorkDate(req.getWorkDate());
			this.internalAcctTransferPayerDeal(acctPo, interReq);
			this.internalAcctTransferPayeeDeal(acctPo, acctPo.getCashPool(), interReq);

			// 获取划转之后，活期子账户信息
			subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acctPo.getAcctNo(),
					ESubAcctNo.CURRENT.getCode());
			// 账户余额
			bal = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
					BigDecimal.ZERO);
			// 原保留金额
			oldReservedAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getReservedAmt(), BigDecimal.ZERO);

			// 可用余额 = 账户余额 - 保留金额 - 冻结金额 。 此处不与0.00做大值处理
			/**
			 * Dead store to local variable. 
			 * 
			 * currAvlAmt = AmtUtils.max(bal.subtract(oldReservedAmt),
					BigDecimal.ZERO); 
			 */
		}

		// currentDate
		Date currDate = new Date();

		// 新保留金额 = 原保留金额 + 保留金额
		BigDecimal newReserveAmt = oldReservedAmt.add(reservedAmt);

		// 更新会员活期账户
		SubAcctPo targetAcSubAcctPo = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		targetAcSubAcctPo.setReservedAmt(newReserveAmt);
		targetAcSubAcctPo.setLastMntOpid(req.getCurrOpId());
		targetAcSubAcctPo.setLastMntTs(currDate);
		subAcctRepository.save(targetAcSubAcctPo);

		// 添加账户冻结保留日志
		FreezeReserveDtlPo acFreezeReserveDtlPo = new FreezeReserveDtlPo();
		acFreezeReserveDtlPo.setAcctNo(subAcctPo.getAcctNo());
		acFreezeReserveDtlPo.setSubAcctNo(subAcctPo.getSubAcctNo());
		acFreezeReserveDtlPo.setOperType(EFnrOperType.RESERVE);
		acFreezeReserveDtlPo.setStatus(EFnrStatus.ACTIVE);
		acFreezeReserveDtlPo.setEffectDt(req.getWorkDate());
		acFreezeReserveDtlPo
				.setExpireDt(DateUtils.getDate(
						DictConsts.DEFAULT_MAX_DATE_VALUE,
						DictConsts.WORK_DATE_FORMAT));
		acFreezeReserveDtlPo.setUseType(req.getUseType());
		acFreezeReserveDtlPo.setTrxAmt(req.getTrxAmt());
		acFreezeReserveDtlPo.setTrxMemo(req.getTrxMemo());
		acFreezeReserveDtlPo.setRelBizId(req.getBizId());
		acFreezeReserveDtlPo.setCreateOpid(req.getCurrOpId());
		acFreezeReserveDtlPo.setCreateTs(currDate);
		FreezeReserveDtlPo retAcFreezeReserveDtlPo = freezeReserveDtlRepository
				.save(acFreezeReserveDtlPo);
		return retAcFreezeReserveDtlPo.getJnlNo();
	}

	private void updateAcctStatusAndChangeFrzCt(AcctPo acctPo,
			final Long changeFrzCt, String currOpid) {
		Long frzCount = AmtUtils
				.processLong(acctPo.getFrzCt(), Long.valueOf(0));
		frzCount += changeFrzCt;
		String acctStatus = EAcctStatus.RECNOPAY.getCode();
		if (AmtUtils.max(frzCount, Long.valueOf(0)) == 0) {
			acctStatus = EAcctStatus.NORMAL.getCode();
			frzCount = Long.valueOf(0);
		}
		AcctPo newAcctPo = ConverterService.convert(acctPo, AcctPo.class);
		newAcctPo.setAcctStatus(acctStatus);
		newAcctPo.setFrzCt(frzCount);
		newAcctPo.setLastMntOpid(currOpid);
		newAcctPo.setLastMntTs(new Date());
		acctRepository.save(newAcctPo);
	}

	@Override
	public String freezeAcct(FreezeReq req, boolean isBizFreeze)
			throws BizServiceException {
		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		this.updateAcctStatusAndChangeFrzCt(acctPo, INCREASE_COUNT,
				req.getCurrOpId());
		EFnrOperType operType = EFnrOperType.FREEZE_MGT_NOPAY;
		req.setUseType(EFundUseType.MGTFREEZEFUNDACCT);
		if (isBizFreeze) {
			operType = EFnrOperType.FREEZE_BIZ_NOPAY;
			req.setUseType(EFundUseType.BIZFREEZEFUNDACCT);
		}
		// 资金账户冻结，冻结金额为0.00
		req.setTrxAmt(BigDecimal.ZERO);
		return freezeAmt(req, acctPo.getAcctNo(), operType);
	}

	@Override
	public BigDecimal unFreezeAcct(UnFreezeReq req, boolean isBizFreeze)
			throws BizServiceException {
		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getAcctByUserId(req.getUserId());
		this.updateAcctStatusAndChangeFrzCt(acctPo, DECREASE_COUNT,
				req.getCurrOpId());
		return unFreezeAmt(req, acctPo);
	}

	@Override
	public void transferAmtIgnoreFrzAmt(String eventId,
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			boolean isAddXwbPay, String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		// check pay receive amt trial balance
		this.checkTransferAmtIgnoreFrzAmt(payerList, payeeList);

		// recv
		this.payeeTransferAmtIgnoreFrzAmt(eventId, payeeList, bizId, currOpId,
				workDate);

		// pay
		this.payerTransferAmtIgnoreFrzAmt(eventId, payerList, isAddXwbPay,
				bizId, currOpId, workDate);
	}

	private void payerTransferAmtIgnoreFrzAmt(String eventId,
			List<TransferInfo> payerList, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException,
			AvlBalNotEnoughException {
		if (payerList == null || payerList.isEmpty()) {
			return;
		}
		Collections.sort(payerList, new ComparatorTransferInfo());
		for (TransferInfo payerInfo : payerList) {
			this.payerTransferAmtIgnoreFrzAmt(eventId, payerInfo, isAddXwbPay,
					bizId, currOpId, workDate);
		}
	}

	private void payerTransferAmtIgnoreFrzAmt(String eventId,
			TransferInfo payerInfo, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException,
			AvlBalNotEnoughException {
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(payerInfo.getTrxAmt(),
				BigDecimal.ZERO);
		if (trxAmt.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		String trxMemo = payerInfo.getTrxMemo();
		String userId = payerInfo.getUserId();
		Date currDate = new Date();

		AcctPo acctPo = this.getAcctByUserId(userId);

		// 通过子账户编号获取子账户信息
		SubAcctPo subAcct = this.getSubAcctByAcctNoAndSubAcctNo(
				acctPo.getAcctNo(), ESubAcctNo.CURRENT.getCode());

		BigDecimal oldBalAmt1 = AmtUtils.processNegativeAmt(subAcct.getBal(),
				BigDecimal.ZERO);
		BigDecimal oldReserveAmt1 = AmtUtils.processNegativeAmt(
				subAcct.getReservedAmt(), BigDecimal.ZERO);

		BigDecimal fromXwbAmt = BigDecimal.ZERO;

		// 可用余额 = 余额 - 保留金额
		BigDecimal avlAmt = AmtUtils.processNegativeAmt(
				oldBalAmt1.subtract(oldReserveAmt1), BigDecimal.ZERO);
		BigDecimal netTrxAmt = avlAmt.subtract(trxAmt);
		if (netTrxAmt.compareTo(BigDecimal.ZERO) < 0) {
			if (isAddXwbPay) {
				fromXwbAmt = netTrxAmt.abs();
				SubAcctPo xwbSubAcct = this.getSubAcctByAcctNoAndSubAcctNo(
						subAcct.getAcctNo(), ESubAcctNo.XWB.getCode());
				BigDecimal xwbAvlAmt = AcctUtils.getSubAcctAvlBal(xwbSubAcct);
				if (xwbAvlAmt.compareTo(fromXwbAmt) < 0) {
					throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员活期与小微宝子账户可用余额总和不足");
				}
			} else {
				throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "会员活期子账户余额不足");
			}
		}

		if (fromXwbAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 处理小微宝金额
			InternalAcctTransferInfo interReq = new InternalAcctTransferInfo();
			interReq.setBizId(StringUtils.defaultIfBlank(payerInfo.getBizId(), bizId));
			interReq.setCurrOpId(currOpId);
			interReq.setEventId(eventId);
			interReq.setFromAcctType(ESubAcctType.XWB);
			interReq.setToAcctType(ESubAcctType.CURRENT);
			interReq.setTrxAmt(fromXwbAmt);
			interReq.setTrxMemo(trxMemo);
			interReq.setUserId(userId);
			interReq.setUseType(EFundUseType.INTERNAL);
			interReq.setWorkDate(workDate);

			this.internalAcctTransferPayerDeal(acctPo, interReq);

			this.internalAcctTransferPayeeDeal(acctPo, acctPo.getCashPool(), interReq);
		}

		SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(
				subAcct.getAcctNo(), ESubAcctNo.CURRENT.getCode());

		BigDecimal oldBalAmt = AmtUtils.processNegativeAmt(subAcctPo.getBal(),
				BigDecimal.ZERO);
		BigDecimal oldReserveAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getReservedAmt(), BigDecimal.ZERO);
		BigDecimal oldIntrAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getIntrBal(), BigDecimal.ZERO);
		BigDecimal oldFreezeAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getFreezableAmt(), BigDecimal.ZERO);
		BigDecimal oldMaxCashableAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);
		// 新余额 = Max(原余额-划转金额,0)
		BigDecimal newBalAmt = AmtUtils.processNegativeAmt(
				oldBalAmt.subtract(trxAmt), BigDecimal.ZERO);
		// 新计息金额 = Max(原计息金额-划转金额,0)
		BigDecimal newIntrAmt = AmtUtils.max(oldIntrAmt.subtract(trxAmt),
				BigDecimal.ZERO);

		// 最大可提现金额计算 , 优先使用非可提现金额
		BigDecimal newMaxCashableAmt = BigDecimal.ZERO;
		// 原可用余额
		BigDecimal oldAvlAmt = AmtUtils.max(oldBalAmt.subtract(oldFreezeAmt)
				.subtract(oldReserveAmt), BigDecimal.ZERO);
		// 非可提现金额
		BigDecimal unCashableAmt = AmtUtils.max(
				oldAvlAmt.subtract(oldMaxCashableAmt), BigDecimal.ZERO);
		if (unCashableAmt.compareTo(trxAmt) >= 0) {
			newMaxCashableAmt = oldMaxCashableAmt;
		} else {
			BigDecimal netAmt = trxAmt.subtract(unCashableAmt);
			newMaxCashableAmt = AmtUtils.max(
					oldMaxCashableAmt.subtract(netAmt), BigDecimal.ZERO);
		}

		// 更新付款会员金额
		SubAcctPo subAcctNewAmt = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		subAcctNewAmt.setBal(newBalAmt);
		subAcctNewAmt.setIntrBal(newIntrAmt);
		subAcctNewAmt.setMaxCashableAmt(newMaxCashableAmt);
		subAcctNewAmt.setLastMntOpid(currOpId);
		subAcctNewAmt.setLastMntTs(currDate);
		subAcctRepository.save(subAcctNewAmt);

		if (LOG.isDebugEnabled()) {
			LOG.debug("记录付款交易日志");
		}
		// 记录子账户支付交易日志
		SubAcctTrxJnlPo trxJnlPo = new SubAcctTrxJnlPo();
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setAcctNo(subAcctPo.getAcctNo());
		trxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, acctPo.getCashPool()));
		trxJnlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		trxJnlPo.setPayRecvFlg(EFundPayRecvFlag.PAY);
		trxJnlPo.setTrxDt(workDate);
		trxJnlPo.setTrxAmt(trxAmt);
		trxJnlPo.setBal(newBalAmt);
		trxJnlPo.setTrxMemo(trxMemo);
		trxJnlPo.setUseType(payerInfo.getUseType());
		trxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payerInfo.getBizId(), bizId));
		trxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		trxJnlPo.setCreateOpid(currOpId);
		trxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(trxJnlPo);
	}

	private void payeeTransferAmtIgnoreFrzAmt(String eventId,
			List<TransferInfo> payeeList, String bizId, String currOpId,
			Date workDate) throws BizServiceException {
		if (payeeList == null || payeeList.isEmpty()) {
			return;
		}
		Collections.sort(payeeList, new ComparatorTransferInfo());
		for (TransferInfo payeeInfo : payeeList) {
			this.payeeTransfer(eventId, payeeInfo, bizId, currOpId, workDate);
		}
	}

	private void payeeTransfer(String eventId, TransferInfo payeeInfo,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(payeeInfo.getTrxAmt(),
				BigDecimal.ZERO);
		String trxMemo = payeeInfo.getTrxMemo();
		String userId = payeeInfo.getUserId();
		EFundUseType useType = payeeInfo.getUseType();
		boolean isRelZQ = payeeInfo.isRelZQ();
		Date currDate = new Date();

		if (LOG.isDebugEnabled()) {
			LOG.debug("会员收款");
			LOG.debug("userId" + userId);
			LOG.debug("subAcctNo" + ESubAcctNo.CURRENT.getCode());
			LOG.debug("trxAmt" + trxAmt.doubleValue());
			LOG.debug("trxMemo" + trxMemo);
		}

		AcctPo acctPo = this.getAcctByUserId(userId);

		String cashPool = acctPo.getCashPool();
		
		String pool = AcctUtils.getValidCashPool(payeeInfo.getCashPool());
		
		if(StringUtils.isNotBlank(pool)){
			cashPool = pool;
		}
		
		// 通过子账户编号获取子账户信息
		SubAcctPo subAcctPo = this.getSubAcctByAcctNoAndSubAcctNo(
				acctPo.getAcctNo(), ESubAcctNo.CURRENT.getCode());

		if (LOG.isDebugEnabled()) {
			LOG.debug("记录收款交易日志");
		}
		// 记录子账户支付交易日志
		SubAcctTrxJnlPo subAcctTrxJnlPo = new SubAcctTrxJnlPo();
		subAcctTrxJnlPo.setEventId(eventId);
		subAcctTrxJnlPo.setAcctNo(subAcctPo.getAcctNo());
		subAcctTrxJnlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		subAcctTrxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, cashPool));
		subAcctTrxJnlPo.setPayRecvFlg(EFundPayRecvFlag.RECV);
		subAcctTrxJnlPo.setTrxDt(workDate);
		subAcctTrxJnlPo.setTrxAmt(trxAmt);
		subAcctTrxJnlPo.setBal(subAcctPo.getBal().add(trxAmt));
		subAcctTrxJnlPo.setTrxMemo(trxMemo);
		subAcctTrxJnlPo.setUseType(payeeInfo.getUseType());
		subAcctTrxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payeeInfo.getBizId(), bizId));
		subAcctTrxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		subAcctTrxJnlPo.setCreateOpid(currOpId);
		subAcctTrxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(subAcctTrxJnlPo);

		SubAcctPo pledgeAcctPo = null;
		BigDecimal toPledgeAmt = BigDecimal.ZERO;
		if (isRelZQ) {
			// 获取收款方会员质押子账户
			pledgeAcctPo = getSubAcctPo(payeeInfo.getUserId(),
					ESubAcctNo.PLEDGE.getCode());
			// 质押账户需要划转的金额
			toPledgeAmt = this.calToPledgeAmt(pledgeAcctPo, trxAmt);
		}

		BigDecimal toCurrAmt = AmtUtils.max(trxAmt.subtract(toPledgeAmt),
				BigDecimal.ZERO);

		if (toCurrAmt.compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal oldBalAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getBal(), BigDecimal.ZERO);
			BigDecimal oldIntrAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getIntrBal(), BigDecimal.ZERO);

			// 计算收款子账户相关金额
			BigDecimal newBalAmt = oldBalAmt.add(toCurrAmt);
			BigDecimal newIntrAmt = oldIntrAmt.add(toCurrAmt);

			// 原最大可提现金额
			BigDecimal oldMaxCashAmt = AmtUtils.processNegativeAmt(
					subAcctPo.getMaxCashableAmt(), BigDecimal.ZERO);

			BigDecimal newMaxCashAmt = BigDecimal.ZERO;
			// 充值 或者 提现冲正
			if (EFundUseType.RECHARGE == useType
					|| EFundUseType.CASH_REVERSE == useType) {
				// 新最大可提现金额 = 原最大可提现金额 + 充值金额
				newMaxCashAmt = oldMaxCashAmt.add(toCurrAmt);
			} else {
				newMaxCashAmt = oldMaxCashAmt;
			}

			// 更新收款会员金额
			SubAcctPo subAcctNewAmt = ConverterService.convert(subAcctPo,
					SubAcctPo.class);
			subAcctNewAmt.setBal(newBalAmt);
			subAcctNewAmt.setIntrBal(newIntrAmt);
			subAcctNewAmt.setMaxCashableAmt(newMaxCashAmt);
			subAcctNewAmt.setLastMntOpid(currOpId);
			subAcctNewAmt.setLastMntTs(currDate);
			subAcctRepository.save(subAcctNewAmt);
		}

		if (toPledgeAmt.compareTo(BigDecimal.ZERO) > 0) {

			// 记录子账户收款交易日志
			SubAcctTrxJnlPo payTrxJnlPo = new SubAcctTrxJnlPo();
			payTrxJnlPo.setEventId(eventId);
			payTrxJnlPo.setAcctNo(subAcctPo.getAcctNo());
			payTrxJnlPo.setSubAcctNo(subAcctPo.getSubAcctNo());
			payTrxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, cashPool));
			payTrxJnlPo.setPayRecvFlg(EFundPayRecvFlag.PAY);
			payTrxJnlPo.setTrxDt(workDate);
			payTrxJnlPo.setTrxAmt(toPledgeAmt);
			payTrxJnlPo.setBal(subAcctPo.getBal().add(trxAmt));
			payTrxJnlPo.setTrxMemo(trxMemo);
			payTrxJnlPo.setUseType(EFundUseType.INTERNAL);
			payTrxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payeeInfo.getBizId(), bizId));
			payTrxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
			payTrxJnlPo.setCreateOpid(currOpId);
			payTrxJnlPo.setCreateTs(currDate);
			subAcctTrxJnlRepository.save(payTrxJnlPo);

			// 质押账户原余额
			BigDecimal pledgeOldBal = AmtUtils.processNegativeAmt(
					pledgeAcctPo.getBal(), BigDecimal.ZERO);

			// 质押账户新账户余额 = 原账户余额 + 账户需要划转的金额
			BigDecimal pledgeNewBal = pledgeOldBal.add(toPledgeAmt);

			// 原可计息余额
			BigDecimal pledgeOldIntrBal = AmtUtils.processNegativeAmt(
					pledgeAcctPo.getIntrBal(), BigDecimal.ZERO);

			// 新可计息余额 = Max(原可计息余额 + 账户需要划转的金额, 0)
			BigDecimal pledgeNewIntrBal = pledgeOldIntrBal.add(toPledgeAmt);

			// 更新子账户信息
			SubAcctPo pledgeTargetAcctPo = ConverterService.convert(
					pledgeAcctPo, SubAcctPo.class);
			pledgeTargetAcctPo.setBal(pledgeNewBal);
			pledgeTargetAcctPo.setIntrBal(pledgeNewIntrBal);
			pledgeTargetAcctPo.setLastMntOpid(currOpId);
			pledgeTargetAcctPo.setLastMntTs(currDate);
			subAcctRepository.save(pledgeTargetAcctPo);

			// 记录子账户收款交易日志
			SubAcctTrxJnlPo recvTrxJnlPo = new SubAcctTrxJnlPo();
			recvTrxJnlPo.setEventId(eventId);
			recvTrxJnlPo.setAcctNo(pledgeAcctPo.getAcctNo());
			recvTrxJnlPo.setSubAcctNo(pledgeAcctPo.getSubAcctNo());
			recvTrxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, cashPool));
			recvTrxJnlPo.setPayRecvFlg(EFundPayRecvFlag.RECV);
			recvTrxJnlPo.setTrxDt(workDate);
			recvTrxJnlPo.setTrxAmt(toPledgeAmt);
			recvTrxJnlPo.setBal(pledgeNewBal);
			recvTrxJnlPo.setTrxMemo(trxMemo);
			recvTrxJnlPo.setUseType(EFundUseType.INTERNAL);
			recvTrxJnlPo.setRelBizId(StringUtils.defaultIfBlank(payeeInfo.getBizId(), bizId));
			recvTrxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
			recvTrxJnlPo.setCreateOpid(currOpId);
			recvTrxJnlPo.setCreateTs(currDate);
			subAcctTrxJnlRepository.save(recvTrxJnlPo);
		}
	}

	private void checkTransferAmtIgnoreFrzAmt(List<TransferInfo> payerList,
			List<TransferInfo> payeeList) throws BizServiceException {

		// 不能收付双边都为空
		if ((payerList == null || payerList.isEmpty())
				&& (payeeList == null || payeeList.isEmpty())) {
			throw new BizServiceException(TECH_DATA_INVALID, "付款方列表和收款方列表不能都为空");
		}

		if (payeeList == null || payeeList.isEmpty()) {

			LOG.debug("充值冲正交易....");

			boolean existsNotReverse = existsNotReverseRecharge(payerList);

			if (existsNotReverse) {
				throw new BizServiceException(FUND_ACCT_REVERSE_FAILED,
						"存在非冲正的交易");
			}
			return;
		}

		if (payerList == null || payerList.isEmpty()) {

			LOG.debug("提现冲正交易....");

			boolean existsNotReverse = existsNotReverseWithdraw(payeeList);

			if (existsNotReverse) {
				throw new BizServiceException(FUND_ACCT_REVERSE_FAILED,
						"存在非冲正的交易");
			}
			return;
		}

		BigDecimal sumPayerAmt = BigDecimal.ZERO;
		for (TransferInfo payerInfo : payerList) {
			sumPayerAmt = sumPayerAmt.add(AmtUtils.processNegativeAmt(
					payerInfo.getTrxAmt(), BigDecimal.ZERO));
		}

		BigDecimal sumPayeeAmt = BigDecimal.ZERO;
		for (TransferInfo payeeInfo : payeeList) {
			sumPayeeAmt = sumPayeeAmt.add(AmtUtils.processNegativeAmt(
					payeeInfo.getTrxAmt(), BigDecimal.ZERO));
		}

		if (sumPayerAmt.compareTo(sumPayeeAmt) != 0) {
			throw new BizServiceException(TECH_DATA_INVALID,
					"sum(付款方金额)!=sum(收款方金额)");
		}
	}

	private boolean existsNotReverseRecharge(List<TransferInfo> payerList) {
		boolean bool = false;
		for (TransferInfo payerInfo : payerList) {
			if (payerInfo.getUseType() != EFundUseType.RECHARGE_REVERSE) {
				bool = true;
				break;
			}
		}
		return bool;
	}

	private boolean existsNotReverseWithdraw(List<TransferInfo> payeeList) {
		boolean bool = false;
		for (TransferInfo payerInfo : payeeList) {
			if (payerInfo.getUseType() != EFundUseType.CASH_REVERSE) {
				bool = true;
				break;
			}
		}
		return bool;
	}

	@Override
	public BigDecimal getUserSubAcctAvlBalIgnoreFrzAmt(String userId,
			ESubAcctType subAcctType) throws BizServiceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("获取忽略冻结金额之后的可用余额会员编号为:" + userId);
		}
		String subAcctNo = BusinessAcctUtils
				.getSubAcctNoBySubAcctType(subAcctType.getCode());

		BigDecimal avlAmt = subAcctRepository.getUserSubAcctAvlAmtIgnoreFrzAmt(
				userId, subAcctNo);

		avlAmt = AmtUtils.processNullAmt(avlAmt, BigDecimal.ZERO);

		avlAmt = AmtUtils.max(avlAmt, BigDecimal.ZERO);

		if (LOG.isDebugEnabled()) {
			LOG.debug("忽略冻结金额之后，可用余额为:" + avlAmt.doubleValue());
		}
		return avlAmt;
	}

	@Override
	public BigDecimal getUserCurrentAndXwbSubAcctAvlAmt(String userId)
			throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("获取活期与小微宝的可用余额会员编号为:" + userId);
		}

		BigDecimal avlAmt = subAcctRepository
				.getUserCurrentAndXwbSubAcctAvlAmt(userId);

		avlAmt = AmtUtils.processNullAmt(avlAmt, BigDecimal.ZERO);

		avlAmt = AmtUtils.max(avlAmt, BigDecimal.ZERO);

		if (LOG.isDebugEnabled()) {
			LOG.debug("活期与小微宝的可用余额为:" + avlAmt.doubleValue());
		}
		return avlAmt;
	}

	@Override
	public BigDecimal getUserCurrentAndXwbSubAcctAvlAmtIgnoreFrzAmt(
			String userId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("获取忽略冻结金额之后的可用余额会员编号为:" + userId);
		}

		BigDecimal avlAmt = subAcctRepository
				.getUserCurrentAndXwbSubAcctAvlAmtIgnoreFrzAmt(userId);

		avlAmt = AmtUtils.processNullAmt(avlAmt, BigDecimal.ZERO);

		avlAmt = AmtUtils.max(avlAmt, BigDecimal.ZERO);

		if (LOG.isDebugEnabled()) {
			LOG.debug("忽略冻结金额之后，可用余额为:" + avlAmt.doubleValue());
		}
		return avlAmt;
	}

	@Override
	public void addMaxCashableAmt(String userId, BigDecimal trxAmt,
			String currOpId) throws BizServiceException,
			AvlBalNotEnoughException {
		BigDecimal addAmt = AmtUtils
				.processNegativeAmt(trxAmt, BigDecimal.ZERO);
		SubAcctPo subAcct = this.getSubAcctPo(userId,
				ESubAcctNo.CURRENT.getCode());
		BigDecimal oldMaxCashableAmt = AmtUtils.processNegativeAmt(
				subAcct.getMaxCashableAmt(), BigDecimal.ZERO);
		
//		BigDecimal oldBal = AmtUtils.processNegativeAmt(subAcct.getBal(),
//				BigDecimal.ZERO);
//		// 冻结金额不考虑在内，特殊场景下，冻结会导致 可用 小于等于 0，导致无法更新金额。
//		BigDecimal oldFreezableAmt = BigDecimal.ZERO;
//		
//		BigDecimal oldReservedAmt = AmtUtils.processNegativeAmt(
//				subAcct.getReservedAmt(), BigDecimal.ZERO);
//		BigDecimal avlAmt = AmtUtils.max(oldBal.subtract(oldFreezableAmt)
//				.subtract(oldReservedAmt), BigDecimal.ZERO);
//		if (avlAmt.compareTo(addAmt) < 0) {
//			throw new AvlBalNotEnoughException(subAcct.getAcctNo(), "可用余额不足");
//		}

		BigDecimal newMaxCashableAmt = oldMaxCashableAmt.add(addAmt);

		subAcct.setMaxCashableAmt(newMaxCashableAmt);
		subAcct.setLastMntOpid(currOpId);
		subAcct.setLastMntTs(new Date());
		subAcctRepository.save(subAcct);
	}

	@Override
	public Long getAcctFrzCt(String userId) throws BizServiceException {
		AcctPo acct = this.getAcctByUserId(userId);
		return acct.getFrzCt();
	}

	@Override
	public void treasurerAcctTotalBal(String eventId, String acctNo,
			String fromPool, String toPool, String trxMemo, String bizId,
			String currOpId, Date workDate) throws BizServiceException {

		BigDecimal totalBal = subAcctRepository.getAcctTotalBalByAcctNo(acctNo);
		
		String userId = acctRepository.findByAcctNo(acctNo).getUserId();
		
		Date currDate = new Date();
		// pay
		SubAcctTrxJnlPo payTrxJnlPo = new SubAcctTrxJnlPo();
		payTrxJnlPo.setEventId(eventId);
		payTrxJnlPo.setAcctNo(acctNo);
		payTrxJnlPo.setSubAcctNo(null);
		payTrxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, fromPool));
		payTrxJnlPo.setPayRecvFlg(EFundPayRecvFlag.PAY);
		payTrxJnlPo.setTrxDt(workDate);
		payTrxJnlPo.setTrxAmt(totalBal);
		payTrxJnlPo.setBal(BigDecimal.ZERO);
		payTrxJnlPo.setTrxMemo(trxMemo);
		payTrxJnlPo.setUseType(EFundUseType.CHANGE_POOL);
		payTrxJnlPo.setRelBizId(bizId);
		payTrxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		payTrxJnlPo.setCreateOpid(currOpId);
		payTrxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(payTrxJnlPo);

		// recv
		SubAcctTrxJnlPo recvTrxJnlPo = new SubAcctTrxJnlPo();
		recvTrxJnlPo.setEventId(eventId);
		recvTrxJnlPo.setAcctNo(acctNo);
		recvTrxJnlPo.setSubAcctNo(null);
		recvTrxJnlPo.setCashPool(EnumHelper.translate(ECashPool.class, toPool));
		recvTrxJnlPo.setPayRecvFlg(EFundPayRecvFlag.RECV);
		recvTrxJnlPo.setTrxDt(workDate);
		recvTrxJnlPo.setTrxAmt(totalBal);
		recvTrxJnlPo.setBal(totalBal);
		recvTrxJnlPo.setTrxMemo(trxMemo);
		recvTrxJnlPo.setUseType(EFundUseType.CHANGE_POOL);
		recvTrxJnlPo.setRelBizId(bizId);
		recvTrxJnlPo.setAuthzdCtrId(getAuthzdCtrId(userId));
		recvTrxJnlPo.setCreateOpid(currOpId);
		recvTrxJnlPo.setCreateTs(currDate);
		subAcctTrxJnlRepository.save(recvTrxJnlPo);
	}
	
	private String getAuthzdCtrId(String userId){
		return ServiceCenterUtils.getAuthzdCtrIdByUserId(userId);
	}

	@Override
	public BigDecimal getUserSubAcctsSumBalAmt(String userId)
			throws BizServiceException {
		AcctPo acct = this.getAcctByUserId(userId);
		List<SubAcctPo> subAcctList = subAcctRepository.findByAcctNo(acct.getAcctNo());
		BigDecimal sumAmt = BigDecimal.ZERO;
		for(SubAcctPo subAcct : subAcctList){
			sumAmt =  sumAmt.add(AmtUtils.processNegativeAmt(subAcct.getBal(), BigDecimal.ZERO));
		}
		return sumAmt;
	}

}
