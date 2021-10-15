package io.uvera.springbootkotlinreactivetemplate.common.security.configuration

import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
@Primary
class CustomReactiveAuthenticationManager(
    private val userDetailsService: ReactiveUserDetailsService,
) :
    AbstractUserDetailsReactiveAuthenticationManager() {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val username = authentication.name
        return retrieveUser(username)
            .doOnNext { defaultPreAuthenticationChecks(it) }
            .switchIfEmpty(Mono.defer { Mono.error(BadCredentialsException("Invalid Credentials")) })
            .doOnNext { defaultPostAuthenticationChecks(it) }
            .map { createUsernamePasswordAuthenticationToken(it) }
    }

    override fun retrieveUser(username: String?): Mono<UserDetails> = userDetailsService.findByUsername(username)

    private fun defaultPreAuthenticationChecks(user: UserDetails) {
        if (!user.isAccountNonLocked) {
            logger.debug("User account is locked")
            throw LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                "User account is locked"))
        }
        if (!user.isEnabled) {
            logger.debug("User account is disabled")
            throw DisabledException(
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"))
        }
        if (!user.isAccountNonExpired) {
            logger.debug("User account is expired")
            throw AccountExpiredException(messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"))
        }
    }

    private fun defaultPostAuthenticationChecks(user: UserDetails) {
        if (!user.isCredentialsNonExpired) {
            logger.debug("User account credentials have expired")
            throw CredentialsExpiredException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"))
        }
    }

    private fun createUsernamePasswordAuthenticationToken(userDetails: UserDetails): UsernamePasswordAuthenticationToken? {
        return UsernamePasswordAuthenticationToken(userDetails, userDetails.password,
            userDetails.authorities)
    }
}
