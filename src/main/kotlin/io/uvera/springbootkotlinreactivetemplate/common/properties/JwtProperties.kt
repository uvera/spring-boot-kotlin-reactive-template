package io.uvera.springbootkotlinreactivetemplate.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.validation.constraints.NotBlank


@ConfigurationProperties(prefix = "app.jwt.token.access")
@ConstructorBinding
class JwtAccessTokenProperties(
    @field:NotBlank val secret: String,
    @field:NotBlank val expirationInMinutes: Int,
)

@ConfigurationProperties(prefix = "app.jwt.token.refresh")
@ConstructorBinding
class JwtRefreshTokenProperties(
    @field:NotBlank val secret: String,
    @field:NotBlank val expirationInMinutes: Int,
)
