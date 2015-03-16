package com.hengxin.platform.report.service;

import java.math.BigDecimal;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.product.domain.CreditorRightsTransfer;
import com.hengxin.platform.product.domain.CreditorsRightTransferContract;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.repository.CreditorRightsTransferRepository;
import com.hengxin.platform.product.repository.CreditorsRightTransferContractRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.report.dto.TransferContractDetailsDto;

@Service
public class TransferContractService {

	@Autowired
	private CreditorsRightTransferContractRepository creditorsRightTransferContractRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductPackageRepository productPackageRepository;

	@Autowired
	private PositionLotRepository positionLotRepository;

	@Autowired
	private CreditorRightsTransferRepository creditorRightsTransferRepository;

	@Autowired
	private AcctRepository acctRepository;

	@Transactional(readOnly = true)
	public TransferContractDetailsDto getTransferContractDetails(String lotId,
			String type) {
		CreditorsRightTransferContract contract = null;
		if ("seller".equals(type)) {
			contract = creditorsRightTransferContractRepository
					.findBySellerLotId(lotId);
		} else if ("buyer".equals(type)) {
			contract = creditorsRightTransferContractRepository
					.findByBuyerLotId(lotId);
		}
		if (contract != null) {
			TransferContractDetailsDto dto = new TransferContractDetailsDto();
			dto.setId(contract.getId());
			dto.setSellerName(userRepository.findOne(contract.getSellerId())
					.getName());
			dto.setBuyerName(userRepository.findOne(contract.getBuyerId())
					.getName());
			dto.setPkgId(contract.getPackageId());
			ProductPackage pkg = productPackageRepository.findOne(contract
					.getPackageId());
			dto.setFinancierName(userRepository.findOne(
					pkg.getProduct().getApplUserId()).getName());
			PositionLotPo lot = positionLotRepository.findOne(lotId);
			dto.setLoanContractId(lot.getContractId());
			CreditorRightsTransfer transfer = creditorRightsTransferRepository
					.findByTransferContractId(contract.getId());
			dto.setLastPayDate(DateFormatUtils.format(
					transfer.getMaturityDate(), "yyyy年MM月dd日"));
			dto.setRemainAmount(transfer.getRemainAmount().setScale(2).toString());
			dto.setRemainAmountRMB(NumberUtil.toRMB(dto.getRemainAmount()));
			dto.setPrice(transfer.getPrice().setScale(2).toString());
			dto.setPriceRMB(NumberUtil.toRMB(dto.getPrice()));
			dto.setFeeRate(contract.getBuyerFeeRate()
					.multiply(new BigDecimal(100)).toString());
			dto.setSellerAcctNo(acctRepository.findByUserId(
					contract.getSellerId()).getAcctNo());
			dto.setBuyerAcctNo(acctRepository.findByUserId(
					contract.getBuyerId()).getAcctNo());
			dto.setSignTimestamp(DateFormatUtils.format(
					transfer.getLastMntTs(), "yyyy年MM月dd日"));
			dto.setCreateTimestamp(DateFormatUtils.format(
					contract.getCreateTs(), "yyyy-MM-dd HH:mm:ss"));
			return dto;
		}
		return null;
	}
}

