/*
 * Project Name: kmfex-platform
 * File Name: RetreatDto.java
 * Class Name: RetreatDto
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

package com.hengxin.platform.product.dto;

import java.io.Serializable;

import com.hengxin.platform.product.enums.EProductStatus;

/**
 * Class Name: RetreatDto Description: TODO
 * 
 * @author chunlinwang
 * 
 */

public class RetreatDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;

    private EProductStatus status;

    private String comment;

    /**
     * @return return the value of the var productId
     */

    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            Set productId value
     */

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return return the value of the var status
     */

    public EProductStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            Set status value
     */

    public void setStatus(EProductStatus status) {
        this.status = status;
    }

    /**
     * @return return the value of the var comment
     */

    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     *            Set comment value
     */

    public void setComment(String comment) {
        this.comment = comment;
    }

}
