package com.sparta.junglespring.common.authority

import com.sparta.junglespring.domain.CustomUser
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 60 * 12


@Component
class JwtTokenProvider {

    // JWT 서명에 사용될 비밀키 (application.properties 등에서 주입)
    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    // 서명에 사용할 키를 지연 초기화 (lazy)
    private val key by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    }
    /**
     * token 생성
     */
    /**
     * JWT 토큰 생성 메서드
     *
     * 예시:
     * val tokenProvider = JwtTokenProvider()
     * val token = tokenProvider.createToken(authentication)
     */

    // 토큰은 payload, signature, header 세 가지로 나뉘어진다.
    fun createToken(authentication: Authentication): TokenInfo {
        // 권한 정보 가져오기
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)

        val now = Date()
        val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)

        // Access Token 생성
        val accessToken = Jwts.builder()
            .setSubject(authentication.name)  // 사용자 이름 설정
            .claim("auth", authorities)       // 사용자 권한 정보 추가
            .claim("userId", (authentication.principal as CustomUser).userId)       // 사용자 권한 정보 추가
            .setIssuedAt(now)                 // 토큰 발급 시간
            .setExpiration(accessExpiration)  // 토큰 만료 시간
            .signWith(key, SignatureAlgorithm.HS256)  // HS256 알고리즘으로 서명
            .compact()

        // Bearer 타입으로 반환
        return TokenInfo("Bearer", accessToken)
    }
    /**
     * token 정보 추출
     */

    /**
     * JWT 토큰에서 인증 정보 추출 메서드
     *
     * 예시:
     * val authentication = tokenProvider.getAuthentication(token)
     */
    fun getAuthentication(token: String): Authentication {
        val claims: Claims = getClaims(token)

        // 토큰에서 권한 정보 가져오기
        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰 입니다.")
        val userId = claims["userId"] ?: throw RuntimeException("잘못된 토큰 입니다.")

        // 문자열로 된 권한 목록을 GrantedAuthority 객체로 변환
        val authorities: Collection<GrantedAuthority> = (auth as String)
            .split(",")
            .map { SimpleGrantedAuthority(it) }

        // UserDetails 객체 생성 (username과 권한 정보를 포함)
        val principal: UserDetails = CustomUser(userId.toString().toLong(), claims.subject, "", authorities)

        // UsernamePasswordAuthenticationToken 반환 (인증 객체)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }


    /**
     * Token 검증
     */
    /**
     * JWT 토큰 검증 메서드
     *
     * 예시:
     * val isValid = tokenProvider.validateToken(token)
     */
    fun validateToken(token: String): Boolean {
        try {
            getClaims(token)  // 토큰을 파싱하여 유효성 검증
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> println("잘못된 JWT 서명입니다.")  // 서명 오류
                is MalformedJwtException -> println("잘못된 JWT 토큰입니다.")  // 구조 오류
                is ExpiredJwtException -> println("만료된 JWT 토큰입니다.")  // 만료된 토큰
                is UnsupportedJwtException -> println("지원되지 않는 JWT 토큰입니다.")  // 지원되지 않음
                is IllegalArgumentException -> println("JWT claims 문자열이 비어 있습니다.")  // 빈 claims
                else -> println("기타 오류: ${e.message}")  // 기타 예외
            }
            println(e.message)
        }
        return false
    }


    /**
     * JWT 토큰에서 클레임 추출 메서드
     *
     * 예시:
     * val claims = tokenProvider.getClaims(token)
     */
    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)  // 서명 검증을 위한 키 설정
            .build()
            .parseClaimsJws(token)  // JWT 파싱 및 유효한 경우 클레임 반환
            .body
}