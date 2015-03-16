
/*
 * Project Name: kmfex-platform
 * File Name: TradeDetailsDto.java
 * Class Name: TradeDetailsDto
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

import com.hengxin.platform.account.enums.ETradeType;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.product.enums.ERiskLevel;


/**
 * Class Name: TradeDetailsDto
 * Description: TODO
 * @author congzhou
 *
 */

public class TradeDetailsDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 交易日期
     */
    private String trdDt;
    /**
     * 交易类型
     */
    private EFundTrdType trdType;
    /**
     * 交易方向
     */
    private String direction;
    /**
     * 交易金额(万)
     */
    private String amount;
    /**
     * 融资包编号
     */
    private String pkgId;
    /**
     * 融资包简称
     */
    private String pkgName;
    /**
     * 融资期限
     */
    private String term;
    /**
     * 年利率
     */
    private String rate;
    /**
     * 风险等级
     */
    private ERiskLevel riskLvl;
    /**
     * 交易状态    
     */
    private ETradeType status;
    /**
     * 是否已转让  true-已转让 false-未转让
     */
    private boolean isSelled;
    /**
     * 头寸编号 
     */
    private String lotId;
    /**
     * 操作时间
     */
    private String createTs;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTrdDt() {
        return trdDt;
    }

    public void setTrdDt(String trdDt) {
        this.trdDt = trdDt;
    }

    public EFundTrdType getTrdType() {
        return trdType;
    }

    public void setTrdType(EFundTrdType trdType) {
        this.trdType = trdType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ETradeType getStatus() {
        return status;
    }

    public void setStatus(ETradeType status) {
        this.status = status;
    }

    public boolean isSelled() {
        return isSelled;
    }

    public void setSelled(boolean isSelled) {
        this.isSelled = isSelled;
    }
    
    public ERiskLevel getRiskLvl() {
        return riskLvl;
    }

    public void setRiskLvl(ERiskLevel riskLvl) {
        this.riskLvl = riskLvl;
    }

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

}
