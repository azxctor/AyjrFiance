/*
 * Project Name: kmfex
 * File Name: ExcelService.java
 * Class Name: ExcelService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.hengxin.platform.common.domain.ExcelCellsTypeRecognizer;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * Class Name: ExcelService Description: TODO
 * 
 * @author congzhou
 * 
 */

public abstract class ExcelService extends AbstractExcelView {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.view.document.AbstractExcelView#buildExcelDocument(java.util.Map,
     * org.apache.poi.hssf.usermodel.HSSFWorkbook, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String fileName = (String) model.get("fileName");
        String tempPath = (String) model.get("tempPath");
        StringBuffer pathBuffer = new StringBuffer();
        pathBuffer.append(super.getServletContext().getRealPath("WEB-INF"));
        pathBuffer.append(File.separator);
        pathBuffer.append("classes");
        pathBuffer.append(File.separator);
        pathBuffer.append(tempPath);
        tempPath = pathBuffer.toString();
        workbook = new HSSFWorkbook(new FileInputStream(tempPath));
        try {

            buildExcelDocument(model, workbook);
            response.reset();// 清空输出流
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            OutputStream out = response.getOutputStream();

            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * bulid excel doc
     */
    protected abstract void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception;

    /**
     * 获取样式
     * 
     * @param workbook
     * @return
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体;
        HSSFFont font = workbook.createFont();
        // 设置字体大小;
        font.setFontHeightInPoints((short) 12);
        // 设置字体名字;
        font.setFontName("宋体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // // 设置底边框;
        // style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // // 设置底边框颜色;
        // style.setBottomBorderColor(HSSFColor.BLACK.index);
        // // 设置左边框;
        // style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // // 设置左边框颜色;
        // style.setLeftBorderColor(HSSFColor.BLACK.index);
        // // 设置右边框;
        // style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // // 设置右边框颜色;
        // style.setRightBorderColor(HSSFColor.BLACK.index);
        // // 设置顶边框;
        // style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // // 设置顶边框颜色;
        // style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 创建工作簿对象
     * 
     * @param filePath
     * @return
     * @throws IOException
     */
    public Workbook createWorkbook(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("参数错误!!!");
        }
        if (filePath.trim().toLowerCase().endsWith("xls")) {
            return new HSSFWorkbook(new FileInputStream(filePath));
        } else if (filePath.trim().toLowerCase().endsWith("xlsx")) {
            return new XSSFWorkbook(new FileInputStream(filePath));
        } else {
            throw new IllegalArgumentException("不支持除：xls/xlsx以外的文件格式!!!");
        }
    }

    public Workbook createWorkbook(InputStream inputStream, String fileName) throws IOException {
        if (inputStream == null || StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("参数错误!!!");
        }
        if (fileName.trim().toLowerCase().endsWith("xls")) {
            return new HSSFWorkbook(inputStream);
        } else if (fileName.trim().toLowerCase().endsWith("xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("不支持除：xls/xlsx以外的文件格式!!!");
        }
    }

    public Sheet getSheet(Workbook wb, String sheetName) {
        return wb.getSheet(sheetName);
    }

    public Sheet getSheet(Workbook wb, int index) {
        return wb.getSheetAt(index);
    }

    public String buildExcelFilePath(String filepath) {
        StringBuffer pathBuffer = new StringBuffer();
        pathBuffer.append(super.getServletContext().getRealPath("WEB-INF"));
        pathBuffer.append(File.separator);
        pathBuffer.append("classes");
        pathBuffer.append(File.separator);
        pathBuffer.append(filepath);
        return pathBuffer.toString();
    }

    /**
     * 获取单元格内文本信息
     * 
     * @param sheet
     * @return
     */
    public List<Object[]> listFromSheet(Sheet sheet, boolean hasTitle, int rowLength,
            ExcelCellsTypeRecognizer recognizer) {
        // int rowTotal = sheet.getPhysicalNumberOfRows();
        List<Object[]> list = new ArrayList<Object[]>();
        for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null)
                continue;
            // 不能用row.getPhysicalNumberOfCells()，可能会有空cell导致索引溢出
            // 使用row.getLastCellNum()至少可以保证索引不溢出，但会有很多Null值
            boolean isTitle = false;
            if (hasTitle && r == sheet.getFirstRowNum()) {
                isTitle = true;
            }
            addRecognizer(row, isTitle, rowLength, recognizer);
            int lastNum = row.getLastCellNum();
            if (lastNum > rowLength) {
                lastNum = rowLength;
            }
            list.add(listFromRow(row, rowLength));
        }
        return list;
    }

    /**
     * 
     * Description: TODO
     * 
     * @param row
     * @param rowLength
     * @return
     */
    public Object[] listFromRow(Row row, int rowLength) {
        int firstNum = row.getFirstCellNum();
        Object[] cells = new Object[rowLength];
        for (int c = 0; c < rowLength; c++) {
            Cell cell = row.getCell(firstNum + c);
            if (cell == null) {
                cells[c] = StringUtils.EMPTY;
            } else {
                cells[c] = getValueFromCell(cell);
            }

        }
        return cells;
    }

    /**
     * Description: TODO
     * 
     * @param recognizer
     */

    private void addRecognizer(Row row, boolean isTitle, int rowLength, ExcelCellsTypeRecognizer recognizer) {
        if (recognizer != null && !isTitle) {
            int[] cellsType = recognizer.getCellsType();
            int last = row.getLastCellNum();
            int first = row.getFirstCellNum();
            int cellsNo = last - first;
            if (cellsType != null && cellsType.length == rowLength && cellsNo == rowLength) {
                for (int i = 0; i < rowLength; i++) {
                    Cell cell = row.getCell(first + i);
                    if (cell == null) {
                        continue;
                    }
                    cell.setCellType(cellsType[i]);
                }
            }
        }
    }

    /**
     * 获取单元格内文本信息
     * 
     * @param cell
     * @return
     */
    public String getValueFromCell(Cell cell) {
        if (cell == null) {
            return StringUtils.EMPTY;
        }
        String value = null;
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC: // 数字
            if (HSSFDateUtil.isCellDateFormatted(cell)) { // 如果是日期类型
                value = DateUtils.formatDate(cell.getDateCellValue(), "yyyy-MM-dd");
            } else
                value = String.valueOf(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_STRING: // 字符串
            value = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_FORMULA: // 公式
            // 用数字方式获取公式结果，根据值判断是否为日期类型
            double numericValue = cell.getNumericCellValue();
            if (HSSFDateUtil.isValidExcelDate(numericValue)) { // 如果是日期类型
                value = DateUtils.formatDate(cell.getDateCellValue(), "yyyy-MM-dd");
            } else
                value = String.valueOf(numericValue);
            break;
        case Cell.CELL_TYPE_BLANK: // 空白
            value = StringUtils.EMPTY;
            break;
        case Cell.CELL_TYPE_BOOLEAN: // Boolean
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_ERROR: // Error，返回错误码
            value = String.valueOf(cell.getErrorCellValue());
            break;
        default:
            value = StringUtils.EMPTY;
            break;
        }

        return value;
    }

}
