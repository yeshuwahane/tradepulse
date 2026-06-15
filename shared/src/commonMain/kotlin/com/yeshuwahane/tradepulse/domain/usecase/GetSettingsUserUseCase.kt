package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class GetSettingsUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): User? {
        return userRepository.getSettingsUser()
    }
}
