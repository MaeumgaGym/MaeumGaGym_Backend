package com.info.maeumgagym.auth.port.out

import com.info.maeumgagym.auth.dto.response.TokenResponse

interface GenerateJwtPort {

    fun generateTokens(subject: String): TokenResponse
}
