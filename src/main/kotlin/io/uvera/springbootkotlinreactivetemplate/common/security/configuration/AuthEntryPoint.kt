package io.uvera.springbootkotlinreactivetemplate.common.security.configuration

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class AuthEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> = Mono.error(ex)
}
