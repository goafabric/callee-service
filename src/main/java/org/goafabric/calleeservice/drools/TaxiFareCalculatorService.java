package org.goafabric.calleeservice.drools;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
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
                    new TaxiRide(false, 70l), new Fare());

            System.out.println(totalCharge);

        };
    }

}
