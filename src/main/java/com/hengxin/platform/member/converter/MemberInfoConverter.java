/*
 * Project Name: kmfex-platform
 * File Name: ApplicationConverterService.java
 * Class Name: ApplicationConverterService
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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.dto.MemberDto;

/**
 * Class Name: ApplicationConverterService Description:
 * 
 * @author runchen
 * @param <F>
 * 
 */
@Component
public class MemberInfoConverter implements ObjectConverter<MemberDto, Member> {

    @Autowired
    private BankAcctRepository bankRepo;

    @Override
    public void convertFromDomain(Member domain, MemberDto dto) {
        List<BankAcctPo> bankList = bankRepo.findBankAcctByUserId(domain.getUserId());
        if (CollectionUtils.isNotEmpty(bankList)) {
            BankAcctPo bank = bankList.get(0);
            dto.setBankAccount(bank.getBnkAcctNo());
            dto.setBankAccountName(bank.getBnkAcctName());
            dto.setBankShortName(SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.BANK, bank.getBnkCd()));
            dto.setBankFullName(bank.getBnkName());
            dto.setBankCardFrontImg(bank.getBnkCardImg());
            
            DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, bank.getBnkOpenProv());
            dto.setBankOpenProvince(option);
            DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, bank.getBnkOpenCity());
            dto.setBankOpenCity(option2);
//            dto.setBankOpenProvince(bank.getBnkOpenProv());
//            dto.setBankOpenCity(bank.getBnkOpenCity());
            dto.setBankOpenBranch(bank.getBnkBrch());
        }
    }

    @Override
    public void convertToDomain(MemberDto dto, Member domain) {

    }
}
