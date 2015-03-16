package com.hengxin.platform.report.excel.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSpreadsheetConverter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlSpreadsheetConverter.class);

	private static final Map<String, Short> ALIGNMENT_VERTICAL_MAP = new HashMap<String, Short>();
	private static final Map<String, Short> ALIGNMENT_HORIZONTAL_MAP = new HashMap<String, Short>();
	private static final Map<String, Short> FILL_PATTEN_MAP = new HashMap<String, Short>();
	private static final Map<String, BorderSide> BORDER_SIDE_MAP = new HashMap<String, BorderSide>();
	private static final Map<String, short[]> BORDER_LINE_STYLE_MAP = new HashMap<String, short[]>();
	private static final Map<String, FontFamily> FONT_FAMILY_MAP = new HashMap<String, FontFamily>();
	private static final Map<String, Byte> FONT_UNDER_LINE_MAP = new HashMap<String, Byte>();
	private static final Map<String, Integer> CELL_TYPE_MAP = new HashMap<String, Integer>();
	private static final Map<String, Integer> PIC_TYPE_MAP = new HashMap<String, Integer>();

	static {
		ALIGNMENT_HORIZONTAL_MAP.put("Left", CellStyle.ALIGN_LEFT);
		ALIGNMENT_HORIZONTAL_MAP.put("Center", CellStyle.ALIGN_CENTER);
		ALIGNMENT_HORIZONTAL_MAP.put("Right", CellStyle.ALIGN_RIGHT);
		ALIGNMENT_HORIZONTAL_MAP.put("Fill", CellStyle.ALIGN_FILL);
		ALIGNMENT_HORIZONTAL_MAP.put("Justify", CellStyle.ALIGN_JUSTIFY);
		ALIGNMENT_HORIZONTAL_MAP.put("CenterAcrossSelection",
				CellStyle.ALIGN_CENTER_SELECTION);

		ALIGNMENT_VERTICAL_MAP.put("Top", CellStyle.VERTICAL_TOP);
		ALIGNMENT_VERTICAL_MAP.put("Bottom", CellStyle.VERTICAL_BOTTOM);
		ALIGNMENT_VERTICAL_MAP.put("Center", CellStyle.VERTICAL_CENTER);
		ALIGNMENT_VERTICAL_MAP.put("Justify", CellStyle.VERTICAL_JUSTIFY);

		FILL_PATTEN_MAP.put("None", CellStyle.NO_FILL);
		FILL_PATTEN_MAP.put("Solid", CellStyle.SOLID_FOREGROUND);

		BORDER_SIDE_MAP.put("Left", BorderSide.LEFT);
		BORDER_SIDE_MAP.put("Right", BorderSide.RIGHT);
		BORDER_SIDE_MAP.put("Top", BorderSide.TOP);
		BORDER_SIDE_MAP.put("Bottom", BorderSide.BOTTOM);

		BORDER_LINE_STYLE_MAP.put("None", new short[] { CellStyle.BORDER_NONE,
				CellStyle.BORDER_NONE, CellStyle.BORDER_NONE,
				CellStyle.BORDER_NONE });
		BORDER_LINE_STYLE_MAP.put("Continuous", new short[] {
				CellStyle.BORDER_HAIR, CellStyle.BORDER_THIN,
				CellStyle.BORDER_MEDIUM, CellStyle.BORDER_THICK });
		BORDER_LINE_STYLE_MAP.put("Dash",
				new short[] { CellStyle.BORDER_DASHED, CellStyle.BORDER_DASHED,
						CellStyle.BORDER_MEDIUM_DASHED,
						CellStyle.BORDER_MEDIUM_DASHED });
		BORDER_LINE_STYLE_MAP.put("Dot", new short[] { CellStyle.BORDER_DOTTED,
				CellStyle.BORDER_DOTTED, CellStyle.BORDER_DOTTED,
				CellStyle.BORDER_DOTTED });
		BORDER_LINE_STYLE_MAP.put("DashDot", new short[] {
				CellStyle.BORDER_DASH_DOT, CellStyle.BORDER_DASH_DOT,
				CellStyle.BORDER_MEDIUM_DASH_DOT,
				CellStyle.BORDER_MEDIUM_DASH_DOT });
		BORDER_LINE_STYLE_MAP.put("DashDotDot", new short[] {
				CellStyle.BORDER_DASH_DOT_DOT, CellStyle.BORDER_DASH_DOT_DOT,
				CellStyle.BORDER_MEDIUM_DASH_DOT_DOT,
				CellStyle.BORDER_MEDIUM_DASH_DOT_DOT });
		BORDER_LINE_STYLE_MAP.put("SlantDashDot", new short[] {
				CellStyle.BORDER_SLANTED_DASH_DOT,
				CellStyle.BORDER_SLANTED_DASH_DOT,
				CellStyle.BORDER_SLANTED_DASH_DOT,
				CellStyle.BORDER_SLANTED_DASH_DOT });
		BORDER_LINE_STYLE_MAP.put("Double", new short[] {
				CellStyle.BORDER_DOUBLE, CellStyle.BORDER_DOUBLE,
				CellStyle.BORDER_DOUBLE, CellStyle.BORDER_DOUBLE });

		FONT_FAMILY_MAP.put("Automatic", FontFamily.NOT_APPLICABLE);
		FONT_FAMILY_MAP.put("Decorative", FontFamily.DECORATIVE);
		FONT_FAMILY_MAP.put("Modern", FontFamily.MODERN);
		FONT_FAMILY_MAP.put("Roman", FontFamily.ROMAN);
		FONT_FAMILY_MAP.put("Script", FontFamily.SCRIPT);
		FONT_FAMILY_MAP.put("Swiss", FontFamily.SWISS);

		FONT_UNDER_LINE_MAP.put("None", Font.U_NONE);
		FONT_UNDER_LINE_MAP.put("Single", Font.U_SINGLE);
		FONT_UNDER_LINE_MAP.put("Double", Font.U_DOUBLE);
		FONT_UNDER_LINE_MAP.put("SingleAccounting", Font.U_SINGLE_ACCOUNTING);
		FONT_UNDER_LINE_MAP.put("DoubleAccounting", Font.U_DOUBLE_ACCOUNTING);

		CELL_TYPE_MAP.put("Number", Cell.CELL_TYPE_NUMERIC);
		CELL_TYPE_MAP.put("DateTime", Cell.CELL_TYPE_STRING);
		CELL_TYPE_MAP.put("Boolean", Cell.CELL_TYPE_BOOLEAN);
		CELL_TYPE_MAP.put("String", Cell.CELL_TYPE_STRING);
		CELL_TYPE_MAP.put("Error", Cell.CELL_TYPE_ERROR);

		PIC_TYPE_MAP.put(".BMP", Workbook.PICTURE_TYPE_DIB);
		PIC_TYPE_MAP.put(".JPG", Workbook.PICTURE_TYPE_JPEG);
		PIC_TYPE_MAP.put(".JEPG", Workbook.PICTURE_TYPE_JPEG);
		PIC_TYPE_MAP.put(".PNG", Workbook.PICTURE_TYPE_PNG);
		PIC_TYPE_MAP.put(".GIF", Workbook.PICTURE_TYPE_PNG);
	}

	private Map<String, Integer> picIndexMap = new HashMap<String, Integer>();
	private Map<String, Short> styleIndexMap = new HashMap<String, Short>();
	private Map<String, Font> fontMap = new HashMap<String, Font>();
	private Map<String, XSSFColor> colorMap = new HashMap<String, XSSFColor>();
	private Map<Integer, Map<Integer, String>> mergedCellMap = new HashMap<Integer, Map<Integer, String>>();

	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private Cell cell;
	private XSSFCellStyle cellStyle;
	private Drawing patriarch;

	private int colnum;
	private int rownum;
	private int columnIndex;

	private boolean inRow;
	private boolean inCell;
	private boolean inCellData;
	private XSSFFont richTextFont;
	private List<Object[]> cellTextCache = new ArrayList<Object[]>();

	/**
	 * Constructor.
	 * 
	 * @param wookbook
	 *            only support SXSSFWorkbook and XSSFWorkbook.
	 */
	public XmlSpreadsheetConverter(Workbook wookbook) {
		this.workbook = wookbook;
	}
	
	/**
	 * Define picture.
	 * 
	 * @param node
	 *            node
	 */
	public void definePicture(XmlSpreadsheetNode node) {
		String id = node.get(XmlSpreadsheetConstants.ATTR_PIC_ID);
		String src = node.get(XmlSpreadsheetConstants.ATTR_PIC_SRC);
		createNewPicture(id, src);
	}

	/**
	 * Insert picture.
	 * 
	 * @param node
	 *            node
	 */
	public void insertPicture(XmlSpreadsheetNode node) {
		String picId = node.get(XmlSpreadsheetConstants.ATTR_PIC_ID);
		String picSource = node.get(XmlSpreadsheetConstants.ATTR_PIC_SRC);
		if (StringUtils.isEmpty(picId) && StringUtils.isEmpty(picSource)) {
			return;
		}

		Integer picIndex = picIndexMap.get(picId);
		if (picIndex == null) {
			picIndex = createNewPicture(picId, picSource);
		}
		if (picIndex == null) {
			return;
		}
		if (patriarch == null) {
			return;
		}

		ClientAnchor clientAnchor = workbook.getCreationHelper().createClientAnchor();
		parsePictureAttributes(node, clientAnchor);
		Picture pic = patriarch.createPicture(clientAnchor, picIndex);
		if (ClientAnchor.MOVE_AND_RESIZE == clientAnchor.getAnchorType()) {
			pic.resize();
		}
	}

	/*
	 * Create new picture, support BMP, JPG, PNG only.
	 */
	private Integer createNewPicture(String picId, String picSource) {
		String validPicId = picId == null ? "*src*:" + picSource : picId;
		Integer picIndex = picIndexMap.get(validPicId);
		if (picIndex != null) {
			return picIndex;
		}
		if (picSource == null) {
			return null;
		}
		Integer picType =
			PIC_TYPE_MAP.get(String.valueOf(picSource.substring(picSource.lastIndexOf("."))).toUpperCase());
		if (picType == null) {
			LOGGER.debug("picture type not support.");
			return null;
		}
		try {
			byte[] bytes = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream(picSource));
			picIndex = Integer.valueOf(workbook.addPicture(bytes, picType));
			picIndexMap.put(validPicId, picIndex);
		} catch (FileNotFoundException e) {
			LOGGER.debug("Image not found!");
		} catch (IOException e) {
			LOGGER.debug("IOException while loading image!");
		}
		return picIndex;
	}

	/*
	 * Parse picture attributes.
	 */
	private void parsePictureAttributes(XmlSpreadsheetNode node, ClientAnchor clientAnchor) {
		boolean resize = true;
		/* default position */
		if (inRow) {
			clientAnchor.setRow1(row.getRowNum());
			clientAnchor.setRow2(row.getRowNum());
		}
		if (inCell) {
			clientAnchor.setCol1(cell.getColumnIndex());
			clientAnchor.setCol2(cell.getColumnIndex());
		}

		Number col1 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_COL1));
		if (col1 != null) {
			clientAnchor.setCol1(col1.intValue());
			clientAnchor.setCol2(col1.intValue());
		}
		Number col2 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_COL2));
		if (col2 != null) {
			clientAnchor.setCol2(col2.intValue());
		}
		Number colDif = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_COL_DIF));
		if (colDif != null) {
			clientAnchor.setCol2(clientAnchor.getCol1() + colDif.byteValue());
		}
		Number row1 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_ROW1));
		if (row1 != null) {
			clientAnchor.setRow1(row1.intValue());
			clientAnchor.setRow2(row1.intValue());
		}
		Number row2 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_ROW2));
		if (row2 != null) {
			clientAnchor.setRow2(row2.intValue());
		}
		Number rowDif = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_ROW_DIF));
		if (rowDif != null) {
			clientAnchor.setRow2(clientAnchor.getRow1() + rowDif.byteValue());
		}
		resize = clientAnchor.getRow1() == clientAnchor.getRow2() && clientAnchor.getCol1() == clientAnchor.getCol2();
		Number dx1 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_DX1));
		if (dx1 != null) {
			resize = false;
			clientAnchor.setDx1(XSSFShape.EMU_PER_PIXEL * dx1.intValue());
		}
		Number dx2 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_DX2));
		if (dx2 != null) {
			resize = false;
			clientAnchor.setDx2(XSSFShape.EMU_PER_PIXEL * dx2.intValue());
		}
		Number dy1 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_DY1));
		if (dy1 != null) {
			resize = false;
			clientAnchor.setDy1(XSSFShape.EMU_PER_PIXEL * dy1.intValue());
		}
		Number dy2 = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_DY2));
		if (dy2 != null) {
			resize = false;
			clientAnchor.setDy2(XSSFShape.EMU_PER_PIXEL * dy2.intValue());
		}

		clientAnchor.setAnchorType(resize ? ClientAnchor.MOVE_AND_RESIZE : ClientAnchor.MOVE_DONT_RESIZE);
	}

	/**
	 * Create style.
	 * 
	 * @param node
	 *            cached node
	 */
	public void createStyle(XmlSpreadsheetNode node) {
		if (workbook != null && node != null) {
			String styleIndexId = node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_ID);
			if (XmlSpreadsheetConstants.DEFAULT_STYLE.equals(styleIndexId)) {
				LOGGER.debug("Get default style.");
				cellStyle = (XSSFCellStyle) workbook.getCellStyleAt((short) 0);
			} else {
				cellStyle = (XSSFCellStyle) workbook.createCellStyle();
				/* clone style form parent */
				String parentId = node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_PARENT);
				Short parentIdx = styleIndexMap.get(parentId);
				if (parentIdx != null) {
					XSSFCellStyle parentStyle = (XSSFCellStyle) workbook
							.getCellStyleAt(parentIdx);
					if (parentStyle != null) {
						cellStyle.cloneStyleFrom(parentStyle);
					}
				}
			}
			styleIndexMap.put(styleIndexId, cellStyle.getIndex());
		}
	}

	/**
	 * Parse style alignment.
	 * 
	 * @param node
	 *            cached node
	 */
	public void parseStyleAlignment(XmlSpreadsheetNode node) {
		if (cellStyle != null && node != null) {
			/* wrap text */
			Number wrap = convertNumber(node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_WRAP_TEXT));
			if (wrap != null) {
				cellStyle.setWrapText(wrap.intValue() != 0);
			}
			/* horizontal */
			String horizontal = node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_ALIGNMENT_HORIZONTAL);
			Short horArg = ALIGNMENT_HORIZONTAL_MAP.get(horizontal);
			if (horArg != null) {
				cellStyle.setAlignment(horArg.shortValue());
			}
			/* vertical */
			String vertical = node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_ALIGNMENT_VERTICAL);
			Short verArg = ALIGNMENT_VERTICAL_MAP.get(vertical);
			if (verArg != null) {
				cellStyle.setVerticalAlignment(verArg.shortValue());
			}
		}
	}

	/**
	 * Set border.
	 * 
	 * @param node
	 *            cached node
	 */
	public void parseStyleBorder(XmlSpreadsheetNode node) {
		if (cellStyle != null && node != null) {
			String position = node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_BORDER_POSITION);
			String lineStyle = node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_BORDER_LINESTYLE);
			Number weight = convertNumber(node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_BORDER_WEIGHT));
			int index = weight == null ? 0 : weight.intValue();
			short[] options = BORDER_LINE_STYLE_MAP.get(lineStyle);

			if (options != null && index < options.length) {
				BorderSide side = BORDER_SIDE_MAP.get(position);
				/* line style */
				short border = options[index];
				if (BorderSide.LEFT == side) {
					cellStyle.setBorderLeft(border);
				} else if (BorderSide.TOP == side) {
					cellStyle.setBorderTop(border);
				} else if (BorderSide.RIGHT == side) {
					cellStyle.setBorderRight(border);
				} else if (BorderSide.BOTTOM == side) {
					cellStyle.setBorderBottom(border);
				}
				/* border color */
				XSSFColor color = convertXSSFColor(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_COLOR));
				if (side != null && color != null) {
					cellStyle.setBorderColor(side, color);
				}
			}
		}
	}

	/**
	 * Parse back ground.
	 * 
	 * @param node
	 *            cached node.
	 */
	public void parseInterior(XmlSpreadsheetNode node) {
		if (cellStyle != null && node != null) {
			Short patten = FILL_PATTEN_MAP.get(node
					.get(XmlSpreadsheetConstants.ATTR_STYLE_PATTERN));
			if (patten != null
					&& CellStyle.SOLID_FOREGROUND == patten.shortValue()) {
				XSSFColor color = convertXSSFColor(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_COLOR));
				if (color != null) {
					cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
					cellStyle.setFillForegroundColor(color);
				}
			}
		}
	}

	/**
	 * Create font for cell style.
	 * 
	 * @param node
	 *            cached node
	 */
	public void createFontForCellStyle(XmlSpreadsheetNode node) {
		if (cellStyle != null && workbook != null && node != null) {
			String attrs = node.attrsToString();
			XSSFFont font = (XSSFFont) fontMap.get(attrs);
			if (font == null) {
				/* create font */
				if (cellStyle.getIndex() == 0) {
					LOGGER.debug("Set param for default font, {}", attrs);
					font = (XSSFFont) workbook.getFontAt((short) 0);
				} else {
					font = (XSSFFont) workbook.createFont();
				}
				/* bold */
				Boolean bold = convertBoolean(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_BOLD));
				if (bold != null) {
					font.setBold(bold.booleanValue());
				}
				/* color */
				XSSFColor color = convertXSSFColor(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_COLOR));
				if (color != null) {
					font.setColor(color);
				}
				/* font name */
				String fontName = node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_NAME);
				if (fontName != null) {
					font.setFontName(fontName);
				}
				/* italic */
				Boolean italic = convertBoolean(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_ITALIC));
				if (italic != null) {
					font.setItalic(italic.booleanValue());
				}
				/* size */
				Number heightInPoints = convertNumber(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_SIZE));
				if (heightInPoints != null) {
					font.setFontHeightInPoints(heightInPoints.shortValue());
				}
				/* strike through */
				Boolean strikeThrough = convertBoolean(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_STRIKE_THROUGH));
				if (strikeThrough != null) {
					font.setStrikeout(strikeThrough.booleanValue());
				}
				/* under line */
				Byte underLine = FONT_UNDER_LINE_MAP
						.get(node
								.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_UNDERLINE));
				if (underLine != null) {
					font.setUnderline(underLine.byteValue());
				}
				/* char set */
				Number charset = convertNumber(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_CHARSET));
				if (charset != null) {
					font.setCharSet(charset.intValue());
				}
				/* family */
				FontFamily family = FONT_FAMILY_MAP.get(node
						.get(XmlSpreadsheetConstants.ATTR_STYLE_FONT_FAMILY));
				if (family != null) {
					font.setFamily(family);
				}
				/* put to map */
				// fontMap.put(attrs, font);
			}

			/* set to style */
			cellStyle.setFont(font);
		}

	}

	/**
	 * Create sheet.
	 * 
	 * @param node
	 *            cached node
	 */
	public void createSheet(XmlSpreadsheetNode node) {
		if (workbook == null || node == null) {
			return;
		}
		String sheetName = node.get(XmlSpreadsheetConstants.ATTR_SHEET_NAME);
		if (sheetName != null && !sheetName.trim().isEmpty()) {
			sheet = workbook.createSheet(sheetName);
			rownum = 0;
			colnum = 0;
			columnIndex = 0;
			patriarch = sheet.createDrawingPatriarch();
		}
	}

	/**
	 * Set sheet parameters form table.
	 * 
	 * @param node
	 *            cached node
	 */
	public void parseTable(XmlSpreadsheetNode node) {
		if (sheet == null || node == null) {
			return;
		}
		/* default row height */
		Number defaultRowHeight = convertNumber(node
				.get(XmlSpreadsheetConstants.ATTR_SHEET_DEFAULT_ROW_HEIGHT));
		if (defaultRowHeight != null) {
			sheet.setDefaultRowHeightInPoints(defaultRowHeight.floatValue());
		}
	}

	/**
	 * Parse column.
	 * 
	 * @param node
	 *            node
	 */
	public void parseColumn(XmlSpreadsheetNode node) {
		if (sheet == null || node == null) {
			return;
		}
		/* default size */
		Short size = workbook.getFontAt((short) 0).getFontHeightInPoints();

		Number span = convertNumber(node
				.get(XmlSpreadsheetConstants.ATTR_COLUMN_SPAN));
		Number columnWidth = convertNumber(node
				.get(XmlSpreadsheetConstants.ATTR_COLUMN_WIDTH));
		int width = -1;
		if (columnWidth != null) {
			// process column width
			width = (int) (columnWidth.shortValue() * size / 58);
			width = Math.min(width, 255) * 256;
			sheet.setColumnWidth(columnIndex++, width);
		}
		if (span != null) {
			for (int i = 0; i < span.intValue(); ++i) {
				if (width != -1) {
					sheet.setColumnWidth(columnIndex++, width);
				}
			}
		}
	}

	/**
	 * Create row.
	 * 
	 * @param node
	 *            cached node
	 */
	public void createRow(XmlSpreadsheetNode node) {
		if (sheet == null || node == null) {
			return;
		}
		/* calculate row index. */
		Number rowIndex = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_ROW_INDEX));
		if (rowIndex != null) {
			rownum = rowIndex.intValue() - 1;
		}
		row = sheet.createRow(rownum);
		inRow = true;
		Number heightInPoints = convertNumber(node.get(XmlSpreadsheetConstants.ATTR_ROW_HEIGHT));
		if (heightInPoints != null) {
			row.setHeightInPoints(heightInPoints.floatValue());
		}
		/* prepare for next row creation */
		colnum = 0;
		rownum++;
		createMergedCellsInCurrentRow(row);
	}

	/**
	 * Clear row flag.
	 */
	public void flushRowValue() {
		inRow = false;
	}

	/**
	 * Create cell.
	 * 
	 * @param node
	 *            cached node
	 */
	public void createCell(XmlSpreadsheetNode node) {
		if (row == null || node == null) {
			return;
		}
		/* calculate column index. */
		Number cellIndex = convertNumber(node
				.get(XmlSpreadsheetConstants.ATTR_CELL_INDEX));
		if (cellIndex != null) {
			colnum = cellIndex.intValue() - 1;
		}
		/* cell style ID for this cell and merged cell */
		String styleId = node.get(XmlSpreadsheetConstants.ATTR_CELL_STYLE_ID);

		/* create cell */
		cell = createBlankCell(row, colnum, styleId);
		inCell = true;
		richTextFont = null;
		cellTextCache.clear();

		/* hyper link */
		String href = node
				.get(XmlSpreadsheetConstants.ATTR_CELL_HYPER_LINK_REF);
		if (href != null) {
			int linkType = 0;
			if (href.startsWith("#") || href.startsWith("=")) {
				linkType = Hyperlink.LINK_DOCUMENT;
			} else if (href.startsWith("http://")
					|| href.startsWith("https://") || href.startsWith("ftp://")) {
				linkType = Hyperlink.LINK_URL;
				href = new String(URLCodec.encodeUrl(null, href.getBytes()));
			} else if (href.startsWith("mailto:")) {
				linkType = Hyperlink.LINK_EMAIL;
				href = new String(URLCodec.encodeUrl(null, href.getBytes()));
			} else {
				linkType = Hyperlink.LINK_FILE;
				href = new String(URLCodec.encodeUrl(null, href.getBytes()));
			}
			Hyperlink link = workbook.getCreationHelper().createHyperlink(
					linkType);
			link.setAddress(href);
			cell.setHyperlink(link);
		}

		/* Merge */
		Number mergeAcross = convertNumber(node
				.get(XmlSpreadsheetConstants.ATTR_CELL_MERGE_ACROSS));
		Number mergeDown = convertNumber(node
				.get(XmlSpreadsheetConstants.ATTR_CELL_MERGE_DOWN));

		if (mergeAcross != null || mergeDown != null) {
			int firstCol = cell.getColumnIndex();
			int firstRow = row.getRowNum();
			int lastCol = firstCol
					+ (mergeAcross == null ? 0 : mergeAcross.intValue());
			int lastRow = firstRow
					+ (mergeDown == null ? 0 : mergeDown.intValue());
			if (lastCol != firstCol || lastRow != firstRow) {
				sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow,
						firstCol, lastCol));
				/* cache merged */
				for (int r = firstRow; r <= lastRow; ++r) {
					Map<Integer, String> map = mergedCellMap.get(Integer
							.valueOf(r));
					if (map == null) {
						map = new HashMap<Integer, String>();
						mergedCellMap.put(Integer.valueOf(r), map);
					}
					for (int c = firstCol; c <= lastCol; ++c) {
						if (r != firstRow || c != firstCol) {
							map.put(Integer.valueOf(c), styleId);
						}
					}
				}
				/* write cells in line. */
				createMergedCellsInCurrentRow(row);
				colnum = lastCol;
			}
		}
		/* prepare for next cell creation. */
		colnum++;
	}

	/**
	 * Parse cell data.
	 * 
	 * @param node
	 *            data
	 */
	public void parseCellData(XmlSpreadsheetNode node) {
		if (cell != null && node != null) {
			Integer type = CELL_TYPE_MAP.get(node
					.get(XmlSpreadsheetConstants.ATTR_CELL_TYPE));
			if (type != null && Cell.CELL_TYPE_BLANK != type.intValue()) {
				/* "string" type only for MVEL value supporting. */
				cell.setCellType(Cell.CELL_TYPE_STRING);
				inCellData = true;
			}
		}
	}

	/**
	 * Parse font style for rich text.
	 * 
	 * @param node
	 *            node
	 */
	public void parseRichTextFontStyle(XmlSpreadsheetNode node) {
		if (richTextFont == null) {
			richTextFont = (XSSFFont) workbook.createFont();
			richTextFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		}
		if (XmlSpreadsheetConstants.QNAME_B.equals(node.getqName())) {
			richTextFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		} else if (XmlSpreadsheetConstants.QNAME_FONT.equals(node.getqName())) {
			XSSFColor color = convertXSSFColor(node
					.get(XmlSpreadsheetConstants.ATTR_HTML_COLOR));
			if (color != null) {
				richTextFont.setColor(color);
			}
		} else if (XmlSpreadsheetConstants.QNAME_I.equals(node.getqName())) {
			richTextFont.setItalic(true);
		} else if (XmlSpreadsheetConstants.QNAME_S.equals(node.getqName())) {
			richTextFont.setStrikeout(true);
		} else if (XmlSpreadsheetConstants.QNAME_U.equals(node.getqName())) {
			richTextFont.setUnderline(Font.U_SINGLE);
		}
	}

	/**
	 * Parse cell text.
	 * 
	 * @param text
	 *            text
	 */
	public void parseCellText(String text) {
		String value = text == null ? "".intern() : text.trim();
		if (inCellData && cell != null && !text.isEmpty()) {

			/* Escape XML characters. */
			value = text.replace("&#10;", "\n").replace("&gt;", ">").replace("&lt;", "<").replace("&amp;", "&");

			cellTextCache.add(new Object[] { value, richTextFont });
			richTextFont = null;
		}
	}

	/**
	 * Set cell text and clear cache.
	 */
	public void flushCellValue() {
		if (cell != null && cellTextCache != null) {
			List<Object[]> richTextParams = new ArrayList<Object[]>();
			StringBuilder sb = new StringBuilder();
			int startIdx = 0;
			for (Object[] cellText : cellTextCache) {
				if (cellText != null && cellText.length > 1) {
					String text = (String) cellText[0];
					Font font = (Font) cellText[1];
					if (text != null) {
						sb.append(text);
						if (font != null) {
							richTextParams.add(new Object[] {
									Integer.valueOf(startIdx),
									Integer.valueOf(startIdx + text.length()),
									font });
						}
						/* offset start index */
						startIdx += text.length();
					}
				}
			}
			if (!richTextParams.isEmpty()) {
				RichTextString rts = workbook.getCreationHelper()
						.createRichTextString(sb.toString());
				/* Rich text is not working for SXSSFWorkbook. */
				for (Object[] param : richTextParams) {
					if (param != null && param.length > 2) {
						int sratr = ((Integer) param[0]).intValue();
						int end = ((Integer) param[1]).intValue();
						Font font = (Font) param[2];
						rts.applyFont(sratr, end, font.getIndex());
					}
				}
				cell.setCellValue(rts);
			} else {
				cell.setCellValue(sb.toString());
			}
		}
		inCell = false;
		inCellData = false;
		richTextFont = null;
		cellTextCache.clear();
	}

	/*
	 * Create blank cells for the merged area in current row.
	 */
	private void createMergedCellsInCurrentRow(Row row) {
		if (row != null) {
			Map<Integer, String> map = mergedCellMap.remove(Integer.valueOf(row
					.getRowNum()));
			if (map != null) {
				for (Entry<Integer, String> entry : map.entrySet()) {
					createBlankCell(row, entry.getKey(), entry.getValue());
				}
			}
		}
	}

	/*
	 * Create blank cell.
	 */
	private Cell createBlankCell(Row row, Integer colnum, String styleId) {
		if (row != null && colnum != null) {
			Cell cell = row.createCell(colnum);
			Short idx = styleIndexMap.get(styleId);
			if (idx != null) {
				cell.setCellStyle(workbook.getCellStyleAt(idx.shortValue()));
			}
			return cell;
		}
		return null;
	}

	/*
	 * Convert to Number.
	 */
	private Number convertNumber(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return (Number) obj;
		} else {
			try {
				return Double.valueOf(String.valueOf(obj));
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	/*
	 * Convert to Boolean
	 */
	private Boolean convertBoolean(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof String) {
			String val = (String) obj;
			if (Boolean.TRUE.toString().equalsIgnoreCase(val)
					|| String.valueOf(1).equals(val)) {
				return Boolean.TRUE;
			} else if (Boolean.FALSE.toString().equalsIgnoreCase(val)
					|| String.valueOf(0).equals(val)) {
				return Boolean.FALSE;
			}
		}
		return null;
	}

	/*
	 * Convert to XSSFolor.
	 */
	private XSSFColor convertXSSFColor(String string) {
		XSSFColor color = colorMap.get(string);
		if (color == null) {
			if (string != null && string.matches("^[#][a-fA-F0-9]{6}$")) {
				if ("#000000".equals(string)) {
					/*
					 * avoid XSSFColor.correctRGB() covert {0,0,0} <->
					 * {-1,-1,-1}
					 */
					color = new XSSFColor();
				} else {
					String r = string.substring(1, 3);
					String g = string.substring(3, 5);
					String b = string.substring(5);
					color = new XSSFColor(new byte[] {
							Integer.valueOf(r, 16).byteValue(),
							Integer.valueOf(g, 16).byteValue(),
							Integer.valueOf(b, 16).byteValue() });
				}
				colorMap.put(string, color);
			}
		}
		return color;
	}

}
