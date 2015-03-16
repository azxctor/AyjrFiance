/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report;

import static java.lang.String.valueOf;

import java.io.Serializable;
import java.util.Arrays;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 * Wraps the built in CompiledEvalNode and encodes XML tags.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
class FilteredCompiledEvalNode
	extends Node {
	private static final long serialVersionUID = -8264881803066633888L;
	private Serializable ce;
	private boolean escape;
/**
 * Constructs a new instance with the specified parameters
 * @param begin
 * @param name
 * @param template
 * @param start
 * @param end
 * @param context
 * @param esc
 */
	public FilteredCompiledEvalNode(int begin, String name, char[] template, int start, int end, ParserContext context,
		boolean esc) {
		this.begin = begin;
		this.name = name;
		this.contents = Arrays.copyOf(template.clone(), template.length);
		this.cStart = start;
		this.cEnd = end - 1;
		this.end = end;
		this.escape = esc;
		ce = MVEL.compileExpression(template, cStart, cEnd - cStart, context);
	}

	@Override
	public Object eval(TemplateRuntime runtime, TemplateOutputStream appender, Object ctx,
		VariableResolverFactory factory) {
		final String value = valueOf(MVEL.executeExpression(ce, ctx, factory));
		// escape XML characters(&, <, >)
		if (escape) {
			for (int i = 0; i < value.length(); i++) {
				final char c = value.charAt(i);
				switch (c) {
					case '&':
						appender.append("&amp;");
						break;
					case '<':
						appender.append("&lt;");
						break;
					case '>':
						appender.append("&gt;");
						break;
					default:
						appender.append(new char[] { c });
				}
			}
		} else {
			appender.append(value);
		}
		return next != null ? next.eval(runtime, appender, ctx, factory) : null;
	}
/**
 * @return demarcate
 */
	public boolean demarcate(Node terminatingNode, char[] template) {
		return false;
	}
/**
 * @return FilteredCompiledEvalNode
 */
	public String toString() {
		return "FilteredCompiledEvalNode:" + name + "{" + (contents == null ? "" : new String(contents)) + "} (start="
			+ begin + ";end=" + end + ")";
	}
}
