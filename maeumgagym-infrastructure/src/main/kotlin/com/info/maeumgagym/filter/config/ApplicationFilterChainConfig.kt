package com.info.maeumgagym.filter.config

import com.info.maeumgagym.common.exception.CriticalException
import com.info.maeumgagym.error.filter.ErrorLogResponseFilter
import com.info.maeumgagym.error.filter.ExceptionConvertFilter
import com.info.maeumgagym.error.filter.filterchain.ExceptionChainedFilterChain
import com.info.maeumgagym.error.filter.filterchain.ExceptionChainedFilterChainProxy
import com.info.maeumgagym.error.repository.ExceptionRepository
import com.info.maeumgagym.response.writer.DefaultHttpServletResponseWriter
import com.info.maeumgagym.response.writer.ErrorLogHttpServletResponseWriter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter
import javax.servlet.ServletContext

/**
 * [org.apache.catalina.core.ApplicationFilterChain] 속 Filter들의 삽입과 순서를 설정
 *
 * @author Daybreak312
 * @since 27-02-2024
 */
@Configuration
class ApplicationFilterChainConfig(
    private val servletContext: ServletContext,
    private val defaultHttpServletResponseWriter: DefaultHttpServletResponseWriter,
    private val errorLogHttpServletResponseWriter: ErrorLogHttpServletResponseWriter,
    private val exceptionRepository: ExceptionRepository
) {

    /**
     * FilterChain에 등록할 [Filter]를 명시해두는 list.
     *
     * 명시된 순서에 따라 [Filter]를 등록할 것.
     *
     * [getFilterOrder] 메소드를 이용해 정수형 순서 정보를 얻을 수 있음
     */
    private val filtersOrderList = listOf<Class<out Filter>>(
        ExceptionChainedFilterChainProxy::class.java
    )

    //@Bean
    fun exceptionChainedFilterChainProxyConfig(): FilterRegistrationBean<ExceptionChainedFilterChainProxy> {
        val filterChain = ExceptionChainedFilterChain(
            mapOf(
                Pair(
                    ErrorLogResponseFilter::class.simpleName!!,
                    ErrorLogResponseFilter(
                        defaultHttpServletResponseWriter,
                        errorLogHttpServletResponseWriter,
                        exceptionRepository
                    )
                ),
                Pair(
                    ExceptionConvertFilter::class.simpleName!!,
                    ExceptionConvertFilter()
                )
            )
        )

        val bean = FilterRegistrationBean(
            ExceptionChainedFilterChainProxy(filterChain)
        )

        bean.addUrlPatterns("/*")
        bean.order = getFilterOrder(ExceptionChainedFilterChainProxy::class.java)

        return bean
    }

    /**
     * [filtersOrderList]에 등록된 [Filter] 순서의 정수형 정보 반환.
     *
     * 만약 순서 정보를 얻으려는 [Filter]가 [filtersOrderList]에 등록되어 있지 않을 경우 [CriticalException] 발생.
     */
    private fun getFilterOrder(`class`: Class<out Filter>): Int {
        val index = this.filtersOrderList.indexOf(`class`)

        if (index == -1) {
            throw CriticalException(500, "Attempted registration of Unknown filter")
        }

        return index
    }
}
