package org.goafabric.calleeservice

import org.goafabric.calleeservice.controller.CalleeController
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Bean
fun weatherTools(calleeController: CalleeController): ToolCallbackProvider {
    return MethodToolCallbackProvider.builder().toolObjects(calleeController).build()
}