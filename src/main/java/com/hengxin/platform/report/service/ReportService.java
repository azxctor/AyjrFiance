package com.hengxin.platform.report.service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.ReportUtil;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.repository.ServiceCenterInfoRepository;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.product.domain.LoanContract;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.repository.FinancingPackageViewRepository;
import com.hengxin.platform.product.repository.LoanContractRepository;
import com.hengxin.platform.product.repository.ProductContractTemplateRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.report.ReportParameter;
import com.hengxin.platform.report.dto.ReportParameterStringDto;
import com.hengxin.platform.report.dto.SimpleLot;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;

@Service
public class ReportService {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageViewRepository financingPackageViewRepository;

    @Autowired
    private AcctRepository acctRepository;

    @Autowired
    private ServiceCenterInfoRepository serviceCenterInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductPackageRepository packageRepository;

    @Autowired
    private ProductContractTemplateRepository productContractTemplateRepository;

    @Autowired
    private PositionLotRepository positionLotRepository;

    @Autowired
    private LoanContractRepository loanContractRepository;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    public ReportParameterStringDto GetReportParameterService(String rId, String pageName, String exportFileExt,
            String parentClientEvent, String eventName, Map<String, Object> filter, String... masks) {
        ReportParameter reportParameter = new ReportParameter();
        reportParameter.setuId(this.securityContext.getCurrentUserId());
        reportParameter.setrId(rId);
        reportParameter.setPageName(pageName);
        reportParameter.setFilter(filter);
        Map<String, String> feature = new HashMap<String, String>();
        if (StringUtils.isNotBlank(exportFileExt)) {
            feature.put("ExportFileExt", exportFileExt);
        }
        feature.put("ParentClientEvent", parentClientEvent + "," + eventName);

        for (int i = 0; i < masks.length; i++) {
            if (StringUtils.isNotBlank(masks[i])) {
                if (i == 0) {
                    feature.put("MaskColumns", masks[i]);
                } else if (i == 1) {
                    feature.put("PageTitle", masks[i]);
                } else if (i == 2) {
                    feature.put("FixedColumns", masks[i]);
                } else if (i == 3) {
                    feature.put("DisableColumns", masks[i]);
                } else if (i == 4) {
                    feature.put("SpecialColumnText", masks[i]);
                } else if (i == 5) {
                    reportParameter.setuId(masks[i]);
                }
            }
        }

        reportParameter.setFeature(feature);
        String byteString = ReportUtil.buildReportCriteriaToken(reportParameter);
        ReportParameterStringDto reportParameterStringDto = new ReportParameterStringDto();
        reportParameterStringDto.setParameterString(AppConfigUtil.getReportServerAddr() + pageName + "?key="
                + byteString);

        return reportParameterStringDto;
    }

	@Transactional(readOnly = true)
	public ReportParameterStringDto getLegacyContractReport(String lotId) {
		PositionLotPo lot = positionLotRepository.findOne(lotId);
		String oldContractId = "";
		if (lot.getContractId() != null) {
			LoanContract contract = loanContractRepository.findOne(lot
					.getContractId());
			if (contract != null) {
				oldContractId = contract.getOldContractId();
			}
		}
		String userId = lot.getPosition().getUserId();
		String acctNo = acctRepository.findByUserId(userId).getAcctNo();
		String legacyUrl = MessageFormat
				.format("{0}/back/investBaseAction!agreement_ui2?invest_record_id={1}&username={2}",
						AppConfigUtil.getLegacySystemUrl(), oldContractId,
						acctNo);
		return new ReportParameterStringDto(legacyUrl);
	}
	
	@Transactional(readOnly = true)
	public SimpleLot getOriginalLotId(String lotId) {
		PositionLotPo lot = positionLotRepository.findOne(lotId);
		List<PositionLotPo> lots = positionLotRepository.findByCrIdOrderByCreateTsAsc(lot.getCrId());
		if (!lots.isEmpty()) {
			return new SimpleLot(lots.get(0).getLotId(), lot.getPosition().getUserId());
		} else {
			LOGGER.warn("unable to find original lot id for lot: {}", lotId);
			return new SimpleLot(lot.getLotId(), lot.getPosition().getUserId());
		}
	}

    public BigDecimal getContractTemplateId(String lotId) {
        BigDecimal id = this.financingPackageViewRepository.getContractTemplateIdByLotId(lotId);
        return id;
    }

    public AcctPo getAcctPo(String userId) {
        return this.acctRepository.findByUserId(userId);
    }

    public List<String> getServiceCenterInfoList() {
        return this.acctRepository.getAcctNoByServiceCenter();
    }

    public List<EnumOption> getServiceCenterList() {
    	List<EnumOption> options = new ArrayList<EnumOption>();
    	List<ServiceCenterInfo> serviceCenters = serviceCenterInfoRepository.findAll();
    	options.add(new EnumOption("", ""));
    	for(ServiceCenterInfo serviceCenter : serviceCenters){
        	options.add(new EnumOption(serviceCenter.getUserId(), serviceCenter.getServiceCenterDesc()));
    	}
        return options;
    }
    
    public UserPo getUserByUserId(String userId) {
        UserPo userPo = this.userRepository.findUserByUserId(userId);
        return userPo;
    }

    public ProductPackage getPackageByPackageId(String packageId) {
        ProductPackage productPackage = this.packageRepository.getProductPackageById(packageId);
        return productPackage;
    }
}
