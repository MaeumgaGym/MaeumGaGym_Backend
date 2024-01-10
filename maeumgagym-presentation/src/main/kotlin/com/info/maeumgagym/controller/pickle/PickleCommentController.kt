package com.info.maeumgagym.controller.pickle

import com.info.common.WebAdapter
import com.info.maeumgagym.controller.pickle.dto.PickleCommentWebRequest
import com.info.maeumgagym.pickle.port.`in`.CreatePickleCommentUseCase
import com.info.maeumgagym.pickle.port.`in`.CreatePickleReplyCommentUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Tag(name = "Pickle Comment API")
@Validated
@WebAdapter
@RequestMapping("/pickles/comment")
class PickleCommentController(
    private val createPickleCommentUseCase: CreatePickleCommentUseCase,
    private val createPickleReplyCommentUseCase: CreatePickleReplyCommentUseCase
) {
    @Operation(summary = "피클 댓글 추가 API")
    @PostMapping("/{videoId}")
    fun createPickleComment(
        @RequestBody @Valid
        req: PickleCommentWebRequest,
        @PathVariable
        @NotBlank(message = "videoId는 null일 수 없습니다.")
        @Valid
        videoId: String?
    ) {
        createPickleCommentUseCase.createPickleComment(req.toRequest(), videoId!!)
    }

    @Operation(summary = "피클 대댓글 추가 API")
    @PostMapping("/{videoId}/{parentId}")
    fun createPickleReplyComment(
        @RequestBody @Valid
        req: PickleCommentWebRequest,
        @PathVariable(value = "videoId")
        @NotBlank(message = "videoId는 null일 수 없습니다.")
        @Valid
        videoId: String?,
        @PathVariable(value = "parentId")
        parentId: Long
    ) {
        createPickleReplyCommentUseCase.createPickleReplyComment(req.toRequest(), videoId!!, parentId)
    }
}
