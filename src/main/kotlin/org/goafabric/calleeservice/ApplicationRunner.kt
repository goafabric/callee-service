package org.goafabric.calleeservice

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationPredicate
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationRunner {
    @Bean
    fun runner(context: ApplicationContext?): CommandLineRunner? {
        return CommandLineRunner { args: Array<String> ->
            if (args.isNotEmpty() && "-check-integrity" == args[0]) {
                SpringApplication.exit(context, ExitCodeGenerator { 0 })
            }
        }
    }
}