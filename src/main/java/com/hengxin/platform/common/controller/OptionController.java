
/*
 * Project Name: kmfex-platform
 * File Name: OptionController.java
 * Class Name: OptionController
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.downstream.TreeOptionDto;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.SystemDictUtil;


/**
 * Class Name: OptionController Description: TODO
 * 
 * @author runchen
 * 
 */
@Controller
public class OptionController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OptionController.class);

    @RequestMapping(value = "/option/{category}/{code}", method = RequestMethod.GET)
    @ResponseBody
    public List<DynamicOption> getChildrenOptionList(@PathVariable("category") String category,
            @PathVariable("code") String code) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getChildrenOptionList() start: " + category);
        }
        EOptionCategory categoryEnum = EnumHelper.translate(EOptionCategory.class, category.toUpperCase());
        return SystemDictUtil.getChildrenDictByParentCode(categoryEnum, code);
    }

    @RequestMapping(value = "/option/treeitem/{category}/{onlyLeafSelected}", method = RequestMethod.GET)
    @ResponseBody
    public List<TreeOptionDto> getTreeOption(@PathVariable("category") String category,
            @PathVariable("onlyLeafSelected") String onlyLeafSelected) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getChildrenOptionList() start: " + category);
        }
        boolean parentNoteSelected = StringUtils.isBlank(onlyLeafSelected) || "N".equals(onlyLeafSelected);
        EOptionCategory categoryEnum = EnumHelper.translate(EOptionCategory.class, category.toUpperCase());
        List<DynamicOption> rootDictList = SystemDictUtil.getRootDictList(categoryEnum);
        return generateTreeDtoList(categoryEnum, rootDictList, parentNoteSelected);
    }

    private List<TreeOptionDto> generateTreeDtoList(EOptionCategory categoryEnum, List<DynamicOption> rootDictList, boolean parentNoteSelected) {
        List<TreeOptionDto> list = new ArrayList<TreeOptionDto>();
        for (DynamicOption option : rootDictList) {
            TreeOptionDto dto = new TreeOptionDto();
            if (parentNoteSelected || "Y".equals(option.getLeaf())) {
                dto.setId(option.getCode());
            }
            dto.setText(option.getText());
            dto.setChildren(getChildrenTreeOptions(categoryEnum, option.getCode(), parentNoteSelected));
            list.add(dto);
        }
        return list;
    }

    private List<TreeOptionDto> getChildrenTreeOptions(EOptionCategory category, String parentCode, boolean parentNoteSelected) {
        List<DynamicOption> children = SystemDictUtil.getChildrenDictByParentCode(category, parentCode, false);
        if (CollectionUtils.isNotEmpty(children)) {
            List<TreeOptionDto> list = generateTreeDtoList(category, children, parentNoteSelected);
            return list;
        }
        return null;
    }

}
