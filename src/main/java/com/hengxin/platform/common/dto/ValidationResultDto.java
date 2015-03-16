/*
 * Project Name: standard-code-base
 * File Name: ValidationResultDto.java
 * Class Name: ValidationResultDto
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
package com.hengxin.platform.common.dto;

import java.util.Map;

/**
 * 
 * Class Name: ValidationResultDto
 * <p>
 * Description: the validation result for form.
 * 
 * @author minhuang
 * 
 */
public class ValidationResultDto {

    private String formId;

    private String objectName;

    private Object generalError;

    private Map<String, Object> fieldErrors;

    /**
     * @return return the value of the var formId
     */

    public String getFormId() {
        return formId;
    }

    /**
     * @param formId
     *            Set formId value
     */

    public void setFormId(String formId) {
        this.formId = formId;
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

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return return the value of the var generalError
     */

    public Object getGeneralError() {
        return generalError;
    }

    /**
     * @param generalError
     *            Set generalError value
     */

    public void setGeneralError(Object generalError) {
        this.generalError = generalError;
    }

    /**
     * @return return the value of the var fieldErrors
     */

    public Map<String, Object> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * @param fieldErrors
     *            Set fieldErrors value
     */

    public void setFieldErrors(Map<String, Object> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldErrors == null) ? 0 : fieldErrors.hashCode());
        result = prime * result + ((formId == null) ? 0 : formId.hashCode());
        result = prime * result + ((generalError == null) ? 0 : generalError.hashCode());
        result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValidationResultDto other = (ValidationResultDto) obj;
        if (fieldErrors == null) {
            if (other.fieldErrors != null)
                return false;
        } else if (!fieldErrors.equals(other.fieldErrors))
            return false;
        if (formId == null) {
            if (other.formId != null)
                return false;
        } else if (!formId.equals(other.formId))
            return false;
        if (generalError == null) {
            if (other.generalError != null)
                return false;
        } else if (!generalError.equals(other.generalError))
            return false;
        if (objectName == null) {
            if (other.objectName != null)
                return false;
        } else if (!objectName.equals(other.objectName))
            return false;
        return true;
    }
}
