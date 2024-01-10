package com.info.maeumgagym.global.jwt

import com.info.maeumgagym.auth.dto.response.TokenResponse
import com.info.maeumgagym.auth.port.out.GenerateJwtPort
import com.info.maeumgagym.auth.port.out.GetJwtBodyPort
import com.info.maeumgagym.auth.port.out.ReissuePort
import com.info.maeumgagym.global.jwt.entity.AccessTokenRedisEntity
import com.info.maeumgagym.global.jwt.entity.RefreshTokenRedisEntity
import com.info.maeumgagym.global.jwt.repository.AccessTokenRepository
import com.info.maeumgagym.global.jwt.repository.RefreshTokenRepository
import com.info.maeumgagym.global.env.jwt.JwtProperties
import com.info.maeumgagym.global.exception.ExpiredTokenException
import com.info.maeumgagym.global.exception.InvalidTokenException
import com.info.maeumgagym.global.security.principle.CustomUserDetailService
import com.info.maeumgagym.global.security.principle.CustomUserDetails
import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.security.PublicKey
import java.util.*

@Component
class JwtAdapter(
    private val jwtProperties: JwtProperties,
    private val customUserDetailService: CustomUserDetailService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val accessTokenRepository: AccessTokenRepository
) : GenerateJwtPort, ReissuePort, GetJwtBodyPort {

    // 모든 토큰 발급
    @Transactional
    override fun generateTokens(subject: String): TokenResponse {
        // access_token 발급
        val access = generateAccessToken()

        // refresh_token 발급
        val refresh = generateRefreshToken()

        // access_token cache에 저장
        // 만약 이전에 cache에 저장된 토큰이 있다 해도 id(subject)가 같으므로 update 쿼리가 나감
        accessTokenRepository.save(
            AccessTokenRedisEntity(
                subject = subject,
                accessToken = access,
                ttl = jwtProperties.accessExpiredExp
            )
        )

        // refresh_token cache에 저장
        // 만약 이전에 cache에 저장된 토큰이 있다 해도 id(subject)가 같으므로 update 쿼리가 나감
        refreshTokenRepository.save(
            RefreshTokenRedisEntity(
                subject = subject,
                rfToken = refresh,
                ttl = jwtProperties.refreshExpiredExp
            )
        )

        // tokens dto에 담아 반환
        return TokenResponse(
            access,
            refresh
        )
    }

    // access_token 발급
    private fun generateAccessToken(): String {
        // 현재 시각
        val now = Date()

        // 토큰 발급 및 반환
        return Jwts.builder()
            .setHeaderParam("role", "access")
            .setIssuedAt(now)
            .setExpiration(Date(now.time.plus(jwtProperties.accessExpiredExp))) // exp 설정
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    // refresh_token 발급
    private fun generateRefreshToken(): String {
        // 현재 시각
        val now = Date()

        // 토큰 발급 및 반환
        return Jwts.builder()
            .setHeaderParam("role", "refresh")
            .setIssuedAt(now)
            .setExpiration(Date(now.time.plus(jwtProperties.refreshExpiredExp))) // exp 설정
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    // 토큰 재발급
    override fun reissue(refreshToken: String): TokenResponse {
        // refresh_token을 redis에서 불러오기
        val rfToken = refreshTokenRepository.findByRfToken(refreshToken) ?: throw InvalidTokenException

        // 토큰 재발급 및 반환
        return generateTokens(rfToken.subject)
    }

    // 토큰의 subject값으로 Authentication얻는 함수
    fun getAuthentication(subject: String): Authentication {
        // CustomUserDetails로 갑싼 user 불러오기
        val authDetails = customUserDetailService.loadUserByUsername(subject) as CustomUserDetails

        // Authentication발급
        return UsernamePasswordAuthenticationToken(authDetails, null, authDetails.authorities)
    }

    // Apple id_token parsing 함수
    override fun getJwtBody(token: String, publicKey: PublicKey): Claims =
        try {
            // body 불러오기
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).body
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                else -> throw InvalidTokenException
            }
        }
}
