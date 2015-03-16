/*
 * Project Name: kmfex-platform
 * File Name: ProductPackageInvestorDetailsDto.java
 * Class Name: ProductPackageInvestorDetailsDto
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
package com.hengxin.platform.market.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.PaymentScheduleDto;
import com.hengxin.platform.market.dto.downstream.TransferMarketItemDetailDto;
import com.hengxin.platform.market.dto.upstream.TransferMarketPurchaseDto;
import com.hengxin.platform.market.dto.upstream.TransferMarketSearchDto;
import com.hengxin.platform.market.enums.ETransferType;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.market.service.TransferMarketService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.service.FinancingPackageAmountService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class TransferMarketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferMarketController.class);

    @Autowired
    private transient TransferMarketService transferMarketService;

    @Autowired
    private transient SecurityContext securityContext;

    @Autowired
    private transient FinancingMarketService financingMarketService;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private transient FinancingPackageAmountService financingPackageAmountService;
    
    @Autowired
    private transient PaymentScheduleRepository paymentScheduleRepository;
    
    @Autowired
    private transient MemberMessageService messageService;
    
    @Autowired
    private transient UserRepository userRepository;

    @RequestMapping(value = UrlConstant.FINANCING_MARKETING_TRANSFER_VIEW_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
    public String home(Model model) {
        LOGGER.debug("home() invoked");
        model.addAttribute("marketClose", !CommonBusinessUtil.isMarketOpen());
        model.addAttribute(
                "hideTransfer",
                !securityContext.isInvestorFinancierTourist() && !securityContext.isInvestor()
                        && !securityContext.isFinancier());
        model.addAttribute("investorFlag", securityContext.isInvestor());
        model.addAttribute("transferTypes", ETransferType.values());
        return "market/market_transfer";
    }

    @RequestMapping(value = "market/transfer/search")
    @ResponseBody
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
    public DataTablesResponseDto<MarketItemDto> search(HttpServletRequest req,
            @RequestBody TransferMarketSearchDto request) {
        LOGGER.debug("search() invoked");
        boolean isPlatformUser = securityContext.isPlatformUser();
        DataTablesResponseDto<MarketItemDto> resp = transferMarketService.getTransferMarketItems(request,
                securityContext.getCurrentUserId(), isPlatformUser);
        resp.setEcho(request.getEcho());
        return resp;
    }

    @RequestMapping(value = "market/transfer/purchase/{id}", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
    public TransferMarketItemDetailDto detail(@PathVariable(value = "id") String id) {
        LOGGER.debug("detail() invoked");
        TransferMarketItemDetailDto resp = transferMarketService.getTransferItemDetail(id,
                securityContext.getCurrentUserId());
        if (!securityContext.isInvestor()) {
            resp.setTransfered(false);
            resp.setReason(MessageUtil.getMessage("market.error.subscribe.permissions.nothave"));
        }
        if (!CommonBusinessUtil.isMarketOpen()) {
            resp.setTransfered(false);
            resp.setReason(MessageUtil.getMessage("market.error.market.close"));
        }
        return resp;
    }

    @RequestMapping(value = "market/transfer/detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
    public String getDetailPage(@PathVariable(value = "id") String id, HttpServletRequest request, HttpSession session,
            Model model) {
        LOGGER.debug("getDetailPage() invoked");
        Map<String, Object> map = processMarketTransferDetail(id);
        model.addAttribute("paymentList", map.get("paymentList"));
        model.addAttribute("itemDto", map.get("itemDto"));
        model.addAttribute("marketClose", map.get("marketClose"));
        model.addAttribute("isUserTourist", map.get("isUserTourist"));
        model.addAttribute("investorFlag", map.get("investorFlag"));
        return "market/market_transfer_detail";
    }

	public Map<String, Object> processMarketTransferDetail(String id) {
		TransferMarketItemDetailDto itemDto = transferMarketService.getTransferItemDetail(id,
                securityContext.getCurrentUserId());
        List<PaymentScheduleDto> payments = new ArrayList<PaymentScheduleDto>();
        if (itemDto != null && itemDto.getProduct() != null) {
            if (itemDto.getProduct().getWarrantyType() == EWarrantyType.MONITORASSETS
                    || itemDto.getProduct().getWarrantyType() == EWarrantyType.NOTHING) {
                itemDto.getProduct().setGuaranteeInstitution("--");
                itemDto.getProduct().setGuaranteeInstitutionShow("--");
            } else if (StringUtils.isNotBlank(itemDto.getProduct().getWarrantIdShow())) {
                Agency agency = memberService.getAgencyByUserId(itemDto.getProduct().getWarrantIdShow());
                itemDto.getProduct().setGuaranteeLicenseNoImg(agency != null ? agency.getLicenceNoImg() : "");
            }
            if (this.securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)) {
            	itemDto.getProduct().getUser().setMaskChineseName(itemDto.getProduct().getUser().getName());
            } else {
            	itemDto.getProduct().getUser().setMaskChineseName(MaskUtil.maskChinsesName(itemDto.getProduct().getUser().getName()));
            }
        	for (PaymentSchedule p : paymentScheduleRepository.getByPackageIdOrderBySequenceIdAsc(itemDto.getPkg().getId())) {
        		PaymentScheduleDto dto = ConverterService.convert(p, PaymentScheduleDto.class);
    			BigDecimal rate = new BigDecimal(itemDto.getUnit()).divide(
    					itemDto.getPkg().getSupplyAmount().divide(itemDto.getPkg().getUnitAmount()), 10,
    					RoundingMode.DOWN);
        		dto.setRate(rate);
        		payments.add(dto);
        	}
        }
        Map<String, Object> map = new HashMap<String, Object>();
        boolean isUserTourist = securityContext.isInvestorFinancierTourist() || securityContext.isInvestor()
                || securityContext.isFinancier();
        map.put("paymentList", payments);
        map.put("itemDto", itemDto);
        map.put("marketClose", !CommonBusinessUtil.isMarketOpen());
        map.put("isUserTourist", isUserTourist);
        map.put("investorFlag", securityContext.isInvestor());
        return map;
	}
    
    @RequestMapping(value = "market/transfer/purchase", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_PURCHASE)
    public ResultDto purchase(@RequestBody TransferMarketPurchaseDto item) {
        LOGGER.debug("purchase() invoked");
        // 判断是否开市
        if (!CommonBusinessUtil.isMarketOpen()) {
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.market.close"));
        }
        // 验证投资会员
        if (!securityContext.isInvestor()) {
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.subscribe.permissions.nothave"));
        }
        try {
            item = transferMarketService.purchase(securityContext.getCurrentUserId(), item.getId());
        } catch (BizServiceException ex) {
            return ResultDtoFactory.toNack(ex.getError());
        }
        
        String sellerMessageKey = "package.transfer.seller.message";
        String buyerMessageKey = "package.transfer.buyer.message";
        String financierMessageKey = "package.transfer.financier.message";
        
        // 发送消息和短信提醒
        messageService.sendMessage(EMessageType.MESSAGE, sellerMessageKey, item.getSellerId(), item.getPkgId(), NumberUtil.formatMoney(item.getFee()), NumberUtil.formatMoney(item.getAmount().subtract(item.getFee())));
        messageService.sendMessage(EMessageType.SMS, sellerMessageKey, item.getSellerId(), item.getPkgId(), NumberUtil.formatMoney(item.getFee()), NumberUtil.formatMoney(item.getAmount().subtract(item.getFee())));
        messageService.sendMessage(EMessageType.MESSAGE, buyerMessageKey, item.getBuyerId(), item.getPkgId(), NumberUtil.formatMoney(item.getAmount().add(item.getFee())), NumberUtil.formatMoney(item.getFee()));
        messageService.sendMessage(EMessageType.SMS, buyerMessageKey, item.getBuyerId(), item.getPkgId(), NumberUtil.formatMoney(item.getAmount().add(item.getFee())), NumberUtil.formatMoney(item.getFee()));
        
        String financierName = userRepository.findUserByUserId(item.getFinancierId()).getName();
        String sellerName = userRepository.findUserByUserId(item.getSellerId()).getName();
        String buyerName = userRepository.findUserByUserId(item.getBuyerId()).getName();
        
        messageService.sendMessage(EMessageType.MESSAGE, financierMessageKey, item.getFinancierId(), financierName, sellerName, buyerName, item.getContractId(), NumberUtil.formatMoney(item.getAccumCrAmt()), DateFormatUtils.format(item.getWorkDate(), "yyyy年MM月dd日"));
        
        return ResultDtoFactory.toAck(MessageUtil.getMessage("market.transfer.deal.success", NumberUtil.formatMoney(item.getAmount()),
        		NumberUtil.formatMoney(item.getFee())));
    }

	public void setTransferMarketService(TransferMarketService transferMarketService) {
		this.transferMarketService = transferMarketService;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	public void setPaymentScheduleRepository(
			PaymentScheduleRepository paymentScheduleRepository) {
		this.paymentScheduleRepository = paymentScheduleRepository;
	}
}
