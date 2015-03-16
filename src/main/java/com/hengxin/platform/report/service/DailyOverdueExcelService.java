package com.hengxin.platform.report.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.report.consts.ReportConsts;
import com.hengxin.platform.report.dto.DailyOverDueDetailDto;

@Service
public class DailyOverdueExcelService extends ExcelService {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<DailyOverDueDetailDto> list = (List<DailyOverDueDetailDto>) model.get("applList");
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet(ReportConsts.SHEETNAME_DAILY_OVERDUE);
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);
        for (DailyOverDueDetailDto dailyOverDueDetailDto : list) {

            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(dailyOverDueDetailDto.getSequenceId());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getSignContractTime());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getId());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getPackageName());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getPackageQuota().toString());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getAccountNo());
            cell.setCellStyle(style);

            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getFinancierName());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getWrtrName());
            cell.setCellStyle(style);

            cell = row.createCell(8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getPayDate());
            cell.setCellStyle(style);

            cell = row.createCell(9);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(dailyOverDueDetailDto.getOverdueDay());
            cell.setCellStyle(style);

            cell = row.createCell(10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getWarrantyType().getText());
            cell.setCellStyle(style);

            cell = row.createCell(11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getItem());
            cell.setCellStyle(style);

            cell = row.createCell(12);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getMonthPrincipal().toString());
            cell.setCellStyle(style);

            a++;
        }

    }

}
