package spaceflight.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.util.List;
import java.util.Map;

public interface SaveExcelService {

    XSSFWorkbook saveDataToFile(List<? extends Map<String, Object>> elements);

}
