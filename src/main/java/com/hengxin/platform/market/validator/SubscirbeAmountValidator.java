/*
 * Project Name: kmfex-platform
 * File Name: SubscirbeAmountValidator.java
 * Class Name: SubscirbeAmountValidator
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

package com.hengxin.platform.market.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.common.service.validator.SubscribeAmountCheck;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.market.dto.downstream.FinancingMarketItemDetailDto;
import com.hengxin.platform.market.service.PackageSubscirbeService;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: SubscirbeAmountValidator Description: TODO
 * 
 * @author runchen
 * 
 */
@Component
public class SubscirbeAmountValidator extends BaseValidator implements
        ConstraintValidator<SubscribeAmountCheck, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscirbeAmountValidator.class);

    @Autowired
    private SecurityContext sec;

    @Autowired
    private PackageSubscirbeService subcribeService;

    @Autowired
    private transient FundAcctBalService fundAccoutService;

    private String pkgId;
    private String amount;
    private String messageTemplete;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SecurityContext securityContext;

    @Override
    public void initialize(SubscribeAmountCheck check) {
        this.pkgId = check.pkgId();
        this.amount = check.amount();
        this.messageTemplete = check.message();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            final String amountStr = BeanUtils.getProperty(object, this.amount);
            final String pkgId = BeanUtils.getProperty(object, this.pkgId);
            final String userId = sec.getCurrentUserId();
            messageTemplete = "";
            if (StringUtils.isNotBlank(amountStr)) {
                if (!SubscribeUtils.isValidFaceValue(amountStr)) {
                    // 数字格式，faceValue 的整数倍
                    messageTemplete = "{market.error.subscribe.amount.invaild}";
                } else {
                    BigDecimal amount = new BigDecimal(amountStr);
                    FinancingMarketItemDetailDto dto = subcribeService.getLatestSubcribeAmount(pkgId, userId);
                    if (!dto.isSubscribed()) {
                        // 错误为不可申购理由
                        messageTemplete = dto.getReason();
                    } else {
                        if (dto.getMinSubscribeAmount().compareTo(amount) > 0
                                || dto.getMaxSubscribeAmount().compareTo(amount) < 0) {
                            // 超出范围
                            messageTemplete = "{market.error.subscribe.amount.outrange}";
                        } else {
                            try {
                                BigDecimal balance = fundAccoutService.getUserCurrentAcctAvlBal(userId, true);
                                if (balance.compareTo(amount) < 0) {
                                    messageTemplete = "{market.error.balance.lack}";
                                }
                            } catch (BizServiceException ex) {
                                messageTemplete = ex.getMessage();
                            }
                        }
                    }
                }

            } else {
                // 无效申购金额
                messageTemplete = "{market.error.subscribe.amount.empty}";
            }
            if (StringUtils.isNotBlank(messageTemplete)) {
                bindNode(context, this.amount, this.messageTemplete);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("SubscirbeAmountValidator throw error!", e);
        }
        return true;
    }

}
