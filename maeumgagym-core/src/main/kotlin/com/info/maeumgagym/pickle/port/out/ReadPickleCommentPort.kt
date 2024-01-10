package com.info.maeumgagym.pickle.port.out

import com.info.maeumgagym.pickle.model.PickleComment

interface ReadPickleCommentPort {
    fun readPickleComment(pickleCommentId: Long): PickleComment?
}
