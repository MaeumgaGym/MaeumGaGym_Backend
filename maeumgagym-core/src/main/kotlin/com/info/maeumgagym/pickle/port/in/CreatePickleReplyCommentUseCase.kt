package com.info.maeumgagym.pickle.port.`in`

import com.info.maeumgagym.pickle.dto.request.PickleCommentRequest

interface CreatePickleReplyCommentUseCase {
    fun createPickleReplyComment(pickleCommentRequest: PickleCommentRequest, videoId: String, parentId: Long)
}
