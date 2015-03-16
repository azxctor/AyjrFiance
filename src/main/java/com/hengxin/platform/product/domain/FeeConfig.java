/*
 * Project Name: kmfex-platform
 * File Name: FeeConfig.java
 * Class Name: FeeConfig
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class Name: FeeConfig Description: TODO
 * 
 * @author tingwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "GL_FEE_CFG")
public class FeeConfig extends BaseInfo implements Serializable {

    @Id
    @Column(name = "FEE_ID")
    private Integer feeId;

    @Column(name = "FEE_NAME")
    private String feeName;

    @Column(name = "FEE_BASE")
    private String feeBase;

    @Column(name = "ALGORITHM")
    private String algorithm;

    @Column(name = "FEE_RT")
    private String feeRt;

    @Column(name = "PAY_COUNT")
    private String payCount;

    @Column(name = "PAY_METHOD")
    private String payMethod;

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeeBase() {
        return feeBase;
    }

    public void setFeeBase(String feeBase) {
        this.feeBase = feeBase;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getFeeRt() {
        return feeRt;
    }

    public void setFeeRt(String feeRt) {
        this.feeRt = feeRt;
    }

    public String getPayCount() {
        return payCount;
    }

    public void setPayCount(String payCount) {
        this.payCount = payCount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

}
