package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * @author qimingzou
 * 
 */
public class TransactionJournalSearchDto extends DataTablesRequestDto implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 关键字模糊查询,支持投资会员“交易帐号”、投资会员“姓名”
	 */
	private String keyword;

	/**
	 * 交易类型
	 */
	private EFundUseType useType;

	/**
	 * 交易时间 - 开始
	 */
	private Date beginDate;
	
	/**
	 * 交易时间 - 结束
	 */
	private Date endDate;

	/**
	 * 经办人
	 */
	private String agent;

	/**
	 * 代办人
	 */
	private String agentName;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

}
