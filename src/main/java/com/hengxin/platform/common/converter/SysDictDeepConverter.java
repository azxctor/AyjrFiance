/*
 * Project Name: kmfex-platform
 * File Name: SysDictDeepConverter.java
 * Class Name: SysDictDeepConverter
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

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.util.SystemDictUtil;

/**
 * Class Name: SysDictDeepConverter Description: TODO
 * 
 * @author runchen
 * 
 */
public final class SysDictDeepConverter extends DeepConverter {

    @SuppressWarnings("rawtypes")
    @Override
    public Object convert(Object value, Class targetClass, Object context) {
        Object target = super.convert(value, targetClass, context);
        if (target == null && value != null) {
            Class<? extends Object> sourceClass = value.getClass();
            if (sourceClass.equals(DynamicOption.class) && targetClass.equals(String.class)) {
                String code = ((DynamicOption) value).getCode();
                return StringUtils.isNotBlank(code) ? code : null;
            } else if (sourceClass.equals(String.class) && targetClass.equals(DynamicOption.class) && context != null) {
                String setterMethod = context.toString();
                String fieldName = setterMethod.replace("set", "");
                fieldName = fieldName.replace(fieldName.substring(0, 1), fieldName.substring(0, 1).toLowerCase());
                Field field = (Field) context;
                OptionCategory annotation = field.getAnnotation(OptionCategory.class);
                if (annotation != null) {
                    return SystemDictUtil.getDictByCategoryAndCode(annotation.value(), value.toString());
                }
            }
        }
        return target;
    }

}
