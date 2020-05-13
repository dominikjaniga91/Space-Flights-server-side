package spaceflight.service.fileservice;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
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
            setTableColumnWidth(key, sheet, cell);
            cell.setCellStyle(setHeaderStyle());
            cell.setCellValue(key);
        }
    }

    private void setTableColumnWidth(String key, XSSFSheet sheet, Cell cell){
        int columnIndex = cell.getColumnIndex();
        sheet.setColumnWidth(columnIndex, key.length()*256);
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
        XSSFCellStyle cellStyle = setNormalCellStyle();
        cell.setCellStyle(cellStyle);

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
            setCellDateFormat(cellStyle);
            cell.setCellStyle(cellStyle);
            cell.setCellValue((LocalDate) obj);
        }else if (obj == null){
            cell.setCellValue(" ");
        }
    }

    private void setCellDateFormat(CellStyle cellStyle){
        CreationHelper createHelper = spreadsheet.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("m/d/yy"));
    }

    private XSSFCellStyle setNormalCellStyle(){

        XSSFCellStyle cellStyle = spreadsheet.createCellStyle();
        setCellBorder(cellStyle);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }

    private XSSFCellStyle setHeaderStyle() {
        XSSFCellStyle cellStyle = spreadsheet.createCellStyle();
        byte[] color = {0, 51, 102};
        XSSFColor xssfColor = new XSSFColor(color, null);
        cellStyle.setFillForegroundColor(xssfColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(createCustomHeaderFont());
        return cellStyle;
    }

    private void setCellBorder(XSSFCellStyle cellStyle){
        BorderStyle style = BorderStyle.THIN;
        cellStyle.setBorderBottom(style);
        cellStyle.setBorderTop(style);
        cellStyle.setBorderLeft(style);
        cellStyle.setBorderRight(style);
    }

    private XSSFFont createCustomHeaderFont(){
        XSSFFont xssfFont = spreadsheet.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(9);
        xssfFont.setColor(IndexedColors.WHITE.index);
        return xssfFont;
    }
}
