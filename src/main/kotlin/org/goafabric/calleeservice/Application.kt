package org.goafabric.calleeservice

import com.fasterxml.jackson.databind.BeanDescription
import io.swagger.v3.core.jackson.mixin.Schema31Mixin
import io.swagger.v3.oas.models.media.JsonSchema
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS
import org.springframework.aot.hint.annotation.RegisterReflection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
@RegisterReflection(
    classes = [Schema31Mixin.TypeSerializer::class, JsonSchema::class, BeanDescription::class],
    memberCategories = [MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, INVOKE_PUBLIC_METHODS]
)
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