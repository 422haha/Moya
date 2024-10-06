package com.ssafy.main.login

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.UploadRepository
import com.ssafy.ui.login.LoginScreenState
import com.ssafy.ui.login.LoginUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val uploadRepository: UploadRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Loading)
    val state: StateFlow<LoginScreenState> = _state

    fun onIntent(intent: LoginUserIntent) {

    }

    fun upload(context: Context, uri: Uri){
        getFileFromUri(context, uri)?.let {
            viewModelScope.launch {
                uploadRepository.upload("test_image.jpg", it).collectLatest { response ->
                    when(response){
                        is ApiResponse.Error -> {
                            Log.d("TAG", "upload: ${response.errorMessage}")
                        }
                        is ApiResponse.Success -> {
                            Log.d("TAG", "upload: ${response.body}")
                        }
                    }
                }
            }
        }
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val fileName = getFileName(context, uri) ?: return null
        val cacheDir = context.cacheDir
        val file = File(cacheDir,fileName)

        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getFileName(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return it.getString(displayNameIndex)
                }
            }
        }
        return null
    }
}