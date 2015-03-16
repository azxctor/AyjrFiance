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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.MarketProductDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketHistorySearchDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketSearchDto;
import com.hengxin.platform.market.enums.EHistoryPeriod;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.member.domain.SubscribeGroupUser;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.product.domain.MortgageResidential;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductAsset;
import com.hengxin.platform.product.domain.ProductDebt;
import com.hengxin.platform.product.domain.ProductMortgageVehicle;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.domain.ProductPledge;
import com.hengxin.platform.product.domain.ProductWarrantEnterprise;
import com.hengxin.platform.product.domain.ProductWarrantPerson;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.dto.ProductUserDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: MemberService
 * 
 * @author runchen
 * 
 */
@Service
public class FinancingMarketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancingMarketService.class);

    @Autowired
    private transient ProductPackageRepository packageRepository;

    @Autowired
    private transient AgencyRepository agencyRepository;

    @Autowired
    private transient ServiceCenterService serviceCenterService;

    @Autowired
    private transient FinancingResourceService resourceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FinancingPackageService productPackageService;

    @Transactional(readOnly = true)
    public ProductPackage getPackageDetail(String pkgId) {
        ProductPackage pkg = packageRepository.findOne(pkgId);
        if (pkg != null) {
            LOGGER.info("find package, productId:" + pkg.getProduct().getProductId());
        }
        return pkg;
    }

    @Transactional(readOnly = true)
    public ProductDetailsDto getProductDetail(String productId) {

        Product product = productService.getProductBasicInfo(productId);
        ProductDetailsDto productDetailsDto = ConverterService.convert(product, ProductDetailsDto.class);
        productDetailsDto.setProductUserDto(ConverterService.convert(product.getUser(), ProductUserDto.class));
        UserPo user = productService.getUser(product.getWarrantId());
        String wrtrName = user == null ? null : user.getName();
        productDetailsDto.setGuaranteeInstitution(wrtrName);
        String wrtrNameShow = wrtrName;
        if(StringUtils.isNotBlank(product.getWarrantIdShow())
        		&& !StringUtils.equals(product.getWarrantIdShow(), product.getWarrantId())){
            UserPo user2 = productService.getUser(product.getWarrantIdShow());
            wrtrNameShow = user2 == null ? null : user2.getName();
        }
        productDetailsDto.setGuaranteeInstitutionShow(wrtrNameShow);
        // 房产抵押
        productDetailsDto.setProductMortgageResidentialDetailsDtoList(getMortgageResidentialList(productId));
        // 车辆抵押
        productDetailsDto.setProductMortgageVehicleDetailsDtoList(getMortgageVehicleList(productId));
        // 动产质押
        productDetailsDto.setProductPledgeDetailsDtoList(getPledgeList(productId));

        // 保证法人
        productDetailsDto.setProductWarrantEnterpriseDtoList(getProductWarrantEnterpriseList(productId));
        // 保证人
        productDetailsDto.setProductWarrantPersonDtoList(getProductWarrantPersonList(productId));

        // 资产
        productDetailsDto.setProductAssetDtoList(getAssertList(productId));
        // 负债
        productDetailsDto.setProductDebtDtoList(getProductDebtList(productId));
        
        if (product.getProductProviderInfo() != null) {
        	productDetailsDto.setWrtrCreditDesc(product.getProductProviderInfo().getDesc());
        	productDetailsDto.setWrtrCreditFile(product.getProductProviderInfo().getWrtrCreditFile());
		}

        return productDetailsDto;

    }

    /**
     * 融资包查询
     * 
     * @param searchDto
     * @param currentUserId
     * @param map
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<MarketItemDto> getFinancingMarketItems(final FinancingMarketSearchDto searchDto,
            final String currentUserId, final boolean isPlatformUser) {

        Pageable pageRequest = buildPageRequest(searchDto);

        Specification<ProductPackage> specification = new Specification<ProductPackage>() {

            @Override
            public Predicate toPredicate(Root<ProductPackage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                // 无论是否满额，根据结束日期计算T+1日，今天0点之前结束的不应该显示
                Expression<Boolean> notFullExpression = root.<EPackageStatus> get("status").in(
                        EPackageStatus.SUBSCRIBE, EPackageStatus.WAIT_SUBSCRIBE, EPackageStatus.WAIT_SIGN);
                expressions.add(cb.and(notFullExpression,
                        cb.greaterThanOrEqualTo(root.<Date> get("supplyEndTime"), getTodayDate())));

                expressions.add(cb.notEqual(root.<EAutoSubscribeStatus> get("autoSubscribeFlag"),
                        EAutoSubscribeStatus.ALLOW));

                // // 已满额，根据满额日期计算T+1日
                // Expression<Boolean> fullExpression = cb.equal(root.<EPackageStatus> get("status"),
                // EPackageStatus.WAIT_SIGN);
                // fullExpression = cb.and(fullExpression,
                // cb.greaterThanOrEqualTo(root.<Date> get("lastSubsTime"), getTodayDate()));
                // expressions.add(cb.or(notFullExpression, fullExpression));

                // 非平台用户，只能看到公开的或者定投到自己的行情
                if (!isPlatformUser) {
                    Expression<Boolean> openExpression = cb.equal(root.<String> get("aipFlag"), false);
                    Expression<Boolean> aipExpression = cb.equal(root.<String> get("aipFlag"), true);
                    Expression<Boolean> aipCloseExpression = cb.and(aipExpression,
                            cb.exists(getAipSubquery(query, cb, currentUserId, root.<String> get("id"))));
                    // 定投在定投结束时间后未满额，就变成公开
                    Expression<Boolean> aipOpenExpression = cb.and(aipExpression,
                            cb.lessThan(root.<Date> get("aipEndTime"), cb.currentDate()));

                    expressions.add(cb.or(openExpression, cb.or(aipCloseExpression, aipOpenExpression)));
                }

                if (searchDto != null) {
                    List<EPackageStatus> statusList = new ArrayList<EPackageStatus>();
                    if (searchDto.isTopurchase()) {
                        statusList.add(EPackageStatus.SUBSCRIBE);
                    }
                    if (searchDto.isPrerelease()) {
                        statusList.add(EPackageStatus.WAIT_SUBSCRIBE);
                    }
                    if (searchDto.isHasexpired()) {
                        statusList.add(EPackageStatus.WAIT_SIGN);
                    }
                    if (!statusList.isEmpty()) {
                        expressions.add(cb.and(root.<EPackageStatus> get("status").in(statusList)));
                    }
                    if(null != searchDto.getCreateDate()){
                        expressions.add(cb.lessThanOrEqualTo(root.get("supplyStartTime").as(Date.class), searchDto.getCreateDate()));
                    }
                }
                return predicate;
            }

            private Subquery<ProductPackage> getAipSubquery(CriteriaQuery<?> query, CriteriaBuilder cb, String userId,
                    Path<String> pkgId) {
                Subquery<ProductPackage> sq = query.subquery(ProductPackage.class);
                Root<ProductPackage> pkg = sq.from(ProductPackage.class);
                Join<ProductPackage, SubscribeGroup> packageAIP = pkg.join("aipGroups");
                Join<SubscribeGroup, SubscribeGroupUser> groupUser = packageAIP.join("users");
                return sq.select(pkg).where(
                        cb.and(cb.equal(pkg.get("id"), pkgId), cb.equal(groupUser.<String> get("userId"), userId)));
            }
        };

        Page<ProductPackage> packages = packageRepository.findAll(specification, pageRequest);

        DataTablesResponseDto<MarketItemDto> responseDtoList = new DataTablesResponseDto<MarketItemDto>();
        List<MarketItemDto> itemList = new ArrayList<MarketItemDto>();
        responseDtoList.setEcho(searchDto.getEcho());
        Map<String, MarketItemDto> map = resourceService.getAsMap();
        for (ProductPackage pkg : packages) {
            MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
            dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
                    && pkg.getAipEndTime().compareTo(new Date()) >= 0);
            dto.setProduct(ConverterService.convert(pkg.getProduct(), MarketProductDto.class));
            dto.getProduct().setRiskLevel(pkg.getProduct().getProductLevel());
            if (StringUtils.isNotBlank(pkg.getProduct().getWarrantId())) {
                Agency agency = serviceCenterService.getAgencyById(pkg.getProduct().getWarrantId());
                if (agency != null) {
                    dto.getProduct().setWarrantor(agency.getName());
                }
            }
            dto.setProgress(pkg.getSupplyAmount().divide(pkg.getPackageQuota(), 3, RoundingMode.DOWN));
            if (pkg.getStatus() == EPackageStatus.SUBSCRIBE) {
                MarketItemDto itemCache = map.get(pkg.getId());
                if (itemCache == null) {
                    LOGGER.info("package does NOT exists in cache: {}", pkg.getId());
                } else {
                    dto.setProgress(itemCache.getProgress());
                }
                if (dto.getProgress().compareTo(BigDecimal.ONE) >= 0) {
                	dto.setProgress(BigDecimal.ONE.setScale(3));
//    				dto.setStatus(EPackageStatus.WAIT_SIGN);
    			}
            }
            if (dto.getProduct() != null) {
                if (dto.getProduct().getWarrantyType() == EWarrantyType.MONITORASSETS
                        || dto.getProduct().getWarrantyType() == EWarrantyType.NOTHING) {
                    dto.getProduct().setWarrantor(null);
                }
            }
            itemList.add(dto);
        }
        responseDtoList.setData(itemList);
        responseDtoList.setTotalDisplayRecords(packages.getTotalElements());
        responseDtoList.setTotalRecords(packages.getTotalElements());
        return responseDtoList;
    }

	@Transactional(readOnly = true)
	public DataTablesResponseDto<MarketItemDto> getFinancingMarketHistoryItems(
			final FinancingMarketHistorySearchDto searchDto,
			final String currentUserId, final boolean isPlatformUser) {

		Pageable pageRequest = buildPageRequest(searchDto);

		Specification<ProductPackage> specification = new Specification<ProductPackage>() {

			@Override
			public Predicate toPredicate(Root<ProductPackage> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();

				Expression<Boolean> statusExpression = root
						.<EPackageStatus> get("status").in(
								EPackageStatus.WAIT_LOAD_APPROAL,
								EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM,
								EPackageStatus.PAYING, EPackageStatus.END);
				expressions.add(statusExpression);

				expressions.add(cb.notEqual(
						root.<EAutoSubscribeStatus> get("autoSubscribeFlag"),
						EAutoSubscribeStatus.ALLOW));

				// 非平台用户，只能看到公开的或者定投到自己的行情
				if (!isPlatformUser) {
					Expression<Boolean> nonAipExpression = cb.equal(
							root.<String> get("aipFlag"), false);
					Expression<Boolean> aipExpression = cb.equal(
							root.<String> get("aipFlag"), true);
					Expression<Boolean> fullAipExpression = cb.and(
							cb.equal(root.<String> get("supplyStartTime"),
									root.<String> get("aipStartTime")),
							cb.equal(root.<String> get("supplyEndTime"),
									root.<String> get("aipEndTime")));
					Expression<Boolean> aipGroupExpression = cb
							.exists(getAipSubquery(query, cb, currentUserId,
									root.<String> get("id")));

					expressions.add(cb.or(nonAipExpression, cb.and(
							aipExpression, cb.or(cb.not(fullAipExpression),
									aipGroupExpression))));
				}

				if (searchDto != null) {
					Date startTime = null;
					if (searchDto.getPeriod() == EHistoryPeriod.TWOWEEK) {
						startTime = DateUtils.addDays(getTodayDate(), -13);
					} else if (searchDto.getPeriod() == EHistoryPeriod.ONEMONTH) {
						startTime = DateUtils.addDays(getTodayDate(), -29);
					} else if (searchDto.getPeriod() == EHistoryPeriod.ONEWEEK) {
						startTime = DateUtils.addDays(getTodayDate(), -6);
					}
					if(searchDto.getPeriod() != EHistoryPeriod.ALL){
						expressions.add(cb.greaterThanOrEqualTo(
								root.get("supplyStartTime").as(Date.class),
								startTime));
					}
				}
				return predicate;
			}

			private Subquery<ProductPackage> getAipSubquery(
					CriteriaQuery<?> query, CriteriaBuilder cb, String userId,
					Path<String> pkgId) {
				Subquery<ProductPackage> sq = query
						.subquery(ProductPackage.class);
				Root<ProductPackage> pkg = sq.from(ProductPackage.class);
				Join<ProductPackage, SubscribeGroup> packageAIP = pkg
						.join("aipGroups");
				Join<SubscribeGroup, SubscribeGroupUser> groupUser = packageAIP
						.join("users");
				return sq.select(pkg).where(
						cb.and(cb.equal(pkg.get("id"), pkgId), cb.equal(
								groupUser.<String> get("userId"), userId)));
			}

		};

		Page<ProductPackage> packages = packageRepository.findAll(
				specification, pageRequest);

		DataTablesResponseDto<MarketItemDto> responseDtoList = new DataTablesResponseDto<MarketItemDto>();
		List<MarketItemDto> itemList = new ArrayList<MarketItemDto>();
		responseDtoList.setEcho(searchDto.getEcho());
		for (ProductPackage pkg : packages) {
			MarketItemDto dto = ConverterService.convert(pkg,
					MarketItemDto.class);
			dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
					&& pkg.getAipEndTime().compareTo(new Date()) >= 0);
			dto.setProduct(ConverterService.convert(pkg.getProduct(),
					MarketProductDto.class));
			dto.getProduct().setRiskLevel(pkg.getProduct().getProductLevel());
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(packages.getTotalElements());
		responseDtoList.setTotalRecords(packages.getTotalElements());
		return responseDtoList;
	}
	
	/**
	 * 两个参数 默认为无分页，最近一个月的数据
	 * @param currentUserId
	 * @param isPlatformUser
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesResponseDto<MarketItemDto> getFinancingMarketHistoryItems(
			final String currentUserId, final boolean isPlatformUser) {

		Sort sort = null ;
		Pageable pageRequest = new PageRequest( 0,200,sort);

		Specification<ProductPackage> specification = new Specification<ProductPackage>() {

			@Override
			public Predicate toPredicate(Root<ProductPackage> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();

				Expression<Boolean> statusExpression = root
						.<EPackageStatus> get("status").in(
								EPackageStatus.WAIT_LOAD_APPROAL,
								EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM,
								EPackageStatus.PAYING, EPackageStatus.END);
				expressions.add(statusExpression);

				expressions.add(cb.notEqual(
						root.<EAutoSubscribeStatus> get("autoSubscribeFlag"),
						EAutoSubscribeStatus.ALLOW));

				// 非平台用户，只能看到公开的或者定投到自己的行情
				if (!isPlatformUser) {
					Expression<Boolean> nonAipExpression = cb.equal(
							root.<String> get("aipFlag"), false);
					Expression<Boolean> aipExpression = cb.equal(
							root.<String> get("aipFlag"), true);
					Expression<Boolean> fullAipExpression = cb.and(
							cb.equal(root.<String> get("supplyStartTime"),
									root.<String> get("aipStartTime")),
							cb.equal(root.<String> get("supplyEndTime"),
									root.<String> get("aipEndTime")));
					Expression<Boolean> aipGroupExpression = cb
							.exists(getAipSubquery(query, cb, currentUserId,
									root.<String> get("id")));

					expressions.add(cb.or(nonAipExpression, cb.and(
							aipExpression, cb.or(cb.not(fullAipExpression),
									aipGroupExpression))));
				}

				Date startTime = DateUtils.addDays(getTodayDate(), -29);
				expressions.add(cb.greaterThanOrEqualTo(
						root.get("supplyStartTime").as(Date.class),
						startTime));
				return predicate;
			}

			private Subquery<ProductPackage> getAipSubquery(
					CriteriaQuery<?> query, CriteriaBuilder cb, String userId,
					Path<String> pkgId) {
				Subquery<ProductPackage> sq = query
						.subquery(ProductPackage.class);
				Root<ProductPackage> pkg = sq.from(ProductPackage.class);
				Join<ProductPackage, SubscribeGroup> packageAIP = pkg
						.join("aipGroups");
				Join<SubscribeGroup, SubscribeGroupUser> groupUser = packageAIP
						.join("users");
				return sq.select(pkg).where(
						cb.and(cb.equal(pkg.get("id"), pkgId), cb.equal(
								groupUser.<String> get("userId"), userId)));
			}

		};

		Page<ProductPackage> packages = packageRepository.findAll(
				specification, pageRequest);

		DataTablesResponseDto<MarketItemDto> responseDtoList = new DataTablesResponseDto<MarketItemDto>();
		List<MarketItemDto> itemList = new ArrayList<MarketItemDto>();
		responseDtoList.setEcho("");
		for (ProductPackage pkg : packages) {
			MarketItemDto dto = ConverterService.convert(pkg,
					MarketItemDto.class);
			dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
					&& pkg.getAipEndTime().compareTo(new Date()) >= 0);
			dto.setProduct(ConverterService.convert(pkg.getProduct(),
					MarketProductDto.class));
			dto.getProduct().setRiskLevel(pkg.getProduct().getProductLevel());
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(packages.getTotalElements());
		responseDtoList.setTotalRecords(packages.getTotalElements());
		return responseDtoList;
	}

    @Transactional(readOnly = true)
    public List<MarketItemDto> getAllItems() {

        List<ProductPackage> packages = packageRepository.findAllFetchProduct(EPackageStatus.SUBSCRIBE);

        List<MarketItemDto> itemList = new ArrayList<MarketItemDto>();
        for (ProductPackage pkg : packages) {
            MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
            dto.setProduct(ConverterService.convert(pkg.getProduct(), MarketProductDto.class));
            itemList.add(dto);
        }
        return itemList;
    }

	@Transactional(readOnly = true)
	public MarketItemDto getItemById(String packageId) {
		ProductPackage pkg = packageRepository.findPackageByPackageIdFetchProduct(EPackageStatus.SUBSCRIBE, packageId);
		if (pkg != null) {
			MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
			dto.setProduct(ConverterService.convert(pkg.getProduct(), MarketProductDto.class));
			return dto;
		}
		return null;
	}

    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {

        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

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
            if (sortColumn.equals("product.riskLevel")) {
                sortColumn = "product.productLevel";
            }
            if (sortColumn.equals("product.term")) {
                sortColumn = "product.termToDays";
            }
            if (sortColumn.equals("subscribeStartTimeStr")) {
                sortColumn = "supplyStartTime";
            }
            if (sortColumn.equals("packageQuotaStr")) {
                sortColumn = "packageQuota";
            }
            if (sortColumn.equals("product.ratePercentage")) {
                sortColumn = "product.rate";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.DESC, "product.apprDate"));
            sort = sort.and(new Sort(Direction.ASC, "id"));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    private Date getTodayDate() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    /**
     * Get product pledge list, 质押
     * 
     * @param productId
     * @return
     */
    private List<ProductPledgeDetailsDto> getPledgeList(String productId) {
        List<ProductPledge> productPledgeList = productService.getProductWarrantPledge(productId);
        List<ProductPledgeDetailsDto> productPledgeDetailsDtoList = new ArrayList<ProductPledgeDetailsDto>();
        for (ProductPledge productPledge : productPledgeList) {
            productPledgeDetailsDtoList.add(ConverterService.convert(productPledge, ProductPledgeDetailsDto.class));
        }
        return productPledgeDetailsDtoList;
    }

    /**
     * Get product mortgage vehicle list, 抵押车辆
     * 
     * @param productId
     * @return
     */
    private List<ProductMortgageVehicleDetailsDto> getMortgageVehicleList(String productId) {
        List<ProductMortgageVehicle> productMortgageVehicleList = productService
                .getProductWarrantMortgageForVE(productId);
        List<ProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList = new ArrayList<ProductMortgageVehicleDetailsDto>();
        for (ProductMortgageVehicle productMortgageVehicle : productMortgageVehicleList) {
            productMortgageVehicleDetailsDtoList.add(ConverterService.convert(productMortgageVehicle,
                    ProductMortgageVehicleDetailsDto.class));
        }
        return productMortgageVehicleDetailsDtoList;
    }

    /**
     * Get product mortgage residential list, 抵押房产
     * 
     * @param productId
     * @return
     */
    private List<ProductMortgageResidentialDetailsDto> getMortgageResidentialList(String productId) {
        List<MortgageResidential> productMortgageResidentialList = productService
                .getProductWarrantMortgageForRE(productId);
        List<ProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList = new ArrayList<ProductMortgageResidentialDetailsDto>();
        for (MortgageResidential mortgageResidential : productMortgageResidentialList) {
            productMortgageResidentialDetailsDtoList.add(ConverterService.convert(mortgageResidential,
                    ProductMortgageResidentialDetailsDto.class));
        }
        return productMortgageResidentialDetailsDtoList;
    }

    /**
     * Get product asset vehicle list, 资产
     * 
     * @param productId
     * @return
     */
    private List<ProductAssetDto> getAssertList(String productId) {
        List<ProductAsset> productAssetList = productService.getProductAsset(productId);
        List<ProductAssetDto> productAssetDtoList = new ArrayList<ProductAssetDto>();
        for (ProductAsset productAsset : productAssetList) {
        	ProductAssetDto dto = ConverterService.convert(productAsset, ProductAssetDto.class);
        	dto.setDtype(ConverterService.convert(
					productService.getDicByCode(
							EOptionCategory.ASSET_TYPE.getCode(),
							productAsset.getType()), DynamicOption.class));
            dto.setAssertAmount(NumberUtil.formatTenThousandUnitAmt(productAsset.getAssertAmount()));
            productAssetDtoList.add(dto);
        }
        return productAssetDtoList;
    }

    /**
     * Get product real estate assets list, 担保人
     * 
     * @param productId
     * @return
     */
    private List<ProductWarrantPersonDto> getProductWarrantPersonList(String productId) {
        List<ProductWarrantPerson> productWarrantList = productService.getProductWarrantPerson(productId);
        List<ProductWarrantPersonDto> productWarrantDtoList = new ArrayList<ProductWarrantPersonDto>();
        for (ProductWarrantPerson productWarrant : productWarrantList) {
        	ProductWarrantPersonDto dto = ConverterService.convert(productWarrant, ProductWarrantPersonDto.class);
        	dto.setName(MaskUtil.maskChinsesName(dto.getName()));
            productWarrantDtoList.add(dto);
        }
        return productWarrantDtoList;
    }

    /**
     * Get product real estate assets list, 担保法人
     * 
     * @param productId
     * @return
     */
    private List<ProductWarrantEnterpriseDto> getProductWarrantEnterpriseList(String productId) {
        List<ProductWarrantEnterprise> productWarrantList = productService.getProductWarrantEnterprise(productId);
        List<ProductWarrantEnterpriseDto> productWarrantDtoList = new ArrayList<ProductWarrantEnterpriseDto>();
        for (ProductWarrantEnterprise productWarrant : productWarrantList) {
        	ProductWarrantEnterpriseDto dto = ConverterService.convert(productWarrant, ProductWarrantEnterpriseDto.class);
        	dto.setEnterpriseName(MaskUtil.maskEnterpriseName(dto.getEnterpriseName()));
            productWarrantDtoList.add(dto);
        }
        return productWarrantDtoList;
    }

    /**
     * 
     * Description: 负债
     * 
     * @param productId
     * @return
     */
    private List<ProductDebtDto> getProductDebtList(String productId) {

        List<ProductDebt> productDebtList = productService.getProductDebt(productId);

        List<ProductDebtDto> productDebtDtoList = new ArrayList<ProductDebtDto>();
        for (ProductDebt productDebt : productDebtList) {
        	ProductDebtDto dto = ConverterService.convert(productDebt, ProductDebtDto.class);
            dto.setDebtAmount(NumberUtil.formatTenThousandUnitAmt(productDebt.getDebtAmount()));
            dto.setDtype(ConverterService.convert(
					productService.getDicByCode(
							EOptionCategory.DEBT_TYPE.getCode(),
							productDebt.getType()), DynamicOption.class));
            productDebtDtoList.add(dto);
        }
        return productDebtDtoList;
    }

}
