package org.goafabric.calleeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.ExitCodeGenerator
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import kotlin.jvm.JvmStatic

/**
 * Created by amautsch on 26.06.2015.
 */
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