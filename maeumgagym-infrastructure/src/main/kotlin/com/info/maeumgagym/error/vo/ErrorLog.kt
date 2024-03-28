package com.info.maeumgagym.error.vo

import com.info.maeumgagym.common.exception.MaeumGaGymException
import com.info.maeumgagym.common.exception.PresentationValidationException
import java.time.LocalDateTime
import java.util.*

data class ErrorLog(
    val id: String = UUID.randomUUID().toString().substring(0 until 7),
    val exceptionClassName: String,
    val errorOccurredClassName: String,
    val status: Int = 500,
    val message: String? = null,
    val map: Map<String, String> = mapOf(),
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun of(e: Exception) =
            when (e) {
                is PresentationValidationException -> e.run {
                    ErrorLog(
                        exceptionClassName = javaClass.name,
                        errorOccurredClassName = stackTrace[2].className + " or " + stackTrace[1].className,
                        status = status,
                        message = message,
                        map = fields
                    )
                }

                is MaeumGaGymException -> e.run {
                    ErrorLog(
                        exceptionClassName = javaClass.name,
                        errorOccurredClassName = stackTrace[2].className + " or " + stackTrace[1].className,
                        status = status,
                        message = message
                    )
                }

                else -> e.run {
                    ErrorLog(
                        exceptionClassName = javaClass.name,
                        errorOccurredClassName = stackTrace[2].className + " or " + stackTrace[1].className,
                        message = message
                    )
                }
            }
    }

    override fun toString() =
        "[$id] $status : \"$message\" in $errorOccurredClassName cause $exceptionClassName"
}
