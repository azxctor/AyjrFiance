/*
 * Project Name: kmfex-platform
 * File Name: SubAcctTrxJnlService.java
 * Class Name: SubAcctTrxJnlService
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

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.SubAcctTrxJnlRepository;

/**
 * Class Name: SubAcctTrxJnlService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class SubAcctTrxJnlService {

	@Autowired
	SubAcctTrxJnlRepository subAcctTrxJnlRepository;

	public BigDecimal getXWBToalProfit(String acctNo, String subAcctNo) {
		return getSubAcctTrxAmt(acctNo, subAcctNo, EFundUseType.XWBINTEREST);
	}

	public BigDecimal getSubAcctTrxAmt(String acctNo, String subAcctNo,
			EFundUseType useType) {
		return subAcctTrxJnlRepository.getTrxAmtByAcctAndUseType(acctNo,
				subAcctNo, useType);
	}

	public Page<SubAcctTrxJnlPo> getPageableSubAcctTrxJnls(
			Specification<SubAcctTrxJnlPo> spec, Pageable pageable) {
		return subAcctTrxJnlRepository.findAll(spec, pageable);

	}

}
