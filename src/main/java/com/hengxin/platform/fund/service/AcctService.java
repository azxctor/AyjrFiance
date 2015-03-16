/*
 * Project Name: kmfex-platform
 * File Name: AcctService.java
 * Class Name: AcctService
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

package com.hengxin.platform.fund.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.dto.AcctFreezeSearchDto;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.UserAcctInfoView;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.EAcctType;
import com.hengxin.platform.fund.repository.AcctInfoViewRepository;
import com.hengxin.platform.fund.repository.AcctRepository;

/**
 * Class Name: AcctService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class AcctService {

    @Autowired
    private AcctRepository acctRepository;

    @Autowired
    private AcctInfoViewRepository acctInfoViewRepository;

    public AcctPo getAcct(String userId, EAcctType acctType) {
        List<AcctPo> acctPoList = acctRepository.findAcctByUserIdAndAcctType(userId, acctType.getCode());
        AcctPo acct = null;
        if (acctPoList != null && !acctPoList.isEmpty()) {
            acct = acctPoList.get(0);
        }
        return acct;
    }

    public Map<String, String> getUserIdAndAcctNo() {
        Map<String, String> map = new HashMap<String, String>();
        List<Object[]> results = acctRepository.getUserIdAndAcctNo();
        if (!results.isEmpty()) {
            for (Object[] objs : results) {
                if (objs != null && objs.length == 2) {
                    String userId = objs[0].toString();
                    String acctNo = objs[1].toString();
                    if (!map.containsKey(userId)) {
                        map.put(userId, acctNo);
                    }
                }
            }
        }
        return map;
    }

    public AcctPo getAcctByUserId(String userId) {
        return acctRepository.findByUserId(userId);
    }
    
    public String getAcctNo(String userId){
    	AcctPo acct = getAcctByUserId(userId);
    	return acct==null?null:acct.getAcctNo();
    }

    public AcctPo getAcctByAcctNo(String acctNo) {
        return acctRepository.findByAcctNo(acctNo);
    }

    public boolean acctCanPay(String userId) {
        AcctPo acct = getAcctByUserId(userId);
        return canPay(acct);
    }

    public boolean acctCanPay(AcctPo acct) {
        return canPay(acct);
    }

    private boolean canPay(AcctPo acct) {
        boolean bool = false;
        if (acct != null) {
            if (StringUtils.equalsIgnoreCase(EAcctStatus.NORMAL.getCode(), acct.getAcctStatus())) {
                bool = true;
            }
        }
        return bool;
    }

    public Page<UserAcctInfoView> getAcctUnFrozenList(final AcctFreezeSearchDto queryDto) {
        Specification<UserAcctInfoView> specification = new Specification<UserAcctInfoView>() {
            @Override
            public Predicate toPredicate(Root<UserAcctInfoView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                expressions.add(cb.equal(root.<EAcctType> get("acctType"), EAcctType.DEBT));

                if (StringUtils.isNotBlank(queryDto.getAcctNo())) {
                    expressions.add(cb.like(cb.lower(root.<String> get("acctNo")), "%"
                            + queryDto.getAcctNo().trim().toLowerCase() + "%"));
                }

                if (StringUtils.isNotBlank(queryDto.getUserName())) {
                    expressions.add(cb.like(cb.lower(root.<String> get("userName")), "%"
                            + queryDto.getUserName().trim().toLowerCase() + "%"));
                }
                return predicate;
            }
        };
        Pageable pageable = PaginationUtil.buildPageRequest(queryDto);
        Page<UserAcctInfoView> acctPage = this.acctInfoViewRepository.findAll(specification, pageable);

        return acctPage;
    }
    
    public boolean isAcctMatchUser(String acctNo, String name){
        AcctPo acct = acctRepository.getAcctByBnkAcctName(acctNo, name);
        return acct!=null;      
    }
}
