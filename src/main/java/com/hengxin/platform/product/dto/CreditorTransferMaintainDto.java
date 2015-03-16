package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

public class CreditorTransferMaintainDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<CreditorTransferInitDto> packageListSelected; // 选择的package
    
    private List<CreditorTransferInitDto> ruleListSelected; // 选择的package type
    
    @Valid
    private CreditorTransferRuleDto creditorTransferRuleDto; // rule details

    public List<CreditorTransferInitDto> getPackageListSelected() {
        return packageListSelected;
    }

    public void setPackageListSelected(List<CreditorTransferInitDto> packageListSelected) {
        this.packageListSelected = packageListSelected;
    }

    public List<CreditorTransferInitDto> getRuleListSelected() {
        return ruleListSelected;
    }

    public void setRuleListSelected(List<CreditorTransferInitDto> ruleListSelected) {
        this.ruleListSelected = ruleListSelected;
    }

    public CreditorTransferRuleDto getCreditorTransferRuleDto() {
        return creditorTransferRuleDto;
    }

    public void setCreditorTransferRuleDto(CreditorTransferRuleDto creditorTransferRuleDto) {
        this.creditorTransferRuleDto = creditorTransferRuleDto;
    }

}
