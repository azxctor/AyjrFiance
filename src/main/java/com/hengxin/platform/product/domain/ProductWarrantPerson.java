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
 * Class Name: ProductWarrantPerson 融资产品担保信息
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_WRTR_P")
public class ProductWarrantPerson extends BaseProduct implements Serializable {

    @Column(name = "WRTR_NAME")
    private String name;// 自然人姓名

    @Column(name = "WRTR_ID_NO")
    private String idNo;// 自然人身份证号

    @Column(name = "WRTR_JOB_CD")
    private String job;// 自然人职业

    @Column(name = "WRTR_DESC")
    private String notes;// 自然人备注

    /**
     * @return return the value of the var name
     */
    
    public String getName() {
        return name;
    }

    /**
     * @param name Set name value
     */
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return return the value of the var idNo
     */
    
    public String getIdNo() {
        return idNo;
    }

    /**
     * @param idNo Set idNo value
     */
    
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    /**
     * @return return the value of the var job
     */
    
    public String getJob() {
        return job;
    }

    /**
     * @param job Set job value
     */
    
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return return the value of the var notes
     */
    
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes Set notes value
     */
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

}
