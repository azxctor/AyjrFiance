package com.hengxin.platform.member.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.security.entity.UserPo;

@Entity
@Table(name = "UM_INVSTR_INFO")
public class InvestorUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private AcctPo account;
    
    @Column(name = "INVSTR_LVL_SW", insertable = false, updatable = false)
    private String investorLevel;

    /**
     * keep it lazy to fetch the related objects in a single query.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHZD_CTR_ID", insertable = false, updatable = false)
//    private UserPo authorizedCenter;
//    private ServiceCenterInfo authorizedCenter;
    private Agency authorizedCenter;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the user
	 */
	public UserPo getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserPo user) {
		this.user = user;
	}

	/**
	 * @return the account
	 */
	public AcctPo getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(AcctPo account) {
		this.account = account;
	}

	/**
	 * @return the investorLevel
	 */
	public String getInvestorLevel() {
		return investorLevel;
	}

	/**
	 * @param investorLevel the investorLevel to set
	 */
	public void setInvestorLevel(String investorLevel) {
		this.investorLevel = investorLevel;
	}

	/**
	 * @return the authorizedCenter
	 */
	public Agency getAuthorizedCenter() {
		return authorizedCenter;
	}

	/**
	 * @param authorizedCenter the authorizedCenter to set
	 */
	public void setAuthorizedCenter(Agency authorizedCenter) {
		this.authorizedCenter = authorizedCenter;
	}

}
