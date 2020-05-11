package spaceflight.service.fileservice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SavePdfFile {
    Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);

    public Document saveDateToPdfFile(List<? extends Map<String,Object>> elements, Document document){

        int numberOfColumns = elements.get(0).size();
        try{
            document.open();
            PdfPTable table = new PdfPTable(numberOfColumns);

            //create header
            int[] columnsWidth = setUpTableColumnWidth(elements, numberOfColumns);

            table.setWidths(columnsWidth);
            table.setWidthPercentage(100);

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
            document.add(table);
            document.close();


        }catch (DocumentException ex){
            System.out.println(ex.getMessage());
        }

        return document;
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


}
