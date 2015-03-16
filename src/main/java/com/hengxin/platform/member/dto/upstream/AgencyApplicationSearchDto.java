package com.hengxin.platform.member.dto.upstream;

import java.io.Serializable;

import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.member.dto.downstream.ProductProviderApplicationSearchDto;
import com.hengxin.platform.member.dto.downstream.ServiceCenterApplicationDto;

/**
 * Class Name: AgencyApplicationSearch
 * 
 * @author junwei
 * 
 */
public class AgencyApplicationSearchDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AgencyApplicationDto agency;

    private ProductProviderApplicationSearchDto productProvider;

    private ServiceCenterApplicationDto serviceCenter;
    
    private Acct account;
    
    public AgencyApplicationDto getAgency() {
        return agency;
    }

    public void setAgency(AgencyApplicationDto agency) {
        this.agency = agency;
    }


    public ProductProviderApplicationSearchDto getProductProvider() {
        return productProvider;
    }

    public void setProductProvider(ProductProviderApplicationSearchDto productProvider) {
        this.productProvider = productProvider;
    }

    public ServiceCenterApplicationDto getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(ServiceCenterApplicationDto serviceCenter) {
        this.serviceCenter = serviceCenter;
    }


    public Acct getAccount() {
        return account;
    }

    public void setAccount(Acct account) {
        this.account = account;
    }
}
