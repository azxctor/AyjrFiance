/**
 * 
 */
package com.hengxin.platform.report.excel.xml;

import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSpreadsheetSAXHandler extends DefaultHandler {

	private XmlSpreadsheetConverter converter;

	/**
	 * Constructor.
	 * 
	 * @param wookbook
	 *            only support SXSSFWorkbook or XSSFWorkbook
	 */
	public XmlSpreadsheetSAXHandler(Workbook workbook) {
		converter = new XmlSpreadsheetConverter(workbook);
	}

	/**
	 * Parse XML node.
	 */
	public void parse(String uri, String localName, String qName, Attributes attributes) {
		XmlSpreadsheetNode record = buildNode(uri, localName, qName, attributes);
		if (XmlSpreadsheetConstants.QNAME_PIC_DEF.equals(qName)) {
			converter.definePicture(record);
		} else if (XmlSpreadsheetConstants.QNAME_PIC_INSERT.equals(qName)) {
			converter.insertPicture(record);
		} else if (XmlSpreadsheetConstants.QNAME_STYLE.equals(qName)) {
			converter.createStyle(record);
		} else if (XmlSpreadsheetConstants.QNAME_ALIGNMENT.equals(qName)) {
			converter.parseStyleAlignment(record);
		} else if (XmlSpreadsheetConstants.QNAME_BORDER.equals(qName)) {
			converter.parseStyleBorder(record);
		} else if (XmlSpreadsheetConstants.QNAME_INTERIOR.equals(qName)) {
			converter.parseInterior(record);
		} else if (XmlSpreadsheetConstants.QNAME_FONT.equals(qName)) {
			if (record.containsKey(XmlSpreadsheetConstants.ATTR_HTML_COLOR)) {
				converter.parseRichTextFontStyle(record);
			} else {
				converter.createFontForCellStyle(record);
			}
		} else if (XmlSpreadsheetConstants.QNAME_WORKSHEET.equals(qName)) {
			converter.createSheet(record);
		} else if (XmlSpreadsheetConstants.QNAME_TABLE.equals(qName)) {
			converter.parseTable(record);
		} else if (XmlSpreadsheetConstants.QNAME_COLUMN.equals(qName)) {
			converter.parseColumn(record);
		} else if (XmlSpreadsheetConstants.QNAME_ROW.equals(qName)) {
			converter.createRow(record);
		} else if (XmlSpreadsheetConstants.QNAME_CELL.equals(qName)) {
			converter.createCell(record);
		} else if (XmlSpreadsheetConstants.QNAME_DATA.equals(qName)
				|| XmlSpreadsheetConstants.QNAME_SS_DATA.equals(qName)) {
			converter.parseCellData(record);
		} else if (XmlSpreadsheetConstants.QNAME_B.equals(qName)
				|| XmlSpreadsheetConstants.QNAME_I.equals(qName)
				|| XmlSpreadsheetConstants.QNAME_S.equals(qName)
				|| XmlSpreadsheetConstants.QNAME_U.equals(qName)) {
			converter.parseRichTextFontStyle(record);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		parse(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (XmlSpreadsheetConstants.QNAME_CELL.equals(qName)) {
			converter.flushCellValue();
		} else if (XmlSpreadsheetConstants.QNAME_CELL.equals(qName)) {
			converter.flushRowValue();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		converter.parseCellText(new String(ch, start, length));
	}

	private XmlSpreadsheetNode buildNode(String uri, String localName,
			String qName, Attributes attributes) {
		XmlSpreadsheetNode record = new XmlSpreadsheetNode();
		record.setqName(qName);
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); ++i) {
				String key = attributes.getQName(i);
				String value = attributes.getValue(i);
				record.put(key, value);
			}
		}
		return record;
	}

}
