package com.info.maeumgagym.daily.service

import com.info.common.ReadOnlyUseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.daily.dto.response.DailyListResponse
import com.info.maeumgagym.daily.dto.response.DailyResponse
import com.info.maeumgagym.daily.exception.ThereNoDailiesException
import com.info.maeumgagym.daily.port.`in`.ReadDailyUseCase
import com.info.maeumgagym.daily.port.out.ReadDailyPort

@ReadOnlyUseCase
internal class ReadDailyService(
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val readDailyPort: ReadDailyPort
) : ReadDailyUseCase {

    private companion object {
        const val URL_FORMAT = "%s/%s/daily_exercise_complete/%s/%s/%s"
    }

    override fun readDailies(page: Int, size: Int): DailyListResponse {
        val user = readCurrentUserPort.readCurrentUser()

        val dailies = readDailyPort.readAllByUploader(user, page, size)
            .takeIf {
                it.isNotEmpty()
            }?.map {
                DailyResponse(
                    title = it.title,
                    createAt = it.date.atTime(it.time),
                    url = URL_FORMAT.format(
                        System.getenv("MINIO_END_POINT"),
                        System.getenv("MINIO_BUCKET_NAME"),
                        user.oauthId,
                        it.date,
                        it.title
                    )
                )
            } ?: throw ThereNoDailiesException

        return DailyListResponse(dailies)
    }
}