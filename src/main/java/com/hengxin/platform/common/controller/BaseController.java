/*
 * Project Name: kmfex-platform
 * File Name: BaseController.java
 * Class Name: BaseController
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

package com.hengxin.platform.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.SysMenuDto;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.SystemDictUtil;

/**
 * Class Name: BaseController Description: TODO
 * 
 * @author zhengqingye
 * 
 */

public abstract class BaseController {

    /**
     * Description: get all static options from a enum object for display.
     * 
     * @param enumClass
     * @return
     */

    @Autowired
    private LocalValidatorFactoryBean validator;

    protected void validate(final Object validatedObj, final Class<?>[] groups) {
        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(validatedObj, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(ApplicationConstant.MANUAL_VALIDATE, constraintViolations);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List<EnumOption> getStaticOptions(Class<? extends Enum> enumClass, boolean containBlankOption) {
        List<Enum> enumList = EnumHelper.inspectConstants(enumClass, containBlankOption);
        List<EnumOption> options = new ArrayList<EnumOption>();
        for (Enum e : enumList) {
            options.add(new EnumOption(e.name(), ((PageEnum) e).getText()));
        }
        return options;
    }

    /**
     * Description: get all static options from a DynamicOption object for display.
     * 
     * @param optionCategory
     * @param containBlankOption
     * @return
     */

    protected List<DynamicOption> getDynamicOptions(EOptionCategory optionCategory, boolean containBlankOption) {
        return SystemDictUtil.getRootDictList(optionCategory, containBlankOption);
    }

    /**
     * Description: get all children static options from a DynamicOption object by parent code for display.
     * 
     * @param optionCategory
     * @param containBlankOption
     * @return
     */

    protected List<DynamicOption> getChildrenDynamicOptions(EOptionCategory optionCategory, String parentCode,
            boolean containBlankOption) {
        return SystemDictUtil.getChildrenDictByParentCode(optionCategory, parentCode, containBlankOption);
    }

    protected EFormMode getFormMode(String mode) {
        return EnumHelper.translateByLabel(EFormMode.class, mode);
    }

    /**
     * TODO: yingchang-set default menu
     * 
     * @param session
     * @param firstLevelMenu
     * @param secondLevelMenu
     */
    protected void setDefaultMenu(HttpSession session, String firstLevelMenu, String secondLevelMenu) {
        SysMenuDto sysmenu = (SysMenuDto) session.getAttribute("sysmenu");
        if (StringUtils.isNotBlank(firstLevelMenu)) {
            sysmenu.setFirstLevelHighlight(firstLevelMenu);
        }
        if (StringUtils.isNotBlank(secondLevelMenu)) {
            sysmenu.setSecondLevelHighlight(secondLevelMenu);
        }
    }

    protected List<DynamicOption> getMemberLevelList() {
        // List<DynamicOption> result = new ArrayList<DynamicOption>();
        // List<DynamicOptionPo> dynOptions = memberService.getDynOptions(EOptionCategory.MEMBER_LEVEL.getCode(), "Y");
        // for (DynamicOptionPo dynamicOptionPo : dynOptions) {
        // result.add(ConverterService.convert(dynamicOptionPo, DynamicOption.class));
        // }
        return SystemDictUtil.getRootDictList(EOptionCategory.MEMBER_LEVEL, false);
    }
}
