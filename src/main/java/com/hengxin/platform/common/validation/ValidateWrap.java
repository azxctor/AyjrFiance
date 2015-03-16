/*
 * Project Name: standard-code-base
 * File Name: ValidateWrap.java
 * Class Name: ValidateWrap
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

package com.hengxin.platform.common.validation;

import java.util.List;

import org.springframework.validation.Validator;

/**
 * Class Name: ValidateWrap Description: the validate wrap clss.
 * 
 * @author minhuang
 * 
 */
public class ValidateWrap {

    private String objectName;

    private Object target;

    private List<Validator> validatores;

    public ValidateWrap() {

    }

    public ValidateWrap(final String objectName, final Object target, final List<Validator> validatores) {
        this.objectName = objectName;
        this.target = target;
        this.validatores = validatores;
    }

    /**
     * @return return the value of the var objectName
     */

    public String getObjectName() {
        return objectName;
    }

    /**
     * @param objectName
     *            Set objectName value
     */

    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return return the value of the var target
     */

    public Object getTarget() {
        return target;
    }

    /**
     * @param target
     *            Set target value
     */

    public void setTarget(final Object target) {
        this.target = target;
    }

    /**
     * @return return the value of the var validator
     */

    public List<Validator> getValidatores() {
        return validatores;
    }

    /**
     * @param validator
     *            Set validator value
     */

    public void setValidatores(final List<Validator> validatores) {
        this.validatores = validatores;
    }
}
