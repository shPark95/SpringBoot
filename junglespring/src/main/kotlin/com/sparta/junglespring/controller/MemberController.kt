package com.sparta.junglespring.controller

import com.sparta.junglespring.common.BaseResponse
import com.sparta.junglespring.common.authority.TokenInfo
import com.sparta.junglespring.domain.CustomUser
import com.sparta.junglespring.service.MemberDtoResponse
import com.sparta.junglespring.service.LoginDto
import com.sparta.junglespring.service.MemberService
import com.sparta.junglespring.service.MemberDtoRequest
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/member")
@RestController
class MemberController (
    private val memberService: MemberService
) {
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    /**
    * 로그인
    */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<MemberDtoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/info")
    fun saveMyInfo(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
//        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val userId = memberService.getCurrentUserId()
        memberDtoRequest.id = userId
        val resultMsg: String = memberService.saveMyInfo(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    /**
     * 내 정보 삭제
     */
    @DeleteMapping("/info")
    fun deleteMyInfo(): BaseResponse<MemberDtoResponse> {
        val userId = memberService.getCurrentUserId()
        val response = memberService.deleteMyInfo(userId)
        return BaseResponse(message = response)
    }
}
