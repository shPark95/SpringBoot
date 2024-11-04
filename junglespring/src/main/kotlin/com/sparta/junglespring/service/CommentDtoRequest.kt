package com.sparta.junglespring.service

import com.sparta.junglespring.domain.Comment
import com.sparta.junglespring.domain.Member
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDateTime

data class CommentDtoRequest(
    val content: String
): Serializable {
    fun toComment(memoId: Long): Comment {
        return Comment(content = this.content, memoId = memoId)
    }
}

data class CommentDtoResponse(
    val content: String,
    val createdBy: String,
//    val nickname: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
): Serializable {
    companion object {
//        fun toResponse(comment: Comment, id: Id): CommentDtoResponse {
        fun toResponse(comment: Comment): CommentDtoResponse {
            return CommentDtoResponse(
                content = comment.content,
                createdBy = comment.createdBy,
//                nickname = user.nickname,
                createdAt = comment.createdAt,
                modifiedAt = comment.modifiedAt)
        }
    }
}