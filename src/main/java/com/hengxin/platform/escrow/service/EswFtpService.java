package com.hengxin.platform.escrow.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.ebc.dto.reconcilation.ReconciliationEntity;
import com.hengxin.platform.escrow.entity.EswReconEbcPo;
import com.hengxin.platform.escrow.repository.EswReconEbcRepository;
import com.hengxin.platform.escrow.utils.ReconciliationTextReader;

@Service
public class EswFtpService {

	// private final String HOST_NAME = AppConfigUtil.getEbcFtpHost();
	// private final String USER_NAME = AppConfigUtil.getEbcFtpUserName();
	// private final String PASSWORD = AppConfigUtil.getEbcFtpPassword();

	private final static String REMOTE_DIR = "/";
	// private final String LOCAL_DIR = AppConfigUtil.getEbcReconPath();

	private final List<String> txtSuffix = Arrays.asList(".txt", "-check.txt",
			"-error.txt");

	@Autowired
	EswReconEbcRepository eswReconEbcRepository;

	public void readReconciliationText() {
		FTPClient ftpClient = new FTPClient();
		try {
			String host = AppConfigUtil.getEbcFtpHost();
			String userName = AppConfigUtil.getEbcFtpUserName();
			String pwd = AppConfigUtil.getEbcFtpPassword();
			String localDir = AppConfigUtil.getEbcReconPath();
			ftpClient.connect(host, 21);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.login(userName, pwd);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// FTPFile[] files = ftpClient.listFiles(REMOTE_DIR);
			// for (int i = 0; i < files.length; i++) {
			// System.out.println(files[i].getName());
			// }
			List<String> fileNames = getFileName();
			List<EswReconEbcPo> eswReconPos = Lists.newArrayList();
			List<ReconciliationEntity> reconciliationEntities = Lists
					.newArrayList();
			for (String fileName : fileNames) {
				String path = localDir + fileName;
				File file = new File(path);
				FileOutputStream fos = new FileOutputStream(file);
				ftpClient.retrieveFile(REMOTE_DIR + fileName, fos);
				fos.close();
				reconciliationEntities.addAll(ReconciliationTextReader
						.guavaRead(path));
			}
			for (ReconciliationEntity re : reconciliationEntities) {
				EswReconEbcPo po = ConverterService.convert(re,
						EswReconEbcPo.class);
				eswReconPos.add(po);
			}
			eswReconEbcRepository.save(eswReconPos);
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.SOCKET_IO_ERROR);
		}
	}

	private List<String> getFileName() {
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		// Date worDate = new Date();
		Date yestoday = DateUtils.addDays(workDate, -1);
		String date = com.hengxin.platform.fund.util.DateUtils.formatDate(
				yestoday, "yyyy-MM-dd");
		List<String> result = Lists.newArrayList();
		for (String suffix : txtSuffix) {
			result.add(date + suffix);
		}
		return result;
	}

}
