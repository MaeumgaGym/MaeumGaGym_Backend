package com.info.maeumgagym.response.writer

import com.info.maeumgagym.error.vo.ErrorLog
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse

/**
 * [ErrorLog]의 정보로 [HttpServletResponse]를 작성하기 위한 [HttpServletResponseWriter].
 *
 * [DefaultHttpServletResponseWriter]를 내부 변수로 갖고 있어, [setBody]와 [doDefaultSettingWithStatusCode]는 [DefaultHttpServletResponseWriter]를 통해 Proxy 형태로 구현됨.
 *
 * @see DefaultHttpServletResponseWriter
 *
 * @author Daybreak312
 * @since 12-03-2024
 */
abstract class ErrorLogHttpServletResponseWriter : HttpServletResponseWriter {

    /**
     * [DefaultHttpServletResponseWriter.setBody]와 같은 동작을 하지만, 사용하지 않을 것을 권고.
     *
     * @author Daybreak312
     * @since 12-03-2024
     */
    abstract override fun setBody(response: HttpServletResponse, `object`: Any): HttpServletResponse

    /**
     * [DefaultHttpServletResponseWriter.doDefaultSettingWithStatusCode]와 같은 동작을 하지만, 사용하지 않을 것을 권고.
     *
     * @author Daybreak312
     * @since 12-03-2024
     */
    abstract override fun doDefaultSettingWithStatusCode(
        response: HttpServletResponse,
        status: Int
    ): HttpServletResponse

    /**
     * 인자로 받은 [ErrorLog]의 정보를 기반으로 [HttpServletResponse]를 작성.
     *
     * 사용되는 정보는 [ErrorLogResponse]의 Docs 참고.
     *
     * @see [ErrorLogResponse]
     *
     * @author Daybreak312
     * @since 12-03-2024
     */
    abstract fun writeResponseWithErrorLog(
        response: HttpServletResponse,
        errorLog: ErrorLog
    ): HttpServletResponse
}

/**
 * Error의 정보를 담고 있는 Response
 *
 * @param status 상태 코드.
 * @param message 예외 메세지.
 * @param errorLogId 저장된 에러 로그의 Key.
 * @param timestamp timestamp
 * @param map Field Error 등 경우에 따라 Key-Value의 정보가 추가적으로 필요할 경우 사용
 *
 * @author Daybreak312
 * @since 12-03-2024
 */
data class ErrorLogResponse(
    val status: Int,
    val message: String?,
    val errorLogId: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val map: Map<String, String> = mapOf()
) {

    companion object {
        fun of(errorLog: ErrorLog): ErrorLogResponse =
            errorLog.run {
                ErrorLogResponse(
                    status = status,
                    message = message,
                    errorLogId = id,
                    timestamp = timestamp,
                    map = map
                )
            }
    }
}
