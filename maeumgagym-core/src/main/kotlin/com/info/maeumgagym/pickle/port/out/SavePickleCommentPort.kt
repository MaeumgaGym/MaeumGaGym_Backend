package com.info.maeumgagym.pickle.port.out

import com.info.maeumgagym.pickle.model.PickleComment

interface SavePickleCommentPort {
    fun savePickleComment(pickleComment: PickleComment): PickleComment
}
