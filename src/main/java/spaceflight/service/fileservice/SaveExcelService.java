package spaceflight.service.fileservice;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class SaveExcelService {


    public XSSFWorkbook createNewXlsxFile(String fileName){
        XSSFWorkbook spreadsheet = new XSSFWorkbook();
        spreadsheet.createSheet(fileName);
        return  spreadsheet;
    }

}
