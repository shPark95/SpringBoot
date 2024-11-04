package com.sparta.junglespring.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement() { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    // 회원가입 요청을 허용
                    .requestMatchers("/api/member/signup", "/api/member/login").anonymous()
                    .requestMatchers("/api/member/**").hasRole("MEMBER")
                    .requestMatchers("/api/memos/new-memo").authenticated()
//                    .requestMatchers("/api/memos/**").hasRole("MEMBER")
                    .anyRequest().permitAll()
            }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
