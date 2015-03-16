package com.hengxin.platform.common.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.entity.DynamicOptionPo;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.repository.DynamicOptionRepository;
import com.hengxin.platform.member.domain.InvestorLevel;
import com.hengxin.platform.member.repository.InvestorLevelRepository;

/**
 * System dictionary.
 * 
 * @author tingyu
 * 
 */
public final class SystemDictUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemDictUtil.class);

    private static DynamicOptionRepository sysDictRepository;
    
	private static InvestorLevelRepository investorLevelRepository;
    
    private static List<String> UNMEANING_PARENT_CODE = new ArrayList<String>();
    
    private static List<DynamicOption> investorLevels = new ArrayList<DynamicOption>();
    private static Map<String, DynamicOption> INVESTOR_LEVEL_MAP = new ConcurrentHashMap<String, DynamicOption>();

    private static Map<Long, DynamicOption> SYS_DIC_MAP = new ConcurrentHashMap<Long, DynamicOption>();
    private static Map<String, Map<String, DynamicOption>> SYS_DIC_CODE_MAP = new ConcurrentHashMap<String, Map<String, DynamicOption>>();
    private static Map<String, List<DynamicOption>> ROOT_DICT_MAP = new ConcurrentHashMap<String, List<DynamicOption>>();

    private static Map<String, Map<String, List<DynamicOption>>> CHILDREN_DIC_MAP = new ConcurrentHashMap<String, Map<String, List<DynamicOption>>>();

    static {
        UNMEANING_PARENT_CODE.add("0000");
    }

    private SystemDictUtil() {

    }

    /**
     * Description: Should be called only when Spring start or synchronous data from database.
     * 
     */

    public static void initAndRefresh() {
        int count = 0;
        synchronized (SYS_DIC_MAP) {
            SYS_DIC_MAP.clear();
            SYS_DIC_CODE_MAP.clear();
            ROOT_DICT_MAP.clear();
            CHILDREN_DIC_MAP.clear();
            // FIXME get all
            List<DynamicOptionPo> list = sysDictRepository.findByEnabled(ApplicationConstant.ENABLE_CODE);
            for (DynamicOptionPo po : list) {
                DynamicOption sysDict = ConverterService.convert(po, DynamicOption.class);

                SYS_DIC_MAP.put(sysDict.getId(), sysDict);

                String category = sysDict.getCategory();
                if (!SYS_DIC_CODE_MAP.containsKey(category)) {
                    SYS_DIC_CODE_MAP.put(category, new ConcurrentHashMap<String, DynamicOption>());
                }
                SYS_DIC_CODE_MAP.get(category).put(sysDict.getCode(), sysDict);

                String parentCode = sysDict.getParentCode();
                if (isParentCodeBlank(parentCode)) {
                    if (!ROOT_DICT_MAP.containsKey(category)) {
                        ROOT_DICT_MAP.put(category, new ArrayList<DynamicOption>());
                    }
                    ROOT_DICT_MAP.get(category).add(sysDict);
                } else {
                    if (!CHILDREN_DIC_MAP.containsKey(category)) {
                        CHILDREN_DIC_MAP.put(category, new ConcurrentHashMap<String, List<DynamicOption>>());
                    }
                    if (!CHILDREN_DIC_MAP.get(category).containsKey(parentCode)) {
                        CHILDREN_DIC_MAP.get(category).put(parentCode, new LinkedList<DynamicOption>());
                    }
                    CHILDREN_DIC_MAP.get(category).get(parentCode).add(sysDict);
                }
                count++;
            }
            List<InvestorLevel> investorLevelList = investorLevelRepository.findAll();
            for (InvestorLevel level : investorLevelList) {
				DynamicOption option = new DynamicOption();
				option.setCode(level.getLevelId());
				option.setText(level.getLevelName());
				option.setLongText(level.getLevelName());
				option.setEnabled("Y");
				investorLevels.add(option);
				INVESTOR_LEVEL_MAP.put(option.getCode(), option);
			}
            // TODO sort children
        }
        LOGGER.info("Kmfex platform INFO: Reload " + count + " records from SYS_DICT table!");
    }

    public static List<DynamicOption> getRootDictList(EOptionCategory category) {
        return getRootDictList(category, true);
    }

    /**
     * Get collection by category.<br/>
     * Pay attention for <strong>INVESTOR_LEVEL</strong>, it exist at UM_INVSTR_LVL rather than GL_DICT.
     * @param category
     * @param hasEmpty
     * @return
     */
    public static List<DynamicOption> getRootDictList(EOptionCategory category, boolean hasEmpty) {
    	if (EOptionCategory.INVESTOR_LEVEL == category) {
			return investorLevels;
		}
        List<DynamicOption> list = new LinkedList<DynamicOption>();
        if (hasEmpty) {
            list.add(new DynamicOption("", "", ""));
        }
        List<DynamicOption> rootList = ROOT_DICT_MAP.get(category.getCode());
        if (rootList != null) {
            list.addAll(rootList);
        }
        return list;
    }

    /**
     * display new investor level.
     */
    public static DynamicOption getDictByCategoryAndCode(EOptionCategory category, String code) {
    	if (EOptionCategory.INVESTOR_LEVEL == category) {
    		if (INVESTOR_LEVEL_MAP.containsKey(code)) {
				return INVESTOR_LEVEL_MAP.get(code);
			} else {
				LOGGER.warn("Invalid INVESTOR LEVEL {}", code);
			}
    	}
        Map<String, DynamicOption> dictMap = SYS_DIC_CODE_MAP.get(category.getCode());
        if (dictMap != null && StringUtils.isNotBlank(code)) {
            return dictMap.get(code);
        }
        return null;
    }

    public static DynamicOption getParentDict(DynamicOption option) {
        Map<String, DynamicOption> dictMap = SYS_DIC_CODE_MAP.get(option.getCategory());
        if (dictMap != null && StringUtils.isNotBlank(option.getParentCode())) {
            return dictMap.get(option.getParentCode());
        }
        return null;
    }

    public static List<DynamicOption> getChildrenDictByParentCode(EOptionCategory category, String parentCode) {
        return getChildrenDictByParentCode(category, parentCode, true);
    }

    public static List<DynamicOption> getChildrenDictByParentCode(EOptionCategory category, String parentCode,
            boolean hasEmpty) {
        List<DynamicOption> list = new LinkedList<DynamicOption>();
        if (hasEmpty) {
            list.add(new DynamicOption("", "", ""));
        }
        Map<String, List<DynamicOption>> dictMap = CHILDREN_DIC_MAP.get(category.getCode());
        if (dictMap != null) {
            List<DynamicOption> dictList = dictMap.get(parentCode);
            if (dictList != null) {
                list.addAll(dictList);
            }
        }
        return list;
    }

    public static String getFullTextWithParent(DynamicOption option) {
        StringBuilder sb = new StringBuilder();
        String parentCode = option.getParentCode();
        if (StringUtils.isNotBlank(parentCode) && !UNMEANING_PARENT_CODE.contains(parentCode)) {
            DynamicOption parent = SYS_DIC_CODE_MAP.get(option.getCategory()).get(parentCode);
            if (parent != null) {
                sb.append(getFullTextWithParent(parent));
            }
        }
        sb.append(option.getLongText());
        return sb.toString();
    }

    public static void setSysDictRepository(DynamicOptionRepository sysDictRepository) {
        SystemDictUtil.sysDictRepository = sysDictRepository;
    }
    
    public static void setInvestorLevelRepository(InvestorLevelRepository investorLevelRepository) {
		SystemDictUtil.investorLevelRepository = investorLevelRepository;
	}

	public static String[] getMultilayerDetail(DynamicOption option, int length) {
        String[] detail = new String[length];
        populateParentDetail(option, detail, 0);
        return detail;
    }

    private static int populateParentDetail(DynamicOption option, String[] detail, int index) {
        int i = 0;
        if (option != null) {
            if (!isParentCodeBlank(option.getParentCode())) {
                i = populateParentDetail(getParentDict(option), detail, index);
            }
            detail[i] = option.getCode();
        }
        return ++i;
    }

    private static boolean isParentCodeBlank(String parentCode) {
        return StringUtils.isBlank(parentCode) || UNMEANING_PARENT_CODE.contains(parentCode);
    }

}
