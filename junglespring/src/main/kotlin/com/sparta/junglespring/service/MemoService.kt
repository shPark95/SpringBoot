package com.sparta.junglespring.service

import com.sparta.junglespring.domain.MemberRepository
import com.sparta.junglespring.domain.Memo
import com.sparta.junglespring.domain.MemoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemoService(
    private val memoRepository: MemoRepository,
) {
    @Transactional
    fun createMemo(memoDtoRequest: MemoDtoRequest): MemoDtoResponse {
        val memo: Memo = memoRepository.save(
            Memo.of(
                memoDtoRequest.title,
                memoDtoRequest.content
            ))

        return MemoDtoResponse.toResponse(memo)
    }

    @Transactional(readOnly = true)
    fun getMemo(id: Long): Memo {
        return memoRepository.findById(id).orElseThrow()
    }

    @Transactional(readOnly = true)
    fun getMemos(): List<Memo> {
        return memoRepository.findAllByOrderByModifiedAtDesc()
    }

    @Transactional
    fun update(id: Long, memoDtoRequest: MemoDtoRequest): Long {
        val memo = memoRepository.findById(id).orElseThrow { IllegalArgumentException("아이디가 존재하지 않습니다.") }
        memo.update(memoDtoRequest)
        return memo.id
    }

    @Transactional
    fun deleteMemo(id: Long): Long {
        memoRepository.deleteById(id)
        return id
    }
}