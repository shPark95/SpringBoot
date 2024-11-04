package com.sparta.junglespring.service

import com.sparta.junglespring.common.authority.JwtTokenProvider
import com.sparta.junglespring.common.authority.TokenInfo
import com.sparta.junglespring.common.exception.InvalidInputException
import com.sparta.junglespring.common.status.ROLE
import com.sparta.junglespring.domain.*
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    /**
     * 회원가입
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        var member: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (member != null) {
            throw InvalidInputException("loginId, 이미 등록된 아이디입니다.")
        }

        member = memberDtoRequest.toEntity()
        memberRepository.save(member)

        val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입을 성공했습니다."
    }

    /**
     * 로그인
     */
    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    /**
     * 내 정보 조회
     */
    fun searchMyInfo(id:Long): MemberDtoResponse {
        val member: Member= memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        return member.toDto()
    }

    /**
     * 내 정보 수정
     */
    fun saveMyInfo(memberDtoRequest: MemberDtoRequest): String {
        val member: Member = memberDtoRequest.toEntity()
        memberRepository.save(member)
        return "수정 완료되었습니다."
    }

    /**
     * 내 정보 삭제
     */
    fun deleteMyInfo(id: Long): String {
        // 먼저 관련된 MemberRole을 모두 삭제합니다.
        val member: Member= memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        // MemberRole 삭제
        member.memberRole?.forEach { memberRole ->
            memberRoleRepository.delete(memberRole) // 각각의 MemberRole 삭제
        }
        // 마지막으로 Member 삭제
        memberRepository.delete(member)

        return "삭제 완료되었습니다."
    }

    fun getCurrentUserId(): Long {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        return userId
    }
}
