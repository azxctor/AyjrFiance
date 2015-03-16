package com.hengxin.platform.report;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mvel2.integration.impl.DefaultLocalVariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.util.io.StandardOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;

import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.report.excel.ExcelRender;
import com.hengxin.platform.report.render.AgentCallback;
import com.hengxin.platform.report.render.ITextRendererWrapper;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * Report generator machine which actually puts data and template together and
 * drives the whole process.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public class ReportGenerator {
	private static final int OUTPUT_BUF_SIZE = 128;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportGenerator.class);

	private ReportContext ctx;
	private ReportTemplateRegistry templateRegistry;
	private ReportDataProviderFactory providerFactory;

	public ReportGenerator(ReportContext ctx,
			ReportTemplateRegistry templateRegistry,
			ReportDataProviderFactory providerFactory) {
		if (ctx == null) {
			throw new IllegalArgumentException("ReportContext cannot be null");
		}
		if (templateRegistry == null) {
			throw new IllegalArgumentException(
					"templateRegistry cannot be null");
		}
		if (providerFactory == null) {
			throw new IllegalArgumentException("providerFactory cannot be null");
		}
		this.ctx = ctx;
		this.templateRegistry = templateRegistry;
		this.providerFactory = providerFactory;
	}

	/**
	 * Starts to create report.
	 * 
	 * @throws ReportingException
	 *             if failed due to errors, for instance, syntax error exists
	 *             within template
	 */
	public void generate() throws ReportingException {
		LOGGER.info("createReport() invoked");
		try {
			CompiledTemplate tem = loadTemplate();
			LOGGER.debug("template file is loaded successfully");
			makeReport(tem);
			LOGGER.debug("report is generated successfully");
		} catch (TemplateExecutionException e) {
			LOGGER.error("template execution failure", e);
			throw new ReportingException(ctx, e);
		} catch (RenderingException e) {
			LOGGER.error("report rendering failed");
			throw new ReportingException(ctx, e);
		} catch (Exception e) {
			LOGGER.error("report failed");
			throw new ReportingException(ctx, e);
		}

		LOGGER.debug("createReport() completed");
	}

	private void makeReport(CompiledTemplate template)
			throws TemplateExecutionException, RenderingException {
		LOGGER.info("makeReport() invoked, location:{}", ctx.getOutput().getFileName());
		if (ctx.getCriteria().getOutputType() == EReportOutputType.PDF) {
			// PDF
			ReportDataProvider dataSource = prepareReportData();
			ReportData data = dataSource.getReportData();
			List<File> files = new ArrayList<File>();
			do {
				File tempReport = null;
				try {
					// create temp file if possible
					tempReport = File.createTempFile("report", "report",
							new File(dataSource.getReportConfiguration()
									.getReportSavingFolder()));
					LOGGER.debug("temp report created @ {}",
							tempReport.getCanonicalPath());
				} catch (IOException ioe) {
					// we cannot continue, but we must clear temp files before
					// exit.
					for (File f : files) {
						if (!f.delete()) {
							LOGGER.info("failed to clean up temp file:{}",
									f.getAbsolutePath());
						}
					}
					throw new RenderingException(ioe);
				}
				files.add(tempReport);
				BufferedOutputStream os = null;
				try {
					os = new BufferedOutputStream(new FileOutputStream(
							tempReport), OUTPUT_BUF_SIZE);
					createTemplateRuntime(template).execute(
							new ReportOutputStream(os), data,
							new DefaultLocalVariableResolverFactory());
				} catch (Exception e) {
					// we cannot continue as an error found in template, but we
					// must clear temp files before exit.
					for (File f : files) {
						if (!f.delete()) {
							LOGGER.info("failed to clean up temp file:{}",
									f.getAbsolutePath());
						}
					}
					throw new TemplateExecutionException(e);
				} finally {
					closeStreamIgnoreError(os);
				}
			} while (dataSource.hasMoreDocument());
			if (files.size() > 1) {
				final ReportOutput outputOld = this.ctx.getOutput();
				this.ctx.setOutput(NullReportOutput.INSTANCE);
				int pagesCount = writeReportAsPDF(files,
						ITextRendererWrapper.PAGES_COUNTER_NOT_DEFINED, true);
				// now do real work
				this.ctx.setOutput(outputOld);
				writeReportAsPDF(files, pagesCount, false);
			} else {
				// do it as before
				writeReportAsPDF(files,
						ITextRendererWrapper.PAGES_COUNTER_NOT_DEFINED, false);

			}
		} else if (ctx.getCriteria().getOutputType() == EReportOutputType.Excel) {
			writeReportAsExcel(template);
		} else {
			// Delimited, XML can be generated without temporary files
			writeReportAsDelimited(template);
		}
		LOGGER.debug("makeReport() completed");
	}

	private void writeReportAsExcel(CompiledTemplate template)
			throws RenderingException, TemplateExecutionException {
		ReportDataProvider dataSource = prepareReportData();
		// create temporary xml file
		File tempReport = null;
		try {
			// create temp file if possible
			tempReport = File.createTempFile("excel", "report",
					new File(dataSource.getReportConfiguration()
							.getReportSavingFolder()));
			LOGGER.debug("temp report created @ {}",
					tempReport.getCanonicalPath());
		} catch (IOException ioe) {
			// abort
			throw new RenderingException(ioe);
		}
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(tempReport),
					OUTPUT_BUF_SIZE);
			ReportData data = dataSource.getReportData();
			createTemplateRuntime(template).execute(
					new StandardOutputStream(bos), data,
					new DefaultLocalVariableResolverFactory());
		} catch (Exception e) {
			// clear temp files before exit
			if (!tempReport.delete()) {
				LOGGER.info("failed to clean up temp file:{}",
						tempReport.getAbsolutePath());
			}
			throw new TemplateExecutionException(e);
		} finally {
			closeStreamIgnoreError(bos);
		}

		try {
			ExcelRender.render(tempReport,
					new FileOutputStream(ctx.getOutput().getFileName()));
		} catch (Exception e) {
			throw new RenderingException(e);
		} finally {
			// clear temp files before exit
			if (!tempReport.delete()) {
				LOGGER.info("failed to clean up temp file:{}",
						tempReport.getAbsolutePath());
			}
		}
	}

	private void closeStreamIgnoreError(Closeable s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (IOException e) {
			// not interesting
			LOGGER.warn("faild to close stream", e);
		}
	}

	private void writeReportAsDelimited(CompiledTemplate template)
			throws RenderingException {
		BufferedOutputStream bos = new BufferedOutputStream(
				openStreamForReportSaving(), OUTPUT_BUF_SIZE);
		try {
			ReportDataProvider dataSource = prepareReportData();
			ReportData data = dataSource.getReportData();
			createTemplateRuntime(template).execute(
					new StandardOutputStream(bos), data,
					new DefaultLocalVariableResolverFactory());
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				LOGGER.warn("cannot close output", e);
			}
		}
	}

	private TemplateRuntime createTemplateRuntime(CompiledTemplate template) {
		return new TemplateRuntime(template.getTemplate(), null,
				template.getRoot(), templateRegistry.getTemplateHomePath());
	}

	private ReportDataProvider prepareReportData() {
		return providerFactory.createDataProvider(ctx);
	}

	private int writeReportAsPDF(List<File> tempXHTMLs,
			int predefinedPagesCounter, boolean retainTempFiles)
			throws RenderingException {
		ITextRendererWrapper renderer = new ITextRendererWrapper(
				predefinedPagesCounter);
		
		ITextFontResolver fontResolver = renderer.getFontResolver();
		try {
			fontResolver.addFont(AppConfigUtil.getChineseFontPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// we need to specify how to resolve uri
		AgentCallback agentCallback = new AgentCallback(
				renderer.getOutputDevice(), templateRegistry);
		agentCallback.setSharedContext(renderer.getSharedContext());
		renderer.getSharedContext().setUserAgentCallback(agentCallback);
		OutputStream os = openStreamForReportSaving();
		try {
			Iterator<File> files = tempXHTMLs.iterator();
			renderer.setDocument(files.next());
			renderer.layout();
			renderer.createPDF(os, false);

			while (files.hasNext()) {
				renderer.setDocument(files.next());
				renderer.layout();
				renderer.writeNextDocument(renderer.getWriter()
						.getCurrentPageNumber() + 1);
			}
			// remove temporary files
			if (!retainTempFiles) {
				for (File f : tempXHTMLs) {
					if (!f.delete()) {
						LOGGER.info("file cannot be deleted: {}",
								f.getCanonicalPath());
					}
				}
			}
			return renderer.getPagesCreated();
		} catch (IOException ioe) {
			throw new RenderingException(ioe);
		} catch (DocumentException ioe) {
			throw new RenderingException(ioe);
		} finally {
			renderer.finishPDF();
			closeStreamIgnoreError(os);
		}
	}

	private CompiledTemplate loadTemplate() throws TemplateExecutionException {
		String template = templateRegistry.getTemplate(ctx.getCriteria());
		if (template == null) {
			throw new TemplateExecutionException("template not defined");
		}
		LOGGER.debug("loading template file {} ...", template);
		InputStream is = ClasspathResourceLoader.getResourceAsStream(template);
		if (is == null) {
			LOGGER.error("cannot open template file for reading. file not found?");
			throw new TemplateExecutionException("template not found");
		}
		LOGGER.debug("compiling template file {} ...", template);
		try {
			return ClassPathTemplateCompiler
					.compileTemplate(
							is,
							templateRegistry,
							ctx.getCriteria().getOutputType() == EReportOutputType.PDF
									|| ctx.getCriteria().getOutputType() == EReportOutputType.Excel
									|| ctx.getCriteria().getOutputType() == EReportOutputType.Xml);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// ignore
				LOGGER.warn("failed to close stream", e);
			}
		}
	}

	private OutputStream openStreamForReportSaving() throws RenderingException {
		if (NullReportOutput.isNull(ctx.getOutput())) {
			LOGGER.debug("NULL output is being used");
			return NullOutputStream.INSTANCE;
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(ctx.getOutput().getFileName()));
		} catch (Exception e) {
			LOGGER.error("cannot open {} for writing {}", ctx.getOutput().getFileName(),
					e);
		}
		if (os == null) {
			throw new RenderingException("output not defined or cannot be open");
		}
		return os;
	}

	public ReportContext getCtx() {
		return ctx;
	}

	public void setCtx(ReportContext ctx) {
		this.ctx = ctx;
	}

	public ReportTemplateRegistry getTemplateRegistry() {
		return templateRegistry;
	}

	public void setTemplateRegistry(ReportTemplateRegistry templateRegistry) {
		this.templateRegistry = templateRegistry;
	}

	public ReportDataProviderFactory getProviderFactory() {
		return providerFactory;
	}

	public void setProviderFactory(ReportDataProviderFactory providerFactory) {
		this.providerFactory = providerFactory;
	}
	
}
