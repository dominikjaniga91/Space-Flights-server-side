package spaceflight.controller;

import spaceflight.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AddFromExcelController {


    private ExcelService excelService;

    @Autowired
    public AddFromExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/addFromExcel")
    @ResponseStatus(HttpStatus.OK)
    public void addDataFromExcel(){

        excelService.addFromExcelFile();

    }
}
