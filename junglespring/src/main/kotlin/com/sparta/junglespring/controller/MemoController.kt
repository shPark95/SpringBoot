package com.sparta.junglespring.controller

import com.sparta.junglespring.common.BaseResponse
import com.sparta.junglespring.domain.Memo
import com.sparta.junglespring.service.MemberService
import com.sparta.junglespring.service.MemoDtoRequest
import com.sparta.junglespring.service.MemoDtoResponse
import com.sparta.junglespring.service.MemoService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
class MemoController(
    private val memoService: MemoService, // 생성자 주입
    private val memberService: MemberService
) {

    @GetMapping("/")
    fun home(): ModelAndView {
        return ModelAndView("register")
    }

    @PostMapping("/api/memos/new-memo")
    fun createMemo(@RequestBody memoDtoRequest: MemoDtoRequest): BaseResponse<MemoDtoResponse> {
            val response = memoService.createMemo(memoDtoRequest)
            return BaseResponse(data = response)
        }

    @GetMapping("/api/memo")
    fun getMemos() : List<Memo> {
        return memoService.getMemos()
    }

    @GetMapping("/api/memos/{memo-id}")
    fun memo(@PathVariable("memo-id") id:Long) = memoService.getMemo(id)

    @PutMapping("/api/memos/{memo-id}")
    fun updateMemo(@PathVariable("memo-id") id: Long, @RequestBody memoDtoRequest: MemoDtoRequest): Long {
        return memoService.update(id, memoDtoRequest)
    }

    @DeleteMapping("/api/memos/{memo-id}")
    fun deleteMemo(@PathVariable("memo-id") id: Long): Long {
        return memoService.deleteMemo(id)
    }
}