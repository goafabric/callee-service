//package org.goafabric.calleeservice.pdf;
//
//import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//
////implementation("com.github.librepdf:openpdf:2.0.3")
//@RestController
//public class Html2PdfMulti {
//
//    @PostMapping(
//            value = "/pdf/upload",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_PDF_VALUE
//    )
//    public byte[] convert(@RequestParam("files") List<MultipartFile> files) {
//        try {
//            // Create temp dir
//            Path tempDir = Files.createTempDirectory("pdf-input");
//
//            File htmlFile = null;
//
//            // Write all uploaded files into the temp dir
//            for (MultipartFile mf : files) {
//                Path target = tempDir.resolve(mf.getOriginalFilename());
//                Files.write(target, mf.getBytes());
//
//                if (htmlFile == null && mf.getOriginalFilename() != null &&
//                        (mf.getOriginalFilename().endsWith(".html") || mf.getOriginalFilename().endsWith(".htm"))) {
//                    htmlFile = target.toFile();
//                }
//            }
//
//            if (htmlFile == null) {
//                throw new IllegalArgumentException("No HTML file provided in upload");
//            }
//
//            byte[] output = createPdf(htmlFile, tempDir.toUri().toString());
//            try (FileOutputStream os = new FileOutputStream("output2.pdf")) { os.write(output); }
//
//            return output;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private byte[] createPdf(File htmlFile, String baseUri) {
//        try {
//            String html = sanitize(Files.readString(htmlFile.toPath(), StandardCharsets.UTF_8));
//            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//                var builder = new PdfRendererBuilder();
//
//                // baseUri points to temp dir so relative <img src="foo.png"> works
//                builder.withHtmlContent(html, baseUri);
//                builder.toStream(os);
//                builder.run();
//
//                return os.toByteArray();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String sanitize(String html) {
//        return html
//                .replaceAll("<br>", "<br />")
//                .replaceAll("&nbsp;", "")
//                // strip any folder prefixes in src attributes, keep only the file name
//                .replaceAll("(?i)(src=[\"'])(?:.*/)?([^\"']+)([\"'])", "$1$2$3");
//    }
//
//
//}
