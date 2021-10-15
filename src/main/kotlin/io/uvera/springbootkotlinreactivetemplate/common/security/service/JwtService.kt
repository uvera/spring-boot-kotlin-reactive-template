package io.uvera.springbootkotlinreactivetemplate.common.security.service

import io.jsonwebtoken.*
import io.uvera.springbootkotlinreactivetemplate.common.properties.Base64Secret
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

interface JwtService {
    fun generateToken(userDetails: UserDetails): String
    fun validateToken(token: String): Boolean
    fun getClaimsFromToken(token: String): Claims?
}

/**
 * Generic implementation for JWT handling
 */
@Service
class GenericTokenService {
    /**
     * Generates token from [userDetails] using [base64Secret] [String]
     *
     * @param userDetails instance of [UserDetails] implementation
     * @param expirationInMinutes expiration in minutes as [Int]
     * @param base64Secret used for signing the JWT
     *
     * @return generated token [String]
     */
    fun generateToken(userDetails: UserDetails, expirationInMinutes: Int, base64Secret: Base64Secret): String {
        val subject = userDetails.username
        val claims = mutableMapOf<String, Any>()
        val issuedAt = Date(System.currentTimeMillis())
        val expiration = Calendar
            .getInstance()
            .also { calendar ->
                calendar.add(Calendar.MINUTE, expirationInMinutes)
            }
            .toInstant()
            .toEpochMilli()
            .let { millis ->
                Date(millis)
            }

        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, base64Secret.value)
            .compact()
    }

    /**
     * Validates [token] against [base64Secret]
     * @return [Boolean] indicating if validation passed
     */
    fun validateToken(token: String, base64Secret: Base64Secret): Boolean = try {
        Jwts.parserBuilder()
            .setSigningKey(base64Secret.value)
            .build()
            .parseClaimsJws(token)
        true
    } catch (ex: SecurityException) {
        false
    } catch (ex: MalformedJwtException) {
        false
    } catch (ex: UnsupportedJwtException) {
        false
    } catch (ex: IllegalArgumentException) {
        false
    } catch (ex: ExpiredJwtException) {
        false
    }

    /**
     * Parses claims from tokens
     * @return nullable instance of [Claims]
     */
    fun getClaimsFromToken(token: String, base64Secret: Base64Secret): Claims? = try {
        Jwts.parserBuilder()
            .setSigningKey(base64Secret.value)
            .build()
            .parseClaimsJws(token)
            .body
    } catch (ex: Exception) {
        null
    }
}
