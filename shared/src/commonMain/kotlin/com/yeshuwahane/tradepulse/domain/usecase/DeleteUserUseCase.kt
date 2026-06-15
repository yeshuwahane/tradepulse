package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String): DataResource<String> {
        return userRepository.deleteUser(id)
    }
}
