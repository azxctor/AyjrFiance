/*
 * Project Name: kmfex-platform
 * File Name: PackageExpiredSignJob.java
 * Class Name: PackageExpiredSignJob
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

package com.hengxin.platform.job.bean;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.product.service.PackageJobService;

/**
 * Class Name: PackageExpiredSignJob
 * 
 * @author yingchangwang
 * 
 */
@DisallowConcurrentExecution
public class PackageExpiredSignJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageExpiredSignJob.class);

    @Autowired
    @Qualifier("packageExpiredSignJob")
    private Object bean;

    @Autowired
    private PackageJobService service;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.debug("running packageExpiredSignJob..");
        LOGGER.debug("{}", bean);

        // 逾期未签约，待签约->订单废弃
        try {
            service.expiredSignedPackages();
        } catch (Exception e) {
            LOGGER.info("Process Failed.", e);
        }

    }

}
