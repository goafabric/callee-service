//package org.goafabric.calleeservice.pdf;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.Arrays;
//
//@SpringBootTest
//public class InvoiceTest {
//    @Autowired
//    private InvoiceCreatorLogic invoiceCreatorLogic;
//
//    @Test
//    public void createInvoice() throws IOException {
//        var invoiceData = createInvoiceData();
//        Files.write(new File("invoice.pdf").toPath(), invoiceCreatorLogic.createInvoice(invoiceData));
//    }
//
//    private static InvoiceCreatorLogic.InvoiceData createInvoiceData() {
//        var organization = new InvoiceCreatorLogic.Organization("Practice Dr. Hibbert",
//                Arrays.asList("Hibbertstreet 123", "443 Shelbyville"));
//
//        var patient = new InvoiceCreatorLogic.Patient("Homer", "Simpson",
//                Arrays.asList("Mr", "Homer Simpson", "Evergreen Terrace 742", "443 Springfield"));
//
//        var invoiceItems = Arrays.asList(
//                new InvoiceCreatorLogic.ChargeItem("", 1, "5", "2,300", "Examination System", 10.72),
//                new InvoiceCreatorLogic.ChargeItem("", 1, "7", "2,300", "Examination Body", 21.46),
//                new InvoiceCreatorLogic.ChargeItem("", 1, "5", "1,800", "Wrapping Bandages", 4.19));
//
//        var textBlockIntro = "Dear Mr. Simpson,\nfor my work i would like to charge you with the following items:";
//        var textBlockOutro = "Please pay the until the 01.01.2031 refering the invoice number";
//
//        var invoiceData = new InvoiceCreatorLogic.InvoiceData(organization, patient, "E66.00 Adipositas",
//                invoiceItems, textBlockIntro, textBlockOutro);
//        return invoiceData;
//    }
//
//}
