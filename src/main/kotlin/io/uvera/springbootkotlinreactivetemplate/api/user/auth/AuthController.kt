package io.uvera.springbootkotlinreactivetemplate.api.user.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

//region SwaggerDoc
@Tag(description = "Endpoints for authenticating users.", name = "auth")
//endregion
@RestController
@RequestMapping("/api/auth")
class AuthController(
    protected val service: AuthService,
) {

    //region SwaggerDoc
    @Operation(
        summary = "Authenticate",
        description = "Authenticated by email and password",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful authentication",
            content = *[(Content(schema = Schema(implementation = TokenResponseDTO::class)))]
        ), ApiResponse(
            responseCode = "400", description = "Invalid DTO"
        ), ApiResponse(
            responseCode = "401",
            description = "Auth error",
        )
    )
    //endregion
    @PostMapping("/login")
    suspend fun login(
        @Validated @RequestBody authenticationRequest: AuthenticationRequestDTO,
    ) = service.authenticate(authenticationRequest)

    //region SwaggerDoc
    @Operation(
        summary = "Register account",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Registration successful",
            content = *[(Content(schema = Schema(implementation = WhoAmIDTO::class)))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad credentials / user already exists",
        )
    )
    //endregion
    @PostMapping("/register")
    suspend fun register(
        @Validated @RequestBody registrationDTO: RegistrationDTO,
    ) = service.register(registrationDTO)

    //region SwaggerDoc
    @Operation(
        summary = "Get your own info",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "DTO Returned",
            content = *[(Content(schema = Schema(implementation = WhoAmIDTO::class)))]
        ), ApiResponse(
            responseCode = "404",
            description = "User not found",
        )
    )
    //endregion
    @PreAuthorize("authenticated")
    @GetMapping("/who-am-i")
    suspend fun whoAmI() = service.whoAmI()


    //region SwaggerDoc
    @Operation(
        summary = "Refresh the tokens",
        description = "Refresh both access and the refresh token with older refresh token",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful refresh",
            content = *[Content(schema = Schema(implementation = TokenResponseDTO::class))],
        ), ApiResponse(
            responseCode = "400", description = "Invalid DTO"
        ), ApiResponse(
            responseCode = "401",
            description = "Invalid token or account disabled",
        )
    )
    //endregion
    @PostMapping("/refresh")
    suspend fun refreshToken(
        @Validated @RequestBody refreshRequest: RefreshRequestDTO,
    ) = service.generateTokensFromRefreshToken(refreshRequest)
}
