package org.goafabric.calleeservice.controller

import org.goafabric.calleeservice.logic.CalleeLogic
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["callees"], produces = [APPLICATION_JSON_VALUE])
class CalleeController (
    private val calleeLogic : CalleeLogic) {

    @GetMapping("sayMyName")
    fun sayMyName (@RequestParam("name") name : String) : Callee {
        return calleeLogic.sayMyName(name)
    }

    @GetMapping("sayMyOtherName/{name}")
    fun sayMyOtherName (@PathVariable("name") name : String) : Callee {
        return calleeLogic.sayMyOtherName(name)
    }

}