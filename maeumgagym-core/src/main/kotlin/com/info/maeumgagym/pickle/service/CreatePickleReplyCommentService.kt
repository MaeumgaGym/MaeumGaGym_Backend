package com.info.maeumgagym.pickle.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.pickle.dto.request.PickleCommentRequest
import com.info.maeumgagym.pickle.exception.PickleCommentNotFoundException
import com.info.maeumgagym.pickle.exception.PickleMisMatchException
import com.info.maeumgagym.pickle.exception.PickleNotFoundException
import com.info.maeumgagym.pickle.model.PickleComment
import com.info.maeumgagym.pickle.port.`in`.CreatePickleReplyCommentUseCase
import com.info.maeumgagym.pickle.port.out.ExistsPickleByIdPort
import com.info.maeumgagym.pickle.port.out.ReadPickleCommentPort
import com.info.maeumgagym.pickle.port.out.SavePickleCommentPort

@UseCase
class CreatePickleReplyCommentService(
    private val savePickleCommentPort: SavePickleCommentPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val readPickleCommentPort: ReadPickleCommentPort,
    private val existsPickleByIdPort: ExistsPickleByIdPort
) : CreatePickleReplyCommentUseCase {
    override fun createPickleReplyComment(
        pickleCommentRequest: PickleCommentRequest,
        videoId: String,
        parentId: Long
    ) {
        val user = readCurrentUserPort.readCurrentUser()
        val parentComment = readPickleCommentPort.readPickleComment(parentId) ?: throw PickleCommentNotFoundException

        if (!existsPickleByIdPort.existsPickleById(videoId)) {
            throw PickleNotFoundException
        }

        if (parentComment.videoId != videoId) {
            throw PickleMisMatchException
        }

        pickleCommentRequest.run {
            savePickleCommentPort.savePickleComment(
                PickleComment(
                    content = content,
                    videoId = videoId,
                    writerId = user.id,
                    parentComment = parentComment
                )
            )
        }
    }
}
