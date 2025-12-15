package com.b1nd.dauthserver.infrastructure.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(listOf(
                Server().url("http://localhost:8003").description("Local Server"),
                Server().url("https://dauth.b1nd.com").description("Production Server")
            ))
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("DAuth Access Token")
                    )
            )
    }

    private fun apiInfo(): Info {
        return Info()
            .title("DAuth Server API")
            .description("""
                # DAuth OAuth 2.0 Server API

                도담도담 OAuth 2.0 인증 서버 API 문서입니다.

                ## 인증 방식

                ### 1. ID/PW 로그인
                - `/auth/id-login` 엔드포인트를 통해 도담도담 계정으로 로그인
                - 로그인 성공 시 `code`가 반환됨
                - `code`를 사용하여 `/oauth/token`에서 Access Token 발급

                ### 2. QR 로그인
                - `/auth/qr`로 QR 세션 생성
                - 앱에서 QR 스캔 후 `/auth/qr-login`으로 로그인
                - `/auth/qr/check`로 로그인 완료 확인 후 `code` 획득

                ### 3. 앱 자체 로그인
                - `/auth/app-login`으로 앱에서 직접 로그인

                ## Scope 종류

                | Scope | 설명 | 제공 데이터 |
                |-------|------|------------|
                | openid | 기본 식별 정보 | sub, name |
                | phone | 전화번호 | phone |
                | read:profile | 프로필 정보 | name, email, profileImage, role |
                | read:dormitory | 기숙사 정보 | (준비중) |
                | write:dormitory | 기숙사 정보 수정 | (준비중) |
                | read:outsleep | 외박 정보 | (준비중) |
                | read:club | 동아리 정보 | (준비중) |

                ## OAuth 2.0 Flow

                ```
                1. 클라이언트 → /auth/id-login (로그인)
                2. DAuth → 클라이언트 (code 반환)
                3. 클라이언트 → /oauth/token (code + client_secret)
                4. DAuth → 클라이언트 (access_token, refresh_token, id_token)
                5. 클라이언트 → /oauth/userinfo (access_token)
                6. DAuth → 클라이언트 (사용자 정보)
                ```
            """.trimIndent())
            .version("1.0.0")
            .contact(
                Contact()
                    .name("B1ND Team")
                    .url("https://github.com/Team-B1ND")
            )
    }
}
