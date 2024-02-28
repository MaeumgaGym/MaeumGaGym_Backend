package com.info.maeumgagym.controller.pose

import com.info.common.WebAdapter
import com.info.maeumgagym.pose.dto.response.PoseDetailResponse
import com.info.maeumgagym.pose.port.`in`.ReadPoseUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid
import javax.validation.constraints.Positive

@Tag(name = "Pose API")
@RequestMapping("/poses")
@WebAdapter
@Validated
class PoseController(
    private val readPoseUseCase: ReadPoseUseCase
) {

    @Operation(summary = "포즈 조회 API")
    @GetMapping("/{id}")
    fun readById(
        @PathVariable("id")
        @Valid
        @Positive(message = "0보다 커야 합니다.")
        id: Long
    ): PoseDetailResponse = readPoseUseCase.detailResponseById(id)
}
