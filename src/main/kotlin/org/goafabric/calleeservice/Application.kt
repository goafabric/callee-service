package org.goafabric.calleeservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.client.RestClient

@SpringBootApplication
class Application(@Autowired private val context: ConfigurableApplicationContext):
    CommandLineRunner {
    override fun run(vararg args: String) {
        if (args.isNotEmpty() && "-check-integrity" == args[0]) {
            context.addApplicationListener(ApplicationListener { _: ApplicationReadyEvent? ->
                RestClient.create().get()
                    .uri("http://localhost:" + context.environment.getProperty("local.server.port") + "/v3/api-docs")
                    .retrieve().body(
                        String::class.java
                    )
                SpringApplication.exit(context!!, ExitCodeGenerator { 0 })
            })
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}