package com.info.maeumgagym.domain.routin.mapper

import com.info.maeumgagym.domain.routin.entity.ExerciseInfo
import com.info.maeumgagym.domain.routin.entity.RoutineJpaEntity
import com.info.maeumgagym.domain.routin.entity.RoutineStatus
import com.info.maeumgagym.routine.model.ExerciseInfoModel
import com.info.maeumgagym.routine.model.Routine
import com.info.maeumgagym.routine.model.RoutineStatusModel
import org.springframework.stereotype.Component

@Component
class RoutineMapper {
    fun toEntity(routine: Routine): RoutineJpaEntity = routine.run {
        RoutineJpaEntity(
            id = id,
            routineName = routineName,
            exerciseInfoList = exerciseInfoModelList.map {
                ExerciseInfo(
                    exerciseName = it.exerciseName,
                    repetitions = it.repetitions,
                    sets = it.sets
                )
            }.toMutableList(),
            dayOfWeeks = dayOfWeeks,
            routineStatus = RoutineStatus(
                isArchived = routineStatusModel.isArchived,
                isShared = routineStatusModel.isShared
            )
        )
    }

    fun toDomain(routineJpaEntity: RoutineJpaEntity): Routine = routineJpaEntity.run {
        Routine(
            id = id,
            routineName = routineName,
            exerciseInfoModelList = exerciseInfoList.map {
                ExerciseInfoModel(
                    exerciseName = it.exerciseName,
                    repetitions = it.repetitions,
                    sets = it.sets
                )
            }.toMutableList(),
            dayOfWeeks = dayOfWeeks,
            routineStatusModel = RoutineStatusModel(
                isArchived = routineStatus.isArchived,
                isShared = routineStatus.isShared
            )
        )
    }
}
