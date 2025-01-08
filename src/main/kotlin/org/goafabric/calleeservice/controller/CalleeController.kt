package org.goafabric.calleeservice.controller

import org.goafabric.calleeservice.controller.dto.Callee
import org.goafabric.calleeservice.logic.CalleeLogic
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

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

    @PostMapping(value = ["save"], consumes = [APPLICATION_JSON_VALUE])
    fun save(@RequestBody callee: Callee): Callee? {
        return calleeLogic.save(callee)
    }

}