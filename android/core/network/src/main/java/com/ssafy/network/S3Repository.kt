package com.ssafy.network

import android.content.Context
import com.amazonaws.auth.CognitoCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class S3Repository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun upload(){


        val credentialsProvider =
            CognitoCredentialsProvider(null, IDENTITY_POOL_ID, null, null, Regions.AP_SOUTH_1)

        val transferUtility = TransferUtility.builder()
            .context(context)
            .defaultBucket(BUCKET_NAME)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(credentialsProvider))
            .build()

        val uploadObserver =
            transferUtility.upload(FILE_NAME, ACTUAL_FILE, CannedAccessControlList.PublicRead)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                when(state){
                    TransferState.COMPLETED -> {

                        // Handle a completed upload
                    }
                    TransferState.CANCELED -> {
                        // Handle a canceled upload
                        }
                    TransferState.FAILED -> {
                        // Handle a failed upload
                    }
                    else -> {
                        // Handle an upload in progress
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            }

            override fun onError(id: Int, ex: Exception) {
                // Handle errors
            }
        })
    }
}