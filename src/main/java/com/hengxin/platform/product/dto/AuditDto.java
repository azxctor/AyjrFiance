/*
 * Project Name: kmfex-platform
 * File Name: AuditDto.java
 * Class Name: AuditDto
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

/**
 * Class Name: AuditDto
 * 
 * @author huanbinzhang
 * 
 */

public class AuditDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
    private String comments;
    private boolean passed;
    private String templateId;
    private long overdueToCompDays;
    private long compensatoryDays;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public long getOverdueToCompDays() {
        return overdueToCompDays;
    }

    public void setOverdueToCompDays(long overdueToCompDays) {
        this.overdueToCompDays = overdueToCompDays;
    }

    public long getCompensatoryDays() {
        return compensatoryDays;
    }

    public void setCompensatoryDays(long compensatoryDays) {
        this.compensatoryDays = compensatoryDays;
    }

}
