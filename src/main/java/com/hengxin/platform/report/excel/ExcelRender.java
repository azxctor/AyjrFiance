package com.hengxin.platform.report.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.hengxin.platform.report.excel.xml.XmlSpreadsheetSAXHandler;

public final class ExcelRender {
	private ExcelRender() {
	}

	public static void render(File xml, OutputStream out) throws Exception {
		Workbook workbook = new SXSSFWorkbook(null, 1024, true);
		// Use XSSFWorkbook to enable richText: Workbook workbook = new
		// XSSFWorkbook();
		InputStream is = null;
		try {
			is = new FileInputStream(xml);
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(is, new XmlSpreadsheetSAXHandler(workbook));
			workbook.write(out);
			out.flush();
		} finally {
			if (is != null) {
				is.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
}
