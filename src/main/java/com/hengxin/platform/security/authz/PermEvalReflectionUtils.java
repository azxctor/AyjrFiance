package com.hengxin.platform.security.authz;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermEvalReflectionUtils {
    
//    public static DataPermissionEvaluator getTargetDomainPermEval(TargetDomainId targetDomainIdAnno, Object id, ApplicationContext appContext){
//        TargetDomain targetDomain = targetDomainIdAnno.targetClass().getAnnotation(TargetDomain.class);
//        if(targetDomain==null){
//            throw new DataPermissionMissConfigException("Wrong configuration: The target domain object has not been annotated as TargetDomain yet!");
//        }
//        Serializable targetId;
//        if(id instanceof Serializable){
//            targetId = (Serializable)id;
//        }else{
//            throw new DataPermissionMissConfigException("The target Id parameter is not Serialiazable!");
//        }
//        return appContext.getBean(targetDomain.permEvaluaor());
//    }
    
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fields;
    }
    
}
