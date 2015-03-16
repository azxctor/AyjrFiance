package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.product.domain.converter.ProductCommonConditionConverter;
import com.hengxin.platform.product.enums.EProductCommonCondition;

/**
 * 房产抵押表
 * 
 * @author tiexiyu
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_MTGE_R")
@IdClass(MortgageResidentialPK.class)
public class MortgageResidential implements Serializable {

    /**
     * 产品Id
     */
    @Id
    @Column(name = "PROD_ID")
    private String productId;

    /**
     * 序号
     */
    @Id
    @Column(name = "SEQ_ID")
    private int sequenceId;

    /**
     * 抵押类型
     */
    @Column(name = "MTGE_TYPE")
    private String mortgageType;

    /**
     * 房产证号码
     */
    @Column(name = "PROP_CERT_NO")
    private String premisesPermitNo;

    /**
     * 房产拥有者
     */
    @Column(name = "OWNER")
    private String owner;

    /**
     * 拥有类型
     */
    @Column(name = "OWNER_TYPE")
    @Convert(converter = ProductCommonConditionConverter.class)
    private EProductCommonCondition ownerType;

    /**
     * 房产共有人
     */
    @Column(name = "CO_OWNER")
    private String coOwner;

    /**
     * 房产共有人年龄
     */
    @Column(name = "CO_OWNER_AGE")
    private Integer coOwnerAge;

    /**
     * 地址
     */
    @Column(name = "LOCATION")
    private String location;

    /**
     * 房产登记日期
     */
    @Column(name = "REGIST_DT")
    private Date registDate;

    /**
     * 房屋面积
     */
    @Column(name = "AREA")
    private BigDecimal area;

    /**
     * 房屋购买价格
     */
    @Column(name = "PRCHS_PRC")
    private BigDecimal purchasePrice;

    /**
     * 房屋评估价格
     */
    @Column(name = "EVAL_PRC")
    private BigDecimal evaluatePrice;

    /**
     * 房屋市场价格
     */
    @Column(name = "MKT_PRC")
    private BigDecimal marketPrice;

    /**
     * 创建人
     */
    @Column(name = "CREATE_OPID")
    private String createOperateorId;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "LAST_MNT_OPID")
    private String lastOperatorId;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastTime;

    /**
     * 版本
     */
    @Column(name = "VERSION_CT")
    private long versionCount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getPremisesPermitNo() {
        return premisesPermitNo;
    }

    public void setPremisesPermitNo(String premisesPermitNo) {
        this.premisesPermitNo = premisesPermitNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCoOwner() {
        return coOwner;
    }

    public void setCoOwner(String coOwner) {
        this.coOwner = coOwner;
    }

    public Integer getCoOwnerAge() {
        return coOwnerAge;
    }

    public void setCoOwnerAge(Integer coOwnerAge) {
        this.coOwnerAge = coOwnerAge;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getEvaluatePrice() {
        return evaluatePrice;
    }

    public void setEvaluatePrice(BigDecimal evaluatePrice) {
        this.evaluatePrice = evaluatePrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getCreateOperateorId() {
        return createOperateorId;
    }

    public void setCreateOperateorId(String createOperateorId) {
        this.createOperateorId = createOperateorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastOperatorId() {
        return lastOperatorId;
    }

    public void setLastOperatorId(String lastOperatorId) {
        this.lastOperatorId = lastOperatorId;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public long getVersionCount() {
        return versionCount;
    }

    public void setVersionCount(long versionCount) {
        this.versionCount = versionCount;
    }

    /**
     * @return return the value of the var mortgageType
     */
    
    public String getMortgageType() {
        return mortgageType;
    }

    /**
     * @param mortgageType Set mortgageType value
     */
    
    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }

    /**
     * @return return the value of the var ownerType
     */

    public EProductCommonCondition getOwnerType() {
        return ownerType;
    }

    /**
     * @param ownerType
     *            Set ownerType value
     */

    public void setOwnerType(EProductCommonCondition ownerType) {
        this.ownerType = ownerType;
    }

}
