package org.goafabric.calleeservice.logic

import org.goafabric.calleeservice.controller.dto.Callee
import org.springframework.stereotype.Component

@Component
class CalleeLogic {
    fun sayMyName (name : String) : Callee {
        return Callee(id = "0", message = "Your name is: $name")
    }

    fun sayMyOtherName (name : String) : Callee {
        return Callee(id = "0", message = "Your other name is: $name")
    }


    fun save(callee: Callee): Callee? {
        return Callee(id = "0", message = "Storing your message: $callee.message")
    }
}