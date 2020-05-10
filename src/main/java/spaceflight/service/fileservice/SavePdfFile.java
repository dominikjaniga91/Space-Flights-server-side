package spaceflight.service.fileservice;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SavePdfFile {

    public Document saveDateToPdfFile(List<? extends Map<String,Object>> elements, Document document){

        int numberOfColumns = elements.get(0).size();
        try{
            document.open();
            PdfPTable table = new PdfPTable(numberOfColumns);

            document.add(table);
            document.close();


        }catch (DocumentException ex){
            System.out.println(ex.getMessage());
        }

        return document;
    }


}
