# DAuth - 도담도담 OAuth 2.0 서비스

도담도담 계정을 사용하여 서드파티 애플리케이션에 인증을 제공하는 OAuth 2.0 서버입니다.

## 목차

- [시작하기](#시작하기)
- [OAuth 2.0 플로우](#oauth-20-플로우)
- [Scope](#scope)
- [API 레퍼런스](#api-레퍼런스)
- [클라이언트 구현 가이드](#클라이언트-구현-가이드)
  - [Spring Boot Starter (권장)](#spring-boot-starter-권장)
  - [웹 프론트엔드 (React)](#웹-프론트엔드-react)
  - [백엔드 서버 (Spring Boot)](#백엔드-서버-spring-boot)
  - [백엔드 서버 (Node.js)](#백엔드-서버-nodejs)

---

## 시작하기

### 1. 애플리케이션 등록

DAuth 개발자 콘솔에서 애플리케이션을 등록하면 다음 정보를 받습니다:

- **Client ID**: 애플리케이션 식별자 (공개)
- **Client Secret**: 토큰 발급 시 사용 (비공개, 서버에서만 사용)
- **Redirect URL**: OAuth 인증 후 리다이렉트될 URL

### 2. 환경 설정

```
DAUTH_CLIENT_ID=your-client-id
DAUTH_CLIENT_SECRET=your-client-secret
DAUTH_REDIRECT_URL=https://your-app.com/auth/callback
DAUTH_BASE_URL=https://dauth.b1nd.com
```

---

## OAuth 2.0 플로우

### Authorization Code Flow

```
┌──────────┐                              ┌──────────┐                              ┌──────────┐
│  사용자   │                              │ Your App │                              │  DAuth   │
└────┬─────┘                              └────┬─────┘                              └────┬─────┘
     │                                         │                                         │
     │ 1. 로그인 버튼 클릭                       │                                         │
     │────────────────────────────────────────>│                                         │
     │                                         │                                         │
     │                                         │ 2. DAuth 로그인 페이지로 리다이렉트         │
     │<─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─│                                         │
     │                                         │                                         │
     │ 3. 도담도담 계정으로 로그인                │                                         │
     │────────────────────────────────────────────────────────────────────────────────>│
     │                                         │                                         │
     │                                         │ 4. Authorization Code와 함께 리다이렉트   │
     │<─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─│
     │                                         │                                         │
     │ 5. Code를 서버로 전달                     │                                         │
     │────────────────────────────────────────>│                                         │
     │                                         │                                         │
     │                                         │ 6. Code + Client Secret으로 토큰 요청     │
     │                                         │────────────────────────────────────────>│
     │                                         │                                         │
     │                                         │ 7. Access Token, Refresh Token 반환      │
     │                                         │<────────────────────────────────────────│
     │                                         │                                         │
     │ 8. 로그인 완료                            │                                         │
     │<────────────────────────────────────────│                                         │
     │                                         │                                         │
```

---

## Scope

사용자 데이터 접근 범위를 지정합니다.

| Scope | 설명 | 제공 데이터 |
|-------|------|------------|
| `openid` | 기본 식별 정보 | sub, name |
| `phone` | 전화번호 | phone |
| `read:profile` | 프로필 정보 | name, email, profileImage, role |
| `read:dormitory` | 기숙사 정보 | (준비중) |
| `read:outsleep` | 외박 정보 | (준비중) |
| `read:club` | 동아리 정보 | (준비중) |

---

## API 레퍼런스

### Base URL

- Production: `https://dauth.b1nd.com`
- Local: `http://localhost:8003`

### 인증 API

#### ID/PW 로그인

```http
POST /auth/id-login
Content-Type: application/json

{
  "id": "도담도담_아이디",
  "password": "비밀번호",
  "clientId": "your-client-id",
  "redirectUrl": "https://your-app.com/callback",
  "scopes": ["openid", "read:profile"]
}
```

**응답:**
```json
{
  "status": 200,
  "message": "id로 로그인 성공",
  "data": {
    "code": "authorization-code-here",
    "redirectUrl": "https://your-app.com/callback"
  }
}
```

#### QR 로그인 세션 생성

```http
POST /auth/qr
Content-Type: application/json

{
  "scopes": ["openid", "read:profile"]
}
```

**응답:**
```json
{
  "status": 201,
  "message": "qr용 코드 생성 성공",
  "data": {
    "code": "qr-session-code"
  }
}
```

#### QR 로그인 처리 (앱에서 호출)

```http
POST /auth/qr-login
Content-Type: application/json

{
  "code": "qr-session-code",
  "access": "도담도담-access-token",
  "refresh": "도담도담-refresh-token",
  "clientId": "your-client-id"
}
```

#### QR 로그인 확인 (폴링)

```http
POST /auth/qr/check
Content-Type: application/json

{
  "code": "qr-session-code",
  "redirectUrl": "https://your-app.com/callback"
}
```

### 토큰 API

#### 토큰 발급

```http
POST /oauth/token
Content-Type: application/json

{
  "code": "authorization-code-here",
  "clientSecret": "your-client-secret"
}
```

**응답:**
```json
{
  "status": 200,
  "message": "토큰 발급 성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "idToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer"
  }
}
```

#### 토큰 재발급

```http
POST /oauth/token/reissue
Content-Type: application/json

{
  "refresh": "refresh-token-here",
  "clientId": "your-client-id"
}
```

### 사용자 정보 API

#### 사용자 정보 조회

```http
GET /oauth/userinfo
Authorization: Bearer {access_token}
```

**응답 (scope에 따라 다름):**
```json
{
  "status": 200,
  "message": "사용자 정보 조회 성공",
  "data": {
    "sub": "user-unique-id",
    "name": "홍길동",
    "email": "hong@dsm.hs.kr",
    "profileImage": "https://...",
    "role": "STUDENT",
    "phone": "010-1234-5678"
  }
}
```

---

## 클라이언트 구현 가이드

### Spring Security OAuth2 Client (표준 방식)

Spring Security의 내장 OAuth2 Client를 사용하여 가장 간단하게 DAuth를 연동할 수 있습니다.

#### 1. 의존성 추가

```kotlin
// build.gradle.kts
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
}
```

#### 2. 설정

```yaml
# application.yml
spring:
  security:
    oauth2:
      client:
        registration:
          dauth:
            client-id: your-client-id
            client-secret: your-client-secret
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/dauth"
            scope: openid,profile
        provider:
          dauth:
            issuer-uri: https://dauth.b1nd.com
            # 또는 수동 설정:
            # authorization-uri: https://dauth.b1nd.com/oauth/authorize
            # token-uri: https://dauth.b1nd.com/oauth/token
            # user-info-uri: https://dauth.b1nd.com/userinfo
            # user-name-attribute: sub
```

#### 3. Security 설정

```kotlin
@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/", "/login").permitAll()
                auth.anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2.defaultSuccessUrl("/home", true)
            }
        return http.build()
    }
}
```

#### 4. 사용자 정보 접근

```kotlin
@RestController
class UserController {
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal oauth2User: OAuth2User): Map<String, Any?> {
        return mapOf(
            "sub" to oauth2User.getAttribute<String>("sub"),
            "name" to oauth2User.getAttribute<String>("name"),
            "email" to oauth2User.getAttribute<String>("email"),
            "role" to oauth2User.getAttribute<String>("role")
        )
    }
}
```

#### 표준 엔드포인트

| 용도 | 경로 | 설명 |
|------|------|------|
| OpenID Discovery | `/.well-known/openid-configuration` | Provider 자동 설정 |
| Authorization | `GET /oauth/authorize` | 인가 페이지 |
| Token | `POST /oauth/token` | 토큰 발급 (form-urlencoded) |
| UserInfo | `GET /userinfo` | 사용자 정보 (표준 형식) |

#### 표준 응답 형식

**토큰 응답:**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
  "id_token": "eyJhbGciOiJIUzI1NiIs...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "scope": "openid profile"
}
```

**UserInfo 응답:**
```json
{
  "sub": "user-unique-id",
  "name": "홍길동",
  "email": "hong@dsm.hs.kr",
  "profile_image": "https://...",
  "role": "STUDENT",
  "phone": "010-1234-5678"
}
```
---

### 웹 프론트엔드 (React)

#### 1. 환경 변수 설정

```env
# .env
VITE_DAUTH_CLIENT_ID=your-client-id
VITE_DAUTH_REDIRECT_URL=http://localhost:3000/auth/callback
VITE_DAUTH_BASE_URL=https://dauth.b1nd.com
VITE_API_URL=http://localhost:8080
```

#### 2. DAuth 설정 파일

```typescript
// src/config/dauth.ts
export const DAUTH_CONFIG = {
  clientId: import.meta.env.VITE_DAUTH_CLIENT_ID,
  redirectUrl: import.meta.env.VITE_DAUTH_REDIRECT_URL,
  baseUrl: import.meta.env.VITE_DAUTH_BASE_URL,
  scopes: ['openid', 'read:profile'],
};
```

#### 3. 로그인 페이지 컴포넌트

```tsx
// src/pages/Login/index.tsx
import { useState } from 'react';
import { DAUTH_CONFIG } from '@/config/dauth';

export default function LoginPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch(`${DAUTH_CONFIG.baseUrl}/auth/id-login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id,
          password,
          clientId: DAUTH_CONFIG.clientId,
          redirectUrl: DAUTH_CONFIG.redirectUrl,
          scopes: DAUTH_CONFIG.scopes,
        }),
      });

      const data = await response.json();

      if (data.status === 200) {
        // 백엔드 서버로 code 전달하여 토큰 교환
        window.location.href = `${DAUTH_CONFIG.redirectUrl}?code=${data.data.code}`;
      } else {
        setError('로그인에 실패했습니다.');
      }
    } catch (err) {
      setError('네트워크 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h1>로그인</h1>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="도담도담 아이디"
          value={id}
          onChange={(e) => setId(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? '로그인 중...' : '로그인'}
        </button>
      </form>
    </div>
  );
}
```

#### 4. OAuth 콜백 처리 컴포넌트

```tsx
// src/pages/Auth/Callback.tsx
import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

export default function AuthCallback() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [error, setError] = useState('');

  useEffect(() => {
    const code = searchParams.get('code');

    if (!code) {
      setError('인증 코드가 없습니다.');
      return;
    }

    // 백엔드 서버로 code 전달
    exchangeToken(code);
  }, [searchParams]);

  const exchangeToken = async (code: string) => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/auth/dauth/callback`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ code }),
        credentials: 'include', // 쿠키 사용 시
      });

      const data = await response.json();

      if (response.ok) {
        // 토큰 저장 (localStorage 또는 쿠키)
        localStorage.setItem('accessToken', data.accessToken);
        navigate('/');
      } else {
        setError('토큰 발급에 실패했습니다.');
      }
    } catch (err) {
      setError('네트워크 오류가 발생했습니다.');
    }
  };

  if (error) {
    return <div className="error">{error}</div>;
  }

  return <div>로그인 처리 중...</div>;
}
```

#### 5. QR 로그인 컴포넌트

```tsx
// src/pages/Login/QRLogin.tsx
import { useEffect, useState } from 'react';
import QRCode from 'qrcode.react';
import { DAUTH_CONFIG } from '@/config/dauth';

interface QRSession {
  code: string;
}

export default function QRLoginPage() {
  const [session, setSession] = useState<QRSession | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    createQRSession();
  }, []);

  useEffect(() => {
    if (!session) return;

    // 3초마다 로그인 완료 여부 확인
    const interval = setInterval(() => {
      checkQRLogin();
    }, 3000);

    // 5분 후 세션 만료
    const timeout = setTimeout(() => {
      clearInterval(interval);
      setError('QR 세션이 만료되었습니다.');
    }, 5 * 60 * 1000);

    return () => {
      clearInterval(interval);
      clearTimeout(timeout);
    };
  }, [session]);

  const createQRSession = async () => {
    try {
      const response = await fetch(`${DAUTH_CONFIG.baseUrl}/auth/qr`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          scopes: DAUTH_CONFIG.scopes,
        }),
      });

      const data = await response.json();
      setSession(data.data);
    } catch (err) {
      setError('QR 세션 생성에 실패했습니다.');
    }
  };

  const checkQRLogin = async () => {
    if (!session) return;

    try {
      const response = await fetch(`${DAUTH_CONFIG.baseUrl}/auth/qr/check`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          code: session.code,
          redirectUrl: DAUTH_CONFIG.redirectUrl,
        }),
      });

      const data = await response.json();

      if (data.status === 200) {
        // 로그인 성공, 콜백으로 이동
        window.location.href = `${DAUTH_CONFIG.redirectUrl}?code=${data.data.code}`;
      }
    } catch (err) {
      // 아직 로그인 안됨, 계속 폴링
    }
  };

  if (error) {
    return (
      <div>
        <p>{error}</p>
        <button onClick={createQRSession}>다시 시도</button>
      </div>
    );
  }

  if (!session) {
    return <div>QR 코드 생성 중...</div>;
  }

  // QR 데이터: 앱에서 스캔할 정보
  const qrData = JSON.stringify({
    code: session.code,
    clientId: DAUTH_CONFIG.clientId,
  });

  return (
    <div className="qr-login">
      <h2>QR 코드로 로그인</h2>
      <QRCode value={qrData} size={200} />
      <p>도담도담 앱으로 QR 코드를 스캔하세요</p>
    </div>
  );
}
```

#### 6. API 클라이언트 (Axios 인터셉터)

```typescript
// src/lib/api.ts
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

// 요청 인터셉터: Access Token 추가
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 응답 인터셉터: 토큰 만료 시 재발급
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await axios.post(`${import.meta.env.VITE_API_URL}/auth/refresh`, {
          refreshToken,
        });

        const { accessToken } = response.data;
        localStorage.setItem('accessToken', accessToken);

        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // 리프레시 토큰도 만료됨, 로그아웃 처리
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);

export default api;
```

#### 7. 라우터 설정

```tsx
// src/App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/Login';
import QRLoginPage from './pages/Login/QRLogin';
import AuthCallback from './pages/Auth/Callback';
import HomePage from './pages/Home';
import ProtectedRoute from './components/ProtectedRoute';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/login/qr" element={<QRLoginPage />} />
        <Route path="/auth/callback" element={<AuthCallback />} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}
```

---

### 백엔드 서버 (Spring Boot)

#### 1. 의존성 추가

```kotlin
// build.gradle.kts
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux") // WebClient용
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
}
```

#### 2. 설정 파일

```yaml
# application.yml
dauth:
  client-id: ${DAUTH_CLIENT_ID}
  client-secret: ${DAUTH_CLIENT_SECRET}
  redirect-url: ${DAUTH_REDIRECT_URL}
  base-url: https://dauth.b1nd.com
```

#### 3. DAuth 설정 클래스

```kotlin
// DAuthProperties.kt
@ConfigurationProperties(prefix = "dauth")
data class DAuthProperties(
    val clientId: String,
    val clientSecret: String,
    val redirectUrl: String,
    val baseUrl: String
)
```

#### 4. DAuth 클라이언트

```kotlin
// DAuthClient.kt
@Component
class DAuthClient(
    private val webClient: WebClient,
    private val properties: DAuthProperties
) {
    // 토큰 발급
    suspend fun exchangeToken(code: String): DAuthTokenResponse {
        return webClient.post()
            .uri("${properties.baseUrl}/oauth/token")
            .bodyValue(
                mapOf(
                    "code" to code,
                    "clientSecret" to properties.clientSecret
                )
            )
            .retrieve()
            .awaitBody<DAuthApiResponse<DAuthTokenResponse>>()
            .data
    }

    // 토큰 재발급
    suspend fun refreshToken(refreshToken: String): DAuthRefreshResponse {
        return webClient.post()
            .uri("${properties.baseUrl}/oauth/token/reissue")
            .bodyValue(
                mapOf(
                    "refresh" to refreshToken,
                    "clientId" to properties.clientId
                )
            )
            .retrieve()
            .awaitBody<DAuthApiResponse<DAuthRefreshResponse>>()
            .data
    }

    // 사용자 정보 조회
    suspend fun getUserInfo(accessToken: String): DAuthUserInfo {
        return webClient.get()
            .uri("${properties.baseUrl}/oauth/userinfo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .awaitBody<DAuthApiResponse<DAuthUserInfo>>()
            .data
    }
}
```

#### 5. DTO 클래스

```kotlin
// DAuthDto.kt
data class DAuthApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T
)

data class DAuthTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
    val tokenType: String
)

data class DAuthRefreshResponse(
    val access: String
)

data class DAuthUserInfo(
    val sub: String,
    val name: String?,
    val email: String?,
    val profileImage: String?,
    val role: String?,
    val phone: String?
)
```

#### 6. 인증 서비스

```kotlin
// AuthService.kt
@Service
class AuthService(
    private val dAuthClient: DAuthClient,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {
    // DAuth 콜백 처리
    suspend fun handleDAuthCallback(code: String): TokenResponse {
        // 1. DAuth에서 토큰 교환
        val dAuthTokens = dAuthClient.exchangeToken(code)

        // 2. 사용자 정보 조회
        val userInfo = dAuthClient.getUserInfo(dAuthTokens.accessToken)

        // 3. 사용자 저장/업데이트
        val user = userRepository.findByDodamId(userInfo.sub)
            ?: userRepository.save(
                User(
                    dodamId = userInfo.sub,
                    name = userInfo.name ?: "",
                    email = userInfo.email,
                    role = userInfo.role
                )
            )

        // 4. 자체 JWT 발급 (또는 DAuth 토큰 그대로 사용)
        val accessToken = jwtProvider.generateAccessToken(user)
        val refreshToken = jwtProvider.generateRefreshToken(user)

        // 5. Refresh Token 저장
        user.refreshToken = dAuthTokens.refreshToken // DAuth refresh token 저장
        userRepository.save(user)

        return TokenResponse(accessToken, refreshToken)
    }

    // 토큰 재발급
    suspend fun refreshToken(refreshToken: String): TokenResponse {
        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw UnauthorizedException("Invalid refresh token")

        // DAuth 토큰 재발급
        val newDAuthToken = dAuthClient.refreshToken(user.refreshToken)

        // 자체 토큰 재발급
        val newAccessToken = jwtProvider.generateAccessToken(user)

        return TokenResponse(newAccessToken, refreshToken)
    }
}
```

#### 7. 컨트롤러

```kotlin
// AuthController.kt
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    // DAuth 콜백 처리
    @PostMapping("/dauth/callback")
    suspend fun handleCallback(@RequestBody request: CallbackRequest): TokenResponse {
        return authService.handleDAuthCallback(request.code)
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    suspend fun refresh(@RequestBody request: RefreshRequest): TokenResponse {
        return authService.refreshToken(request.refreshToken)
    }
}

data class CallbackRequest(val code: String)
data class RefreshRequest(val refreshToken: String)
data class TokenResponse(val accessToken: String, val refreshToken: String)
```

#### 8. JWT 필터 (Spring Security)

```kotlin
// JwtAuthenticationFilter.kt
@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)

        if (token != null && jwtProvider.validateToken(token)) {
            val authentication = jwtProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (bearerToken?.startsWith("Bearer ") == true) {
            bearerToken.substring(7)
        } else null
    }
}
```

---

### 백엔드 서버 (Node.js)

#### 1. 패키지 설치

```bash
npm install express axios jsonwebtoken dotenv
```

#### 2. 환경 변수

```env
# .env
DAUTH_CLIENT_ID=your-client-id
DAUTH_CLIENT_SECRET=your-client-secret
DAUTH_REDIRECT_URL=http://localhost:3000/auth/callback
DAUTH_BASE_URL=https://dauth.b1nd.com
JWT_SECRET=your-jwt-secret
```

#### 3. DAuth 클라이언트

```javascript
// src/lib/dauth.js
const axios = require('axios');

const DAUTH_BASE_URL = process.env.DAUTH_BASE_URL;
const CLIENT_ID = process.env.DAUTH_CLIENT_ID;
const CLIENT_SECRET = process.env.DAUTH_CLIENT_SECRET;

const dAuthClient = {
  // 토큰 발급
  async exchangeToken(code) {
    const response = await axios.post(`${DAUTH_BASE_URL}/oauth/token`, {
      code,
      clientSecret: CLIENT_SECRET,
    });
    return response.data.data;
  },

  // 토큰 재발급
  async refreshToken(refreshToken) {
    const response = await axios.post(`${DAUTH_BASE_URL}/oauth/token/reissue`, {
      refresh: refreshToken,
      clientId: CLIENT_ID,
    });
    return response.data.data;
  },

  // 사용자 정보 조회
  async getUserInfo(accessToken) {
    const response = await axios.get(`${DAUTH_BASE_URL}/oauth/userinfo`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data.data;
  },
};

module.exports = dAuthClient;
```

#### 4. 인증 라우터

```javascript
// src/routes/auth.js
const express = require('express');
const jwt = require('jsonwebtoken');
const dAuthClient = require('../lib/dauth');
const User = require('../models/User');

const router = express.Router();

// DAuth 콜백 처리
router.post('/dauth/callback', async (req, res) => {
  try {
    const { code } = req.body;

    // 1. DAuth에서 토큰 교환
    const tokens = await dAuthClient.exchangeToken(code);

    // 2. 사용자 정보 조회
    const userInfo = await dAuthClient.getUserInfo(tokens.accessToken);

    // 3. 사용자 저장/업데이트
    let user = await User.findOne({ dodamId: userInfo.sub });
    if (!user) {
      user = await User.create({
        dodamId: userInfo.sub,
        name: userInfo.name,
        email: userInfo.email,
        role: userInfo.role,
        dAuthRefreshToken: tokens.refreshToken,
      });
    } else {
      user.dAuthRefreshToken = tokens.refreshToken;
      await user.save();
    }

    // 4. 자체 JWT 발급
    const accessToken = jwt.sign(
      { userId: user._id, dodamId: user.dodamId },
      process.env.JWT_SECRET,
      { expiresIn: '1h' }
    );

    const refreshToken = jwt.sign(
      { userId: user._id },
      process.env.JWT_SECRET,
      { expiresIn: '14d' }
    );

    res.json({ accessToken, refreshToken });
  } catch (error) {
    console.error('DAuth callback error:', error);
    res.status(401).json({ message: '인증에 실패했습니다.' });
  }
});

// 토큰 재발급
router.post('/refresh', async (req, res) => {
  try {
    const { refreshToken } = req.body;

    // JWT 검증
    const decoded = jwt.verify(refreshToken, process.env.JWT_SECRET);
    const user = await User.findById(decoded.userId);

    if (!user) {
      return res.status(401).json({ message: '사용자를 찾을 수 없습니다.' });
    }

    // 새 Access Token 발급
    const accessToken = jwt.sign(
      { userId: user._id, dodamId: user.dodamId },
      process.env.JWT_SECRET,
      { expiresIn: '1h' }
    );

    res.json({ accessToken, refreshToken });
  } catch (error) {
    res.status(401).json({ message: '토큰 재발급에 실패했습니다.' });
  }
});

module.exports = router;
```

#### 5. 인증 미들웨어

```javascript
// src/middleware/auth.js
const jwt = require('jsonwebtoken');

const authMiddleware = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ message: '인증이 필요합니다.' });
  }

  const token = authHeader.substring(7);

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(401).json({ message: '유효하지 않은 토큰입니다.' });
  }
};

module.exports = authMiddleware;
```

#### 6. Express 앱 설정

```javascript
// src/app.js
require('dotenv').config();
const express = require('express');
const cors = require('cors');
const authRouter = require('./routes/auth');
const authMiddleware = require('./middleware/auth');

const app = express();

app.use(cors());
app.use(express.json());

// 인증 라우터 (공개)
app.use('/auth', authRouter);

// 보호된 라우터 예시
app.get('/api/me', authMiddleware, async (req, res) => {
  res.json({ userId: req.user.userId, dodamId: req.user.dodamId });
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

---

## 에러 처리

### HTTP 상태 코드

| 코드 | 설명 |
|------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 |
| 401 | 인증 실패 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 500 | 서버 오류 |

### 에러 응답 형식

```json
{
  "status": 401,
  "message": "인증에 실패했습니다.",
  "data": null
}
```

---

## 보안 권장사항

1. **Client Secret 보호**: 절대 프론트엔드 코드에 노출하지 마세요.
2. **HTTPS 사용**: 프로덕션 환경에서는 반드시 HTTPS를 사용하세요.
3. **토큰 저장**: Access Token은 메모리에, Refresh Token은 httpOnly 쿠키에 저장하는 것을 권장합니다.
4. **Redirect URL 검증**: 등록된 Redirect URL만 허용됩니다.
5. **Scope 최소화**: 필요한 scope만 요청하세요.

---

## Swagger 문서

- Production: `https://dauth.b1nd.com/swagger-ui.html`
- Local: `http://localhost:8003/swagger-ui.html`

---

## 지원

문제가 발생하면 [GitHub Issues](https://github.com/Team-B1ND/dauth-server/issues)에 등록해주세요.
