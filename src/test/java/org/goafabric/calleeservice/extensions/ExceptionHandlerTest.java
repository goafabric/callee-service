package org.goafabric.calleeservice.extensions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Test
    void handleIllegalArgumentException() {
        assertThat(exceptionHandler.handleIllegalArgumentException(new IllegalArgumentException("illegal argument")).getStatusCode())
                .isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    void handleIllegalStateException() {
        assertThat(exceptionHandler.handleIllegalStateException(new IllegalStateException("illegal state")).getStatusCode())
                .isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    void handleGeneralException() {
        assertThat(exceptionHandler.handleGeneralException(new IllegalStateException("general failure")).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}