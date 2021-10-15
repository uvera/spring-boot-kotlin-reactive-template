package io.uvera.springbootkotlinreactivetemplate.common.security.util.extension

import org.springframework.security.config.web.server.ServerCorsDsl
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.server.ServerWebExchange

inline fun ServerCorsDsl.configurationSource(crossinline block: (ServerWebExchange) -> CorsConfiguration?) {
    this.configurationSource = CorsConfigurationSource {
        block(it)
    }
}

inline fun corsConfiguration(block: CorsConfiguration.() -> Unit) =
    CorsConfiguration().apply {
        block()
    }
