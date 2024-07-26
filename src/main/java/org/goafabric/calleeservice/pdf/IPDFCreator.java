//package org.goafabric.calleeservice.pdf;
//
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.io.FileNotFoundException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
////implementation("com.itextpdf:itext-core:8.0.2")
//@Component
//public class IPDFCreator {
//    @PostConstruct
//    public static void create() {
//        PdfDocument pdf = null;
//        try {
//            pdf = new PdfDocument(new PdfWriter("invoice.pdf"));
//            Document document = new Document(pdf);
//            document.add(new Paragraph("Bart Simpson"))
//                    .add(new Paragraph("Evergreen Terrace 703"))
//                    .add(new Paragraph("Springfield"));
//
//            document.add(new Paragraph(""))
//                    .add(new Paragraph("* We will charge the following *"))
//                    .add(new Paragraph(""));
//
//            var date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//            document.add(new Paragraph("-------------------------------------------------------"))
//                    .add(new Paragraph(String.format("%s   %s   %s   %s", date, "03003", "description", "13.03")))
//                    .add(new Paragraph())
//                    .add(new Paragraph("-------------------------------------------------------"));
//
//            document.close();
//
//            System.out.println("Awesome PDF just got created.");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
