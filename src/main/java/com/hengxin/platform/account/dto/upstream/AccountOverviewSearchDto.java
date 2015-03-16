package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.hengxin.platform.account.dto.AccountDetailsDto;
import com.hengxin.platform.account.dto.AccountOverviewDto;
import com.hengxin.platform.account.dto.CurrentAccountDto;
import com.hengxin.platform.account.dto.InvestBenifitDto;
import com.hengxin.platform.account.dto.UserInfoDto;
import com.hengxin.platform.account.dto.XwbAccountDto;

/**
 * Class Name: AccountOverviewSearchDto
 * 
 * @author jishen
 * 
 */
public class AccountOverviewSearchDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 活期账户信息
	 */
	private CurrentAccountDto currentAccount;
	
	/**
	 * 账户概览
	 */
	private AccountOverviewDto accountOverview;
	
    /**
	 * 小微宝账户信息
	 */
	private XwbAccountDto xwbAccount;
	
//	/**
//	 * 收益信息
//	 */
//	private EarningsInfoDto earningsInfo;
	
	/**
	 * 登陆会员信息
	 */
	private UserInfoDto userInfo;
	
	/**
	 * 账户明细
	 */
	private List<AccountDetailsDto> accountDetails;
	
	/**
	 * 投资收益
	 */
	private List<InvestBenifitDto> investBenifit;
	
	/**
	 * 交易所在银行资金总额
	 */
	private BigDecimal totalAmount;

	public CurrentAccountDto getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(CurrentAccountDto currentAccount) {
		this.currentAccount = currentAccount;
	}

	public AccountOverviewDto getAccountOverview() {
        return accountOverview;
    }

    public void setAccountOverview(AccountOverviewDto accountOverview) {
        this.accountOverview = accountOverview;
    }
    
	public XwbAccountDto getXwbAccount() {
		return xwbAccount;
	}

	public void setXwbAccount(XwbAccountDto xwbAccount) {
		this.xwbAccount = xwbAccount;
	}

//	public EarningsInfoDto getEarningsInfo() {
//		return earningsInfo;
//	}
//
//	public void setEarningsInfo(EarningsInfoDto earningsInfo) {
//		this.earningsInfo = earningsInfo;
//	}

	public UserInfoDto getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoDto userInfo) {
		this.userInfo = userInfo;
	}

    public List<AccountDetailsDto> getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(List<AccountDetailsDto> accountDetails) {
        this.accountDetails = accountDetails;
    }

    public List<InvestBenifitDto> getInvestBenifit() {
        return investBenifit;
    }

    public void setInvestBenifit(List<InvestBenifitDto> investBenifit) {
        this.investBenifit = investBenifit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

}
