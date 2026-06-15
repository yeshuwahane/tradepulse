package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        id: String,
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): DataResource<String> {
        return userRepository.updateUser(id, name, email, password, role)
    }
}
