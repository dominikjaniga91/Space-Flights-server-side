package spaceflight.service.implementation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spaceflight.service.SavePdfService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SavePdfFileImpl implements SavePdfService {

    private final Logger logger = LoggerFactory.getLogger(SavePdfFileImpl.class);

    @Override
    public Document saveDateToPdfFile(List<? extends Map<String,Object>> elements, Document document){

        int numberOfColumns = elements.get(0).size();
        try{
            document.open();
            PdfPTable table = createPdfTable(elements, numberOfColumns);
            createTableHeader(elements, table);
            saveDataToTable(elements, table);
            document.add(table);

        }catch (DocumentException ex){
           logger.error(ex.getMessage());
        }finally {
            document.close();
        }
        return document;
    }

    private PdfPTable createPdfTable(List<? extends Map<String, Object>> elements, int numberOfColumns) throws DocumentException {

        PdfPTable table = new PdfPTable(numberOfColumns);
        int[] columnsWidth = setUpTableColumnWidth(elements, numberOfColumns);
        table.setWidths(columnsWidth);
        table.setWidthPercentage(100);

        return table;
    }


    private int[] setUpTableColumnWidth(List<? extends Map<String,Object>> elements,
                                        int numberOfColumns){

        int[] columnsWidth = new int[numberOfColumns];
        List<Integer> numbersOfChars = new ArrayList<>();
        elements.get(0).keySet().forEach(key -> numbersOfChars.add(key.length()));

        for (int i=0; i<columnsWidth.length ; i++) {
            columnsWidth[i] = numbersOfChars.get(i);
        }
        return columnsWidth;
    }

    private void createTableHeader(List<? extends Map<String,Object>> elements, PdfPTable table) {

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        elements.get(0).keySet().forEach(key -> {
            Paragraph paragraph = new Paragraph(key.toUpperCase(), headerFont);
            PdfPCell header = new PdfPCell(paragraph);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setPaddingTop(5);
            header.setPaddingBottom(5);
            header.setBackgroundColor(new CMYKColor(0.644f, 0.413f, 0f, 0.525f));
            table.addCell(header);

        });
    }

    private void saveDataToTable(List<? extends Map<String,Object>> elements, PdfPTable table){

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
        for (Map<String, Object> element : elements) {
            for(Object object : element.values()){
                String value = object != null ? object.toString() : " ";
                Paragraph paragraph = new Paragraph(value, cellFont);
                PdfPCell row = new PdfPCell(paragraph);
                row.setHorizontalAlignment(Element.ALIGN_CENTER);
                row.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(row);
            }
        }
    }
}
