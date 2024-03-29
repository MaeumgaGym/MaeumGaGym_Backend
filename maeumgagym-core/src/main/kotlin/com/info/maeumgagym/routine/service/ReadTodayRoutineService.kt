package com.info.maeumgagym.routine.service

import com.info.common.ReadOnlyUseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.routine.dto.response.RoutineResponse
import com.info.maeumgagym.routine.port.`in`.ReadTodayRoutineUseCase
import com.info.maeumgagym.routine.port.out.ReadRoutinePort
import java.time.LocalDate

@ReadOnlyUseCase
class ReadTodayRoutineService(
    private val readRoutinePort: ReadRoutinePort,
    private val readCurrentUserPort: ReadCurrentUserPort
) : ReadTodayRoutineUseCase {

    override fun readTodayRoutine(): RoutineResponse? =
        readRoutinePort.readByUserIdAndDayOfWeekAndIsArchivedFalse(
            readCurrentUserPort.readCurrentUser().id!!,
            LocalDate.now().dayOfWeek
        )?.run { toResponse() }
}
