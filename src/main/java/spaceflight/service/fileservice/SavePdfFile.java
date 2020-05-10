package spaceflight.service.fileservice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SavePdfFile {
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

    public Document saveDateToPdfFile(List<? extends Map<String,Object>> elements, Document document){

        int numberOfColumns = elements.get(0).size();
        try{
            document.open();
            PdfPTable table = new PdfPTable(numberOfColumns);

            //create header
            int[] columnsWidth = new int[numberOfColumns];
            List<Integer> numbersOfChars = new ArrayList<>();
            elements.get(0).keySet().forEach(key -> numbersOfChars.add(key.length()));

            for (int i=0; i<columnsWidth.length ; i++) {
                columnsWidth[i] = numbersOfChars.get(i);
            }

            table.setWidths(columnsWidth);
            table.setWidthPercentage(100);

            for (Map<String, Object> element : elements) {

                for(Object object : element.values()){
                    String value = object != null ? object.toString() : " ";
                    Paragraph paragraph = new Paragraph(value, headerFont);
                    PdfPCell row = new PdfPCell(paragraph);
                    row.setHorizontalAlignment(Element.ALIGN_CENTER);
                    row.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(row);
                }
            }
            document.add(table);
            document.close();


        }catch (DocumentException ex){
            System.out.println(ex.getMessage());
        }

        return document;
    }


}
