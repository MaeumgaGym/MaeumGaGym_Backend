package com.info.maeumgagym.feign.oauth.google

import com.info.maeumgagym.feign.oauth.google.dto.GoogleInfoFeignResponse
import com.info.maeumgagym.global.config.feign.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "GoogleInfoClient",
    url = "https://www.googleapis.com/oauth2/v3/userinfo",
    configuration = [FeignConfig::class]
)
interface GoogleInfoClient {

    @GetMapping
    fun googleInfo(
        @RequestParam("alt") alt: String,
        @RequestParam("access_token") accessToken: String
    ): GoogleInfoFeignResponse
}
