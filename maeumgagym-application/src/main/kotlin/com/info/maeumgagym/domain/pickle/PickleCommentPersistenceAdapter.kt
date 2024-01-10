package com.info.maeumgagym.domain.pickle

import com.info.common.PersistenceAdapter
import com.info.maeumgagym.domain.pickle.mapper.PickleCommentMapper
import com.info.maeumgagym.domain.pickle.repository.PickleCommentRepository
import com.info.maeumgagym.pickle.model.PickleComment
import com.info.maeumgagym.pickle.port.out.ReadPickleCommentPort
import com.info.maeumgagym.pickle.port.out.SavePickleCommentPort
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
class PickleCommentPersistenceAdapter(
    private val pickleCommentRepository: PickleCommentRepository,
    private val pickleCommentMapper: PickleCommentMapper
): SavePickleCommentPort, ReadPickleCommentPort {
    override fun readPickleComment(pickleCommentId: Long): PickleComment? =
        pickleCommentRepository.findByIdOrNull(pickleCommentId)?.let { pickleCommentMapper.toDomain(it) }

    override fun savePickleComment(pickleComment: PickleComment): PickleComment =
        pickleCommentMapper.toDomain(pickleCommentRepository.save(pickleCommentMapper.toEntity(pickleComment)))
}
