package com.info.maeumgagym.pickle.service

import com.info.common.ReadOnlyUseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.pickle.dto.response.PreSignedUploadURLResponse
import com.info.maeumgagym.pickle.exception.FileTypeMismatchedException
import com.info.maeumgagym.pickle.model.VideoIdAndUploaderId
import com.info.maeumgagym.pickle.port.`in`.GetPreSignedUploadURLUseCase
import com.info.maeumgagym.pickle.port.out.GetPreSignedVideoUploadURLPort
import com.info.maeumgagym.pickle.port.out.SaveVideoIdAndUploaderIdPort

@ReadOnlyUseCase
internal class GetPreSignedURLService(
    private val getPreSignedVideoUploadURLPort: GetPreSignedVideoUploadURLPort,
    private val saveVideoIdAndUploaderIdPort: SaveVideoIdAndUploaderIdPort,
    private val readCurrentUserPort: ReadCurrentUserPort
) : GetPreSignedUploadURLUseCase {

    private companion object {
        const val QUICKTIME = "video/quicktime"
        const val MP4 = "video/mp4"
    }

    override fun getPreSignedUploadURL(fileType: String): PreSignedUploadURLResponse {
        // WHEN : 확인 되지 않은 파일 타입 -> Exception
        if (fileType != QUICKTIME && fileType != MP4) throw FileTypeMismatchedException

        // WHAT : Feign으로 PreSignedURL 불러오기
        val preSignedUploadURLDto = getPreSignedVideoUploadURLPort.getPreSignedURL(fileType)

        // video id 저장
        saveVideoIdAndUploaderIdPort.save(
            VideoIdAndUploaderId(
                preSignedUploadURLDto.videoId,
                readCurrentUserPort.readCurrentUser().id!!
            )
        )

        return PreSignedUploadURLResponse(preSignedUploadURLDto.url)
    }
}
