//implementation("io.dapr.spring:dapr-spring-boot-starter:0.15.0")
//testImplementation("io.dapr.spring:dapr-spring-boot-starter-test:0.15.0")

package org.goafabric.calleeservice.controller;

import io.dapr.Topic;
import io.dapr.client.DaprClient;
import io.dapr.client.domain.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyDaprController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());


    private final DaprClient daprClient;

    public MyDaprController(DaprClient daprClient) {
        this.daprClient = daprClient;

        //saveState();
        //publish();
    }

    @RequestMapping("/dapr/save")
    public void save() {
        saveState();
        publish();
    }

    private void saveState() {
        String key = "mykey";
        daprClient.saveState("mystore", key, "hello world").block();
        log.info("dapr state: {}", daprClient.getState("mystore", key, String.class).block().getValue());
        daprClient.deleteState("mystore", key);
    }

    private void publish() {
        daprClient.publishEvent("pubsub", "mytopic", "myevent").block();
    }

    //dapr needs to be restarted everytime before we try to, du to waiting for connection
    @PostMapping("subscribe")
    @Topic(pubsubName = "pubsub", name = "mytopic")
    public void subscribe(@RequestBody CloudEvent<String> cloudEvent){
        log.info("dapr subscripe: {}" , cloudEvent.getData());
    }

}
