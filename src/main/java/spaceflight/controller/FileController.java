package spaceflight.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spaceflight.service.fileservice.SaveExcelService;
import spaceflight.service.fileservice.SavePdfFile;
import spaceflight.service.implementation.FlightServiceImpl;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/save")
public class FileController {

    private SaveExcelService excelService;
    private SavePdfFile pdfService;
    private FlightServiceImpl flightService;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public FileController(SaveExcelService excelService,
                          SavePdfFile pdfService,
                          FlightServiceImpl flightService) {
        this.excelService = excelService;
        this.pdfService = pdfService;
        this.flightService = flightService;
    }

    @GetMapping("/excel")
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

    @PostMapping("/pdf")
    public void generatePdfFile(@RequestBody List<LinkedHashMap<String, Object>> elements,
                                HttpServletResponse response){
        Document document = new Document();
        try(OutputStream outStream = response.getOutputStream()){
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, outStream);
            pdfService.saveDateToPdfFile(elements, document);
            outStream.flush();
        }catch (IOException | DocumentException ex){
            logger.error(ex.getMessage());
        }
    }
}
