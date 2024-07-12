//package org.goafabric.calleeservice.pdf;
//
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
//import org.springframework.aot.hint.RuntimeHints;
//import org.springframework.aot.hint.RuntimeHintsRegistrar;
//import org.springframework.context.annotation.ImportRuntimeHints;
//import org.springframework.stereotype.Component;
//
////implementation("org.apache.pdfbox:pdfbox:3.0.2") {exclude("commons-logging", "commons-logging")};
//@Component
//@ImportRuntimeHints(PDFBoxCreator.PdfBoxRuntimeHints.class)
//public class PDFBoxCreator {
//    public void create() {
//        try (PDDocument document = new PDDocument()) {
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            System.out.println("creating pdf ...");
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                contentStream.beginText();
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
//                contentStream.newLineAtOffset(100, 700);
//                contentStream.showText("Hello, PDFBox!");
//                contentStream.endText();
//            }
//
//            document.save("HelloPDFBox.pdf");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //Ported over from Quarkus: https://github.com/quarkiverse/quarkus-pdfbox/
//    //Also needs runtime Init like below in gradle file
//    //But currently missing, would also need awt porting over: https://github.com/quarkusio/quarkus/blob/main/extensions/awt/deployment/src/main/java/io/quarkus/awt/deployment/AwtProcessor.java
//    static class PdfBoxRuntimeHints implements RuntimeHintsRegistrar {
//        @Override
//        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
//            // Register resource directories
//            hints.resources().registerPattern("org/apache/pdfbox/resources/afm/**");
//            hints.resources().registerPattern("org/apache/pdfbox/resources/glyphlist/**");
//            hints.resources().registerPattern("org/apache/fontbox/cmap/**");
//            hints.resources().registerPattern("org/apache/fontbox/unicode/**");
//
//            // Enable all charsets
//            hints.resources().registerPattern("META-INF/services/java.nio.charset.spi.CharsetProvider");
//        }
//    }
//}
//

/*
graalvmNative {
	binaries.named("main") {
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.pdmodel")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.GlyphCache")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.GroupGraphics")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.PDFRenderer")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.PageDrawer")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.PageDrawerParameters")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.RenderDestination")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.SoftMask")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.TilingPaint")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.rendering.TilingPaintFactory")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.pdmodel.font.PDType1Font")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.text.LegacyPDFStreamEngine")
		buildArgs.add("--initialize-at-run-time=org.apache.pdfbox.pdmodel.encryption.PublicKeySecurityHandler")
		buildArgs.add("--initialize-at-run-time=sun.java2d.Disposer")
	}
}
*/