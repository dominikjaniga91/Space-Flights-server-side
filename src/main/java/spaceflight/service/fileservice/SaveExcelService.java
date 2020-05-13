package spaceflight.service.fileservice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SaveExcelService {

    XSSFWorkbook spreadsheet;

    public XSSFWorkbook saveDataToFile(List<? extends Map<String, Object>> elements) {
        spreadsheet = createNewXlsxFile();
        XSSFSheet sheet = spreadsheet.getSheetAt(0);
        setUpSheetHeaders(elements, sheet);
        saveDataIntoRows(elements, sheet);
        return spreadsheet;
    }

    private XSSFWorkbook createNewXlsxFile(){
        XSSFWorkbook spreadsheet = new XSSFWorkbook();
        spreadsheet.createSheet("spaceflights_data");
        return spreadsheet;
    }

    private void setUpSheetHeaders(List<? extends Map<String,Object>> elements, XSSFSheet sheet) {
        Row firstRow = sheet.createRow(0);
        int headerCellNumber = 0;
        for (String key : elements.get(0).keySet()  ) {
            Cell cell = firstRow.createCell(headerCellNumber++);
            cell.setCellValue(key);
        }
    }

    private void saveDataIntoRows(List<? extends Map<String,Object>> elements, XSSFSheet sheet){
        int rowNumber = sheet.getLastRowNum() + 1;
        for (Map<String, Object> element : elements) {

            Row row = sheet.createRow(rowNumber++);
            int cellNumber = 0;
            for (Map.Entry<String, Object> object : element.entrySet()) {

                Cell cell = row.createCell(cellNumber++);
                Object obj = object.getValue();
                setValueToCell(cell, obj);
            }
        }
    }

    private void setValueToCell(Cell cell, Object obj) {
        if (obj instanceof Integer) {
            cell.setCellValue((Integer) obj);
        } else if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Long) {
            cell.setCellValue((Long) obj);
        } else if (obj instanceof Double) {
            cell.setCellValue((Double) obj);
        } else if (obj instanceof Date) {
            cell.setCellValue((Date) obj);
        } else if (obj instanceof Boolean) {
            cell.setCellValue((Boolean) obj);
        }else if (obj instanceof LocalDate){
            setCellDateFormat(cell);
            cell.setCellValue((LocalDate) obj);
        }else if (obj == null){
            cell.setCellValue(" ");
        }
    }

    private void setCellDateFormat(Cell cell){
        CellStyle cellStyle = spreadsheet.createCellStyle();
        CreationHelper createHelper = spreadsheet.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("m/d/yy"));
        cell.setCellStyle(cellStyle);
    }
}
