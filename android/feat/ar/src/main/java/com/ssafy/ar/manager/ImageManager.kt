package com.ssafy.ar.manager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

fun imageToNV21(image: Image): ByteArray {
    val planes = image.planes
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    // YUV 데이터를 하나의 배열로 합침
    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    return nv21
}

// Bitmap으로 변환
suspend fun imageToBitmap(image: Image): Bitmap =
    withContext(Dispatchers.Default) {
        val nv21 = imageToNV21(image)

        // NV21 포맷을 Bitmap으로 변환
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
        val imageBytes = out.toByteArray()

        // JPEG로 변환된 데이터를 Bitmap으로 디코딩
        return@withContext BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

// 내부 저장소에 이미지 저장
suspend fun saveImageToInternalStorage(
    image: Image,
    file: File,
): Boolean =
    withContext(Dispatchers.IO) {
        val nv21 = imageToNV21(image)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val outStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, outStream)
        val imageBytes = outStream.toByteArray()

        try {
            FileOutputStream(file).use { output ->
                output.write(imageBytes)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            image.close()
        }
    }

fun generateUniqueFileName(): String {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    return "captured_image_$timeStamp.jpg"
}