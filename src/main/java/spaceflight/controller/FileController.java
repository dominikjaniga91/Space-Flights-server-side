package spaceflight.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spaceflight.service.fileservice.SaveExcelService;
import spaceflight.service.implementation.FlightServiceImpl;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class FileController {

    private SaveExcelService excelService;
    private FlightServiceImpl flightService;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public FileController(SaveExcelService excelService,
                          FlightServiceImpl flightService) {
        this.excelService = excelService;
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public void generateXlsxFile(HttpServletResponse response) {

        List<Map<String,Object>> elements = flightService.getFlightsAsListOfMaps();
        XSSFWorkbook spreadSheet = excelService.saveDataToFile(elements);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try(OutputStream outStream = response.getOutputStream()){
            spreadSheet.write(outStream);
            outStream.flush();
        }catch (IOException ex){
            logger.error(ex.getMessage());
        }
    }


}
