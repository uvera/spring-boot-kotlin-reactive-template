package io.uvera.springbootkotlinreactivetemplate.common.security.configuration

import io.uvera.springbootkotlinreactivetemplate.common.model.User
import io.uvera.springbootkotlinreactivetemplate.common.model.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MongoUserDetails(user: User) : UserDetails {
    val email = user.email
    private val password: String = user.password
    private val active = user.active
    private val authorities: MutableList<GrantedAuthority> = user.roleSet
        .map { role -> SimpleGrantedAuthority("${UserRole.ROLE_PREFIX_VALUE}$role") }
        .toMutableList()

    override fun getUsername(): String = this.email

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = this.authorities

    override fun getPassword(): String = this.password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = active
}
