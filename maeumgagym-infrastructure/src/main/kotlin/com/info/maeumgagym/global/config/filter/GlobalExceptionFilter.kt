package com.info.maeumgagym.global.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.info.maeumgagym.common.exception.MaeumGaGymException
import com.info.maeumgagym.common.exception.ErrorCode
import com.info.maeumgagym.global.error.ErrorResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GlobalExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: MaeumGaGymException) {
            writerErrorCode(response, e.errorCode)
        } catch (e: Exception) {
            e.printStackTrace()
            writerErrorCode(response, ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    @Throws(IOException::class)
    private fun writerErrorCode(response: HttpServletResponse, errorCode: ErrorCode) {
        response.apply {
            status = errorCode.status
            characterEncoding = StandardCharsets.UTF_8.name()
            contentType = MediaType.APPLICATION_JSON_VALUE
            writer.write(objectMapper.writeValueAsString(ErrorResponse(errorCode.status, errorCode.message)))
            writer.flush()
        }
    }
}
