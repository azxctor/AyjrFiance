/*
 * Project Name: standard-code-base
 * File Name: DomainField.java
 * Class Name: DomainField
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

package com.hengxin.platform.common.dto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class Name: DistrictCheck
 * <p>
 * Description: the distict annotation
 * 
 * @author minhuang
 * 
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Domain {

    /**
     * 
     * Description: the domain class
     * 
     * @return
     */
    Class<?>[] types() default {};

    /**
     * 
     * Description: TODO
     * 
     * @return
     */
    boolean firstValidate() default true;

    /**
     * 
     * Description: if true, then the <code>groups</code> will be impact.
     * 
     * @return
     */
    boolean strict() default false;
}
