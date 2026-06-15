package com.yeshuwahane.tradepulse.data.model

import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String
)

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
        role = UserRole.valueOf(role.uppercase())
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        email = email,
        password = password,
        role = role.name
    )
}
