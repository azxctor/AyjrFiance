package com.hengxin.platform.escrow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.consts.HttpUrlConsts;
import com.hengxin.platform.ebc.dto.EbcBankListRequest;
import com.hengxin.platform.ebc.dto.EbcBankListResponse;
import com.hengxin.platform.ebc.dto.EbcBindingBankCardRequest;
import com.hengxin.platform.ebc.dto.EbcBindingBankCardResponse;
import com.hengxin.platform.ebc.dto.EbcEditBindingBankCardRequest;
import com.hengxin.platform.ebc.dto.EbcEditBindingBankCardResponse;
import com.hengxin.platform.ebc.dto.EbcErrorResponse;
import com.hengxin.platform.ebc.dto.bank.BankInfo;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.dto.EswBankCardAddOrderDto;
import com.hengxin.platform.escrow.dto.EswBankCardModOrderDto;
import com.hengxin.platform.escrow.dto.EswBankDto;
import com.hengxin.platform.escrow.dto.EswEditBankDto;
import com.hengxin.platform.escrow.dto.bank.Bank;
import com.hengxin.platform.escrow.dto.bank.City;
import com.hengxin.platform.escrow.dto.bank.PayeeBank;
import com.hengxin.platform.escrow.dto.bank.Province;
import com.hengxin.platform.escrow.dto.card.AcctCardDto;
import com.hengxin.platform.escrow.dto.card.AddCardRespDto;
import com.hengxin.platform.escrow.dto.card.ModCardRespDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswBankCardAddOrderPo;
import com.hengxin.platform.escrow.enums.EEbcAbtNoType;
import com.hengxin.platform.escrow.enums.EEbcBankQueryType;
import com.hengxin.platform.escrow.enums.EEbcErrorType;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswAcctRepository;
import com.hengxin.platform.escrow.repository.EswBankCardAddOrderRepository;
import com.hengxin.platform.escrow.utils.EswUtils;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;

@Service
public class EswBindingService extends EswBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EswBindingService.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EswAcctService eswAcctService;
	@Autowired
	private EswAcctRepository eswAcctRepository;
	@Autowired
	private BankAcctRepository bankAcctRepository;
	@Autowired
	private EswAddCardRequiresNewService addCardRequiresNewService;
	@Autowired
	private EswModCardRequiresNewService modCardRequiresNewService;
	@Autowired
	private EswBankCardAddOrderRepository eswCardAddRep;

	@SuppressWarnings("unchecked")
	public List<PayeeBank> getPayeeBankInfo(String userId) {
		List<PayeeBank> result = new ArrayList<PayeeBank>();
		EbcBankListRequest request = buildEbcBankListRequest(userId);
		request.setQueryType(EEbcBankQueryType.PAYEEBANK.getCode());
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcBankListResponse response = (EbcBankListResponse) rawResponse;
			result.add(new PayeeBank("", ""));
			result.addAll(response.getDataList());
			return result;
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			throw new BizServiceException(EErrorCode.EBC_BIZ_ERROR, response.getErrText());
		}
	}

	@SuppressWarnings("unchecked")
	public List<Province> getProvinceInfo(String userId) {
		List<Province> result = new ArrayList<Province>();
		EbcBankListRequest request = buildEbcBankListRequest(userId);
		request.setQueryType(EEbcBankQueryType.PROVINCE.getCode());
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcBankListResponse response = (EbcBankListResponse) rawResponse;
			result.add(new Province("", ""));
			result.addAll(response.getDataList());
			return result;
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			throw new BizServiceException(EErrorCode.EBC_BIZ_ERROR, response.getErrText());
		}
	}

	@SuppressWarnings("unchecked")
	public List<City> getCityInfo(String userId, String provinceCode) {
		List<City> result = new ArrayList<City>();
		EbcBankListRequest request = buildEbcBankListRequest(userId);
		request.setQueryType(EEbcBankQueryType.CITY.getCode());
		request.setProvinceCode(provinceCode);
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcBankListResponse response = (EbcBankListResponse) rawResponse;
			result.add(new City("", ""));
			result.addAll(response.getDataList());
			return result;
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			throw new BizServiceException(EErrorCode.EBC_BIZ_ERROR, response.getErrText());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BankInfo getBankInfo(String userId, String payeeBankId, String cityCode) {
		BankInfo result = new BankInfo();
		EbcBankListRequest request = buildEbcBankListRequest(userId);
		request.setQueryType(EEbcBankQueryType.BANK.getCode());
		request.setPayeeBankId(payeeBankId);
		request.setCityCode(cityCode);
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcBankListResponse response = (EbcBankListResponse) rawResponse;
			List banks = new ArrayList();
			response = (EbcBankListResponse) request.execute();
			banks.add(new Bank("", ""));
			banks.addAll(response.getDataList());
			result.setBankId(response.getBankId());
			result.setBankList(banks);
			return result;
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			throw new BizServiceException(EErrorCode.EBC_BIZ_ERROR, response.getErrText());
		}
	}

	private EbcBankListRequest buildEbcBankListRequest(String userId) {
		EbcBankListRequest request = new EbcBankListRequest();
		request.setOrderSn(IdUtil.produce());
		EswAcctPo eswAcctPo = eswAcctService.getEscrowAccountByUserId(userId);
		request.setUserNo(eswAcctPo.getEswUserNo());
		request.setMediumNo(eswAcctPo.getEswAcctNo());
		request.setOwnerId(EBCConsts.OWNER_ID);
		return request;
	}

	@Transactional
	public EOrderStatusEnum binding(String userId, EswBankDto bankDto) {
		LOGGER.info("binding() invoked, {}", userId);
		EswAcctPo eswAcctPo = eswAcctService.getEscrowAccountByUserId(userId);
		// 首先判断该卡号是否已与银盈通绑定
		List<EswBankCardAddOrderPo> orderList = eswCardAddRep.findByUserIdAndBankCardNoAndStatusOrderByCreateTsDesc(userId, 
				bankDto.getBankCardNo(), EOrderStatusEnum.SUCCESS);
		for (EswBankCardAddOrderPo order : orderList) {
			if (order.getBankCardNo().equals(bankDto.getBankCardNo())) {
				// 绑卡信息与之前的绑卡指令内容一致
				String assetNo = order.getBankAssetsId();
				bankDto.setBankTypeCode(order.getBankTypeCode());
				bankDto.setBankCardName(order.getBankCardname());
				bankDto.setProvinceCode(order.getProvCode());
				bankDto.setCityCode(order.getCityCode());
				bankDto.setBankId(order.getBankId());
				bankDto.setBankCode(order.getBankCode());
				bankDto.setBankName(order.getBankName());
				AcctCardDto cardDto = generateAcctCardDto(bankDto, eswAcctPo, assetNo);
				addCardRequiresNewService.saveBindBankCardSuccess(cardDto, null, userId);
				return EOrderStatusEnum.SUCCESS;
			}
		}
		EswBankCardAddOrderDto newOrderDto = buildCardAddDto(userId, bankDto, eswAcctPo);
		EswBankCardAddOrderDto addBankCardOrderDto = addCardRequiresNewService.saveNewBindBankCardOrder(newOrderDto);
		String orderId = addBankCardOrderDto.getOrderId();

		EbcBindingBankCardRequest request = buildEbcBindingBankCardRequest(bankDto, eswAcctPo);
		CommandResponse rawResponse = request.execute();

		if (isSuccessRespsonse(rawResponse)) {
			EbcBindingBankCardResponse response = (EbcBindingBankCardResponse) rawResponse;
			// 绑定银行卡信息入库
			String assetNo = response.getAssetNo();
			AddCardRespDto addCardRespDto = new AddCardRespDto();
			addCardRespDto.setOrderId(orderId);
			addCardRespDto.setStatus(EOrderStatusEnum.SUCCESS); // 状态
			addCardRespDto.setRetCode(response.getReturnCode()); // 返回编码
			addCardRespDto.setRetMsg(response.getErrText()); // 返回信息
			addCardRespDto.setBankAssetsId(response.getAssetNo()); // 银行卡资产编号

			AcctCardDto cardDto = generateAcctCardDto(bankDto, eswAcctPo, assetNo);
			addCardRequiresNewService.saveBindBankCardSuccess(cardDto, addCardRespDto, userId);
			return EOrderStatusEnum.SUCCESS;
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			EEbcErrorType et = EnumHelper.translate(EEbcErrorType.class, response.getReturnCode());
			// 银行受理中
			if ((EEbcErrorType.HANDLING_BY_BANK).equals(et)) {
				AddCardRespDto addCardRespDto = new AddCardRespDto();
				addCardRespDto.setOrderId(orderId);
				addCardRespDto.setStatus(EOrderStatusEnum.PROCESS); // 状态
				addCardRespDto.setRetCode(response.getReturnCode()); // 返回编码
				addCardRespDto.setRetMsg(response.getErrText()); // 返回信息
				addCardRequiresNewService.saveBindBankCardOrderDealing(addCardRespDto, userId);
				return EOrderStatusEnum.PROCESS;
			} else {
				AddCardRespDto addCardRespDto = new AddCardRespDto();
				addCardRespDto.setOrderId(orderId);
				addCardRespDto.setStatus(EOrderStatusEnum.FAILED); // 状态
				addCardRespDto.setRetCode(response.getReturnCode()); // 返回编码
				addCardRespDto.setRetMsg(response.getErrText()); // 返回信息
				addCardRequiresNewService.saveBindBankCardOrderFailed(addCardRespDto, userId);
				EErrorCode errorCode = EErrorCode.EBC_BINDING_ERROR;
				errorCode.setArgs(new Object[] { response.getErrText() });
				throw new BizServiceException(errorCode, et.getText());
			}
		}
	}

	public AcctCardDto generateAcctCardDto(EswBankDto bankDto, EswAcctPo eswAcctPo, String assetNo) {
		AcctCardDto cardDto = new AcctCardDto();
		cardDto.setAcctNo(eswAcctPo.getAcctNo());
		cardDto.setBankCardNo(bankDto.getBankCardNo());
		cardDto.setBankCardName(bankDto.getBankCardName());
		cardDto.setAssetNo(assetNo);
		cardDto.setBankId(bankDto.getBankId());
		cardDto.setBankCode(bankDto.getBankCode());
		cardDto.setBankName(bankDto.getBankName());
		cardDto.setBankTypeCode(bankDto.getBankTypeCode());
		cardDto.setProvinceCode(bankDto.getProvinceCode());
		cardDto.setCityCode(bankDto.getCityCode());
		return cardDto;
	}

	public EbcBindingBankCardRequest buildEbcBindingBankCardRequest(EswBankDto bankDto, EswAcctPo eswAcctPo) {
		EbcBindingBankCardRequest request = new EbcBindingBankCardRequest();
		request.setMerchNo(EswUtils.getEswMerChNo()); // 商户号
		request.setVersion(EBCConsts.VERSION); // 版本号
		request.setOrderSn(IdUtil.produce()); // 系统流水号
		request.setOwnerId(EswUtils.getEswServProv()); // 运营商
		request.setCurrency(EBCConsts.CURRENCY); // 币种

		request.setUserNo(eswAcctPo.getEswUserNo()); // 用户ID 托管会员编号
		request.setMediumNo(eswAcctPo.getEswAcctNo()); // 钱包介质ID
		request.setUserName(bankDto.getBankCardName()); // 持卡人姓名
		request.setUserType(eswAcctPo.getEswUserType()); // 用户类型
		request.setCardNo(bankDto.getBankCardNo()); // 银行卡号
		request.setAbtNo(EEbcAbtNoType.BANKCARD.getCode()); // 资产大类ID
		request.setBankId(bankDto.getBankId()); // 总行联行号
		request.setBankCode(bankDto.getBankCode()); // 支行/分行联行号
		request.setBankName(bankDto.getBankName()); // 发行卡名称
		request.setPayeeBankid(bankDto.getBankTypeCode()); // 行别码
		request.setProvinceCode(bankDto.getProvinceCode()); // 省份编码
		request.setCityCode(bankDto.getCityCode()); // 城市编码
		request.setDsybUrl(getBindCardAsyncRespUrl()); // 异步回调地址
		return request;
	}

	public EswBankCardAddOrderDto buildCardAddDto(String userId, EswBankDto bankDto, EswAcctPo eswAcctPo) {
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		EswBankCardAddOrderDto newOrderDto = new EswBankCardAddOrderDto();
		newOrderDto.setStatus(EOrderStatusEnum.PROCESS); // 状态处理中
		newOrderDto.setTrxDate(workDate); // 处理日期
		newOrderDto.setAcctNo(eswAcctPo.getAcctNo()); // 会员综合账户
		newOrderDto.setUserId(userId); // 会员编号
		newOrderDto.setEswCustAcctNo(eswAcctPo.getEswCustAcctNo()); // 托管商户账号
		newOrderDto.setEswAcctNo(eswAcctPo.getEswAcctNo()); // 托管会员账号//);
		newOrderDto.setEswSubAcctNo(eswAcctPo.getEswSubAcctNo()); // 托管子账户编号
		newOrderDto.setEswUserNo(eswAcctPo.getEswUserNo()); // 托管会员编号
		newOrderDto.setBankCardNo(bankDto.getBankCardNo()); // 银行卡号
		newOrderDto.setBankCardname(bankDto.getBankCardName()); // 持卡人姓名
		newOrderDto.setBankId(bankDto.getBankId()); // 总行联行号
		newOrderDto.setBankCode(bankDto.getBankCode()); // 分行联行号
		newOrderDto.setBankName(bankDto.getBankName()); // 发卡行名称
		newOrderDto.setBankTypeCode(bankDto.getBankTypeCode()); // 行别码
		newOrderDto.setProvCode(bankDto.getProvinceCode()); // 省份编码
		newOrderDto.setCityCode(bankDto.getCityCode()); // 城市编码
		newOrderDto.setServProv(EBCConsts.OWNER_ID); // 运营商
		newOrderDto.setCreateOpid(userId); // 创建用户
		newOrderDto.setCreateTs(new Date()); // 创建时间
		return newOrderDto;
	}

	private String getBindCardAsyncRespUrl() {
		StringBuffer url = new StringBuffer();
		url.append(CommonBusinessUtil.getEbcRespUrl());
		url.append("/");
		url.append(HttpUrlConsts.BINDING_BANK_CARD_ASYNC_RESPONSE_URL);
		return url.toString();
	}

	@Transactional
	public EOrderStatusEnum editBinding(String userId, EswEditBankDto bankDto) {
		LOGGER.info("editBinding() invoked, {}", userId);
		EswAcctPo eswAcctPo = eswAcctService.getEscrowAccountByUserId(userId);
		EswBankCardModOrderDto modOrderDto = buildCardModDto(userId, bankDto, eswAcctPo);
		EswBankCardModOrderDto modOrder = modCardRequiresNewService.saveNewModBankCardOrder(modOrderDto);

		EbcEditBindingBankCardRequest request = buildEbcEditBindingBankCardRequest(bankDto, eswAcctPo);
		CommandResponse rawResponse = request.execute();

		if (isSuccessRespsonse(rawResponse)) {
			EbcEditBindingBankCardResponse response = (EbcEditBindingBankCardResponse) rawResponse;
			ModCardRespDto respDto = new ModCardRespDto();
			respDto.setOrderId(modOrder.getOrderId());
			respDto.setStatus(EOrderStatusEnum.SUCCESS); // 状态
			respDto.setRetCode(response.getReturnCode()); // 返回编码
			respDto.setRetMsg(response.getErrText()); // 返回信息
			modCardRequiresNewService.saveModBankCardSuccess(eswAcctPo.getAcctNo(), bankDto.getProvinceCode(), 
					bankDto.getCityCode(), bankDto.getBankId(), bankDto.getBankCode(),
					bankDto.getBankName(), respDto, userId);
			return EOrderStatusEnum.SUCCESS;
		} else {
			// 修改绑定卡信息失败，不更新托管账户表
			EbcErrorResponse errResp = (EbcErrorResponse) rawResponse;
			String retCode = errResp.getReturnCode();
			String retMsg = errResp.getErrText();
			ModCardRespDto respDto = new ModCardRespDto();
			respDto.setOrderId(modOrder.getOrderId());
			respDto.setStatus(EOrderStatusEnum.FAILED); // 状态
			respDto.setRetCode(retCode); // 返回编码
			respDto.setRetMsg(retMsg); // 返回信息
			modCardRequiresNewService.saveModBankCardFailed(respDto, userId);
			return EOrderStatusEnum.FAILED;
		}
	}

	public EswBankCardModOrderDto buildCardModDto(String userId, EswEditBankDto bankDto, EswAcctPo eswAcctPo) {
		EswBankCardModOrderDto modOrderDto = new EswBankCardModOrderDto();
		modOrderDto.setAcctNo(eswAcctPo.getAcctNo()); // 会员综合账户
		modOrderDto.setTrxDate(new Date());
		modOrderDto.setUserId(userId); // 会员编号
		modOrderDto.setEswCustAcctNo(eswAcctPo.getEswCustAcctNo()); // 托管商户账号
		modOrderDto.setEswAcctNo(eswAcctPo.getEswAcctNo()); // 托管会员账号
		modOrderDto.setEswSubAcctNo(eswAcctPo.getEswSubAcctNo()); // 托管子账户编号
		modOrderDto.setEswUserNo(eswAcctPo.getEswUserNo()); // 托管会员编号
		modOrderDto.setBankAssetsId(eswAcctPo.getBankAssetsId()); // 银行卡资产编号
		modOrderDto.setBankCardNo(bankDto.getBankCardNo()); // 银行卡号
		modOrderDto.setBankCardName(bankDto.getBankCardName()); // 持卡人姓名
		modOrderDto.setServProv(EBCConsts.OWNER_ID); // 运营商
		modOrderDto.setCreateOpid(userId); // 创建用户
		modOrderDto.setCreateTs(new Date()); // 创建时间
		modOrderDto.setStatus(EOrderStatusEnum.WAITING); // 状态
		modOrderDto.setBankId(bankDto.getBankId()); // 总行联行号
		modOrderDto.setBankCode(bankDto.getBankCode()); // 分行联行号
		modOrderDto.setBankName(bankDto.getBankName()); // 发卡行名称
		modOrderDto.setCreateOpid(userId);
		modOrderDto.setCreateTs(new Date());
		return modOrderDto;
	}

	public EbcEditBindingBankCardRequest buildEbcEditBindingBankCardRequest(EswEditBankDto bankDto, EswAcctPo eswAcctPo) {
		EbcEditBindingBankCardRequest request = new EbcEditBindingBankCardRequest();
		request.setMerchNo(EswUtils.getEswMerChNo()); // 商户号
		request.setOrderSn(IdUtil.produce()); // 系统流水号
		request.setOwnerId(EswUtils.getEswServProv()); // 运营商
		request.setCurrency(EBCConsts.CURRENCY); // 币种

		request.setUserNo(eswAcctPo.getEswUserNo()); // 用户ID
		request.setMediumNo(eswAcctPo.getEswAcctNo()); // 钱包介质ID
		request.setUserName(bankDto.getBankCardName()); // 持卡人姓名
		request.setUserType(eswAcctPo.getEswUserType()); // 用户类型
		request.setCardNo(bankDto.getBankCardNo()); // 银行卡号
		request.setAbtNo(EEbcAbtNoType.BANKCARD.getCode()); // 资产大类id
		request.setBankId(bankDto.getBankId()); // 总行联行号
		request.setBankCode(bankDto.getBankCode()); // 支行/分行联行号
		request.setBankDeposit(bankDto.getBankName()); // 发行卡名称
		request.setAssets(eswAcctPo.getBankAssetsId()); // assets
		return request;
	}

	@Transactional
	public void unbinding(String userId) {
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setBindingCardStatus(BindingCardStatusEnum.UNDO);
		EswAcctPo eswAcctPo = eswAcctRepository.findByUserId(userId);
		String bankCardNo = eswAcctPo.getBankCardNo();
		eswAcctPo.setBankCardNo(null);
		eswAcctPo.setBankAssetsId(null);
		eswAcctPo.setBankCardName(null);
		eswAcctPo.setBankCode(null);
		eswAcctPo.setBankId(null);
		eswAcctPo.setBankName(null);
		eswAcctPo.setBankTypeCode(null);
		eswAcctPo.setCityCode(null);
		eswAcctPo.setProvCode(null);
		eswAcctRepository.save(eswAcctPo);
		userRepository.save(userPo);
		BankAcctPo bankAcct = bankAcctRepository.findOne(bankCardNo);
		if (null != bankAcct) {
			if (!StringUtils.equals(userId, bankAcct.getUserId())) {
				EErrorCode errorCode = EErrorCode.EBC_BINDING_ERROR;
				errorCode.setArgs(new Object[] { "银行卡信息与用户信息不匹配，解绑银行卡失败" });
				throw new BizServiceException(errorCode, "银行卡信息与用户信息不匹配，解绑银行卡失败");
			}
			bankAcctRepository.delete(bankCardNo);
		}
	}

	/**
	 * 获取已绑定的银行卡列表
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<EswBankDto> getBindedBankList(String userId) {
		List<EswBankCardAddOrderPo> orderList = eswCardAddRep.findByUserIdAndStatusOrderByCreateTsDesc(userId, 
				EOrderStatusEnum.SUCCESS);
		List<EswBankDto> bankDtoList = null ;
		if( null!=orderList){
			bankDtoList = new ArrayList<EswBankDto>();
			for(EswBankCardAddOrderPo orderPo: orderList){
				EswBankDto dto = new EswBankDto(orderPo.getBankCardNo(), orderPo.getBankId(), 
						orderPo.getBankCode(), orderPo.getBankTypeCode(), orderPo.getProvCode(), 
						orderPo.getCityCode(), orderPo.getBankName(), orderPo.getBankCardname());
				bankDtoList.add(dto);
			}
		}
		return bankDtoList;
	}
	
	/**
	 * 获取已绑定的银行卡数量
	 * @param userId
	 * @return
	 */
	@Transactional
	public int getBindedBankSize(String userId) {
		List<EswBankCardAddOrderPo> orderList = eswCardAddRep.findByUserIdAndStatusOrderByCreateTsDesc(userId, 
				EOrderStatusEnum.SUCCESS);
		return orderList == null ? 0: orderList.size();
	}
	
	/**
	 * 获取已绑定的银行卡信息
	 * @param userId
	 * @return
	 */
	@Transactional
	public EswBankDto getBindedBankInfo(String userId, String bankCardNo) {
		List<EswBankCardAddOrderPo> orderList = eswCardAddRep.findByUserIdAndBankCardNoAndStatusOrderByCreateTsDesc(userId, bankCardNo,
				EOrderStatusEnum.SUCCESS);
		if(null == orderList) return null;
		EswBankCardAddOrderPo orderPo = orderList.get(0);
		EswBankDto dto = new EswBankDto(orderPo.getBankCardNo(), orderPo.getBankId(), 
				orderPo.getBankCode(), orderPo.getBankTypeCode(), orderPo.getProvCode(), 
				orderPo.getCityCode(), orderPo.getBankName(), orderPo.getBankCardname());
		return dto;
	}
}
