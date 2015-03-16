
/*
* Project Name: kmfex-platform
* File Name: AccountDetailsDto.java
* Class Name: AccountDetailsDto
*
* Copyright 2014 Hengtian Software Inc
*
* Licensed under the Hengtiansoft
*
* http://www.hengtiansoft.com
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
	
package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundUseType;


/**
 * Class Name: AccountDetailsDto
 * Description: TODO
 * @author congzhou
 *
 */

public class AccountDetailsDto implements Serializable{
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 日期時間
     */
    private Date trxDt;
    
    /**
     * 出入金類型
     */
    private EFundUseType useType;
    
    /**
     * 金額
     */
    private BigDecimal trxAmt;
    
    /**
     * 方向
     */
    private String payRecvFlg;
    
    /**
     * 備註
     */
    private String trxMemo;
    
    /**
     * 创建时间
     */
    private String createTs;
    
    /**
     * 关联业务编号
     */
    private String relBizId;
    
    /**
    * AccountDetailsDto Constructor
    *
    * @param trxDt
    * @param useType
    * @param trxAmt
    * @param payRecvFlg
    * @param trxMemo
    */
    	
    public AccountDetailsDto(Date trxDt, EFundUseType useType, BigDecimal trxAmt, String payRecvFlg, String trxMemo) {
        super();
        this.trxDt = trxDt;
        this.useType = useType;
        this.trxAmt = trxAmt;
        this.payRecvFlg = payRecvFlg;
        this.trxMemo = trxMemo;
    }

    public AccountDetailsDto() {
        super();
    }

    public Date getTrxDt() {
        return trxDt;
    }


    public void setTrxDt(Date trxDt) {
        this.trxDt = trxDt;
    }


    public EFundUseType getUseType() {
        return useType;
    }


    public void setUseType(EFundUseType useType) {
        this.useType = useType;
    }


    public BigDecimal getTrxAmt() {
        return trxAmt;
    }


    public void setTrxAmt(BigDecimal trxAmt) {
        this.trxAmt = trxAmt;
    }


    public String getPayRecvFlg() {
        return payRecvFlg;
    }


    public void setPayRecvFlg(String payRecvFlg) {
        this.payRecvFlg = payRecvFlg;
    }


    public String getTrxMemo() {
        return trxMemo;
    }


    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

	public String getRelBizId() {
		return relBizId;
	}

	public void setRelBizId(String relBizId) {
		this.relBizId = relBizId;
	}

    
}
