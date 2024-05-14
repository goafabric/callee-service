package org.goafabric.calleeservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class Application(@Autowired private val applicationContext: ApplicationContext):
    CommandLineRunner {
    override fun run(vararg args: String?) {
        if (args.isNotEmpty() && "-check-integrity" == args[0]) {
            SpringApplication.exit(applicationContext, ExitCodeGenerator { 0 })
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}