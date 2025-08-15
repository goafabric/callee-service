package org.goafabric.calleeservice.extensions;

import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    public ExceptionHandler(@Value("${sentry.dsn}") String sentryDsn, @Value("${sentry.send-default-pii}") boolean sentryDefaultPii) {
        Sentry.init(options -> {options.setDsn(sentryDsn); options.setSendDefaultPii(sentryDefaultPii);});
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn(ex.getMessage(), ex);
        Sentry.captureException(ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        log.warn(ex.getMessage(), ex);
        Sentry.captureException(ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);
        Sentry.captureException(ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
