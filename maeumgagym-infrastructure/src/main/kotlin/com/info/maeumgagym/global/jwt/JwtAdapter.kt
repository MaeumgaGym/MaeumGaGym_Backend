package com.info.maeumgagym.global.jwt

import com.info.maeumgagym.auth.dto.response.TokenResponse
import com.info.maeumgagym.auth.port.out.GenerateJwtPort
import com.info.maeumgagym.auth.port.out.ParsePublicKeyPort
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.domain.user.exception.UserNotFoundException
import com.info.maeumgagym.global.env.jwt.JwtProperties
import com.info.maeumgagym.global.exception.ExpiredTokenException
import com.info.maeumgagym.global.exception.InvalidTokenException
import com.info.maeumgagym.global.security.principle.CustomUserDetailService
import com.info.maeumgagym.global.security.principle.CustomUserDetails
import com.info.maeumgagym.user.model.User
import com.info.maeumgagym.user.port.out.FindUserByUUIDPort
import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.security.PublicKey
import java.util.*

@Component
class JwtAdapter(
    private val jwtProperties: JwtProperties,
    private val findUserByUUIDPort: FindUserByUUIDPort,
    private val customUserDetailService: CustomUserDetailService
) : GenerateJwtPort, ReadCurrentUserPort, ParsePublicKeyPort {
    override fun generateToken(subject: String): TokenResponse {
        return TokenResponse(
            generateAccessToken(subject),
            generateRefreshToken()
        )
    }

    private fun generateAccessToken(subject: String): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(subject.toString())
            .setIssuedAt(now)
            .setExpiration(Date(now.time + jwtProperties.accessExpiredExp * 1000L))
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    private fun generateRefreshToken(): String {
        val now = Date()
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date(now.time + jwtProperties.refreshExpiredExp * 1000L))
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    private fun getBody(token: String): Claims {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token).body
        } catch (e: JwtException) {
            throw InvalidTokenException
        }
    }

    override fun readCurrentUser(): User {
        val userId = SecurityContextHolder.getContext().authentication.name
        return findUserByUUIDPort.findUserById(UUID.fromString(userId)) ?: throw UserNotFoundException
    }

    fun getAuthentication(token: String): Authentication {
        val subject = getBody(token).subject

        val authDetails = customUserDetailService.loadUserByUsername(subject) as CustomUserDetails

        return UsernamePasswordAuthenticationToken(authDetails, null, authDetails.authorities)
    }

    override fun parseClaimsFromPublicKey(token: String, publicKey: PublicKey): Claims {
        return try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).body
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                else -> throw InvalidTokenException
            }
        }
    }
}
