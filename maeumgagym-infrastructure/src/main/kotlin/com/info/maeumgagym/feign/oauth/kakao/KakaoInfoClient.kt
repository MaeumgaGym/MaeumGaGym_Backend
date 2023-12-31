package com.info.maeumgagym.feign.oauth.kakao

import com.info.maeumgagym.feign.oauth.kakao.dto.KakaoInfoFeignResponse
import com.info.maeumgagym.global.config.feign.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "KakaoInfoClient",
    url = "https://kapi.kakao.com",
    configuration = [FeignConfig::class]
)
interface KakaoInfoClient {
    @PostMapping("/v2/user/me")
    fun kakaoInfo(@RequestHeader("Authorization") accessToken: String): KakaoInfoFeignResponse
}
