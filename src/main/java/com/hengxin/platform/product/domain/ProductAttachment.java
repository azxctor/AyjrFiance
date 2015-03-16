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
import javax.persistence.Transient;

import com.hengxin.platform.common.entity.SysFilePo;

/**
 * Class Name: ProductAttachment 融资产品附件
 * 
 * @author Ryan
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD_ATCHMNT")
public class ProductAttachment extends BaseProduct implements Serializable {
    
    @Column(name="FILE_ID")
    private String file;// 附件
    
    @Transient
    private SysFilePo filePo;

    /**
     * @return return the value of the var file
     */
    
    public String getFile() {
        return file;
    }

    /**
     * @param file Set file value
     */
    
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return return the value of the var filePo
     */
    
    public SysFilePo getFilePo() {
        return filePo;
    }

    /**
     * @param filePo Set filePo value
     */
    
    public void setFilePo(SysFilePo filePo) {
        this.filePo = filePo;
    }

}
