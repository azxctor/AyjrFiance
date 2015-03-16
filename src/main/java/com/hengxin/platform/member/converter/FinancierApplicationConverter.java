/*
 * Project Name: kmfex-platform
 * File Name: InvestorInfoConverter.java
 * Class Name: InvestorInfoConverter
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
package com.hengxin.platform.member.converter;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.repository.UserRepository;

/**
 * Class Name: InvestorInfoConverter Description:
 * 
 * @author runchen
 * @param <F>
 * 
 */
@Component
public class FinancierApplicationConverter implements ObjectConverter<FinancierDto, FinancierApplication> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Override
    public void convertFromDomain(FinancierApplication domain, FinancierDto dto) {
        if (StringUtils.isNotBlank(domain.getAuthCenterId())) {
            Agency agency = agencyRepository.findOne(domain.getAuthCenterId());
            AgencyDto agentcy = ConverterService.convert(agency, AgencyDto.class);
            dto.setAuthCenter(agentcy);
        }
    }

    @Override
    public void convertToDomain(FinancierDto dto, FinancierApplication domain) {

        Date now = new Date();
        domain.setLastMntTs(now);
        if (dto.getCreateTs() == null) {
            domain.setCreateTs(now);
        }

        if (dto.getAuthCenter() != null) {
            domain.setAuthCenterId(dto.getAuthCenter().getUserId());
        }
    }

}
