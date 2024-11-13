//package org.goafabric.calleeservice.pdf;
//
//import com.lowagie.text.*;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import com.lowagie.text.pdf.draw.LineSeparator;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayOutputStream;
//
//@Component
//public class InvoiceCreatorLogic {
//    public record Organization(String name, java.util.List<String> address) {}
//    public record Patient(String givenName, String familyName, java.util.List<String> address) {}
//    public record ChargeItem(
//            String date,
//            int quantity,
//            String code,
//            String factor,
//            String description,
//            double amount
//    ) {}
//
//
//    public record InvoiceData(
//            Organization organization,
//            Patient patient,
//            String condition,
//            java.util.List<ChargeItem> chargeItems,
//
//            String textBlockIntro,
//            String textBlockOutro
//    ) {
//    }
//
//    public byte[] createInvoice(InvoiceData invoiceData) {
//        var document = new Document(PageSize.A4, 50, 50, 50, 50);
//        var fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);  // Regular font for other text
//
//        try (var out = new ByteArrayOutputStream()) {
//            PdfWriter.getInstance(document, out);
//
//            document.open();
//
//            addOrganizationHeader(document, invoiceData);
//            addPatientAddressAndIinvoiceNumber(document, invoiceData);
//            addConditionHeader(document, invoiceData);
//
//            document.add(new Paragraph(invoiceData.textBlockIntro(), fontNormal));
//
//            addChargeItems(document, invoiceData.chargeItems());
//
//            document.add(new Paragraph(invoiceData.textBlockOutro(), fontNormal));
//
//            document.close();
//            return out.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static void addOrganizationHeader(Document document, InvoiceData invoiceData) throws DocumentException {
//        var fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
//        var fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//        var titleParagraph = new Paragraph(invoiceData.organization().name(), fontBold);
//        titleParagraph.setAlignment(Element.ALIGN_CENTER);
//
//        var addressParagraph = new Paragraph(String.join("\n", invoiceData.organization().address()), fontNormal);
//        addressParagraph.setAlignment(Element.ALIGN_CENTER);
//
//        document.add(titleParagraph);
//        document.add(addressParagraph);
//
//        document.add(new Chunk(new LineSeparator()));
//    }
//
//    private static void addPatientAddressAndIinvoiceNumber(Document document, InvoiceData invoiceData) {
//        var table = new PdfPTable(2);
//        table.setWidthPercentage(100);  // Set table width to 100%
//
//        table.addCell(createCell(String.join("\n", invoiceData.patient().address), Element.ALIGN_LEFT));
//        table.addCell(createCell("Invoicenumber 29022024-165", Element.ALIGN_RIGHT));
//        table.addCell(createCell("Springfield, 01.01.2030", Element.ALIGN_RIGHT));
//
//        document.add(table);
//    }
//
//
//    private static PdfPCell createCell(String string, int allignment) {
//        var font = FontFactory.getFont(FontFactory.HELVETICA, 12);
//        var cell = createCell(new Paragraph(string, font));
//        cell.setBorder(Rectangle.NO_BORDER);
//        cell.setHorizontalAlignment(allignment);
//        cell.setVerticalAlignment(Element.ALIGN_TOP);
//        return cell;
//    }
//
//    private static void addConditionHeader(Document document, InvoiceData invoiceData) throws DocumentException {
//        var fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
//        var fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//        document.add(new Paragraph("\n\n\n\n"));
//
//        var diagnosisParagraph = new Paragraph("Diagnosis:\n", fontBold);
//        diagnosisParagraph.add(new Phrase(invoiceData.condition, fontNormal));
//
//        document.add(diagnosisParagraph);
//        document.add(new Paragraph("\n"));
//    }
//
//    private static void addChargeItems(Document document, java.util.List<ChargeItem> items) throws DocumentException {
//        // Create the table with 6 columns, one for each field
//        var table = new PdfPTable(6);
//        table.setWidthPercentage(100);
//
//        float[] columnWidths = {1.5f, 1f, 1f, 1f, 3f, 1.5f};
//        table.setWidths(columnWidths);
//
//        document.add(new Paragraph(new Chunk(new LineSeparator())));
//        addHeaderRow(table);
//
//        double totalAmount = 0.0;
//
//        // Add data rows and calculate the total amount
//        for (ChargeItem item : items) {
//            addDataRow(table, item);
//            totalAmount += (item.quantity * item.amount());
//        }
//        // Add separator line below the table
//
//        addTotalAmount(document, table, totalAmount);
//        document.add(table);
//        document.add(new Paragraph(new Chunk(new LineSeparator())));
//    }
//
//    private static void addHeaderRow(PdfPTable table) {
//        var fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
//
//        table.addCell(createCell(new Phrase("Date", fontBold)));
//        table.addCell(createCell(new Phrase("Quantity", fontBold)));
//        table.addCell(createCell(new Phrase("Code", fontBold)));
//        table.addCell(createCell(new Phrase("Factor", fontBold)));
//        table.addCell(createCell(new Phrase("Description", fontBold)));
//        table.addCell(createCell(new Phrase("Value in €", fontBold)));
//    }
//
//
//    private static void addDataRow(PdfPTable table, ChargeItem item) {
//        var fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
//        table.addCell(createCell(new Phrase(item.date(), fontNormal)));
//        table.addCell(createCell(new Phrase(String.valueOf(item.quantity()), fontNormal)));
//        table.addCell(createCell(new Phrase(item.code(), fontNormal)));
//        table.addCell(createCell(new Phrase(item.factor(), fontNormal)));
//        table.addCell(createCell(new Phrase(item.description(), fontNormal)));
//        table.addCell(createCell(new Phrase(String.format("%.2f", item.amount()), fontNormal)));
//    }
//
//    private static PdfPCell createCell(Phrase font) {
//        var cell = new PdfPCell(font);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setBorder(Rectangle.NO_BORDER);
//        return cell;
//    }
//
//    private static void addTotalAmount(Document document, PdfPTable table, double totalAmount) {
//        var fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
//        PdfPCell totalCell = new PdfPCell(new Phrase("Total", fontBold));
//        totalCell.setColspan(5);  // Span across 5 columns
//        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        totalCell.setBorder(Rectangle.NO_BORDER);
//        table.addCell(totalCell);
//
//        PdfPCell amountCell = new PdfPCell(new Phrase(String.format("%.2f €", totalAmount), fontBold));
//        amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        amountCell.setBorder(Rectangle.NO_BORDER);
//        table.addCell(amountCell);
//    }
//
//}
