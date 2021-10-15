package io.uvera.springbootkotlinreactivetemplate.api.user.auth.dto

import io.uvera.springbootkotlinreactivetemplate.common.model.User
import io.uvera.springbootkotlinreactivetemplate.common.model.UserRole

class WhoAmIDTO(val email: String, val roles: List<String>) {
    companion object {
        fun fromUser(user: User) = WhoAmIDTO(
            email = user.email,
            roles = user.roleSet.map(UserRole::toString)
        )
    }
}
