package spaceflight.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spaceflight.service.fileservice.SavePdfFile;
import spaceflight.service.implementation.FlightServiceImpl;
import spaceflight.service.implementation.PassengerServiceImpl;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfFileController {


    private SavePdfFile pdfService;
    private FlightServiceImpl flightService;
    private PassengerServiceImpl passengerService;
    private Logger logger = LoggerFactory.getLogger(PdfFileController.class);

    @Autowired
    public PdfFileController(SavePdfFile pdfService,
                            FlightServiceImpl flightService,
                            PassengerServiceImpl passengerService) {

        this.pdfService = pdfService;
        this.flightService = flightService;
        this.passengerService = passengerService;
    }

    @GetMapping("/flights")
    public void generateFlightsPdfFile(HttpServletResponse response){

        List<Map<String,Object>> elements = flightService.getFlightsAsListOfMaps();
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

    @GetMapping("/passengers")
    public void generatePassengersPdfFile(HttpServletResponse response){

        List<Map<String,Object>> passengers = passengerService.getPassengersAsListOfMaps();
        Document document = new Document();
        try(OutputStream outStream = response.getOutputStream()){
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, outStream);
            pdfService.saveDateToPdfFile(passengers, document);
            outStream.flush();
        }catch (IOException | DocumentException ex){
            logger.error(ex.getMessage());
        }
    }
}
