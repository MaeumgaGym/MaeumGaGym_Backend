package com.info.maeumgagym.domain.routine.repository

import com.info.maeumgagym.domain.routine.entity.RoutineJpaEntity
import org.springframework.data.repository.Repository
import java.util.*

@org.springframework.stereotype.Repository
interface RoutineRepository : Repository<RoutineJpaEntity, Long> {
    fun save(entity: RoutineJpaEntity): RoutineJpaEntity

    fun findById(id: Long): RoutineJpaEntity?

    fun findAllByUserId(userId: UUID): List<RoutineJpaEntity>

    fun delete(entity: RoutineJpaEntity)
}
