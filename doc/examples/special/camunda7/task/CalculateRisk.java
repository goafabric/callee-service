package org.goafabric.calleeservice.task;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalculateRisk implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(CalculateRisk.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("calculating risk");
    }

}
