package com.info.maeumgagym.pickle.port.`in`

import com.info.maeumgagym.pickle.dto.request.PickleCommentRequest

interface CreatePickleCommentUseCase {
    fun createPickleComment(pickleCommentRequest: PickleCommentRequest, videoId: String)
}
