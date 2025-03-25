package org.goafabric.calleeservice.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@Component
@ImportRuntimeHints(TaxiFareCalculatorService.DroolsRuntimeHints.class)
public class TaxiFareCalculatorService {

    public Long calculateFare(TaxiRide taxiRide, Fare rideFare) {
        var kieSession = kieContainer().newKieSession();
        kieSession.setGlobal("rideFare", rideFare);
        kieSession.insert(taxiRide);
        kieSession.fireAllRules();
        kieSession.dispose();
        return rideFare.getRideFare();
    }

    @Bean
    public KieContainer kieContainer() {
        var kieServices = KieServices.Factory.get();
        var kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("TAXI_FARE_RULE.drl"));
        var kieBuilder = kieServices.newKieBuilder(kieFileSystem);

        kieBuilder.buildAll();
        var kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    static class DroolsRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("TAXI_FARE_RULE.drl");
        }
    }

    @Bean
    public CommandLineRunner droolsInit(TaxiFareCalculatorService taxiFareCalculatorService) {
        return args -> {
            System.err.println(taxiFareCalculatorService.calculateFare(
                    new TaxiRide(false, 5l), new Fare()));
        };
    }

}
