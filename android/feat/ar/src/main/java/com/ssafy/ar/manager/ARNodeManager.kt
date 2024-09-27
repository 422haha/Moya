package com.ssafy.ar.manager

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.ssafy.ar.data.QuestData
import com.ssafy.ar.dummy.scriptNode
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

private const val kMaxModelInstances = 1

class ARNodeManager(
    private val engine: Engine,
    private val modelLoader: ModelLoader,
    private val materialLoader: MaterialLoader,
) {
    private val mutex = Mutex()

    // 평면에 노드 배치
    suspend fun placeNode(
        plane: Plane,
        pose: Pose,
        anchorId: String,
        childNodes: SnapshotStateList<Node>,
        onSuccess: () -> Unit,
    ) = mutex.withLock {
        if(childNodes.any { it.name == anchorId }) return@withLock

        val anchorNode = createAnchorNode(
            scriptNode[0],
            plane.createAnchor(pose),
        ).apply { name = anchorId }

        childNodes.add(anchorNode)

        onSuccess()
    }

    private fun createImageNode(stateUrl: String): ImageNode {
        return ImageNode(
            materialLoader = materialLoader,
            imageFileLocation = stateUrl,
            size = Size(0.35f, 0.35f),
            center = Position(0f, 0.67f, 0f)
        )
    }

    // 앵커노드 생성
    private suspend fun createAnchorNode(
        node: QuestData,
        anchor: Anchor
    ): AnchorNode = withContext(Dispatchers.Main) {
        val idx = (1..4).random()

        val anchorNode = AnchorNode(engine = engine, anchor = anchor).apply {
            isPositionEditable = false
            isRotationEditable = false
            isScaleEditable = false
        }

        val modelInstance = modelLoader.createInstancedModel(
            node.model,
            kMaxModelInstances
        ).first()

        val modelNode = ModelNode(
            modelInstance = modelInstance,
            scaleToUnits = 0.5f
        ).apply {
            name = idx.toString()
            rotation = Rotation(0f, 180f, 0f)
        }

        val imageNode = createImageNode("picture/wait.png")

        modelNode.addChildNode(imageNode)

        anchorNode.addChildNode(modelNode)

        anchorNode
    }

    // 앵커노드 업데이트
    suspend fun updateAnchorNode(
        prevNode: Node,
        parentAnchor: AnchorNode,
        questId: String,
        questModel: String,
    ) = withContext(Dispatchers.Main) {
        parentAnchor.removeChildNode(prevNode).apply {
            val modelInstance = modelLoader.createInstancedModel(
                questModel,
                kMaxModelInstances
            ).first()

            val newModelNode = ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 0.5f
            ).apply {
                name = questId
                position = prevNode.worldPosition
                rotation = prevNode.worldRotation
            }

            val imageNode = createImageNode("picture/progress.png",)

            newModelNode.addChildNode(imageNode)

            parentAnchor.addChildNode(newModelNode)
        }
    }

    // 모델노드 업데이트
    fun updateModelNode(
        childNode: ImageNode,
        parentNode: ModelNode,
    ) {
        parentNode.removeChildNode(childNode).apply {
            val imageNode = createImageNode("picture/complete.png",)

            parentNode.addChildNode(imageNode)
        }
    }
}

