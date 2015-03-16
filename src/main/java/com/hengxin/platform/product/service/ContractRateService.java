package com.hengxin.platform.product.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
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
import com.hengxin.platform.product.domain.ContractRate;
import com.hengxin.platform.product.domain.ContractRatePK;
import com.hengxin.platform.product.dto.ContractRateDto;
import com.hengxin.platform.product.dto.ContractRatePKDto;
import com.hengxin.platform.product.repository.ContractRateRepository;

/**
 * ContractRateService
 * @author chenwulou
 *
 */
@Service
public class ContractRateService {
	
	private static final  String Y = "Y";
	private static final  String N = "N";
	
	@Autowired
	ContractRateRepository contractRateRepository ;
	
	@Autowired
	ProductContractTemplateService productContractTemplateService;
	
	@Transactional(readOnly=true)
	public ContractRate findOne(ContractRatePK contractRatePk) {
		return contractRateRepository.findOne(contractRatePk);
	}
	
	@Transactional
	public boolean validateAndSave(ContractRateDto contractRateDto) {
		ContractRatePK pk = new ContractRatePK();
		pk.setContractId(contractRateDto.getContractId());
		pk.setProductLevelId(contractRateDto.getProductLevelId());
		
		ContractRate cr = findOne(pk);
		if(cr != null){
			return false;
		}
		ContractRate contractRate = ConverterService.convert(contractRateDto, ContractRate.class);
		contractRate.setProductLevelId(contractRateDto.getProductLevelId());
		contractRate.setContractId(contractRateDto.getContractId());
		if(contractRateDto.isDeductFlg()){
			contractRate.setPrepayDeductIntrFlg(Y);
		}else{
			contractRate.setPrepayDeductIntrFlg(N);
		}
		contractRateRepository.save(contractRate);
		
		return true;
		
	}
	
	@Transactional
	public void delete(ContractRatePKDto contractRatePKDto) {
		ContractRatePK contractRatePK = ConverterService.convert(contractRatePKDto,ContractRatePK.class);
		contractRateRepository.delete(contractRatePK);
	}
	
	@Transactional(readOnly=true)
	public Page<ContractRate> findAll(final ContractRatePKDto contractRatePKDto) {
		Specification<ContractRate> spec = new Specification<ContractRate>() {
			
			@Override
			public Predicate toPredicate(Root<ContractRate> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				
				if(StringUtils.isNotBlank(contractRatePKDto.getContractId())){
                	expressions.add(cb.equal(root.<String> get("contractId"),contractRatePKDto.getContractId()));
                	
                }
				
				if(StringUtils.isNotBlank(contractRatePKDto.getProductLevelId())){
                	expressions.add(cb.equal(root.<String> get("productLevelId"),contractRatePKDto.getProductLevelId()));
                	
                }
				
				return predicate;
			}
		};
		
		Pageable pageable = ContractRateService.buildPageRequest(contractRatePKDto);
		return contractRateRepository.findAll(spec, pageable);
	}
	
	@Transactional
	public boolean update(ContractRateDto contractRateDto) {
		ContractRatePK contractRatePk = new ContractRatePK();
		contractRatePk.setContractId(contractRateDto.getContractId());
		contractRatePk.setProductLevelId(contractRateDto.getProductLevelId());
		ContractRate cr = findOne(contractRatePk);
		if(cr == null){
			return false;
		}
		cr.setFinancierPrepaymentPenaltyRate(contractRateDto.getFinancierPrepaymentPenaltyRate());
		cr.setPaymentPenaltyFineRate(contractRateDto.getPaymentPenaltyFineRate());
		cr.setPlatformPrepaymentPenaltyRate(contractRateDto.getPlatformPrepaymentPenaltyRate());
		if(contractRateDto.isDeductFlg()){
			cr.setPrepayDeductIntrFlg(Y);
		}else{
			cr.setPrepayDeductIntrFlg(N);
		}
		
		contractRateRepository.save(cr);
		return true;
	}
	
	
	private static Pageable buildPageRequest(DataTablesRequestDto requestDto) {

        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith("financierPrepaymentPenaltyRate")) {
            	sortColumn = "contractId";
            } 
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }
	
	
	
}
