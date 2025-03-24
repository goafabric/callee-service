package org.goafabric.calleeservice.drools;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@Component
@ImportRuntimeHints(TaxiFareCalculatorService.DroolsRuntimeHints.class)
public class TaxiFareCalculatorService {
    @Autowired
    private KieContainer kieContainer;

    public Long calculateFare(TaxiRide taxiRide, Fare rideFare) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("rideFare", rideFare);
        kieSession.insert(taxiRide);
        kieSession.fireAllRules();
        kieSession.dispose();
        return rideFare.getRideFare();
    }

    @Bean
    public CommandLineRunner init2(TaxiFareCalculatorService taxiFareCalculatorService) {
        return args -> {
            Long totalCharge = taxiFareCalculatorService.calculateFare(
                    new TaxiRide(false, 5l), new Fare());

            if (totalCharge == null) {
                throw new IllegalStateException("drools failed");
            }
            System.err.println(totalCharge);
        };
    }

    static class DroolsRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("TAXI_FARE_RULE.drl");
        }
    }

}
