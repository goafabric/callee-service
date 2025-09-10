package org.goafabric.calleeservice.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//implementation("com.github.librepdf:openpdf:2.0.3")
@RestController
public class Html2Pdf {

    @PostMapping(value = "/pdf/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] convert(@RequestParam("file") MultipartFile file)  {
        try {
            byte[] output = createPdf(file.getInputStream());
            try (FileOutputStream os = new FileOutputStream("output2.pdf")) { os.write(output); }
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    @PostConstruct
    public void init() {
        try {
            File file = new File("./html/text-editor-test.html");
            try (FileOutputStream os = new FileOutputStream("output.pdf")) {
                os.write(createPdf(new FileInputStream(file)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

     */

    private byte[] createPdf(InputStream input) {
        try {
            String html = sanitize(new String(input.readAllBytes()));
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                var builder = new PdfRendererBuilder();

                builder.withHtmlContent(html, ""); //, htmlFile.getParentFile().toURI().toString());

                builder.toStream(os);
                builder.run();
                return os.toByteArray();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String sanitize(String html) {
        return html.replaceAll("<br>", "<br />").replaceAll("&nbsp;", "");
    }

}
