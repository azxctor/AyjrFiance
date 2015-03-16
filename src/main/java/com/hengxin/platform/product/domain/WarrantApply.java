package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 担保申请表,一个产品在申请担保的时候可能申请多个担保机构，
 * 但是最后只能有一个担保机构作为担保
 * @author tiexiyu
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_WRT_APPL")
public class WarrantApply implements Serializable {
	
	/**
	 * 担保申请ID
	 */
	@Id
	@Column(name = "APPL_ID")
	private String warrentApplyId;	
	
	/**
	 * 产品ID
	 */
	@Column(name = "PROD_ID")
	private String productId;

	/**
	 * 申请用户ID
	 */
	@Column(name = "APPL_USER_ID")
	private String applyUserId;

	/**
	 * 担保机构ID
	 */
	@Column(name = "WRTR_ID")
	private String warrantOrgId;

	/**
	 * 申请状态
	 */
	@Column(name = "STATUS")
	private String status;

	/**
	 * 
	 */
	@Column(name = "CREATE_OPID")
	private String createOperatorId;

	/**
	 * 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTime;

	/**
	 * 
	 */
	@Column(name = "LAST_MNT_OPID")
	private String lastOperatorId;

	/**
	 * 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastTime;

	/**
	 * 
	 */
	@Version
	@Column(name = "VERSION_CT")
	private Long versionCount;

	/**
	 * 获得担保申请ID
	 * @return
	 */
	public String getWarrentApplyId() {
		return warrentApplyId;
	}

	/**
	 * 设置担保申请ID
	 * @param warrentApplyId
	 */
	public void setWarrentApplyId(String warrentApplyId) {
		this.warrentApplyId = warrentApplyId;
	}

	/**
	 * 获得产品ID
	 * @return
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * 设置产品ID
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 获得申请用户ID
	 * @return
	 */
	public String getApplyUserId() {
		return applyUserId;
	}

	/**
	 * 设置申请用户ID
	 * @param applyUserId
	 */
	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	/**
	 * 获得担保机构ID
	 * @return
	 */
	public String getWarrantOrgId() {
		return warrantOrgId;
	}

	/**
	 * 设置担保机构ID
	 * @param warrantOrgId
	 */
	public void setWarrantOrgId(String warrantOrgId) {
		this.warrantOrgId = warrantOrgId;
	}

	/**
	 * 获得申请状态
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置申请状态
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获得创建用户ID
	 * @return
	 */
	public String getCreateOperatorId() {
		return createOperatorId;
	}

	/**
	 * 设置创建用户ID
	 * @param createOperatorId
	 */
	public void setCreateOperatorId(String createOperatorId) {
		this.createOperatorId = createOperatorId;
	}

	/**
	 * 获得创建申请时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建申请时间
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获得更新用户ID
	 * @return
	 */
	public String getLastOperatorId() {
		return lastOperatorId;
	}

	/**
	 * 设置更新用户ID
	 * @param lastOperatorId
	 */
	public void setLastOperatorId(String lastOperatorId) {
		this.lastOperatorId = lastOperatorId;
	}

	/**
	 * 获得更新时间
	 * @return
	 */
	public Date getLastTime() {
		return lastTime;
	}

	/**
	 * 设置更新时间
	 * @param lastTime
	 */
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	/**
	 * 获取versionCt
	 * @return
	 */
	public Long getVersionCt() {
		return versionCount;
	}

	/**
	 * 设置versionCt
	 * @param versionCt
	 */
	public void setVersionCt(Long versionCt) {
		this.versionCount = versionCt;
	}	
}
