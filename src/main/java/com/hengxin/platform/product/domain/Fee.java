/*
 * Project Name: kmfex-platform
 * File Name: Fee.java
 * Class Name: Fee
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class Name: Fee Description: TODO
 *
 * @author yingchangwang
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "GL_FEE_CFG")
public class Fee extends BaseInfo implements Serializable {
    @Id
    @Column(name = "FEE_ID")
    private Integer id;

    @Column(name = "FEE_NAME")
    private String feeName;

    @Column(name = "FEE_RT")
    private BigDecimal feeRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

}
