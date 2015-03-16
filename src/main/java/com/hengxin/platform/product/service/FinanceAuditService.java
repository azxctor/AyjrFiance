/*
 * Project Name: kmfex-platform
 * File Name: FinanceAuditService.java
 * Class Name: FinanceAuditService
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

package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductRepository;

/**
 * Class Name: FinanceAuditService Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

@Service
public class FinanceAuditService extends BaseService {

    @Autowired
    ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void productAudit(String productId, boolean passed, String templateId, String comments,
            String currentUserId, long overdueToCompDays, long compensatoryDays) {
        Date currentTs = new Date();
        Product product = StringUtils.isBlank(productId) ? null : productRepository.findOne(productId);
        if (product == null) {
            return;
        }
        // 处于待审核的产品才需要审核
        if (product.getStatus() == EProductStatus.WAITTOAPPROVE) {
            // EProductStatus status = passed ? EProductStatus.WAITTOEVALUATE : EProductStatus.STANDBY;
            ActionResult actionResult = passed ? ActionResult.PRODUCT_AUDIT_PASS : ActionResult.PRODUCT_AUDIT_REJECT;

            product.setProductId(productId);
            // product.setStatus(status);
            product.setLastMntOpid(currentUserId);
            product.setLastMntTs(currentTs);
            product.setApprDate(currentTs);
            product.setApprUserId(currentUserId);
            if (passed && NumberUtils.isNumber(templateId)) {
                product.setContractTemplateId(BigDecimal.valueOf(Long.valueOf(templateId)));
                product.setOverdue2CmpnsGracePeriod(overdueToCompDays);
                product.setCmpnsGracePeriod(compensatoryDays);
            }
            // added by martin for jbpm process
            EProductStatus productStatus = productService.approveProduct(productId, null, product.getStatus(), passed);
            product.setStatus(productStatus);
            // end
            productRepository.save(product);

            actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.APPROVE, actionResult, comments);
        }
    }
}
