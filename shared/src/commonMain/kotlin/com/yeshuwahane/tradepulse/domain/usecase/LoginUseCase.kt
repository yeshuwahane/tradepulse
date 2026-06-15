package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String, role: UserRole): DataResource<User> {
        return userRepository.login(email, password, role)
    }
}
