package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class RegisterUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, role: UserRole): DataResource<User> {
        return userRepository.register(name, email, password, role)
    }
}
