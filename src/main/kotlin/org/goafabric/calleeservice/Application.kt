/*
package org.goafabric.calleeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.ExitCodeGenerator
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import kotlin.jvm.JvmStatic

@SpringBootApplication
class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

    @Bean
    fun init(context: ApplicationContext?): CommandLineRunner {
        return CommandLineRunner { args: Array<String> ->
            if (args.size > 0 && "-check-integrity" == args[0]) {
                SpringApplication.exit(context, ExitCodeGenerator { 0 })
            }
        }
    }

}

 */