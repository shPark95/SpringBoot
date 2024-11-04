package com.sparta.junglespring.common.authority

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean


class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean(){

    /**
     * 요청이 필터를 통과할 때 호출되는 메서드.
     * JWT 토큰을 검증하고, 유효하면 인증 정보를 설정합니다.
     *
     * 예시:
     * filter.doFilter(request, response, chain)
     */

    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?){

        // HttpServletRequest로 변환하고, 토큰을 추출합니다.
        val token = resolveToken(request as HttpServletRequest)

        // 토큰이 유효한지 검증하고, 유효하면 인증 객체를 SecurityContext에 저장합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        // 필터 체인의 다음 필터를 호출합니다.
        chain?.doFilter(request, response)
    }


    /**
     * 요청 헤더에서 JWT 토큰을 추출하는 메서드.
     * "Authorization" 헤더의 값에서 "Bearer " 접두사를 제거하고 토큰을 반환합니다.
     *
     * 예시:
     * val token = resolveToken(request)
     */
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (StringUtils.hasText(bearerToken) &&
            bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }

}