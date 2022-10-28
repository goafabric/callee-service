package org.goafabric.calleeservice.crossfunctional

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class ExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String?> {
        log.warn(ex.message, ex)
        return ResponseEntity(ex.message, HttpStatus.PRECONDITION_FAILED)
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException::class)
    fun handleIllegalArgumentException(ex: IllegalStateException): ResponseEntity<String?> {
        log.warn(ex.message, ex)
        return ResponseEntity(ex.message, HttpStatus.PRECONDITION_FAILED)
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<String?> {
        log.error(ex.message, ex)
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }
}