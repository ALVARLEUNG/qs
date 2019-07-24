package com.myproject.qs.qs.common.utils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ExcelUtil {

    public static Workbook getWorkbookFromFile(MultipartFile file) {
        Workbook wb = null;
        InputStream in = null;
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        try {
            in = file.getInputStream();
            if (".xlsx".equalsIgnoreCase(suffix)) {
                wb = WorkbookFactory.create(in);
            } else {
                wb = new HSSFWorkbook(in);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }

    public static boolean validateData(Row row, int index) {
        int count = 0;
        String cellVal = getStringCellValue(row.getCell(index));
        if (StringUtils.isEmpty(cellVal)) {
                count++;
        }

        if (count == 0 ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFull(Row trRow, int firstIndex, int lastIndex) {
        int i = 0;
        for (i = firstIndex; i <= lastIndex; i++) {
            String cellVal = getStringCellValue(trRow.getCell(i));
            if (StringUtils.isEmpty(cellVal)) {
                break;
            }
        }
        if (i != lastIndex + 1) {
            return false;
        } else {
            return true;
        }
    }

    public static String getStringCellValue(Cell cell) {
        if (cell != null) {
            String cellTypeName = cell.getCellTypeEnum().name();
            if ("STRING".equals(cellTypeName)) {
                return String.valueOf(cell.getStringCellValue()).trim();
            } else if ("NUMERIC".equals(cellTypeName)) {
                // numeric trim '.'
                String cellStr = String.valueOf(cell.getNumericCellValue());
                return cellStr.substring(0, cellStr.lastIndexOf("."));
            } else if ("BOOLEAN".equals(cellTypeName)) {
                return String.valueOf(cell.getBooleanCellValue());
            } else {
                return "";
            }
        }
        return "";
    }

}
