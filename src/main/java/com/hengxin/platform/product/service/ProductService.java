package com.hengxin.platform.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.entity.DynamicOptionPo;
import com.hengxin.platform.common.entity.SysFilePo;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.repository.DynamicOptionRepository;
import com.hengxin.platform.common.repository.FileRepository;
import com.hengxin.platform.common.repository.SystemParamRepository;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.service.ExtendJbpmService;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.FeePayStatePo;
import com.hengxin.platform.fund.enums.EFeeType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.UserPayFeeService;
import com.hengxin.platform.member.domain.ProductProviderInfo;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.repository.ProductProviderRepository;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.MortgageResidential;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductAsset;
import com.hengxin.platform.product.domain.ProductAttachment;
import com.hengxin.platform.product.domain.ProductContractTemplate;
import com.hengxin.platform.product.domain.ProductDebt;
import com.hengxin.platform.product.domain.ProductLib;
import com.hengxin.platform.product.domain.ProductMortgageVehicle;
import com.hengxin.platform.product.domain.ProductPledge;
import com.hengxin.platform.product.domain.ProductWarrantEnterprise;
import com.hengxin.platform.product.domain.ProductWarrantPerson;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductAssetRepository;
import com.hengxin.platform.product.repository.ProductAttachmentRepository;
import com.hengxin.platform.product.repository.ProductContractTemplateRepository;
import com.hengxin.platform.product.repository.ProductDebtRepository;
import com.hengxin.platform.product.repository.ProductLibRepository;
import com.hengxin.platform.product.repository.ProductMortgageVehicleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductPledgeRepository;
import com.hengxin.platform.product.repository.ProductRealEstateMortgageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.repository.ProductWarrantEnterpriseRepository;
import com.hengxin.platform.product.repository.ProductWarrantPersonRepository;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * ProductService.
 * 
 * @author tiexiyu
 * 
 */

@Service
public class ProductService extends BaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductService.class);

	private static final String SUBMIT = "SUBMIT";

	@Autowired
	private ExtendJbpmService extendJbpmService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductProviderRepository productProviderRepository;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ProductAssetRepository productAssetRepository;

	@Autowired
	private ProductAttachmentRepository productAttachmentRepository;

	@Autowired
	private ProductMortgageVehicleRepository productMortgageVehicleRepository;

	@Autowired
	private ProductPledgeRepository productPledgeRepository;

	@Autowired
	private ProductRealEstateMortgageRepository productRealEstateMortgageRepository;

	@Autowired
	private ProductWarrantEnterpriseRepository productWarrantERepository;

	@Autowired
	private ProductWarrantPersonRepository productWarrantPRepository;

	@Autowired
	private ProductDebtRepository productDebtRepository;

	@Autowired
	private ProductPackageRepository productPackageRepository;

	@Autowired
	private AcctService acctService;

	@Autowired
	private ProductContractTemplateRepository productContractTemplateRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SystemParamRepository systemParamRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private DynamicOptionRepository dynamicOptionRepository;

	@Autowired
	private ProductLibRepository libRepository;

	@Autowired
	private UserPayFeeService userPayFeeService;

	@Autowired
	private MemberMessageService memberMessageService;

	/**
	 * @return return the value of the var extendJbpmService
	 */
	public ExtendJbpmService getExtendJbpmService() {
		return extendJbpmService;
	}

	/**
	 * 
	 * Description: 检查担保机构.
	 * 
	 * @param userId
	 */
	private void checkCurrentWrtrUser(String userId) {
		ProductProviderInfo prodProvider = productProviderRepository
				.findOne(userId);
		if (prodProvider == null
				&& !securityContext
						.hasPermission(Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER)) {
			BizServiceException bizServiceException = new BizServiceException(
					EErrorCode.TECH_DATA_INVALID, "用户" + userId
							+ "没有申请融资项目的权限.");
			LOGGER.debug("", bizServiceException);
			throw bizServiceException;
		}
	}

	@Transactional
	public String saveFinanceApply(Product productApply, final String flag) {

		checkCurrentWrtrUser(productApply.getWarrantId());
		final String aproductId = productApply.getProductId();
		if (!StringUtils.isEmpty(aproductId)) {
			deleteApply(productApply.getProductId());
		}
		// jbpm process by martin
		if (productApply.getStatus() == null
				&& StringUtils.isNotEmpty(aproductId)) {
			EProductStatus productStatus = commitProduct(aproductId, null);
			productApply.setStatus(productStatus);
		}
		// end
		if (securityContext.isFinancier()) {
			productApply.setWarrantId(null);
			productApply.setWarrantIdShow(null);
		}
		String productId = saveProductBasicInfo(productApply);
		// jbpm process by martin
		if (productApply.getStatus() == null && StringUtils.isEmpty(aproductId)) {
			EProductStatus productStatus = commitProduct(productId, null);
			productApply.setStatus(productStatus);
		}
		// end
		List<ProductAsset> productAssetList = new ArrayList<ProductAsset>();

		for (int i = 0; i < productApply.getProductAssetList().size(); i++) {
			ProductAsset productAsset = productApply.getProductAssetList().get(
					i);
			productAsset.setProductId(productId);
			productAsset.setSeqId(i + 1);
			productAssetList.add(productAsset);

		}
		saveProductAsset(productAssetList);

		List<ProductDebt> productDebtList = new ArrayList<ProductDebt>();
		List<ProductDebt> debtList = productApply.getProductDebtList();

		for (int i = 0; i < productApply.getProductDebtList().size(); i++) {
			ProductDebt productDebt = debtList.get(i);
			productDebt.setProductId(productId);
			productDebt.setSeqId(i + 1);
			productDebtList.add(productDebt);

		}
		saveProductDebt(productDebtList);

		List<ProductMortgageVehicle> productMortgageVehicleList = new ArrayList<ProductMortgageVehicle>();

		for (int i = 0; i < productApply.getProductMortgageVehicleList().size(); i++) {
			ProductMortgageVehicle productMortgageVehicle = productApply
					.getProductMortgageVehicleList().get(i);
			productMortgageVehicle.setProductId(productId);
			productMortgageVehicle.setSeqId(i + 1);
			productMortgageVehicleList.add(productMortgageVehicle);

		}
		saveProductMortgageVehicle(productMortgageVehicleList);

		List<MortgageResidential> mortgageResidentialList = new ArrayList<MortgageResidential>();

		for (int i = 0; i < productApply.getProductMortgageResidentialList()
				.size(); i++) {
			MortgageResidential mortgageResidential = productApply
					.getProductMortgageResidentialList().get(i);
			mortgageResidential.setProductId(productId);
			mortgageResidential.setSequenceId(i + 1);
			mortgageResidentialList.add(mortgageResidential);

		}
		saveMortgageResidential(mortgageResidentialList);

		List<ProductPledge> productPledgeList = new ArrayList<ProductPledge>();

		for (int i = 0; i < productApply.getProductPledgeList().size(); i++) {
			ProductPledge productPledge = productApply.getProductPledgeList()
					.get(i);
			productPledge.setProductId(productId);
			productPledge.setSeqId(i + 1);
			productPledgeList.add(productPledge);

		}
		saveProductPledge(productPledgeList);

		List<ProductAttachment> productAttachmentList = new ArrayList<ProductAttachment>();

		for (int i = 0; i < productApply.getProductAttachmentList().size(); i++) {
			ProductAttachment productAttachment = productApply
					.getProductAttachmentList().get(i);
			productAttachment.setProductId(productId);
			productAttachment.setSeqId(i + 1);
			productAttachmentList.add(productAttachment);

		}
		saveProductAttachment(productAttachmentList);

		List<ProductWarrantEnterprise> productWarrantEnterpriseList = new ArrayList<ProductWarrantEnterprise>();
		for (int i = 0; i < productApply.getProductWarrantEnterpriseList()
				.size(); i++) {
			ProductWarrantEnterprise productWarrantEnterprise = productApply
					.getProductWarrantEnterpriseList().get(i);
			productWarrantEnterprise.setProductId(productId);
			productWarrantEnterprise.setSeqId(i + 1);
			productWarrantEnterpriseList.add(productWarrantEnterprise);

		}
		saveProductWarrantEnterprise(productWarrantEnterpriseList);

		List<ProductWarrantPerson> productWarrantPersonList = new ArrayList<ProductWarrantPerson>();

		for (int i = 0; i < productApply.getProductWarrantPersonList().size(); i++) {
			ProductWarrantPerson productWarrantPerson = productApply
					.getProductWarrantPersonList().get(i);
			productWarrantPerson.setProductId(productId);
			productWarrantPerson.setSeqId(i + 1);
			productWarrantPersonList.add(productWarrantPerson);

		}
		saveProductWarrantPerson(productWarrantPersonList);
		if (SUBMIT.equals(flag)) {
			actionHistoryUtil.save(EntityType.PRODUCT,
					productApply.getProductId(), ActionType.NEWPRODUCT, null);
		}
		return productId;

	}

	public FeePayStatePo findSeatFee(String userId) {
		return userPayFeeService.getFeePayState(userId, EFeeType.SEAT);
	}

	public DynamicOptionPo getDicByCode(String category, String code) {

		return dynamicOptionRepository.findByCategoryAndCodeAndEnabled(
				category, code, "Y");

	}

	private void deleteApply(String productId) {

		productAssetRepository.deleteByProductId(productId);

		productAttachmentRepository.deleteByProductId(productId);

		productMortgageVehicleRepository.deleteByProductId(productId);

		productPledgeRepository.deleteByProductId(productId);

		productRealEstateMortgageRepository.deleteByProductId(productId);

		productWarrantERepository.deleteByProductId(productId);

		productWarrantPRepository.deleteByProductId(productId);

		productDebtRepository.deleteByProductId(productId);

		productRepository.deleteByProductId(productId);
	}

	/**
	 * 
	 * Description: 审核基本信息.
	 * 
	 * @param productId
	 */
	public Product getProductBasicInfo(String productId) {
		Product prod = productRepository.findByProductId(productId);
		if(prod.getProductProviderInfo()==null && StringUtils.isNotBlank(prod.getWarrantIdShow())){
			ProductProviderInfo proServ = productProviderRepository.findByUserId(prod.getWarrantIdShow());
			prod.setProductProviderInfo(proServ);
		}
		return prod;
	}

	/**
	 * 
	 * Description: 反担保情况抵押产品房产.
	 * 
	 * @param productId
	 */
	public List<MortgageResidential> getProductWarrantMortgageForRE(
			String productId) {
		return productRealEstateMortgageRepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 反担保情况抵押产品车辆.
	 * 
	 * @param productId
	 */
	public List<ProductMortgageVehicle> getProductWarrantMortgageForVE(
			String productId) {
		return productMortgageVehicleRepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 反担保情况质押产品.
	 * 
	 * @param productId
	 */
	public List<ProductPledge> getProductWarrantPledge(String productId) {
		return productPledgeRepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 反担保情况保证法人.
	 * 
	 * @param productId
	 */
	public List<ProductWarrantEnterprise> getProductWarrantEnterprise(
			String productId) {
		return productWarrantERepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 反担保情况保证人.
	 * 
	 * @param productId
	 */
	public List<ProductWarrantPerson> getProductWarrantPerson(String productId) {
		return productWarrantPRepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 资产.
	 * 
	 * @param productService
	 */
	public List<ProductAsset> getProductAsset(String productId) {

		return productAssetRepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 资料库.
	 * 
	 * @param productService
	 */
	@Transactional
	public List<ProductAttachment> getProductFile(String productId) {
		List<ProductAttachment> attachments = productAttachmentRepository.findByProductId(productId);
		List<String> filesToBeDeleted = new ArrayList<String>();
		for (Iterator<ProductAttachment> ite = attachments.iterator(); ite.hasNext();) {
			ProductAttachment attachment = (ProductAttachment) ite.next();
			String fileId = attachment.getFile();
			SysFilePo file = this.fileRepository.findFileById(fileId);
			if (file != null) {
				attachment.setFilePo(file);
			} else {
				filesToBeDeleted.add(fileId);
				ite.remove();
			}
		}
		if (!filesToBeDeleted.isEmpty()) {
			for (String fileId : filesToBeDeleted) {
				this.productAttachmentRepository.deleteByFileId(fileId);
			}
		}
		return attachments;
	}

	public List<ProductDebt> getProductDebt(String productId) {
		return productDebtRepository.findByProductId(productId);
	}

	/**
	 * 
	 * Description: 保存基本信息.
	 * 
	 * @param productId
	 * @return
	 */
	private String saveProductBasicInfo(Product product) {
		productRepository.save(product);
		return product.getProductId();
	}

	/**
	 * 
	 * Description: 保存反担保情况抵押产品房产.
	 * 
	 * @param productId
	 */
	private void saveMortgageResidential(
			List<MortgageResidential> mortgageResidentialList) {
		productRealEstateMortgageRepository.save(mortgageResidentialList);
	}

	/**
	 * 
	 * Description: 保存反担保情况抵押产品车辆.
	 * 
	 * @param productId
	 */
	private void saveProductMortgageVehicle(
			List<ProductMortgageVehicle> productMortgageVehicleList) {
		productMortgageVehicleRepository.save(productMortgageVehicleList);
	}

	/**
	 * 
	 * Description: 保存反担保情况质押产品.
	 * 
	 * @param productId
	 */
	private void saveProductPledge(List<ProductPledge> productPledgeList) {
		productPledgeRepository.save(productPledgeList);
	}

	/**
	 * 
	 * Description: 保存反担保情况保证人.
	 * 
	 * @param productId
	 */
	private void saveProductWarrantPerson(
			List<ProductWarrantPerson> productWarrant) {
		productWarrantPRepository.save(productWarrant);
	}

	/**
	 * 
	 * Description: 保存反担保情况保证法人.
	 * 
	 * @param productId
	 */
	private void saveProductWarrantEnterprise(
			List<ProductWarrantEnterprise> productWarrant) {
		productWarrantERepository.save(productWarrant);
	}

	/**
	 * 
	 * Description: 保存资产车辆.
	 * 
	 * @param productService
	 */
	private void saveProductAsset(List<ProductAsset> productAssetList) {

		productAssetRepository.save(productAssetList);
	}

	/**
	 * 
	 * Description: 保存负债.
	 * 
	 * @param productService
	 */
	private void saveProductDebt(List<ProductDebt> productDebtList) {
		productDebtRepository.save(productDebtList);
	}

	/**
	 * 
	 * Description: 保存资料库.
	 * 
	 * @param productService
	 */
	private void saveProductAttachment(
			List<ProductAttachment> productAttachmentList) {
		productAttachmentRepository.save(productAttachmentList);
	}

	/**
	 * 
	 * Description: 根据交易账号获得名字.
	 * 
	 * @param accName
	 * @return
	 */
	public String getUserId(final String accName) {
		/*
		 * Specification<AcctPo> spec = new Specification<AcctPo>() {
		 * 
		 * @Override public Predicate toPredicate(Root<AcctPo> root,
		 * CriteriaQuery<?> query, CriteriaBuilder cb) { Predicate predicate =
		 * cb.conjunction(); List<Expression<Boolean>> expressions =
		 * predicate.getExpressions(); if (StringUtils.isNotBlank(accName)) {
		 * expressions .add(cb.like(cb.lower(root.<String> get("userId")), "%" +
		 * accName.toLowerCase() + "%")); } return predicate; } };
		 * 
		 * Page<AgencyApplicationView> agencies = acctRepository.findAll(spec);
		 */
		AcctPo local = acctService.getAcctByAcctNo(accName);
		return local == null ? StringUtils.EMPTY : local.getUserId();
	}

	public String getAccNo(String userId) {
		AcctPo acc = acctService.getAcctByUserId(userId);
		return acc == null ? StringUtils.EMPTY : acc.getAcctNo();
	}

	public List<AcctPo> getAccNoByUserName(String userName) {
		List<UserPo> userList = userRepository
				.findUserByNameIgnoreCase(userName);
		List<AcctPo> result = new ArrayList<AcctPo>();
		for (UserPo user : userList) {
			user.getUserId();
			AcctPo acc = acctService.getAcctByUserId(user.getUserId());
			if (acc != null) {
				result.add(acc);
			}
		}
		return result;
	}

	public UserPo getUser(String userId) {
		if (userId == null) {
			return null;
		}
		return userRepository.findOne(userId);
	}

	public List<ProductLib> getProductLib() {

		return (List<ProductLib>) libRepository.findAll();

	}

	/**
	 * 
	 * Description: 获取所有合同模板信息
	 * 
	 * @return
	 */
	public List<ProductContractTemplate> getAllTemplateList() {
		return productContractTemplateRepository.findByEnable("Y");
	}

	/**
	 * 
	 * 
	 * @param productId
	 * @return
	 */
	public Product getProductById(String productId) {
		return productRepository.findOne(productId);
	}

	/**
	 * @author minhuang
	 * 
	 *         Description: commit the product process.
	 * 
	 */
	public EProductStatus commitProduct(final String productId,
			final String language) {
		if (!extendJbpmService.isAvailable()) {// 开关关闭状态
			return EProductStatus.WAITTOAPPROVE;
		}
		// final String userId = securityContext.getCurrentUserId();
		// TODO make the intergration with jbpm working well.
		final String userId = "prod_serv";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("approve", true);// create processs, the user task always to
									// be run
		ProcessInstance prodessInstance = extendJbpmService.createProcess(
				ApplicationConstant.JBPM_PROCESS_PRODUCT_PACKAGENAME, params);
		List<Status> status = new ArrayList<Status>();
		status.add(Status.Reserved);
		TaskSummary task = extendJbpmService
				.getTaskByIdAndStatusAndProcessInstanceId(
						prodessInstance.getId(), userId, status, productId,
						language);
		if (task != null) {
			extendJbpmService.doTask(params, userId, task.getId());
			Map<String, Object> varMap = extendJbpmService.getJbpmService()
					.getProcessInstanceVariables(
							String.valueOf(prodessInstance.getId()));
			final String productStatus = (String) (varMap.get("productStatus"));
			for (EProductStatus eProductStatus : EProductStatus.values()) {
				if (eProductStatus.name().equalsIgnoreCase(productStatus)) {
					return eProductStatus;
				}
			}
		}
		return null;
	}

	/**
	 * @author minhuang
	 * 
	 *         Description: approve the product process.
	 * 
	 * @param productId
	 * @param approve
	 */
	public EProductStatus approveProduct(final String productId,
			final String language, EProductStatus currentStatus,
			final boolean approve) {
		sendRetreatMessage(currentStatus,productId);
		if (!extendJbpmService.isAvailable()) {// 开关关闭状态
			if (currentStatus == null || currentStatus == EProductStatus.NULL) {
				return EProductStatus.NULL;
			}
			if (currentStatus == EProductStatus.WAITTOAPPROVE && approve) {
				return EProductStatus.WAITTOEVALUATE;
			}
			if (currentStatus == EProductStatus.WAITTOAPPROVE && !approve) {
				return EProductStatus.STANDBY;
			}
			if (currentStatus == EProductStatus.WAITTOEVALUATE && approve) {
				return EProductStatus.WAITTOFREEZE;
			}
			if (currentStatus == EProductStatus.WAITTOEVALUATE && !approve) {// 项目评级退回
				return EProductStatus.WAITTOAPPROVE;
			}
			if (currentStatus == EProductStatus.WAITTOFREEZE && approve) {
				return EProductStatus.WAITTOPUBLISH;
			}
			if (currentStatus == EProductStatus.WAITTOFREEZE && !approve) {// 保证金冻结退回
				return EProductStatus.WAITTOEVALUATE;
			}
			if (currentStatus == EProductStatus.WAITTOPUBLISH && approve) {
				return EProductStatus.FINISHED;
			}
			if (currentStatus == EProductStatus.WAITTOPUBLISH && !approve) {// 交易参数设置退回
				return EProductStatus.WAITTOFREEZE;
			}
			// 撤单暂未处理
			return EProductStatus.NULL;
		}
		// final String userId = securityContext.getCurrentUserId();
		// TODO make the intergration with jbpm working well.
		final String userId = "rc_a";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", productId);
		params.put("approve", approve);
		List<Status> status = new ArrayList<Status>();
		status.add(Status.Reserved);
		Map<String, Boolean> approveMap = new HashMap<String, Boolean>();
		approveMap.put("approve", approve);
		TaskSummary task = extendJbpmService.getTaskByIdAndStatusAndProcessId(
				ApplicationConstant.JBPM_PROCESS_PRODUCT_PACKAGENAME, userId,
				status, productId, language, approveMap);
		if (task != null) {
			extendJbpmService.doTask(params, userId, task.getId());
			final String productStatus = (String) (extendJbpmService
					.getProcessInstanceVariableByVariableId(
							String.valueOf(task.getProcessInstanceId()),
							"productStatus"));
			if (StringUtils.isNotEmpty(productStatus)) {
				for (EProductStatus eProductStatus : EProductStatus.values()) {
					if (eProductStatus.name().equalsIgnoreCase(productStatus)) {
						return eProductStatus;
					}
				}
			}
		}
		return null;
	}

	public void sendRetreatMessage(EProductStatus status,String productId) {
		String messageKey = null;
		List<EBizRole> roleList = new ArrayList<EBizRole>();
		String content = productRepository.findByProductId(productId).getProductName();
		switch (status) {
		case WAITTOEVALUATE:
			messageKey = "retreat.evaluate.message";
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR);
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_PRODUCT_APPROVE);
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_MAKER);
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_LOAD_APPROVE);
			break;
		case WAITTOFREEZE:
			messageKey = "retreat.freeze.message";
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_MAKER);
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_POST_LENDING);
			break;
		case WAITTOPUBLISH:
			messageKey = "retreat.publish.message";
			roleList.add(EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR);
			break;
		default:
			return;
		}
		memberMessageService.sendMessage(EMessageType.TODO, messageKey, roleList, content);
	}
}
