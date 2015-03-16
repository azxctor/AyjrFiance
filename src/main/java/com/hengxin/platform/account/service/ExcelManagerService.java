package com.hengxin.platform.account.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;

/**
 * 导出excel
 * 
 * @author jishen
 * 
 */
@Service
public class ExcelManagerService extends ExcelService {

    /* (non-Javadoc)
     * @see com.hengxin.platform.common.service.ExcelService#buildExcelDocument(java.util.Map, org.apache.poi.hssf.usermodel.HSSFWorkbook)
     */

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<WithdrawDepositApplPo> list = (List<WithdrawDepositApplPo>) model.get("applList");
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet(DictConsts.ICBC_SHEETNAME);
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);
        for (WithdrawDepositApplPo po : list) {

            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(po.getBnkAcctName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(po.getBnkAcctNo());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(po.getTrxAmt().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if (null != po.getTrxMemo()
                    && po.getTrxMemo().getBytes().length > 12) {
                cell.setCellValue("");
            } else if (null != po.getTrxMemo()
                    && po.getTrxMemo().getBytes().length <= 12) {
                cell.setCellValue(po.getTrxMemo());
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(style);

            a++;
        }

    }

    /**
     * 获取导出的excel
     * 
     * @param list
     *            数据
     * @param filepath
     *            模板地址
     * @param sheetname
     *            工作表名称
     * @param out
     *            输出流
     * @throws FileNotFoundException
     * @throws IOException
     * @deprecated
     */
    public void getExcel(List<WithdrawDepositApplPo> list, String sheetname,
            OutputStream out) throws FileNotFoundException, IOException {

        String filepath = AppConfigUtil.getExcelTemplatePath();// excel模板路径

        StringBuffer pathBuffer = new StringBuffer();
        pathBuffer.append(super.getServletContext().getRealPath("WEB-INF"));
        pathBuffer.append(File.separator);
        pathBuffer.append("classes");
        pathBuffer.append(File.separator);
        pathBuffer.append(filepath);
        String appPath = pathBuffer.toString();

        // 读取工作簿
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(appPath));
        try {

            // 读取工作表
            HSSFSheet sheet = workbook.getSheet(sheetname);
            HSSFRow row;
            HSSFCell cell = null;
            int a = 1;
            HSSFCellStyle style = this.getStyle(workbook);
            for (WithdrawDepositApplPo po : list) {

                row = sheet.getRow(a);
                cell = row.createCell(0);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(po.getBnkAcctName());
                cell.setCellStyle(style);

                cell = row.createCell(1);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(po.getBnkAcctNo());
                cell.setCellStyle(style);

                cell = row.createCell(2);
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(po.getTrxAmt().doubleValue());
                cell.setCellStyle(style);

                cell = row.createCell(3);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                if (null != po.getTrxMemo()
                        && po.getTrxMemo().getBytes().length > 12) {
                    cell.setCellValue("");
                } else if (null != po.getTrxMemo()
                        && po.getTrxMemo().getBytes().length <= 12) {
                    cell.setCellValue(po.getTrxMemo());
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(style);

                a++;
            }
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
