package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.fund.service.FinancierEntrustDepositService;
import com.hengxin.platform.fund.service.FinancierSigningService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.MarketProductDto;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.repository.SubscribeGroupRepository;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.converter.FinancingPackageConverter;
import com.hengxin.platform.product.converter.ProductFeeConverter;
import com.hengxin.platform.product.domain.CreditorRightsTransfer;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PackageContract;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.FinancingPackageLogDto;
import com.hengxin.platform.product.dto.FinancingPackageSearchDto;
import com.hengxin.platform.product.dto.FinancingTransactionSettingsDto;
import com.hengxin.platform.product.dto.PackageSumDto;
import com.hengxin.platform.product.dto.ProductFeeDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;
import com.hengxin.platform.product.enums.EPackageActionType;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ETransferStatus;
import com.hengxin.platform.product.repository.CreditorRightsTransferRepository;
import com.hengxin.platform.product.repository.FinancingPackageViewRepository;
import com.hengxin.platform.product.repository.PackageContractRepository;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductFeeRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;

/**
 * 产品包.
 * 
 * @author tiexiyu
 * 
 */
@Service
public class FinancingPackageService extends BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancingPackageService.class);
    private static final String MAX_DATE = "9999-12-31 23:59:59";
    private static final String MIN_DATE = "1970-01-01 00:00:00";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final BigDecimal BIGDECIMAL_HUNDRED = new BigDecimal(100);
    private static final String ID_FORMAT = "{0}{1}{2,number,00}";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final int SCALE = 20;

    @Autowired
    private FinancingResourceService resourceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SubscribeGroupRepository subscribeGroupRepository;

    @Autowired
    private ProductPackageRepository productPackageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFeeRepository productFeeRepository;

    @Autowired
    private FinancingPackageViewRepository financingPackageViewRepository;

    @Autowired
    private PackageSubscribesRepository packageSubscribesRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private FinancierEntrustDepositService financierEntrustDepositService;

    @Autowired
    private WarrantDepositService warrantDepositService;

    @Autowired
    private ProductFreezeService productFreezeService;

    @Autowired
    private PositionLotRepository positionLotRepository;

    @Autowired
    private FinancierSigningService financierSigningService;

    @Autowired
    private PackageContractRepository packageContractRepository;

    @Autowired
    private CreditorRightsTransferRepository creditorRightsTransferRepository;

    @Autowired
    private JobWorkService jobWorkService;

    @Autowired
    private MemberMessageService memberMessageService;
    
    private BigDecimal getUnitFaceValue(){
    	return SubscribeUtils.getUnitFaceValue();
    }

    /**
     * 
     * Description: 查询指定融资项目的融资包列表.
     * 
     * @param productId
     * @return list
     */
    @Transactional(readOnly = true)
    public List<ProductPackage> getProductPackageInfo(String productId) {
    	return productPackageRepository.findByProductId(productId);
    }

    /**
     * 
     * Description: 查询指定融资包详细内容.
     * 
     * @param packageId
     * @return
     */
    public ProductPackage getProductPackageById(String packageId) {
        return productPackageRepository.findOne(packageId);
    }

    /**
     * 
     * Description: 查询指定融资包的申购人数.
     * 
     * @param packageId
     * @return
     */
    public int getSupplyCountForPackage(String packageId) {
        List<BigDecimal> supplyCountByPkgId = packageSubscribesRepository.getSupplyCountByPkgId(packageId);
        if (supplyCountByPkgId != null && !supplyCountByPkgId.isEmpty()) {
            BigDecimal value = supplyCountByPkgId.get(0);
            if (value != null) {
                return value.intValue();
            }
        }
        return 0;
    }

    /**
     * Description: 拆包操作.
     * 
     * @param transDto
     * @param currentUserId
     */
    @Transactional
    public void createFinancingPackages(FinancingTransactionSettingsDto transDto, String currentUserId) {

        // 判断日终批量是否成功
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        Date currentDate = new Date();
        if (transDto == null) {
            return;
        }
        String productId = transDto.getProductId();
        List<ProductPackageDto> packageList = transDto.getPackageList();
        if (packageList == null || packageList.isEmpty()) {
            return;
        }
        List<ProductPackage> packages = new ArrayList<ProductPackage>();

        // 更新项目属性
        Product product = productRepository.findOne(productId);
        EProductStatus productStatus = productService.approveProduct(productId, null, product.getStatus(), true);
        product.setStatus(productStatus);
        product.setLastMntOpid(currentUserId);
        product.setLoanMrgnRt(transDto.getPerformanceBondRate().divide(BIGDECIMAL_HUNDRED));
        product.setLastMntTs(currentDate);
        product.setAvgPkgFlg(transDto.isAverage());
        product.setPublishDate(currentDate);
        productRepository.save(product);

        // 保存融资项目费用信息
        List<ProductFee> productFeeList = new ArrayList<ProductFee>();
        ProductFee productFee = null;
        for (ProductFeeDto productFeeDto : transDto.getProductFeeList()) {
            productFee = ConverterService.convert(productFeeDto, ProductFee.class, ProductFeeConverter.class);
            productFee.setProductId(productId);
            productFeeList.add(productFee);
        }
        productFeeRepository.save(productFeeList);

        // 判定融资项目是否由担保机构发起
        boolean isProdServ = StringUtils.isNotBlank(product.getWarrantId());
        BigDecimal appliedQuota = product.getAppliedQuota();

        // 解冻融资项目服务合同保证金
        UnReserveReq unReq = new UnReserveReq();
        unReq.setCurrOpId(currentUserId);
        unReq.setReserveJnlNo(product.getServFnrJnlNo());
        unReq.setTrxMemo("融资人：" + product.getApplUserId() + "解冻发布保证金");
        unReq.setUserId(product.getApplUserId());
        unReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        financierSigningService.refundAllEntrustDeposit(unReq);

        // 解冻融资项目担保保证金
        if (isProdServ) {
            UnReserveReq unReqWrtr = new UnReserveReq();
            unReqWrtr.setCurrOpId(currentUserId);
            unReqWrtr.setReserveJnlNo(product.getWrtrFnrJnlNo());
            unReqWrtr.setTrxMemo("融资服务商：" + product.getWarrantId() + "解冻担保保证金");
            unReqWrtr.setUserId(product.getWarrantId());
            unReqWrtr.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
            warrantDepositService.refundWarrantDeposit(unReqWrtr);
        }

        // 保存拆包后每个融资包的信息
        int count = 0;
        int packageListSize = packageList.size();
        BigDecimal servMrgnAmt = product.getServMrgnAmt();
        BigDecimal calculateWrtrMrgnAmount = BigDecimal.ZERO;
        if (isProdServ) {
            calculateWrtrMrgnAmount = productFreezeService.calculateWrtrMrgnAmount(product);
        }
        BigDecimal totalServTrxAmt = BigDecimal.ZERO;
        BigDecimal totalwrtrTrxAmt = BigDecimal.ZERO;
        for (ProductPackageDto packageDto : packageList) {
            ProductPackage productPackage = ConverterService.convert(packageDto, ProductPackage.class,
                    FinancingPackageConverter.class);
            productPackage.setProductId(productId);
            productPackage.setProduct(product);
            productPackage.setCreateOperatorId(currentUserId);
            productPackage.setCreateTime(currentDate);
            productPackage.setLastOperatorId(currentUserId);

            // 如果勾选即时发布，直接申购中,预发布时间和申购时间都为当前时间
            if (packageDto.getInstantPublish()) {
                Date date = DateUtils.getDate(DateUtils.formatDate(currentDate, DATE_FORMAT), DATE_FORMAT);
                productPackage.setPrePublicTime(date);
                productPackage.setSupplyStartTime(date);
                if (packageDto.getAipFlag()) {
                    productPackage.setAipStartTime(date);
                }
                productPackage.setStatus(EPackageStatus.SUBSCRIBE);
            }
            // 如果预发布时间==申购起始时间 && 申购起始时间==当前时间，状态为申购中
            else if (productPackage.getPrePublicTime().compareTo(productPackage.getSupplyStartTime()) == 0
                    && productPackage.getSupplyStartTime().compareTo(currentDate) == 0) {
                productPackage.setStatus(EPackageStatus.SUBSCRIBE);
            } else if (productPackage.getPrePublicTime().compareTo(currentDate) == 0) {
                productPackage.setStatus(EPackageStatus.WAIT_SUBSCRIBE);
            } else {
                productPackage.setStatus(EPackageStatus.PRE_PUBLISH);
            }

            productPackage.setIndex(packageDto.getIndex());
            productPackage.setFinancierId(product.getApplUserId());
            productPackage.setSupplyAmount(BigDecimal.ZERO);
            
            /** version:20150306 融资项目拆包时，在融资包名称后面加上"-1"、"-2" **/
            if(packageList.size() <= 1){
            	productPackage.setPackageName(product.getProductName());
            }else if(packageList.size() > 1){
            	productPackage.setPackageName(product.getProductName() + "-" + packageDto.getIndex());
            }
            
            /**
            	productPackage.setPackageName(product.getProductName()
                    + Numeric2ChineseString.numberArab2ChineseString(packageDto.getIndex()) + "期");
			**/

            BigDecimal servTrxAmt = BigDecimal.ZERO;
            BigDecimal wrtrTrxAmt = BigDecimal.ZERO;
            if (count < packageListSize - 1) {
                // 计算融资包服务合同保证金
                servTrxAmt = servMrgnAmt.multiply(packageDto.getPackageQuota())
                        .divide(appliedQuota, SCALE, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
                totalServTrxAmt = totalServTrxAmt.add(servTrxAmt);
                if (isProdServ) {
                    // 计算融资包担保保证金
                    wrtrTrxAmt = calculateWrtrMrgnAmount.multiply(packageDto.getPackageQuota())
                            .divide(appliedQuota, SCALE, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
                    totalwrtrTrxAmt = totalwrtrTrxAmt.add(wrtrTrxAmt);
                }
            } else {
                servTrxAmt = servMrgnAmt.subtract(totalServTrxAmt);
                if (isProdServ) {
                    wrtrTrxAmt = calculateWrtrMrgnAmount.subtract(totalwrtrTrxAmt);
                }
            }
            // 根据规则计算融资包ID，该值与融资包save成功时生成的值一致
            String bizId = MessageFormat.format(ID_FORMAT, product.getProductLevel().getCode(), productId, productPackage.getIndex());
            // 发布保证金流水号
            productPackage.setServFnrJnlNo(getServFnrJnlNo(servTrxAmt, currentUserId, product.getApplUserId(), bizId));
            if (isProdServ) {
                // 担保保证金流水号
                productPackage
                        .setWrtrFnrJnlNo(getWrtrFnrJnlNo(wrtrTrxAmt, currentUserId, product.getWarrantId(), bizId));
            }

            if (packageDto.getPackageQuota().compareTo(BigDecimal.ZERO) > 0) {
                productPackage.setUnit(packageDto.getPackageQuota().divide(getUnitFaceValue()).intValue());
            }
            productPackage.setUnitAmount(getUnitFaceValue());
            productPackage.setAipGroups(getAIPGroups(packageDto.getAipGroupIds()));
            packages.add(productPackage);
            count++;
        }
        Iterable<ProductPackage> savePkgs = productPackageRepository.save(packages);

        // 插入融资包生成日志
        List<ActionHistoryPo> packageHistoryList = new ArrayList<ActionHistoryPo>();
        ActionHistoryPo pkgHistroy = null;
        for (ProductPackage pkg : savePkgs) {
            sendStatusChangeMessage(product.getApplUserId(), pkg);
            pkgHistroy = new ActionHistoryPo();
            pkgHistroy.setEntityType(EntityType.PRODUCTPACKAGE);
            pkgHistroy.setEntityId(pkg.getId());
            pkgHistroy.setAction(ActionType.NEW);
            pkgHistroy.setOperateTime(currentDate);
            pkgHistroy.setOperateUser(currentUserId);
            pkgHistroy.setResult(ActionResult.PRODUCT_PACKAGE_SETTINGS.getText());
            packageHistoryList.add(pkgHistroy);

            if (pkg.getInstantPublish() || pkg.getStatus() == EPackageStatus.SUBSCRIBE) {
                // 修改resource pool中数据
                MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
                dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
                        && pkg.getAipEndTime().compareTo(new Date()) >= 0);
                dto.setProduct(ConverterService.convert(product, MarketProductDto.class));
                resourceService.addItem(dto, true);
            }
        }
        actionHistoryUtil.save(packageHistoryList);

        for (ProductPackage pkg : savePkgs) {
            // 添加即时发布的融资包日志
            if (pkg.getInstantPublish()) {
                pkgHistroy = new ActionHistoryPo();
                pkgHistroy.setEntityType(EntityType.PRODUCTPACKAGE);
                pkgHistroy.setEntityId(pkg.getId());
                pkgHistroy.setAction(ActionType.SUBSCRIBE);
                pkgHistroy.setOperateTime(addDateTime(currentDate));
                pkgHistroy.setOperateUser(currentUserId);
                pkgHistroy.setResult(ActionResult.PRODUCT_PACKAGE_CHANGE_TO_SUBSCRIBE.getText());
                actionHistoryUtil.save(pkgHistroy);
            }
        }

        // 融资项目发布成功日志
        actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.PUBLISH, ActionResult.PRODUCT_PUBLISH_SUCCESS);
    }

    /**
     * 
     * Description: 时间增加一秒
     * 
     * @param date
     * @return
     */
    private Date addDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, 1);
        return cal.getTime();
    }

    /**
     * 
     * Description: 担保保证金计算
     * 
     * @param servFnrJnlNo
     */
    private String getWrtrFnrJnlNo(BigDecimal wrtrTrxAmt, String currentUserId, String financierId, String bizId) {

        ReserveReq wrtrMrgnAmtReq = new ReserveReq();
        wrtrMrgnAmtReq.setCurrOpId(currentUserId);
        wrtrMrgnAmtReq.setUserId(financierId);
        wrtrMrgnAmtReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        wrtrMrgnAmtReq.setUseType(EFundUseType.POSTDEPOSIT);
        wrtrMrgnAmtReq.setTrxAmt(wrtrTrxAmt);
        wrtrMrgnAmtReq.setBizId(bizId);
        wrtrMrgnAmtReq.setTrxMemo(" 融资包冻结担保保证金");

        return warrantDepositService.payDeposit(wrtrMrgnAmtReq);
    }

    /**
     * 
     * Description: 发布保证金计算
     * 
     * @param trxAmt
     * @param currentUserId
     * @param financierId
     * @param bizId
     * @return
     */
    private String getServFnrJnlNo(BigDecimal trxAmt, String currentUserId, String financierId, String bizId) {
        ReserveReq servMrgnAmtReq = new ReserveReq();
        servMrgnAmtReq.setCurrOpId(currentUserId);
        servMrgnAmtReq.setUserId(financierId);
        servMrgnAmtReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        servMrgnAmtReq.setUseType(EFundUseType.POSTDEPOSIT);
        servMrgnAmtReq.setTrxAmt(trxAmt);
        servMrgnAmtReq.setBizId(bizId);
        servMrgnAmtReq.setTrxMemo("融资包冻结融资服务合同保证金");

        return financierEntrustDepositService.payDeposit(servMrgnAmtReq);
    }

    /**
     * 
     * Description:编辑交易参数设置
     * 
     * @param transDto
     * @param currentUserId
     */
    @Transactional
    public void updateTransactionSettings(FinancingTransactionSettingsDto transDto, String currentUserId) {

        // 判断日终批量是否成功
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }

        Date currentDate = new Date();
        if (transDto == null) {
            return;
        }
        String productId = transDto.getProductId();
        List<ProductPackageDto> packageList = transDto.getPackageList();
        if (packageList == null || packageList.isEmpty()) {
            return;
        }
        List<ProductPackage> packages = new ArrayList<ProductPackage>();

        // 更新融资项目信息
        Product product = productRepository.findOne(productId);
        product.setLastMntOpid(currentUserId);
        product.setLoanMrgnRt(transDto.getPerformanceBondRate().divide(BIGDECIMAL_HUNDRED));
        product.setLastMntTs(currentDate);
        productRepository.save(product);

        // 判断融资项目是否由担保机构发起
        boolean isProdServ = StringUtils.isNotBlank(product.getWarrantId());

        // 更新融资项目费用信息
        List<ProductFee> productFeeList = new ArrayList<ProductFee>();
        ProductFee productFee = null;
        for (ProductFeeDto productFeeDto : transDto.getProductFeeList()) {
            productFee = productFeeRepository.getFeeByProductIdAndFeeId(productId, productFeeDto.getFeeId());
            productFee.setFeeRt(productFeeDto.getFeeRt().divide(BIGDECIMAL_HUNDRED));
            productFee.setPayMethod(productFeeDto.getPayMethod());
            productFeeList.add(productFee);
        }
        productFeeRepository.save(productFeeList);

        List<ProductPackage> allPackages = productPackageRepository.findByProductId(productId);
        boolean resetJnrl = true;
        for (ProductPackage pp : allPackages) {
            if (pp.getStatus() != EPackageStatus.PRE_PUBLISH && pp.getStatus() != EPackageStatus.WAIT_SUBSCRIBE) {
                resetJnrl = false;
                break;
            }
        }

        // update financing package
        ProductPackage productPackage = null;
        BigDecimal appliedQuota = product.getAppliedQuota();
        int count = 0;
        int packageListSize = packageList.size();
        BigDecimal servMrgnAmt = product.getServMrgnAmt();
        BigDecimal calculateWrtrMrgnAmount = productFreezeService.calculateWrtrMrgnAmount(product);
        BigDecimal totalServTrxAmt = BigDecimal.ZERO;
        BigDecimal totalwrtrTrxAmt = BigDecimal.ZERO;
        EPackageStatus oldStatus = null;
        for (ProductPackageDto packageDto : packageList) {
            productPackage = productPackageRepository.findOne(packageDto.getId());
            oldStatus = productPackage.getStatus();
            Long versionCount = productPackage.getVersionCount();
            if (versionCount > packageDto.getVersionCount()) {
                throw new HibernateOptimisticLockingFailureException(new StaleStateException(""));
            }
            productPackage.setLastOperatorId(currentUserId);
            productPackage.setPackageQuota(packageDto.getPackageQuota());
            Date prePublishTime = formatDate(packageDto.getPrePublicTime());
            productPackage.setPrePublicTime(prePublishTime);
            Date supplyStartTime = formatDate(packageDto.getSupplyStartTime());
            productPackage.setSupplyStartTime(supplyStartTime);
            productPackage.setSupplyEndTime(formatDate(packageDto.getSupplyEndTime()));
            productPackage.setAipFlag(packageDto.getAipFlag());
            productPackage.setAipStartTime(formatDate(packageDto.getAipStartTime()));
            productPackage.setAipEndTime(formatDate(packageDto.getAipEndTime()));
            productPackage.setInstantPublish(packageDto.getInstantPublish());

            // 即时发布
            if (packageDto.getInstantPublish()) {
                Date date = DateUtils.getDate(DateUtils.formatDate(currentDate, DATE_FORMAT), DATE_FORMAT);
                productPackage.setPrePublicTime(date);
                productPackage.setSupplyStartTime(date);
                if (packageDto.getAipFlag()) {
                    productPackage.setAipStartTime(date);
                }
                productPackage.setStatus(EPackageStatus.SUBSCRIBE);
            } else if (productPackage.getStatus() == EPackageStatus.PRE_PUBLISH
                    || productPackage.getStatus() == EPackageStatus.WAIT_SUBSCRIBE) {
                if (supplyStartTime.compareTo(prePublishTime) == 0 && supplyStartTime.compareTo(currentDate) == 0) {
                    productPackage.setStatus(EPackageStatus.SUBSCRIBE);
                } else if (prePublishTime.compareTo(currentDate) == 0) {
                    productPackage.setStatus(EPackageStatus.WAIT_SUBSCRIBE);
                }
            }

            sendStatusChangeMessage(product.getApplUserId(), productPackage, oldStatus);

            // 解冻已有服务合同保证金
            if (resetJnrl) {
                String servFnrJnlNo = productPackage.getServFnrJnlNo();
                UnReserveReq req = new UnReserveReq();
                req.setReserveJnlNo(servFnrJnlNo);
                req.setTrxMemo("融资包" + productPackage.getId() + "服务合同保证金解冻");
                req.setUserId(product.getApplUserId());
                req.setCurrOpId(currentUserId);
                req.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
                financierSigningService.refundAllEntrustDeposit(req);

                if (isProdServ) {
                    // 解冻已有担保保证金
                    String wrtrFnrJnlNo = productPackage.getWrtrFnrJnlNo();
                    UnReserveReq wrtrReq = new UnReserveReq();
                    wrtrReq.setReserveJnlNo(wrtrFnrJnlNo);
                    wrtrReq.setTrxMemo("融资包" + productPackage.getId() + "担保保证金解冻");
                    wrtrReq.setUserId(product.getWarrantId());
                    wrtrReq.setCurrOpId(currentUserId);
                    wrtrReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
                    warrantDepositService.refundWarrantDeposit(wrtrReq);
                }

                BigDecimal servTrxAmt = BigDecimal.ZERO;
                BigDecimal wrtrTrxAmt = BigDecimal.ZERO;
                if (count < packageListSize - 1) {
                    // 计算融资包服务合同保证金
                    servTrxAmt = servMrgnAmt.multiply(packageDto.getPackageQuota())
                            .divide(appliedQuota, SCALE, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
                    totalServTrxAmt = totalServTrxAmt.add(servTrxAmt);
                    if (isProdServ) {
                        // 计算融资包担保保证金
                        wrtrTrxAmt = calculateWrtrMrgnAmount.multiply(packageDto.getPackageQuota())
                                .divide(appliedQuota, SCALE, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
                        totalwrtrTrxAmt = totalwrtrTrxAmt.add(wrtrTrxAmt);
                    }
                } else {
                    servTrxAmt = servMrgnAmt.subtract(totalServTrxAmt);
                    if (isProdServ) {
                        wrtrTrxAmt = calculateWrtrMrgnAmount.subtract(totalwrtrTrxAmt);
                    }
                }
                // 冻结更新后的服务合同保证金
                productPackage.setServFnrJnlNo(getServFnrJnlNo(servTrxAmt, currentUserId, product.getApplUserId(),
                        productPackage.getId()));
                if (isProdServ) {
                    // 冻结更新后的担保保证金
                    productPackage.setWrtrFnrJnlNo(getWrtrFnrJnlNo(wrtrTrxAmt, currentUserId, product.getWarrantId(),
                            productPackage.getId()));
                }
            }
            if (packageDto.getPackageQuota().compareTo(BigDecimal.ZERO) > 0) {
                productPackage.setUnit(packageDto.getPackageQuota().divide(getUnitFaceValue()).intValue());
            }
            if (EAutoSubscribeStatus.DOESNOT == productPackage.getAutoSubscribeFlag()) {
                if (packageDto.getAutoSubscribeFlag()) {
                    productPackage.setAutoSubscribeFlag(EAutoSubscribeStatus.ALLOW);
                } else if (!packageDto.getAutoSubscribeFlag()) {
                    productPackage.setAutoSubscribeFlag(EAutoSubscribeStatus.DOESNOT);
                }
            }
            productPackage.setAipGroups(getAIPGroups(packageDto.getAipGroupIds()));
            productPackage.setLastTime(currentDate);
            packages.add(productPackage);

        }
        List<ProductPackage> savePkgs = productPackageRepository.save(packages);

        // 添加修改日志
        List<ActionHistoryPo> packageHistoryList = new ArrayList<ActionHistoryPo>();
        ActionHistoryPo pkgHistroy = null;
        for (ProductPackage pkg : savePkgs) {
            pkgHistroy = new ActionHistoryPo();
            pkgHistroy.setEntityType(EntityType.PRODUCTPACKAGE);
            pkgHistroy.setEntityId(pkg.getId());
            pkgHistroy.setAction(ActionType.EDIT);
            pkgHistroy.setOperateTime(currentDate);
            pkgHistroy.setOperateUser(currentUserId);
            pkgHistroy.setResult(ActionResult.PRODUCT_PACKAGE_SETTINGS_UPDATE.getText());
            packageHistoryList.add(pkgHistroy);
            MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
            dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
            		&& pkg.getAipEndTime().compareTo(new Date()) >= 0);
            dto.setProduct(ConverterService.convert(product, MarketProductDto.class));
            // 修改resource pool中数据
            if (pkg.getInstantPublish()) {
                resourceService.addItem(dto, true);
            } else if (pkg.getStatus() == EPackageStatus.SUBSCRIBE) {
                resourceService.addItem(dto, false);
            }
        }
        actionHistoryUtil.save(packageHistoryList);
    }

    /**
     * 
     * Description: 格式化日期
     * 
     * @param date
     * @return
     */
    private Date formatDate(String date) {
        return DateUtils.getDate(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * Description: 查询定投组详情
     * 
     * @param groupIds
     * @param packageId
     * @param currentUser
     * @return
     */
    private List<SubscribeGroup> getAIPGroups(String groupIds) {
        if (StringUtils.isBlank(groupIds)) {
            return null;
        }
        List<SubscribeGroup> aipGroups = new ArrayList<SubscribeGroup>();
        String[] groupIdArray = groupIds.split(",");
        for (String groupId : groupIdArray) {
            SubscribeGroup subscribeGroup = subscribeGroupRepository.findOne(Integer.valueOf(groupId));
            aipGroups.add(subscribeGroup);
        }
        return aipGroups;
    }

    /**
     * Description: 分页查询融资包列表
     * 
     * @param searchDto
     * @param currentUser
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<FinancingPackageView> getPackageListForFinancier(FinancingPackageSearchDto searchDto,
            final String currentUser, final boolean isFinancier) {
        Pageable pageable = buildPageRequest2(searchDto);
        final String keyword = searchDto.getKeyword();
        final EPackageStatus packageStatus = searchDto.getPackageStatus();
        final Date createTime = searchDto.getCreateTime();
        Specification<FinancingPackageView> spec = new Specification<FinancingPackageView>() {
            @Override
            public Predicate toPredicate(Root<FinancingPackageView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (isFinancier) {
                    expressions.add(cb.equal(root.<String> get("financierId"), currentUser));
                } else {
                    expressions.add(cb.equal(root.<String> get("watrid"), currentUser));
                }
                if (StringUtils.isNotBlank(keyword)) {
                    Predicate pre = cb.disjunction();
                    List<Expression<Boolean>> expr = pre.getExpressions();
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.PACKAGE_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get("id")), "%" + keyword.trim().toLowerCase() + "%"));

                    if (!isFinancier) {
                        expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.FINANCIER_NAME)), "%"
                                + keyword.trim().toLowerCase() + "%"));
                    } else {
                        expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.WRTR_NAME)), "%" + keyword.trim().toLowerCase()
                                + "%"));
                    }
                    expressions.add(cb.or(pre));
                }
                if (packageStatus != EPackageStatus.NULL) {
                    expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), packageStatus));
                }
                /* fetch the records which are before current time. */
                if (createTime != null) {
                	expressions.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), createTime));
                }
                return predicate;
            }
        };
        return financingPackageViewRepository.findAll(spec, pageable);
    }

    /**
     * Description:分页查询平台可看的融资包列表
     * 
     * @param searchDto
     * @param currentUser
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<FinancingPackageView> getPackageListForPlatform(final FinancingPackageSearchDto searchDto,
            String currentUser) {
        Pageable pageable = buildPageRequest(searchDto);
        final String keyword = searchDto.getKeyword();
        final EPackageStatus packageStatus = searchDto.getPackageStatus();
        Specification<FinancingPackageView> spec = new Specification<FinancingPackageView>() {
            @Override
            public Predicate toPredicate(Root<FinancingPackageView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (StringUtils.isNotBlank(keyword)) {
                    Predicate pre = cb.disjunction();
                    List<Expression<Boolean>> expr = pre.getExpressions();
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.PACKAGE_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get("id")), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.WRTR_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.FINANCIER_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expressions.add(cb.or(pre));
                }
                
                String startDate = searchDto.getStartDate();
                String endDate = searchDto.getEndDate();
                Date signStart = DateUtils.getDate(MIN_DATE, YYYY_MM_DD_HH_MM_SS);
                Date signEnd = DateUtils.getDate(MAX_DATE, YYYY_MM_DD_HH_MM_SS);
                try {
                    if (StringUtils.isNotBlank(startDate)) {
                        signStart = DateUtils.getStartDate(DateUtils.getDate(startDate, YYYY_MM_DD));
                    }
                    if (StringUtils.isNotBlank(endDate)) {
                        signEnd = DateUtils.getEndDate(DateUtils.getDate(endDate, YYYY_MM_DD));
                    }
                } catch (ParseException e) {
                	LOGGER.error("Date ParseException {}", e);
                }

                if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
                    expressions.add(cb.between(root.<Date> get("signingDt"), signStart, signEnd));
                }

                String endStartDate = searchDto.getEndStartDate();
                String endEndDate = searchDto.getEndEndDate();
                Date endStart = DateUtils.getDate(MIN_DATE, YYYY_MM_DD_HH_MM_SS);
                Date endEnd = DateUtils.getDate(MAX_DATE, YYYY_MM_DD_HH_MM_SS);
                if (packageStatus != EPackageStatus.NULL) {//融资包状态不为空
                	if (StringUtils.isNotBlank(endStartDate) || StringUtils.isNotBlank(endEndDate)) {
                		//只要选择了:结清日期，那么status="E"，不再考虑融资包状态选择的值，与汇总数据保持一致
                        expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.END));
                    }else{
                    	expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), packageStatus));
                    }
                }
                try {
                    if (StringUtils.isNotBlank(endStartDate)) {
                        endStart = DateUtils.getStartDate(DateUtils.getDate(endStartDate, YYYY_MM_DD));
                    }
                    if (StringUtils.isNotBlank(endEndDate)) {
                        endEnd = DateUtils.getEndDate(DateUtils.getDate(endEndDate, YYYY_MM_DD));
                    }
                } catch (ParseException e) {
                	LOGGER.error("Date ParseException {}", e);
                }

                if (StringUtils.isNotBlank(endStartDate) || StringUtils.isNotBlank(endEndDate)) {
                    expressions.add(cb.between(root.<Date> get("lastModifyTs"), endStart, endEnd));
                    expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.END));
                }

                return predicate;
            }
        };
        return financingPackageViewRepository.findAll(spec, pageable);
    }

    /**
     * Description: 分页查询提前还款融资包列表
     * 
     * @param searchDto
     *            FinancingPackageSearchDto
     * @param currentUser
     *            String
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<FinancingPackageView> getPackagePaymentAdvanceList(final FinancingPackageSearchDto searchDto,
            String currentUser) {
        Pageable pageable = buildPageRequest2(searchDto);
        final String keyword = searchDto.getKeyword();
        Specification<FinancingPackageView> spec = new Specification<FinancingPackageView>() {
            @Override
            public Predicate toPredicate(Root<FinancingPackageView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (StringUtils.isNotBlank(keyword)) {
                    Predicate pre = cb.disjunction();
                    List<Expression<Boolean>> expr = pre.getExpressions();
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.PACKAGE_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get("id")), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.WRTR_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.FINANCIER_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expressions.add(cb.or(pre));
                }
                expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.PAYING));

                Subquery<String> sq = query.subquery(String.class);
                Root<PaymentSchedule> pRoot = sq.from(PaymentSchedule.class);

                Subquery<String> paymentSubQuery = query.subquery(String.class);
                Root<PaymentSchedule> paymentRoot = paymentSubQuery.from(PaymentSchedule.class);
                List<EPayStatus> overdueList = new ArrayList<EPayStatus>();
                overdueList.add(EPayStatus.OVERDUE);
                overdueList.add(EPayStatus.COMPENSATING);
                overdueList.add(EPayStatus.COMPENSATORY);
                overdueList.add(EPayStatus.CLEARED);
                paymentSubQuery.select(paymentRoot.<String> get(LiteralConstant.PACKAGE_ID)).where(
                        paymentRoot.<String> get(LiteralConstant.STATUS).in(overdueList),
                        cb.equal(paymentRoot.<String> get(LiteralConstant.PACKAGE_ID), pRoot.<String> get(LiteralConstant.PACKAGE_ID)));

                sq.select(pRoot.<String> get(LiteralConstant.PACKAGE_ID))
                        .where(cb.and(cb.equal(pRoot.get(LiteralConstant.PACKAGE_ID), root.<String> get("id"))),
                                cb.equal(pRoot.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.NORMAL),
                                cb.and(cb.not(cb.exists(paymentSubQuery)))).groupBy(pRoot.<String> get(LiteralConstant.PACKAGE_ID))
                        .having(cb.greaterThan(cb.count(pRoot.<String> get(LiteralConstant.PACKAGE_ID)), 1L));
                expressions.add(cb.exists(sq));
                return predicate;
            }
        };
        return financingPackageViewRepository.findAll(spec, pageable);
    }

    /**
     * 
     * Description: 分页查询融资包列表
     * 
     * @param searchDto
     *            FinancingPackageSearchDto
     * @param currentUser
     *            String
     * @param type
     *            EPackageActionType
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<FinancingPackageView> getPackageListForAction(final FinancingPackageSearchDto searchDto,
            String currentUser, final EPackageActionType type) {
        Pageable pageable = buildPageRequest2(searchDto);
        final String keyword = searchDto.getKeyword();
        Specification<FinancingPackageView> spec = new Specification<FinancingPackageView>() {
            @Override
            public Predicate toPredicate(Root<FinancingPackageView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (StringUtils.isNotBlank(keyword)) {
                    Predicate pre = cb.disjunction();
                    List<Expression<Boolean>> expr = pre.getExpressions();
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.PACKAGE_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get("id")), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.WRTR_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(root.<String> get(LiteralConstant.FINANCIER_NAME)), "%" + keyword.trim().toLowerCase()
                            + "%"));
                    expressions.add(cb.or(pre));
                }
                // 放款审批
                if (type == EPackageActionType.APPROVELOAN) {
                    if (searchDto.getPackageStatus() == EPackageStatus.WAIT_LOAD_APPROAL) {
                        expressions
                                .add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.WAIT_LOAD_APPROAL));
                    } else {
                        Date dateStart = null;
                        Date dateEnd = null;
                        try {
                            dateStart = DateUtils.getStartDate(new Date());
                            dateEnd = DateUtils.getEndDate(new Date());
                        } catch (ParseException e) {
                            LOGGER.debug("Parse date failed.", e);
                        }
                        expressions.add(cb.between(root.<Date> get("loanApprTs"), dateStart, dateEnd));
                    }
                    if (StringUtils.isNotBlank(searchDto.getStartDate())
                            || StringUtils.isNotBlank(searchDto.getEndDate())) {
                        Date beginDateTimme = null;
                        Date lastDateTime = null;
                        if (StringUtils.isNotBlank(searchDto.getStartDate())) {
                            Date startDate = DateUtils.getDate(searchDto.getStartDate(), YYYY_MM_DD);
                            try {
                                beginDateTimme = DateUtils.getStartDate(startDate);
                            } catch (ParseException e) {
                                LOGGER.debug("Parse date failed.", e);
                            }
                        } else {
                            beginDateTimme = DateUtils.getDate(MIN_DATE, YYYY_MM_DD_HH_MM_SS);
                        }

                        if (StringUtils.isNotBlank(searchDto.getEndDate())) {
                            Date endDate = DateUtils.getDate(searchDto.getEndDate(), YYYY_MM_DD);
                            try {
                                lastDateTime = DateUtils.getEndDate(endDate);
                            } catch (ParseException e) {
                                LOGGER.debug("Parse date failed.", e);
                            }
                        } else {
                            lastDateTime = DateUtils.getDate(MAX_DATE, YYYY_MM_DD_HH_MM_SS);
                        }
                        if (StringUtils.isNotBlank(searchDto.getStartDate())
                                || StringUtils.isNotBlank(searchDto.getEndDate())) {
                            expressions.add(cb.between(root.<Date> get("signingDt"), beginDateTimme, lastDateTime));
                        }
                    }
                }
                // 放款确认
                else if (type == EPackageActionType.APPROVELOAN_CONFIRM) {
                    if (searchDto.getPackageStatus() == EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM) {
                        expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS),
                                EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM));
                    } else {
                        Date dateStart = null;
                        Date dateEnd = null;
                        try {
                            dateStart = DateUtils.getStartDate(new Date());
                            dateEnd = DateUtils.getEndDate(new Date());
                        } catch (ParseException e) {
                            LOGGER.debug("Parse date failed.", e);
                        }
                        expressions.add(cb.between(root.<Date> get("loanTs"), dateStart, dateEnd));
                    }
                    if (StringUtils.isNotBlank(searchDto.getStartDate())
                            || StringUtils.isNotBlank(searchDto.getEndDate())) {
                        Date beginDateTimme = null;
                        Date lastDateTime = null;
                        if (StringUtils.isNotBlank(searchDto.getStartDate())) {
                            Date startDate = DateUtils.getDate(searchDto.getStartDate(), YYYY_MM_DD);
                            try {
                                beginDateTimme = DateUtils.getStartDate(startDate);
                            } catch (ParseException e) {
                                LOGGER.debug("Parse date failed.", e);
                            }
                        } else {
                            beginDateTimme = DateUtils.getDate(MIN_DATE, YYYY_MM_DD_HH_MM_SS);
                        }

                        if (StringUtils.isNotBlank(searchDto.getEndDate())) {
                            Date endDate = DateUtils.getDate(searchDto.getEndDate(), YYYY_MM_DD);
                            try {
                                lastDateTime = DateUtils.getEndDate(endDate);
                            } catch (ParseException e) {
                                LOGGER.debug("Parse date failed.", e);
                            }
                        } else {
                            lastDateTime = DateUtils.getDate(MAX_DATE, YYYY_MM_DD_HH_MM_SS);
                        }
                        expressions.add(cb.between(root.<Date> get("signingDt"), beginDateTimme, lastDateTime));
                    }
                }
                // 撤单
                else if (type == EPackageActionType.WITHDRAW) {
                    Predicate predicate2 = cb.disjunction();
                    List<Expression<Boolean>> expressions2 = predicate2.getExpressions();
                    expressions2.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.SUBSCRIBE));
                    expressions2.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.WAIT_SIGN));
                    expressions.add(cb.or(predicate2));
                }
                // 手工还款
                else if (type == EPackageActionType.MANUALPAY) {
                    expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.PAYING));
                    Subquery<String> sq = query.subquery(String.class);
                    Root<PaymentSchedule> pRoot = sq.from(PaymentSchedule.class);
                    Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
                    Date startDate = null;
                    Date endDate = null;
                    try {
                        startDate = DateUtils.getStartDate(currentWorkDate);
                        endDate = DateUtils.getEndDate(currentWorkDate);
                    } catch (ParseException e) {
                        LOGGER.debug("Parse date failed.", e);
                    }
                    Subquery<String> paymentSubQuery = query.subquery(String.class);
                    Root<PaymentSchedule> paymentRoot = paymentSubQuery.from(PaymentSchedule.class);
                    List<EPayStatus> overdueList = new ArrayList<EPayStatus>();
                    overdueList.add(EPayStatus.OVERDUE);
                    overdueList.add(EPayStatus.COMPENSATING);
                    overdueList.add(EPayStatus.COMPENSATORY);
                    overdueList.add(EPayStatus.CLEARED);
                    paymentSubQuery.select(paymentRoot.<String> get(LiteralConstant.PACKAGE_ID)).where(
                            paymentRoot.<String> get(LiteralConstant.STATUS).in(overdueList),
                            cb.equal(paymentRoot.<String> get(LiteralConstant.PACKAGE_ID), pRoot.<String> get(LiteralConstant.PACKAGE_ID)));

                    sq.select(pRoot.<String> get(LiteralConstant.PACKAGE_ID)).where(
                            cb.and(cb.equal(pRoot.get(LiteralConstant.PACKAGE_ID), root.<String> get("id"))),
                            cb.between(pRoot.<Date> get("paymentDate"), startDate, endDate),
                            cb.equal(pRoot.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.NORMAL),
                            cb.and(cb.not(cb.exists(paymentSubQuery))));
                    expressions.add(cb.exists(sq));
                }
                // 终止申购
                else if (type == EPackageActionType.STOP) {
                    expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.SUBSCRIBE));
                } else if (type == EPackageActionType.PAYPRE) {
                    expressions.add(cb.equal(root.<EPackageStatus> get(LiteralConstant.STATUS), EPackageStatus.PAYING));
                }
                return predicate;
            }
        };
        Page<FinancingPackageView> allPackages = financingPackageViewRepository.findAll(spec, pageable);
        // 当前事务内加载数据
        if (type == EPackageActionType.MANUALPAY && allPackages.hasContent()) {
            for (FinancingPackageView fpv : allPackages) {
                List<PaymentSchedule> paymentScheduleList = fpv.getPaymentScheduleList();
                if (paymentScheduleList != null && !CollectionUtils.isNotEmpty(paymentScheduleList)) {
                    for (PaymentSchedule ps : paymentScheduleList) {
                        ps.getPackageId();
                    }
                }
            }
        }

        return allPackages;
    }

    /**
     * Description: 构建分页对象
     * 
     * @param requestDto
     *            DataTablesRequestDto
     * @return Pageable
     */
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
            if (StringUtils.isNotBlank(sortColumn) && "duration".equals(sortColumn)) {
                sortColumn = "durationDate";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            //增加按主表主键排序，解决翻页排序显示不正确的问题
            sort = sort.and(new Sort(Direction.fromString(sortDir), "id"));
        }
        return new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
    }
    


    /**
     * 
     * @param requestDto
     * @return
     */
    public Pageable buildPageRequest2(DataTablesRequestDto requestDto) {

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
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            Order order = sort.getOrderFor("id");
            if(order==null){
	            //增加按主表主键排序，解决翻页排序显示不正确的问题
	            sort = sort.and(new Sort(Direction.fromString(sortDir), "id"));
            }
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
     * Description: 构建分页对象
     * 
     * @param requestDto
     *            DataTablesRequestDto
     * @return Pageable
     */
    private Pageable buildInvestorPageRequest(DataTablesRequestDto requestDto) {
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
            if (StringUtils.isNotBlank(sortColumn) && "duration".equals(sortColumn)) {
                sortColumn = "durationDate";
            }
            if (StringUtils.isNotBlank(sortColumn) && "subsDate".equals(sortColumn)) {
                sortColumn = "subsDt";
            }
            if (StringUtils.isNotBlank(sortColumn) && "term".equals(sortColumn)) {
                sortColumn = "position.financingPackageView.durationDate";
            }
            if (StringUtils.isNotBlank(sortColumn) && "rate".equals(sortColumn)) {
                sortColumn = "position.financingPackageView.rate";
            }
            if (StringUtils.isNotBlank(sortColumn) && "signContractTime".equals(sortColumn)) {
                sortColumn = "position.financingPackageView.signContractTime";
            }
            if (StringUtils.isNotBlank(sortColumn) && "pkgId".equals(sortColumn)) {
                sortColumn = "position.financingPackageView.id";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            //增加按主表主键排序，解决翻页排序显示不正确的问题
            sort = sort.and(new Sort(Direction.fromString(sortDir), "lotId"));
        }
        return new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
    }

    /**
     * 
     * Description: 通过product Id获取融资包列表
     * 
     * @param productId
     *            String
     * @return List
     */
    @Transactional(readOnly = true)
    public List<ProductPackage> getProductPackageListByProductId(final String productId) {
        Sort sort = new Sort("id");

        Specification<ProductPackage> spec = new Specification<ProductPackage>() {
            @Override
            public Predicate toPredicate(Root<ProductPackage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                root.join("product");
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                expressions.add(cb.equal(root.<String> get("productId"), productId));
                return conjunction;
            }
        };
        List<ProductPackage> productPackageList = productPackageRepository.findAll(spec, sort);
        // 当前事务内加载数据,以避免懒加载问题
        if (productPackageList != null && !productPackageList.isEmpty()) {
            for (ProductPackage pkg : productPackageList) {
                List<SubscribeGroup> aipGroups = pkg.getAipGroups();
                if (aipGroups != null && !aipGroups.isEmpty()) {
                    pkg.getAipGroups().get(0).getCreateTs();
                }
            }
        }
        return productPackageList;
    }

    /**
     * Get financing package list for investor
     * 
     * @param userId
     * @param searchDto
     * @return List<PackageSubscribes>
     */
    @Transactional(readOnly = true)
    public Page<PositionLotPo> getFinancingPackageListByUserId(final String userId, FinancingPackageSearchDto searchDto) {
        Pageable pageable = buildInvestorPageRequest(searchDto);
        final String keyword = searchDto.getKeyword();
        final String startDate = searchDto.getStartDate();
        final String endDate = searchDto.getEndDate();
        final EPayStatus payStatus = searchDto.getPayStatus();
        final Date createTime = searchDto.getCreateTime();
        Specification<PositionLotPo> spec = new Specification<PositionLotPo>() {
            @Override
            public Predicate toPredicate(Root<PositionLotPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Object, Object> joinPosition = root.join("position");
                // joinPosition.join("financingPackageView");
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                expressions.add(cb.greaterThan(root.<Integer> get("unit"), 0));
                expressions.add(cb.equal(joinPosition.<String> get("userId"), userId));
                List<EPackageStatus> statusList = new ArrayList<EPackageStatus>();
                statusList.add(EPackageStatus.WAIT_LOAD_APPROAL);
                statusList.add(EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM);
                statusList.add(EPackageStatus.PAYING);
                expressions.add(joinPosition.<FinancingPackageView> get("financingPackageView")
                        .<EPackageStatus> get(LiteralConstant.STATUS).in(statusList));
                if (StringUtils.isNotBlank(startDate)) {
                    try {
                        expressions.add(cb
                                .greaterThanOrEqualTo(joinPosition.<FinancingPackageView> get("financingPackageView")
                                        .<Date> get("signingDt"), DateUtils.getStartDate(DateUtils.getDate(startDate,
                                        YYYY_MM_DD))));
                    } catch (ParseException e) {
                    	LOGGER.error("Date ParseException {}", e);
                    }
                }
                if (StringUtils.isNotBlank(endDate)) {
                    try {
                        expressions.add(cb
                                .lessThanOrEqualTo(joinPosition.<FinancingPackageView> get("financingPackageView")
                                        .<Date> get("signingDt"), DateUtils.getEndDate(DateUtils.getDate(endDate,
                                        YYYY_MM_DD))));
                    } catch (ParseException e) {
                    	LOGGER.error("Date ParseException {}", e);
                    }
                }
                if (StringUtils.isNotBlank(keyword)) {
                    Predicate pre = cb.disjunction();
                    List<Expression<Boolean>> expr = pre.getExpressions();
                    expr.add(cb.like(
                            cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView").<String> get(
                                    LiteralConstant.PACKAGE_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView")
                            .<String> get("id")), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(
                            cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView").<String> get(
                                    LiteralConstant.WRTR_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(
                            cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView").<String> get(
                                    LiteralConstant.FINANCIER_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expressions.add(cb.or(pre));
                }

                if (payStatus != EPayStatus.NULL) {
                    Subquery<PositionLotPo> sq = query.subquery(PositionLotPo.class);
                    Root<PositionLotPo> pRoot = sq.from(PositionLotPo.class);
                    Join<PositionLotPo, PositionPo> joins = pRoot.join("position", JoinType.INNER);
                    Join<PositionPo, FinancingPackageView> pf = joins.join("financingPackageView");
                    Join<FinancingPackageView, PaymentSchedule> payment = pf.join("paymentScheduleList");
                    sq.select(pRoot).where(cb.and(cb.equal(pRoot.get("positionId"), root.<String> get("positionId"))),
                            cb.equal(payment.<EPayStatus> get(LiteralConstant.STATUS), payStatus));

                    expressions.add(cb.exists(sq));
                }
                
                /* fetch the records which are before current time. */
                if (createTime != null) {
                	expressions.add(cb.lessThanOrEqualTo(root.get("createTs").as(Date.class), createTime));
                }
                return conjunction;
            }
        };
        Page<PositionLotPo> resultList = positionLotRepository.findAll(spec, pageable);
        return resultList;
    }
    
    @Transactional(readOnly = true)
    public List<PositionLotPo> getAllCreditors(final String userId, FinancingPackageSearchDto searchDto) {
    	
        final String keyword = searchDto.getKeyword();
        final String startDate = searchDto.getStartDate();
        final String endDate = searchDto.getEndDate();
        final EPayStatus payStatus = searchDto.getPayStatus();
        final Date createTime = searchDto.getCreateTime();
        
        Specification<PositionLotPo> spec = new Specification<PositionLotPo>() {
            @Override
            public Predicate toPredicate(Root<PositionLotPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Object, Object> joinPosition = root.join("position");
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                expressions.add(cb.greaterThan(root.<Integer> get("unit"), 0));
                expressions.add(cb.equal(joinPosition.<String> get("userId"), userId));

                List<EPackageStatus> statusList = new ArrayList<EPackageStatus>();
                statusList.add(EPackageStatus.WAIT_LOAD_APPROAL);
                statusList.add(EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM);
                statusList.add(EPackageStatus.PAYING);
                expressions.add(joinPosition.<FinancingPackageView> get("financingPackageView")
                        .<EPackageStatus> get(LiteralConstant.STATUS).in(statusList));
                
                if (StringUtils.isNotBlank(startDate)) {
                    try {
                        expressions.add(cb
                                .greaterThanOrEqualTo(joinPosition.<FinancingPackageView> get("financingPackageView")
                                        .<Date> get("signingDt"), DateUtils.getStartDate(DateUtils.getDate(startDate,
                                        YYYY_MM_DD))));
                    } catch (ParseException e) {
                    	LOGGER.error("Date ParseException {}", e);
                    }
                }
                if (StringUtils.isNotBlank(endDate)) {
                    try {
                        expressions.add(cb
                                .lessThanOrEqualTo(joinPosition.<FinancingPackageView> get("financingPackageView")
                                        .<Date> get("signingDt"), DateUtils.getEndDate(DateUtils.getDate(endDate,
                                        YYYY_MM_DD))));
                    } catch (ParseException e) {
                    	LOGGER.error("Date ParseException {}", e);
                    }
                }
                if (StringUtils.isNotBlank(keyword)) {
                    Predicate pre = cb.disjunction();
                    List<Expression<Boolean>> expr = pre.getExpressions();
                    expr.add(cb.like(
                            cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView").<String> get(
                                    LiteralConstant.PACKAGE_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView")
                            .<String> get("id")), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(
                            cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView").<String> get(
                                    LiteralConstant.WRTR_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expr.add(cb.like(
                            cb.lower(joinPosition.<FinancingPackageView> get("financingPackageView").<String> get(
                                    LiteralConstant.FINANCIER_NAME)), "%" + keyword.trim().toLowerCase() + "%"));
                    expressions.add(cb.or(pre));
                }

                if (payStatus != EPayStatus.NULL) {
                    Subquery<PositionLotPo> sq = query.subquery(PositionLotPo.class);
                    Root<PositionLotPo> pRoot = sq.from(PositionLotPo.class);
                    Join<PositionLotPo, PositionPo> joins = pRoot.join("position", JoinType.INNER);
                    Join<PositionPo, FinancingPackageView> pf = joins.join("financingPackageView");
                    Join<FinancingPackageView, PaymentSchedule> payment = pf.join("paymentScheduleList");
                    sq.select(pRoot).where(cb.and(cb.equal(pRoot.get("positionId"), root.<String> get("positionId"))),
                            cb.equal(payment.<EPayStatus> get(LiteralConstant.STATUS), payStatus));

                    expressions.add(cb.exists(sq));
                }
                
                /* fetch the records which are before current time. */
                if (createTime != null) {
                	expressions.add(cb.lessThanOrEqualTo(root.get("createTs").as(Date.class), createTime));
                }
                return conjunction;
            }
        };
        return positionLotRepository.findAll(spec);
    }

    /**
     * 查询投资人所有债权
     * 
     * @param userId
     * @return List<PackageSubscribes>
     */
    @Transactional(readOnly = true)
    public List<PositionLotPo> getAllCreditors(final String userId) {
        Specification<PositionLotPo> spec = new Specification<PositionLotPo>() {
            @Override
            public Predicate toPredicate(Root<PositionLotPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Object, Object> joinPosition = root.join("position");
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                expressions.add(cb.greaterThan(root.<Integer> get("unit"), 0));
                expressions.add(cb.equal(joinPosition.<String> get("userId"), userId));

                List<EPackageStatus> statusList = new ArrayList<EPackageStatus>();
                statusList.add(EPackageStatus.WAIT_LOAD_APPROAL);
                statusList.add(EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM);
                statusList.add(EPackageStatus.PAYING);
                expressions.add(joinPosition.<FinancingPackageView> get("financingPackageView")
                        .<EPackageStatus> get(LiteralConstant.STATUS).in(statusList));
                return conjunction;
            }
        };
        return positionLotRepository.findAll(spec);
    }

    /**
     * 
     * Description: 获取融资包还款信息
     * 
     * @param packageId
     * @return
     */
    @Transactional(readOnly = true)
    public List<PaymentSchedule> getCurrentPackageScheduleListByPkgId(final String packageId) {
        Sort sort = new Sort(Direction.ASC, "paymentDate");
        Specification<PaymentSchedule> spec = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                expressions.add(cb.equal(root.<String> get(LiteralConstant.PACKAGE_ID), packageId));
                expressions.add(root.<EPayStatus> get(LiteralConstant.STATUS).in(EPayStatus.NORMAL, EPayStatus.COMPENSATING,
                        EPayStatus.COMPENSATORY, EPayStatus.OVERDUE));
                return conjunction;
            }
        };
        return paymentScheduleRepository.findAll(spec, sort);
    }

    /**
     * Description: 查询融资包详情
     * 
     * @param packageId
     * @return
     */
    @Transactional(readOnly = true)
    public FinancingPackageView getPackageDetails(String packageId) {
        return financingPackageViewRepository.findOne(packageId);
    }

    /**
     * Description: 查询融资包模板信息
     * 
     * @param packageId
     * @return
     */
    @Transactional(readOnly = true)
    public PackageContract getPackageContractDetails(String packageId) {
        return packageContractRepository.findOne(packageId);
    }

    /**
     * Description: 查询融资包申购信息
     * 
     * @param packageId
     * @param searchDto
     * @return
     */
    @Transactional(readOnly = true)
    public Page<PackageSubscribes> getPackageSubscribesByPackageId(final String pkgId,
            final FinancingPackageLogDto searchDto) {
        Specification<PackageSubscribes> spec = new Specification<PackageSubscribes>() {
            @Override
            public Predicate toPredicate(Root<PackageSubscribes> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("pkgId"), pkgId));
                return predicate;
            }
        };
        Pageable pageable = buildSubscribesPageRequest(searchDto);
        Page<PackageSubscribes> productPackageList = packageSubscribesRepository.findAll(spec, pageable);
        // to load user info
        if (productPackageList.hasContent()) {
            for (PackageSubscribes ps : productPackageList) {
                ps.getUser().getUserId();
            }
        }
        return productPackageList;
    }

    /**
     * 
     * Description: 构建分页对象
     * 
     * @param requestDto
     * @return Pageable
     */
    private Pageable buildSubscribesPageRequest(DataTablesRequestDto requestDto) {
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
            if (sortColumn.equals("supplyAmount")) {
                sortColumn = "unit";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
     * 
     * Description: 查询融资包还款总期数
     * 
     * @param pkgId
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer getPaymentScheduleCountByPackageId(final String pkgId) {
        Specification<PaymentSchedule> spec = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (StringUtils.isNotBlank(pkgId)) {
                    expressions.add(cb.like(cb.lower(root.<String> get(LiteralConstant.PACKAGE_ID)), "%" + pkgId.toLowerCase() + "%"));
                }
                return predicate;
            }
        };
        return (int) paymentScheduleRepository.count(spec);
    }

    /**
     * 
     * Description: 头寸份额
     * 
     * @param lotId
     * @return
     */
    @Transactional(readOnly = true)
    public PositionLotPo getPositionLot(final String lotId) {
        return positionLotRepository.findOne(lotId);
    }

    /**
     * 
     * Description: 融资包申购记录
     * 
     * @param pkgId
     * @return
     */
    @Transactional(readOnly = true)
    public List<PackageSubscribes> getPackageSubscribesByPackageId(String pkgId) {
        return packageSubscribesRepository.getSubsByPkgId(pkgId);
    }

    @Transactional(readOnly = true)
    public List<ProductPackage> getProductPackagesStatusNotIn(List<EPackageStatus> statusList) {
        return productPackageRepository.findByStatusNotIn(statusList);
    }

    @Transactional(readOnly = true)
    public CreditorRightsTransfer getCreditorRightsTransfer(String lotId) {
        return creditorRightsTransferRepository.findByLotIdAndStatus(lotId, ETransferStatus.ACTIVE.getCode());
    }

    /**
     * 判断交易状态是否变化，变化的需要发送提醒
     * 
     * @param userId
     * @param productPackage
     * @param oldStatus
     */
    private void sendStatusChangeMessage(String userId, ProductPackage productPackage, EPackageStatus oldStatus) {
        if (oldStatus != productPackage.getStatus()) {
            sendStatusChangeMessage(userId, productPackage);
        }
    }

    /**
     * 融资包状态变更发送提醒
     * 
     * @param userId
     * @param productPackage
     */
    public void sendStatusChangeMessage(String userId, ProductPackage productPackage) {
        String content = null;
        switch (productPackage.getStatus()) {
        case WAIT_SUBSCRIBE:
            content = "当前状态为待申购";
            break;
        case SUBSCRIBE:
            content = "已可以申购";
            break;
        default:
            return;
        }
        String messageKey = "product.parameter.message";
        memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, productPackage.getId(), content);
    }

    
	/**
	 * 融资包是否正在逾期
	 */
	public boolean isOverdue(final String pkgId) {

		Specification<PaymentSchedule> spec = new Specification<PaymentSchedule>() {
			@Override
			public Predicate toPredicate(Root<PaymentSchedule> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				List<EPayStatus> overdueList = new ArrayList<EPayStatus>();
				overdueList.add(EPayStatus.OVERDUE);
				overdueList.add(EPayStatus.COMPENSATING);
				overdueList.add(EPayStatus.COMPENSATORY);
				overdueList.add(EPayStatus.BADDEBT);
				expressions
						.add(cb.equal(root.<String> get(LiteralConstant.PACKAGE_ID), pkgId));
				expressions.add(root.<String> get(LiteralConstant.STATUS).in(overdueList));
				return predicate;
			}
		};
		return !paymentScheduleRepository.findAll(spec).isEmpty();

	}
	
	/**
     * 获取融资包汇总数据
     * 
     * @param searchDto
     * @return
     */
	@Transactional(readOnly = true)
    public PackageSumDto getPackageSum(final FinancingPackageSearchDto searchDto) {
		PackageSumDto dto = new PackageSumDto();
		
		StringBuffer bf = new StringBuffer();
		String keyword = searchDto.getKeyword().toLowerCase().trim();
		String status = null;
        final EPackageStatus packageStatus = searchDto.getPackageStatus();
        if (packageStatus != EPackageStatus.NULL) {
            status = packageStatus.getCode();
        }
        String startDate = searchDto.getStartDate();
        String endDate = searchDto.getEndDate();
        int signed = 0;
        if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
            signed = 1;//选择了签约日期中的任意一个，则按签约日期字段过滤
        }
        
        Date signStart = DateUtils.getDate(MIN_DATE, YYYY_MM_DD_HH_MM_SS);
        Date signEnd = DateUtils.getDate(MAX_DATE, YYYY_MM_DD_HH_MM_SS);
        try {
            if (StringUtils.isNotBlank(startDate)) {
                signStart = DateUtils.getStartDate(DateUtils.getDate(startDate, YYYY_MM_DD));
            }
            if (StringUtils.isNotBlank(endDate)) {
                signEnd = DateUtils.getEndDate(DateUtils.getDate(endDate, YYYY_MM_DD));
            }
        } catch (ParseException e) {
        	LOGGER.error("Date ParseException {}", e);
        }

        String endStartDate = searchDto.getEndStartDate();
        String endEndDate = searchDto.getEndEndDate();
        int ended = 0;
        if (StringUtils.isNotBlank(endStartDate) || StringUtils.isNotBlank(endEndDate)) {
        	ended = 1;//选择了结清日期中的任意一个，则按结清日期字段过滤
        }
        Date endStart = DateUtils.getDate(MIN_DATE, YYYY_MM_DD_HH_MM_SS);
        Date endEnd = DateUtils.getDate(MAX_DATE, YYYY_MM_DD_HH_MM_SS);
        try {
            if (StringUtils.isNotBlank(endStartDate)) {
                endStart = DateUtils.getStartDate(DateUtils.getDate(endStartDate, YYYY_MM_DD));
            }
            if (StringUtils.isNotBlank(endEndDate)) {
                endEnd = DateUtils.getEndDate(DateUtils.getDate(endEndDate, YYYY_MM_DD));
            }
        } catch (ParseException e) {
        	LOGGER.error("Date ParseException {}", e);
        }

        if (StringUtils.isNotBlank(endStartDate) || StringUtils.isNotBlank(endEndDate)) {
             status = EPackageStatus.END.getCode();//只要选择了:结清日期，那么status="E"
        }
        
        if (StringUtils.isNotBlank(keyword)) {
        	bf.append("%");
        	bf.append(keyword);
        	bf.append("%");
        }
        String sumAll = this.financingPackageViewRepository.getSumAll(
        		DateUtils.formatDate(signStart, LiteralConstant.YYYYMMDD), 
        		DateUtils.formatDate(signEnd, LiteralConstant.YYYYMMDD), 
        		DateUtils.formatDate(endStart, LiteralConstant.YYYYMMDD), 
        		DateUtils.formatDate(endEnd, LiteralConstant.YYYYMMDD),
        		bf.toString(), status, signed, ended);
        String[] ss = sumAll.split("@");
        dto.setSum_quota(new BigDecimal(ss[0]));
        dto.setSum_invstr(new BigDecimal(ss[1]));
        dto.setCount(ss[2]);
        return dto;
    }
	
	/**
	 * 融资包是否有历史逾期记录.
	 */
	public boolean isHistoryOverdue(final String pkgId) {
		List<PaymentSchedule> allowPaymentList = paymentScheduleRepository
                .getByPackageIdOrderBySequenceIdDesc(pkgId);
        BigDecimal forFeit = null;
        for (PaymentSchedule paymentSchedule : allowPaymentList) {
            forFeit = paymentSchedule.getPrincipalForfeit().add(paymentSchedule.getInterestForfeit()).add(paymentSchedule.getFeeForfeit());
            if (forFeit.compareTo(BigDecimal.valueOf(0)) > 0) {
                return true;
            }
        }
        return false;
	}
	
}
