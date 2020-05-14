package spaceflight.service;

import com.itextpdf.text.Document;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface SavePdfService {

    Document saveDateToPdfFile(List<? extends Map<String,Object>> elements, Document document);

}
