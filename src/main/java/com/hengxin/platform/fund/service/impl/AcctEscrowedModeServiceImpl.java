package com.hengxin.platform.fund.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.service.AcctEscrowedModeService;

@Service
@Qualifier("acctEscrowedModeService")
public class AcctEscrowedModeServiceImpl implements AcctEscrowedModeService {
	
	@Override
	public ECashPool getCashPool(String signedFlag, String bnkCode) {
		ECashPool cashPool = null;
		if (isEscrowedMode()){
			String servProv = CommonBusinessUtil.getEscrowProvider();
			cashPool = EnumHelper.translate(ECashPool.class, servProv);
		}
		else {
			if (StringUtils.equalsIgnoreCase(bnkCode, EBankType.ICBC.getCode())) {
				cashPool = ECashPool.ICBC_COMMON;
			} 
			else if (StringUtils.equalsIgnoreCase(signedFlag,
					EFlagType.YES.getCode())
					&& StringUtils.equalsIgnoreCase(bnkCode,
							EBankType.CMB.getCode())) {
				cashPool = ECashPool.CMB_SPECIAL;
			}
			else {
				cashPool = ECashPool.CMB_COMMON;
			}
		}
		return cashPool;
	}

	@Override
	public boolean isEscrowedMode() {
		return CommonBusinessUtil.isEscrowedMode();
	}
}
