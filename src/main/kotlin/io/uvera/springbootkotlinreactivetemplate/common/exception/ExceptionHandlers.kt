package io.uvera.springbootkotlinreactivetemplate.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange

private fun exceptionEntity(
    ex: Exception,
    exchange: ServerWebExchange,
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
) =
    ResponseEntity<ApiError>(ApiError.fromException(ex, exchange, status), status)

@RestControllerAdvice
class ExceptionHandlers {
    @ExceptionHandler(WebExchangeBindException::class)
    fun bindException(exception: WebExchangeBindException, exchange: ServerWebExchange) =
        ResponseEntity<ApiError>(ApiError.fromBindException(exception, exchange, HttpStatus.BAD_REQUEST),
            HttpStatus.BAD_REQUEST)

    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(ex: RuntimeException, exchange: ServerWebExchange) =
        exceptionEntity(ex, exchange)

    @ExceptionHandler(AuthenticationException::class)
    fun authenticationException(ex: RuntimeException, exchange: ServerWebExchange) =
        exceptionEntity(ex, exchange, HttpStatus.UNAUTHORIZED)

}
