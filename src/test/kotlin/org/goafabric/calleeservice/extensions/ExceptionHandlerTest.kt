package org.goafabric.calleeservice.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class ExceptionHandlerTest {
    private val exceptionHandler: ExceptionHandler = ExceptionHandler()

    @Test
    fun handleIllegalArgumentException() {
        assertThat(exceptionHandler.handleIllegalArgumentException(IllegalArgumentException("illegal argument")).statusCode)
            .isEqualTo(HttpStatus.PRECONDITION_FAILED)
    }

    @Test
    fun handleIllegalStateException() {
        assertThat(exceptionHandler.handleIllegalStateException(IllegalStateException("illegal state")).statusCode)
            .isEqualTo(HttpStatus.PRECONDITION_FAILED)
    }

    @Test
    fun handleGeneralException() {
        assertThat(exceptionHandler.handleGeneralException(IllegalStateException("general failure")).statusCode)
            .isEqualTo(HttpStatus.BAD_REQUEST)
    }
}