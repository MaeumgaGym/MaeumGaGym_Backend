package com.info.maeumgagym.error.filter

import com.info.maeumgagym.common.exception.*
import com.info.maeumgagym.error.repository.ExceptionRepository
import com.info.maeumgagym.error.vo.ErrorLog
import com.info.maeumgagym.response.writer.DefaultHttpServletResponseWriter
import com.info.maeumgagym.response.writer.ErrorLogHttpServletResponseWriter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.NestedServletException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * [Exception]이 발생했을 때, 응답 및 로그를 작성
 *
 * [doFilter], 정확히는 [doFilterInternal]를 *try*문으로 감싸 실행.
 * 이후 발생한 모든 예외를 *catch*해 각 예외에 따라 [ErrorLog]및 그에 대한 Response를 작성.
 * @see [ErrorLogHttpServletResponseWriter]
 *
 * 원래 [org.springframework.web.servlet.DispatcherServlet] 통과 이후 발생한 예외는 [NestedServletException.cause]로 감싸져 *throw*되지만, [ExceptionConvertFilter]에서 이를 [MaeumGaGymException]의 하위 타입으로 변환함. 자세한 것은 [ExceptionConvertFilter] 참조.
 *
 * 해당 *Filter*의 순서 설정 정보는 [com.info.maeumgagym.filter.config.ApplicationFilterChainConfig]에 존재
 *
 * @see ExceptionConvertFilter
 *
 * @author Daybreak312
 * @since 22.02.2024
 */
class ErrorLogResponseFilter(
    private val defaultHttpServletResponseWriter: DefaultHttpServletResponseWriter,
    private val errorLogHttpServletResponseWriter: ErrorLogHttpServletResponseWriter,
    private val exceptionRepository: ExceptionRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: MaeumGaGymException) {
            resolveMaeumGaGymException(e, response)
        } catch (e: Exception) {
            resolveUnknownException(e, response)
        } finally {
            resolveExceptionRepositoryException(response)
        }
    }

    private fun resolveExceptionRepositoryException(response: HttpServletResponse) {
        val e = exceptionRepository.get() ?: return

        when (e) {
            is MaeumGaGymException -> resolveMaeumGaGymException(e, response)
            else -> resolveUnknownException(e, response)
        }
    }

    private fun resolveMaeumGaGymException(e: MaeumGaGymException, response: HttpServletResponse) {
        if (isOkStatus(e.status)) {
            defaultHttpServletResponseWriter.doDefaultSettingWithStatusCode(response, e.status)
            return
        }

        val errorLog = printErrorLogAndReturn(e)

        if (isUnknownMaeumGaGymException(e)) {
            e.printStackTrace()
        }

        errorLogHttpServletResponseWriter.writeResponseWithErrorLog(response, errorLog)
    }

    private fun resolveUnknownException(e: Exception, response: HttpServletResponse) {
        val errorLog = printErrorLogAndReturn(e)

        e.printStackTrace()

        errorLogHttpServletResponseWriter.writeResponseWithErrorLog(response, errorLog)
    }

    private fun isOkStatus(status: Int): Boolean =
        status in 200..299

    private fun isUnknownMaeumGaGymException(e: MaeumGaGymException): Boolean =
        e !is BusinessLogicException && e !is SecurityException &&
            e !is FilterException && e !is InterceptorException &&
            e !is AuthenticationException && e !is PresentationValidationException

    private fun printErrorLogAndReturn(e: Exception): ErrorLog {
        ErrorLog.of(e).run {
            logger.info(this.toString())
            return this
        }
    }
}
