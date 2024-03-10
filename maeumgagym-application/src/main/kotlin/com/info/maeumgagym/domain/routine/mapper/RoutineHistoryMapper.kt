package com.info.maeumgagym.domain.routine.mapper

import com.info.maeumgagym.domain.routine.entity.ExerciseInfo
import com.info.maeumgagym.domain.routine.entity.RoutineHistoryJpaEntity
import com.info.maeumgagym.domain.routine.entity.RoutineJpaEntity
import com.info.maeumgagym.domain.routine.entity.RoutineStatus
import com.info.maeumgagym.routine.model.ExerciseInfoModel
import com.info.maeumgagym.routine.model.Routine
import com.info.maeumgagym.routine.model.RoutineHistory
import com.info.maeumgagym.routine.model.RoutineStatusModel
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class RoutineHistoryMapper {
    fun toEntity(model: RoutineHistory): RoutineHistoryJpaEntity = model.run {
        RoutineHistoryJpaEntity(
            id = id,
            exerciseInfoList = toExerciseInfoList(exerciseInfoList),
            userId = userId,
            date = date
        )
    }

    fun toDomain(entity: RoutineHistoryJpaEntity): RoutineHistory = entity.run {
        RoutineHistory(
            id = id,
            exerciseInfoList = toExerciseInfoModelList(exerciseInfoList),
            userId = userId,
            date = date
        )
    }

    private fun toExerciseInfoList(exerciseInfoList: MutableList<ExerciseInfoModel>) =
        exerciseInfoList.map {
            ExerciseInfo(
                exerciseName = it.exerciseName,
                repetitions = it.repetitions,
                sets = it.sets
            )
        }.toMutableList()

    private fun toExerciseInfoModelList(exerciseInfoList: MutableList<ExerciseInfo>) =
        exerciseInfoList.map {
            ExerciseInfoModel(
                exerciseName = it.exerciseName,
                repetitions = it.repetitions,
                sets = it.sets
            )
        }.toMutableList()
}