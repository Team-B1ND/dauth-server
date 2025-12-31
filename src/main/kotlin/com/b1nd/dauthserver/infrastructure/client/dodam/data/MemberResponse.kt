package com.b1nd.dauthserver.infrastructure.client.dodam.data

import com.b1nd.dauthserver.domain.user.enumeration.RoleType

data class MemberResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: RoleType,
    val status: ActiveStatus,
    val profileImage: String? = null,
    val phone: String,
    val student: StudentResponse? = null,
    val teacher: TeacherResponse? = null
) {
    enum class ActiveStatus{
        ACTIVE,
        DEACTIVATE
    }
}

data class TeacherResponse(
    val tel: String,
    val position: String
)

data class StudentResponse(
    val id: Int,
    val grade: Int,
    val room: Int,
    val number: Int,
    val code: String
)

data class ClubResponse(
    val name: String,
    val type: String
)