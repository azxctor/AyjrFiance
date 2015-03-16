package com.hengxin.platform.common.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.hengxin.platform.common.domain.SysFile;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EFileCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.FileService;

public final class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    private static FileService fileService;

    private FileUploadUtil() {

    }

    public static String getFolder() {
        return AppConfigUtil.getUploadFilePath();
    }

    public static String getFolder(EFileCategory category, String id) {
        return AppConfigUtil.getUploadFilePath() + category.getText() + "/" + id;
    }

    public static String getTempFolder() {
        return AppConfigUtil.getUploadFileURL();
    }

    public static String getTempFolder(EFileCategory category, String id) {
        return AppConfigUtil.getUploadFileURL() + category.getText() + "/" + id + "/";
    }

    public static SysFile uploadFile(MultipartFile file, boolean isExcel) throws BizServiceException {
        String[] split = file.getOriginalFilename().split("\\.");
        String suffix = "." + split[split.length - 1];

        if (isExcel && !suffix.equalsIgnoreCase(".xls") && !suffix.equalsIgnoreCase(".xlsx")) {
            throw new BizServiceException(EErrorCode.FILE_FORMAT_NOT_MATCH);
        } else if (!isExcel
                && !(suffix.equalsIgnoreCase(".jpg") || suffix.equalsIgnoreCase(".png")
                        || suffix.equalsIgnoreCase(".jpeg") || suffix.equalsIgnoreCase(".gif")
                        || suffix.equalsIgnoreCase(".bmp") || suffix.equalsIgnoreCase(".pdf")
                        || suffix.equalsIgnoreCase(".doc") || suffix.equalsIgnoreCase(".docx"))) {
            throw new BizServiceException(EErrorCode.FILE_FORMAT_NOT_MATCH);
        }

        String fileName = generateFileName(file, suffix);
        File realFile = new File(FileUploadUtil.getFolder(), fileName);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("File name: {}", fileName);
            LOGGER.debug("File name: {}", file.getName());
            LOGGER.debug("File size: {}", file.getSize());
            LOGGER.debug("File type: {}", file.getContentType());
        }
        SysFile resultFile = new SysFile();
        resultFile.setId(fileName);
        resultFile.setPath(FileUploadUtil.getTempFolder() + fileName);
        resultFile.setOrgFileName(file.getOriginalFilename());
        resultFile.setUploadPath(FileUploadUtil.getFolder() + "/" + fileName);

        try {
            fileService.uploadAndSaveFile(file.getInputStream(), realFile, resultFile);
        } catch (IOException e) {
            LOGGER.error("file upload failed.", e);
            throw new BizServiceException(EErrorCode.FILE_UPLOAD_FAIL);
        }

        return resultFile;
    }

    private static String generateFileName(MultipartFile file, String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append((new SimpleDateFormat("yyMMddhhmmssSSS")).format(new Date()));
        sb.append("_");
        sb.append(UUID.randomUUID().toString().replaceAll("-", ""));
        sb.append(suffix);
        return sb.toString();
    }

    public static void setFileService(FileService fileService) {
        FileUploadUtil.fileService = fileService;
    }

}
