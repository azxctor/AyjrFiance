package com.hengxin.platform.common.converter;

import org.codehaus.plexus.util.StringUtils;

import com.hengxin.platform.common.domain.BusinessSigninJournal;
import com.hengxin.platform.common.dto.BusinessSigninJournalDto;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * Class Name: BusinessSigninJournalConverter Description: TODO
 * 
 * @author junwei
 * 
 */

public class BusinessSigninJournalConverter implements ObjectConverter<BusinessSigninJournalDto, BusinessSigninJournal> {

    @Override
    public void convertFromDomain(BusinessSigninJournal domain, BusinessSigninJournalDto dto) {
        if (domain != null) {
            dto.setWorkDate(DateUtils.formatDate(domain.getWorkDate(), "yyyy-MM-dd"));
            if(null != domain.getSignDate()) {
            dto.setSignDate(DateUtils.formatDate(domain.getSignDate(), "yyyy-MM-dd"));
            }
            dto.setOperateType(StringUtils.equals("O", domain.getOperateType()) ? "开市" : "闭市");
            if (domain.getCreateTime() != null) {
                dto.setCreateTime(DateUtils.formatDate(domain.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    @Override
    public void convertToDomain(BusinessSigninJournalDto dto, BusinessSigninJournal domain) {
    }

}
