package org.goafabric.calleeservice;

import org.goafabric.calleeservice.aspect.TestComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        var context = SpringApplication.run(Application.class, args);

        if (context.isActive()) {
            //context.getBean(ReflectionComponent.class).run();

            //caching stuff
            //doCachingStuff(context);
        }

        try { Thread.currentThread().join(1000);} catch (InterruptedException e) {}
    }

    private static void doCachingStuff(ConfigurableApplicationContext context) {
        System.err.println(context.getBean(TestComponent.class).callOnMe("0"));
        System.err.println(context.getBean(TestComponent.class).callOnMe("0")); //when @Cacheable annotated only gets called once

        System.err.println(context.getBean(TestComponent.class).getFoo("1")); 
        System.err.println(context.getBean(TestComponent.class).getBar("1")); //will class cast exception without KeyGenerator
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context) {
        return args -> {if ((args.length > 0) && ("-check-integrity".equals(args[0]))) {SpringApplication.exit(context, () -> 0);}};
    }


}
