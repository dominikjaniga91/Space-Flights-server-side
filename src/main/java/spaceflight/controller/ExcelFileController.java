package spaceflight.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spaceflight.service.implementation.SaveExcelServiceImpl;
import spaceflight.service.implementation.FlightServiceImpl;
import spaceflight.service.implementation.PassengerServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelFileController {

    private SaveExcelServiceImpl excelService;
    private FlightServiceImpl flightService;
    private PassengerServiceImpl passengerService;
    private Logger logger = LoggerFactory.getLogger(ExcelFileController.class);

    @Autowired
    public ExcelFileController(SaveExcelServiceImpl excelService,
                               FlightServiceImpl flightService,
                               PassengerServiceImpl passengerService) {
        this.excelService = excelService;
        this.flightService = flightService;
        this.passengerService = passengerService;
    }

    @GetMapping("/flights")
    public void generateFlightsXlsxFile(HttpServletResponse response) {

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

    @GetMapping("/passengers")
    public void generatePassengersXlsxFile(HttpServletResponse response) {

        List<Map<String,Object>> passengers = passengerService.getPassengersAsListOfMaps();
        XSSFWorkbook spreadSheet = excelService.saveDataToFile(passengers);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try(OutputStream outStream = response.getOutputStream()){
            spreadSheet.write(outStream);
            outStream.flush();
        }catch (IOException ex){
            logger.error(ex.getMessage());
        }
    }


}
