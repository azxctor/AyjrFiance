/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report.render;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.layout.BoxBuilder;
import org.xhtmlrenderer.layout.Layer;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFontContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFCreationListener;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.PageBox;
import org.xhtmlrenderer.render.ViewportBox;

/**
 * Wrapper class of ITextRenderer which fixes the pages counter problem caused
 * by cutting.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public class ITextRendererWrapper extends ITextRenderer implements PDFCreationListener {
	public static final int PAGES_COUNTER_NOT_DEFINED = 0;
	private static final Logger LOGGER = LoggerFactory.getLogger(ITextRendererWrapper.class);

	private int presetPagesCounter = PAGES_COUNTER_NOT_DEFINED;
	private int pagesCreated;

	/**
	 * Constructs a new instance with the specified integer pagesCount.
	 * 
	 * @param pagesCounter
	 */
	public ITextRendererWrapper(int pagesCounter) {
		this.presetPagesCounter = pagesCounter;
		setListener(this);
	}

	/**
	 * Constructs a new instance.
	 */

	public ITextRendererWrapper() {
		this(PAGES_COUNTER_NOT_DEFINED);
	}

	@Override
	public void layout() {
		LayoutContext c;
		c = newLayoutContext();
		fixPagesCounter(c);
		BlockBox root = BoxBuilder.createRootBox(c, getDocument());
		root.setContainingBlock(new ViewportBox(getInitialExtents(c)));
		root.layout(c);
		Dimension dim = root.getLayer().getPaintingDimension(c);
		root.getLayer().trimEmptyPages(c, dim.height);
		root.getLayer().layoutPages(c);
		setRoot(root);
	}

	private void fixPagesCounter(LayoutContext lc) {
		try {
			Field field = LayoutContext.class.getDeclaredField("_contentFunctionFactory");
			field.setAccessible(true);
			CustomizedContentFunctionFactory cff = new CustomizedContentFunctionFactory();
			cff.setTotalPageCounter(presetPagesCounter);
			field.set(lc, cff);
		} catch (Exception e) {
			LOGGER.error("fatal error", e);
		}
	}

	private void setRoot(BlockBox root) {
		try {
			Field field = ITextRenderer.class.getDeclaredField("_root");
			field.setAccessible(true);
			field.set(this, root);
		} catch (Exception e) {
			LOGGER.error("fatal error", e);
		}
	}

	/**
	 * Copied from super.
	 * 
	 * @param c
	 * 
	 * @return
	 * 
	 * @see {@link ITextRenderer}
	 */
	private LayoutContext newLayoutContext() {
		LayoutContext result = getSharedContext().newLayoutContextInstance();
		result.setFontContext(new ITextFontContext());

		getSharedContext().getTextRenderer().setup(result.getFontContext());

		return result;
	}

	/**
	 * Copied from super.
	 * 
	 * @param c
	 * 
	 * @return
	 * 
	 * @see {@link ITextRenderer}
	 */
	private Rectangle getInitialExtents(LayoutContext c) {
		PageBox first = Layer.createPageBox(c, "first");

		return new Rectangle(0, 0, first.getContentWidth(c), first.getContentHeight(c));
	}

	/**
	 * Called immediately after the iText Document instance is created but
	 * before the call to {@link com.lowagie.text.Document#open()} is called. At
	 * this point you may still modify certain properties of the PDF document
	 * header via the {@link com.lowagie.text.pdf.PdfWriter}; once open() is
	 * called, you can't change, e.g. the version. See the iText documentation
	 * for what limitations there are at this phase of processing.
	 * 
	 * @param iTextRenderer
	 *            the renderer preparing the document
	 */

	@Override
	public void preOpen(ITextRenderer iTextRenderer) {
	}

	/**
	 * Called immediately before the pages of the PDF file are about to be
	 * written out. This is an opportunity to modify any document metadata that
	 * will be used to generate the PDF header fields (the document information
	 * dictionary). Document metadata may be accessed through the
	 * {@link ITextOutputDevice} that is returned by
	 * {@link ITextRenderer#getOutputDevice()}.
	 * 
	 * @param iTextRenderer
	 *            the renderer preparing the document
	 * @param pageCount
	 *            the number of pages that will be written to the PDF document
	 */
	@Override
	public void preWrite(ITextRenderer iTextRenderer, int pageCount) {
		LOGGER.debug("{} pages will be created", pageCount);
		pagesCreated += pageCount;
	}

	/**
	 * Called immediately before the iText Document instance is closed, e.g.
	 * before {@link com.itextpdf.text.Document#close()} is called.
	 * 
	 * @param renderer
	 *            the iTextRenderer preparing the document
	 */

	@Override
	public void onClose(ITextRenderer renderer) {
	}

	/**
	 * 
	 * @return pagesCreated
	 */
	public int getPagesCreated() {
		return pagesCreated;
	}
}
