package com.ssafy.network.repositoryImpl

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.ssafy.model.Upload
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.UploadRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class UploadRepositoryImpl @Inject constructor(
    private val transferUtility: TransferUtility,
) : UploadRepository {
    override suspend fun upload(
        key: String,
        file: File,
        ) : Flow<ApiResponse<Upload>> = flow {

        val uploadObserver =
            transferUtility.upload(key, file, CannedAccessControlList.PublicRead)

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                when(state){
                    TransferState.COMPLETED -> {
                        coroutineScope.launch {
                            emit(ApiResponse.Success(Upload.Completed(key)))
                        }
                    }
                    TransferState.CANCELED -> {
                        coroutineScope.launch {
                            emit(ApiResponse.Error(errorMessage = "Upload canceled"))
                        }
                    }
                    TransferState.FAILED -> {
                        coroutineScope.launch {
                            emit(ApiResponse.Error(errorMessage = "Upload failed"))
                        }
                    }
                    else -> {}
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                coroutineScope.launch {
                    emit(ApiResponse.Success(Upload.InProgress(progress = bytesCurrent / bytesTotal.toFloat())))
                }
            }

            override fun onError(id: Int, ex: Exception) {
                coroutineScope.launch {
                    emit(ApiResponse.Error(errorCode = id, errorMessage = ex.message))
                }
            }
        })
    }
}