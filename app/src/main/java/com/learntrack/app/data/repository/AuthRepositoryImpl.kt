package com.learntrack.app.data.repository

import com.learntrack.app.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        delay(1000)
        return if (email.equals("fail@test.com", ignoreCase = true)) {
            Result.failure(Exception("Invalid email or password"))
        } else {
            Result.success("fake-token-123")
        }
    }
}