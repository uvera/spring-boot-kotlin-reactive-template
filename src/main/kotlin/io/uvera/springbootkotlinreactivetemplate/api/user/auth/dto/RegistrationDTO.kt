package io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class RegistrationDTO(
    @field:NotBlank(message = "E-Mail field cannot be blank")
    @field:Email
    val email: String,

    @field:NotBlank(message = "Password field cannot be blank")
    val password: String,
)
