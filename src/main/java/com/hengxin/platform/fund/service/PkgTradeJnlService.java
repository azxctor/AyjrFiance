/*
 * Project Name: kmfex-platform
 * File Name: PkgTradeJnlService.java
 * Class Name: PkgTradeJnlService
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;

/**
 * Class Name: PkgTradeJnlService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class PkgTradeJnlService {

	@Autowired
	PkgTradeJnlRepository pkgTradeJnlRepository;

	public List<String> getInvestorsByProductPkgId(String pkgId, String sellerId) {
		return pkgTradeJnlRepository.getBuyersByProductPkgId(pkgId, sellerId);
	}
	
	public List<PkgTradeJnlPo> savePkgTradeJnls(List<PkgTradeJnlPo> jnls){
	    return pkgTradeJnlRepository.save(jnls);
	}

}
