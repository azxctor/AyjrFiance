/*
 * Project Name: standard-code-base
 * File Name: Label.java
 * Class Name: Label
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hengxin.platform.common.enums.EOptionCategory;

/**
 * Class Name: Label
 * <p>
 * Description: constant label/description annotation.
 * 
 * @author runchen
 * 
 */

@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptionCategory {
    /**
     * 
     * Description: the category of SysDict field.
     * 
     * @return
     */
    EOptionCategory value();
    
}
