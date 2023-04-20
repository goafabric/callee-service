package org.goafabric.calleeservice;

import org.goafabric.calleeservice.controller.Callee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@EnableFeignClients
public class MyFeignClient implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        sayMyName();
    }

    @FeignClient("callee-service")
    interface CalleeService {
        @GetMapping("sayMyName")
        Callee sayMyName(String name);
    }

    @Autowired
    private CalleeService calleeService;

    public void sayMyName() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("calling via eureka");
        calleeService.sayMyName("hello from feign");
    }


}
