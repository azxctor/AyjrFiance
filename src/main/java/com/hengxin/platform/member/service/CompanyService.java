package com.hengxin.platform.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.member.domain.Company;
import com.hengxin.platform.member.repository.CompanyPo;
import com.hengxin.platform.member.repository.CompanyPoRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyPoRepository companyRepository;
    

    public List<Company> findAll(){
    	List<Company> ls = new ArrayList<Company>();
    	Sort s=new Sort(Direction.ASC, "companyId");
    	List<CompanyPo> lsp = this.companyRepository.findAll(s);
    	for(CompanyPo p : lsp){
    		Company c = ConverterService.convert(p, Company.class);
    		ls.add(c);
    	}
    	return ls;
    }
    
    public Company findOne(String one){
    	Company c = ConverterService.convert(this.companyRepository.findOne(one), Company.class);
    	return c;
    }
}