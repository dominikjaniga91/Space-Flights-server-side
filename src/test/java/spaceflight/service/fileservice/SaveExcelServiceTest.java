package spaceflight.service.fileservice;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SaveExcelServiceTest {

    private SaveExcelService saveExcelService;

    @BeforeEach
    void setUp() {
        saveExcelService = new SaveExcelService();
    }

    @Test
    @DisplayName("Should return spreadSheet name 'users' after create new file")
    void shouldReturnSpreadSheetName_afterCreateNewXlsxFile(){

        XSSFWorkbook spreadsheet = saveExcelService.createNewXlsxFile("Users");
        Assertions.assertEquals("Users", spreadsheet.getSheetName(0));

    }

}
