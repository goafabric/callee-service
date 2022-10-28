package org.goafabric.calleeservice.logic

import org.goafabric.calleeservice.service.Callee
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class CalleeLogic {
    private val sleepTime = AtomicLong(0L)

    fun sayMyName (name : String) : Callee {
        sleep();
        return Callee(message = "Your name is: $name")
    }

    fun sayMyOtherName (name : String) : Callee {
        sleep();
        return Callee(message = "Your name is: $name")
    }

    fun setSleepTime(sleepTime : Long) : Callee {
        this.sleepTime.set(sleepTime)
        return Callee(message = "set sleepTime to: $sleepTime")
    }

    private fun sleep() {
        Thread.sleep(sleepTime.get())
    }
}