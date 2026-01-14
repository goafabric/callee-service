/*
package org.goafabric.calleeservice.epcqr;

import de.muehlencord.epcqr.EpcBuilder;
import de.muehlencord.epcqr.EpcException;
import de.muehlencord.epcqr.ImageFileGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class EpcQr {
    @PostConstruct
    public void init() throws EpcException {
        var builder = new EpcBuilder()
                .withRecipient("Max Mustermann")
                .withIban("GB33BUKB20201555555555")
                .withPaymentAmount(48.81D)
                .withPurposeText("Test");

        new ImageFileGenerator()
                .withOutputFile("temp.png")
                .generate(builder);

        //System.out.println(new Base64ImageGenerator().generate(builder););
    }
}

 */
