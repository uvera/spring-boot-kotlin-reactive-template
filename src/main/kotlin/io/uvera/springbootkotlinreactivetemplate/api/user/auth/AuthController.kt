package io.uvera.springbootkotlinreactivetemplate.api.user.auth

import io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto.AuthenticationRequestDTO
import io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto.RefreshRequestDTO
import io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto.RegistrationDTO
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    protected val service: AuthService,
) {

    @PostMapping("/login")
    suspend fun login(
        @Validated @RequestBody authenticationRequest: AuthenticationRequestDTO,
    ) = service.authenticate(authenticationRequest)

    @PostMapping("/register")
    suspend fun register(
        @Validated @RequestBody registrationDTO: RegistrationDTO,
    ) = service.register(registrationDTO)

    @PreAuthorize("authenticated")
    @GetMapping("/who-am-i")
    suspend fun whoAmI() = service.whoAmI()

    @PostMapping("/refresh")
    suspend fun refreshToken(
        @Validated @RequestBody refreshRequest: RefreshRequestDTO,
    ) = service.generateTokensFromRefreshToken(refreshRequest)
}
