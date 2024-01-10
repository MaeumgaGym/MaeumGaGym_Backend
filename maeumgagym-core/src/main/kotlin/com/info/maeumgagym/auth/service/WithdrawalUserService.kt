package com.info.maeumgagym.auth.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.port.`in`.WithdrawalUserUseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.user.model.DeletedAt
import com.info.maeumgagym.user.port.out.DeleteUserPort
import com.info.maeumgagym.user.port.out.SaveDeletedAtPort

@UseCase
class WithdrawalUserService(
    private val deleteUserPort: DeleteUserPort,
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val saveDeleteAtPort: SaveDeletedAtPort
) : WithdrawalUserUseCase {
    // 회원 탈퇴 함수
    override fun withdrawal() {
        // 토큰으로 유저 불러오기
        val user = readCurrentUserPort.readCurrentUser()

        /* 탈퇴 후 토큰이 만료 되기 때문에, 애초에 UserNotFound 발생 -> 필요 X
        * // 이미 탈퇴된 유저시 409
        * if (existsDeletedUserByIdPort.existsByIdInNative(user.oauthId)) throw AlreadyWithdrawalUserException
        */

        // user soft delete
        deleteUserPort.deleteUser(user)

        // 삭제된 유저 생명주기 관리용 테이블 생성
        saveDeleteAtPort.save(DeletedAt(user.id))
    }
}
