package com.hengxin.platform.report.excel.xml;

public final class XmlSpreadsheetConstants {
	public static final String ENTITY_LINE_BREAK = "&#10;";
	public static final String COLOR_TEXT_FORMAT = "^[#][a-fA-F0-9]{6}$";
	public static final String DEFAULT_STYLE = "Default";
	public static final String DEFAULT_COLOR = "#000000";

	public static final String QNAME_WORKBOOK = "Workbook";
	public static final String QNAME_STYLES = "Styles";
	public static final String QNAME_STYLE = "Style";
	public static final String QNAME_ALIGNMENT = "Alignment";
	public static final String QNAME_BORDERS = "Borders";
	public static final String QNAME_BORDER = "Border";
	public static final String QNAME_FONT = "Font";
	public static final String QNAME_INTERIOR = "Interior";

	public static final String QNAME_WORKSHEET = "Worksheet";
	public static final String QNAME_TABLE = "Table";
	public static final String QNAME_COLUMN = "Column";
	public static final String QNAME_ROW = "Row";
	public static final String QNAME_CELL = "Cell";
	public static final String QNAME_DATA = "Data";
	public static final String QNAME_SS_DATA = "ss:Data";
	public static final String QNAME_B = "B";
	public static final String QNAME_I = "I";
	public static final String QNAME_S = "S";
	public static final String QNAME_U = "U";

	public static final String ATTR_STYLE_ID = "ss:ID";
	public static final String ATTR_STYLE_PARENT = "ss:Parent";
	public static final String ATTR_STYLE_WRAP_TEXT = "ss:WrapText";
	public static final String ATTR_STYLE_ALIGNMENT_VERTICAL = "ss:Vertical";
	public static final String ATTR_STYLE_ALIGNMENT_HORIZONTAL = "ss:Horizontal";
	public static final String ATTR_STYLE_COLOR = "ss:Color";
	public static final String ATTR_STYLE_PATTERN = "ss:Pattern";

	public static final String ATTR_STYLE_FONT_NAME = "ss:FontName";
	public static final String ATTR_STYLE_FONT_CHARSET = "x:CharSet";
	public static final String ATTR_STYLE_FONT_FAMILY = "x:Family";
	public static final String ATTR_STYLE_FONT_SIZE = "ss:Size";
	public static final String ATTR_STYLE_FONT_BOLD = "ss:Bold";
	public static final String ATTR_STYLE_FONT_ITALIC = "ss:Italic";
	public static final String ATTR_STYLE_FONT_STRIKE_THROUGH = "ss:StrikeThrough";
	public static final String ATTR_STYLE_FONT_UNDERLINE = "ss:Underline";
	public static final String ATTR_STYLE_FONT_VERTICAL_ALIGN = "ss:VerticalAlign";

	public static final String ATTR_STYLE_BORDER_POSITION = "ss:Position";
	public static final String ATTR_STYLE_BORDER_LINESTYLE = "ss:LineStyle";
	public static final String ATTR_STYLE_BORDER_WEIGHT = "ss:Weight";

	public static final String ATTR_SHEET_NAME = "ss:Name";
	public static final String ATTR_SHEET_DEFAULT_ROW_HEIGHT = "ss:DefaultRowHeight";
	public static final String ATTR_DEFAULT_COLUMN_HEIGHT = "ss:DefaultRowHeight";
	public static final String ATTR_COLUMN_WIDTH = "ss:Width";
	public static final String ATTR_COLUMN_SPAN = "ss:Span";
	public static final String ATTR_ROW_HEIGHT = "ss:Height";
	public static final String ATTR_ROW_INDEX = "ss:Index";
	public static final String ATTR_CELL_STYLE_ID = "ss:StyleID";
	public static final String ATTR_CELL_INDEX = "ss:Index";
	public static final String ATTR_CELL_TYPE = "ss:Type";
	public static final String ATTR_CELL_HYPER_LINK_REF = "ss:HRef";
	public static final String ATTR_HTML_COLOR = "html:Color";
	public static final String ATTR_CELL_MERGE_ACROSS = "ss:MergeAcross";
	public static final String ATTR_CELL_MERGE_DOWN = "ss:MergeDown";

	/* picture support */
	public static final String QNAME_PIC_DEF = "pic:Define";
	public static final String QNAME_PIC_INSERT = "pic:Insert";
	public static final String ATTR_PIC_ID = "id";
	public static final String ATTR_PIC_SRC = "src";
	public static final String ATTR_COL1 = "col1";
	public static final String ATTR_COL2 = "col2";
	public static final String ATTR_COL_DIF = "colDif";
	public static final String ATTR_ROW1 = "row1";
	public static final String ATTR_ROW2 = "row2";
	public static final String ATTR_ROW_DIF = "rowDif";
	public static final String ATTR_DX1 = "dx1";
	public static final String ATTR_DX2 = "dx2";
	public static final String ATTR_DY1 = "dy1";
	public static final String ATTR_DY2 = "dy2";

	private XmlSpreadsheetConstants() {
	}
}
