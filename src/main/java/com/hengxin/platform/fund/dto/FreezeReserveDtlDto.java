
/*
* Project Name: kmfex
* File Name: FrezzeReserveDtlDto.java
* Class Name: FrezzeReserveDtlDto
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
	
package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.member.enums.EMemberType;


/**
 * Class Name: FrezzeReserveDtlDto
 * Description: TODO
 * @author congzhou
 *
 */

public class FreezeReserveDtlDto implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jnlNo;
    
    private String acctNo;
    
    private String subAcctNo;
    
    private String name;
    
    private EMemberType type;
    
    private EFnrStatus status;
    
    private EFnrOperType operType;
    
    private Date effectDt;
    
    private Date expireDt;
    
    private EFundUseType useType;
    
    private BigDecimal trxAmt;
    
    private String trxMemo;
    
    
    public EFnrOperType getOperType() {
        return operType;
    }

    public void setOperType(EFnrOperType operType) {
        this.operType = operType;
    }

    public String getJnlNo() {
        return jnlNo;
    }

    public void setJnlNo(String jnlNo) {
        this.jnlNo = jnlNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getSubAcctNo() {
        return subAcctNo;
    }

    public void setSubAcctNo(String subAcctNo) {
        this.subAcctNo = subAcctNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EMemberType getType() {
        return type;
    }

    public void setType(EMemberType type) {
        this.type = type;
    }

    public Date getEffectDt() {
        return effectDt;
    }

    public void setEffectDt(Date effectDt) {
        this.effectDt = effectDt;
    }

    public Date getExpireDt() {
        return expireDt;
    }

    public void setExpireDt(Date expireDt) {
        this.expireDt = expireDt;
    }

    public BigDecimal getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(BigDecimal trxAmt) {
        this.trxAmt = trxAmt;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public EFnrStatus getStatus() {
		return status;
	}

	public void setStatus(EFnrStatus status) {
		this.status = status;
	}

}
