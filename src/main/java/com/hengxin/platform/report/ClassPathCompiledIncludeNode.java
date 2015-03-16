/**
 * ArgusHealth and HT. All rights reserved.
 */
package com.hengxin.platform.report;

// CHECKSTYLE OFF: 3rd party library hack
// NOSONAR: 3rd party library hack

import static org.mvel2.templates.util.TemplateTools.captureToEOS;

import java.io.Serializable;
import java.util.Arrays;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replacement of MVEL builtin CompiledIncludeNode class. It supports class path resource
 * loading.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public class ClassPathCompiledIncludeNode
	extends Node {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ClassPathCompiledIncludeNode.class);
	private Serializable cIncludeExpression;
	private Serializable cPreExpression;

	private ParserContext context;
	private ReportTemplateRegistry registry;
	private boolean escape;

	/**
	 * Constructs a new exception with the specified parameters
	 * 
	 * @param begin
	 * @param name
	 * @param template
	 * @param start
	 * @param end
	 * @param context
	 * @param reg
	 * @param esc
	 */
	public ClassPathCompiledIncludeNode(int begin, String name, char[] template, int start, int end,
		ParserContext context, ReportTemplateRegistry reg, boolean esc) {
		this.begin = begin;
		this.name = name;
		this.contents = Arrays.copyOf(template.clone(), template.length);
		this.cStart = start;
		this.cEnd = end - 1;
		this.end = end;
		this.context = context;
		this.registry = reg;
		this.escape = esc;

		int mark = captureToEOS(contents, cStart);
		cIncludeExpression = MVEL.compileExpression(contents, cStart, mark - cStart, context);
		if (mark != contents.length) {
			cPreExpression = MVEL.compileExpression(contents, ++mark, cEnd - mark, context);
		}
	}

	@Override
	public Object eval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx,
		VariableResolverFactory factory) {
		String file = MVEL.executeExpression(cIncludeExpression, ctx, factory, String.class);

		if (this.cPreExpression != null) {
			MVEL.executeExpression(cPreExpression, ctx, factory);
		}

		if (next != null) {
			return next.eval(runtime, appender.append(String.valueOf(TemplateRuntime.eval(
				readFile(runtime, file, ctx, factory), ctx, factory))), ctx, factory);
		} else {
			return appender.append(String.valueOf(MVEL.eval(readFile(runtime, file, ctx, factory), ctx, factory)));
		}
	}

	protected String readFile(TemplateRuntime runtime, String fileName, Object ctx, VariableResolverFactory factory) {
		String parent = String.valueOf(runtime.getRelPath().peek());
		logger.info("readFile() invoked, home:{}, file:{}", parent, fileName);
		CompiledTemplate compiled =
			ClassPathTemplateCompiler.compileTemplate(ClasspathResourceLoader.getResourceAsStream(parent + fileName),
				context, registry, escape);
		return String.valueOf(createTemplateRuntime(compiled).execute(new StringBuilder(), ctx, factory));
	}

	private TemplateRuntime createTemplateRuntime(CompiledTemplate template) {
		return new TemplateRuntime(template.getTemplate(), null, template.getRoot(), registry.getTemplateHomePath());
	}

	/**
	 * @return demarcate
	 */
	@Override
	public boolean demarcate(Node terminatingNode, char[] template) {
		return false;
	}
}
