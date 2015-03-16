/*
 * Project Name: kmfex-platform
 * File Name: ProductContractTemplate.java
 * Class Name: ProductContractTemplate
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class Name: ProductContractTemplate Description: 合同模板
 * 
 * @author huanbinzhang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_CNTRCT_TMPLT")
public class ProductContractTemplate implements Serializable {
    @Id
    @Column(name = "TMPLT_ID")
    private String templateId;

    @Column(name = "TMPLT_NAME")
    private String templateName; // 合同模板名

    @Column(name = "FNCR_PREPAY_PENALTY_RT")
    private BigDecimal financierPrepaymentPenaltyRate; // 融资人提前还款违约金率

    @Column(name = "PLTFRM_PREPAY_PENALTY_RT")
    private BigDecimal platformPrepaymentPenaltyRate; // 平台提前还款违约金率

    @Column(name = "PAY_PENALTY_FINE_RT")
    private BigDecimal paymentPenaltyFineRate; // 还款违约时计算罚金的违约金率（滞纳金）

    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "PREPAY_DEDUCT_INTR_FLG")
    private String prepayDeductIntrFlg; // 提前还款是否扣除当月利息,Y/N

    @Column(name = "CREATE_OPID")
    private String createOpid;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;
    
    @Column(name = "ENABLED")
    private String enable;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return return the value of the var createOpid
     */

    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid
     *            Set createOpid value
     */

    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    /**
     * @return return the value of the var lastMntOpid
     */

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    /**
     * @param lastMntOpid
     *            Set lastMntOpid value
     */

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    /**
     * @return return the value of the var createTs
     */

    public Date getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs
     *            Set createTs value
     */

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    /**
     * @return return the value of the var lastMntTs
     */

    public Date getLastMntTs() {
        return lastMntTs;
    }

    /**
     * @param lastMntTs
     *            Set lastMntTs value
     */

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    public BigDecimal getFinancierPrepaymentPenaltyRate() {
        return financierPrepaymentPenaltyRate;
    }

    public void setFinancierPrepaymentPenaltyRate(BigDecimal financierPrepaymentPenaltyRate) {
        this.financierPrepaymentPenaltyRate = financierPrepaymentPenaltyRate;
    }

    public BigDecimal getPlatformPrepaymentPenaltyRate() {
        return platformPrepaymentPenaltyRate;
    }

    public void setPlatformPrepaymentPenaltyRate(BigDecimal platformPrepaymentPenaltyRate) {
        this.platformPrepaymentPenaltyRate = platformPrepaymentPenaltyRate;
    }

    public BigDecimal getPaymentPenaltyFineRate() {
        return paymentPenaltyFineRate;
    }

    public void setPaymentPenaltyFineRate(BigDecimal paymentPenaltyFineRate) {
        this.paymentPenaltyFineRate = paymentPenaltyFineRate;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPrepayDeductIntrFlg() {
        return prepayDeductIntrFlg;
    }

    public void setPrepayDeductIntrFlg(String prepayDeductIntrFlg) {
        this.prepayDeductIntrFlg = prepayDeductIntrFlg;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
    
}
