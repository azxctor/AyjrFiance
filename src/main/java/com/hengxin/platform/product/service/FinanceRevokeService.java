/*
 * Project Name: kmfex-platform
 * File Name: FinanceRevokeController.java
 * Class Name: FinanceRevokeController
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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.service.FinancierRevokeFundService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: FinanceRevokeService Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

@Service
public class FinanceRevokeService extends BaseService {

    private static final String THAW_PRODUCT_PROVIDER_DEPOSIT = "解保留担保公司还款保证金";

    private static final String THAW_FINANCE_SERVICE_DEPOSIT = "解保留融资服务合同保证金";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FinancierRevokeFundService financierRevokeFundService;

    @Autowired
    private WarrantDepositService warrantDepositService;

    @Transactional
    public void productRevoke(String productId, String currentUserId, String comment, EBizRole role) {
        Product product = StringUtils.isBlank(productId) ? null : productRepository.findOne(productId);
        if (product == null) {
            return;
        }

        Date currentTs = new Date();

        // 1.处于待提交的产品，担保机构才能撤单
        if (role == EBizRole.PROD_SERV && product.getStatus() == EProductStatus.STANDBY) {
            product.setProductId(productId);
            product.setStatus(EProductStatus.ABANDONED);
            product.setLastMntOpid(currentUserId);
            product.setLastMntTs(currentTs);
            productRepository.save(product);

            actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.REVOKE, null, comment);
        }
        // 2.处于待发布的产品,交易经理才能撤单
        if (role == EBizRole.TRANS_MANAGER && product.getStatus() == EProductStatus.WAITTOPUBLISH) {
            product.setProductId(productId);
            product.setStatus(EProductStatus.ABANDONED);
            product.setLastMntOpid(currentUserId);
            product.setLastMntTs(currentTs);
            productRepository.save(product);

            productThawDeposit(product, currentUserId);

            actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.REVOKE, null, comment);
        }
    }

    @Transactional
    public void ThawFinanceServiceDeposit(String reserveJnlNo, String financer, String comments, String currOpId) {
        if (reserveJnlNo == null || financer == null || currOpId == null) {
            return;
        }
        UnReserveReq req = new UnReserveReq();
        req.setCurrOpId(currOpId);
        req.setReserveJnlNo(reserveJnlNo);
        req.setTrxMemo(comments);
        req.setUserId(financer);
        req.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        financierRevokeFundService.refundSubsPrePayAmt(req);
    }

    @Transactional
    public void ThawProductProviderDeposit(String reserveJnlNo, String warranter, String comments, String currOpId) {
        if (reserveJnlNo == null || warranter == null || currOpId == null) {
            return;
        }
        UnReserveReq req = new UnReserveReq();
        req.setCurrOpId(currOpId);
        req.setReserveJnlNo(reserveJnlNo);
        req.setTrxMemo(comments);
        req.setUserId(warranter);
        req.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        warrantDepositService.refundWarrantDeposit(req);
    }

    /**
     * 
     * Description: 解保留融资服务合同保证金和担保机构还款保证金
     * 
     * @param product
     * @param currOpId
     */
    public void productThawDeposit(Product product, String currOpId) {
        if (EProductStatus.ABANDONED == product.getStatus() || EProductStatus.WAITTOFREEZE == product.getStatus()) {
            ThawFinanceServiceDeposit(product.getServFnrJnlNo(), product.getApplUserId(), THAW_FINANCE_SERVICE_DEPOSIT,
                    currOpId);
            actionHistoryUtil.save(EntityType.PRODUCT, product.getProductId(), ActionType.THAW,
                    ActionResult.PRODUCT_THAW_PRODUCT_PROVIDER, THAW_FINANCE_SERVICE_DEPOSIT);
            ThawProductProviderDeposit(product.getWrtrFnrJnlNo(), product.getWarrantId(),
                    THAW_PRODUCT_PROVIDER_DEPOSIT, currOpId);
            actionHistoryUtil.save(EntityType.PRODUCT, product.getProductId(), ActionType.THAW,
                    ActionResult.PRODUCT_THAW_FINANCE_SERVICE, THAW_PRODUCT_PROVIDER_DEPOSIT);
        }
    }
}
