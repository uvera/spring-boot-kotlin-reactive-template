package io.uvera.springbootkotlinreactivetemplate.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.*
import javax.validation.constraints.NotBlank


@ConfigurationProperties(prefix = "app.jwt.token.access")
@ConstructorBinding
class JwtAccessTokenProperties(
    @field:NotBlank val secret: Base64Secret,
    @field:NotBlank val expirationInMinutes: Int,
)

@ConfigurationProperties(prefix = "app.jwt.token.refresh")
@ConstructorBinding
class JwtRefreshTokenProperties(
    @field:NotBlank val secret: Base64Secret,
    @field:NotBlank val expirationInMinutes: Int,
)

class Base64Secret private constructor(val value: String) {
    companion object {
        fun parse(value: String): Base64Secret {
            return Base64Secret(Base64.getEncoder().encodeToString(value.encodeToByteArray()))
        }
    }
}

@Component
@ConfigurationPropertiesBinding
class Base64SecretConverter : Converter<String, Base64Secret> {
    override fun convert(source: String): Base64Secret {
        return Base64Secret.parse(source)
    }

}
