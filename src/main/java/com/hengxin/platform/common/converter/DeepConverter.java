/*
 * Project Name: kmfex-platform
 * File Name: DeepConverter.java
 * Class Name: DeepConverter
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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;

import net.sf.cglib.core.Converter;

import com.hengxin.platform.fund.util.DateUtils;

/**
 * Class Name: DeepConverter Description: TODO
 * 
 * @author shengzhou
 * 
 */
public class DeepConverter implements Converter {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeStampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object convert(Object value, Class targetClass, Object context) {
        if (targetClass != null && value != null) {
            Class<? extends Object> sourceClass = value.getClass();
            if (sourceClass == targetClass) {
                return value;
            }
            // primitive type
            if (sourceClass.isPrimitive() || targetClass.isPrimitive()) {
                return value;
            }
            // collection
            if (Collection.class.isAssignableFrom(sourceClass) && Collection.class.isAssignableFrom(targetClass)) {
                // TODO
            } else if (Timestamp.class.isAssignableFrom(sourceClass) && targetClass.isAssignableFrom(String.class)) {
                return timeStampFormatter.format((Timestamp) value);
            } else if (Date.class.isAssignableFrom(sourceClass) && targetClass.isAssignableFrom(String.class)) {
                return dateFormatter.format((Date) value);
            } else if (java.util.Date.class.isAssignableFrom(targetClass) && sourceClass.isAssignableFrom(String.class)) {
                return DateUtils.getDate(value.toString(), "yyyy-MM-dd");
            } else if (targetClass.isAssignableFrom(sourceClass)) {

                return value;
            }
            // complex type
            if (targetClass.getClassLoader() != null && sourceClass.getClassLoader() != null) {
                return ConverterService.convert(value, targetClass, null);
            }

        }
        return null;
    }

}
