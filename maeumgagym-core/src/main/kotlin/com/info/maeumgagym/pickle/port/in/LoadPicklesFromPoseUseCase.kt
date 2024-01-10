package com.info.maeumgagym.pickle.port.`in`

import com.info.maeumgagym.pickle.dto.response.PickleListResponse

interface LoadPicklesFromPoseUseCase {
    fun loadPicklesFromPose(poseId: Long): PickleListResponse
}
