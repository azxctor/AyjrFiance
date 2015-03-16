package com.hengxin.platform.report.render;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import com.hengxin.platform.report.ClasspathResourceLoader;
import com.hengxin.platform.report.ReportTemplateRegistry;

/**
 * URI resolver for xhtml render.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public class AgentCallback
	extends ITextUserAgent {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AgentCallback.class);

	private ReportTemplateRegistry registry;

	/**
	 * @see ITextUserAgent#ITextUserAgent(ITextOutputDevice)
	 */
	public AgentCallback(ITextOutputDevice outputDevice,
			ReportTemplateRegistry reg) {
		super(outputDevice);
		registry = reg;
	}

	/**
	 * @see super{@link #resolveURI(String)}
	 */
	@Override
	public String resolveURI(String uri) {
		LOGGER.info("resolveURI() invoked, uri:{}", uri);
		URL ejbBase = ClasspathResourceLoader.getResource(registry
				.getTemplateHomePath());
		if (ejbBase != null) {
			setBaseURL(ejbBase.toExternalForm());
			LOGGER.debug("base resolved to {}", ejbBase.toExternalForm());
		}
		return super.resolveURI(uri);
	}
}
