package com.sparta.junglespring.domain

import com.sparta.junglespring.common.status.ROLE
import com.sparta.junglespring.service.MemberDtoResponse
import jakarta.persistence.Entity
import jakarta.persistence.*

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["login_id"])]
)
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false, length = 30, updatable = false)
    val loginId: String,

    @Column(nullable = false, length = 100)
    val password: String
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null

    fun toDto(): MemberDtoResponse =
        MemberDtoResponse(id!!, loginId)
}

@Entity
class MemberRole(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_member_id"))
    val member: Member,
)
