package org.example.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtility {

    private static Workbook book;
    private static Sheet sheet;

    private static void openExcel(String filePath){

        try {
            FileInputStream fis = new FileInputStream(filePath);
            book = new XSSFWorkbook(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSheet(String sheetName){
        sheet = book.getSheet(sheetName);
    }

    private static int rowCount(){
        return sheet.getPhysicalNumberOfRows();
    }

    private static int colsCount(int rowIndex){
        return sheet.getRow(rowIndex).getLastCellNum();
    }

    private static String cellData(int rowIndex, int colIndex){
        return sheet.getRow(rowIndex).getCell(colIndex).toString();
    }

    /**
     * returns a 2d array
     * @param filePath of the worksheet
     * @param sheetName sheet name of worksheet
     * @return 2D array
     */
    public static Object[][] excelToArray(String filePath, String sheetName){

        openExcel(filePath);
        loadSheet(sheetName);
        int rows = rowCount();
        int cols = colsCount(rows);

        Object[][] data = new Object[rows - 1][cols];
//        iterate over rows
        for (int row = 1; row < rows; row ++){
//            iterate over columns
            for (int col = 0; col < cols; col++){
                data[row - 1][col] = cellData(row, col);
            }
        }
        return data;
    }

    public static List<Map<String, String>> excelIntoListOfMaps(String filePath, String sheetName){

        openExcel(filePath);
        loadSheet(sheetName);

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> excelMap;

        for (int row = 1; row < rowCount(); row++){
            excelMap = new LinkedHashMap<>();
            for (int col = 0; col < colsCount(row); col++){
                excelMap.put(cellData(0, col), cellData(row, col));
            }
            list.add(excelMap);
        }
        return list;
    }
}
