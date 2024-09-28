package com.ssafy.ar.manager

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.ssafy.ar.data.ModelType
import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.data.getImageUrl
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

private const val kMaxModelInstances = 1

class ARNodeManager {
    private val mutex = Mutex()

    // 평면에 노드 배치
    suspend fun placeNode(
        plane: Plane,
        pose: Pose,
        questInfo: QuestInfo,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        childNodes: SnapshotStateList<Node>,
        onSuccess: () -> Unit,
    ) = mutex.withLock {
        if(childNodes.any { it.name == questInfo.id.toString() }) return@withLock

        val anchorNode = createAnchorNode(
            questInfo.id,
            questInfo.isComplete,
            questInfo.npcId,
            plane.createAnchor(pose),
            engine,
            modelLoader,
            materialLoader
        ).apply { name = questInfo.id.toString() }

        childNodes.add(anchorNode)

        onSuccess()
    }

    private fun createImageNode(state: QuestState, materialLoader: MaterialLoader): ImageNode {
        return ImageNode(
            materialLoader = materialLoader,
            imageFileLocation = state.getImageUrl(),
            size = Size(0.35f, 0.35f),
            center = Position(0f, 0.67f, 0f)
        )
    }

    // 앵커노드 생성
    private suspend fun createAnchorNode(
        id: Long,
        questState: QuestState,
        npcId: Long,
        anchor: Anchor,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader
    ): AnchorNode = withContext(Dispatchers.Main) {
        val stateNpcId = if(questState != QuestState.WAIT) npcId else 0

        val url = ModelType.fromId(stateNpcId).modelUrl

        val imageNode = createImageNode(questState, materialLoader)

        val modelInstance = modelLoader.createInstancedModel(
            url,
            kMaxModelInstances
        ).first()

        val modelNode = ModelNode(
            modelInstance = modelInstance,
            scaleToUnits = 0.5f
        ).apply {
            name = id.toString()
            rotation = Rotation(0f, 180f, 0f)
        }

        val anchorNode = AnchorNode(engine = engine, anchor = anchor).apply {
            isPositionEditable = false
            isRotationEditable = false
            isScaleEditable = false
        }

        modelNode.addChildNode(imageNode)

        anchorNode.addChildNode(modelNode)

        anchorNode
    }

    // 앵커노드 업데이트 (시작전 -> 진행중)
    suspend fun updateAnchorNode(
        id: Long,
        questModel: String,
        childNode: ModelNode,
        parentNode: AnchorNode,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader
    ) = withContext(Dispatchers.Main) {
        parentNode.removeChildNode(childNode).apply {
            val modelInstance = modelLoader.createInstancedModel(
                questModel,
                kMaxModelInstances
            ).first()

            val newModelNode = ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 0.5f
            ).apply {
                name = id.toString()
                position = childNode.worldPosition
                rotation = childNode.worldRotation
            }

            val imageNode = createImageNode(QuestState.PROGRESS, materialLoader)

            newModelNode.addChildNode(imageNode)

            parentNode.addChildNode(newModelNode)
        }
    }

    // 모델노드 업데이트 (진행중 -> 완료)
    fun updateModelNode(
        childNode: ImageNode,
        parentNode: ModelNode,
        materialLoader: MaterialLoader
    ) {
        parentNode.removeChildNode(childNode).apply {
            val imageNode = createImageNode(QuestState.COMPLETE, materialLoader)

            parentNode.addChildNode(imageNode)
        }
    }
}

