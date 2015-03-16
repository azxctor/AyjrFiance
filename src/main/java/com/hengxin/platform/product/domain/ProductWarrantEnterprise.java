/*
 * Project Name: kmfex-platform
 * File Name: ProductProviderApplicationPo.java
 * Class Name: ProductProviderApplicationPo
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
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
import javax.persistence.Table;

/**
 * Class Name: ProductWarrantEnterprise 融资产品担保信息
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_WRTR_O")
public class ProductWarrantEnterprise extends BaseProduct implements Serializable {

    @Column(name = "WRTR_NAME")
    private String enterpriseName;// 企业名称

    @Column(name = "WRTR_LICENCE_NO")
    private String enterpriseLicenceNo;// 企业营业执照号

    @Column(name = "WRTR_INDUSTRY_CD")
    private String enterpriseIndustry;// 企业行业

    @Column(name = "WRTR_DESC")
    private String enterpriseNotes;// 企业备注

    /**
     * @return return the value of the var enterpriseName
     */
    
    public String getEnterpriseName() {
        return enterpriseName;
    }

    /**
     * @param enterpriseName Set enterpriseName value
     */
    
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    /**
     * @return return the value of the var enterpriseLicenceNo
     */
    
    public String getEnterpriseLicenceNo() {
        return enterpriseLicenceNo;
    }

    /**
     * @param enterpriseLicenceNo Set enterpriseLicenceNo value
     */
    
    public void setEnterpriseLicenceNo(String enterpriseLicenceNo) {
        this.enterpriseLicenceNo = enterpriseLicenceNo;
    }

    /**
     * @return return the value of the var enterpriseIndustry
     */
    
    public String getEnterpriseIndustry() {
        return enterpriseIndustry;
    }

    /**
     * @param enterpriseIndustry Set enterpriseIndustry value
     */
    
    public void setEnterpriseIndustry(String enterpriseIndustry) {
        this.enterpriseIndustry = enterpriseIndustry;
    }

    /**
     * @return return the value of the var enterpriseNotes
     */
    
    public String getEnterpriseNotes() {
        return enterpriseNotes;
    }

    /**
     * @param enterpriseNotes Set enterpriseNotes value
     */
    
    public void setEnterpriseNotes(String enterpriseNotes) {
        this.enterpriseNotes = enterpriseNotes;
    }
    
}
