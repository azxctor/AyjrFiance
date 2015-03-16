/*
 * Project Name: kmfex-platform
 * File Name: BaseService.java
 * Class Name: BaseService
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

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class Name: BaseService Description:
 * <p>
 * the base service
 * 
 * @author minhuang
 * 
 */

public class BaseService {
    @Autowired
    private Validator validator;

    @Autowired
    protected ActionHistoryService actionHistoryUtil;

    /**
     * 
     * Description: validate the domain.
     * 
     * @param <T>
     * 
     * @param object
     * @param groups
     */
    protected <T> void validate(T object, Class<?>... groups) {
        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);

        if (constraintViolations != null && constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}