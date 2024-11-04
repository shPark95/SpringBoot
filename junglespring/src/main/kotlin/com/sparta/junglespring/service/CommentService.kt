package com.sparta.junglespring.service

import com.sparta.junglespring.domain.CommentRepository
import com.sparta.junglespring.domain.MemberRepository
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository
) {
    fun createComment(memoId: Long, commentDtoRequest: CommentDtoRequest): CommentDtoResponse {
        val comment = commentRepository.save(commentDtoRequest.toComment(memoId))
//        val loginId = memberRepository.getCurrentUserId() 닉네임
        return CommentDtoResponse.toResponse(comment)
    }
}