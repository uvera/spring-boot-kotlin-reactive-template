package io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto

import javax.validation.constraints.NotBlank

class RefreshRequestDTO(
    @field:NotBlank(message = "(Refresh) token field cannot be blank")
    val token: String
)
