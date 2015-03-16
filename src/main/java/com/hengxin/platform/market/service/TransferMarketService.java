/*
 * Project Name: kmfex-platform
 * File Name: MemberService.java
 * Class Name: MemberService
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.market.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.fund.dto.biz.req.TransferCRReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.FundPositionService;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.MarketProductDto;
import com.hengxin.platform.market.dto.downstream.TransferMarketItemDetailDto;
import com.hengxin.platform.market.dto.upstream.TransferMarketPurchaseDto;
import com.hengxin.platform.market.dto.upstream.TransferMarketSearchDto;
import com.hengxin.platform.market.enums.ETransferType;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.member.domain.SubscribeGroupUser;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.product.domain.CreditorRightsTransfer;
import com.hengxin.platform.product.domain.CreditorsRightTransferContract;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETransferStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.CreditorRightsTransferRepository;
import com.hengxin.platform.product.repository.CreditorsRightTransferContractRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.risk.dto.PurchaseRiskResultDto;
import com.hengxin.platform.risk.enums.ERiskBearLevel;
import com.hengxin.platform.risk.service.InvestRiskBearService;
import com.hengxin.platform.risk.service.InvestorPurchaseLimitationService;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: MemberService.
 * 
 * @author runchen
 * 
 */
@Service
public class TransferMarketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferMarketService.class);

    @Autowired
    private CreditorRightsTransferRepository transferRepository;

    @Autowired
    private transient FundAcctBalService fundAccoutService;

    @Autowired
    private transient ServiceCenterService serviceCenterService;

    @Autowired
    private transient ProductPackageRepository packageRepository;

    @Autowired
    private transient FundPositionService positionService;
    
    @Autowired
    private transient AcctService acctService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private transient CreditorsRightTransferContractRepository creditorsRightTransferContractRepository;

    @Autowired
    private transient PkgTradeJnlRepository pkgTradeJnlRepository;

    @Autowired
    private transient FinancingMarketService financingMarketService;

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private transient FinancingPackageService financingPackageService;
    
    @Autowired
    private transient InvestorPurchaseLimitationService limitationService;
    
	@Autowired
	private transient InvestRiskBearService investRiskBearService;
    
	private String getAcctNo(String userId){
		AcctPo acct = acctService.getAcctByUserId(userId);
		return acct == null?"":acct.getAcctNo();
	}
	
    @Transactional
    public TransferMarketPurchaseDto purchase(String userId, String id) throws BizServiceException {

        CreditorRightsTransfer transfer = transferRepository.findOne(id);
        if (transfer == null || !ETransferStatus.ACTIVE.getCode().equals(transfer.getStatus())) {
            throw new BizServiceException(EErrorCode.TRANSFER_NOT_FOUND);
        }
        if (userId.equals(transfer.getPositionLot().getPosition().getUserId())) {
            throw new BizServiceException(EErrorCode.TRANSFER_YOUR_OWN);
        }
        String lotId = transfer.getLotId();
        boolean canPay = acctService.acctCanPay(userId);
        if(!canPay){
        	LOGGER.debug("账户只收不付，转让失败");
        	throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY);
        }
        // 判断定投条件
        ProductPackage pkg = transfer.getPositionLot().getPosition().getProductPackage();
        if (pkg.getAipFlag() && pkg.getAipEndTime() != null && pkg.getAipEndTime().compareTo(new Date()) >= 0) {
            List<ProductPackage> pkgs = packageRepository.findByPkgIdAndAIPGroupUser(pkg.getId(), userId);
            if (CollectionUtils.isEmpty(pkgs)) {
                throw new BizServiceException(EErrorCode.TRANSFER_NOT_FOUND);
            }
        }
        BigDecimal price = transfer.getPrice();
        BigDecimal userCurrentAcctAvlBal = fundAccoutService.getUserCurrentAcctAvlBal(userId, true);
        if (userCurrentAcctAvlBal.compareTo(price) < 0) {
            throw new BizServiceException(EErrorCode.BALANCE_LACK);
        }
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        // 债权转让
        PositionPo position = transfer.getPositionLot().getPosition();
        TransferCRReq changeReq = new TransferCRReq();
        changeReq.setBuyerUserId(userId);
        changeReq.setSellerUserId(position.getUserId());
        changeReq.setPkgId(position.getPkgId());
        changeReq.setUnit(transfer.getPositionLot().getUnit());
        changeReq.setLotBuyPrice(price);
        changeReq.setLotId(lotId);
        changeReq.setWorkDate(workDate);
        changeReq.setCurrOpId(userId);
        changeReq.setTrdMemo("卖方会员"+position.getUserId()+"转让头寸编号"+lotId+"的债权给买方会员"+userId);
        String packageTradeJnlNo = positionService.transferCR(changeReq);

        String eventId = IdUtil.produce();

        // 资金划转
        String trxMemo = "债权转让，资金由" + getAcctNo(userId) + "划转到" + getAcctNo(position.getUserId());
        boolean isRelZQ = true;
        TransferInfo payer = new TransferInfo();
        payer.setUserId(userId);
        payer.setTrxAmt(price);
        payer.setTrxMemo(trxMemo);
        payer.setUseType(EFundUseType.TRANSFERCR);
        payer.setRelZQ(isRelZQ);
        TransferInfo payee = new TransferInfo();
        payee.setUserId(position.getUserId());
        payee.setTrxAmt(price);
        payee.setTrxMemo(trxMemo);
        payee.setUseType(EFundUseType.TRANSFERCR);
        payee.setRelZQ(isRelZQ);
        positionService.transferCRPayAmt(eventId, Arrays.asList(payer), Arrays.asList(payee), 
        		true, lotId, pkg.getId(), null, userId, workDate);

        BigDecimal feeRate = CommonBusinessUtil.getTransferFeeRate();
        TransferMarketPurchaseDto item = new TransferMarketPurchaseDto();
        item.setAmount(transfer.getPrice());
        item.setFee(transfer.getPrice().multiply(feeRate).setScale(2, RoundingMode.DOWN));
        item.setSellerId(position.getUserId());
        item.setBuyerId(userId);
        item.setPkgId(position.getPkgId());
        item.setFinancierId(pkg.getFinancierId());
        item.setContractId(transfer.getPositionLot().getContractId());
        item.setAccumCrAmt(transfer.getPositionLot().getAccumCrAmt());
        item.setWorkDate(workDate);

        // 费用划转 - 买卖双方会员把债权转让费用转平台账户以及各自授权服务中心账户
        trxMemo = "融资项目" + position.getPkgId() + "，扣取债权转让费用" + item.getFee() + "元";
        isRelZQ = false;
        payer = new TransferInfo();
        payer.setUserId(position.getUserId());
        payer.setTrxAmt(item.getFee());
        payer.setTrxMemo(trxMemo);
        payer.setUseType(EFundUseType.TRANSFERCR_FEE);
        payer.setRelZQ(isRelZQ);
        payee = new TransferInfo();
        payee.setUserId(CommonBusinessUtil.getExchangeUserId());
        payee.setTrxAmt(item.getFee());
        payee.setTrxMemo(trxMemo);
        payee.setUseType(EFundUseType.TRANSFERCR_FEE);
        payee.setRelZQ(isRelZQ);
        positionService.transferCRPayAmt(eventId, Arrays.asList(payer), Arrays.asList(payee), 
        		true, lotId, pkg.getId(), null, userId, workDate);

        trxMemo = "融资项目" + position.getPkgId() + "，扣取债权转让费用" + item.getFee() + "元";
        isRelZQ = false;
        payer = new TransferInfo();
        payer.setUserId(userId);
        payer.setTrxAmt(item.getFee());
        payer.setTrxMemo(trxMemo);
        payer.setUseType(EFundUseType.TRANSFERCR_FEE);
        payer.setRelZQ(isRelZQ);
        payee = new TransferInfo();
        payee.setUserId(CommonBusinessUtil.getExchangeUserId());
        payee.setTrxAmt(item.getFee());
        payee.setTrxMemo(trxMemo);
        payee.setUseType(EFundUseType.TRANSFERCR_FEE);
        payee.setRelZQ(isRelZQ);
        positionService.transferCRPayAmt(eventId, Arrays.asList(payer), Arrays.asList(payee), 
        		true, lotId, pkg.getId(), null, userId, workDate);

		// 债权转让协议
        CreditorsRightTransferContract contract = new CreditorsRightTransferContract();
        contract.setSellerId(position.getUserId());
        contract.setBuyerId(userId);
		contract.setPackageId(position.getPkgId());
		contract.setTradeDate(workDate);
		contract.setTradePrice(transfer.getPrice());
		contract.setCreditorsRightId(transfer.getPositionLot().getCrId());
		contract.setCrRemainAmount(transfer.getRemainAmount());
		contract.setCrMaturityDate(transfer.getMaturityDate());
		contract.setLoanContractId(transfer.getPositionLot().getContractId());
		contract.setSellerFeeRate(feeRate);
		contract.setBuyerFeeRate(feeRate);
		contract.setSellerLotId(lotId);
		contract.setBuyerLotId(pkgTradeJnlRepository.findOne(packageTradeJnlNo).getLotId());
		contract.setPackageTradeJnlNo(packageTradeJnlNo);
		contract.setCreateOpid(userId);
		contract.setCreateTs(new Date());
        creditorsRightTransferContractRepository.save(contract);

		// 交易成功，更新状态
		transfer.setStatus(ETransferStatus.SUCCESS.getCode());
		transfer.setLastMntOpid(userId);
		transfer.setLastMntTs(new Date());
		transfer.setTransferContractId(contract.getId());
		transferRepository.save(transfer);
		
		return item;
    }

    @Transactional(readOnly = true)
    public TransferMarketItemDetailDto getTransferItemDetail(String id, String userId) {
        TransferMarketItemDetailDto dto = new TransferMarketItemDetailDto();
        CreditorRightsTransfer transfer = transferRepository.findOne(id);
        if (transfer == null) {
            dto.setTransfered(false);
            dto.setReason(MessageUtil.getMessage("market.error.transfer.notfound"));
        } else {
        	dto.setTransferId(id);
        	dto.setUnit(transfer.getUnit());
            BigDecimal price = transfer.getPrice();
            BigDecimal userCurrentAcctAvlBal = fundAccoutService.getUserCurrentAcctAvlBal(userId, true);
            if (userCurrentAcctAvlBal.compareTo(price) < 0) {
                dto.setTransfered(false);
                dto.setReason(MessageUtil.getMessage("market.error.balance.lack"));
            }
            if (userId.equals(transfer.getPositionLot().getPosition().getUserId())) {
                dto.setTransfered(false);
                dto.setReason(MessageUtil.getMessage("market.error.transfer.youown"));
            }
            if (!ETransferStatus.ACTIVE.getCode().equals(transfer.getStatus())) {
            	dto.setTransfered(false);
                dto.setReason(MessageUtil.getMessage("market.error.transfer.notfound"));
                dto.setClosed(true);
            }
            /** risk limitation.
            if (CommonBusinessUtil.isRiskActive()) {
        		ProductPackage pkg = transfer.getPositionLot().getPosition().getProductPackage();
				PurchaseRiskResultDto limitation = limitationService.getSubscribeLimitation(userId, pkg.getId());
        		if (limitation.isQualified()) {
        			if (price.compareTo(limitation.getUpperLimit()) > 0) {
        				// 债券金额  > 申购上限.
						dto.setTransfered(false);
						dto.setReason(MessageUtil.getMessage("market.error.subscribe.amount.lack", 
								limitation.getMaxSubscribeAmount(), limitation.getSubscribeAmount()));
					}
				} else {
					dto.setTransfered(false);
					dto.setReason(limitation.getMessage());
				}
            } else 
            **/
            if (CommonBusinessUtil.isRiskActiveTemp()) {
            	ProductPackage pkg = transfer.getPositionLot().getPosition().getProductPackage();
            	ERiskBearLevel userLevel = this.investRiskBearService.getUserRiskBearLevel(userId);
            	if (ERiskLevel.HIGH_RISK == pkg.getProduct().getProductLevel()) {
            		if (ERiskBearLevel.AGGRESSIVE != userLevel) {
                		/** 非进取型不能买高风险. **/
        				dto.setTransfered(false);
        				dto.setReason(MessageUtil.getMessage("market.error.userlevel.unqualified"));
                	}
            		if (ERiskBearLevel.AGGRESSIVE == userLevel 
            				&& price.compareTo(SubscribeUtils.fiveUnitFaceValue()) > 0) {
                    	/** 转让报价大于5000元. **/
        				dto.setTransfered(false);
        				dto.setReason(MessageUtil.getMessage("market.error.subscribe.aomout.upperlimit"));
        			}
				}
            	if (dto.isTransfered()) {
            		if (ERiskLevel.HIGH_RISK == pkg.getProduct().getProductLevel()) {
            			PurchaseRiskResultDto limitation = limitationService.getSubscribeLimitationTemp(userId, pkg.getId());
                		if (limitation.isQualified()) {
                			if (price.compareTo(limitation.getUpperLimit()) > 0) {
                				/** 债券金额  > 申购上限. **/
        						dto.setTransfered(false);
        						dto.setReason(MessageUtil.getMessage("market.error.subscribe.amount.overflow",
        								limitation.getMaxSubscribeAmount(), limitation.getSubscribeAmount()));
        					} else if (price.add(limitation.getAccumulativeAmount()).compareTo(limitation.getUpperLimitForSameCategory()) > 0) {
        						/** 债券金额  + 累计金额 > 同一风险类型累计上限. **/
        						dto.setTransfered(false);
        						dto.setReason(MessageUtil.getMessage("market.error.subscribe.amount.overflow"));
        					}
        				} else {
        					dto.setTransfered(false);
        					dto.setReason(limitation.getMessage());
        				}
    				}
				}
            }
            
            dto.setLotId(transfer.getPositionLot().getLotId());
            dto.setPrice(price);
			dto.setRemainingAmount(transfer.getRemainAmount());
			dto.setRemainIntrAmount(transfer.getRemainIntrAmount());
			ProductPackage pkg = transfer.getPositionLot().getPosition().getProductPackage();
			dto.setRate(pkg.getProduct().getRate());
			dto.setSupplyEndDate(pkg.getSupplyEndTime());
			dto.setPkg(ConverterService.convert(pkg, ProductPackageDto.class));
			ProductDetailsDto productDetails = financingMarketService.getProductDetail(pkg.getProductId());
			UserPo user = productService.getUser(productDetails.getWarrantId());
			productDetails.setGuaranteeInstitution(user == null ? null : user.getName());
            
            if (productDetails.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productDetails.getWarrantyType() == EWarrantyType.NOTHING) {
                productDetails.setGuaranteeInstitution("--");
            }
            dto.setProduct(productDetails);
            dto.setMaturityDate(transfer.getMaturityDate());
        }

        return dto;
    }

    /**
     * Get member info
     * 
     * @param searchDto
     * @param currentUserId
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<MarketItemDto> getTransferMarketItems(final TransferMarketSearchDto searchDto,
            final String currentUserId, final boolean isPlatformUser) {

        Pageable pageRequest = buildPageRequest(searchDto);

        Specification<CreditorRightsTransfer> specification = new Specification<CreditorRightsTransfer>() {

            @Override
            public Predicate toPredicate(Root<CreditorRightsTransfer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                Path<ProductPackage> packagePath = root.<PositionLotPo> get("positionLot").<PositionPo> get("position")
                        .<ProductPackage> get("productPackage");
				if (ETransferType.MINE == searchDto.getTransferType()) {
					expressions.add(cb.equal(root.<String> get("createOpid"),
							currentUserId));
				} else if (searchDto.getTransferType() == ETransferType.OTHER) {
					expressions.add(cb.notEqual(
							root.<String> get("createOpid"), currentUserId));
				}

                if (!isPlatformUser) {
                    Expression<Boolean> openExpression = cb.equal(packagePath.<String> get("aipFlag"), false);
                    Expression<Boolean> aipExpression  = cb.equal(packagePath.<String> get("aipFlag"), true);
                    Expression<Boolean> aipCloseExpression = cb.and(aipExpression,
                            cb.exists(getAipSubquery(query, cb, currentUserId, packagePath.<String> get("id"))));
                    // 定投在定投结束时间后未满额，就变成公开
                    Expression<Boolean> aipOpenExpression = cb.and(aipExpression,
                            cb.lessThan(packagePath.<Date> get("aipEndTime"), cb.currentDate()));

                    expressions.add(cb.or(openExpression, cb.or(aipCloseExpression, aipOpenExpression)));
                }
                
                if (searchDto != null) {
                    if (StringUtils.isNotBlank(searchDto.getProductId())) {
                        expressions.add(cb.equal(packagePath.<Product> get("product").<String> get("productId"),
                                searchDto.getProductId()));
                    }
					if (StringUtils.isNotBlank(searchDto.getPackageId())) {
						expressions.add(cb.like(
								cb.lower(packagePath.<String> get("id")), "%"
										+ searchDto.getPackageId().trim()
												.toLowerCase() + "%"));
					}
	                /* fetch the records which are before current time. */
	                if (searchDto.getCreateTime() != null) {
	                	expressions.add(cb.lessThanOrEqualTo(root.get("createTs").as(Date.class), searchDto.getCreateTime()));
	                }
                }
                expressions.add(cb.equal(root.<String> get("status"), ETransferStatus.ACTIVE.getCode()));
                return predicate;
                
            }
            
            private Subquery<ProductPackage> getAipSubquery(CriteriaQuery<?> query, CriteriaBuilder cb, String userId,
                    Path<String> pkgId) {
                Subquery<ProductPackage> sq = query.subquery(ProductPackage.class);
                Root<ProductPackage> pkg = sq.from(ProductPackage.class);
                Join<ProductPackage, SubscribeGroup> packageAIP = pkg.join("aipGroups");
                Join<SubscribeGroup, SubscribeGroupUser> groupUser = packageAIP.join("users");
                return sq.select(pkg).where(cb.and(cb.equal(pkg.get("id"), pkgId), cb.equal(groupUser.<String> get("userId"), userId)));
            }
            
        };

        Page<CreditorRightsTransfer> transfers = transferRepository.findAll(specification, pageRequest);

        DataTablesResponseDto<MarketItemDto> responseDtoList = new DataTablesResponseDto<MarketItemDto>();
        List<MarketItemDto> itemList = new ArrayList<MarketItemDto>();
        responseDtoList.setEcho(searchDto.getEcho());
        for (CreditorRightsTransfer transfer : transfers) {
            ProductPackage pkg = transfer.getPositionLot().getPosition().getProductPackage();
            MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
            dto.setTransferId(transfer.getId());
            dto.setLotId(transfer.getLotId());
            dto.setProduct(ConverterService.convert(pkg.getProduct(), MarketProductDto.class));
            dto.getProduct().setRiskLevel(pkg.getProduct().getProductLevel());
            String wrtrId = pkg.getProduct().getWarrantId();
            String wrtrIdShow = pkg.getProduct().getWarrantIdShow();
            String wrtrName = null;
            String wrtrNameShow = null;
            if (StringUtils.isNotBlank(wrtrId)) {
                Agency agency = serviceCenterService.getAgencyById(wrtrId);
                if (agency != null) {
                	wrtrName = agency.getName();
                	wrtrNameShow = agency.getName();
                }
            }
            if(StringUtils.isNotBlank(wrtrIdShow)
            		&&!StringUtils.equals(wrtrId, wrtrIdShow)){
                UserPo user = userRepository.findUserByUserId(wrtrIdShow);
                if (user != null) {
                	wrtrNameShow = user.getName();
                }
            }
            dto.getProduct().setWarrantor(wrtrName);
            dto.getProduct().setWarrantorShow(wrtrNameShow);
            
            dto.setPrice(transfer.getPrice());
            dto.setRemainingAmount(transfer.getRemainAmount());
            dto.setOneselfOwn(currentUserId.equals(transfer.getPositionLot().getPosition().getUserId()));
            dto.setMaturityDate(transfer.getMaturityDate());
            dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
                    && pkg.getAipEndTime().compareTo(new Date()) >= 0);
            dto.setMine(StringUtils.equals(transfer.getCreateOpid(), currentUserId));
            
            dto.setResidualPrincipalAmt(transfer.getRemainPrinAmount());
            dto.setResidualInterestAmt(transfer.getRemainIntrAmount());
            dto.setRemTermLength(transfer.getRemainPeriods());
            
            dto.setTotalHolderCount(pkgTradeJnlRepository.getBuyerCount(pkg.getId()));
            dto.setOverdue(financingPackageService.isHistoryOverdue(pkg.getId()));
            itemList.add(dto);
        }
        responseDtoList.setData(itemList);
        responseDtoList.setTotalDisplayRecords(transfers.getTotalElements());
        responseDtoList.setTotalRecords(transfers.getTotalElements());
        return responseDtoList;
    }

    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {

        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        if (sortedColumns != null) {
	        for (Integer item : sortedColumns) {
	            String sortColumn = dataProps.get(item);
	            int indexOf = 0;
	            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
	                indexOf = sortColumn.lastIndexOf(".text");
	            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
	                indexOf = sortColumn.lastIndexOf(".fullText");
	            }
	            if (indexOf > 0) {
	                sortColumn = sortColumn.substring(0, indexOf);
	            }
	            if ("product.riskLevel".equals(sortColumn)) {
	                sortColumn = "product.totalGrage";
	            }
	            if ("product.termLength".equals(sortColumn)) {
	                sortColumn = "product.termToDays";
	            }
	            if ("subscribeEndTimeStr".equals(sortColumn)) {
	                sortColumn = "supplyEndTime";
	            }
	            if ("packageQuotaStr".equals(sortColumn)) {
	                sortColumn = "packageQuota";
	            }
	            if ("product.ratePercentage".equals(sortColumn)) {
	                sortColumn = "product.rate";
	            }
	
	            if ("priceStr".equals(sortColumn)) {
	                sortColumn = "price";
	            } else if ("remainingAmountStr".equals(sortColumn)) {
	                sortColumn = "remainAmount";
	            } else if ("maturityDateStr".equals(sortColumn)) {
	                sortColumn = "maturityDate";
	            } else {
	                sortColumn = "positionLot.position.productPackage." + sortColumn;
	            }
	            String sortDir = sortDirections.get(0);
	            sort = new Sort(Direction.fromString(sortDir), sortColumn);
	            sort = sort.and(new Sort(Direction.ASC, "maturityDate"));
	        }
        }
        if (sort == null) {
        	sort = new Sort(Direction.DESC, "createTs");
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

}
