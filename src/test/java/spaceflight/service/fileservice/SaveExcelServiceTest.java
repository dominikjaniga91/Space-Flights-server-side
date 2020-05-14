package spaceflight.service.fileservice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest
public class SaveExcelServiceTest {

    private SaveExcelService saveExcelService;

    @BeforeEach
    void setUp() {
        saveExcelService = new SaveExcelService();
    }


    @Test
    @DisplayName("Should return number of rows after save data to xlsx file")
    void shouldReturnRightNumberOfRowsANdColumns_afterSaveDataToXlsxFile(){
        LinkedHashMap<String, Object> user1 = new LinkedHashMap<>();
        user1.put("firstName","Dominik");
        user1.put("lastName","Janiga");

        LinkedHashMap<String, Object> user2 = new LinkedHashMap<>();
        user2.put("firstName","Jan");
        user2.put("lastName","Kowalski");

        List<Map<String, Object>> users = List.of(user1, user2);
        XSSFWorkbook spreadsheet = saveExcelService.saveDataToFile(users);

        int rows = spreadsheet.getSheetAt(0).getLastRowNum();

        Assertions.assertEquals(2, rows);
    }

    @Test
    @DisplayName("Should return first name Dominik after save data to xlsx file")
    void shouldReturnFirstNameDominik_afterSaveDataToXlsxFile(){
        LinkedHashMap<String, Object> user1 = new LinkedHashMap<>();
        user1.put("firstName","Dominik");
        user1.put("lastName","Janiga");

        LinkedHashMap<String, Object> user2 = new LinkedHashMap<>();
        user2.put("firstName","Jan");
        user2.put("lastName","Kowalski");

        List<LinkedHashMap<String, Object>>  users = List.of(user1, user2);
        XSSFWorkbook spreadsheet = saveExcelService.saveDataToFile(users);

        XSSFSheet sheet = spreadsheet.getSheetAt(0);
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(0);
        String actualValue =cell.getStringCellValue();

        Assertions.assertEquals("Dominik", actualValue);
    }

}
