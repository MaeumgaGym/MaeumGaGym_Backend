package com.info.maeumgagym.controller.pickle

import com.info.common.WebAdapter
import com.info.maeumgagym.controller.pickle.dto.PickleUploadWebRequest
import com.info.maeumgagym.controller.pickle.dto.PreSignedUploadURLWebRequest
import com.info.maeumgagym.controller.pickle.dto.UpdatePickleWebRequest
import com.info.maeumgagym.pickle.dto.response.PreSignedUploadURLResponse
import com.info.maeumgagym.pickle.dto.response.PickleListResponse
import com.info.maeumgagym.pickle.dto.response.PickleResponse
import com.info.maeumgagym.pickle.port.`in`.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Tag(name = "Pickle API")
@Validated
@WebAdapter
@RequestMapping("/pickles")
class PickleController(
    private val loadRecommendationPicklesUseCase: LoadRecommendationPicklesUseCase,
    private val loadPickleFromIdUseCase: LoadPickleFromIdUseCase,
    private val loadPicklesFromPoseUseCase: LoadPicklesFromPoseUseCase,
    private val pickleUploadUseCase: PickleUploadUseCase,
    private val pickleDeleteUseCase: PickleDeleteUseCase,
    private val updatePickleUseCase: UpdatePickleUseCase,
    private val getPreSignedUploadURLUseCase: GetPreSignedUploadURLUseCase
) {

    @Operation(summary = "추천 피클 전체 조회 API")
    @GetMapping
    fun recommendationPicklesLoad(@RequestParam("idx") index: Int): PickleListResponse =
        loadRecommendationPicklesUseCase.loadRecommendationPickles(index)

    @Operation(summary = "피클 조회 API")
    @GetMapping("/{id}")
    fun pickleLoadFromId(
        @PathVariable(name = "id")
        @Valid
        @NotBlank(message = "null일 수 없습니다")
        @Pattern(regexp = "^[0-9a-fA-F]{8}$", message = "잘못된 id입니다.")
        id: String?
    ): PickleResponse = loadPickleFromIdUseCase.loadPickleFromId(id!!)

    @Operation(summary = "자세 관련 피클 조회 API")
    @GetMapping("/pose/{poseId}")
    fun picklesLoadFromPose(
        @PathVariable(name = "poseId")
        poseId: Long?
    ): PickleListResponse = loadPicklesFromPoseUseCase.loadPicklesFromPose(poseId!!)

    @Operation(summary = "PreSignedUploadURL 조회 API")
    @GetMapping("/url")
    fun getPreSignedUploadURL(
        @RequestBody @Valid
        req: PreSignedUploadURLWebRequest
    ): PreSignedUploadURLResponse = getPreSignedUploadURLUseCase.getPreSignedUploadURL(req.fileType!!)

    @Operation(summary = "피클 업로드 API")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadPickle(
        @RequestBody @Valid
        req: PickleUploadWebRequest
    ) {
        pickleUploadUseCase.uploadPickle(req.toRequest())
    }

    @Operation(summary = "피클 삭제 API")
    @DeleteMapping("/{id}")
    fun deletePickle(
        @PathVariable(name = "id")
        @Valid
        @NotBlank(message = "null일 수 없습니다")
        @Pattern(regexp = "^[0-9a-fA-F]{8}$", message = "잘못된 id입니다.")
        id: String?
    ) {
        pickleDeleteUseCase.deletePickle(id!!)
    }

    @Operation(summary = "피클 수정 API")
    @PutMapping("/{id}")
    fun updatePickle(
        @PathVariable(name = "id")
        @Valid
        @NotBlank(message = "null일 수 없습니다")
        @Pattern(regexp = "^[0-9a-fA-F]{8}$", message = "잘못된 id입니다.")
        id: String?,
        @RequestBody @Valid
        req: UpdatePickleWebRequest
    ) { updatePickleUseCase.updatePickle(id!!, req.toRequest()) }
}
