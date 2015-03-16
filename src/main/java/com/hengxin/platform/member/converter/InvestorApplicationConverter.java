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
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.repository.ServiceCenterInfoRepository;
import com.hengxin.platform.member.repository.UserRepository;

@Component
public class InvestorApplicationConverter implements ObjectConverter<InvestorDto, InvestorApplication> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgencyRepository agencyRepository;
    
    @Autowired
    private ServiceCenterInfoRepository serviceCenterInfoRepository;

    @Override
    public void convertFromDomain(InvestorApplication domain, InvestorDto dto) {
        if (StringUtils.isNotBlank(domain.getAuthCenterId())) {
            Agency agency = agencyRepository.findOne(domain.getAuthCenterId());
            AgencyDto agencyDto = ConverterService.convert(agency, AgencyDto.class);
            ServiceCenterInfo serviceCenter = serviceCenterInfoRepository.findOne(domain.getAuthCenterId());
            if (serviceCenter != null) {
            	if (serviceCenter.getServiceCenterDesc() == null || serviceCenter.getServiceCenterDesc().isEmpty()) {
                	agencyDto.setDesc(agency.getName());
    			} else {
    				agencyDto.setDesc(serviceCenter.getServiceCenterDesc());
    			}	
			}
            dto.setAuthCenter(agencyDto);
        }
    }

    @Override
    public void convertToDomain(InvestorDto dto, InvestorApplication domain) {

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
