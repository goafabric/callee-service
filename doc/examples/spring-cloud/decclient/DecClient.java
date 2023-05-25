package org.goafabric.calleeservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class DecClient implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CalleeServiceAdapterDec calleeServiceAdapter;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info(calleeServiceAdapter.sayMyName("hello from dec client").message());
    }
}
