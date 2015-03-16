/*
 * Project Name: kmfex-platform
 * File Name: ActionHistoryUtil.java
 * Class Name: ActionHistoryUtil
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

package com.hengxin.platform.common.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.repository.ActionHistoryRepository;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.dto.ActionHistoryDto;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: ActionHistoryUtil.
 * 
 * @author chunlinwang
 * 
 */

@Service
@Transactional
public class ActionHistoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionHistoryService.class);

	private static final String ENTITY_TYPE = "entityType";
	private static final String ENTITY_ID = "entityId";
	private static final String EX_DESC = "Error comparing old object with new object, probably a bug, field:";
	
    @Autowired
    private SecurityContext secContext;

    @Autowired
    private ActionHistoryRepository actionHistoryRepo;

    @Autowired
    private AgencyRepository agencyRepository;

    /**
     * 
     * Description: 时间未传入，所以该方法会用 new Date()做为操作时间。
     * 
     * @param entityId
     *            被操作的实体的ID
     * @param action
     *            操作类型
     * @param result
     *            操作结果/说明
     * @return
     */
    public ActionHistoryPo save(EntityType entityType, String entityId, ActionType action, ActionResult result) {
        ActionHistoryPo actionHistory = constructActionHistory(entityType, entityId, new Date(), action, result,
                getEventId(), null, null, null, null);

        return save(actionHistory);
    }

    /**
     * 
     * Description: TODO
     * 
     * @param entityId
     * @param action
     * @param result
     * @param comment
     * @return
     */
    public ActionHistoryPo save(EntityType entityType, String entityId, ActionType action, ActionResult actionResult,
            String comment) {
        ActionHistoryPo actionHistory = constructActionHistory(entityType, entityId, new Date(), action, actionResult,
                getEventId(), null, null, null, comment);

        return save(actionHistory);
    }

    public void saveUserLog(String userId, String oldValue, String newValue, String userField) {
    	try {
    		ActionHistoryPo po = this.constructActionHistory(EntityType.USER, userId, new Date(), ActionType.EDIT, ActionResult.BASIC_INFO_EDIT, this.getEventId(), userField, oldValue, newValue, null);
        	this.actionHistoryRepo.save(po);	
		} catch (RuntimeException e) {
			LOGGER.error("EX {}", e);
		}
    }
    
    /**
     * 
     * Description: 注意：对于级联修改，该方法无法自动完成级联对象的日志插入，必需对级联对象再次调用相关方法( 通常需要调用saveForEditWithEventId方法来指定eventId)。
     * 
     * 可以使用getEventId()方法来返回eventId。
     * 
     * 例如： 对象A包含List[B]，需要先对A调用一次本方法，再对List[B]中的所有元素分别调用一次本方法。
     * 
     * @param oldObject
     *            老的对象
     * @param newObject
     *            新的对象
     * @param entityId
     *            被操作的实体的ID
     * @return
     */
    public Collection<ActionHistoryPo> saveForEdit(Object oldObject, Object newObject, String entityId,
            EntityType entityType) {

        return save(generateActionHistories(oldObject, newObject, entityId, entityType, ActionType.EDIT, null));

    }

    public Collection<ActionHistoryPo> saveForMaintain(Object oldObject, Object newObject, String entityId,
            EntityType entityType) {

        return save(generateActionHistories(oldObject, newObject, entityId, entityType, ActionType.MAINTAIN, null));

    }

    /**
     * 
     * Description: 注意：对于级联修改，该方法无法自动完成级联对象的日志插入，必需对级联对象再次调用该方法.
     * 
     * 例如： 对象A包含List[B]，需要先对A调用一次本方法，再对List[B]中的所有元素分别调用一次本方法。
     * 
     * @param oldObject
     *            老的对象
     * @param newObject
     *            新的对象
     * @param entityId
     *            被操作的实体的ID
     * @param eventId
     *            批量操作的eventId，相同的eventId用以表明这些操作时同一次操作
     * @return
     */
    public Collection<ActionHistoryPo> saveForEditWithEventId(Object oldObject, Object newObject, String entityId,
            EntityType entityType, String eventId) {

        return save(generateActionHistories(oldObject, newObject, entityId, entityType, ActionType.EDIT, eventId));

    }

    public Page<ActionHistoryPo> findUserLogByUserId(final String userId, Pageable pageRequest) {
        Specification<ActionHistoryPo> spec = new Specification<ActionHistoryPo>() {

            @Override
            public Predicate toPredicate(Root<ActionHistoryPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<EntityType> get(ENTITY_TYPE), EntityType.USER));
                if (!StringUtils.isEmpty(userId)) {
                    expressions.add(cb.equal(root.<String> get(ENTITY_ID), userId));

                }
                return predicate;
            }
        };
        return actionHistoryRepo.findAll(spec, pageRequest);
    }

    public Page<ActionHistoryPo> findProductLogByProductId(final String productId, Pageable pageRequest) {
        if (StringUtils.isBlank(productId)) {
            return null;
        }
        Specification<ActionHistoryPo> spec = new Specification<ActionHistoryPo>() {

            @Override
            public Predicate toPredicate(Root<ActionHistoryPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<EntityType> get(ENTITY_TYPE), EntityType.PRODUCT));
                expressions.add(cb.equal(root.<String> get(ENTITY_ID), productId));

                return predicate;
            }
        };
        return actionHistoryRepo.findAll(spec, pageRequest);
    }

    /**
     * 
     * Description: 查看融资包日志
     * 
     * @param packageId
     * @param pageRequest
     * @return
     */
    public Page<ActionHistoryPo> findFinancingpackageLogByPackageId(final String packageId, Pageable pageRequest) {
        if (StringUtils.isBlank(packageId)) {
            return null;
        }
        Specification<ActionHistoryPo> spec = new Specification<ActionHistoryPo>() {

            @Override
            public Predicate toPredicate(Root<ActionHistoryPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<EntityType> get(ENTITY_TYPE), EntityType.PRODUCTPACKAGE));
                expressions.add(cb.equal(root.<String> get(ENTITY_ID), packageId));

                return predicate;
            }
        };
        return actionHistoryRepo.findAll(spec, pageRequest);
    }

    public Page<ActionHistoryPo> findProductPkgLogByPkgIdAndActionType(final String pkgId,
            final List<ActionType> actionTypes, Pageable pageRequest) {
        Specification<ActionHistoryPo> spec = new Specification<ActionHistoryPo>() {

            @Override
            public Predicate toPredicate(Root<ActionHistoryPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<EntityType> get(ENTITY_TYPE), EntityType.PRODUCTPACKAGE));
                if (!StringUtils.isEmpty(pkgId)) {
                    expressions.add(cb.equal(root.<String> get(ENTITY_ID), pkgId));
                }
                if (actionTypes != null && !actionTypes.isEmpty()) {
                    expressions.add(root.<String> get("actionType").in(actionTypes));
                }
                return predicate;
            }
        };
        return actionHistoryRepo.findAll(spec, pageRequest);
    }

    public static String formatDate(Date date) {
        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public String getEventId() {
        return IdUtil.produce();
    }

    public ActionHistoryPo save(ActionHistoryPo actionHistory) {
        try {	
        	return actionHistoryRepo.save(actionHistory);
		} catch (Exception e) {
			LOGGER.error("saveForMaintain ex: {}", e);
			return null;
		}
    }

    public Collection<ActionHistoryPo> save(Collection<ActionHistoryPo> actionHistorys) {
    	try{
    		return actionHistoryRepo.save(actionHistorys);
		} catch (Exception e) {
			LOGGER.error("saveForMaintain ex: {}", e);
			return Collections.emptyList();
		}
    }

    /**
     * 
     * Description: TODO
     * 
     * @param entityId
     *            被操作的实体的ID
     * @param time
     *            操作时间
     * @param action
     *            操作类型
     * @param result
     *            操作结果/说明
     * @return
     */
    protected ActionHistoryPo save(EntityType entityType, String entityId, Date time, ActionType action,
            ActionResult result) {
        ActionHistoryPo actionHistory = constructActionHistory(entityType, entityId, time, action, result,
                getEventId(), null, null, null, null);

        return save(actionHistory);
    }

    /**
     * 
     * Description: TODO
     * 
     * @param entityId
     *            被操作的实体的ID
     * @param time
     *            操作时间
     * @param action
     *            操作类型
     * @param field
     *            被修改的域的名字（考虑配合@BusinessName）
     * @param oldValue
     *            原值
     * @param newValue
     *            新值
     * @param comment
     *            备注
     * @return
     */
    protected ActionHistoryPo saveForEdit(EntityType entityType, String entityId, Date time, ActionType action,
            String field, String oldValue, String newValue, String comment) {
        ActionHistoryPo actionHistory = constructActionHistory(entityType, entityId, time, action, null, getEventId(),
                field, oldValue, newValue, comment);

        return save(actionHistory);
    }

    protected List<ActionHistoryPo> generateActionHistories(Object oldObject, Object newObject, String entityId,
            EntityType entityType, ActionType actionType, String eventId) {

        List<ActionHistoryPo> result = new ArrayList<ActionHistoryPo>();
        if (oldObject == null || newObject == null || oldObject.getClass() != newObject.getClass()) {
            throw new BizServiceException(EErrorCode.REFLECT_EXCTPION,
                    "Old object is null or new object is null or old object type is not same as new one!");
        }

        Date currentDate = new Date();
        if (StringUtils.isEmpty(eventId)) {
            eventId = getEventId();
        }

        List<Field> fields = getAllFields(null, oldObject.getClass());
        for (Field field : fields) {
            if (isId(field) || isAssociatedOrTransient(field)) {
                // Assume ID field should not be changed; ignore associated and
                // transient fields.
                continue;
            }

            try {
                field.setAccessible(true);
                Object oldValue = field.get(oldObject);
                Field fieldInNewObject = getField(newObject.getClass(), field.getName());
                fieldInNewObject.setAccessible(true);
                Object newValue = fieldInNewObject.get(newObject);

                String businessName = field.getName();
                BusinessName annotation = getBusinessNameAnnotation(field);
                if (annotation != null) {
                    businessName = annotation.value();
                    if (annotation.ignore()) {
                        continue;
                    }
                    if (annotation.optionCategory() != EOptionCategory.NULL) {
                        if (oldValue != null && StringUtils.isNotBlank(oldValue.toString())) {
                            DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(annotation.optionCategory(),
                                    oldValue.toString());
                            oldValue = option == null ? oldValue : option.getText();
                        }
                        if (newValue != null && StringUtils.isNotBlank(newValue.toString())) {
                            DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(annotation.optionCategory(),
                                    newValue.toString());
                            newValue = option == null ? newValue : option.getText();
                        }
                    }
                }
                if (oldValue != null && PageEnum.class.isAssignableFrom(oldValue.getClass())) {
                    oldValue = ((PageEnum) oldValue).getText();
                } else if (oldValue != null && Date.class.isAssignableFrom(oldValue.getClass())) {
                    oldValue = DateUtils.formatDate(((Date) oldValue), "yyyy-MM-dd");
                }
                if (newValue != null && PageEnum.class.isAssignableFrom(newValue.getClass())) {
                    newValue = ((PageEnum) newValue).getText();
                } else if (newValue != null && Date.class.isAssignableFrom(newValue.getClass())) {
                    newValue = DateUtils.formatDate(((Date) newValue), "yyyy-MM-dd");
                }

                ActionHistoryPo actionHistoryPo = null;
                if (oldValue == null || StringUtils.isBlank(oldValue.toString())) {
                    if (newValue == null || StringUtils.isBlank(newValue.toString())) {
                        continue;
                    } else {
                        actionHistoryPo = constructActionHistory(entityType, entityId, currentDate, actionType, null,
                                eventId, businessName, null, newValue.toString(), null);
                        result.add(actionHistoryPo);

                    }
                } else if (newValue == null || StringUtils.isBlank(newValue.toString())) {
                    actionHistoryPo = constructActionHistory(entityType, entityId, currentDate, actionType, null,
                            eventId, businessName, oldValue.toString(), null, null);
                    result.add(actionHistoryPo);
                } else if (!oldValue.equals(newValue)) {
                    actionHistoryPo = constructActionHistory(entityType, entityId, currentDate, actionType, null,
                            eventId, businessName, oldValue.toString(), newValue.toString(), null);
                    result.add(actionHistoryPo);

                }

            } catch (IllegalArgumentException e) {
                throw new BizServiceException(EErrorCode.REFLECT_EXCTPION,
                        EX_DESC + field.getName(), e);
            } catch (IllegalAccessException e) {
                throw new BizServiceException(EErrorCode.REFLECT_EXCTPION,
                        EX_DESC + field.getName(), e);
            } catch (SecurityException e) {
                throw new BizServiceException(EErrorCode.REFLECT_EXCTPION,
                        EX_DESC + field.getName(), e);
            } catch (NoSuchFieldException e) {
                throw new BizServiceException(EErrorCode.REFLECT_EXCTPION,
                        EX_DESC + field.getName(), e);
            }

        }

        return result;

    }

    private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        if (clazz == null || StringUtils.isEmpty(name)) {
            return null;
        }
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (SecurityException e) {
        	LOGGER.debug("do nothing, the field maybe in super class");
        } catch (NoSuchFieldException e) {
        	LOGGER.debug("do nothing, the field maybe in super class");
        }

        if (field == null) {
            if (clazz.getSuperclass() == Object.class) {
                throw new NoSuchFieldException(name);
            } else {
                field = getField(clazz.getSuperclass(), name);
            }
        }
        return field;
    }

    private boolean isAssociatedOrTransient(Field field) {
        Annotation[] annos = getFieldAnnotations(field);
        return isTransient(annos) || isAssociated(annos) || isAssociated(getGetterAnnotations(field));
    }

    private boolean isTransient(Annotation[] annotations) {
        for (Annotation anno : annotations) {
            if (anno instanceof Transient) {
                return true;
            }
        }
        return false;
    }

    private boolean isAssociated(Annotation[] annotations) {
        for (Annotation anno : annotations) {
            if (anno instanceof OneToOne || anno instanceof OneToMany || anno instanceof ManyToOne
                    || anno instanceof ManyToMany) {
                return true;
            }
        }
        return false;
    }

    private Annotation[] getFieldAnnotations(Field field) {
        if (field == null) {
            return new Annotation[0];
        }
        return field.getAnnotations();

    }

    private Annotation[] getGetterAnnotations(Field field) {
        if (field == null) {
            return new Annotation[0];
        }

        String fieldName = field.getName();
        StringBuffer getter = null;
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            getter = new StringBuffer("is");
        } else {
            getter = new StringBuffer("get");
        }
        if (fieldName.length() > 1) {
            if (Character.isUpperCase(fieldName.charAt(1))) {
                getter.append(fieldName);
            } else {
                getter.append(Character.toUpperCase(fieldName.charAt(0))).append(fieldName.substring(1));

            }
        } else {
            getter.append(Character.toUpperCase(fieldName.charAt(0)));
        }

        try {
            Method method = field.getDeclaringClass().getDeclaredMethod(getter.toString());
            return method.getAnnotations();
        } catch (SecurityException e) {
        	LOGGER.error("SecurityException {}", e);
        } catch (NoSuchMethodException e) {
        	LOGGER.error("NoSuchMethodException {}", e);
        }

        return new Annotation[0];

    }

    private List<Field> getAllFields(List<Field> result, Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (result == null) {
            result = new ArrayList<Field>();
        }
        result.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != Object.class) {
            getAllFields(result, clazz.getSuperclass());
        }
        return result;
    }

    private boolean isId(Field field) {
        if (field == null) {
            return false;
        }
        Annotation[] fieldAnnotations = getFieldAnnotations(field);
        for (Annotation anno : fieldAnnotations) {
            if (anno instanceof Id) {
                return true;
            }
        }

        Annotation[] methodAnnotations = getGetterAnnotations(field);
        for (Annotation anno : methodAnnotations) {
            if (anno instanceof Id) {
                return true;
            }
        }

        return false;
    }

    private BusinessName getBusinessNameAnnotation(Field field) {
        if (field == null) {
            return null;
        }
        Annotation[] annotations = field.getAnnotations();
        for (Annotation anno : annotations) {
            if (anno instanceof BusinessName) {
                return (BusinessName) anno;
            }
        }

        return null;
    }

    protected ActionHistoryPo constructActionHistory(EntityType entityType, String entityId, Date time,
            ActionType action, ActionResult result, String batchId, String editField, String oldValue, String newValue,
            String comment) {

        ActionHistoryPo actionHistory = new ActionHistoryPo();
        actionHistory.setAction(action);
        actionHistory.setComment(comment);
        actionHistory.setNewValue(newValue);
        actionHistory.setOldValue(oldValue);
        actionHistory.setResult(result == null ? null : result.getText());
        actionHistory.setEventId(batchId);
        actionHistory.setEntityField(editField);
        actionHistory.setOperateTime(time);
        actionHistory.setOperateUser(getCurrentUserId());
        actionHistory.setEntityId(entityId);
        actionHistory.setEntityType(entityType);

        return actionHistory;
    }

    private String getCurrentUserId() {
        try {
        	return secContext.getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }

    // FIXME Not allowed DTO returned from service
    public List<ActionHistoryDto> mergeActionHistory(List<ActionHistoryDto> actionHistorys, boolean inCanViewPage) {
        if (actionHistorys == null) {
            return null;
        }
        List<String> index = new ArrayList<String>();
        return mergeByEventId(groupByEventId(actionHistorys, index), index, inCanViewPage);
    }

    // FIXME Not allowed DTO returned from service
    private Map<String, List<ActionHistoryDto>> groupByEventId(List<ActionHistoryDto> actionHistorys, List<String> index) {
        if (actionHistorys == null || index == null) {
            return null;
        }
        Map<String, List<ActionHistoryDto>> sortedActionHistorys = new HashMap<String, List<ActionHistoryDto>>();
        for (ActionHistoryDto dto : actionHistorys) {
            if (!sortedActionHistorys.containsKey(dto.getEventId())) {
                ArrayList<ActionHistoryDto> dtos = new ArrayList<ActionHistoryDto>();
                dtos.add(dto);
                sortedActionHistorys.put(dto.getEventId(), dtos);
                index.add(dto.getEventId());
            } else {
                sortedActionHistorys.get(dto.getEventId()).add(dto);
            }
        }
        return sortedActionHistorys;
    }

    // FIXME Not allowed DTO returned from service
    private List<ActionHistoryDto> mergeByEventId(Map<String, List<ActionHistoryDto>> groupedActionHistorys,
            List<String> index, boolean inCanViewPage) {
        if (groupedActionHistorys == null || index == null) {
            return null;
        }
        List<ActionHistoryDto> actionHistoryDtos = new ArrayList<ActionHistoryDto>();
        Map<String, ActionHistoryDto> actionHistoryDtoSet = new HashMap<String, ActionHistoryDto>();
        Set<Entry<String, List<ActionHistoryDto>>> set = groupedActionHistorys.entrySet();
        for (Entry<String, List<ActionHistoryDto>> entry : set) {
            List<ActionHistoryDto> list = entry.getValue();
            String eventId = entry.getKey();
            if (!list.isEmpty()) {
                StringBuilder result = new StringBuilder();
                ActionHistoryDto firstRecord = list.get(0);
                if (firstRecord.getAction() == ActionType.EDIT || firstRecord.getAction() == ActionType.MAINTAIN) {
                    for (ActionHistoryDto dto : list) {
                        String oldValue = maskField(dto, dto.getOldValue(), inCanViewPage);
                        String newValue = maskField(dto, dto.getNewValue(), inCanViewPage);
                        if ("授权服务中心".equals(dto.getEntityField())) {
                            if (StringUtils.isNotBlank(dto.getOldValue())) {
                                Agency agency = agencyRepository.findByUserId(oldValue);
                                oldValue = agency == null ? oldValue : agency.getName();
                            }
                            if (StringUtils.isNotBlank(dto.getNewValue())) {
                                Agency agency = agencyRepository.findByUserId(newValue);
                                newValue = agency == null ? newValue : agency.getName();
                            }
                        }

                        result.append("\"").append(dto.getEntityField()).append("\"从旧值:\"").append(oldValue)
                                .append("\"修改为新值:\"").append(newValue).append("\";");
                    }
                    firstRecord.setResult(result.toString());
                }
                actionHistoryDtoSet.put(eventId, firstRecord);
            }
        }
        for (String id : index) {
            actionHistoryDtos.add(actionHistoryDtoSet.get(id));
        }
        return actionHistoryDtos;
    }

    public String maskField(ActionHistoryDto dto, String value, boolean inCanViewPage) {
    	String newReturnValue = value;
        if (dto.getEntityType() == EntityType.USER) {
            String userId = dto.getEntityId();
            String field = dto.getEntityField();
            if (("姓名".equals(field) || "法人代表".equals(field) || "联系人".equals(field) || "银行账户名".equals(field))
                    && secContext.cannotViewRealName(userId, inCanViewPage)) {
            	newReturnValue = MaskUtil.maskChinsesName(newReturnValue);
            } else if (("手机".equals(field) || "法人手机".equals(field) || "联系人手机".equals(field))
                    && secContext.cannotViewRealPhoneNo(userId, inCanViewPage)) {
            	newReturnValue = MaskUtil.maskPhone(newReturnValue);
            } else if (("身份证号".equals(field) || "法人身份证号".equals(field))
                    && secContext.cannotViewRealIdCardNo(userId, inCanViewPage)) {
            	newReturnValue = MaskUtil.maskCardNumber(newReturnValue);
            } else if ("银行账号".equals(field) && secContext.cannotViewRealBankCardNo(userId, inCanViewPage)) {
            	newReturnValue = MaskUtil.maskCardNumber(newReturnValue);
            }
        }
        return StringUtils.isNotBlank(newReturnValue) ? newReturnValue : "空";
    }

}
