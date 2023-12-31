package com.info.maeumgagym.pickle.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.pickle.dto.request.PickleUploadRequest
import com.info.maeumgagym.pickle.model.Pickle
import com.info.maeumgagym.pickle.port.`in`.PickleUploadUseCase
import com.info.maeumgagym.pickle.port.out.SavePicklePort

@UseCase
class UploadPickleService(
    private val savePicklePort: SavePicklePort,
    private val readCurrentUserPort: ReadCurrentUserPort
) : PickleUploadUseCase {

    override fun uploadPickle(req: PickleUploadRequest) {
        // 토큰으로 유저 찾기
        val user = readCurrentUserPort.readCurrentUser()

        // 피클 저장
        savePicklePort.savePickle(
            Pickle(
                videoId = req.videoId,
                title = req.title,
                description = req.description,
                uploader = user,
                tags = req.tags
            )
        )
    }
}
