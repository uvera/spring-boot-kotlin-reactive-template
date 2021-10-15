package io.uvera.springbootkotlinreactivetemplate.common.exception

import io.uvera.springbootkotlinreactivetemplate.common.security.util.extension.compact
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import java.time.LocalDateTime
import java.time.ZoneOffset


/**
 * Class representing error returned from this API.
 *
 * This class is used for serializing into json format representing error DTO.
 *
 * @property errors Collection representing multiple errors that occurred during request parsing.
 * @property firstError Property representing first error from [errors].
 * @property timestamp Property representing timestamp when error occurred.
 */
class ApiError(
    val timestamp: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
    val path: String,
    val status: Int,
    val error: String,
    val message: String,
    val errors: Collection<ObjectErrorCompact>,
    val firstError: ObjectErrorCompact? = errors.firstOrNull(),
) {
    /**
     * [Companion] object of [ApiError] holding helper methods.
     */
    companion object {

        /**
         * Convert a [BindingResult] to instance of [ApiError].
         *
         * @return ApiError instance.
         */
        fun fromBindException(exception: WebExchangeBindException, exchange: ServerWebExchange, status: HttpStatus) =
            ApiError(
                path = exchange.request.path.value(),
                status = status.value(),
                error = status.reasonPhrase,
                message = exception.localizedMessage,
                errors = exception.bindingResult.allErrors.compact,
            )

        fun fromException(exception: Exception, exchange: ServerWebExchange, status: HttpStatus) =
            ApiError(
                path = exchange.request.path.value(),
                status = status.value(),
                error = status.reasonPhrase,
                message = exception.localizedMessage,
                errors = listOf(
                    ObjectErrorCompact(
                        defaultMessage = exception.localizedMessage ?: "Unknown error",
                        code = exception::class.simpleName ?: "UnknownException"
                    )
                )
            )
    }
}
