/*
 * Project Name: kmfex-platform
 * File Name: ConverterService.java
 * Class Name: ConverterService
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
package com.hengxin.platform.common.converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.hengxin.platform.common.util.ApplicationContextUtil;

/**
 * Class Name: ConverterService Description: TODO
 * 
 * @author runchen
 * 
 */
@SuppressWarnings("rawtypes")
public final class ConverterService {

    private static final Map<String, BeanCopier> cachedCopierMap = new ConcurrentHashMap<String, BeanCopier>();
    private static final Map<String, ObjectConverter> cachedCustomConverterMap = new ConcurrentHashMap<String, ObjectConverter>();
    private static final String PO = "Po";
    private static final String DTO = "Dto";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterService.class);
    
    
    /**
    * Description: this method will be removed.
    *
    * @param source
    * @param targetClass
    * @param converter
    * @param customConverterClass
    * @return
    */
    	
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <T, F> F convert(T source, Class<F> targetClass, Converter converter,
            Class<? extends ObjectConverter> customConverterClass) {
        if (source == null || targetClass == null) {
            return null;
        }
        if (source.getClass().equals(targetClass)) {
            return (F) source;
        }

        try {
            F target = targetClass.newInstance();
            copy(source, target, converter, customConverterClass);
            return target;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static <T, F> F convert(T source, F target, Converter converter,
            Class<? extends ObjectConverter> customConverterClass) {
        if (source == null || target == null) {
            return null;
        }

        copy(source, target, converter, customConverterClass);
        return target;
    }

    /** Overloaded methods */

    public static <T, F> F convert(T source, Class<F> targetClass) {
        return convert(source, targetClass, new SysDictDeepConverter(), null);
    }

    public static <T, F> F convert(T source, F target) {
        return convert(source, target, new SysDictDeepConverter(), null);
    }

    public static <T, F> F convert(T source, Class<F> targetClass,
            Class<? extends ObjectConverter> customConverterClass) {
        return convert(source, targetClass, new SysDictDeepConverter(), customConverterClass);
    }

    @Deprecated
    public static <T, F> F convert(T source, F target,
            Class<? extends ObjectConverter> customConverterClass) {
        return convert(source, target, new SysDictDeepConverter(), customConverterClass);
    }

    /** Private methods */

    @SuppressWarnings("unchecked")
    private static <T, F> void copy(T source, F target, Converter converter,
            Class<? extends ObjectConverter> customConverterClass) {
        BeanCopier beanCopier = getBeanCopierInstance(source, target.getClass(), converter);
        beanCopier.copy(source, target, converter);
        ObjectConverter customConverter = getCustomConverterInstance(customConverterClass);
        if (customConverter != null) {
            if (target.getClass().getName().endsWith(PO) || target.getClass().getName().endsWith(DTO)) {
                customConverter.convertFromDomain(source, target);
            } else if (source.getClass().getName().endsWith(PO) || source.getClass().getName().endsWith(DTO)) {
                customConverter.convertToDomain(source, target);
            }
        }
    }

    private static <T, F> BeanCopier getBeanCopierInstance(T source, Class<F> targetClass, Converter converter) {
        String key = source.getClass().getName() + "#" + targetClass.getName();
        BeanCopier beanCopier = cachedCopierMap.get(key);
        if (beanCopier == null) {
            synchronized (cachedCopierMap) {
                beanCopier = cachedCopierMap.get(key);
                if (beanCopier == null) {
                    beanCopier = TypeAwareBeanCopier.instantiate(source.getClass(), targetClass, converter != null);
                    cachedCopierMap.put(key, beanCopier);
                }
            }
        }
        return beanCopier;
    }

    private static <T, F> ObjectConverter getCustomConverterInstance(
            Class<? extends ObjectConverter> customConverterClass) {
        if (customConverterClass == null) {
            return null;
        }
        String key = customConverterClass.getName();
        ObjectConverter converter = cachedCustomConverterMap.get(key);
        if (converter == null) {
            synchronized (cachedCustomConverterMap) {
                try {
                    converter = ApplicationContextUtil.getBean(customConverterClass);
                } catch (BeansException e) {
                    LOGGER.info(customConverterClass.getName() + " is not a component, need new instance.");
                }
                if (converter == null) {
                    try {
                        converter = customConverterClass.newInstance();
                        cachedCustomConverterMap.put(key, converter);
                    } catch (InstantiationException e) {
                        return null;
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                }
            }
        }
        return converter;
    }

}
