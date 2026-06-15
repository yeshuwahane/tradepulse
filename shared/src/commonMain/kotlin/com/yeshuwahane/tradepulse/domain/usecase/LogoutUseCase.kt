package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Boolean {
        return userRepository.clearSettingsUser()
    }
}
