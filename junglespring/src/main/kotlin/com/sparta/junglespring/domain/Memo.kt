package com.sparta.junglespring.domain

import com.sparta.junglespring.service.MemoDtoRequest
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
@Table(
    name = "memo",
    indexes = [
        Index(columnList = "title"),
        Index(columnList = "createdBy"),
        Index(columnList = "createdAt"),
    ])
class Memo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, length = 1000)
    var content: String,

    @CreatedBy
    @Column(nullable = false)
    var createdBy: String = ""
): BaseEntity() {
    companion object {
        fun of(title: String, content: String):
                Memo = Memo(
                    title = title,
                    content = content
                )
    }
//
//    protected constructor(requestDto: MemoRequestDto) : this(
//        id = null,
//        username = requestDto.getUsername(),
//        content = requestDto.getContent()
//    )
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Memo) return false
        return id == other.id
    }

    fun update(memoDtoRequest: MemoDtoRequest) {
        this.title = memoDtoRequest.title
        this.content = memoDtoRequest.content
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + createdBy.hashCode()
        return result
    }
}