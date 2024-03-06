package com.info.maeumgagym.daily.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.exception.PermissionDeniedException
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.daily.exception.DailyNotFoundException
import com.info.maeumgagym.daily.port.`in`.DeleteDailyUseCase
import com.info.maeumgagym.daily.port.out.DeleteDailyPort
import com.info.maeumgagym.daily.port.out.DeleteImageObjectPort
import com.info.maeumgagym.daily.port.out.ReadDailyPort
import java.time.LocalDate

@UseCase
internal class DeleteDailyService(
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val deleteImageObjectPort: DeleteImageObjectPort,
    private val readDailyPort: ReadDailyPort,
    private val deleteDailyPort: DeleteDailyPort
) : DeleteDailyUseCase {

    override fun delete(date: LocalDate) {
        val user = readCurrentUserPort.readCurrentUser()

        val daily = readDailyPort.readByUploaderAndDate(user, date) ?: throw DailyNotFoundException

        if (daily.uploader != user) throw PermissionDeniedException

        deleteImageObjectPort.deleteObjects(user.oauthId, date, daily.title)

        deleteDailyPort.delete(daily)
    }
}