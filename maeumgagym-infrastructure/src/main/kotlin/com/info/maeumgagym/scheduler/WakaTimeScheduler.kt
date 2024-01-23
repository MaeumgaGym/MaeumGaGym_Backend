package com.info.maeumgagym.scheduler

import com.info.maeumgagym.domain.user.entity.UserJpaEntity
import com.info.maeumgagym.domain.user.mapper.UserMapper
import com.info.maeumgagym.domain.user.repository.UserNativeRepository
import com.info.maeumgagym.domain.user.repository.UserRepository
import com.info.maeumgagym.wakatime.model.WakaTime
import com.info.maeumgagym.wakatime.port.out.ReadWakaTimeFromUserAndDatePort
import com.info.maeumgagym.wakatime.port.out.SaveWakaTimePort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime


@Transactional
@Service
class WakaTimeScheduler(
    private val userRepository: UserRepository,
    private val userNativeRepository: UserNativeRepository,
    private val userMapper: UserMapper,
    private val saveWakaTimePort: SaveWakaTimePort,
    private val readWakaTimeFromUserAndDatePort: ReadWakaTimeFromUserAndDatePort
) {

    // 매일 0시 0분 0초에 작동
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun restartAllWakaTime() {
        // 이전 날의 날짜, 지금 시간 = 0시 0분 0초
        val yesterday = LocalDate.now().minusDays(1)
        val now = LocalDateTime.now()
        // 추후 사용될 와카타임 시간 저장용 변수
        var seconds: Long

        // 와카타임을 시작하고 종료하지 않은 모든 유저 불러오기
        userNativeRepository.findAllByWakaStartedAtNotNullOnWithdrawalSafe().forEach { u ->

            val user = userMapper.toDomain(u)

            // 와카타임 시작 시간 ~ 지금까지의 초
            seconds = Duration.between(user.wakaStartedAt, now).seconds

            saveWakaTimePort.save(
                // 먼저 생성한 와카타임 있는지 확인
                readWakaTimeFromUserAndDatePort.findByUserIdAndDate(user.id, yesterday)
                    ?.let {
                        // 있으면 waka += seconds
                        WakaTime(
                            user = it.user,
                            waka = it.waka + seconds,
                            date = it.date,
                            id = it.id
                        )
                    } ?: WakaTime(
                    // 없으면 waka = seconds
                    user = user,
                    waka = seconds,
                    date = yesterday
                ))

            if (user.isDeleted) {
                userRepository.save(
                    user.run {
                        UserJpaEntity(
                            nickname = nickname,
                            oauthId = oauthId,
                            roles = roles,
                            profileImage = profileImage,
                            wakaStartedAt = null,
                            isDelete = isDeleted,
                            id = id
                        )
                    }
                )
            } else {
                // 와카타임 재시작
                userRepository.save(
                    user.run {
                        UserJpaEntity(
                            nickname = nickname,
                            oauthId = oauthId,
                            roles = roles,
                            profileImage = profileImage,
                            wakaStartedAt = now,
                            isDelete = isDeleted,
                            id = id
                        )
                    }
                )
            }
        }
    }
}
