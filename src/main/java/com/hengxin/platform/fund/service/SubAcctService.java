/*
 * Project Name: kmfex-platform
 * File Name: SubAcctService.java
 * Class Name: SubAcctService
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.repository.SubAcctRepository;

/**
 * Class Name: SubAcctService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class SubAcctService {

	@Autowired
	SubAcctRepository subAcctRepository;

	public SubAcctPo getSubAcct(String acctNo, String subAcctNo) {
		SubAcctPo subAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctNo, subAcctNo);
		return subAcctPo;
	}

}
