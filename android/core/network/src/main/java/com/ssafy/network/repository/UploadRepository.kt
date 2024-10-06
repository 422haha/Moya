package com.ssafy.network.repository

import com.ssafy.model.Upload
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UploadRepository {
    suspend fun upload(
        key: String,
        file: File,
    ): Flow<ApiResponse<Upload>>
}
