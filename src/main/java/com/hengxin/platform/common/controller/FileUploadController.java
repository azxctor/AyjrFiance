/*
 * Project Name: kmfex-platform
 * File Name: FileUploadController.java
 * Class Name: FileUploadController
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

package com.hengxin.platform.common.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;



//import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hengxin.platform.common.constant.ResultCode;
import com.hengxin.platform.common.domain.SysFile;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.FileService;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FileUploadController
 * 
 * @author runchen
 */

@Controller
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private SecurityContext securityContext;

    @RequestMapping(value = "/downloadFile/{filename}", method = RequestMethod.GET)
    public void getFile(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "filename") String fileName) throws IOException {
        LOGGER.debug("getFile start: ");
        String newFileName = "";
        String filePath = "";
        if (fileName.equalsIgnoreCase("onlineprotocol")) {
        	newFileName = "投资会员协议";
        	filePath="doc/onlineprotocol.doc";
		} else if (fileName.equalsIgnoreCase("applicationform")) {
			newFileName = "融资会员申请表";
			filePath="doc/applicationform.doc";
		} else if (fileName.equalsIgnoreCase("applicationform2")) {
			newFileName = "个人投资会员申请表";
			filePath="doc/applicationform2.doc";
		} else if (fileName.equalsIgnoreCase("applicationform3")) {
			newFileName = "机构投资会员申请表";
			filePath="doc/applicationform3.doc";
		} else if (fileName.equalsIgnoreCase("financerprotocol")) {
			newFileName = "融资人会员协议";
			filePath="doc/financerprotocol.doc";
		} else {
			throw new BizServiceException();
		}
        /** 中文文件名称. **/
//        String recommendedName = new String(newFileName.getBytes(), "ISO_8859_1");
        String recommendedName = new String(newFileName.getBytes("gb2312"), "ISO8859-1");
//        String recommendedName = MimeUtility.encodeWord(newFileName);
        /** MIME type for MSWord doc. **/
        response.setContentType("application/msword");
//        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("GB2312");
        response.setHeader("Content-Disposition", "attachment; filename=" + recommendedName + ".doc");
        response.resetBuffer();
        InputStream is = FileUploadController.class.getClassLoader().getResourceAsStream(filePath);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buffer = new byte[8192];
        int length = 0;
        while((length = bis.read(buffer)) != -1){
            bos.write(buffer, 0, length);
        }
        bos.flush();
        bos.close();
        bis.close();
    }

    @ResponseBody
    @RequestMapping("/uploadfile")
    public ResultDto uploadFile(@RequestParam MultipartFile file, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("uploadFile start: ");
        }
        if (file.isEmpty()) {
            return ResultDtoFactory.toNack("文件为空，请重新上传");
        }

        SysFile resultFile = null;
        try {
            resultFile = FileUploadUtil.uploadFile(file, false);
        } catch (BizServiceException e) {
            LOGGER.debug("file upload failed.", e);
            return ResultDtoFactory.toCommonError(e);
        }

        return ResultDtoFactory.toAck("文件上传成功", resultFile);
    }

    @ResponseBody
    @RequestMapping("/uploadmultifile")
    public ResultDto uploadMultiFile(@RequestParam MultipartFile[] files, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (files != null && files.length > 0) {
            StringBuilder message = new StringBuilder();
            SysFile[] resultFiles = new SysFile[files.length];
            int i = 0;
            for (MultipartFile file : files) {
                ResultDto result = uploadFile(file, request, response);
                if (ResultCode.NACK.equals(result.getCode())) {
                    message.append(file.getOriginalFilename());
                    message.append(result.getMessage());
                    message.append(", ");
                } else {
                    resultFiles[i] = (SysFile) result.getData();
                }
                i++;
            }
            if (!message.toString().isEmpty()) {
                return ResultDtoFactory.toNack(message.substring(0, message.length() - 2));
            }
            return ResultDtoFactory.toAck("文件上传成功", resultFiles);
        }
        return ResultDtoFactory.toNack("未选择文件");
    }

    @ResponseBody
    @RequestMapping(value = "/removefile", method = RequestMethod.DELETE)
    public ResultDto removeFile(@RequestBody SysFile file, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        if (file == null || StringUtils.isBlank(file.getId())) {
            return null;
        }
        try {
            fileService.removeFile(file.getId());
        } catch (IOException e) {
            return ResultDtoFactory.toNack("文件删除失败");
        }
        return ResultDtoFactory.toAck("文件删除成功");
    }

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

}
