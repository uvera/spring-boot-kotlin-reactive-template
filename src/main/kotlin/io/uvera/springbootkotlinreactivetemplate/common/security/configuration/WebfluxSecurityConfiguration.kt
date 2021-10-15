package io.uvera.springbootkotlinreactivetemplate.common.security.configuration

import io.uvera.springbootkotlinreactivetemplate.common.security.filter.AuthenticationConverter
import io.uvera.springbootkotlinreactivetemplate.common.security.util.extension.configurationSource
import io.uvera.springbootkotlinreactivetemplate.common.security.util.extension.corsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebfluxSecurityConfiguration(
    protected val authConverter: AuthenticationConverter,
    protected val authEntryPoint: AuthEntryPoint,
    protected val userDetailsService: ReactiveUserDetailsService,
) {


    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        cors {
            configurationSource {
                corsConfiguration {
                    allowedOrigins = listOf("*")
                    allowedHeaders = listOf("*")
                    allowedMethods = listOf(
                        HttpMethod.GET,
                        HttpMethod.HEAD,
                        HttpMethod.POST,
                        HttpMethod.DELETE,
                        HttpMethod.PUT,
                        HttpMethod.OPTIONS
                    ).map(HttpMethod::toString)
                }
            }
        }
        csrf { disable() }
        httpBasic { disable() }
        logout { disable() }
        authorizeExchange {
            authorize("/api/auth/**", permitAll)
            authorize(anyExchange, authenticated)
        }
        addFilterAt(authWebFilter(), AUTHENTICATION)
        exceptionHandling {
            authenticationEntryPoint = authEntryPoint
        }
    }

    @Bean
    fun authWebFilter(): AuthenticationWebFilter {
        return AuthenticationWebFilter(authManager()).apply {
            setServerAuthenticationConverter(authConverter)
        }
    }


    @Bean
    fun authManager(): ReactiveAuthenticationManager = CustomReactiveAuthenticationManager(
        userDetailsService,
    )

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
