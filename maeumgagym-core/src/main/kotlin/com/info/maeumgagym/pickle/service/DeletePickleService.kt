package com.info.maeumgagym.pickle.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.exception.PermissionDeniedException
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.pickle.exception.PickleNotFoundException
import com.info.maeumgagym.pickle.port.`in`.PickleDeleteUseCase
import com.info.maeumgagym.pickle.port.out.DeletePicklePort
import com.info.maeumgagym.pickle.port.out.FeignDeletePicklePort
import com.info.maeumgagym.pickle.port.out.ReadPickleByIdPort

@UseCase
class DeletePickleService(
    private val deletePicklePort: DeletePicklePort,
    private val feignDeletePicklePort: FeignDeletePicklePort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val readPickleByIdPort: ReadPickleByIdPort
) : PickleDeleteUseCase {

    override fun deletePickle(id: String) {
        // 토큰으로 유저 불러오기
        val user = readCurrentUserPort.readCurrentUser()

        // 넘겨 받은 파라미터로 피클 찾기, 없다면 -> 예외처리
        val pickle = readPickleByIdPort.readPickleById(id) ?: throw PickleNotFoundException

        // 업로더가 유저와 일치하는지 확인, 아닐시 -> 권한 에러
        if (pickle.uploader.id != user.id) throw PermissionDeniedException

        // 피클 삭제
        deletePicklePort.deletePickle(pickle)

        // 피클 파일 삭제
        feignDeletePicklePort.deletePickle(pickle.videoId)
    }
}
