package io.uvera.springbootkotlinreactivetemplate.common.security.service

import io.jsonwebtoken.*
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
     * Generates token from [userDetails] using [secret] [String]
     *
     * @param userDetails instance of [UserDetails] implementation
     * @param expirationInMinutes expiration in minutes as [Int]
     * @param secret secret used for signing the JWT
     *
     * @return generated token [String]
     */
    fun generateToken(userDetails: UserDetails, expirationInMinutes: Int, secret: String): String {
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
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    /**
     * Validates [token] against [secret]
     * @return [Boolean] indicating if validation passed
     */
    fun validateToken(token: String, secret: String): Boolean = try {
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
        true
    } catch (ex: SignatureException) {
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
    fun getClaimsFromToken(token: String, secret: String): Claims? = try {
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    } catch (ex: Exception) {
        null
    }
}
