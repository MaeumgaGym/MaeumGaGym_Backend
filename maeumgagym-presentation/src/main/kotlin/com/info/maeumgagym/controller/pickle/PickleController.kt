package com.info.maeumgagym.controller.pickle

import com.info.common.WebAdapter
import com.info.maeumgagym.pickle.dto.request.PickleUploadRequest
import com.info.maeumgagym.pickle.port.`in`.PickleDeleteUseCase
import com.info.maeumgagym.pickle.port.`in`.PickleUploadUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@WebAdapter
@RequestMapping("/pickle")
class PickleController(
    private val pickleUploadUseCase: PickleUploadUseCase,
    private val pickleDeleteUseCase: PickleDeleteUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadPickle(
        @RequestBody @Valid
        req: PickleUploadRequest
    ) { pickleUploadUseCase.uploadPickle(req) }

    @DeleteMapping("/{id}")
    fun deletePickle(
        @PathVariable(name = "id", required = true)
        @Valid
        @NotNull(message = "null일 수 없습니다")
        id: Long?
    ) { pickleDeleteUseCase.deletePickle(id!!) }
}
