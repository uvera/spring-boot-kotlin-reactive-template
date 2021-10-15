package io.uvera.springbootkotlinreactivetemplate.common.security.service

import io.uvera.springbootkotlinreactivetemplate.common.repository.UserRepository
import io.uvera.springbootkotlinreactivetemplate.common.security.configuration.MongoUserDetails
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class MongoUserDetailsService(
    protected val userRepository: UserRepository,
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return if (username == null)
            Mono.error(UsernameNotFoundException("Null username"))
        else userRepository.findByEmail(username)
            .switchIfEmpty { Mono.error(UsernameNotFoundException("User by username: $username not found")) }
            .map { user ->
                MongoUserDetails(user)
            }
    }
}
