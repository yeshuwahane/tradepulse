package com.yeshuwahane.tradepulse.domain.repository

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole

interface UserRepository {
    suspend fun getUsers(): DataResource<List<User>>
    suspend fun login(email: String, password: String, role: UserRole): DataResource<User>
    suspend fun register(name: String, email: String, password: String, role: UserRole): DataResource<User>
    suspend fun getSettingsUser(): User?
    suspend fun clearSettingsUser(): Boolean
    suspend fun deleteUser(id: String): DataResource<String>
    suspend fun updateUser(id: String, name: String, email: String, password: String, role: UserRole): DataResource<String>
}
