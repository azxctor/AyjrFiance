package com.hengxin.platform.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

public final class Word2HtmlUtil {

	private Word2HtmlUtil() {
	}

	public static String convert(String fileName) throws Exception {
		boolean forDocx = fileName.toLowerCase().endsWith(".docx");
		boolean forDoc = fileName.toLowerCase().endsWith(".doc");
		
		String html = null;

		if (forDocx) {
			html = processDocx(fileName);
		} else if (forDoc) {
			html = processDoc(fileName);
		}
		
		return processHtml(html, forDocx);

	}

	private static String processDocx(String fileName) throws Exception {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new FileInputStream(new File(fileName));
			XWPFDocument document = new XWPFDocument(in);
			out = new ByteArrayOutputStream();
			XHTMLConverter.getInstance().convert(document, out, null);
			return new String(out.toByteArray());
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	private static String processDoc(String fileName) throws Exception {
		ByteArrayOutputStream out = null;
		try {
			HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(
					fileName));
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.newDocument());
			wordToHtmlConverter.processDocument(wordDocument);
			Document htmlDocument = wordToHtmlConverter.getDocument();
			out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
			out.close();
			return new String(out.toByteArray());
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	public static String processHtml(String html, boolean forDocx) {
		if (html == null) {
			return null;
		}
		
		org.jsoup.nodes.Document doc = Jsoup.parse(html);
		Elements table = doc.select("table");
		table.attr("style", table.attr("style").trim() + ";margin:auto;border-collapse:collapse");
		
		Elements td = table.select("td");
		td.attr("style", td.attr("style").trim() + ";border:thin solid black");
		
		if (forDocx) {
			Element div = doc.select("body").select("div").first();
			div.attr("style", div.attr("style").trim() + ";margin:auto;");
		}
		
		return doc.toString();
	}
	
}