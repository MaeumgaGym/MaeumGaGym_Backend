package com.info.maeumgagym.domain.pickle

import com.info.common.PersistenceAdapter
import com.info.maeumgagym.domain.pickle.mapper.PickleMapper
import com.info.maeumgagym.domain.pickle.repository.PickleRepository
import com.info.maeumgagym.pickle.model.Pickle
import com.info.maeumgagym.pickle.port.out.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@PersistenceAdapter
class PicklePersistenceAdapter(
    private val pickleRepository: PickleRepository,
    private val pickleMapper: PickleMapper
) : SavePicklePort, DeletePicklePort, ReadPickleByIdPort, ReadAllPicklesPort, ExistsPickleByIdPort {

    override fun savePickle(pickle: Pickle): Pickle =
        pickleMapper.toDomain(
            pickleRepository.save(pickleMapper.toEntity(pickle))
        )

    override fun deletePickle(pickle: Pickle) {
        pickleRepository.delete(pickleMapper.toEntity(pickle))
    }

    override fun readPickleById(id: String): Pickle? =
        pickleRepository.findByIdOrNull(id)?.let {
            pickleMapper.toDomain(it)
        }

    override fun readAllPickles(): List<Pickle> =
        pickleRepository.findAll().map {
            pickleMapper.toDomain(it)
        }

    override fun existsPickleById(videoId: String): Boolean =
        pickleRepository.existsByVideoId(videoId)
}
