package com.sparta.junglespring.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
@Table(name = "comment")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false, length = 2000)
    var content: String = "",

    @Column(nullable = false)
    var memoId: Long,

//    @Column
//    var parentCommentId: Long,

    @CreatedBy
    @Column(nullable = false)
    var createdBy: String = ""
): BaseEntity()