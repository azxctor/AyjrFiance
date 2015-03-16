package com.hengxin.platform.escrow.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.ebc.dto.bank.BankInfo;
import com.hengxin.platform.escrow.dto.EswAcctDto;
import com.hengxin.platform.escrow.dto.EswBankDto;
import com.hengxin.platform.escrow.dto.bank.PayeeBank;
import com.hengxin.platform.escrow.dto.bank.Province;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswBankCardAddOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswBankCardAddOrderRepository;
import com.hengxin.platform.escrow.service.EswAccountService;
import com.hengxin.platform.escrow.service.EswAcctService;
import com.hengxin.platform.escrow.service.EswBindingService;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;
import com.hengxin.platform.security.service.UserService;

@Controller
public class EswInfoMgtController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswInfoMgtController.class);

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private EswAcctService eswAcctService;

	@Autowired
	private EswBindingService eswBindingService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EswAccountService eswAccountService;

	@Autowired
	private AcctService acctService;
	
	@Autowired
	private EswBankCardAddOrderRepository cardAddRepository;

	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = UrlConstant.ESCROW_ACCT_MGT_URL, method = RequestMethod.GET)
	public String eswInfoIndex(HttpServletRequest request, Model model) {
		LOGGER.debug("eswInfoIndex() start: ");
		String userId = securityContext.getCurrentUserId();
		User user = userService.getUserByUserId(userId);
		BindingCardStatusEnum bindingState = user.getBindingCardStatus();
		List<PayeeBank> bankList = null;
		List<Province> provinceList = null;
		EswAcctPo eswAcct = null;
		EswBankCardAddOrderPo addCardOrderPo = null;
		if (user.getEswAcctStatus() == EswAcctStatusEnum.DONE) {
			bankList = eswBindingService.getPayeeBankInfo(userId);
			provinceList = eswBindingService.getProvinceInfo(userId);
			eswAcct = eswAcctService.getEscrowAccountByUserId(userId);
		}
		String mobile = user.getMobile();
		boolean isActive = isEswAcctActive(userId);
		boolean needActive = needEswAcctActive(userId);
		boolean isIdCardImgNull = eswAccountService.isIdCardImgNull(userId);
		EswAcctDto eswAcctDto = new EswAcctDto();
		if (eswAcct != null) {
			eswAcctDto.setAcctNo(eswAcct.getAcctNo());
			eswAcctDto.setEswAcctNo(eswAcct.getEswAcctNo());
			eswAcctDto.setEswCustAcctNo(eswAcct.getEswCustAcctNo());
			eswAcctDto.setEswSubAcctNo(eswAcct.getEswSubAcctNo());
			eswAcctDto.setEswUserNo(eswAcct.getEswUserNo());
		}
		model.addAttribute("isIdCardImgNull", isIdCardImgNull);
		model.addAttribute("eswAcctDto", eswAcctDto);
		model.addAttribute("isActive", isActive);
		model.addAttribute("needActive", needActive);
		model.addAttribute("bindingState", bindingState);
		
		EswBankDto dto = null;
		BankInfo bankInfo = null;
		if (BindingCardStatusEnum.PROCESS.equals(bindingState)) {
			List<EswBankCardAddOrderPo> list = cardAddRepository
					.findByUserIdAndStatusOrderByCreateTsDesc(userId, EOrderStatusEnum.PROCESS);
			if(list.size()>0){
				addCardOrderPo = list.get(0);
				String bankCardName = addCardOrderPo.getBankCardname();
				String bankCode = addCardOrderPo.getBankCode();
				String bankCardNo = addCardOrderPo.getBankCardNo();
				String bankId = addCardOrderPo.getBankId();
				String payeeBankId = addCardOrderPo.getBankTypeCode();
				String provinceCode = addCardOrderPo.getProvCode();
				String cityCode = addCardOrderPo.getCityCode();
				String bankName = addCardOrderPo.getBankName();
				dto = new EswBankDto(bankCardNo, bankId , bankCode, payeeBankId,
						provinceCode, cityCode, bankName, bankCardName);
				bankInfo = eswBindingService.getBankInfo(
						securityContext.getCurrentUserId(), payeeBankId, cityCode);
			}
		}else if(BindingCardStatusEnum.DONE.equals(bindingState)){
			String bankCardName = eswAcct.getBankCardName();
			String bankCode = eswAcct.getBankCode();
			String bankCardNo = eswAcct.getBankCardNo();
			String bankId = eswAcct.getBankId();
			String payeeBankId = eswAcct.getBankTypeCode();
			String provinceCode = eswAcct.getProvCode();
			String cityCode = eswAcct.getCityCode();
			String bankName = eswAcct.getBankName();
			dto = new EswBankDto(bankCardNo, bankId , bankCode, payeeBankId,
					provinceCode, cityCode, bankName, bankCardName);
			bankInfo = eswBindingService.getBankInfo(
					securityContext.getCurrentUserId(), payeeBankId, cityCode);
		}
		int bindedBankSize = eswBindingService.getBindedBankSize(userId);
		model.addAttribute("bindedBankSize", bindedBankSize);					//已绑定银行卡数量
		model.addAttribute("bankInfo", bankInfo);
		model.addAttribute("bankDto", dto);
		model.addAttribute("bankList", bankList);
		model.addAttribute("mobile", mobile);
		model.addAttribute("provinceList", provinceList);
		return "escrow/esw_info_mgt";
	}

	/**
	 * 账户是否激活
	 * 
	 * @param userId
	 * @return
	 */
	private boolean isEswAcctActive(String userId) {
		User user = userService.getUserByUserId(userId);
		boolean isActive = user.getEswAcctStatus() == EswAcctStatusEnum.DONE;
		return isActive;
	}

	/**
	 * 是否满足账户激活条件
	 * 
	 * @param userId
	 * @return
	 */
	private boolean needEswAcctActive(String userId) {
		User user = userService.getUserByUserId(userId);
		boolean needActive = user.getEswAcctStatus() == EswAcctStatusEnum.UNDO;
		String acctNo = acctService.getAcctNo(userId);
		return needActive && acctNo != null;
	}

}
