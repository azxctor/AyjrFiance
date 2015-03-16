package com.hengxin.platform.report;

import java.io.IOException;
import java.io.OutputStream;

import org.mvel2.templates.util.TemplateOutputStream;

public class ReportOutputStream implements TemplateOutputStream {

	private OutputStream outputStream;

	public ReportOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public TemplateOutputStream append(CharSequence c) {
		try {
			outputStream.write(c.toString().getBytes("UTF-8"));
			return this;
		} catch (IOException e) {
			throw new RuntimeException("failed to write to stream", e);
		}
	}

	public TemplateOutputStream append(char[] c) {
		try {
			outputStream.write(new String(c).getBytes("UTF-8"));
			return this;
		} catch (IOException e) {
			throw new RuntimeException("failed to write to stream", e);
		}
	}

}
