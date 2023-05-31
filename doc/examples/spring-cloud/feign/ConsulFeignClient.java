package org.goafabric.calleeservice.feign;

import org.goafabric.calleeservice.controller.Callee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@EnableFeignClients
public class ConsulFeignClient implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  CalleeClient calleeClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info(calleeClient.sayMyName("hello from fegin & consul").message());
    }

    @FeignClient(name = "callee-service", configuration = FeignConfiguration.class) //, url = "http://localhost:50900")
    public interface CalleeClient {
        @GetMapping("callees/sayMyName") Callee sayMyName (@RequestParam("name") String name);
    }

}


