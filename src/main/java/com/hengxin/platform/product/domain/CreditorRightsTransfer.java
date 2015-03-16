/*
 * Project Name: kmfex-platform
 * File Name: BaseDo.java
 * Class Name: BaseDo
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

package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.PositionLotPo;

/**
 * Class Name: BaseInfo
 * Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_CR_TSFR")
@EntityListeners(IdInjectionEntityListener.class)
public class CreditorRightsTransfer extends BaseInfo implements Serializable {

	@Id
	@Column(name = "TSFR_ID")
	private String id;

	@Column(name = "LOT_ID")
	private String lotId;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "REM_AMT")
	private BigDecimal remainAmount;
	
	@Column(name = "REM_PRIN_AMT")
	private BigDecimal remainPrinAmount;
	
	@Column(name = "REM_INTR_AMT")
	private BigDecimal remainIntrAmount;
	
	@Column(name = "REM_TERM_LENGTH")
	private Integer remainPeriods;

	@Temporal(TemporalType.DATE)
	@Column(name = "MATURITY_DT")
	private Date maturityDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "UNIT")
	private Integer unit;

	@Column(name = "TSFR_CNTRCT_ID")
	private String transferContractId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "LOT_ID", insertable = false, updatable = false)
	private PositionLotPo positionLot;

	/**
	 * 出让时间.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SELLER_TS", nullable = true)
	private Date transferTs;
	
	/**
	 * @return return the value of the var lotId
	 */

	public String getLotId() {
		return lotId;
	}

	/**
	 * @param lotId
	 *            Set lotId value
	 */

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	/**
	 * @return return the value of the var positionLot
	 */

	public PositionLotPo getPositionLot() {
		return positionLot;
	}

	/**
	 * @param positionLot
	 *            Set positionLot value
	 */

	public void setPositionLot(PositionLotPo positionLot) {
		this.positionLot = positionLot;
	}

	/**
	 * @return return the value of the var price
	 */

	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            Set price value
	 */

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(BigDecimal remainAmount) {
		this.remainAmount = remainAmount;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public String getTransferContractId() {
		return transferContractId;
	}

	public void setTransferContractId(String transferContractId) {
		this.transferContractId = transferContractId;
	}

	public BigDecimal getRemainPrinAmount() {
		return remainPrinAmount;
	}

	public void setRemainPrinAmount(BigDecimal remainPrinAmount) {
		this.remainPrinAmount = remainPrinAmount;
	}

	public BigDecimal getRemainIntrAmount() {
		return remainIntrAmount;
	}

	public void setRemainIntrAmount(BigDecimal remainIntrAmount) {
		this.remainIntrAmount = remainIntrAmount;
	}

	public Integer getRemainPeriods() {
		return remainPeriods;
	}

	public void setRemainPeriods(Integer remainPeriods) {
		this.remainPeriods = remainPeriods;
	}

	public Date getTransferTs() {
		return transferTs;
	}

	public void setTransferTs(Date transferTs) {
		this.transferTs = transferTs;
	}

}
