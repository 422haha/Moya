package com.ssafy.moya.ai

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.FloatBuffer
import java.util.Collections
import java.util.PriorityQueue
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class DataProcess
    @Inject
    constructor(
        private val context: Context,
    ) {
        private lateinit var classes: Array<String>

        companion object {
            const val BATCH_SIZE = 1
            const val INPUT_SIZE = 640
            const val PIXEL_SIZE = 3
            const val FILE_NAME = "train146.onnx"
            const val LABEL_NAME = "labels.txt"
        }

        private fun bitmapToFloatBuffer(bitmap: Bitmap): FloatBuffer {
            val imageSTD = 255.0f
            val buffer = FloatBuffer.allocate(BATCH_SIZE * PIXEL_SIZE * INPUT_SIZE * INPUT_SIZE)
            buffer.rewind()

            val area = INPUT_SIZE * INPUT_SIZE
            val bitmapData = IntArray(area)
            bitmap.getPixels(
                bitmapData,
                0,
                bitmap.width,
                0,
                0,
                bitmap.width,
                bitmap.height,
            )

            for (i in 0 until INPUT_SIZE - 1) {
                for (j in 0 until INPUT_SIZE - 1) {
                    val idx = INPUT_SIZE * i + j
                    val pixelValue = bitmapData[idx]
                    buffer.put(idx, ((pixelValue shr 16 and 0xff) / imageSTD))
                    buffer.put(idx + area, ((pixelValue shr 8 and 0xff) / imageSTD))
                    buffer.put(idx + area * 2, ((pixelValue and 0xff) / imageSTD))
                }
            }
            buffer.rewind()
            return buffer
        }

        fun loadModel() {
            val assetManager = context.assets
            val outputFile = File(context.filesDir.toString() + "/" + FILE_NAME)

            try {
                assetManager.open(FILE_NAME).use { inputStream ->
                    FileOutputStream(outputFile).use { outputStream ->
                        val buffer = ByteArray(4 * 1024)
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }

        fun loadLabel() {
            BufferedReader(InputStreamReader(context.assets.open(LABEL_NAME))).use { reader ->
                var line: String?
                val classList = ArrayList<String>()
                while (reader.readLine().also { line = it } != null) {
                    classList.add(line!!)
                }
                classes = classList.toTypedArray()
            }
        }

        private fun outputsToNPMSPredictions(outputs: Array<*>): ArrayList<Result> {
            val confidenceThreshold = 0.7f
            val results = ArrayList<Result>()
            val rows: Int
            val cols: Int

            (outputs[0] as Array<*>).also {
                rows = it.size
                cols = (it[0] as FloatArray).size
            }

            val output = Array(cols) { FloatArray(rows) }
            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    output[j][i] = ((((outputs[0]) as Array<*>)[i]) as FloatArray)[j]
                }
            }

            for (i in 0 until cols) {
                var detectionClass: Int = -1
                var maxScore = 0f
                val classArray = FloatArray(classes.size)
                System.arraycopy(output[i], 4, classArray, 0, classes.size)
                for (j in classes.indices) {
                    if (classArray[j] > maxScore) {
                        detectionClass = j
                        maxScore = classArray[j]
                    }
                }

                if (maxScore > confidenceThreshold) {
                    val xPos = output[i][0]
                    val yPos = output[i][1]
                    val width = output[i][2]
                    val height = output[i][3]
                    val rectF =
                        RectF(
                            max(0f, xPos - width / 2f),
                            max(0f, yPos - height / 2f),
                            min(INPUT_SIZE - 1f, xPos + width / 2f),
                            min(INPUT_SIZE - 1f, yPos + height / 2f),
                        )
                    val result = Result(detectionClass, maxScore, rectF)
                    results.add(result)
                }
            }
            return nms(results)
        }

        private fun nms(results: ArrayList<Result>): ArrayList<Result> {
            val list = ArrayList<Result>()

            for (i in classes.indices) {
                val pq =
                    PriorityQueue<Result>(50) { o1, o2 ->
                        o1.score.compareTo(o2.score)
                    }
                val classResults = results.filter { it.classIndex == i }
                pq.addAll(classResults)

                while (pq.isNotEmpty()) {
                    val detections = pq.toTypedArray()
                    val max = detections[0]
                    list.add(max)
                    pq.clear()

                    for (k in 1 until detections.size) {
                        val detection = detections[k]
                        val rectF = detection.rectF
                        val iouThresh = 0.5f
                        if (boxIOU(max.rectF, rectF) < iouThresh) {
                            pq.add(detection)
                        }
                    }
                }
            }
            return list
        }

        private fun boxIOU(
            a: RectF,
            b: RectF,
        ): Float = boxIntersection(a, b) / boxUnion(a, b)

        private fun boxIntersection(
            a: RectF,
            b: RectF,
        ): Float {
            val w =
                overlap(
                    (a.left + a.right) / 2f,
                    a.right - a.left,
                    (b.left + b.right) / 2f,
                    b.right - b.left,
                )
            val h =
                overlap(
                    (a.top + a.bottom) / 2f,
                    a.bottom - a.top,
                    (b.top + b.bottom) / 2f,
                    b.bottom - b.top,
                )

            return if (w < 0 || h < 0) 0f else w * h
        }

        private fun boxUnion(
            a: RectF,
            b: RectF,
        ): Float {
            val i: Float = boxIntersection(a, b)
            return (a.right - a.left) * (a.bottom - a.top) + (b.right - b.left) * (b.bottom - b.top) - i
        }

        private fun overlap(
            x1: Float,
            w1: Float,
            x2: Float,
            w2: Float,
        ): Float {
            val l1 = x1 - w1 / 2
            val l2 = x2 - w2 / 2
            val left = max(l1, l2)
            val r1 = x1 + w1 / 2
            val r2 = x2 + w2 / 2
            val right = min(r1, r2)
            return right - left
        }

        suspend fun processImage(
            bitmap: Bitmap,
            ortEnvironment: OrtEnvironment,
            session: OrtSession,
        ): List<Result> =
            withContext(Dispatchers.IO) {
                try {
                    val floatBuffer = bitmapToFloatBuffer(bitmap)
                    val inputName = session.inputNames.iterator().next()
                    val shape =
                        longArrayOf(
                            BATCH_SIZE.toLong(),
                            PIXEL_SIZE.toLong(),
                            INPUT_SIZE.toLong(),
                            INPUT_SIZE.toLong(),
                        )
                    val inputTensor = OnnxTensor.createTensor(ortEnvironment, floatBuffer, shape)
                    val resultTensor = session.run(Collections.singletonMap(inputName, inputTensor))
                    val outputs = resultTensor[0].value as Array<*>
                    val currentResults = outputsToNPMSPredictions(outputs)

                    return@withContext currentResults
                } catch (e: Exception) {
                    emptyList()
                }
            }
    }
