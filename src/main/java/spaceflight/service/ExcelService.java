package spaceflight.service;

import spaceflight.model.Flight;
import spaceflight.repository.FlightRepositoryImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExcelService {

    @Autowired
    private FlightRepositoryImpl flightRepository;

    private File myFile = new File("C:\\Users\\PackardBell\\Desktop\\space.xlsx");
    private HashMap<Integer, List<Object>> mapa = new HashMap<>();

    public void addFromExcelFile(){

        try{

            FileInputStream inputStream = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook (inputStream);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();

            int i =0;
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                List<Object> myList = new ArrayList<>();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    myList.add(cell);
                }
                mapa.put(i, myList );

                i++;
            }
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            for(Map.Entry<Integer, List<Object>> data : mapa.entrySet()) {

                Flight flight = new Flight();

                flight.setDestination(String.valueOf(data.getValue().get(0)));
                flight.setStartDate(LocalDate.parse(String.valueOf(data.getValue().get(1)), formater));
                flight.setFinishDate(LocalDate.parse(String.valueOf(data.getValue().get(2)), formater));
                flight.setNumberOfSeats((int) Double.parseDouble(String.valueOf(data.getValue().get(3))));
                flight.setAmountOfPassengers((int) Double.parseDouble(String.valueOf(data.getValue().get(4))));
                flight.setTicketPrice((int) Double.parseDouble(String.valueOf(data.getValue().get(5))));

                flightRepository.save(flight);
            }
        }catch (Exception ex){

            ex.getMessage();
            ex.printStackTrace();
        }
    }
}
