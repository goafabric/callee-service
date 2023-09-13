package org.goafabric.calleeservice;

import org.goafabric.calleeservice.aspect.AspectComponent;
import org.goafabric.calleeservice.reflection.ReflectionComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        var context = SpringApplication.run(Application.class, args);

        if (context.isActive()) {
            context.getBean(ReflectionComponent.class).run();
            context.getBean(AspectComponent.class).run(context);
        }

        try { Thread.currentThread().join(1000);} catch (InterruptedException e) {}
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context) {
        return args -> {if ((args.length > 0) && ("-check-integrity".equals(args[0]))) {SpringApplication.exit(context, () -> 0);}};
    }


}
