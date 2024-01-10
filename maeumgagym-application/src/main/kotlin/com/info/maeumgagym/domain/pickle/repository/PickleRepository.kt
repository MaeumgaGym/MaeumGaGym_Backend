package com.info.maeumgagym.domain.pickle.repository

import com.info.maeumgagym.domain.pickle.entity.PickleJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PickleRepository : JpaRepository<PickleJpaEntity, String> {
    fun existsByVideoId(videoId: String): Boolean
}

