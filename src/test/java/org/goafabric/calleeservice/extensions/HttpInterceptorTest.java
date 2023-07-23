package org.goafabric.calleeservice.extensions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class HttpInterceptorTest {
    private HttpInterceptor httpInterceptor = new HttpInterceptor();

    @Test
    void preHandle() {
        assertThat(httpInterceptor.preHandle(
                Mockito.mock(HttpServletRequest.class)
                , Mockito.mock(HttpServletResponse.class), new Object()))
                .isTrue();
    }

    @Test
    void getTenantId() {
        assertThat(HttpInterceptor.getTenantId()).isEqualTo("0");
    }

    @Test
    void getUserName() {
        assertThat(HttpInterceptor.getUserName()).isEqualTo("");
    }
}