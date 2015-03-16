package com.hengxin.platform.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hengxin.platform.app.dto.downstream.DynamicOptionDownDto;
import com.hengxin.platform.app.dto.downstream.VersionDto;
import com.hengxin.platform.app.dto.downstream.VersionV3DownDto;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.controller.FileUploadController;
import com.hengxin.platform.common.controller.OptionController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.service.FileService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.security.SecurityContext;

/**
 * 
 * CommonController.
 *
 */
@Controller
public class CommonController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	@Autowired
    private FileService fileService;

    @Autowired
    private SecurityContext securityContext;
	
	// inject web controller
	private FileUploadController fileUploadController;
	
	// inject web controller
	private OptionController optionController;
	
	@PostConstruct
	public void init() {
		LOGGER.debug("init fileUploadController");
		fileUploadController = new FileUploadController();
		fileUploadController.setFileService(fileService);
		LOGGER.debug("init optionController");
		optionController = new OptionController();
	}
	
	
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	@ResponseBody
	public ResultDto checkVersion(HttpServletRequest request) {
		StringBuilder prefix = new StringBuilder()
				.append(request.getScheme()).append("://")
				.append(request.getServerName());
		if (request.getServerPort() != 80 && request.getServerPort() != 443) {
			prefix.append(":").append(request.getServerPort());
		}
		VersionDto version = new VersionDto();
		version.setAndroidAppVersion(CommonBusinessUtil.getAndroidAppVersion());
		version.setAndroidAppDownloadUrl(prefix.toString() + CommonBusinessUtil.getAndroidAppDownloadUrl());
		version.setIosAppVersion(CommonBusinessUtil.getIOSAppVersion());
		version.setIosAppDownloadUrl(CommonBusinessUtil.getIOSAppDownloadUrl());
		return ResultDtoFactory.toAck("获取成功", version);
	}

	/**
	 * 当前pad应用对应版本信息，适用担保机构&&服务中心(三期 app)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/version3", method = RequestMethod.GET)
	@ResponseBody
	public ResultDto checkVersion4padV3(HttpServletRequest request) {
		StringBuilder prefix = new StringBuilder()
				.append(request.getScheme()).append("://")
				.append(request.getServerName());
		/* when protocol is not http or https, we shoul append it. */
		if (request.getServerPort() != 80 && request.getServerPort() != 443) {
			prefix.append(":").append(request.getServerPort());
		}
		VersionV3DownDto version = new VersionV3DownDto();
		version.setAndroidAppVersion(CommonBusinessUtil.getAndroidAppVersion());
		version.setAndroidAppDownloadUrl(prefix.toString() + CommonBusinessUtil.getAndroidAppDownloadUrl());
		version.setIosAppVersion(CommonBusinessUtil.getIOSAppVersion());
		version.setIosAppDownloadUrl(CommonBusinessUtil.getIOSAppDownloadUrl());
		return ResultDtoFactory.toAck("获取成功", version);
	}
	
	/* refer to FileUploadUtil.java */
	
	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto uploadFile(@RequestParam MultipartFile file, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("uploadFile start: ");
		}
		return fileUploadController.uploadFile(file, request, response);
	}
	
    @RequestMapping(value = "/multifile/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto uploadMultiFile(@RequestParam MultipartFile[] files, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
    	if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("uploadMultiFile start: ");
		}
    	return fileUploadController.uploadMultiFile(files, request, response);
    }
    
    @RequestMapping(value = "/file/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultDto removeFile(@RequestBody String fileId) {
    	if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("removeFile start: ");
		}
    	if (StringUtils.isBlank(fileId)) {
            return null;
        }
        try {
            fileService.removeFile(fileId);
        } catch (IOException e) {
            return ResultDtoFactory.toNack("文件删除失败");
        }
        return ResultDtoFactory.toAck("文件删除成功");
    }
    
    @RequestMapping(value = "/file/download/{filename}", method = RequestMethod.GET)
    public void getFile(@PathVariable(value = "filename") String fileName, 
    		HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getFile start: ");
		}
    	fileUploadController.getFile(request, response, fileName);
    }
    
    /**
     * fetch region: category input "region"
     * 
     * refer to OptionController
     * 
     */
    @RequestMapping(value = "/option/{category}/{code}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto getCategory(@PathVariable("category") String category,
            @PathVariable("code") String code) {
    	if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getCategory() start: " + category);
        }
    	List<DynamicOption> dynamicOptionList = optionController.getChildrenOptionList(category, code);
    	List<DynamicOptionDownDto> dynamicOptionDownDtoList = new ArrayList<DynamicOptionDownDto>();
    	DynamicOptionDownDto dynamicOptionDownDto = null;
    	for (DynamicOption dynamicOption : dynamicOptionList) {
    		dynamicOptionDownDto = new DynamicOptionDownDto();
    		ConverterService.convert(dynamicOption, dynamicOptionDownDto);
    		dynamicOptionDownDtoList.add(dynamicOptionDownDto);
    	}
    	return ResultDtoFactory.toAck("获取成功", dynamicOptionDownDtoList);
    }
}
