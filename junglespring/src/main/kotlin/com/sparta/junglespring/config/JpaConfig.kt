package com.sparta.junglespring.config

import com.sparta.junglespring.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig(
    private val memberService: MemberService,
) {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware {
            Optional.of(memberService.getCurrentUserId().toString() )
        }
    }
}