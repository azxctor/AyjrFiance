package com.hengxin.platform.escrow.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.hengxin.platform.ebc.dto.reconcilation.ReconciliationEntity;
import com.hengxin.platform.fund.util.DateUtils;

public final class ReconciliationTextReader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReconciliationTextReader.class);

	private static final String PREFIX_STRING = "商户名称_P2P_000000000000000_";

	@Deprecated
	public static void read(Date date) {
		List<ReconciliationEntity> result = new ArrayList<ReconciliationEntity>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(getPath(date)));
		} catch (FileNotFoundException e) {
			LOGGER.error("reconciliation file not found");
			return;
		}
		try {
			String line = br.readLine();
			while (line != null) {
				result.add(getReconciliationEntity(line));
				line = br.readLine();
			}
		} catch (IOException e) {
			LOGGER.error("error while reading", e);
		} finally {
			try {
				if (null != br)
					br.close();
			} catch (IOException e) {
				LOGGER.error("", e);
			}
		}
	}

	/**
	 * WARN:this will read the whole file into memory. if the file is very huge,
	 * this may lead to an out of memory exception
	 * 
	 * @param date
	 */
	public static List<ReconciliationEntity> guavaRead(String path) {
		List<ReconciliationEntity> result = new ArrayList<ReconciliationEntity>();
		try {
			for (String line : Files.readLines(new File(path),
					Charsets.UTF_8)) {
				result.add(getReconciliationEntity(line));
			}
		} catch (IOException e) {
			LOGGER.error("can not read file:" + path, e);
		}
		return result;
	}

	private static String getPath(Date date) {
		String dateString = DateUtils.formatDate(date, "yyyyMMdd");
		String pathString = PREFIX_STRING + dateString + ".txt";
		return pathString;
	}

	private static ReconciliationEntity getReconciliationEntity(String line) {
		ReconciliationEntity result = new ReconciliationEntity();
		String[] rawData = StringUtils.split(line, "|");
		result.setMerchNo(rawData[0]);
		result.setOrderId(rawData[1]);
		result.setOrderType(rawData[2]);
		result.setOrderSn(rawData[3]);
		result.setOutMediumNo(rawData[4]);
		result.setOutCardNo(rawData[5]);
		result.setInMediumNo(rawData[6]);
		result.setInCardNo(rawData[7]);
		result.setCurrency(rawData[8]);
		result.setAmount(rawData[9]);
		result.setStatus(rawData[10]);
		result.setTrxDate(rawData[11]);
		return result;
	}

}
