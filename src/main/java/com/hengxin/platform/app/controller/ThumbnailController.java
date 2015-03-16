package com.hengxin.platform.app.controller;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hengxin.platform.common.util.AppConfigUtil;

/**
 * ThumbnailController.
 * 
 */
@Controller
public class ThumbnailController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ThumbnailController.class);

	@RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
	public void thumbnail(@RequestParam String fileName,
			HttpServletResponse response) {
		response.setContentType("image/jpeg");
		try {
			Thumbnails
					.of(new File(AppConfigUtil.getUploadFilePath() + fileName)).size(960, 640)
					.outputFormat("jpeg")
					.toOutputStream(response.getOutputStream());
		} catch (Exception e) {
			LOGGER.warn("error creating thumbnail", e);
		}

	}

}
