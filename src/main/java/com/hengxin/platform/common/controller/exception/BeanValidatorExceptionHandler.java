package com.hengxin.platform.common.controller.exception;

/*
 * Project Name: kmfex-platform
 * File Name: ExceptionHandler.java
 * Class Name: ExceptionHandler
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ValidationResultDto;
import com.hengxin.platform.common.dto.annotation.Domain;

/**
 * Class Name: ExceptionHandler
 * <p>
 * Description: the <code>ValidateException</code> handler<br>
 * the validation from service will be wrapped into <code>ValidateException</code>, then the handler will catch the
 * exception and return the errors into view
 * 
 * @author minhuang
 * 
 */
@Service
public class BeanValidatorExceptionHandler extends ExceptionHandler {

    /**
     * 
     * Description: set the validation data.
     * 
     * @param constraintsViolatioins
     * @param handler
     * @param formId
     * @param error
     */
    /* (non-Javadoc)
    * @see com.hengxin.platform.common.controller.exception.ExceptionHandler#setValidationErrorData(java.lang.Exception, java.lang.Object, java.lang.String, com.kmfex.platform.common.dto.ResultDto)
    */
    @Override
    protected void setValidationErrorData(final Exception ex, final Object handler, final String formId, ResultDto error) {
        ConstraintViolationException vex = (ConstraintViolationException) ex;
        Set<ConstraintViolation<?>> constraintsViolatioins = vex.getConstraintViolations();
        @SuppressWarnings("unchecked")
        final List<ValidationResultDto> errorData = (List<ValidationResultDto>) error.getData();
        if (StringUtils.isNotEmpty(formId) && constraintsViolatioins != null && constraintsViolatioins.size() > 0
                && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // method parameter arrays
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            if (methodParameters != null && methodParameters.length > 0
                    && !ApplicationConstant.MANUAL_VALIDATE.equals(vex.getMessage())) {
                for (ConstraintViolation<?> constraintViolation : constraintsViolatioins) {
                    Class<?> doaminClass = constraintViolation.getRootBeanClass();
                    for (MethodParameter methodParameter : methodParameters) {
                        Class<?> dtoClass = methodParameter.getParameterType();
                        Domain dtoAnnotation = dtoClass.getAnnotation(Domain.class);
                        if (dtoAnnotation == null && !dtoClass.equals(doaminClass)) {
                            continue;
                        } else if (doaminClass.equals(dtoClass)) {
                            setResultDto(constraintViolation, errorData, formId, null, false);
                        } else if (Arrays.asList(dtoAnnotation.types()).contains(doaminClass)) {
                        	System.out.println("Pay attention to dtoClass = " + dtoClass);
                            setResultDto(constraintViolation, errorData, formId, dtoClass, false);
                        } else {
                            setResultDto(constraintViolation, errorData, formId, null, false);
                        }
                    }
                }
            } else {
                for (ConstraintViolation<?> constraintViolation : constraintsViolatioins) {
                	Class<?> rootClass = constraintViolation.getRootBeanClass();
                    setResultDto(constraintViolation, errorData, formId, rootClass, true);
                }
            }
        }
    }

    /**
     * 
     * Description: set the result dto
     * 
     * @param constraintViolation
     * @throws NoSuchFieldException
     */
    private void setResultDto(ConstraintViolation<?> constraintViolation, List<ValidationResultDto> errorData,
            String formId, Class<?> dtoClass, boolean notManually) throws RuntimeException {
        final String beanName = constraintViolation.getRootBeanClass().getName();
        final String errorMessage = constraintViolation.getMessage();
        final String fieldName = constraintViolation.getPropertyPath().toString();
        Class<?> rootClass = constraintViolation.getRootBeanClass();
        setFieldErrorMap(fieldName, beanName, rootClass, dtoClass, errorData, errorMessage, formId, notManually);
    }
}
