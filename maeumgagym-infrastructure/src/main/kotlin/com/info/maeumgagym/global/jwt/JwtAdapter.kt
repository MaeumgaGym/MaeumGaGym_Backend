package com.info.maeumgagym.global.jwt

import com.info.common.WebAdapter
import com.info.maeumgagym.auth.dto.response.TokenResponse
import com.info.maeumgagym.auth.port.out.GenerateJwtPort
import com.info.maeumgagym.auth.port.out.GetJwtBodyPort
import com.info.maeumgagym.auth.port.out.ReissuePort
import com.info.maeumgagym.global.env.jwt.JwtProperties
import com.info.maeumgagym.global.exception.ExpiredTokenException
import com.info.maeumgagym.global.exception.InvalidTokenException
import com.info.maeumgagym.global.security.principle.CustomUserDetailService
import com.info.maeumgagym.global.security.principle.CustomUserDetails
import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.security.PublicKey
import java.util.*

@WebAdapter
class JwtAdapter(
    private val jwtProperties: JwtProperties,
    private val customUserDetailService: CustomUserDetailService
) : GenerateJwtPort, ReissuePort, GetJwtBodyPort {
    override fun generateToken(subject: String): TokenResponse {
        return TokenResponse(
            generateAccessToken(subject),
            generateRefreshToken()
        )
    }

    private fun generateAccessToken(subject: String): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(subject)
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

    override fun reissue(refreshToken: String): TokenResponse =
        generateToken(getJwtBody(refreshToken).subject)

    fun getAuthentication(token: String): Authentication {
        val subject = getJwtBody(token).subject

        val authDetails = customUserDetailService.loadUserByUsername(subject) as CustomUserDetails

        return UsernamePasswordAuthenticationToken(authDetails, null, authDetails.authorities)
    }

    override fun getJwtBody(token: String, publicKey: PublicKey): Claims =
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).body
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                else -> throw InvalidTokenException
            }
        }

    override fun getJwtBody(token: String, key: String) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).body
        } catch (e: JwtException) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                else -> throw InvalidTokenException
            }
        }
    }

    override fun getJwtBody(token: String): Claims =
        try {
            Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token).body
        } catch (e: JwtException) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                else -> throw InvalidTokenException
            }
        }
}
