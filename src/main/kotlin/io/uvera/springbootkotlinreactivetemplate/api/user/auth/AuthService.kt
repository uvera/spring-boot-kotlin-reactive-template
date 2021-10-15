package io.uvera.springbootkotlinreactivetemplate.api.user.auth

import io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto.*
import io.uvera.springbootkotlinreactivetemplate.common.model.User
import io.uvera.springbootkotlinreactivetemplate.common.model.UserRole
import io.uvera.springbootkotlinreactivetemplate.common.repository.UserRepository
import io.uvera.springbootkotlinreactivetemplate.common.security.service.JwtAccessService
import io.uvera.springbootkotlinreactivetemplate.common.security.service.JwtRefreshService
import io.uvera.springbootkotlinreactivetemplate.common.security.service.MongoUserDetailsService
import io.uvera.springbootkotlinreactivetemplate.common.security.util.principalDelegate
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    protected val reactiveAuthenticationManager: ReactiveAuthenticationManager,
    protected val jwtAccessService: JwtAccessService,
    protected val jwtRefreshService: JwtRefreshService,
    protected val userDetailsService: MongoUserDetailsService,
    protected val userRepository: UserRepository,
    protected val passwordEncoder: PasswordEncoder,
) {
    suspend fun authenticate(dto: AuthenticationRequestDTO): TokenResponseDTO? {
        reactiveAuthenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                dto.email, dto.password
            )
        ).awaitFirstOrNull() ?: throw BadCredentialsException("User not found")
        return generateToken(dto.email)
            .awaitFirstOrNull()
    }

    suspend fun generateToken(email: String): Mono<TokenResponseDTO> {
        // load userDetails from database
        return userDetailsService.findByUsername(email)
            .map { userDetails ->
                // generate access token
                val accessToken: String = jwtAccessService.generateToken(userDetails)
                // generate longer lasting refresh token
                val refreshToken: String = jwtRefreshService.generateToken(userDetails)
                TokenResponseDTO(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            }
    }

    suspend fun register(registrationDTO: RegistrationDTO): WhoAmIDTO? {
        if (userRepository.existsByEmail(registrationDTO.email).awaitFirst() == true)
            throw BadCredentialsException("User already exists")
        val user = User(
            id = "",
            password = passwordEncoder.encode(registrationDTO.password),
            email = registrationDTO.email,
            active = true,
        ).apply {
            roleSet.add(UserRole.USER)
        }
        return userRepository.save(user).awaitFirstOrNull()?.let {
            WhoAmIDTO.fromUser(it)
        }
    }

    suspend fun whoAmI(): WhoAmIDTO? {
        val principal by principalDelegate()
        return principal.flatMap {
            userRepository.findByEmail(it.email)
        }.map {
            WhoAmIDTO.fromUser(it)
        }.awaitFirstOrNull() ?: throw BadCredentialsException("User not found in database")
    }

    suspend fun generateTokensFromRefreshToken(refreshRequest: RefreshRequestDTO): TokenResponseDTO {
        val token = refreshRequest.token
        if (!jwtRefreshService.validateToken(token))
            throw BadCredentialsException("Invalid refresh token")

        val subject = jwtRefreshService.getClaimsFromToken(token)?.subject
        val userDetails = userDetailsService.findByUsername(subject).awaitFirstOrNull()
            ?: throw UsernameNotFoundException("User by username $subject not found")

        if (!userDetails.isEnabled)
            throw DisabledException("Account disabled")

        return TokenResponseDTO(
            accessToken = jwtAccessService.generateToken(userDetails),
            refreshToken = jwtRefreshService.generateToken(userDetails)
        )

    }
}
