package com.sparta.junglespring.service

import com.sparta.junglespring.domain.Memo
import java.io.Serializable
import java.time.LocalDateTime

data class MemoDtoRequest(
    val title: String,
    val content: String,
): Serializable {
    fun toMemo() = Memo(
        title = title,
        content = content
        )
}

data class MemoDtoResponse(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
): Serializable {
    companion object {
        //        fun toResponse(comment: Comment, id: Id): CommentDtoResponse {
        fun toResponse(memo: Memo): MemoDtoResponse {
            return MemoDtoResponse(
                id = memo.id,
                title = memo.title,
                createdBy = memo.createdBy,
                createdAt = memo.createdAt,
//                nickname = user.nickname,
                modifiedAt = memo.modifiedAt)
        }
    }
}
