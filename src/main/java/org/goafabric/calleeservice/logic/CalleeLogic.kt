package org.goafabric.calleeservice.logic

import org.goafabric.calleeservice.controller.Callee
import org.springframework.stereotype.Component

@Component
class CalleeLogic {
    fun sayMyName (name : String) : Callee {
        return Callee(message = "Your name is: $name")
    }

    fun sayMyOtherName (name : String) : Callee {
        return Callee(message = "Your name is: $name")
    }

}