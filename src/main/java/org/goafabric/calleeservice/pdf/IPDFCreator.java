//package org.goafabric.calleeservice.pdf;
//
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//
//import java.io.FileNotFoundException;
//
////implementation("com.itextpdf:itext-core:8.0.2")
//public class IPDFCreator {
//    public static void create() {
//        PdfDocument pdf = null;
//        try {
//            pdf = new PdfDocument(new PdfWriter("hello.pdf"));
//            Document document = new Document(pdf);
//            String line = "Hello! Welcome to iTextPdf";
//            document.add(new Paragraph(line));
//            document.close();
//
//            System.out.println("Awesome PDF just got created.");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
