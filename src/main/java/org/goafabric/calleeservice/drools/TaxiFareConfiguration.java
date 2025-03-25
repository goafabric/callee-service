package org.goafabric.calleeservice.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaxiFareConfiguration {
    private static final String drlFile = "TAXI_FARE_RULE.drl";

    @Bean
    public KieContainer kieContainer() {
        var kieServices = KieServices.Factory.get();

        var kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
        var kieBuilder = kieServices.newKieBuilder(kieFileSystem);

        kieBuilder.buildAll();
        var kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}
