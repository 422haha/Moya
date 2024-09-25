package com.ssafy.ar.manager

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.ssafy.ar.data.QuestData
import com.ssafy.ar.dummy.scriptNode
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val kMaxModelInstances = 1

class ARNodeManager(
    private val engine: Engine,
    private val modelLoader: ModelLoader,
    private val materialLoader: MaterialLoader,
) {
    private val mutex = Mutex()

    // 평면에 노드 배치
    suspend fun placeNode(
        anchorId: String,
        frame: Frame?,
        childNodes: SnapshotStateList<Node>,
        onSuccess: () -> Unit,
    ) = mutex.withLock {
        if(childNodes.any { it.name == anchorId }) return@withLock

        frame?.getUpdatedPlanes()
            ?.lastOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
            ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                val anchorNode = createAnchorNode(
                    scriptNode[0],
                    anchor,
                ).apply { name = anchorId }

                childNodes.add(anchorNode)

                delay(5000)

                onSuccess()
            }
    }

    // 특정 위치에 앵커노드 생성
    private fun createAnchorNode(
        node: QuestData,
        anchor: Anchor
    ): AnchorNode {
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

        val imageNode = ImageNode(
            materialLoader = materialLoader,
            imageFileLocation = "picture/wait.png",
            size = Size(0.35f, 0.35f),
            center = Position(0f, 0.65f, 0f)
        )

        modelNode.addChildNode(imageNode)

        anchorNode.addChildNode(modelNode)

        return anchorNode
    }

    // 모델노드 업데이트
    fun updateAnchorNode(
        prevNode: Node,
        questId: String,
        questModel: String,
        parentAnchor: AnchorNode,
    ) {
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

            val imageNode = ImageNode(
                materialLoader = materialLoader,
                imageFileLocation = "picture/progress.png",
                size = Size(0.35f, 0.35f),
                center = Position(0f, 0.67f, 0f)
            )

            newModelNode.addChildNode(imageNode)

            parentAnchor.addChildNode(newModelNode)
        }
    }
}

