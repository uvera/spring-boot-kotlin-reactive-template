package io.uvera.springbootkotlinreactivetemplate.common.security.util

import io.uvera.springbootkotlinreactivetemplate.common.security.configuration.MongoUserDetails
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


private class PrincipalDelegate : ReadOnlyProperty<Nothing?, Mono<MongoUserDetails>> {
    private val instance: Mono<MongoUserDetails> = Mono.defer {
        ReactiveSecurityContextHolder.getContext().flatMap {
            val details = it.authentication.principal as? MongoUserDetails
            details?.let { ud ->
                Mono.just(ud)
            } ?: Mono.error(PrincipalDelegateException("Could not delegate user's principal"))
        }

    }

    override fun getValue(thisRef: Nothing?, property: KProperty<*>): Mono<MongoUserDetails> = instance

    private class PrincipalDelegateException(message: String?) : AuthenticationException(message)

}

fun principalDelegate(): ReadOnlyProperty<Nothing?, Mono<MongoUserDetails>> = PrincipalDelegate()
