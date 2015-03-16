package com.hengxin.platform.common.util;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.entity.id.CachingDatabaseSequenceProducer.CacheWatcher;
import com.hengxin.platform.common.repository.DynamicOptionRepository;
import com.hengxin.platform.common.service.CommonBusinessService;
import com.hengxin.platform.common.service.FileService;
import com.hengxin.platform.member.repository.InvestorLevelRepository;

@Component
public class UtilityPreparationPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilityPreparationPostProcessor.class);

    @Autowired
    private DynamicOptionRepository sysDictRepository;
    
    @Autowired
    private InvestorLevelRepository investorLevelRepository;

    @Autowired
    private CommonBusinessService commonBusinessService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    
    @Autowired
    @Qualifier("appConfig")
    private MessageSource appConfig;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private CacheWatcher cacheWatcher;

    @PostConstruct
    public void postProcessAfterInitialization() throws BeansException {
        LOGGER.info("postProcessAfterInitialization() invoked");
        SystemDictUtil.setSysDictRepository(sysDictRepository);
        SystemDictUtil.setInvestorLevelRepository(investorLevelRepository);
        SystemDictUtil.initAndRefresh();

        CommonBusinessUtil.commonBusinessService = commonBusinessService;
        CommonBusinessUtil.cacheWatcher = cacheWatcher;
        CommonBusinessUtil.init();

        FileUploadUtil.setFileService(fileService);

        MessageUtil.setMessageSource(messageSource);
        AppConfigUtil.setMessageSource(appConfig);
    }
	
}
