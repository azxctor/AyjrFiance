/*
 * Project Name: kmfex-platform
 * File Name: CreditorRightsTransferRepository.java
 * Class Name: CreditorRightsTransferRepository
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

package com.hengxin.platform.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.CreditorRightsTransfer;

/**
 * Class Name: CreditorRightsTransferRepository Description: TODO
 * 
 * @author runchen
 * 
 */

public interface CreditorRightsTransferRepository extends
		PagingAndSortingRepository<CreditorRightsTransfer, String>,
		JpaSpecificationExecutor<CreditorRightsTransfer> {

	CreditorRightsTransfer findByLotIdAndStatus(String lotId, String status);

	List<CreditorRightsTransfer> findByLotId(String lotId);

	@Modifying
	@Query(value = "UPDATE FP_CR_TSFR SET STATUS='04', LAST_MNT_OPID='admin', LAST_MNT_TS=CURRENT_TIMESTAMP WHERE STATUS='01'", nativeQuery = true)
	public int updateOverdueTransfer();

	public List<CreditorRightsTransfer> findByStatus(String status);

	public CreditorRightsTransfer findByTransferContractId(
			String transferContractId);

}
