package com.sparta.junglespring.controller

import com.sparta.junglespring.common.BaseResponse
import com.sparta.junglespring.service.CommentDtoRequest
import com.sparta.junglespring.service.CommentDtoResponse
import com.sparta.junglespring.service.CommentService
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/memos/{memo-id}/comment")
@RestController
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/new_comment")
    fun createComment(
        @PathVariable("memo-id") memoId: Long,
        @RequestBody commentDtoRequest: CommentDtoRequest
    ): BaseResponse<CommentDtoResponse> {
        val response = commentService.createComment(memoId, commentDtoRequest)
        return BaseResponse(data = response)
    }
}