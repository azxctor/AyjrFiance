package com.hengxin.platform.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mvel2.templates.TemplateError;

public final class TemplateTools {
	private TemplateTools() {

	}

	public static String readStream(InputStream instream) {
		try {
			StringBuilder appender = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream, "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				appender.append(line);
			}
			return appender.toString();
		} catch (NullPointerException e) {
			if (instream == null) {
				throw new TemplateError("null input stream", e);
			} else {
				throw e;
			}
		} catch (IOException e) {
			throw new TemplateError(
					"unknown I/O exception while including (stacktrace nested)",
					e);
		}
	}
}
