package com.info.maeumgagym.pose.port.`in`

import com.info.maeumgagym.pose.dto.response.PoseListResponse

interface ReadPoseFromTagUseCase {

    fun readFromTag(tag: String): PoseListResponse
}