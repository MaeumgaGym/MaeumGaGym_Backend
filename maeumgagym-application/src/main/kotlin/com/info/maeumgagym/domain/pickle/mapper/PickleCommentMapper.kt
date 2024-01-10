package com.info.maeumgagym.domain.pickle.mapper

import com.info.maeumgagym.domain.pickle.entity.PickleCommentJpaEntity
import com.info.maeumgagym.pickle.model.PickleComment
import org.springframework.stereotype.Component

@Component
class PickleCommentMapper {
    fun toEntity(pickleComment: PickleComment): PickleCommentJpaEntity {
        return pickleComment.run {
            PickleCommentJpaEntity(
                id = id,
                content = content,
                videoId = videoId,
                writerId = writerId,
                parentComment = pickleComment.parentComment?.let { toEntity(it) }
            )
        }
    }

    fun toDomain(pickleCommentJpaEntity: PickleCommentJpaEntity): PickleComment {
        return pickleCommentJpaEntity.run {
            PickleComment(
                id = id,
                content = content,
                videoId = videoId,
                writerId = writerId,
                parentComment = pickleCommentJpaEntity.parentComment?.let { toDomain(it) }
            )
        }
    }
}

