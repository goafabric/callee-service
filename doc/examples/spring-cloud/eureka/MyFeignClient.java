package org.goafabric.calleeservice.feign;

import com.netflix.discovery.EurekaClient;
import feign.auth.BasicAuthRequestInterceptor;
import org.goafabric.calleeservice.controller.Callee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@EnableFeignClients
public class MyFeignClient implements ApplicationListener<ApplicationReadyEvent> {

    @FeignClient(value = "CALLEE-SERVICE", configuration = FeignClientConfiguration.class)
    interface CalleeService {
        @GetMapping("/callees/sayMyName")
        Callee sayMyName(@RequestParam("name") String name);
    }

    @Configuration
    public static class FeignClientConfiguration {
        @Bean
        public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
            return new BasicAuthRequestInterceptor("admin", "admin");
        }
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CalleeService calleeService;

    @Autowired
    private EurekaClient eurekaClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        sayMyName();
    }

    private void sayMyName() {
        do {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) { throw new RuntimeException(e); }
            log.info("sleeping for eureka client to become available ...");
        } while (eurekaClient.getApplication("CALLEE-SERVICE") == null);

        log.info("found eureka client: " + eurekaClient.getApplication("CALLEE-SERVICE").getInstances().get(0).getHostName());

        log.info(calleeService.sayMyName("hello from feign").message());
    }

}
