package io.uvera.springbootkotlinreactivetemplate.common.security.filter

import io.uvera.springbootkotlinreactivetemplate.common.security.service.JwtAccessService
import io.uvera.springbootkotlinreactivetemplate.common.security.service.MongoUserDetailsService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.time.ExperimentalTime

@Component
class AuthenticationConverter(
    protected val userDetailsService: MongoUserDetailsService,
    protected val jwtAccessService: JwtAccessService,
) : (ServerWebExchange) -> Mono<Authentication> {
    override fun invoke(exchange: ServerWebExchange): Mono<Authentication> {
        return exchange.extractJwt()
            .flatMap { token ->
                if (!jwtAccessService.validateToken(token))
                    return@flatMap Mono.error(BadCredentialsException("Invalid token"))

                val claims = jwtAccessService.getClaimsFromToken(token)
                    ?: return@flatMap Mono.error(BadCredentialsException("Invalid token"))
                return@flatMap userDetailsService.findByUsername(claims.subject).flatMap { ud ->
                    if (!ud.isEnabled) Mono.error(DisabledException("Account disabled"))
                    else Mono.just(UsernamePasswordAuthenticationToken(
                        ud, null, ud.authorities
                    ))
                }
            }
    }
}

private fun ServerWebExchange.extractJwt(): Mono<String> {
    val header: String = this.request.headers.getFirst("Authorization") ?: return Mono.empty()
    return if (header.startsWith("Bearer ")) {
        Mono.just(header.drop(7))
    } else Mono.empty()
}
