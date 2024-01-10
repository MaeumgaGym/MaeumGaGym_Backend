package com.info.maeumgagym.pickle.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.pickle.dto.request.PickleCommentRequest
import com.info.maeumgagym.pickle.exception.PickleNotFoundException
import com.info.maeumgagym.pickle.model.PickleComment
import com.info.maeumgagym.pickle.port.`in`.CreatePickleCommentUseCase
import com.info.maeumgagym.pickle.port.out.ExistsPickleByIdPort
import com.info.maeumgagym.pickle.port.out.SavePickleCommentPort

@UseCase
class CreatePickleCommentService(
    private val savePickleCommentPort: SavePickleCommentPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val existsPickleByIdPort: ExistsPickleByIdPort
) : CreatePickleCommentUseCase {
    override fun createPickleComment(pickleCommentRequest: PickleCommentRequest, videoId: String) {
        val user = readCurrentUserPort.readCurrentUser()

        if (!existsPickleByIdPort.existsPickleById(videoId)) {
            throw PickleNotFoundException
        }

        pickleCommentRequest.run {
            savePickleCommentPort.savePickleComment(
                PickleComment(
                    content = content,
                    videoId = videoId,
                    writerId = user.id
                )
            )
        }
    }
}
