package org.goafabric.calleeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
        System.err.println("Hello from Spring Boot");

        try {
            final Class clazz = Class.forName("org.goafabric.calleeservice.Callee");
            System.err.println(clazz.getMethod("getMessage").invoke(
                    clazz.getDeclaredConstructor().newInstance()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try { Thread.currentThread().join(10000);} catch (InterruptedException e) {}
    }

}
