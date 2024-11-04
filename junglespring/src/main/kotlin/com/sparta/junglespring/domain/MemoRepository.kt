package com.sparta.junglespring.domain

import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<Memo, Long> {
    fun findAllByOrderByModifiedAtDesc(): List<Memo>
}