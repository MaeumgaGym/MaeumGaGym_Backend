package com.info.maeumgagym.report.service

import com.info.common.UseCase
import com.info.maeumgagym.auth.port.out.ReadCurrentUserPort
import com.info.maeumgagym.pickle.exception.PickleCommentNotFoundException
import com.info.maeumgagym.pickle.exception.PickleNotFoundException
import com.info.maeumgagym.pickle.exception.PickleReplyNotFoundException
import com.info.maeumgagym.pickle.port.out.ReadPickleCommentPort
import com.info.maeumgagym.pickle.port.out.ReadPicklePort
import com.info.maeumgagym.pickle.port.out.ReadPickleReplyPort
import com.info.maeumgagym.report.dto.request.ReportRequest
import com.info.maeumgagym.report.exception.CannotReportOneselfException
import com.info.maeumgagym.report.model.Report
import com.info.maeumgagym.report.model.ReportType
import com.info.maeumgagym.report.port.`in`.ReportPickleCommentUseCase
import com.info.maeumgagym.report.port.`in`.ReportPickleReplyUseCase
import com.info.maeumgagym.report.port.`in`.ReportPickleUseCase
import com.info.maeumgagym.report.port.`in`.ReportUserUseCase
import com.info.maeumgagym.report.port.out.SaveReportPort
import com.info.maeumgagym.user.exception.UserNotFoundException
import com.info.maeumgagym.user.port.out.ReadUserPort

@UseCase
internal class ReportService(
    private val readCurrentUserPort: ReadCurrentUserPort,
    private val saveReportPort: SaveReportPort,
    private val readUserPort: ReadUserPort,
    private val readPicklePort: ReadPicklePort,
    private val readPickleCommentPort: ReadPickleCommentPort,
    private val readPickleReplyPort: ReadPickleReplyPort
) : ReportUserUseCase, ReportPickleUseCase, ReportPickleCommentUseCase, ReportPickleReplyUseCase {

    override fun reportUser(request: ReportRequest, nickname: String) {
        val user = readCurrentUserPort.readCurrentUser()

        if (user.nickname == nickname) {
            throw CannotReportOneselfException
        }

        val reportedUser = readUserPort.readByNickname(nickname) ?: throw UserNotFoundException

        saveReportPort.save(
            Report(
                type = ReportType.USER,
                reason = request.reason,
                reporterId = user.id!!,
                targetId = reportedUser.id!!
            )
        )
    }

    override fun reportPickle(request: ReportRequest, videoId: String) {
        val user = readCurrentUserPort.readCurrentUser()

        val pickle = readPicklePort.readById(videoId) ?: throw PickleNotFoundException

        if (user == pickle.uploader) {
            throw CannotReportOneselfException
        }

        saveReportPort.save(
            Report(
                type = ReportType.PICKLE,
                reason = request.reason,
                reporterId = user.id!!,
                targetId = videoId
            )
        )
    }

    override fun reportPickleComment(request: ReportRequest, id: Long) {
        val user = readCurrentUserPort.readCurrentUser()

        val pickleComment = readPickleCommentPort.readById(id) ?: throw PickleCommentNotFoundException

        if (user == pickleComment.writer) {
            throw CannotReportOneselfException
        }

        saveReportPort.save(
            Report(
                type = ReportType.PICKLE_COMMENT,
                reason = request.reason,
                reporterId = user.id!!,
                targetId = id
            )
        )
    }

    override fun reportPickleReply(request: ReportRequest, id: Long) {
        val user = readCurrentUserPort.readCurrentUser()

        val pickleReply = readPickleReplyPort.readById(id) ?: throw PickleReplyNotFoundException

        if (user == pickleReply.writer) {
            throw CannotReportOneselfException
        }

        saveReportPort.save(
            Report(
                type = ReportType.PICKLE_REPLY,
                reason = request.reason,
                reporterId = user.id!!,
                targetId = id
            )
        )
    }
}
