package com.hengxin.platform.product.converter;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.dto.ProductPackageSubscribesDto;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: ProductPackageSubscribesConverter Description: TODO
 * 
 * @author junwei
 * 
 */

public class ProductPackageSubscribesConverter implements
        ObjectConverter<ProductPackageSubscribesDto, PackageSubscribes> {

    @Override
    public void convertFromDomain(PackageSubscribes domain, ProductPackageSubscribesDto dto) {
        if (domain != null) {
            dto.setId(domain.getId());
            dto.setCreateTs(DateUtils.formatDate(domain.getCreateTs(), "yyyy-MM-dd HH:mm:ss"));
            dto.setSupplyAmount(domain.getUnitAmt().multiply(new BigDecimal(domain.getUnit())));
            UserPo user = domain.getUser();
            if (user != null) {
                dto.setUserName(domain.getUser().getName());
            }
        }
    }

    @Override
    public void convertToDomain(ProductPackageSubscribesDto dto, PackageSubscribes domain) {

    }

    //名字格式，如：王**
    @SuppressWarnings("unused")
	private String getFormatUserName(String userName) {
        if(StringUtils.isBlank(userName))
        {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        String preName = userName.substring(0, 1);
        buffer.append(preName);
        String afterName = userName.substring(1, userName.length());
        char ch[] = afterName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            buffer.append("*");
        }
        return buffer.toString();
    }
}
